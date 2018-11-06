import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.junit.Test;

import de.thl.jedunit.Constraints;
import de.thl.jedunit.Evaluator;

public class ConstraintsTest {

    @Test public void testConstraintProcessing() {
        ByteArrayOutputStream redirect = new ByteArrayOutputStream();
        PrintStream redirected = System.out;
        System.setOut(new PrintStream(redirect));

        Constraints checks = new Constraints() {
            @Override
            public void configure() {
                super.configure();
                String file = ClassLoader.getSystemClassLoader().getResource("Nightmare.java").getFile();
                Evaluator.EVALUATED_FILES = Arrays.asList(file);
                Constraints.ALLOWED_IMPORTS = Arrays.asList("java.io");
                Constraints.ALLOW_LOOPS = false;                 
                Constraints.ALLOW_METHODS = false;               
                Constraints.ALLOW_LAMBDAS = false;               
                Constraints.ALLOW_INNER_CLASSES = false;         
                Constraints.ALLOW_GLOBAL_VARIABLES = false;      
                Constraints.CHECK_COLLECTION_INTERFACES = true;  
                Constraints.ALLOW_CONSOLE_OUTPUT = false;
            }
        };
        checks.configure();
        checks.conventions();

        String console = redirect.toString();
        System.setOut(redirected);
        // System.out.println(console);
        assertFalse("Main method allowed", console.contains("Nightmare.java:13"));
        assertTrue("Method detection", console.contains("Nightmare.java:21:5: No methods"));
        assertTrue("Method detection", console.contains("Nightmare.java:25:5: No methods"));
        assertTrue("Method detection", console.contains("Nightmare.java:30:5: No methods"));

        assertTrue("Lambda detection", console.contains("Nightmare.java:17:13: lambda expression"));
        
        assertTrue("Collection interface return type detection", console.contains("Nightmare.java:21:5"));
        assertTrue("Collection interface parameter type detection", console.contains("Nightmare.java:25:41"));
        assertTrue("Collection interface variable declarator detection", console.contains("Nightmare.java:31:34"));
        
        assertTrue("for loop detection", console.contains("Nightmare.java:15:9: for loop"));
        assertTrue("forEach detection", console.contains("Nightmare.java:16:9: forEach"));
        
        assertTrue("Inner class detection", console.contains("Nightmare.java:35:5: Inner classes"));
        assertTrue("Inner class detection", console.contains("Nightmare.java:36:9: Inner classes"));
        assertTrue("Inner class detection", console.contains("Nightmare.java:39:5: Inner classes"));
        
        assertTrue("Console output detection", console.contains("Nightmare.java:42:9: Console output"));

        assertTrue("Global variable detection", console.contains("Nightmare.java:47"));
        assertFalse("Global variable detection", console.contains("Nightmare.java:45"));

        assertTrue("Import detection", console.contains("Nightmare.java:1:1: Import of"));
        assertTrue("Import detection", console.contains("Nightmare.java:2:1: Import of"));
        assertTrue("Import detection", console.contains("Nightmare.java:3:1: Import of"));
        assertTrue("Import detection", console.contains("Nightmare.java:4:1: Import of"));
        assertTrue("Import detection", console.contains("Nightmare.java:5:1: Import of"));
        assertTrue("Import detection", console.contains("Nightmare.java:6:1: Import of"));
    }

    @Test
    public void testCheatDetectionProcessing() {
        ByteArrayOutputStream redirect = new ByteArrayOutputStream();
        PrintStream redirected = System.out;
        System.setOut(new PrintStream(redirect));
        
        Constraints check = new Constraints() {
            @Override
            public void configure() {
                super.configure();
                String file = ClassLoader.getSystemClassLoader().getResource("Evil.java").getFile();
                Evaluator.EVALUATED_FILES = Arrays.asList(file);
                Evaluator.REALWORLD = false;
            }
        };
        check.configure();
        check.cheatDetection();

        String console = redirect.toString();
        System.setOut(redirected);
        
        // System.out.println(console);

        assertTrue("Cheat detection", console.contains("Evil.java:1:1: [CHEAT] Forbidden import"));
        assertTrue("Cheat detection", console.contains("Evil.java:2:1: [CHEAT] Forbidden import"));
        assertTrue("Cheat detection", console.contains("Evil.java:7:9: [CHEAT] Forbidden call"));
        assertTrue("Cheat detection", console.contains("Evil.java:8:16: [CHEAT] Forbidden call"));
    }
}