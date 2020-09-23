/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtoplistgui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static wordtoplistgui.WordCollector.LOG;

/**
 *
 * @author laszlop
 */
public class BasicFrame extends javax.swing.JFrame {
    
    public JTextArea startURLs;
    public JTextField threadCount;
    public DefaultListModel listModel = new DefaultListModel();
    public JList finishedURLs;
    public JTable result;
    public String[] col = {"word", "frequency"};
    public int TABLE_SIZE = 53;

    /**
     * Creates new form BasicFrame
     */
    public BasicFrame() {
        super("WORD TOPLIST CREATOR");
        initComponents();
        initExtraComponents();
        initFields();
        setSize(800, 1000);
    }
    
    /**
     * Display the found words and its frequencies on the frame
     * @param map 
     */
    public void displayResult(Map<String, Integer> map){
        result.removeAll();
        List<Map.Entry<String, Integer>> sortedList = sortWordsByFreq(map);
        int rows = Math.min(TABLE_SIZE, sortedList.size());
        String[][] content = new String[TABLE_SIZE][2];
        for (int i = 0; i < rows; i++) {
            content[i][0] = sortedList.get(i).getKey();
            content[i][1] = Integer.toString(sortedList.get(i).getValue());
        }
        result = new JTable(content, col);
        result.setBounds(450, 60, 300, 850);
        add(result);          
    }
    
    /**
     * Adds a finished URL and displays it.
     * @param finishedURL 
     */
    public void displayprocessedURLs(String finishedURL) {
        listModel.addElement(finishedURL);
    }
    
    /**
     * Creates a sorted list from the entries of the freq Map
     * @param map
     * @return 
     */
    private List<Map.Entry<String, Integer>> sortWordsByFreq(Map<String, Integer> map) {
        ArrayList<Map.Entry<String, Integer>> sortedList = new ArrayList<>(map.entrySet());
        Collections.sort(sortedList, new WordFreqComparator());
        return sortedList;
    }
        
    public void initCollectionClass(List<URL> urlList, int maxThread) throws Exception {
        Set<String> skipWords = new HashSet<>(Arrays.asList("and", "but", "for", "that", "the", "with", "www"));
        WordStore wordStoreFreq = new SorterByFrequency(this);
        WordCollection wordCollectionFreq = new WordCollection(urlList, wordStoreFreq, skipWords, maxThread, this);
        wordCollectionFreq.runThreads();
        wordCollectionFreq.print(10);
    }
    
    private void initFields() {
        
        startURLs = new JTextArea(10, 1);
        startURLs.setBounds(50, 60, 350, 350);
        add(startURLs);
        
        threadCount = new JTextField();
        threadCount.setBounds(50, 480, 140, 30);
        threadCount.setText("4");
        add(threadCount);
        
        finishedURLs = new JList(listModel);
        finishedURLs.setBounds(50, 560, 350, 350);
        add(finishedURLs);
        
        String[][] content = new String[1][2];
        result = new JTable(content, col);
        result.setBounds(450, 60, 300, 850);
        add(result); 
        
        setVisible(true);
    }
    
    
    private void initExtraComponents() {

        JLabel URLsToProcess = new JLabel("URLs to process:");
        URLsToProcess.setBounds(50, 20, 150, 30);
        add(URLsToProcess);
        
        JLabel threadLabel = new JLabel("Number of threads:");
        threadLabel.setBounds(50, 450, 150, 30);
        add(URLsToProcess);

        JLabel processedURLs = new JLabel("Processed URLs:");
        processedURLs.setBounds(50, 520, 150, 30);
        add(processedURLs);
        
        JLabel mostCommonWords = new JLabel("Most common words: ");
        mostCommonWords.setBounds(450, 20, 200, 30);
        add(mostCommonWords);        
               
        JButton button = new JButton("Do it");
        button.setBounds(250, 450, 150, 60);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<URL> urlList = new ArrayList<>();
                String[] URLs = startURLs.getText().split("\n");
                for (int i = 0; i < URLs.length; i++) {
                    try {
                        urlList.add(new URL(URLs[i]));
                    } catch (MalformedURLException ex) {
                        LOG.severe(URLs[i] + " is not a proper URL.");
                    }
                }
                int maxThread = 4;
                if (Character.isDigit(threadCount.getText().charAt(0))) {
                    maxThread = Integer.parseInt(threadCount.getText()); 
                    if (maxThread == 0) {
                        maxThread = 1;
                    }
                    threadCount.setText(Integer.toString(maxThread));
                } else {                  
                    threadCount.setBackground(Color.red);
                }    
                try {                
                    initCollectionClass(urlList, maxThread);
                } catch (Exception ex) {
                    LOG.severe("Application failed.");;
                }
            }
        }); 
        add(button);
    }


    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
