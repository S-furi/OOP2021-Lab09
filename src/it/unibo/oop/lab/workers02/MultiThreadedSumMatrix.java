package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {
    
    private final int nthread;
    
    public MultiThreadedSumMatrix(final int nthread) {
        this.nthread = nthread;
    }

    @Override
    public double sum(final double[][] matrix) {
        /**
         * Numbers of how many rows every thread will work with.
         */
        final int partition = matrix.length / this.nthread + matrix.length % this.nthread;
        
        final List<Worker> workers = new ArrayList<>(this.nthread);
        for(int start = 0; start < matrix.length; start += partition) {
            workers.add(new Worker(matrix, start, partition));
        }
        
        for (final Worker worker : workers) {
            worker.start();
        }
        
        double sum = 0;
        
        for (final Worker worker : workers) {
            try {
                worker.join();
                sum += worker.getRes();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
        
        return sum;
    }
    
    private class Worker extends Thread {
        private double[][] matrix;
        private final int start;
        private final int nrows;
        private double res;
        
        /**
         * Constructor that defines a worker and its data structure (a
         * matrix sliced from the original with the same amount of cols 
         * and n rows calculated from the number of threads).
         * 
         * 
         * @param matrix
         *          the matrix to slice
         * @param start
         *          starting row to work with
         * @param nrows
         *          range of n rows to work with
         */
        Worker(final double[][] matrix, final int start, final int nrows) {
            super();
            int row = 0;
            this.matrix = new double[nrows][matrix[0].length];

            /**
             * Control for not going out of bounds with the last
             * thread when threadNum is > 3.
             */
            if (start + nrows > matrix.length) {
                this.nrows = matrix.length - start;
            } else {
                this.nrows = nrows;
            }

            for(int i = start; i < start + this.nrows; i++) {
                this.matrix[row] = matrix[i];
                row++;
            }

            this.start = start;
        }
        
        /**
         * Very slow process, but it gets the job done.
         */
        @Override
        public void run() {
            for(int i = 0; i < this.nrows; i++) {
                for (int j = 0; j < this.matrix[i].length; j++) {
                    this.res += this.matrix[i][j];
                }
            }
        }
        
        /**
         * 
         * @return sum
         *          sum of all the elements of the matrix
         */
        public double getRes() {
            return this.res;
        }
        
    }

}
