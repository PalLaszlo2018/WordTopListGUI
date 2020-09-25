/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.sorters;

import java.util.Comparator;

/**
 * This comparator can help sorting the words by their lengths. (higher values prioritized).
 * @author laszlop
 */
public class WordLenComparator implements Comparator<String> {
/**
 * sorts the two Map.Entries received as parameter
 * @param word1
 * @param word2
 * @return 
 */
    @Override
    public int compare(String word1, String word2) {
        return Integer.compare(word2.length(), word1.length());
    }
    
}