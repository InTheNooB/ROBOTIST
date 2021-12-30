package wrk.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

public abstract class ImageHandler {

    /**
     * Converts bytes array to a BufferedImage.
     *
     * @param bytes the array of bytes to convert
     * @return BufferedImage
     */
    public static BufferedImage bytesToImage(byte[] bytes) {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage image = null;
        try {
            image = ImageIO.read(is);
        } catch (IOException ex) {
        }
        return image;
    }

    /**
     * Converts a BufferedImage to a String.
     *
     * @param image the image to convert
     * @return String
     */
    public static String imageToString(BufferedImage image) {
        byte[] bytes = imageToBytes(image);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Converts a BufferedImage to a bytes array.
     *
     * @param image the image to convert
     * @return the bytes array
     */
    public static byte[] imageToBytes(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
        } catch (IOException ex) {
        }
        return baos.toByteArray();
    }

    /**
     * Converts a bytes array to String.
     *
     * @param bytes the array of bytes to convert
     * @return string
     */
    public static String bytesToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Convert a String to a BufferedImage.
     *
     * @param img the string to convert
     * @return BufferedImage
     */
    public static BufferedImage stringToImage(String img) {
        byte[] bytes = Base64.getDecoder().decode(img);
        return bytesToImage(bytes);
    }
}
