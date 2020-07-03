package journal.data;

import java.util.ArrayList;
import java.util.List;

public class EntryStorage {
	public static final int CURRENT_VERSION = 4;

	public int version = CURRENT_VERSION;
	public List<JournalEntry> entries = new ArrayList<>();
}
