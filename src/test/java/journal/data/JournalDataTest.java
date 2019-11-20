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
		assertEquals(0, actualEntry.id);
		assertEquals(LocalDate.of(2019, 1, 2), actualEntry.date);
		assertEquals(Entry.Mood.Awesome, actualEntry.mood);
		assertEquals("Test", actualEntry.comment);
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
		assertEquals(0, actualEntry.id);
		assertEquals(LocalDate.of(2019, 1, 1), actualEntry.date);
		assertEquals(Entry.Mood.Good, actualEntry.mood);
		assertEquals("New Test", actualEntry.comment);
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
		assertEquals(123, actualEntry.id);
		assertEquals(LocalDate.of(2019, 1, 2), actualEntry.date);
		assertEquals(Entry.Mood.Awesome, actualEntry.mood);
		assertEquals("Test", actualEntry.comment);
	}

	class Counter {
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
}
