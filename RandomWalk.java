/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomwalk;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;

/**
 *
 * @author djamel
 */
public class RandomWalk {

    public static double Mean(double[] data, int index1, int index2) throws Exception {

        if(index1 < 0 || index2 < 0 || index1 >= data.length
                || index2 >= data.length) {
            throw new Exception("Invalid index!");
        }

        if(index1 > index2) {
            int temp = index2;
            index2 = index1;
            index1 = temp;
        }

        double sum = 0;

        for(int i = index1; i <= index2; i++) {
            sum += data[i];
        }

        return sum / (index2 - index1);
    }

    public static double StdDev(double[] timeSeries) throws Exception {
        double mean = Mean(timeSeries, 1, timeSeries.length - 1);
        double var = 0.0;

        for(int i = 1; i < timeSeries.length; i++) {
            var += (timeSeries[i] - mean) * (timeSeries[i] - mean);
        }
        var /= (timeSeries.length - 2);

        return Math.sqrt(var);
    }

    public static double[] Z_Normalization(double[] timeSeries) throws Exception {
        double mean = Mean(timeSeries, 1, timeSeries.length - 1);
        double std = StdDev(timeSeries);

        double[] normalized = new double[timeSeries.length];

        if(std == 0) {
            std = 1;
        }

        for(int i = 1; i < timeSeries.length - 1; i++) {
            normalized[i] = (timeSeries[i] - mean) / std;
        }

        return normalized;
    }

    public static double[] randomWalk(int length) {
        NormalDistribution n = new NormalDistribution(new JDKRandomGenerator(), 0, 1); //mean 0 std 1 variance 1
        double[] ts = new double[length];
        double[] e = new double[length - 1];

        for(int i = 0; i < e.length; i++) {
            e[i] = n.sample();
        }
        ts[0] = 0;
        for(int i = 1; i < length; i++) {
            ts[i] = ts[i - 1] + e[i - 1];
        }

        return ts;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {

        if(args.length < 2) {
            System.err.println("Usage: randomWalkTimeSeriesGenerator <FileName> <TimeSeriesNbr> <TimeSeriesSize>");
            System.exit(1);
        }

        //BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/media/djamel/DATA/randomWalk/" + args[0] + "_" + args[1] + "_" + args[2] + ".csv")));
        CSVWriter writer = new CSVWriter(new FileWriter( args[0] + "_" + args[1] + "_" + args[2] + ".csv"), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);

        int c = 0;

        System.out.println("if you want a normalizes time series tap 1 if not tap 0");
        c = new Scanner(System.in).nextInt();

        if(c == 0) {
            for(int i = 1; i < Integer.parseInt(args[1]) + 2; i++) {
                double[] ts = randomWalk(Integer.parseInt(args[2]) + 1);

                //ts[0] = i;
                
                if(ts.length >= (Integer.parseInt(args[2]) + 1)) {
                    String[] tab = new String[ts.length];
                    for(int j = 1; j < ts.length; j++) {

                        tab[j] = String.format(Locale.US, "%.3f", ts[j]);

                    }

                    tab[0] = Integer.toString(i);
                    if(tab.length > 461) {
                        System.out.print("--"+tab.length);
                    }
                    

                    writer.writeNext(tab);
                    writer.flushQuietly();
                }else{
                    System.out.println(ts.length);
                }

               // writer.close();
                //System.out.println(ts.length);
//                String str = ts[0]+"";
//
//                for(int j = 1; j < ts.length; j++) {
//                    str += (","+ts[j] );
//                }
//
//                bw.write(str);
//                bw.newLine();
//                bw.flush();
            }
        } else {
            for(int i = 1; i < Integer.parseInt(args[1]) + 1; i++) {
                double[] ts = randomWalk(Integer.parseInt(args[2]) + 1);
                //ts[0] = i;
                double[] tsN = Z_Normalization(ts);

                String[] tab = new String[tsN.length];
                for(int j = 1; j < tsN.length; j++) {
                    tab[j] = String.format(Locale.US, "%.5f", tsN[j]);
                }

                tab[0] = Integer.toString(i);

                writer.writeNext(tab);
                //writer.close();

//                String str = tsN[0]+"";
//
//                for(int j = 1; j < tsN.length; j++) {
//                    str += (","+tsN[j]);
//                }
//
//                bw.write(str);
//                bw.newLine();
//                bw.flush();
            }

            writer.close();

        }

    }

}
