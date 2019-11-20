package Journal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JournalData implements IJournalData {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private static ObjectMapper MAPPER = new ObjectMapper();

	private final List<Entry> entries = new ArrayList<>();

	private int lastEntryID = 0;

	@Override
	public List<Entry> findByDate(int day, int month, int year) {
		List<Entry> list = new ArrayList<>();
		for (Entry entry : entries) {
			if (day == -1 || entry.date.getDayOfMonth() == day) {
				if (month == -1 || entry.date.getMonthValue() == month) {
					if (year == -1 || entry.date.getYear() == year) {
						list.add(entry);
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<Entry> getAllEntries() {
		return entries;
	}

	@Override
	public List<Entry> findByString(String text) {
		if (JournalHelper.INSTANCE.getMood(text) != null) {
			return entries.stream()
					.filter(entry -> entry.mood == JournalHelper.INSTANCE.getMood(text))
					.collect(Collectors.toList());
		}

		return entries.stream()
				.filter(entry -> entry.comment.toLowerCase().contains(text.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public void save(Entry entry) {
		entry.id = getNextID();
		entries.add(entry);
	}

	private int getNextID() {
		return ++lastEntryID;
	}

	@Override
	public void delete(Entry entry) {
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).id == entry.id) {
				entries.remove(i);
				break;
			}
		}
		if (entries.size() > 0) {
			lastEntryID = entries.get(entries.size() - 1).id;
		} else {
			lastEntryID = 0;
		}
	}
}
