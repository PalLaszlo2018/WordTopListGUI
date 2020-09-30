/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.sorters;

import java.util.Comparator;
import javax.annotation.Nonnull;

/**
 * This comparator can help sorting the words by their lengths. (higher values prioritized).
 * @author laszlop
 */
public class WordLenComparator implements Comparator<String> {

    
    /**
     * Returns the order of the two words.
     * @param word1 to be sorted
     * @param word2 to be sorted
     * @return 
     */
    @Override
    public int compare(@Nonnull String word1, @Nonnull String word2) {
        return Integer.compare(word2.length(), word1.length());
    }
    
}