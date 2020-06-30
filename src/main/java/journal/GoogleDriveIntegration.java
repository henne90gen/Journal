package journal;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.common.flogger.FluentLogger;
import journal.data.EntryStorage;
import journal.data.FileDataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GoogleDriveIntegration {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private static final String APPLICATION_NAME = "Journal";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_APPDATA);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	private final Drive driveService;

	public GoogleDriveIntegration() {
		driveService = getDriveService();
		if (driveService == null) {
			throw new RuntimeException("Could not initialize Google Drive client.");
		}
	}

	private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
		InputStream in = GoogleDriveIntegration.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.setAccessType("offline")
				.build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	private Drive getDriveService() {
		final NetHttpTransport HTTP_TRANSPORT;
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME)
					.build();
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<File> getAllFiles() throws IOException {
		FileList response = driveService
				.files()
				.list()
				.setSpaces("appDataFolder")
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				.execute();
		List<File> files = response.getFiles();

		if (files == null || files.isEmpty()) {
			return Collections.emptyList();
		}

		List<File> result = new ArrayList<>(files);
		String nextPageToken = response.getNextPageToken();
		while (nextPageToken != null) {
			FileList response2 = driveService.files()
					.list()
					.setSpaces("appDataFolder")
					.setPageSize(10)
					.setFields("nextPageToken, files(id, name)")
					.execute();
			if (response2.getFiles() != null) {
				result.addAll(response2.getFiles());
			}
			nextPageToken = response2.getNextPageToken();
		}

		return result;
	}

	public EntryStorage downloadJournal() throws IOException {
		LOGGER.atInfo().log("Downloading Journal from Google Drive.");
		Optional<String> fileIdOpt = getAllFiles().stream()
				.filter(f -> f.getName().equalsIgnoreCase("journal.json"))
				.map(File::getId)
				.findFirst();
		if (!fileIdOpt.isPresent()) {
			return new EntryStorage();
		}

		InputStream is = driveService.files()
				.get(fileIdOpt.get())
				.executeMediaAsInputStream();
		return FileDataSource.INSTANCE.readFromInputStream(is);
	}

	public void deleteRemoteAndUploadLocalJournal(java.io.File file) throws IOException {
		LOGGER.atInfo().log("Deleting old Journal from Google Drive.");
		getAllFiles().stream()
				.filter(f -> f.getName().equalsIgnoreCase("journal.json"))
				.map(File::getId)
				.forEach(id -> {
					try {
						driveService.files().delete(id);
					} catch (IOException e) {
						// TODO log error correctly
						e.printStackTrace();
					}
				});

		LOGGER.atInfo().log("Uploading new Journal to Google Drive from file: {}", file);

		File uploadFile = new File();
		uploadFile.setName("journal.json");
		uploadFile.setParents(Collections.singletonList("appDataFolder"));

		FileContent mediaContent = new FileContent("application/json", file);
		driveService.files()
				.create(uploadFile, mediaContent)
				.setFields("id")
				.execute();

		LOGGER.atInfo().log("Finished uploading new Journal to Google Drive.");
	}
}
