/*
 * DataProvider.java
 * Create Date: Sep 29, 2020
 * Copyright: SONY MUSIC ENTERTAINMENT
 * Initial-Author: Janos Aron Kiss
 */

package wordtoplistgui.GUI;

import java.util.List;
import java.util.Map;
import wordtoplistgui.DataStore;

/**
 * The implementers provide data about the current status of the collecting process.
 * @version $Revision$ $LastChangedDate$
 * @author $Author$
 */
public interface DataProvider {

    List<DataStore> getSortedWords();
    
    List<String> getFinishedURLs();
    
    boolean isFinished();
    

}
