package wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Scanner;

public class ScoreWords {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file = new File("wordle-list.txt");
		Scanner sc = new Scanner(file);
		
		
		
		int totalWords = 0;
		LinkedList<String> allValidGuesses = new LinkedList<>();
		HashMap<Character,Integer> frequency = new HashMap<Character,Integer>();
		
		while(sc.hasNext()) {
			String word = sc.next();
			allValidGuesses.add(word);
			totalWords++;
		}
		
		for(String word : allValidGuesses) {
			LinkedList<Character> alreadySeen = new LinkedList<>();
			for(int i = 0; i< word.length(); i++) {
				char c = word.charAt(i);
				if(!alreadySeen.contains(c)) {
					frequency.put(c, frequency.getOrDefault(c, 0) + 1);
					alreadySeen.add(c);
				}
				
			}
		}
		
		HashMap<Character, Double> charToScore = new HashMap<Character,Double>();
		for (char c : frequency.keySet()) {
			double score = (double)frequency.get(c) / totalWords;
			charToScore.put(c, score);
		}
		
		LinkedList<Entry<Character,Double>> sortedCharacters = new LinkedList<Entry<Character,Double>>(charToScore.entrySet());
		sortedCharacters.sort(Entry.comparingByValue());
		for(Entry<Character,Double> e : sortedCharacters) {
			System.out.println(e.getKey() + ": " + e.getValue());
		}
		
		
		HashMap<String,Double> wordToScore = new HashMap<String,Double>();
		for(String word : allValidGuesses) {
			double score = 0;
			LinkedList<Character> alreadySeen = new LinkedList<>();
			for(int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				if(!alreadySeen.contains(c)) { //ruling out duplicates
					score += charToScore.get(c);
					alreadySeen.add(c);
				}
				
			}
			wordToScore.put(word, score);
		}
		
		LinkedList<Entry<String,Double>> sortedWords = new LinkedList<Entry<String,Double>>(wordToScore.entrySet());
		sortedWords.sort(Entry.comparingByValue());
		for(Entry<String,Double> e : sortedWords) {
			System.out.println(e.getKey() + ": " + e.getValue());
		}
		
		
		
		String recommendation = allValidGuesses.getFirst();
		double recommendationScore = wordToScore.get(recommendation);
		for(String word : allValidGuesses) {
			double score = wordToScore.get(word);
			if(score > recommendationScore) {
				recommendation = word;
				recommendationScore = score;
			}
		}
		//System.out.println("Recommendation: " + recommendation);
		
	}

}
