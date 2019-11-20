package Journal;

import com.google.common.flogger.FluentLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class FileImporter {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private final IJournalData data;

	public FileImporter(IJournalData data) {
		this.data = data;
	}

	void readFromFile(String filePath) {
		LOGGER.atInfo().log("Loading data from %s", filePath);
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				LOGGER.atWarning().log("File %s does not exist", filePath);
				return;
			}

			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				String line = br.readLine();
				while (line != null) {
					LocalDate date = JournalHelper.INSTANCE.getDate(line);
					Entry.Mood mood = JournalHelper.INSTANCE.getMood(br.readLine());
					Entry entry = new Entry(date, mood, br.readLine());
					data.save(entry);
					line = br.readLine();
				}
			}
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Error while loading data from %s", filePath);
		}
		LOGGER.atInfo().log("Loaded data from %s", filePath);
	}
}
