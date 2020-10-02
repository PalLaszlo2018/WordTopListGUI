/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.10.02. 
 */
package wordtoplistgui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import wordtoplistgui.GUI.BasicFrame;

/**
 * Global exception handler to catch the Exceptions
 * @author laszlop
 */
public class EHandler implements Thread.UncaughtExceptionHandler {
    
    /**
     * Frame to display error messages.
     */
    @Nonnull
    private BasicFrame frame;
    
    /**
     * Creates a new instance with the given frame.
     * @param frame 
     */
    public EHandler(@Nonnull BasicFrame frame) {
        this.frame = frame;
    }
    
    /**
     * Displays caught Exceptions on the frame
     * @param t
     * @param e 
     */
    @Override
    public void uncaughtException(@Nullable Thread t, @Nonnull Throwable e) {
        frame.displayErrors(e.getMessage());
    }

    
    
    
}
