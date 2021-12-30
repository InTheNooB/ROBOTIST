package app.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 *
 * @author dingl01
 */
public abstract class ConversionHandler {

    public static BufferedImage stringToImage(String s) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(s);
        return bytesToImage(bytes);
    }

    public static BufferedImage bytesToImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage image = null;
        try {
            image = ImageIO.read(is);
        } catch (IOException ex) {
            throw ex;
        }
        return image;
    }

    public static String imageToString(BufferedImage image) throws IOException {
        byte[] bytes = imageToBytes(image);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }

    public static String bytesToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static BufferedImage scaleImage(byte[] img, int targetWidth, int targetHeight) throws IOException {
        return scaleImage(bytesToImage(img), targetWidth, targetHeight);
    }

    public static BufferedImage scaleImage(BufferedImage img, int targetWidth, int targetHeight) {
        if (img == null) {
            return null;
        }
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;

        int w = img.getWidth();
        int h = img.getHeight();

        int prevW = w;
        int prevH = h;

        do {
            if (w > targetWidth) {
                w /= 2;
                w = (w < targetWidth) ? targetWidth : w;
            }

            if (h > targetHeight) {
                h /= 2;
                h = (h < targetHeight) ? targetHeight : h;
            }

            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }
            if (g2 == null) {
                return null;
            }

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }

        return ret;

    }

}
