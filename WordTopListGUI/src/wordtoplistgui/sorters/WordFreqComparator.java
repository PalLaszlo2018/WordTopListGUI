/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.sorters;

import java.util.Comparator;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * This comparator can help sorting the word-frequency entries. It checks the frequency first (higher values prioritized), in case
 * of equal frequencies alphabetical order will be used.
 * @author laszlop
 */
public class WordFreqComparator implements Comparator<Map.Entry<String, Integer>> {

    /**
     * sorts the two Map.Entries received as parameter
     * @param wordFreq1
     * @param wordFreq2
     * @return 
     */
    @Override
    public int compare(@Nonnull Map.Entry<String, Integer> wordFreq1, @Nonnull Map.Entry<String, Integer> wordFreq2) {
        Integer freq1 = wordFreq1.getValue();
        Integer freq2 = wordFreq2.getValue();
        if (freq1 != freq2) {
            return - freq1.compareTo(freq2);
        }
        return wordFreq1.getKey().compareTo(wordFreq2.getKey());
    }
    
}