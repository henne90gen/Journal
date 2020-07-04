package journal.data;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JournalDataTest {

	@Test
	public void testGetAllEntries() {
		JournalData data = new JournalData();
		LocalDate date = LocalDate.of(2019, 1, 2);
		LocalDateTime lastModified = LocalDateTime.of(2019, 1, 2, 9, 0);
		JournalEntry entry = new JournalEntry(date, JournalEntry.Mood.Awesome, "Test", lastModified);
		data.save(entry);

		List<JournalEntry> entries = data.getAllEntries(false);
		assertEquals(1, entries.size());

		data.delete(entry);

		List<JournalEntry> entriesAfterDelete = data.getAllEntries(false);
		assertEquals(0, entriesAfterDelete.size());

		List<JournalEntry> entriesIncludeDeleted = data.getAllEntries(true);
		assertEquals(1, entriesIncludeDeleted.size());
	}

	@Test
	public void testSaveNewEntry() {
		JournalData data = new JournalData();
		LocalDate date = LocalDate.of(2019, 1, 2);
		LocalDateTime lastModified = LocalDateTime.of(2019, 1, 2, 9, 0);
		JournalEntry entry = new JournalEntry(date, JournalEntry.Mood.Awesome, "Test", lastModified);
		data.save(entry);

		List<JournalEntry> entries = data.getAllEntries();
		assertEquals(1, entries.size());
		JournalEntry actualEntry = entries.get(0);
		assertEntry(actualEntry, LocalDate.of(2019, 1, 2), JournalEntry.Mood.Awesome, "Test");
	}

	@Test
	public void testSaveExistingEntry() {
		JournalData data = new JournalData();
		LocalDate date = LocalDate.of(2019, 1, 2);
		LocalDateTime lastModified = LocalDateTime.of(2019, 1, 2, 9, 0);
		JournalEntry entry = new JournalEntry(date, JournalEntry.Mood.Awesome, "Test", lastModified);
		data.save(entry);

		entry.comment = "New Test";
		entry.mood = JournalEntry.Mood.Good;
		entry.date = LocalDate.of(2019, 1, 1);
		data.save(entry);

		List<JournalEntry> entries = data.getAllEntries();
		assertEquals(1, entries.size());
		JournalEntry actualEntry = entries.get(0);
		LocalDate expectedDate = LocalDate.of(2019, 1, 1);
		assertEntry(actualEntry, expectedDate, JournalEntry.Mood.Good, "New Test");
	}

	@Test
	public void testSaveNewEntryWithExistingId() {
		IJournalData data = new JournalData();
		LocalDate date = LocalDate.of(2019, 1, 2);
		LocalDateTime lastModified = LocalDateTime.of(2019, 1, 2, 9, 0);
		JournalEntry entry = new JournalEntry(date, JournalEntry.Mood.Awesome, "Test", lastModified);
		UUID uuid = UUID.fromString("8357b5bb-d1e3-43e4-9ad8-d3ad78371d8a");
		entry.uuid = uuid;
		data.save(entry);

		List<JournalEntry> entries = data.getAllEntries();
		assertEquals(1, entries.size());
		JournalEntry actualEntry = entries.get(0);
		assertEntry(actualEntry, LocalDate.of(2019, 1, 2), JournalEntry.Mood.Awesome, "Test");
		assertEquals(uuid, actualEntry.uuid);
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
		LocalDate date = LocalDate.of(2019, 1, 1);
		LocalDateTime lastModified = LocalDateTime.of(2019, 1, 2, 9, 0);
		JournalEntry toBeDeleted = new JournalEntry(date, JournalEntry.Mood.Good, "ToBeDeleted", lastModified);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Bad, "NotToBeDeleted-1", lastModified));
		entries.add(toBeDeleted);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Great, "NotToBeDeleted-2", lastModified));
		data.saveAll(entries);

		data.delete(toBeDeleted);

		List<JournalEntry> actualEntries = data.getAllEntries();
		assertEquals(3, actualEntries.size());
		JournalEntry actualEntry1 = actualEntries.get(0);
		assertEntry(actualEntry1, date, JournalEntry.Mood.Bad, "NotToBeDeleted-1");

		JournalEntry actualEntry2 = actualEntries.get(1);
		assertEntry(actualEntry2, date, JournalEntry.Mood.Good, "ToBeDeleted", true);

		JournalEntry actualEntry3 = actualEntries.get(2);
		assertEntry(actualEntry3, date, JournalEntry.Mood.Great, "NotToBeDeleted-2");
	}

	@Test
	public void testFindByDateCanFindYear() {
		IJournalData data = new JournalData();
		List<JournalEntry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 1, 1);
		LocalDateTime lastModified = LocalDateTime.of(2019, 1, 2, 9, 0);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-1", lastModified));
		entries.add(new JournalEntry(date.plusDays(1), JournalEntry.Mood.Good, "Test-2", lastModified));
		entries.add(new JournalEntry(date.plusYears(1), JournalEntry.Mood.Good, "Test-3", lastModified));
		data.saveAll(entries);

		List<JournalEntry> actualEntries = data.findByDate(-1, -1, 2019);
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, JournalEntry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date.plusDays(1), JournalEntry.Mood.Good, "Test-2");

		actualEntries = data.findByDate(-1, -1, 2020);
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date.plusYears(1), JournalEntry.Mood.Good, "Test-3");
	}

	@Test
	public void testFindByDateCanFindDay() {
		IJournalData data = new JournalData();
		List<JournalEntry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 1, 1);
		LocalDateTime lastModified = LocalDateTime.of(2019, 1, 2, 9, 0);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-1", lastModified));
		entries.add(new JournalEntry(date.plusDays(1), JournalEntry.Mood.Good, "Test-2", lastModified));
		entries.add(new JournalEntry(date.plusYears(1), JournalEntry.Mood.Good, "Test-3", lastModified));
		data.saveAll(entries);

		List<JournalEntry> actualEntries = data.findByDate(1, -1, -1);
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, JournalEntry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date.plusYears(1), JournalEntry.Mood.Good, "Test-3");

		actualEntries = data.findByDate(2, -1, -1);
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date.plusDays(1), JournalEntry.Mood.Good, "Test-2");
	}

	@Test
	public void testFindByDateCanFindMonth() {
		IJournalData data = new JournalData();
		List<JournalEntry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 1, 1);
		LocalDateTime lastModified = LocalDateTime.of(2019, 1, 2, 9, 0);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-1", lastModified));
		entries.add(new JournalEntry(date.plusMonths(1), JournalEntry.Mood.Good, "Test-2", lastModified));
		entries.add(new JournalEntry(date.plusYears(1), JournalEntry.Mood.Good, "Test-3", lastModified));
		data.saveAll(entries);

		List<JournalEntry> actualEntries = data.findByDate(-1, 1, -1);
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, JournalEntry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date.plusYears(1), JournalEntry.Mood.Good, "Test-3");

		actualEntries = data.findByDate(-1, 2, -1);
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date.plusMonths(1), JournalEntry.Mood.Good, "Test-2");
	}

	@Test
	public void testFindByString() {
		IJournalData data = new JournalData();
		List<JournalEntry> entries = new ArrayList<>();
		LocalDate date = LocalDate.of(2019, 1, 1);
		LocalDateTime lastModified = LocalDateTime.of(2019, 1, 2, 9, 0);
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-1", lastModified));
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "Test-2", lastModified));
		entries.add(new JournalEntry(date, JournalEntry.Mood.Good, "A comment", lastModified));
		data.saveAll(entries);

		List<JournalEntry> actualEntries = data.findByString("Test");
		assertEquals(2, actualEntries.size());
		assertEntry(actualEntries.get(0), date, JournalEntry.Mood.Good, "Test-1");
		assertEntry(actualEntries.get(1), date, JournalEntry.Mood.Good, "Test-2");

		actualEntries = data.findByString("omm");
		assertEquals(1, actualEntries.size());
		assertEntry(actualEntries.get(0), date, JournalEntry.Mood.Good, "A comment");

	}

	private void assertEntry(JournalEntry actualEntry, LocalDate date, JournalEntry.Mood mood, String comment) {
		assertEntry(actualEntry, date, mood, comment, false);
	}

	private void assertEntry(JournalEntry actualEntry, LocalDate date, JournalEntry.Mood mood, String comment, boolean deleted) {
		assertEquals(date, actualEntry.date);
		assertEquals(mood, actualEntry.mood);
		assertEquals(comment, actualEntry.comment);
		assertEquals(deleted, actualEntry.deleted);
	}
}
