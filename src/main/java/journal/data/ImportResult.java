package journal.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
			if (originalEntry.deleted != newEntry.deleted) {
				diffs.add(new ImportResult.DeletedDiff(originalEntry.deleted, newEntry.deleted));
			}
		}

		public boolean hasDiffs() {
			return !diffs.isEmpty();
		}
	}

	public static abstract class Diff {
		public abstract String getOriginal();

		public abstract String getNew();
	}

	public static class MoodDiff extends Diff {
		public final JournalEntry.Mood originalMood;
		public final JournalEntry.Mood newMood;

		public MoodDiff(JournalEntry.Mood originalMood, JournalEntry.Mood newMood) {
			this.originalMood = originalMood;
			this.newMood = newMood;
		}

		@Override
		public String getOriginal() {
			return originalMood.toString();
		}

		@Override
		public String getNew() {
			return newMood.toString();
		}
	}

	public static class CommentDiff extends Diff {
		public final String originalComment;
		public final String newComment;

		public CommentDiff(String originalComment, String newComment) {
			this.originalComment = originalComment;
			this.newComment = newComment;
		}

		@Override
		public String getOriginal() {
			return originalComment;
		}

		@Override
		public String getNew() {
			return newComment;
		}
	}

	public static class DateDiff extends Diff {
		public final LocalDate originalDate;
		public final LocalDate newDate;

		public DateDiff(LocalDate originalDate, LocalDate newDate) {
			this.originalDate = originalDate;
			this.newDate = newDate;
		}

		@Override
		public String getOriginal() {
			return originalDate.format(DateTimeFormatter.ISO_DATE);
		}

		@Override
		public String getNew() {
			return newDate.format(DateTimeFormatter.ISO_DATE);
		}
	}

	public static class DeletedDiff extends Diff {
		public final Boolean originalDeleted;
		public final Boolean newDeleted;

		public DeletedDiff(boolean originalDeleted, boolean newDeleted) {
			this.originalDeleted = originalDeleted;
			this.newDeleted = newDeleted;
		}

		@Override
		public String getOriginal() {
			return originalDeleted.toString();
		}

		@Override
		public String getNew() {
			return newDeleted.toString();
		}
	}

	public final List<Problem> problems = new ArrayList<>();

	public void addProblem(Problem problem) {
		if (!problem.hasDiffs()) {
			return;
		}
		problems.add(problem);
	}

	public boolean hasProblems() {
		return !problems.isEmpty();
	}
}
