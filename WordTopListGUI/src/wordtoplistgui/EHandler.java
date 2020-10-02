/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.10.02. 
 */
package wordtoplistgui;

import wordtoplistgui.GUI.BasicFrame;

/**
 * Global exception handler to catch the Exceptions
 * @author laszlop
 */
public class EHandler implements Thread.UncaughtExceptionHandler {
    
    /**
     * Frame to display error messages.
     */
    private BasicFrame frame;
    
    /**
     * Creates a new instance with the given frame.
     * @param frame 
     */

    public EHandler(BasicFrame frame) {
        this.frame = frame;
    }
    
    /**
     * Displays caught Exceptions on the frame
     * @param t
     * @param e 
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        frame.displayErrors(e.getMessage());
    }

    
    
    
}
