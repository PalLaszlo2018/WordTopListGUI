/*
 * Copyright: SONY MUSIC ENTERTAINMENT 
 * Create Date: 2020.09.25. 
 */
package wordtoplistgui.GUI;

import static wordtoplistgui.WordTopListGUI.LOG;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import wordtoplistgui.CollectorManager;

/**
 * The GUI of the application
 * @author laszlop
 */
public class BasicFrame extends javax.swing.JFrame {

    private final CollectorManager manager;
    private JTextArea startURLs;
    private JTextField threadCount;
    private final DefaultListModel listModel = new DefaultListModel();
    private JList finishedURLs;
    private JTable result;
    private DefaultTableModel resultModel;
    private final int TABLE_SIZE = 53;
    private boolean update;

    /**
     * Creates new form BasicFrame
     */
    public BasicFrame (CollectorManager manager) {
        super("WORD TOPLIST CREATOR");
        initComponents();
        initExtraComponents();
        initFields();
        setSize(800, 1000);
        this.manager = manager;
    }
    
    /**
     * To refresh the fields
     */
    public void updateLater() {
        if (update) {
            return;
        }
        update = true;
        Runnable refresher = new Runnable() {
            @Override
            public void run() {
                update = false;
                displayResult();
                displayprocessedURLs();
                displayFinished();
            }
        };
        SwingUtilities.invokeLater(refresher);
    }

    /**
     * Displays the found words and its frequencies on the frame
     *
     * @param map
     */
    public void displayResult() {
        List<Map.Entry<String, Integer>> sortedList = manager.getSortedWords();
        int displayedRows = Math.min(TABLE_SIZE, sortedList.size());
        for (int row = 0; row < displayedRows; row++) {
            if (row >= resultModel.getRowCount()) {
                resultModel.addRow(new String[]{sortedList.get(row).getKey(), Integer.toString(sortedList.get(row).getValue())});
            } else {
                resultModel.setValueAt(sortedList.get(row).getKey(), row, 0);
                resultModel.setValueAt(Integer.toString(sortedList.get(row).getValue()), row, 1);
            }
        }
    }

    /**
     * Displays the full list of finished URLs
     *
     * @param finishedURL
     */
    public void displayprocessedURLs() {
        List<String> processedURLs = manager.getFinishedURLs();
        listModel.clear();
        for (int i = 0; i < processedURLs.size(); i++) {
            listModel.addElement(processedURLs.get(i));
        }
    }
    
    /**
     * after all URLs were processed, it prints a message to inform the user
     */
    public void displayFinished() {
        boolean allURLsFinished = manager.isFinished();
        if (allURLsFinished) {
            startURLs.setText("Processing finished");
        }
    }
    
    /**
     * sets the important fields of the CollectorManager
     * @param urlList
     * @param maxThread
     * @throws Exception 
     */
    public void setCollectorManager(List<URL> urlList, int maxThread) throws Exception {
        manager.setFrame(this);
        manager.setMaxThreads(maxThread);
        manager.setURLs(urlList);
        manager.runThreads();
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

        resultModel = new DefaultTableModel();
        resultModel.addColumn("Words");
        resultModel.addColumn("Frequency");
        result = new JTable(resultModel);
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
                    threadCount.setText(Integer.toString(maxThread));
                } else {
                    threadCount.setBackground(Color.red);
                }
                try {
                    setCollectorManager(urlList, maxThread);
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
