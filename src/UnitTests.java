//import static org.junit.Assert.*;
//import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class UnitTests {
    public static void main(String... args) {
        //System.exit(ucb.junit.textui.runClasses(UnitTests.class));
    }

    //@Test
    public void testTokenizeFile() throws IOException {
        /* Test normal text file. */
        File berkeley = new File("src/Testing/Berkeley");
        /* Test file with multiple delimiters. */
        File alameda = new File("src/Testing/Alameda");
        /* Test new lines and capitalization. */
        File coachella = new File("src/Testing/Coachella");

        /* Expected values. */
        TreeMap<String, Integer> bMap = new TreeMap<>();
        bMap.put("alec", 3);bMap.put("really", 1);bMap.put("wants", 1);bMap.put("to", 2);bMap.put("work", 1);bMap.put("at", 1);
        bMap.put("jetbrains", 1);bMap.put("loves", 1);bMap.put("intellij", 1);bMap.put("feels", 1);bMap.put("like", 1);
        bMap.put("he", 1);bMap.put("can", 1);bMap.put("contribute", 1);bMap.put("the", 1);bMap.put("goland", 1);bMap.put("team", 1);

        TreeMap<String, Integer> aMap = new TreeMap<>();
        aMap.put("alec", 3);aMap.put("xia", 2);

        TreeMap<String, Integer> cMap = new TreeMap<>();
        cMap.put("alec", 5);cMap.put("xia", 2);

        TreeMap<String, Integer> aOut = Main.tokenizeFile(alameda);
        for (String s : aOut.keySet()) {
            //assertEquals(aOut.get(s), aMap.get(s));
        }

        TreeMap<String, Integer> bOut = Main.tokenizeFile(berkeley);
        for (String s : bOut.keySet()) {
            //assertEquals(bOut.get(s), bMap.get(s));
        }

        TreeMap<String, Integer> cOut = Main.tokenizeFile(coachella);
        for (String s : cOut.keySet()) {
            //assertEquals(cOut.get(s), cMap.get(s));
        }

    }
}
