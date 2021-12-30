package app.utils;

import java.util.ArrayList;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;



/**
 *
 * @author dingl01
 */
public class SequenceFileHandlerTest {

  
    //      INPUT                                       OUTPUT
    //      ==================================================
    //      "[FWD,2;LFT,1;BWD,5;TPC,0;HDU,1;RGT,2]"     true
    //      null                                        false
    //      ""                                          false
    //      "["                                         false
    //      "]"                                         false
    //      "[]"                                        false
    //      "]["                                        false
    //      "[asd]"                                     false
    //      "[FWD,2a]"                                  false
    //      "[FWDD,2]"                                  false
    //      "[FWD,2;]"                                  false
    //      "[;FWD,2]"                                  false
    //      "[,FWD,2]"                                  false
    //      "[FWD,2,]"                                  false
    //      "[FWD,]"                                    false
    //      "[,2]"                                      false
    //      "[,ABC]"                                    false
    //      "[,FWD]"                                    false
    //      "[FWD,1;BWD,,2]"                            false
    //      "[FWD,1;;BWD,2]"                            false
    //      "[FWD,1;;]"                                 false
    //      "[FWD,,1;]"                                 false
    //      "[FWD,100000000000000000000000000000]"      false
    //      "[FWD,1.0]"                                 false
    //      "[;]"                                       false
    //      "[,]"                                       false
    @Test
    public void testCheckSequenceFileSyntax() {
        String correctSyntax = "[FWD,2;LFT,1;BWD,5;TPC,0;HDU,1;RGT,2]";
        ArrayList<String> wrongSyntaxes = new ArrayList();
        wrongSyntaxes.add(null);
        wrongSyntaxes.add("");
        wrongSyntaxes.add("[");
        wrongSyntaxes.add("]");
        wrongSyntaxes.add("[]");
        wrongSyntaxes.add("][");
        wrongSyntaxes.add("[asd]");
        wrongSyntaxes.add("[FWD,2a]");
        wrongSyntaxes.add("[FWDD,2]");
        wrongSyntaxes.add("[FWD,2;]");
        wrongSyntaxes.add("[;FWD,2]");
        wrongSyntaxes.add("[,FWD,2]");
        wrongSyntaxes.add("[FWD,2,]");
        wrongSyntaxes.add("[FWD,]");
        wrongSyntaxes.add("[,2]");
        wrongSyntaxes.add("[,ABC]");
        wrongSyntaxes.add("[,FWD]");
        wrongSyntaxes.add("[FWD,1;BWD,,2]");
        wrongSyntaxes.add("[FWD,1;;BWD,2]");
        wrongSyntaxes.add("[FWD,1;;]");
        wrongSyntaxes.add("[FWD,,1;]");
        wrongSyntaxes.add("[FWD,100000000000000000000000000000]");
        wrongSyntaxes.add("[FWD,1.0]");
        wrongSyntaxes.add("[;]");
        wrongSyntaxes.add("[,]");

        boolean testOK = SequenceFileHandler.checkSequenceFileSyntax(correctSyntax);
        for (String wrongSyntaxe : wrongSyntaxes) {
            boolean newResult = SequenceFileHandler.checkSequenceFileSyntax(wrongSyntaxe);
            testOK = (newResult) ? false : testOK;
            System.out.println("Testing \"" + wrongSyntaxe + "\" -> " + newResult);
        }
        assertTrue(testOK);
    }

}
