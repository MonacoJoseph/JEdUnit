package de.thl.jedunit;

import static de.thl.jedunit.DSL.*;

import java.io.*;

public class GradeScopeEvaluator extends Constraints {

    /**
     * Report the current points. Output points to file.
     */
    @Override
    public void grade() {
        File f = new File("/autograder/results/results.json");

        try{
            json.put("tests", ja);
            json.put("score", this.getPoints()/10.0);
        } catch(org.json.JSONException e) {}
        PrintWriter p = null;
        try {
            f.createNewFile();
            p = new PrintWriter(f);
        } catch (IOException e) {

        }
        p.write(json.toString());
        p.close();
    }
}
