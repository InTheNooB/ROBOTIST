package app.ihm;

import static app.consts.Constants.ASSETS_FOLDER;
import static app.consts.Constants.IHM_BACKGROUND_COLOR;
import static app.consts.Constants.READABLE_SEQUENCE_FONT;
import static app.consts.Constants.UNREADABLE_SEQUENCE_FONT;
import app.consts.SequenceActionType;
import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author dingl01
 */
public class CustomCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -7799441088157759804L;
    private JLabel label;

    public CustomCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {

        label.setBorder(new EmptyBorder(10, 10, 10, 10));

        String txt = (String) value;
        String abv = txt.substring(0, 3);
        if (abv.equals("Unr")) {
            label.setFont(UNREADABLE_SEQUENCE_FONT);
            label.setText(txt);
            label.setIcon(new ImageIcon());
        } else {
            label.setFont(READABLE_SEQUENCE_FONT);
            txt = txt.substring(3);
            label.setText(" " + txt + "s");

            String path = "." + File.separator + ASSETS_FOLDER + File.separator;
            //SequenceActionType
            switch (getActionType(abv)) {
                case FWD:
                    label.setIcon(new ImageIcon(path + "robot_forward.png"));
                    break;
                case BWD:
                    label.setIcon(new ImageIcon(path + "robot_backward.png"));
                    break;
                case RGT:
                    label.setIcon(new ImageIcon(path + "robot_right.png"));
                    break;
                case LFT:
                    label.setIcon(new ImageIcon(path + "robot_left.png"));
                    break;
                case HDU:
                    label.setIcon(new ImageIcon(path + "robot_head_up.png"));
                    break;
                case HDD:
                    label.setIcon(new ImageIcon(path + "robot_head_down.png"));
                    break;
                case TPC:
                    label.setIcon(new ImageIcon(path + "camera.png"));
                    label.setText("");
                    break;
                default:
                    label.setIcon(new ImageIcon());
                    break;
            }
        }
        if (selected) {
            label.setBackground(IHM_BACKGROUND_COLOR.brighter());
        } else {
            label.setBackground(IHM_BACKGROUND_COLOR);
        }
        return label;
    }

    private SequenceActionType getActionType(String abv) {
        for (SequenceActionType pt : SequenceActionType.values()) {
            if (abv.equals(pt.name())) {
                return pt;
            }
        }
        return null;
    }
}
