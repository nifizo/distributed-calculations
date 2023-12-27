package com.example;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThreadDemoApp {
    private JFrame frame;
    private JButton startButton;
    private JButton startButton1;
    private JButton startButton2;
    private JSpinner spinnerThread1;
    private JSpinner spinnerThread2;
    private JSlider sharedSlider;
    private ThreadManager threadManager;

    public ThreadDemoApp() {
        frame = new JFrame("thread app");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        startButton = new JButton("start");
        startButton1 = new JButton("start 1");
        startButton2 = new JButton("start 2");
        spinnerThread1 = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinnerThread2 = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        sharedSlider = new JSlider(0, 100);
        sharedSlider.setMajorTickSpacing(10);
        sharedSlider.setPaintTicks(true);
        sharedSlider.setPaintLabels(true);

        threadManager = new ThreadManager(sharedSlider);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startButton.getText().equals("start")) {
                    threadManager.createThread(1);
                    threadManager.createThread(2);
                    threadManager.startThreads(
                            (Integer) spinnerThread1.getValue(),
                            (Integer) spinnerThread2.getValue()
                    );
                    startButton.setText("stop");
                } else {

                    threadManager.stopThreads();
                    startButton.setText("start");
                }
            }
        });

        startButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startButton1.getText().equals("Start 1")) {
                    if (threadManager.getSemaphore() == 0) {

                        threadManager.setSemaphore(1);
                        threadManager.createThread(1);
                        threadManager.setThreadPriority(1, Thread.MIN_PRIORITY);
                        spinnerThread1.setValue(Thread.MIN_PRIORITY);

                        threadManager.startThread(1);
                        
                        startButton1.setText("Stop 1");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Occupied by thread");
                    }
                } else {
                    threadManager.setSemaphore(0);
                    threadManager.stopThread(1);
                    startButton1.setText("Start 1");
                }
            }
        });
        
        startButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startButton2.getText().equals("Start 2")) {
                    if (threadManager.getSemaphore() == 0) {
                        threadManager.setSemaphore(1);
                        threadManager.createThread(2);
                        threadManager.setThreadPriority(2, Thread.MAX_PRIORITY);
                        spinnerThread2.setValue(Thread.MAX_PRIORITY);
                        threadManager.startThread(2);
                        
                        startButton2.setText("Stop 2");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Occupied by thread");
                    }
                } else {
                    threadManager.setSemaphore(0);;
                    threadManager.stopThread(2);
                    startButton2.setText("Start 2");
                }
            }
        });  

        spinnerThread1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int newPriority = (int) spinnerThread1.getValue();
                threadManager.setThreadPriority(1, newPriority);
            }
        });

        spinnerThread2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int newPriority = (int) spinnerThread2.getValue();
                threadManager.setThreadPriority(2, newPriority);
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(sharedSlider);
        JPanel spinnerPanel = new JPanel();
        spinnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        spinnerPanel.add(new JLabel("Thread 1 Priority:"));
        spinnerPanel.add(spinnerThread1);
        spinnerPanel.add(new JLabel("Thread 2 Priority:"));
        spinnerPanel.add(spinnerThread2);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startButton);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(spinnerPanel);
        centerPanel.add(buttonPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(startButton1);
        bottomPanel.add(startButton2);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ThreadDemoApp();
            }
        });
    }
}
