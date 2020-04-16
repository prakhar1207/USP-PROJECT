import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


class SearchMethods {
	
	static ArrayList<String> searchGoodFiles(String  directory, ArrayList<String> paths,
	                                                String word,  String extension) {
		File dir = new File(directory);
		try {
			for (File i : dir.listFiles()) {
				
				if (i.isDirectory()) {
					searchGoodFiles(i.getPath(), paths, word, extension);
				} else if (isInFile(i.getPath(), word, extension)) {
					paths.add(i.getPath());
				}
			}
		} catch (NullPointerException npe) {npe.printStackTrace();
		}
		
		return paths;
	}
	
	private static StyledDocument docInsertString(StyledDocument document ,int length, String str,
	                                              SimpleAttributeSet attributeSet){
		try {
			document.insertString(length, str, attributeSet);
		} catch (BadLocationException ble) {ble.printStackTrace();}
		
		return document;}
	
	static void selectText(File file, ArrayList<ArrayList<Integer>> array,
	                              String word, JTextPane textPane){
		
		try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), StandardCharsets.UTF_8))){
			
			int rowCounter = -1;
			String line;
			
			textPane.setText("");
			
			StyledDocument doc = textPane.getStyledDocument();
			SimpleAttributeSet keyWord = new SimpleAttributeSet();
			StyleConstants.setForeground(keyWord, Color.BLACK);
			StyleConstants.setBackground(keyWord, Color.YELLOW);
			StyleConstants.setBold(keyWord, true);
			
			int j = 0;
			
			while ((line = fileReader.readLine()) != null) {  // i know that's bad
				rowCounter += 1;
				if (j < array.size()){
					if (rowCounter == array.get(j).get(0)){
						int currentPositionInRow = 0;
						int k = 2;
						while (currentPositionInRow < line.length()){
							if (k < array.get(j).size()) {
								if (currentPositionInRow == array.get(j).get(k)) {
									doc = docInsertString(doc, doc.getLength(),
											String.valueOf(line.substring(currentPositionInRow,
													currentPositionInRow + word.length())), keyWord);
									k += 1;
									currentPositionInRow += word.length();
								}
								else {
									doc = docInsertString(doc, doc.getLength(),
											String.valueOf(line.charAt(currentPositionInRow)), null);
									currentPositionInRow +=1;
								}
							}
							else {
								doc = docInsertString(doc,doc.getLength(),
										String.valueOf(line.charAt(currentPositionInRow)), null );
								currentPositionInRow +=1;
							}
						}
						doc = docInsertString(doc, doc.getLength(), "\n", null);
						j+=1;
					}
					else {
						doc = docInsertString(doc, doc.getLength(), line+"\n", null);
					}
				}
				else {
					doc = docInsertString(doc, doc.getLength(), line+"\n", null);
				}
			}}
		catch (IOException ioe){ioe.printStackTrace();}
	}
	
	static ArrayList<ArrayList<Integer>> finder(String filePath, String word) {
		
		ArrayList<ArrayList<Integer>> indList = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
			
			int str = 0;
			int pos = 0;
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				if ((line.contains(word))) {
					ArrayList<Integer> indexesS = new ArrayList<>();
					indexesS.add(str);
					indexesS.add(pos);
					int index = line.indexOf(word);
					while (index >= 0) {
						indexesS.add(index);
						index = line.indexOf(word, index + word.length());
					}
					indList.add(indexesS);
					pos += line.length();
				}
				else {
					pos += line.length();
				}
				str += 1;
			}
			return indList;
			
			
		} catch (IOException ioe) {ioe.printStackTrace();}
		return indList;
		
	}
	
	private static boolean isInFile(String path, String word, String extension){
		
		String[] ar = path.split("/");
		boolean isNeeded = false;
		
		if (ar[ar.length-1].endsWith(extension)) {
			try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
				
				String line;
				
				while ((line = reader.readLine()) != null) {
					if ((line.contains(word))) {
						isNeeded = true;
						break;
					}
				}
			} catch (IOException ioe) {ioe.printStackTrace();}
		}
		
		return isNeeded;
	}
	
}