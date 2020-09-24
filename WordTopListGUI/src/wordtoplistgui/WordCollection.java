/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtoplistgui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import static wordtoplistgui.WordCollector.LOG;

/**
 * This class creates the various threads that will collect the words in a common collection
 *
 * @author laszlop
 */
public class WordCollection {

    private int maxThreads;
    private final Set<String> skipWords = new HashSet<>(Arrays.asList("and", "but", "for", "that", "the", "with", "www"));
    private final SorterByFrequency storer = new SorterByFrequency();
    private BlockingQueue<URL> urlQueue;
    private CountDownLatch latch;
    private BasicFrame frame;
   
   
    /**
     * Creates Threads and starts them
     */    
    public void runThreads() throws Exception {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < maxThreads; i++) {
            threadList.add(new WordCollector(urlQueue, latch, skipWords, storer, frame));
            threadList.get(i).start();
            LOG.info("THREAD " + (i + 1) + " STARTED.");
        }
    }
    
    /**
     * Prints the full list of the found words.
     */
    public void print() {
        storer.print();
    }

    /**
     * Logs the n-sized top-list of the found words.
     *
     * @param n
     */
    public void print(int n) {
        storer.print(n);
    }
    
    //=========GETTERS============

    public SorterByFrequency getStorer() {
        return storer;
    }
    
    
    
    //==========SETTERS===========
    
    public void setURLs(List<URL> urlList) {
        urlQueue = new ArrayBlockingQueue(urlList.size(), false, urlList);
        latch = new CountDownLatch(urlList.size());
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public void setFrame(BasicFrame frame) {
        this.frame = frame;
    }
    
    
    
    

    
    

}
