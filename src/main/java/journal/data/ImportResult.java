package journal.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ImportResult {
	public static class Problem {
		public List<Diff> diffs = new ArrayList<>();

		public void findDiffs(JournalEntry originalEntry, JournalEntry newEntry) {
			if (!originalEntry.comment.equals(newEntry.comment)) {
				diffs.add(new ImportResult.CommentDiff(originalEntry.comment, newEntry.comment));
			}
			if (!originalEntry.mood.equals(newEntry.mood)) {
				diffs.add(new ImportResult.MoodDiff(originalEntry.mood, newEntry.mood));
			}
			if (!originalEntry.date.equals(newEntry.date)) {
				diffs.add(new ImportResult.DateDiff(originalEntry.date, newEntry.date));
			}
		}

		public boolean hasDiffs() {
			return !diffs.isEmpty();
		}
	}

	public static abstract class Diff {
	}

	public static class MoodDiff extends Diff {
		public final JournalEntry.Mood originalMood;
		public final JournalEntry.Mood newMood;

		public MoodDiff(JournalEntry.Mood originalMood, JournalEntry.Mood newMood) {
			this.originalMood = originalMood;
			this.newMood = newMood;
		}
	}

	public static class CommentDiff extends Diff {
		public final String originalComment;
		public final String newComment;

		public CommentDiff(String originalComment, String newComment) {
			this.originalComment = originalComment;
			this.newComment = newComment;
		}
	}

	public static class DateDiff extends Diff {
		public final LocalDateTime originalDate;
		public final LocalDateTime newDate;

		public DateDiff(LocalDateTime originalDate, LocalDateTime newDate) {
			this.originalDate = originalDate;
			this.newDate = newDate;
		}
	}

	public final List<Problem> problems = new ArrayList<>();

	public void addProblem(Problem problem) {
		if (problem.hasDiffs()) {
			return;
		}
		problems.add(problem);
	}

	public boolean hasProblems() {
		return !problems.isEmpty();
	}
}
