package com.cursor;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {
    static long numOfOperations = 50L;
    static int numOfThreads = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        System.out.println("\nN = " + numOfOperations + "\n");
        System.out.println("Consistent execution");
        Date start = new Date();
        System.out.println("Result: " + FibonacciConsistent.getNumber(numOfOperations));
        Date finish = new Date();
        System.out.println("Time: " + (finish.getTime() - start.getTime()) + " milliseconds \n");

        System.out.println("Parallel execution, number of threads = " + numOfThreads);
        start = new Date();
        ForkJoinPool pool = ForkJoinPool.commonPool();
        FibonacciParallel fbP = new FibonacciParallel(numOfOperations);
        pool.invoke(fbP);
        System.out.println("Result: " + fbP.getRawResult());
        finish = new Date();
        System.out.println("Time: " + (finish.getTime() - start.getTime()) + " milliseconds \n");
    }


    static class FibonacciParallel extends RecursiveTask<Long> {
        private long n;
        private final long THRESHOLD = numOfOperations / numOfThreads;

        public FibonacciParallel(long n) {
            this.n = n;
        }

        @Override
        protected Long compute() {
            if (n < THRESHOLD) {
                return FibonacciConsistent.getNumber(n);
            } else {
                FibonacciParallel fb1 = new FibonacciParallel(n - 1);
                fb1.fork();
                FibonacciParallel fb2 = new FibonacciParallel(n - 2);
                return fb2.compute() + fb1.join();
            }
        }
    }


    static class FibonacciConsistent {

        public static Long getNumber(long n) {
            long n1 = 0, n2 = 0, n3 = 0;
            for (int i = 0; i < n; i++) {
                if (i <= 1) {
                    n1 = n2 = 1;
                    n3 = n1;
                } else {
                    n3 = n1 + n2;
                    n1 = n2;
                    n2 = n3;
                }
            }
            return n3;
        }

    }
}

