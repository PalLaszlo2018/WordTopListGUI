/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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