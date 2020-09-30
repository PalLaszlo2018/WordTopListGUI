/*
 * CollectorSettings.java
 * Create Date: Sep 29, 2020
 * Copyright: SONY MUSIC ENTERTAINMENT
 * Initial-Author: Janos Aron Kiss
 */

package wordtoplistgui;

import java.net.URL;
import java.util.Collection;

/**
 * The implementers provide information for the {@link CollectorManager}, to perform the word collecting process.
 * @author Janos Aron Kiss
 */
public interface CollectorSettings {
    
    /**
     * Returns the maximal number of threads.
     * @return 
     */
    int getMaxThreads();
    
    /**
     * Returns the got URLs.
     * @return 
     */
    Collection<URL> getURLs();

}
