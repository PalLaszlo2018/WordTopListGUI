/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.29. 
 */
package wordtoplistgui;

import javax.annotation.Nonnull;

/**
 * Custom data class for storing the results
 * @author laszlop
 */
public class DataStore {
    
    @Nonnull 
    private String word;
    @Nonnull 
    private int value;
    
    /**
     * creates a new instance with the given word and value
     * @param word
     * @param value 
     */
    public DataStore(String word, int value) {
        this.word = word;
        this.value = value;
    }
        
    
    //===========GETTERS============
    
    /**
     * Delivers the stored word.
     * @return 
     */
    public String getWord() {
        return word;
    }
    
    /**
     * Delivers the stored value.
     * @return 
     */
    public int getValue() {
        return value;
    } 
 
    
}
