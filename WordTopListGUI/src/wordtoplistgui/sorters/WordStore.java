/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
