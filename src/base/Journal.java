package base;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingUtilities;

public class Journal {

	private static J_Window window;
	private HashMap<Integer, String> entriesToSearch = new HashMap<Integer, String>();
	private ArrayList<String> entries = new ArrayList<String>();
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window = new J_Window();
			}
		});
		new Journal();
	}
	
	public Journal() {
		if (readComments()) {
			
			
			
			
		}
	}
	
	private boolean readComments() {
		
		try {
			int numDays = countLines("Comments.txt") / 8;
			
			BufferedReader br = new BufferedReader(new FileReader("Comments.txt"));
			String line = br.readLine();
			int lineCounter = 0;
			int key = 0;
			
			while (line != null) {
				if (line.equalsIgnoreCase("--------------------------------------------------")) { lineCounter = 0; } else { lineCounter++; }
				if (lineCounter == 1) { key = getKey(line); } else if (lineCounter == 5) {
					entriesToSearch.put(key, line);
					entries.add(line);
					window.setProgressBar((100 * entries.size()) / numDays);
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private Integer getKey(String day) {
		String key = "";
		
		String[] parts = day.split(" ");
		
		switch (parts[1]) {
		case "Jan":
			key = 11 + parts[2] + parts[5]; 
			break;
		case "Feb":
			key = 12 + parts[2] + parts[5]; 
			break;
		case "Mar":
			key = 13 + parts[2] + parts[5]; 
			break;
		case "Apr":
			key = 14 + parts[2] + parts[5]; 
			break;
		case "May":
			key = 15 + parts[2] + parts[5]; 
			break;
		case "Jun":
			key = 16 + parts[2] + parts[5]; 
			break;
		case "Jul":
			key = 17 + parts[2] + parts[5]; 
			break;
		case "Aug":
			key = 18 + parts[2] + parts[5]; 
			break;
		case "Sep":
			key = 19 + parts[2] + parts[5]; 
			break;
		case "Oct":
			key = 20 + parts[2] + parts[5]; 
			break;
		case "Nov":
			key = 21 + parts[2] + parts[5]; 
			break;
		case "Dec":
			key = 22 + parts[2] + parts[5]; 
			break;
		}
		
		return new Integer(key);
	}
	
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
}
