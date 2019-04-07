package com.algorithm.percolation;


import java.util.Random;

public class PercolationStats {
    private int n;
    private int trials;
    private double[] arrLastOpen;

    public PercolationStats(int n, int trials){
        if(n <=0 || trials <= 0) {
            //throw
        }
        this.n = n;
        this.trials = trials;
        arrLastOpen = new double[trials];
        for (int i = 0; i < trials; i++) {
            Random rand = new Random();

            AlgorithmPercolation algorithmPercolation = new AlgorithmPercolation(n);

            while (!algorithmPercolation.percolation()) {
                int row = rand.nextInt(n);
                int col = rand.nextInt(n);
                if (!algorithmPercolation.isOpen(row, col)) {
                    algorithmPercolation.open(row, col);
                }
            }
            arrLastOpen[i] = ((double)algorithmPercolation.numberOfOpenSites() / (double)(n * n));
        }
    }    // perform trials independent experiments on an n-by-n grid
    public double mean(){
        double sum = 0;
        for(int i = 0; i < arrLastOpen.length; i++){
            sum += arrLastOpen[i];
        }

        return sum/trials;
    }                          // sample mean of percolation threshold
    public double stddev()                        // sample standard deviation of percolation threshold
    {
        double sum = 0;
        for(int i = 0; i < arrLastOpen.length; i++){
            sum += Math.pow(((double)arrLastOpen[i]) - mean(), 2);
        }
        return Math.sqrt(sum / arrLastOpen.length);
    }
    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return mean() - 1.96d/Math.sqrt(trials);
    }
    public double confidenceHi(){
        return mean() + 1.96d/Math.sqrt(trials);
    }
}
