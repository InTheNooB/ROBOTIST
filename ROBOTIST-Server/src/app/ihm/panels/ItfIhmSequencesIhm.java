package app.ihm.panels;

import app.beans.Sequence;
import java.util.List;

/**
 *
 * @author dingl01
 */
public interface ItfIhmSequencesIhm {

     void getSequenceList();

     List<Sequence> deleteSequence(Sequence sequence);

     void navigateBetweenIhm(String ihmName);
}
