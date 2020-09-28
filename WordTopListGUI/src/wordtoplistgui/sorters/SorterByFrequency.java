/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.sorters;

import static wordtoplistgui.WordTopListGUI.LOG;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;


/**
 * This class can be used to create sorting by frequency
 *
 * @author laszlop
 */
public class SorterByFrequency implements WordStore {
    
    private final Map<String, Integer> wordFrequency = new HashMap<>();
    private final Set<String> skipWords = new HashSet<>();

    /**
     * adds the got word to the Map which contains the found valid words.
     * @param charSequence 
     */
    @Override
    public synchronized boolean store(CharSequence charSequence) {
        if (charSequence.length() > 2) {
            String word = charSequence.toString().toLowerCase();
            if (!skipWords.contains(word)) {
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                LOG.log(Level.INFO, Thread.currentThread().getName() + " added word = " + word);
            }
            return true;
        }
        return false;
    }
    
    /**
     * This method adds the got word to the Set which contains the words to be ignored.
     *
     * @param word
     */
    @Override
    public void addSkipWords(Collection<String> c) {
        skipWords.addAll(c);
    }

    /**
     * Prints the full list of the found words.
     */
    @Override
    public void print() {
        LOG.log(Level.INFO, "Full frequency list: " + sortedWordsByFreq());
    }

    /**
     * Logs the n-sized top-list of the found words.
     *
     * @param n
     */
    @Override
    public void print(int n) {
        List<Map.Entry<String, Integer>> sortedList = sortedWordsByFreq();
        if (sortedList.isEmpty()) {
            System.out.println("No list was created.");
            return;
        }
        LOG.log(Level.INFO, "The " + n + " most used words:");
        for (int i = 0; i < n; i++) {
            LOG.log(Level.INFO, " " + sortedList.get(i));
        }
        System.out.println("");
    }

    /**
     * Creates the sorted List of the entries of the Map.
     *
     * @return sorted List
     */  
    public synchronized List<Map.Entry<String, Integer>> sortedWordsByFreq() {
        ArrayList<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordFrequency.entrySet());
        Collections.sort(sortedList, new WordFreqComparator());
        return sortedList;
    }
    
}
