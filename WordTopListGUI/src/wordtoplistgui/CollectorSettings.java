/*
 * CollectorSettings.java
 * Create Date: Sep 29, 2020
 * Copyright: SONY MUSIC ENTERTAINMENT
 * Initial-Author: Janos Aron Kiss
 */

package wordtoplistgui;

import java.net.URL;
import java.util.Collection;
import javax.annotation.Nonnull;

/**
 * The implementers provide information for the {@link CollectorManager}, to perform the word collecting process.
 * @author Janos Aron Kiss
 */
public interface CollectorSettings {
    
    /**
     * Delivers the maximal number of threads.
     * @return the maximal number of threads
     */
    int getMaxThreads();
    
    /**
     * Delivers the got URLs.
     * @return the got URLs.
     */
    @Nonnull
    Collection<URL> getURLs();

}
