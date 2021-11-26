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
        
        Worker(final double[][] matrix, final int start, final int nrows) {
            super();
            int row = 0;
            this.matrix = new double[nrows][matrix[0].length];
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
        
        @Override
        public void run() {
            for(int i = 0; i < this.nrows; i++) {
                for (int j = 0; j < this.matrix[i].length; j++) {
                    this.res += this.matrix[i][j];
                }
            }
        }
        
        public double getRes() {
            return this.res;
        }
        
    }

}
