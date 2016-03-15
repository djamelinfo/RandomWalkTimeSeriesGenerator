/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomwalk;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;

/**
 *
 * @author djamel
 */
public class RandomWalk {

    public static String[] randomWalk(int length) {
        NormalDistribution n = new NormalDistribution(new JDKRandomGenerator(),
                                                      0, 1); //mean 0 std 1 variance 1
        String[] ts = new String[length];
        double[] e = new double[length - 1];

        for(int i = 0; i < e.length; i++) {
            e[i] = (double) n.sample();
        }
        ts[0] = "0.0";
        for(int i = 1; i < length; i++) {
            ts[i] = (Double.
                    parseDouble(ts[i - 1]) + e[i - 1]) + "";
        }

        return ts;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {

        if(args.length < 2) {
            System.err.println(
                    "Usage: randomWalkTimeSeriesGenerator <FileName> <TimeSeriesNbr> <TimeSeriesSize>");
            System.exit(1);
        }

        CSVWriter writer = new CSVWriter(new FileWriter(args[0] + "_" + args[1]
                + "_" + args[2] + ".csv"), CSVWriter.DEFAULT_SEPARATOR,
                                         CSVWriter.NO_QUOTE_CHARACTER);

        for(int i = 1; i < Integer.parseInt(args[1]) + 2; i++) {
            String[] ts = randomWalk(Integer.parseInt(args[2]) + 1);

            ts[0] = Integer.toString(i);

            writer.writeNext(ts);
            writer.flushQuietly();

        }
        writer.close();
    }

}
