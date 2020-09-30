/*
 * ChangeListener.java
 * Create Date: Sep 29, 2020
 * Copyright: SONY MUSIC ENTERTAINMENT
 * Initial-Author: Janos Aron Kiss
 */

package wordtoplistgui;

/**
 * The observer will be noticed when something changed during the word collecting process.
 * @author Janos Aron Kiss
 */
public interface CollectorObserver {
    
    /**
     * Runs the given code after change was realized.
     */
    void changed();

}
