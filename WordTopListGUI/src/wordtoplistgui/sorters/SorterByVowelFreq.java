/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.sorters;

import static wordtoplistgui.WordTopListGUI.LOG;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.Nonnull;


/**
 * This class can be used to create sorting by vowel frequency
 * @author laszlop
 */
public class SorterByVowelFreq implements WordStore {
    
    /**
     * Map to store the found words as keys, and the number of their vowel frequencies as values.
     */
    private final Map<String, Double> wordVowelFreq = new HashMap<>();
    /**
     * Set of words which will be not stored.
     */
    private final Set<String> skipWords = new HashSet<>();
    private final Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));

    /**
     * This method adds the got word and its vowel frequency to the Map which contains the found valid words.
     */
    @Override
    public synchronized boolean store(CharSequence charSequence) {
        String word = charSequence.toString();
        if (word.length() > 2 && !skipWords.contains(word) && !wordVowelFreq.containsKey(word)) {
            double vowelFreq = countVowels(word) / (double) word.length();
            wordVowelFreq.put(word, vowelFreq);
            LOG.log(Level.INFO, Thread.currentThread().getName() + " added word = " + word);
            return true;
        }
        return false;
    }

    /**
     * counts the vowels in the input word
     * @param word to check
     * @return number of vowels
     */
    private int countVowels(String word) {
        int vowelCount = 0;
        for (int i = 0; i < word.length(); i++) {
            if (vowels.contains(word.charAt(i))) {
                vowelCount++;
            }
        }
        return vowelCount;
    }

    /**
     * This method adds the got collection of Strings to the Set which contains the words to be ignored.
     * @param c collection of Strings to be ignored
     */
    @Override
    public void addSkipWords(@Nonnull Collection<String> c) {
        skipWords.addAll(skipWords);
    }

    /**
     * Prints the full list of the found words.
     */
    @Override
    public void print() {
        System.out.println("Full frequency list: " + sortedWordsByFreq());
    }

    /**
     * Logs the n-sized top-list of the found words.
     * @param n
     */
    @Override
    public void print(int n) {
        List<Map.Entry<String, Double>> sortedList = sortedWordsByFreq();
        LOG.log(Level.INFO, "The " + n + "-sized list of words with highest vowel frequency:");
        for (int i = 0; i < n; i++) {
            DecimalFormat df = new DecimalFormat("###.###");
            LOG.log(Level.INFO, " " + sortedList.get(i).getKey() + " = " + df.format(sortedList.get(i).getValue()));
        }
    }

    /**
     * Creates the sorted List of the entries of the Map.
     * @return
     */
    private List<Map.Entry<String, Double>> sortedWordsByFreq() {
        ArrayList<Map.Entry<String, Double>> sortedList = new ArrayList<>(wordVowelFreq.entrySet());
        Collections.sort(sortedList, new WordVowelFreqComparator());
        return sortedList;
    }
}
