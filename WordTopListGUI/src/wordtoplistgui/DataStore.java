/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.29. 
 */
package wordtoplistgui;

/**
 * Custom data class for storing the results
 * @author laszlop
 */
public class DataStore {
    
    private String word;
    private int value;

    public DataStore(String word, int value) {
        this.word = word;
        this.value = value;
    }
        
    
    //===========GETTERS============

    public String getWord() {
        return word;
    }

    public int getValue() {
        return value;
    }
    
       
    
}
