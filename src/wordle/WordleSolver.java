package wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import support.cse131.ArgsProcessor;

public class WordleSolver {

	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		/**
		 * 
		 * @author benpovich
		 * TODO
		 * bug fixes with double letters.
		 * example:
		 * wordle 126
		 * word: smogs
		 * YBBBG
		 * algorithm still recommended single s words
		 */
		class Guess{
			public String color;
			public char letter;
			public int position;
			
			public Guess(String color, char c, int position) {
				this.color = color;
				this.letter = c;
				this.position = position;
			}
			
		}
		
		
		
		ArgsProcessor ap = new ArgsProcessor(args);
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
		
	
		
		
		
		
		
		
		
		
		
		
		
		int round = 0;
		while(round < 6) {
			round++;
			String guess = "";
			if(guess.equals("r")) {
				break;
			}
			while(!(guess.length() == 5 && allValidGuesses.contains(guess))) {
				guess = ap.nextString("Round "+round + ": Please Enter A 5 Letter Word");
			}
			guess = guess.toLowerCase();
			LinkedList<Character> blacklist = new LinkedList<Character>();
			LinkedList<Guess> guesses = new LinkedList<Guess>();
			//assign black,yellow,green
			int greenCount = 0;
			for(int i = 0; i<5; i++) {
				String result = "";
				while(!result.equals("b") && !result.equals("y") && !result.equals("g")) {
					result = ap.nextString("Position "+ (i+1) +": Please enter result of the letter: " + guess.charAt(i) + " (b,y,g)");
					result = result.toLowerCase();
				}
				if(result.equals("y")) {
						Guess g = new Guess("y",guess.charAt(i),i);
						guesses.add(g);
					
				}
				else if(result.equals("g")) {
						Guess g = new Guess("g",guess.charAt(i),i);
						guesses.add(g);
					
				}
				else {
					blacklist.add(guess.charAt(i));
				}
			}
			
			
			for(Guess g : guesses) {
				if(blacklist.contains(g.letter)) {
					
					blacklist.remove(blacklist.indexOf(g.letter));
				}
			}
			//narrow down
			//first we remove words w blacklisted letters
			HashSet<String> remove = new HashSet<>();
			for(String word : allValidGuesses) {
				for(char letter : blacklist) {
					boolean contains = false;
					for(int i = 0; i<word.length(); i++) {
						if(word.charAt(i) == letter) {
							contains = true;
						}
					}
					if(contains) {
						remove.add(word);
						break;
					}
				}
				
			}
			allValidGuesses.removeAll(remove);
			
			remove.clear();
			for(Guess g : guesses) {
				if(g.color.equals("g")) {
					
					//remove all words that dont have the letter at the certain position
					for(String word : allValidGuesses) {
						if(word.charAt(g.position) != g.letter) {
							remove.add(word);
						}
					}
					
				}
				else if(g.color.equals("y")) {
					
					for(String word : allValidGuesses) {
						boolean hasLetter = false;
						for(int i = 0; i<word.length(); i++) {
							if(word.charAt(i) == g.letter) {
								hasLetter = true;
							}
						}
						if(!hasLetter) {
							remove.add(word);
						}
					}
					//now we remove all of the words with the letter at the last position guessed
					//kind of the opposite of green
					for(String word : allValidGuesses) {
						if(word.charAt(g.position) == g.letter) {
							remove.add(word);
						}
					}
					
				}
			}
			allValidGuesses.removeAll(remove);
			
			remove.clear();
			HashMap<Character,Integer> guessFreq = new HashMap<>();
			
			for(Guess g : guesses) {
				guessFreq.put(g.letter, guessFreq.getOrDefault(g.letter, 0) + 1);
			}
			
			for(Guess g : guesses) {
				int freqLetter = guessFreq.get(g.letter);
				if(freqLetter > 1) {
					for(String word : allValidGuesses) {
						int count = 0;
						for(int i = 0; i<word.length(); i++) {
							if(g.letter == word.charAt(i)) {
								count++;
							}
						}
						if(count != freqLetter) {
							remove.add(word);
						}
					}
				}
			}
			allValidGuesses.removeAll(remove);
			
			
			System.out.println("List of valid remaining words:");
			for(String word: allValidGuesses) {
				System.out.println(word);
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
			System.out.println("Recommendation: " + recommendation);
			
			
			
			
		}
		
	}

}
