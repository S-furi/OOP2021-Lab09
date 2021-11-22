package it.unibo.oop.lab.reactivegui02;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ConcurrentGUI {
    
    final JFrame frame = new JFrame();
    final JPanel pane;
    final JLabel text;
    final JButton up;
    final JButton down;
    final JButton stop;

    /**
     * Builds a new GUI.
     */
    public ConcurrentGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane = new JPanel();
        text = new JLabel();
        up = new JButton("Up");
        down = new JButton("Down");
        stop = new JButton("Stop");

        pane.add(text);
        pane.add(up);
        pane.add(down);
        pane.add(stop);
        
        final Agent agent = new Agent();
        new Thread(agent).start();

        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.changeSign(false);
            }
        });
        
        down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.changeSign(true);                
            }
        });
        
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.stopCount();
            }
        });
        

        this.frame.getContentPane().add(pane);
        this.frame.pack();
        this.frame.setVisible(true);
    }
    
    private class Agent implements Runnable {
        
        private boolean stop = false;
        private int count = 0;
        private boolean negative = false;

        @Override
        public void run() {
            while (!stop) {
                try {
                    if (!this.negative ) {
                        this.increment();
                    } else {
                        this.decrement();
                    }
                    ConcurrentGUI.this.text.setText(Integer.toString(this.count));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        }
        
        public void stopCount() {
            this.stop = true;
        }
        
        private int increment() {
            return this.count++;
        }
        
        
        /**
         * Set counter sign.
         * 
         * @param negative
         *          sign of the counter:
         *                  -true: set the counter negative
         *                  -false: set the counter positive
         *                  
         */
        public synchronized void changeSign(final boolean negative) {
            this.negative = negative;
        }
        
        private int decrement() {
            return this.count--;
        }
        
    }
}
