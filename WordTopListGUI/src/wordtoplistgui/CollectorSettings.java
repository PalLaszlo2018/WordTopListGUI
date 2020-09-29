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
 * @version $Revision$ $LastChangedDate$
 * @author $Author$
 */
public interface CollectorSettings {

    int getMaxThreads();
    
    Collection<URL> getURLs();

}
