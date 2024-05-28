package de.nehlen.bingo.util.playerheads;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public BufferedImage rotateImage(BufferedImage buffImage, double angle) {
        double radian = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radian));
        double cos = Math.abs(Math.cos(radian));

        int width = buffImage.getWidth();
        int height = buffImage.getHeight();

        int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
        int nHeight = (int) Math.floor((double) height * cos + (double) width * sin);

        BufferedImage rotatedImage = new BufferedImage(
                nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = rotatedImage.createGraphics();

        graphics.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
        // rotation around the center point
        graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
        graphics.drawImage(buffImage, 0, 0, null);
        graphics.dispose();

        return rotatedImage;
    }

    public BufferedImage combineImages(BufferedImage image, BufferedImage image2) {
        // create the new image, canvas size is the max. of both image sizes
        int w = Math.max(image.getWidth(), image2.getWidth());
        int h = Math.max(image.getHeight(), image2.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.drawImage(image2, 0, 0, null);

        g.dispose();
        return combined;
    }

    public BufferedImage cropImageToHead(BufferedImage originalImage) {
        return originalImage.getSubimage(8, 8, 8, 8);
    }

    public BufferedImage cropImageToHeadOverlay(BufferedImage originalImage) {
        return originalImage.getSubimage(40, 8, 8, 8);
    }
}
