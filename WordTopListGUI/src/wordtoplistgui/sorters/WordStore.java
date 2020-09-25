/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.sorters;

import java.util.Collection;



/**
 * Interface to be implemented to store words
 * @author laszlop
 */
public interface WordStore {
    
    public void store(CharSequence c);
    
    public void addSkipWords(Collection<String> c);
    
    public void print();
    
    public void print(int n);   

    
}
