package com.algorithm.percolation;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(200, 100);

        System.out.println("Mean is " + percolationStats.mean());

        System.out.println("Standart deviation is " + percolationStats.stddev());

        System.out.println("95% confidence interval is [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}
