/*
 * DataProvider.java
 * Create Date: Sep 29, 2020
 * Copyright: SONY MUSIC ENTERTAINMENT
 * Initial-Author: Janos Aron Kiss
 */

package wordtoplistgui.GUI;

import java.util.List;
import java.util.Map;

/**
 * The implementers provide data about the current status of the collecting process.
 * @version $Revision$ $LastChangedDate$
 * @author $Author$
 */
public interface DataProvider {

    // TODO: Maybe an own data class would be better, instead of using Map.Entry.
    List<Map.Entry<String, Integer>> getSortedWords();
    
    List<String> getFinishedURLs();
    
    boolean isFinished();

}
