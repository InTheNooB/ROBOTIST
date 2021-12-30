package beans;

import java.util.ArrayList;

public class Sequence {

    private ArrayList<ActionSequence> actions;
    private String name;

    public Sequence(String name, ArrayList<ActionSequence> actions) {
        this.name = name;
        this.actions = actions;
    }

    /**
     * Converts list of sequence to String.
     *
     * @return string containing the converted sequence list
     */
    public String sequenceToTxt() {
        String result = name + "[";
        for (ActionSequence a : getActions()) {
            if (getActions().indexOf(a) != getActions().size() - 1) {
                result += a.getValue() + "," + a.getDelay() + ";";
            } else {
                result += a.getValue() + "," + a.getDelay();
            }

        }
        result += "]";
        return result;
    }

    //Setter Getter 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ActionSequence> getActions() {
        return actions;
    }

    public void setActions(ArrayList<ActionSequence> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return name;
    }

}
