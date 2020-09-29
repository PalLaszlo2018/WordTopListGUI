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
import wordtoplistgui.GUI.ActionObserver;
import wordtoplistgui.GUI.DataProvider;


/**
 * This class creates the various threads that will collect the words in a common collection
 *
 * @author laszlop
 */
public class CollectorManager implements ActionObserver, DataProvider {

    private int maxThreads;
    private final Set<String> skipWords = new HashSet<>(Arrays.asList("and", "but", "for", "that", "the", "with", "www"));
    private final Set<String> skipTags = new HashSet<>(Arrays.asList("head", "script", "style"));
    private final SorterByFrequency store = new SorterByFrequency();
    private BlockingQueue<URL> urlQueue;
    private CountDownLatch latch;
    private final List<String> finishedURLs = new ArrayList<>();
    private boolean finished;
    private CollectorSettings collectorSettings;
    private CollectorObserver collectorObserver;
   
    
    //Az ActionObserver-be nem kellenek a plusz metódusok. Helyette a CollectorSettings-ről kérd le a CollectorManager.doAction() metódusban.
    // A CollectorSettings-t implementálja a BasicFrame szóval meg fogjuk kapni ami nekünk kell.
   
    /**
     * Creates Threads and starts them
     */    
    @Override
    public void doAction() {
        store.addSkipWords(skipWords);
        maxThreads = collectorSettings.getMaxThreads();
        System.out.println(maxThreads);
        Collection<URL> urlCollection = collectorSettings.getURLs();
        System.out.println(urlCollection);
        urlQueue = new ArrayBlockingQueue(urlCollection.size(), false, urlCollection);
        System.out.println(urlQueue);
        latch = new CountDownLatch(urlCollection.size());
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
    void storeWord(CharSequence charSequence){
        if (store.store(charSequence)) {
            collectorObserver.changed();
        }
    }
    
    boolean isSkipTag(String str) {
        return skipTags.contains(str);
    }
    
    /**
     * Takes out the next URL form the queue thread safe way
     *
     * @return next URL
     */
    synchronized URL takeURLfromQueue() {
        URL url = urlQueue.poll();
        LOG.info(Thread.currentThread().getName() + ": " + url + " was taken out from the queue, " + urlQueue.size()
                + " URL-s remained.");
        return url;
    }

    synchronized void decreaseLatch(String urlString) {
        latch.countDown();
        LOG.info(Thread.currentThread().getName() + ": " + urlString + " finished. The current size of the latch is: "
                + latch.getCount());
        finishedURLs.add(urlString);
        if (latch.getCount() == 0) {
            finished = true;
        }
        collectorObserver.changed();
    }

        
    //===========GETTERS============
    
    @Override
    public List<DataStore> getSortedWords() {
        List<Map.Entry<String, Integer>> entryList = store.sortedWordsByFreq();
        List<DataStore> dataList = new ArrayList<>();
        for (int i = 0; i < entryList.size(); i++) {
            dataList.add(new DataStore(entryList.get(i).getKey(), entryList.get(i).getValue()));
        }
        return dataList;
    }
    
    @Override
    public List<String> getFinishedURLs() {
        return finishedURLs;
    }
    
    public BlockingQueue<URL> getUrlQueue() {
        return urlQueue;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
   
          
    //==========SETTERS===========

    public void setMaxThreads(int maxThreads) {
        if (maxThreads < 1) {
            LOG.severe("The number of threads was too low (" + maxThreads + "), it was amended to 1.");
            maxThreads = 1;
        }
        this.maxThreads = maxThreads;
    }

    public void setCollectorSettings(CollectorSettings collectorSettings) {
        this.collectorSettings = collectorSettings;
    }

    public void setCollectorObserver(CollectorObserver collectorObserver) {
        this.collectorObserver = collectorObserver;
    }
}

