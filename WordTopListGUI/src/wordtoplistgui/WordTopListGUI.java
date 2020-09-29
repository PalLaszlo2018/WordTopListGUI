/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
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
 *
 * @author laszlop
 */
public class WordTopListGUI {

    public final static Logger LOG = Logger.getGlobal();

    public static void main(String[] args) {
        ensureLogger();
        CollectorManager collectorManager = new CollectorManager();
        BasicFrame frame = new BasicFrame();
        collectorManager.setCollectorSettings(frame);
        collectorManager.setCollectorObserver(frame);
        frame.setActionObserver(collectorManager);
        frame.setDataProvider(collectorManager);
    }

    private static void ensureLogger() {
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
}
