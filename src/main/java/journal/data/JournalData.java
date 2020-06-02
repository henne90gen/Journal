package journal.data;

import com.google.common.flogger.FluentLogger;
import journal.JournalHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JournalData implements IJournalData {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private final List<JournalEntry> entries = new ArrayList<>();

	private int lastEntryID = -1;

	private List<Callback> updateCallbacks = new ArrayList<>();

	@Override
	public List<JournalEntry> findByDate(int day, int month, int year) {
		return entries.stream()
				.filter(e -> day == -1 || e.date.getDayOfMonth() == day)
				.filter(e -> month == -1 || e.date.getMonthValue() == month)
				.filter(e -> year == -1 || e.date.getYear() == year)
				.collect(Collectors.toList());
	}

	@Override
	public List<JournalEntry> getAllEntries() {
		return entries;
	}

	@Override
	public List<JournalEntry> findByString(String text) {
		if (JournalHelper.INSTANCE.getMood(text) != null) {
			return entries.stream()
					.filter(entry -> entry.mood == JournalHelper.INSTANCE.getMood(text))
					.collect(Collectors.toList());
		}

		return entries.stream()
				.filter(entry -> entry.comment.toLowerCase().contains(text.toLowerCase()))
				.collect(Collectors.toList());
	}

	private void save_(JournalEntry entry) {
		if (entry.id == -1) {
			entry.id = getNextID();
			entries.add(entry);
			return;
		}

		boolean found = false;
		for (JournalEntry e : entries) {
			if (entry.id != e.id) {
				continue;
			}
			e.date = entry.date;
			e.mood = entry.mood;
			e.comment = entry.comment;
			found = true;
			break;
		}
		if (!found) {
			entries.add(entry);
		}
		if (entry.id > lastEntryID) {
			lastEntryID = entry.id;
		}
	}

	@Override
	public void save(JournalEntry entry) {
		save_(entry);

		invokeCallbacks();
	}

	@Override
	public void saveAll(List<JournalEntry> entries) {
		for (JournalEntry entry : entries) {
			save_(entry);
		}

		invokeCallbacks();
	}

	int getNextID() {
		return ++lastEntryID;
	}

	@Override
	public void delete(JournalEntry entry) {
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).id == entry.id) {
				entries.remove(i);
				break;
			}
		}

		invokeCallbacks();
	}

	@Override
	public void addUpdateCallback(Callback callback) {
		if (callback == null) {
			LOGGER.atWarning().log("Callback should not be null.");
			return;
		}
		this.updateCallbacks.add(callback);
	}

	private void invokeCallbacks() {
		for (Callback callback : updateCallbacks) {
			callback.call();
		}
	}
}
