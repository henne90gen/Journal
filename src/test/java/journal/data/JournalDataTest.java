package journal.data;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JournalDataTest {

	@Test
	public void testSaveNewEntry() {
		JournalData data = new JournalData();
		LocalDate date = LocalDate.of(2019, 1, 2);
		Entry entry = new Entry(date, Entry.Mood.Awesome, "Test");
		data.save(entry);

		assertEquals(1, data.getNextID());

		List<Entry> entries = data.getAllEntries();
		assertEquals(1, entries.size());
		Entry actualEntry = entries.get(0);
		assertEntry(actualEntry, LocalDate.of(2019, 1, 2), 0, Entry.Mood.Awesome, "Test");
	}

	@Test
	public void testSaveExistingEntry() {
		JournalData data = new JournalData();
		LocalDate date = LocalDate.of(2019, 1, 2);
		Entry entry = new Entry(date, Entry.Mood.Awesome, "Test");
		data.save(entry);

		entry.comment = "New Test";
		entry.mood = Entry.Mood.Good;
		entry.date = LocalDate.of(2019, 1, 1);
		data.save(entry);

		assertEquals(1, data.getNextID());

		List<Entry> entries = data.getAllEntries();
		assertEquals(1, entries.size());
		Entry actualEntry = entries.get(0);
		LocalDate expectedDate = LocalDate.of(2019, 1, 1);
		assertEntry(actualEntry, expectedDate, 0, Entry.Mood.Good, "New Test");
	}

	@Test
	public void testSaveNewEntryWithExistingId() {
		IJournalData data = new JournalData();
		LocalDate date = LocalDate.of(2019, 1, 2);
		Entry entry = new Entry(date, Entry.Mood.Awesome, "Test");
		entry.id = 123;
		data.save(entry);

		List<Entry> entries = data.getAllEntries();
		assertEquals(1, entries.size());
		Entry actualEntry = entries.get(0);
		assertEntry(actualEntry, LocalDate.of(2019, 1, 2), 123, Entry.Mood.Awesome, "Test");
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
		data.setUpdateCallback(counter::increment);

		List<Entry> entries = new ArrayList<>();
		entries.add(new Entry());
		entries.add(new Entry());
		entries.add(new Entry());

		data.saveAll(entries);

		assertEquals(1, counter.getCount());
	}

	@Test
	public void testDeleteEntry() {
		JournalData data = new JournalData();
		List<Entry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 1, 1);
		Entry toBeDeleted = new Entry(date, Entry.Mood.Good, "ToBeDeleted");
		entries.add(new Entry(date, Entry.Mood.Bad, "NotToBeDeleted-1"));
		entries.add(toBeDeleted);
		entries.add(new Entry(date, Entry.Mood.Great, "NotToBeDeleted-2"));
		data.saveAll(entries);

		data.delete(toBeDeleted);

		List<Entry> actualEntries = data.getAllEntries();
		assertEquals(2, actualEntries.size());
		Entry actualEntry1 = actualEntries.get(0);
		assertEntry(actualEntry1, date, 0, Entry.Mood.Bad, "NotToBeDeleted-1");

		Entry actualEntry2 = actualEntries.get(1);
		assertEntry(actualEntry2, date, 2, Entry.Mood.Great, "NotToBeDeleted-2");

		assertEquals(3, data.getNextID());
	}

	@Test
	public void testFindByDateCanFindYear() {
		IJournalData data = new JournalData();
		List<Entry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 1, 1);
		entries.add(new Entry(date, Entry.Mood.Good, "Test-1"));
		entries.add(new Entry(date.plusDays(1), Entry.Mood.Good, "Test-2"));
		entries.add(new Entry(date.plusYears(1), Entry.Mood.Good, "Test-3"));
		data.saveAll(entries);

		List<Entry> actualEntries = data.findByDate(-1, -1, 2019);
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 0, Entry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date.plusDays(1), 1, Entry.Mood.Good, "Test-2");

		actualEntries = data.findByDate(-1, -1, 2020);
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date.plusYears(1), 2, Entry.Mood.Good, "Test-3");
	}

	@Test
	public void testFindByDateCanFindDay() {
		IJournalData data = new JournalData();
		List<Entry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 1, 1);
		entries.add(new Entry(date, Entry.Mood.Good, "Test-1"));
		entries.add(new Entry(date.plusDays(1), Entry.Mood.Good, "Test-2"));
		entries.add(new Entry(date.plusYears(1), Entry.Mood.Good, "Test-3"));
		data.saveAll(entries);

		List<Entry> actualEntries = data.findByDate(1, -1, -1);
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 0, Entry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date.plusYears(1), 2, Entry.Mood.Good, "Test-3");

		actualEntries = data.findByDate(2, -1, -1);
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date.plusDays(1), 1, Entry.Mood.Good, "Test-2");
	}

	@Test
	public void testFindByDateCanFindMonth() {
		IJournalData data = new JournalData();
		List<Entry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 1, 1);
		entries.add(new Entry(date, Entry.Mood.Good, "Test-1"));
		entries.add(new Entry(date.plusMonths(1), Entry.Mood.Good, "Test-2"));
		entries.add(new Entry(date.plusYears(1), Entry.Mood.Good, "Test-3"));
		data.saveAll(entries);

		List<Entry> actualEntries = data.findByDate(-1, 1, -1);
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 0, Entry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date.plusYears(1), 2, Entry.Mood.Good, "Test-3");

		actualEntries = data.findByDate(-1, 2, -1);
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date.plusMonths(1), 1, Entry.Mood.Good, "Test-2");
	}

	@Test
	public void testFindByString() {
		IJournalData data = new JournalData();
		List<Entry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 1, 1);
		entries.add(new Entry(date, Entry.Mood.Good, "Test-1"));
		entries.add(new Entry(date, Entry.Mood.Good, "Test-2"));
		entries.add(new Entry(date, Entry.Mood.Good, "A comment"));
		data.saveAll(entries);

		List<Entry> actualEntries = data.findByString("Test");
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 0, Entry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date, 1, Entry.Mood.Good, "Test-2");

		actualEntries = data.findByString("omm");
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date, 2, Entry.Mood.Good, "A comment");

	}

	private void assertEntry(Entry actualEntry, LocalDate date, int id, Entry.Mood mood, String comment) {
		assertEquals(id, actualEntry.id);
		assertEquals(date, actualEntry.date);
		assertEquals(mood, actualEntry.mood);
		assertEquals(comment, actualEntry.comment);
	}
}
