/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtoplistgui;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import wordtoplistgui.GUI.BasicFrame;

/**
 * The starting point of the application, initializes Logger and Frame
 * @author laszlop
 */
public class WordTopListGUI {

    public final static Logger LOG = Logger.getGlobal();

    static {
        LOG.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            private static final String FORMAT = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(FORMAT, new Date(lr.getMillis()), lr.getLevel(), lr.getMessage());
            }
        });
        LOG.addHandler(handler);
    }

    public static void main(String[] args) {
        BasicFrame frame = new BasicFrame(new CollectorManager());
    }
}
