package de.thl.jedunit;

import java.io.*;

public class GradeScopeEvaluator extends Constraints {

    /**
     * Report the current points. Output points to file.
     */
    @Override
    public void grade() {
        File f = new File("/autograder/results/results.json");

        PrintWriter p = null;
        try {
            f.createNewFile();
            p = new PrintWriter(f);
        } catch (IOException e) {

        }
        p.write("{ \"score\":" + this.getPoints()/10.0 + "}");
        p.close();
    }
}
