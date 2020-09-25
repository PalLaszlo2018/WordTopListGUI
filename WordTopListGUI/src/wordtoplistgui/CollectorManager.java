/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui;

import static wordtoplistgui.WordTopListGUI.LOG;
import wordtoplistgui.sorters.SorterByFrequency;
import wordtoplistgui.GUI.BasicFrame;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;


/**
 * This class creates the various threads that will collect the words in a common collection
 *
 * @author laszlop
 */
public class CollectorManager {

    private int maxThreads;
    private final Set<String> skipWords = new HashSet<>(Arrays.asList("and", "but", "for", "that", "the", "with", "www"));
    private final Set<String> skipTags = new HashSet<>(Arrays.asList("head", "script", "style"));
    private final SorterByFrequency store = new SorterByFrequency();
    private BlockingQueue<URL> urlQueue;
    private CountDownLatch latch;
    private BasicFrame frame;
    private final List<String> finishedURLs = new ArrayList<>();
    private boolean finished;
   
   
    /**
     * Creates Threads and starts them
     */    
    public void runThreads() throws Exception {
        store.addSkipWords(skipWords);
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < maxThreads; i++) {
            threadList.add(new Thread(new WordCollector(this)));
            threadList.get(i).start();
            LOG.info("THREAD " + (i + 1) + " STARTED.");
        }
    }
    
    /**
     * stores the found word by calling the appropriate method of the store
     * 
     * @param charSequence 
     */    
    public void storeWord(CharSequence charSequence){
        store.store(charSequence);
    }
        
    //===========GETTERS============
    
    public List<Map.Entry<String, Integer>> getSortedWords() {
        return store.sortedWordsByFreq();
    }

    public List<String> getFinishedURLs() {
        return finishedURLs;
    }

    public Set<String> getSkipWords() {
        return skipWords;
    }

    public Set<String> getSkipTags() {
        return skipTags;
    }
    
    public BlockingQueue<URL> getUrlQueue() {
        return urlQueue;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public BasicFrame getFrame() {
        return frame;
    }

    public boolean isFinished() {
        return finished;
    }
   
          
    //==========SETTERS===========
    
    public void setURLs(Collection<URL> urlCollection) {
        urlQueue = new ArrayBlockingQueue(urlCollection.size(), false, urlCollection);
        latch = new CountDownLatch(urlCollection.size());
    }

    public void setMaxThreads(int maxThreads) {
        if (maxThreads < 1) {
            LOG.severe("The number of threads was too low (" + maxThreads + "), it was amended to 1.");
            maxThreads = 1;
        }
        this.maxThreads = maxThreads;
    }

    public void setFrame(BasicFrame frame) {
        this.frame = frame;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
   

}