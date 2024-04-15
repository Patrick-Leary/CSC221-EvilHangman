/*
 * this program creates a hangman manager class which allows evil hangman to be played
 * 
 * @Author Patrick Leary
 * @Author Donald Lin
 * 
 * Time Spend: 10 hours
 * 
 */


import java.util.*;

public class HangmanManager {
    private int myLength;
    private int myMax;
    private SortedSet<Character> lettersGuessed = new TreeSet<>();
    private Set<String> wordsConsidered = new HashSet<>();
    private List<Character> patternList = new ArrayList<>();


    /*
     * Constructs the hangman manager class
     * throws an illegal argument exception is the length is less than 1 or the max is less than 0
     * 
     * @params - dictionary, length, max
     */
    public HangmanManager(List<String> dictionary, int length, int max) {
        if ((length < 1) || (max < 0)) {
            throw new IllegalArgumentException();
        }
        myLength = length;
        myMax = max;
        for (String x: dictionary) {
            if (x.length() == myLength) {
                wordsConsidered.add(x);
            }
        }
    }


    /*
     * returns the set of possible words that will be considered
     * 
     * @return words considered (set)
     */
    public Set<String> words() {
        return wordsConsidered;
    }

    /*
     * returns the number of guesses the player has left
     * 
     * @return number of guesses (int)
     */
    public int guessesLeft() {
        return myMax;
    }


    /*
     * returns the letters that the player has guessed in alphabetical order
     * 
     * @return letters guessed (sorted set)
     */
    public SortedSet<Character> guesses() {
        return lettersGuessed;
    }


    /*
     * returns the pattern showing the corrected letters guessed in a string pattern
     * if the number of words considered is equal to 0, it will throw an illegal state exception
     * 
     * @return pattern (string)
     */
    public String pattern() {
        if (wordsConsidered.size() == 0) {
            throw new IllegalStateException();
        }
        String pattern = "";
        if (patternList.isEmpty()) {
            for (int i = 0; i < myLength-1; i++) {
                pattern += "- ";
            }
            pattern += "-";
            return pattern;
        }
        for (int i = 0; i < patternList.size()-1; i++) {
            pattern += patternList.get(i) + " ";
        }
        pattern += (patternList.get(myLength-1));

        return pattern;
    }


    /*
     * Takes the guess of the user and find the next set of considered words
     * updates number of guessed left
     * if the number of guesses left is less than 1, it will throw an illegal state exception
     * if the guessed letter has already been guessed, it will throw an illegal argument exception
     * returns the number of guessed letters in the pattern
     * 
     * @param guess (character)
     * @return number of guessed letters in pattern (int)
     */
    public int record(char guess) {
        if (lettersGuessed.contains(guess)) {
            throw new IllegalArgumentException();
        }
        lettersGuessed.add(guess);
        boolean correctGuess = false;
        Map<String, Integer> patternMap = new HashMap<>();
        List<String> wordsConsideredList = new ArrayList<>();
        Set<String> tempWordsConsidered = new HashSet<>();
        Set<String> tempWordsConsidered2 = new HashSet<>();
        List<String> highestValue = new ArrayList<>();
        String tempString1 = "";
        String patternString = "";
        for (String x: wordsConsidered) {      // Creates the patterns of the words with dashes and the guessed letter
            for (int i = 0; i < myLength; i++) {
                if (x.charAt(i) == guess) {
                    tempString1 += guess;
                }
                else {
                    tempString1 += '-';
                }
            }
            wordsConsideredList.add(tempString1);
            tempWordsConsidered.add(tempString1);
            tempString1 = "";
        }
        for (String x: tempWordsConsidered) {
            patternMap.put(x, 0);
        }
        for (int i = 0; i < wordsConsideredList.size(); i++) {
            patternMap.put(wordsConsideredList.get(i), patternMap.get(wordsConsideredList.get(i)) + 1);  // puts all the possible patterns into a map and counts the number of occurences
        }

        int max = 0;
        for (String x: tempWordsConsidered) { // finds the pattern(s) with the most occurences
            if (patternMap.get(x) > max) {
                highestValue.clear();
                highestValue.add(x);
                max = patternMap.get(x);
            }
            else if (patternMap.get(x) == max) {
                highestValue.add(x);
            }
        }

        if (highestValue.size() == 1) {
            patternString = highestValue.get(0);
        }
        else {  // finds the pattern with the least amount of known letters
            int count = 0;
            int maxCount = 1000;
            for (String x: highestValue) {
                for (int i = 0; i < myLength; i++) {
                    if (x.charAt(i) != '-') {
                        count++;
                    }
                }
                if (count < maxCount) {
                    patternString = x;
                    maxCount = count;
                }
            }
        }
        if (patternList.isEmpty()) {  // turns the pattern into a list of charactres for the pattern() function
            for (int i = 0; i < patternString.length(); i++) {
                patternList.add(patternString.charAt(i));
            }
        }
        else {
            for (int i = 0; i < myLength; i++) {
                if (patternString.charAt(i) != '-') {
                    patternList.set(i, patternString.charAt(i));
                    correctGuess = true; // boolean to see if it was a correct guess (whether or not number of guesses should decrease)
                }
            }

        }


        int count = 0;
        for (String x: wordsConsidered) { // turns pattern with dashes and guessed letters back into the original word for the wordsConsidered set
            for (int i = 0; i < myLength; i++) {
                if ((x.charAt(i) == guess) && (patternString.charAt(i) == guess) || 
                ((x.charAt(i) != guess) && (patternString.charAt(i) == '-'))) {
                    count++;
                }
                if (count == myLength) {
                    tempWordsConsidered2.add(x);
                }
            }
            count = 0;
        }
        wordsConsidered = tempWordsConsidered2;

        count = 0;
        for (int i = 0; i < patternString.length(); i++) { // counts the number of guessed letters in the pattern
            if (patternString.charAt(i) != '-') {
                count++;
            }
        }

        if (correctGuess == false) { // decreases number of guessed if the guess was false
            myMax--;
        }
        if (myMax < 0) {
            throw new IllegalStateException();
        }
        
        return count;
    }
}