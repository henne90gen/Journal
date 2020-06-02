package journal.data;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JournalDataTest {

	@Test
	public void testSaveNewEntry() {
		JournalData data = new JournalData();
		LocalDateTime date = LocalDateTime.of(2019, 1, 2, 8, 0);
		JournalEntry entry = new JournalEntry(date, JournalEntry.Mood.Awesome, "Test");
		data.save(entry);

		assertEquals(1, data.getNextID());

		List<JournalEntry> entries = data.getAllEntries();
		assertEquals(1, entries.size());
		JournalEntry actualEntry = entries.get(0);
		assertEntry(actualEntry, LocalDateTime.of(2019, 1, 2, 8, 0), 0, JournalEntry.Mood.Awesome, "Test");
	}

	@Test
	public void testSaveExistingEntry() {
		JournalData data = new JournalData();
		LocalDateTime date = LocalDateTime.of(2019, 1, 2, 8, 0);
		JournalEntry entry = new JournalEntry(date, JournalEntry.Mood.Awesome, "Test");
		data.save(entry);

		entry.comment = "New Test";
		entry.mood = JournalEntry.Mood.Good;
		entry.date = LocalDateTime.of(2019, 1, 1, 8, 0);
		data.save(entry);

		assertEquals(1, data.getNextID());

		List<JournalEntry> entries = data.getAllEntries();
		assertEquals(1, entries.size());
		JournalEntry actualEntry = entries.get(0);
		LocalDateTime expectedDate = LocalDateTime.of(2019, 1, 1, 8, 0);
		assertEntry(actualEntry, expectedDate, 0, JournalEntry.Mood.Good, "New Test");
	}

	@Test
	public void testSaveNewEntryWithExistingId() {
		IJournalData data = new JournalData();
		LocalDateTime date = LocalDateTime.of(2019, 1, 2, 8, 0);
		JournalEntry entry = new JournalEntry(date, JournalEntry.Mood.Awesome, "Test");
		entry.id = 123;
		data.save(entry);

		List<JournalEntry> entries = data.getAllEntries();
		assertEquals(1, entries.size());
		JournalEntry actualEntry = entries.get(0);
		assertEntry(actualEntry, LocalDateTime.of(2019, 1, 2, 8, 0), 123, JournalEntry.Mood.Awesome, "Test");
	}

	static class Counter {
		private int count = 0;

		void increment() {
			count++;
		}

		int getCount() {
			return count;
		}
	}

	@Test
	public void testSaveAllCallsCallbackOnlyOnce() {
		IJournalData data = new JournalData();
		Counter counter = new Counter();
		data.addUpdateCallback(counter::increment);

		List<JournalEntry> entries = new ArrayList<>();
		entries.add(new JournalEntry());
		entries.add(new JournalEntry());
		entries.add(new JournalEntry());

		data.saveAll(entries);

		assertEquals(1, counter.getCount());
	}

	@Test
	public void testDeleteEntry() {
		JournalData data = new JournalData();
		List<JournalEntry> entries = new ArrayList<>();
		LocalDateTime date = LocalDateTime.of(2019, 1, 1, 8, 0);
		JournalEntry toBeDeleted = new JournalEntry(date, JournalEntry.Mood.Good, "ToBeDeleted");
		entries.add(new JournalEntry(date, JournalEntry.Mood.Bad, "NotToBeDeleted-1"));
		entries.add(toBeDeleted);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Great, "NotToBeDeleted-2"));
		data.saveAll(entries);

		data.delete(toBeDeleted);

		List<JournalEntry> actualEntries = data.getAllEntries();
		assertEquals(2, actualEntries.size());
		JournalEntry actualEntry1 = actualEntries.get(0);
		assertEntry(actualEntry1, date, 0, JournalEntry.Mood.Bad, "NotToBeDeleted-1");

		JournalEntry actualEntry2 = actualEntries.get(1);
		assertEntry(actualEntry2, date, 2, JournalEntry.Mood.Great, "NotToBeDeleted-2");

		assertEquals(3, data.getNextID());
	}

	@Test
	public void testFindByDateCanFindYear() {
		IJournalData data = new JournalData();
		List<JournalEntry> entries = new ArrayList<>();
		LocalDateTime date = LocalDateTime.of(2019, 1, 1, 8, 0);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-1"));
		entries.add(new JournalEntry(date.plusDays(1), JournalEntry.Mood.Good, "Test-2"));
		entries.add(new JournalEntry(date.plusYears(1), JournalEntry.Mood.Good, "Test-3"));
		data.saveAll(entries);

		List<JournalEntry> actualEntries = data.findByDate(-1, -1, 2019);
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 0, JournalEntry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date.plusDays(1), 1, JournalEntry.Mood.Good, "Test-2");

		actualEntries = data.findByDate(-1, -1, 2020);
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date.plusYears(1), 2, JournalEntry.Mood.Good, "Test-3");
	}

	@Test
	public void testFindByDateCanFindDay() {
		IJournalData data = new JournalData();
		List<JournalEntry> entries = new ArrayList<>();
		LocalDateTime date = LocalDateTime.of(2019, 1, 1, 8, 0);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-1"));
		entries.add(new JournalEntry(date.plusDays(1), JournalEntry.Mood.Good, "Test-2"));
		entries.add(new JournalEntry(date.plusYears(1), JournalEntry.Mood.Good, "Test-3"));
		data.saveAll(entries);

		List<JournalEntry> actualEntries = data.findByDate(1, -1, -1);
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 0, JournalEntry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date.plusYears(1), 2, JournalEntry.Mood.Good, "Test-3");

		actualEntries = data.findByDate(2, -1, -1);
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date.plusDays(1), 1, JournalEntry.Mood.Good, "Test-2");
	}

	@Test
	public void testFindByDateCanFindMonth() {
		IJournalData data = new JournalData();
		List<JournalEntry> entries = new ArrayList<>();
		LocalDateTime date = LocalDateTime.of(2019, 1, 1, 8, 0);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-1"));
		entries.add(new JournalEntry(date.plusMonths(1), JournalEntry.Mood.Good, "Test-2"));
		entries.add(new JournalEntry(date.plusYears(1), JournalEntry.Mood.Good, "Test-3"));
		data.saveAll(entries);

		List<JournalEntry> actualEntries = data.findByDate(-1, 1, -1);
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 0, JournalEntry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date.plusYears(1), 2, JournalEntry.Mood.Good, "Test-3");

		actualEntries = data.findByDate(-1, 2, -1);
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date.plusMonths(1), 1, JournalEntry.Mood.Good, "Test-2");
	}

	@Test
	public void testFindByString() {
		IJournalData data = new JournalData();
		List<JournalEntry> entries = new ArrayList<>();
		LocalDateTime date = LocalDateTime.of(2019, 1, 1, 8, 0);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-1"));
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-2"));
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "A comment"));
		data.saveAll(entries);

		List<JournalEntry> actualEntries = data.findByString("Test");
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 0, JournalEntry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date, 1, JournalEntry.Mood.Good, "Test-2");

		actualEntries = data.findByString("omm");
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 2, JournalEntry.Mood.Good, "A comment");

	}

	private void assertEntry(JournalEntry actualEntry, LocalDateTime date, int id, JournalEntry.Mood mood, String comment) {
		assertEquals(id, actualEntry.id);
		assertEquals(date, actualEntry.date);
		assertEquals(mood, actualEntry.mood);
		assertEquals(comment, actualEntry.comment);
	}
}
