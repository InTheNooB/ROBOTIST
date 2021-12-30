package beans;

public class ActionSequence {

    private String value;
    private int delay;

    public ActionSequence(String value, int delay) {
        this.value = value;
        this.delay = delay;
    }

    public ActionSequence(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (delay == 0) {
            return value;
        } else {
            return value + " " + delay;

        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

}
