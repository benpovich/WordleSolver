package wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class SortWords {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File file = new File("wordle-list.txt");
		Scanner sc = new Scanner(file);
		
		LinkedList<String> allValidGuesses = new LinkedList<>();
		while(sc.hasNext()) {
			String word = sc.next();
			allValidGuesses.add(word);
		}
		Collections.sort(allValidGuesses);
		
		File sortedFile = new File("wordle-list-sorted.txt");
		FileWriter writer = new FileWriter(sortedFile);
		for(String word : allValidGuesses) {
			writer.write(word+"\n");
		}
		writer.close();
	}

}
