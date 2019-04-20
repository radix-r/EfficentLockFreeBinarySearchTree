import org.jfree.ui.RefineryUtilities;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThroughputTest {

    private static final int TIMES_TO_AVG = 100;
    private static final int TIMES_TO_RUN = 1000;


    public static void main(String[] args){

        // Throughput testing

        // structure , list of points
        Map<String, List<long[]>> dataSets = new HashMap<>();



        // add
        dataSets = addTest(TIMES_TO_AVG);
        // graph
        String title = "Add Throughput";
        MultipleLinesChart ch1 = new MultipleLinesChart(title,title,dataSets);
        ch1.pack();
        RefineryUtilities.centerFrameOnScreen(ch1);
        ch1.setVisible(true);

        // contains
        //dataSets = containsTest(TIMES_TO_AVG);
        // graph
        /*
        title = "Contains Throughput";
        MultipleLinesChart ch2 = new MultipleLinesChart(title,title,dataSets);
        ch1.pack();
        RefineryUtilities.centerFrameOnScreen(ch1);
        ch1.setVisible(true);
        */
        // remove


    }

    private static Map<String, List<long[]>> addTest(int repAmt){
        Map<String, List<long[]>> dataSets = new HashMap<>();

        List<long[]> lfData = new ArrayList<>();
        List<long[]>stmData = new ArrayList<>();

        // test from 0 to 32 threads
        for (int i = 1; i < 32; i++) {


            long duration = 0;
            long endTime;
            long startTime;
            ExecutorService pool;

            // lf add
            for (int rep = 0; rep < repAmt; rep++) {

                LockFreeBST lf = new LockFreeBST();




                // time add funcs
                // limit thread count
                pool = Executors.newFixedThreadPool(i);

                // start timer
                startTime = System.nanoTime();


                for(int th = 0; th < 32; th++){
                    pool.execute(new lfAdd(lf,TIMES_TO_RUN));
                }

                pool.shutdown();
                endTime = System.nanoTime();
                duration += (endTime - startTime) ;  //nano seconds.


            }
            long numOps = 32*TIMES_TO_RUN*repAmt;
            long throughput = (numOps*1000000)/(duration); // throughput in ops per nano sec
            lfData.add(new long[]{(long)i,throughput});

            duration = 0;

            // STM add
            for (int rep = 0; rep < repAmt; rep++) {


                STMBST stm = new STMBST();



                // time add funcs
                pool = Executors.newFixedThreadPool(i);

                // start timer
                startTime = System.nanoTime();

                for(int th = 0; th < 32; th++){
                    pool.execute(new stmAdd(stm,TIMES_TO_RUN));
                }

                pool.shutdown();
                endTime = System.nanoTime();
                duration += (endTime - startTime) ;  //nano seconds.


            }
            numOps = 32*TIMES_TO_RUN*repAmt;
            throughput = (numOps*1000000)/duration; // throughput in ops per milli sec
            stmData.add(new long[]{(long)i,throughput});

            duration = 0;






        }
        dataSets.put("LF", lfData);
        dataSets.put("STM",stmData);
        return dataSets;

    }

    private static Map<String, List<long[]>> containsTest(int repAmt){
        Map<String, List<long[]>> dataSets = new HashMap<>();

        List<long[]> lfData = new ArrayList<>();
        List<long[]>stmData = new ArrayList<>();

        // test from 0 to 32 threads
        for (int i = 1; i < 32; i++) {


            long duration = 0;
            long endTime;
            long startTime;
            ExecutorService pool;

            // lf add
            for (int rep = 0; rep < repAmt; rep++) {

                LockFreeBST lf = new LockFreeBST();




                // time add funcs
                // limit thread count
                pool = Executors.newFixedThreadPool(i);

                // start timer
                startTime = System.nanoTime();


                for(int th = 0; th < 16; th++){
                    pool.execute(new lfAdd(lf,TIMES_TO_RUN));
                }

                pool.shutdown();
                endTime = System.nanoTime();
                duration += (endTime - startTime) ;  //nano seconds.


            }
            long numOps = 32*TIMES_TO_RUN*repAmt;
            long throughput = (numOps*1000000)/(duration); // throughput in ops per nano sec
            lfData.add(new long[]{(long)i,throughput});

            duration = 0;

            // STM add
            for (int rep = 0; rep < repAmt; rep++) {


                STMBST stm = new STMBST();



                // time add funcs
                pool = Executors.newFixedThreadPool(i);

                // start timer
                startTime = System.nanoTime();

                for(int th = 0; th < 32; th++){
                    pool.execute(new stmAdd(stm,TIMES_TO_RUN));
                }

                pool.shutdown();
                endTime = System.nanoTime();
                duration += (endTime - startTime) ;  //nano seconds.


            }
            numOps = 32*TIMES_TO_RUN*repAmt;
            throughput = (numOps*1000000)/duration; // throughput in ops per milli sec
            stmData.add(new long[]{(long)i,throughput});

            duration = 0;






        }
        dataSets.put("LF", lfData);
        dataSets.put("STM",stmData);
        return dataSets;

    }


    static class bstAdd implements Runnable{
        BST bst;
        int n;

        bstAdd(BST bst,int n){
            this.n = n;
            this.bst = bst;
        }

        private void addNTimes(int n){
            for(int i = 0; i<n;i++){
                bst.insert(i);
            }
        }
        public void run(){
            addNTimes(n);
        }
    }

    static class lfAdd implements Runnable{
        int n;
        LockFreeBST lf;
        SplittableRandom rand = new SplittableRandom();

        lfAdd(LockFreeBST lf,int n){
            this.lf = lf;
            this.n=n;
        }

        private void addNTimes(int n){
            for(int i = 0; i<n;i++){
                lf.add(rand.nextInt());
            }
        }

        public void run(){
            addNTimes(n);
        }

    }

    static class stmAdd implements Runnable{
        int n;
        STMBST stm;
        SplittableRandom rand = new SplittableRandom();

        stmAdd(STMBST stm,int n){
            this.n=n;
            this.stm = stm;
        }

        private void addNTimes(int n){
            for(int i = 0; i<n;i++){
                stm.insert(rand.nextInt());
            }
        }

        public void run(){
            addNTimes(n);
        }
    }


}
