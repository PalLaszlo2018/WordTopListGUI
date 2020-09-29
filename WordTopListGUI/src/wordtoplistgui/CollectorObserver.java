/*
 * ChangeListener.java
 * Create Date: Sep 29, 2020
 * Copyright: SONY MUSIC ENTERTAINMENT
 * Initial-Author: Janos Aron Kiss
 */

package wordtoplistgui;

/**
 * The observer will be noticed when something changed during the word collecting process.
 * @version $Revision$ $LastChangedDate$
 * @author $Author$
 */
public interface CollectorObserver {

    void changed();

}
