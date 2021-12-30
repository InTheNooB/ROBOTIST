package ihm;


import static constants.Constants.IHM_BACKGROUND_COLOR;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;

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

        String txt = value.toString();
        String abv = txt.substring(0, 3);
        if (abv.equals("Unr")) {
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setText(txt);
            label.setIcon(new ImageIcon());
        } else {
            label.setFont(new Font("Arial", Font.BOLD, 20));
            txt = txt.substring(3);
            label.setText(" " + txt + "s");

            String path = "." + File.separator + "assets" + File.separator;
            switch (abv) {
                case "FWD":
                    label.setIcon(new ImageIcon(path + "robot_forward.png"));
                    break;
                case "BWD":
                    label.setIcon(new ImageIcon(path + "robot_backward.png"));
                    break;
                case "RGT":
                    label.setIcon(new ImageIcon(path + "robot_right.png"));
                    break;
                case "LFT":
                    label.setIcon(new ImageIcon(path + "robot_left.png"));
                    break;
                case "HDU":
                    label.setIcon(new ImageIcon(path + "robot_head_up.png"));
                    break;
                case "HDD":
                    label.setIcon(new ImageIcon(path + "robot_head_down.png"));
                    break;
                case "TPC":
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
}


