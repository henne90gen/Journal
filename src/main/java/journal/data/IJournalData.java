package journal.data;

import java.util.List;

public interface IJournalData {

	List<JournalEntry> getAllEntries(boolean includeDeleted);

	default List<JournalEntry> getAllEntries() {
		return getAllEntries(true);
	}

	List<JournalEntry> findByString(String text);

	List<JournalEntry> findByDate(int day, int month, int year);

	void save(JournalEntry entry);

	void delete(JournalEntry entry);

	void addUpdateCallback(Callback callback);

	void saveAll(List<JournalEntry> entries);

	void init();

	ImportResult importEntries(List<JournalEntry> entries);

	void syncWithGoogleDrive();
}
