/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.sorters;

import java.util.Comparator;
import java.util.Map;

/**
 * This comparator helps sorting the word - vowel frequency entries. It checks the vowel frequency first (higher values prioritized),
 * in case of equal frequencies alphabetical order will be used.
 * @author laszlop
 */
public class WordVowelFreqComparator implements Comparator<Map.Entry<String, Double>> {
/**
 * sorts the two Map.Entries received as parameter
 * @param wordFreq1
 * @param wordFreq2
 * @return 
 */
    @Override
    public int compare(Map.Entry<String, Double> wordFreq1, Map.Entry<String, Double> wordFreq2) {
        return Double.compare(wordFreq2.getValue(), wordFreq1.getValue());
    }
    
}