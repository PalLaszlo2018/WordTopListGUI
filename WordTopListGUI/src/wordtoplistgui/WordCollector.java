/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui;

import static wordtoplistgui.WordTopListGUI.LOG;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This class manages the processing of the got URLs, collects the words of the contents in a Collection.
 *
 * @author laszlop
 */
public class WordCollector implements Runnable {

    private final CollectorManager manager;

    public WordCollector(CollectorManager manager) {
        this.manager = manager;
    }
     
    /**
     * runs the threads
     */
    @Override
    public void run() {
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
                synchronized (manager.getLatch()) {
                    manager.getLatch().countDown();
                    LOG.info(Thread.currentThread().getName() + ": " + url.toString()
                            + " finished. The current size of the latch is: " + manager.getLatch().getCount());
                    manager.getFinishedURLs().add(url.toString());
                    if (manager.getLatch().getCount() == 0) {
                        manager.setFinished(true);
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
        URL url = manager.getUrlQueue().poll();
        LOG.info(Thread.currentThread().getName() + ": " + url + " was taken out from the queue, " + manager.getUrlQueue().size()
                + " URL-s remained.");
        return url;
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
            eatTag(openingTag, reader, true);
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
                    manager.storeWord(word);
                    manager.getFrame().updateLater();
                }
                String nextTagString = buildTag(reader);
                if (('/' + tag).equals(nextTagString)) {
                    return;
                }
                if (!manager.getSkipTags().contains(tag) && !nextTagString.startsWith("/")) {
                    boolean nextProcessable = processable && !manager.getSkipTags().contains(nextTagString);
                    eatTag(nextTagString, reader, nextProcessable);
                }
            }
            if (Character.isLetter(character)) {
                word.append(character);
            } else {
                if (processable) {
                    //manager.getStorer().store(word.toString().toLowerCase());
                    
                    manager.getFrame().updateLater();
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
 
}
