/*
 * ActionObserver.java
 * Create Date: Sep 29, 2020
 * Copyright: SONY MUSIC ENTERTAINMENT
 * Initial-Author: Janos Aron Kiss
 */

package wordtoplistgui.GUI;

import java.net.URL;
import java.util.Collection;

/**
 * The action observer will be noticed when the user do something important on the GUI.
 * @version $Revision$ $LastChangedDate$
 * @author $Author$
 */
public interface ActionObserver {

    void doAction();
    
    void setMaxThreads(int max);
    
    void setURLs(Collection<URL> coll);

}
