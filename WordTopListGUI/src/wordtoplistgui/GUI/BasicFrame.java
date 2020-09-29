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
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import wordtoplistgui.CollectorObserver;
import wordtoplistgui.CollectorSettings;
import wordtoplistgui.DataStore;

/**
 * The GUI of the application
 * @author laszlop
 */
public class BasicFrame extends javax.swing.JFrame implements CollectorSettings, CollectorObserver{

    private ActionObserver actionObserver;
    private DataProvider dataProvider;
    private JTextArea startURLs;
    private JTextField threadCount;
    private final DefaultListModel listModel = new DefaultListModel();
    private JList finishedURLs;
    private JTable result;
    private DefaultTableModel resultModel;
    private final int TABLE_SIZE = 53;
    private volatile boolean update;
    private ResourceBundle labels;
    private int maxThreads;
    private List<URL>URLs = new ArrayList<>();

    @Override
    public int getMaxThreads() {
        return maxThreads;
    }

    @Override
    public Collection<URL> getURLs() {
        return URLs;
    }

    @Override
    public void changed() {
        updateLater();
    }
    
    /**
     * Inner class for refreshing the page 
     */     
    class Refresher implements Runnable {

        @Override
        public synchronized void run() {
            update = false;
            displayResult();
            displayprocessedURLs();
            displayFinished();
        }
    }

    /**
     * Creates new form BasicFrame
     */
    public BasicFrame() {
        super("WORD TOPLIST CREATOR");
        Locale locale = new Locale("en", "US");
        labels = ResourceBundle.getBundle("wordtoplistgui.GUI.frameLabels", locale);
        initComponents();
        initExtraComponents();
        initFields();
        setSize(800, 1000);
    }
    
    /**
     * To refresh the fields
     */
    private void updateLater() {
        if (update) {
            return;
        }
        update = true;
        Runnable refresher = new Refresher();
        SwingUtilities.invokeLater(refresher);
    }
    
    /**
     * Displays the found words and its frequencies on the frame
     *
     * @param map
     */
    void displayResult() {
        List<DataStore> sortedList = dataProvider.getSortedWords();
        int displayedRows = Math.min(TABLE_SIZE, sortedList.size());
        int rowsInModel = resultModel.getRowCount();
        if (displayedRows > rowsInModel) {
            for (int i = 0; i < displayedRows - rowsInModel; i++) {
                resultModel.addRow(new Object[]{"", ""});
            }
        }
        for (int row = 0; row < displayedRows; row++) {
            DataStore nextElem = sortedList.get(row);
            resultModel.setValueAt(nextElem.getWord(), row, 0);
            resultModel.setValueAt(nextElem.getValue(), row, 1);
        }
    }

    /**
     * Displays the full list of finished URLs
     *
     * @param finishedURL
     */
    void displayprocessedURLs() {
        List<String> processedURLs = dataProvider.getFinishedURLs();
        if (processedURLs.size() > listModel.size()) {
            for (int i = listModel.size(); i < processedURLs.size(); i++) {
                listModel.addElement(processedURLs.get(i));
            }
        }
    }
    
    /**
     * after all URLs were processed, it prints a message to inform the user
     */
    void displayFinished() {
        boolean allURLsFinished = dataProvider.isFinished();
        if (allURLsFinished) {
            startURLs.setText("Processing finished");
        }
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

        JLabel URLsToProcess = new JLabel(labels.getString("URLtoProcessLabel"));
        URLsToProcess.setBounds(50, 20, 150, 30);
        add(URLsToProcess);

        JLabel threadLabel = new JLabel(labels.getString("threadCount"));
        threadLabel.setBounds(50, 450, 150, 30);
        add(threadLabel);

        JLabel processedURLs = new JLabel(labels.getString("proseccedURLsLabel"));
        processedURLs.setBounds(50, 520, 150, 30);
        add(processedURLs);
        
        JLabel mostCommonWords = new JLabel(labels.getString("resultLabel"));
        mostCommonWords.setBounds(450, 20, 200, 30);
        add(mostCommonWords);

        JButton button = new JButton(labels.getString("doitLabel"));
        button.setBounds(250, 450, 150, 60);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processUserInput();
            }
        });
        add(button);
    }
    
    private void processUserInput() {
        String[] URLStrings = startURLs.getText().split("\n");
        for (int i = 0; i < URLStrings.length; i++) {
            try {
                URLs.add(new URL(URLStrings[i]));
            } catch (MalformedURLException ex) {
                LOG.severe(URLStrings[i] + " is not a proper URL.");
            }
        }
        maxThreads = 4;
        if (Character.isDigit(threadCount.getText().charAt(0))) {
            maxThreads = Integer.parseInt(threadCount.getText());
            threadCount.setText(Integer.toString(maxThreads));
        } else {
            threadCount.setBackground(Color.red);
        }
        try {
            actionObserver.doAction();
        } catch (Exception ex) {
            LOG.severe("Application failed.");;
        }
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

    //================SETTERS===================

    
    public void setActionObserver(ActionObserver actionObserver) {
        this.actionObserver = actionObserver;
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }    
}
