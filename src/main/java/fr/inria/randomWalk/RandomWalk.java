/*
 * Copyright 2016 Djamel Edine YAGOUBI <djamel-edine.yagoubi@inria.fr>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;

/**
 *
 * @author Djamel Edine YAGOUBI <djamel-edine.yagoubi@inria.fr>
 */
public class RandomWalkTsG {

    public static String[] randomWalk(int length) {
        NormalDistribution n = new NormalDistribution(new JDKRandomGenerator(),0, 1); //mean 0 std 1 variance 1
        String[] ts = new String[length];
        double[] tstemp = new double[length];
        double[] e = new double[length - 1];

        for(int i = 0; i < e.length; i++) {
            e[i] = n.sample();
        }
        ts[0] = "0.0";
        for(int i = 1; i < length; i++) {
            ts[i] = (tstemp[i] = tstemp[i - 1] + e[i - 1])+"";
        }
        return ts;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {

        if(args.length < 2) {
            System.err.println("Usage: randomWalkTimeSeriesGenerator <FileName> <TimeSeriesNbr> <TimeSeriesSize>");
            System.exit(1);
        }

        CSVWriter writer = new CSVWriter(new FileWriter(args[0] + "_" + args[1]+ "_" + args[2] + ".csv"), CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER);

        for(int i = 1; i < Integer.parseInt(args[1]) + 2; i++) {
            String[] ts = randomWalk(Integer.parseInt(args[2]) + 1);
            ts[0] = Integer.toString(i);
            writer.writeNext(ts);
            writer.flushQuietly();
        }
        writer.close();
    }

}
