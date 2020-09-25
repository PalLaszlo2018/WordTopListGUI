/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.sorters;

import static wordtoplistgui.WordTopListGUI.LOG;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;


/**
 * This class can be used to create sorting by the length of the word
 *
 * @author laszlop
 */
public class SorterByLength implements WordStore {

    private final Set<String> words = new HashSet<>();
    private final Set<String> skipWords = new HashSet<>();

    /**
     * This method adds the got word to the Set which contains the found valid words.
     *
     * @param word
     */
    @Override
    public synchronized void store(CharSequence charSequence) {
        String word = charSequence.toString();
        if (word.length() > 1 && !skipWords.contains(word)) {
            words.add(word);
            LOG.log(Level.INFO, Thread.currentThread().getName() + " added word = " + word);
        }
    }

    /**
     * This method adds the got word to the Set which contains the words to be ignored.
     *
     * @param word
     */
    @Override
    public void addSkipWords(Collection<String> c) {
        skipWords.addAll(skipWords);
    }

    /**
     * Prints the full list of the found words.
     */
    @Override
    public void print() {
        System.out.println("Full wordlength list: " + sortedWordsByLength());
    }

    /**
     * Logs the n-sized top-list of the found words.
     *
     * @param n
     */
    @Override
    public void print(int n) {
        List<String> sortedList = sortedWordsByLength();
        LOG.log(Level.INFO, "The " + n + " longest words:");
        for (int i = 0; i < n; i++) {
            LOG.log(Level.INFO, " " + sortedList.get(i));
        }
    }

    /**
     * Creates the sorted List of the entries of the Map.
     * @return the sorted List
     */
    private List<String> sortedWordsByLength() {
        ArrayList<String> sortedList = new ArrayList<>(words);
        Collections.sort(sortedList, new WordLenComparator());
        return sortedList;
    }

}
