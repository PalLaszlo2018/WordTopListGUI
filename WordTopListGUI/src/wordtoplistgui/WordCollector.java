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

/**
 * This class manages the processing of the got URLs, collects the words of the contents in a Collection.
 * @author laszlop
 */
public class WordCollector implements Runnable {

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
     * runs the threads
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
            } catch (IOException ex) {
                LOG.severe("Processing of " + url.toString() + " failed.");
                LOG.warning(ex.getMessage());
            } finally {
                manager.releaseLatch(url.toString());
            }
        }
    }

    /**
     * Opens a reader for the got URL, finds the opening tag, and starts the substantive work by calling the eatTag method.
     * @param url
     * @throws IOException
     */
    public void processContent(@Nonnull URL url) throws IOException {
        LOG.info("Processing of the homepage " + url.toString() + " started.");
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String openingTag = findOpeningTag(reader);
            eatTag(openingTag, reader, true);
        } catch (IOException e) {
            LOG.severe("Processing of the homepage " + url.toString() + " failed.");
        } finally {
            reader.close();
            reader = null;
        }
    }

    /**
     * Reads the content of the URL, the found words will be put into a Map, found opening tags start the method recursive way,
     * found closing tags close the (sub)method.
     * @param tag that starts the text
     * @param reader open reader in its actual status
     * @throws IOException
     */
    private void eatTag(@Nonnull String tag, @Nonnull BufferedReader reader, @Nonnull boolean processable) throws IOException {
        int value;
        StringBuilder word = new StringBuilder();
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
    }

    /**
     * This method builds up the tag from the read characters.
     * @param reader open reader in its actual status
     * @return tag
     * @throws IOException
     */
    private String buildTag(@Nonnull BufferedReader reader) throws IOException {
        StringBuilder tag = new StringBuilder();
        StringBuilder full = new StringBuilder();
        String tagString = "";
        char tagChar = '/';
        int value;
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
        return tagString; //if tag is closed, it will never run
    }
    
    
    /**
     * This method finds the first opening tag, this tag is needed to start the substantive eatTag method.
     * @param reader open reader in its actual status
     * @return opening tag
     * @throws IOException
     */
    private String findOpeningTag(@Nonnull BufferedReader reader) throws IOException {
        int value;
        String openingTag = "";
        while ( (value = reader.read()) != -1 ) {
            char character = (char) value;
            if ( character == '<' ) {
                openingTag = buildTag(reader);
                return openingTag;
            }
        }
        return openingTag;
    }
 
}
