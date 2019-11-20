package journal;

import journal.data.Entry;
import journal.data.FileHandler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileHandlerTest {

	@Test
	public void canSaveAndLoadEntriesOld() throws IOException {
		List<Entry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 2, 1);
		entries.add(new Entry(date, Entry.Mood.Awesome, "Test"));
		entries.add(new Entry(date, Entry.Mood.Good, "Another Test"));

		File file = File.createTempFile("temp", ".txt");
		file.deleteOnExit();

		FileHandler.INSTANCE.writeToFileOld(entries, file);
		List<Entry> loadedEntries = FileHandler.INSTANCE.readFromFileOld(file);
		for (int i = 0; i < entries.size(); i++) {
			assertEquals(entries.get(i).date, loadedEntries.get(i).date);
			assertEquals(entries.get(i).mood, loadedEntries.get(i).mood);
			assertEquals(entries.get(i).comment, loadedEntries.get(i).comment);
		}
	}
}
