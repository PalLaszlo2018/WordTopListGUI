/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.sorters;

import java.util.Collection;
import javax.annotation.Nonnull;



/**
 * Interface to be implemented to store words.
 * @author laszlop
 */
public interface WordStore {
    
    /**
     * Stores the found CharSequence.
     * @param c CharSequence to be stored
     * @return whether the storing was successful or not 
     */
    public boolean store(@Nonnull CharSequence c);
    
    /**
     * Stores the collection of words to be ignored by storing
     * @param c Collection of words to be ignored
     */
    public void addSkipWords(@Nonnull Collection<String> c);
    
    /**
     * Prints all found words.
     */
    public void print();
    
    /**
     * Prints the n-sized top-list of the found words.
     * @param n 
     */
    public void print(int n);   

    
}
