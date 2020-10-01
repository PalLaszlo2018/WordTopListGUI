/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui;

import static wordtoplistgui.WordTopListGUI.LOG;
import wordtoplistgui.sorters.SorterByFrequency;
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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import wordtoplistgui.GUI.ActionObserver;
import wordtoplistgui.GUI.DataProvider;


/**
 * This class creates the various threads that will collect the words in a common collection
 * @author laszlop
 */
public class CollectorManager implements ActionObserver, DataProvider {

    /**
     * Words that will be not stored (conjunctions, irrelevant words).
     */
    private final static Set<String> skipWords = new HashSet<>(Arrays.asList("and", "all", "but", "for", "from", "that", "the",
            "with", "www"));
    /**
     * Texts between these words will be generally ignored.
     */
    private final static Set<String> skipTags = new HashSet<>(Arrays.asList("head", "script", "style"));
    /**
     * The maximum number of threads.
     */
    private int maxThreads;
    /**
     * To store the collected words.
     */
    private final SorterByFrequency store = new SorterByFrequency();
    /**
     * The received URL-s to process will be transferred to this queue.
     */
    @Nonnull
    private BlockingQueue<URL> urlQueue;
    /**
     * This latch will be used to manage the threads, until it reaches 0, no the main thread will be blocked.
     */
    @Nonnull
    private CountDownLatch latch;
    /**
     * List of already processed URLs.
     */
    private final List<String> finishedURLs = new ArrayList<>();
    /**
     * To mark the end of the whole procedure.
     */
    private boolean finished;
    /**
     * To store the number of threads and the URLs.
     */
    @Nullable
    private CollectorSettings collectorSettings;
    /**
     * To observe the changes in the collection of stored words.
     * If null, nobody will be informed.
     */
    @Nullable
    private CollectorObserver collectorObserver;
     
    /**
     * Creates Threads and starts them.
     */    
    @Override
    public void doAction() {
        store.addSkipWords(skipWords);
        if ( collectorSettings != null ) {
            maxThreads = collectorSettings.getMaxThreads();
            Collection<URL> urlCollection = collectorSettings.getURLs();
            urlQueue = new ArrayBlockingQueue(urlCollection.size(), false, urlCollection);
            latch = new CountDownLatch(urlCollection.size());
        } else {
            throw new NullPointerException("CollectorSettings was not defined.");
        }
        List<Thread> threadList = new ArrayList<>();
        for ( int i = 0; i < maxThreads; i++ ) {
            threadList.add(new Thread(new WordCollector(this)));
            threadList.get(i).start();
            LOG.info("THREAD " + (i + 1) + " STARTED.");
        }
    }
    
    @Override
    public void deleteData() {
        store.deleteMap();
    }
    
    /**
     * stores the found word by calling the appropriate method of the store
     * @param charSequence to be stored
     */    
    void storeWord(@Nonnull CharSequence charSequence){
        if ( store.store(charSequence) && collectorObserver != null ) 
            collectorObserver.changed();

    }
    
    boolean isSkipTag(@Nonnull String str) {
        return skipTags.contains(str);
    }
    
    @Nullable
    synchronized URL takeURLfromQueue() {
        URL url = urlQueue.poll();
        LOG.info(Thread.currentThread().getName() + ": " + url + " was taken out from the queue, " + urlQueue.size()
                + " URL-s remained.");
        return url;
    }

    synchronized void releaseLatch(@Nonnull String urlString) {
        latch.countDown();
        LOG.info(Thread.currentThread().getName() + ": " + urlString + " finished. The current size of the latch is: "
                + latch.getCount());
        finishedURLs.add(urlString);
        if ( latch.getCount() == 0 )
            finished = true;
        if ( collectorObserver != null )
            collectorObserver.changed();
    }

        
    //===========GETTERS============
    
    /**
     * Delivers the stored words in List of DataStores format
     * @return the stored words in List of DataStores format
     */
    @Override
    public List<DataStore> getSortedWords() {
        List<Map.Entry<String, Integer>> entryList = store.sortedWordsByFreq();
        List<DataStore> dataList = new ArrayList<>();
        for ( int i = 0; i < entryList.size(); i++ ) {
            dataList.add(new DataStore(entryList.get(i).getKey(), entryList.get(i).getValue()));
        }
        return dataList;
    }
    
    /**
     * Delivers the List of already processed URLs
     * @return the List of already processed URLs
     */
    @Override
    public List<String> getFinishedURLs() {
        return finishedURLs;
    }
       
    /**
     * Tells whether the entire procedure is finished or not.
     * @return 
     */
    @Override
    public boolean isFinished() {
        return finished;
    }
   
          
    //==========SETTERS===========
    
   
    /**
     * Assigns the CollectorSettings to the instance.
     * @param collectorSettings to be assigned
     */
    public void setCollectorSettings(@Nullable CollectorSettings collectorSettings) {
        this.collectorSettings = collectorSettings;
    }
    
    /**
     * Assigns the CollectorObserver to the instance.
     * @param collectorObserver to be assigned
     */
    public void setCollectorObserver(@Nullable CollectorObserver collectorObserver) {
        this.collectorObserver = collectorObserver;
    }
    
    /**
     * allows setting the value to false for a new search
     * @param True / false
     */
    @Override
    public void setFinished(boolean b) {
        this.finished = b;
    }
    
    /**
     * Allows deleting finished URL-s for a new search.
     */
    @Override
    public void deleteFinishedURLs() {
        finishedURLs.clear();
    }

}

