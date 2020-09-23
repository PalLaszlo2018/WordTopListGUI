/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtoplistgui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class manages the processing of the got URLList, collects the words of the contents in a Map.
 *
 * @author laszlop
 */
public class WordCollector extends Thread {

    public final static Logger LOG = Logger.getGlobal();

    private final BlockingQueue<URL> urlQueue;
    private final CountDownLatch latch;
    private final Set<String> skipTags;
    private final Set<String> skipWords;
    private final WordStore storer;
    private final BasicFrame frame;

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

    public WordCollector(BlockingQueue<URL> urlQueue, CountDownLatch latch, Set<String> skipWords, WordStore storer, BasicFrame frame) {
        this.urlQueue = urlQueue;
        this.latch = latch;
        this.skipTags = new HashSet<>(Arrays.asList("head", "style", "script")); // texts between these tags are ignored
        this.skipWords = skipWords;
        this.storer = storer;
        this.frame = frame;
    }

    @Override
    public void run() {
        fillSkipWords();
        while (true) {
            URL url = takeURLfromQueue();
            if (url == null) {
                LOG.info(Thread.currentThread().getName() + ": No more URL in the queue. Current thread terminates!");
                return;
            }
            try {
                processContent(url);
            } catch (IOException ex) {
                LOG.severe("Processing of " + url.toString() + " failed.");
                LOG.warning(ex.getMessage());
            } finally {
                synchronized (latch) {
                    latch.countDown();
                    LOG.info(Thread.currentThread().getName() + ": " + url.toString()
                            + " finished. The current size of the latch is: " + latch.getCount());
                    frame.displayprocessedURLs(url.toString());
                    if (latch.getCount() == 0) {
                        frame.startURLs.setText("Processing finished.");
                    }
                }
            }
        }
    }

    /**
     * Takes out the next URL form the queue thread safe way
     *
     * @return next URL
     */
    private synchronized URL takeURLfromQueue() {
        URL url = urlQueue.poll();
        LOG.info(Thread.currentThread().getName() + ": " + url + " was taken out from the queue, " + urlQueue.size() + " URL-s remained.");
        return url;
    }

    /**
     * Fills up the skipWord Set using the overridden method of WordStore interface
     */
    public void fillSkipWords() {
        for (String skipWord : skipWords) {
            addSkipWord(skipWord);
        }
    }

    /**
     * Opens a reader for the got URL, finds the opening tag, and starts the substantive work by calling the eatTag method.
     *
     * @param url
     * @throws IOException
     */
    public void processContent(URL url) throws IOException {
        LOG.info("Processing of the homepage " + url.toString() + " started.");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String openingTag = findOpeningTag(reader);
            eatTag(openingTag, reader, true); //TODO
        }
    }

    /**
     * Reads the content of the URL, the found words will be put into a Map, found opening tags start the method recursive way,
     * found closing tags close the (sub)method.
     *
     * @param tag
     * @param reader
     * @throws IOException
     */
    private void eatTag(String tag, BufferedReader reader, boolean processable) throws IOException {
        int value;
        StringBuilder word = new StringBuilder();
        while ((value = reader.read()) != -1) {
            char character = (char) value;
            if (character == '<') {
                if (processable) {
                    storer.store(word.toString().toLowerCase());
                }
                String nextTagString = buildTag(reader);
                if (('/' + tag).equals(nextTagString)) {
                    return;
                }
                if (!skipTags.contains(tag) && !nextTagString.startsWith("/")) {
                    boolean nextProcessable = processable && !skipTags.contains(nextTagString);
                    eatTag(nextTagString, reader, nextProcessable);
                }
            }
            if (Character.isLetter(character)) {
                word.append(character);
            } else {
                if (processable) {
                    storer.store(word.toString().toLowerCase());
                }
                word.setLength(0);
            }

        }
    }

    /**
     * This method builds up the tag from the read characters.
     *
     * @param reader
     * @return tag
     * @throws IOException
     */
    private String buildTag(BufferedReader reader) throws IOException {
        StringBuilder tag = new StringBuilder();
        StringBuilder full = new StringBuilder();
        String tagString = "";
        char tagChar = '/';
        int value;
        while ((value = reader.read()) != -1) {
            tagChar = (char) value;
            if (tagChar == '>') {
                String fullString = full.toString().toLowerCase();
                if (tagString.isEmpty()) {
                    tagString = fullString;
                }
                if (fullString.isEmpty()
                        || fullString.charAt(full.length() - 1) == '/' //self-closing tags will be ignored
                        || fullString.startsWith("!--") && fullString.endsWith("--")) { // HTML comments will be ignored
                    return "";
                } else {
                    return tagString;
                }
            }
            full.append(tagChar);
            if (tagChar == ' ' && tagString.isEmpty()) {
                tagString = tag.toString().toLowerCase();
            }
            if (tagString.isEmpty()) {
                tag.append(tagChar);
            }
        }
        return tagString; //if tag is closed, it will never run
    }
    
    
    /**
     * This method finds the first opening tag, this tag is needed to start the substantive eatTag method.
     *
     * @param reader
     * @return opening tag
     * @throws IOException
     */
    private String findOpeningTag(BufferedReader reader) throws IOException {
        int value;
        String openingTag = "";
        while ((value = reader.read()) != -1) {
            char character = (char) value;
            if (character == '<') {
                openingTag = buildTag(reader);
                return openingTag;
            }
        }
        return openingTag;
    }

    /**
     * This method add the got word to the Set which contains the words to be ignored.
     *
     * @param word
     */
    public void addSkipWord(String word) {
        storer.addSkipWord(word);
    }
}
