/*
 * DataProvider.java
 * Create Date: Sep 29, 2020
 * Copyright: SONY MUSIC ENTERTAINMENT
 * Initial-Author: Janos Aron Kiss
 */

package wordtoplistgui.GUI;

import java.util.List;
import wordtoplistgui.DataStore;

/**
 * The implementers provide data about the current status of the collecting process.
 * @author Janos Aron Kiss
 */
public interface DataProvider {
    
    /**
     * Delivers the stored words.
     * @return 
     */
    List<DataStore> getSortedWords();
    
    /**
     * Delivers the already finished URLs.
     * @return 
     */
    List<String> getFinishedURLs();
    
    /**
     * Checks whether the entire procedure was finished or not.
     * @return 
     */
    boolean isFinished();
    

}
