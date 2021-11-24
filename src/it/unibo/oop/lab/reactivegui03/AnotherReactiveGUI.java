package it.unibo.oop.lab.reactivegui03;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnotherReactiveGUI {

    final JFrame frame = new JFrame("Another Reactive GUI");
    final JLabel text = new JLabel();
    final JPanel pane = new JPanel();
    final JButton up = new JButton("Up");
    final JButton down = new JButton("Down");
    final JButton stop = new JButton("Stop");
    
    final Agent agent = new Agent();

    public AnotherReactiveGUI() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // adding components to the main pane
        pane.add(text);
        pane.add(up);
        pane.add(down);
        pane.add(stop);
        
        // running threads
        new Thread(this.agent).start();
        
        final Clock clock = new Clock();
        new Thread(clock).start();
        
        // adding buttons behaviour
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
        
        
        // adding components to frame and show it
        this.frame.getContentPane().add(pane);
        this.frame.pack();
        this.frame.setVisible(true);
    }
    
    private void stopEverything() {
        this.down.setEnabled(false);
        this.up.setEnabled(false);
        this.stop.setEnabled(false);
    }
    
    public class Agent implements Runnable {
        
        private boolean stop = false;
        private int count = 0;
        private boolean negative = false;
        
        @Override
        public void run() {
            while (!this.stop) {
                try {
                    if (!this.negative) {
                        this.increment();
                    } else {
                        this.decrement();
                    }
                    AnotherReactiveGUI.this.text.setText(Integer.toString(this.count));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        }
        
        private void stopCount() {
            this.stop = true;
        }

        // not respecting DRY...
        private int decrement() {
            return this.count--;
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
        private void changeSign(final boolean negative) {
            this.negative = negative;
        }
        
    }
    
    public class Clock implements Runnable {
        
        private int count;
        
        @Override
        public void run() {
            while (true){
                try {
                    count++;
                    // NOTE: the counter will be 98, because of threads delay
                    if (this.count == 100) {
                        AnotherReactiveGUI.this.agent.stopCount();
                        AnotherReactiveGUI.this.stopEverything();
                        return;
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
}
