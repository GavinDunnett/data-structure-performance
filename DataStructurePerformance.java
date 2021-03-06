import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Individual Recommendation Report
 * 
 * Gavin Dunnett
 * 
 * ECS3390.5W1.21S
 * 
 * This program creates datasets that are used to measure the performance of the
 * Hashmap and TreeSet data structures.
 */

public class DataStructurePerformance {
    private static final int testMin = (int) 1E6; // test: min parameter
    private static final int testStep = (int) 1E6; // test: step parameter
    private static final int testMax = (int) 10E6; // test: max parameter
    private static final int dsNumLength = 7; // dataset: length of the numbers
    private static final int dsLowerBound = 0; // dataset: inclusive lower bound
    private static final int dsUpperBound = (int) Math.pow(10, dsNumLength); // dataset: exclusive upper bound
    private static int[] dsWhole = new int[dsUpperBound]; // dataset: stores the whole dataset
    private static List<int[]> dsSlices = new ArrayList<int[]>(); // dataset: slices of the whole dataset
    private static long start = 0; // used to measure time
    private static String filename = "results.txt"; // name of output file

    public static void main(String[] args) {

        // initialize the output file
        try (FileWriter out = new FileWriter(filename, false)) {
        } catch (IOException e) {
        }
        // create the whole dataset
        System.out.println("Populating the whole dataSet...");
        for (int i = dsLowerBound; i < dsUpperBound; i++)
            dsWhole[i] = i;
        System.out.println("\rPopulating completed.");

        // randomize the dataset
        System.out.println("\rRandom ordering the dataSet...");
        Random rand = new Random();
        for (int swaps = (dsWhole.length) / 2; swaps > 0; swaps--) {
            int a = swaps;
            int b = rand.nextInt(dsUpperBound);
            int temp = dsWhole[a];
            dsWhole[a] = dsWhole[b];
            dsWhole[b] = temp;
        }
        System.out.println("\rRandom ordering complete.");

        // populate the dsSlices array
        System.out.println("\rSlicing the dataset...");
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        write(df.format(new Date()) + "\n");
        write("Subset Size\t");
        for (int i = 0; i <= (testMax - testMin) / testStep; i++) { // number of slices
            int[] dsSlice = new int[testMin + (testStep * i)]; // declare this slice
            for (int j = 0; j < testMin + (testStep * i); j++) { // size of this slice
                dsSlice[j] = dsWhole[j];
            }
            dsSlices.add(dsSlice); // store this slice in the array of slices
            write(testMin + (testStep * i) + "\t");
        }
        write("\n");

        // begin hash map insert test
        write("Hash map insert\t");
        List<HashMap<Integer, Integer>> mapSlices = new ArrayList<>();
        for (int[] dsSilce : dsSlices) {
            HashMap<Integer, Integer> mapSlice = new HashMap<>(dsSilce.length);
            startTimer();
            for (int item : dsSilce)
                mapSlice.put(item, 0);
            stopTimer();
            mapSlices.add(mapSlice);
        }
        write("\n");

        // begin hash map find test
        write("Hash map find\t");
        for (HashMap<Integer, Integer> mapSlice : mapSlices) {
            startTimer();
            for (int i = 0; i < mapSlice.size(); i++)
                mapSlice.get(i);
            stopTimer();
        }
        write("\n");

        // begin hash map sort test
        write("Hash map sort\t");
        for (HashMap<Integer, Integer> mapSlice : mapSlices) {
            startTimer();
            TreeMap<Integer, Integer> sortedSlice = new TreeMap<Integer, Integer>(mapSlice);
            Iterator<Integer> itr = sortedSlice.keySet().iterator();
            while (itr.hasNext())
                itr.next();
            stopTimer();
        }
        write("\n");

        // begin hash map delete test
        write("Hash map delete\t");
        for (HashMap<Integer, Integer> mapSlice : mapSlices) {
            startTimer();
            for (int i = 0; i < mapSlice.size(); i++)
                mapSlice.remove(i);
            stopTimer();
        }
        write("\n");

        // begin TreeSet insert test
        write("Red-Black tree insert\t");
        List<TreeSet<Integer>> treeSlices = new ArrayList<>();
        for (int[] dsSlice : dsSlices) {
            TreeSet<Integer> treeSlice = new TreeSet<>();
            startTimer();
            for (int item : dsSlice)
                treeSlice.add(item);
            stopTimer();
            treeSlices.add(treeSlice);
        }
        write("\n");

        // begin TreeSet find test
        write("Red-Black tree find\t");
        for (TreeSet<Integer> treeSlice : treeSlices) {
            startTimer();
            for (int o = 0; o < treeSlice.size(); o++)
                treeSlice.contains(o);
            stopTimer();

        }
        write("\n");

        // begin TreeSet sort test
        write("Red-Black tree sort\t");
        for (TreeSet<Integer> treeSlice : treeSlices) {
            startTimer();
            for (int i = 0; i < treeSlice.size(); i++)
                treeSlice.first();
            stopTimer();
        }
        write("\n");

        // begin TreeSet delete test
        write("Red-Black tree delete\t");
        for (TreeSet<Integer> treeSlice : treeSlices) {
            startTimer();
            for (int o = 0; o < treeSlice.size(); o++)
                treeSlice.remove(o);
            stopTimer();
        }
        write("\n");
    }

    // Write an entry to the output file.
    private static void write(String str) {
        try (FileWriter out = new FileWriter(filename, true)) {
            out.write(str);
        } catch (IOException e) {
        }
        System.out.print(str);
    }

    // Start timer.
    private static void startTimer() {
        start = System.currentTimeMillis();
    }

    // Stop the timer and write the duration to the output file
    private static void stopTimer() {
        long duration = System.currentTimeMillis() - start;
        write(Long.toString(duration) + "\t");
    }
}