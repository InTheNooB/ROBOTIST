package app.utils;

/**
 *
 * @author dingl01
 */
public abstract class SequenceFileHandler {

    /**
     * Checks the syntax of a sequence file and determines if it contains error
     * or not. If the file is syntax is correct, returns true.
     *
     * @param s A String containing the list of actions
     * @return True if the syntax is correct, false otherwise.
     */
    public static boolean checkSequenceFileSyntax(String s) {
        if ((s == null) || (s.isEmpty())) {
            return false;
        }

        if ((!s.startsWith("[")) || (!s.endsWith("]"))) {
            return false;
        }
        s = s.replace("[", "").replace("]", "");

        if (s.endsWith(";") || s.endsWith(",")) {
            return false;
        }
        if (s.startsWith(";") || s.startsWith(",")) {
            return false;
        }

        String[] actions = s.split(";");

        if ((actions == null) || (actions.length == 0)) {
            return false;
        }
        
        for (String action : actions) {
            if ((action == null) || (action.isEmpty())) {
                return false;
            }

            String[] actionDetails = action.split(",");
            if ((actionDetails == null) || (actionDetails.length != 2)) {
                return false;
            }

            if (actionDetails[0].length() != 3) {
                return false;
            }

            try {
                Integer.parseInt(actionDetails[1]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
