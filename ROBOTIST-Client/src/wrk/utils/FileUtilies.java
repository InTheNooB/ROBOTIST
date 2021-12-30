package wrk.utils;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public abstract class FileUtilies {

    public static String IMAGES_DIRECTORY = "Pictures";

    /**
     * Opens the images directory within file explorer. A new Thread is created
     * to open the directory
     */
    public static void displayFileExplorer() {
        String userImageDirectory = System.getProperty("user.home") + File.separator + IMAGES_DIRECTORY;
        new Thread(() -> {

            try {
                Desktop.getDesktop().open(new File(userImageDirectory));
            } catch (IOException ex) {
                Logger.getLogger(FileUtilies.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, "Explorer Thread").start();

    }

    /**
     * Save image to the images directory with a naming convention. The naming
     * convention is : year.month.day-hours.minutes.seconds A new thread is
     * created to save the image
     *
     * @param img the image to save
     */
    public static void saveImage(BufferedImage img) {
        new Thread(() -> {
            String userImageDirectory = System.getProperty("user.home") + File.separator + "Pictures";
            String dateSep = ".";
            Calendar calendar = Calendar.getInstance(Locale.GERMAN);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            File outputfile = new File(userImageDirectory + File.separator + year + dateSep + month + dateSep + dayOfMonth + "-" + hourOfDay + dateSep + minute + dateSep + second + ".jpg");
            try {
                ImageIO.write(img, "jpg", outputfile);
            } catch (IOException ex) {
                Logger.getLogger(FileUtilies.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, "Save Image Thread").start();
    }

}
