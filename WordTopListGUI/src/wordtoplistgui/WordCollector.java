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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class manages the processing of the got URLs, collects the words of the contents in a Collection.
 * @author laszlop
 */
public class WordCollector implements Runnable {

    /**
     * The collector manager, who runs the threads, and collects the results.
     */
    @Nonnull
    private final CollectorManager manager;
    @Nonnull
    private BufferedReader reader;

    /**
     * creates a new instance with the given CollectorManager
     * @param manager to be assigned
     */
    public WordCollector(CollectorManager manager) {
        this.manager = manager;
    }
     
    /**
     * Runs the threads.
     */
    @Override
    public void run() {
        while ( true ) {
            URL url = manager.takeURLfromQueue();
            if ( url == null ) {
                LOG.info(Thread.currentThread().getName() + ": No more URL in the queue. Current thread terminates!");
                return;
            }
            try {
                processContent(url);
            } finally {
                manager.releaseLatch(url.toString());
            }
        }
    }

    /**
     * Opens a reader for the got URL, finds the opening tag, and starts the substantive work by calling the eatTag method.
     * @param url
     */
    public void processContent(@Nonnull URL url) {
        LOG.info("Processing of the homepage " + url.toString() + " started.");
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String openingTag = findOpeningTag(reader);
            eatTag(openingTag, reader, true);
        } catch (IOException e) {
            LOG.severe("Processing of the homepage " + url.toString() + " failed.");
            throw new RuntimeException("Processing of the homepage " + url.toString() + " failed.");
        } finally {
            if ( reader != null ) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    LOG.severe(url.toString() + " could not be closed.");
                }
            reader = null;
            }
        }
    }

    /**
     * Reads the content of the URL, the found words will be put into a Map, found opening tags start the method recursive way,
     * found closing tags close the (sub)method.
     * @param tag that starts the text
     * @param reader open reader in its actual status
     */
    private void eatTag(@Nonnull String tag, @Nonnull BufferedReader reader, @Nonnull boolean processable) {
        int value;
        StringBuilder word = new StringBuilder();
        try {
            while ( (value = reader.read()) != -1 ) {
                char character = (char) value;
                if ( character == '<' ) {
                    if ( processable )
                        manager.storeWord(word);
                    String nextTagString = buildTag(reader);
                    if ( ('/' + tag).equals(nextTagString) )
                        return;
                    if ( !manager.isSkipTag(tag) && !nextTagString.startsWith("/") ) {
                        boolean nextProcessable = processable && !manager.isSkipTag(nextTagString);
                        eatTag(nextTagString, reader, nextProcessable);
                    }
                }
                if ( Character.isLetter(character) )
                    word.append(character);
                else {
                    if ( processable )
                        manager.storeWord(word);
                    word.setLength(0);
                }
            }
        } catch (IOException ex) {
            LOG.severe("Reading failed.");
            throw new RuntimeException("Reading failed.");
        }
    }

    /**
     * This method builds up the tag from the read characters.
     * @param reader open reader in its actual status
     * @return tag
     */
    private String buildTag(@Nonnull BufferedReader reader) {
        StringBuilder tag = new StringBuilder();
        StringBuilder full = new StringBuilder();
        String tagString = "";
        char tagChar = '/';
        int value;
        try {
            while ( (value = reader.read()) != -1 ) {
                tagChar = (char) value;
                if ( tagChar == '>' ) {
                    String fullString = full.toString().toLowerCase();
                    if ( tagString.isEmpty() )
                        tagString = fullString;
                    if ( fullString.isEmpty()
                            || fullString.charAt(full.length() - 1) == '/' //self-closing tags will be ignored
                            || fullString.startsWith("!--") && fullString.endsWith("--") )  // HTML comments will be ignored
                        return "";
                    else
                        return tagString;
                }
                full.append(tagChar);
                if ( tagChar == ' ' && tagString.isEmpty() )
                    tagString = tag.toString().toLowerCase();
                if ( tagString.isEmpty() )
                    tag.append(tagChar);
            }
        } catch (IOException ex) {
            LOG.severe("Reading failed.");
            throw new RuntimeException("Reading failed.");
        }
        return tagString; //if tag is closed, it will never run
    }
    
    
    /**
     * This method finds the first opening tag, this tag is needed to start the substantive eatTag method.
     * @param reader open reader in its actual status
     * @return opening tag
     */
    @Nullable
    private String findOpeningTag(@Nullable BufferedReader reader) {
        int value;
        String openingTag = "";
        try {
            while ( (value = reader.read()) != -1 ) {
                char character = (char) value;
                if ( character == '<' ) {
                    openingTag = buildTag(reader);
                    return openingTag;
                }
            }
        } catch (IOException ex) {
            LOG.severe("Reading failed.");
            throw new RuntimeException("Reading failed.");
        }
        return openingTag;
    }
 
}
