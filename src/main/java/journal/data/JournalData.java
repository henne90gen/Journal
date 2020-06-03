package journal.data;

import com.google.common.flogger.FluentLogger;
import journal.Journal;
import journal.JournalHelper;

import java.util.*;
import java.util.stream.Collectors;

public class JournalData implements IJournalData {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private final Map<UUID, JournalEntry> entries = new LinkedHashMap<>();

	private final List<Callback> updateCallbacks = new ArrayList<>();

	@Override
	public List<JournalEntry> findByDate(int day, int month, int year) {
		return entries.values().stream()
				.filter(e -> day == -1 || e.date.getDayOfMonth() == day)
				.filter(e -> month == -1 || e.date.getMonthValue() == month)
				.filter(e -> year == -1 || e.date.getYear() == year)
				.collect(Collectors.toList());
	}

	@Override
	public List<JournalEntry> getAllEntries() {
		return entries.values().stream()
				.sorted(Comparator.comparing(a -> a.date))
				.collect(Collectors.toList());
	}

	@Override
	public List<JournalEntry> findByString(String text) {
		if (JournalHelper.INSTANCE.getMood(text) != null) {
			return entries.values().stream()
					.filter(entry -> entry.mood == JournalHelper.INSTANCE.getMood(text))
					.collect(Collectors.toList());
		}

		return entries.values().stream()
				.filter(entry -> entry.comment.toLowerCase().contains(text.toLowerCase()))
				.collect(Collectors.toList());
	}

	private void save_(JournalEntry entry) {
		entries.put(entry.uuid, entry);
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

	@Override
	public void init() {
		saveAll(FileDataSource.INSTANCE.readFromFile().entries);
	}

	@Override
	public ImportResult importEntries(List<JournalEntry> entries) {
		ImportResult importResult = new ImportResult();
		for (JournalEntry entry : entries) {
			if (!this.entries.containsKey(entry.uuid)) {
				this.entries.put(entry.uuid, entry);
				continue;
			}

			JournalEntry originalEntry = this.entries.get(entry.uuid);
			ImportResult.Problem problem = new ImportResult.Problem();
			problem.findDiffs(originalEntry, entry);
			importResult.addProblem(problem);
		}

		return importResult;
	}

	@Override
	public void delete(JournalEntry entry) {
		entries.remove(entry.uuid);

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
