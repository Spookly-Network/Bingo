package de.nehlen.bingo.util.playerheads;

import de.nehlen.bingo.util.fonts.ComponentFont;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.player.SpooklyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Level;

public class PlayerheadChatComponent {
    public static Component getHeadComponent(Player player, Class<? extends ComponentFont> font) {
        SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player);
        byte[] bytes = Base64.getDecoder().decode(spooklyPlayer.textureUrl());
        JSONObject jsonObject = new JSONObject(new String(bytes));new JSONObject();
        String url = jsonObject.getJSONObject("textures").getJSONObject("SKIN").getString("url");
        try {
            BufferedImage imageToSend = processImageLayers(ImageIO.read(new URL(url)));
            return toImgComponent(toTextColorArray(imageToSend, 8), font);
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not create HeadComponent: " + e.getMessage());
            return Component.empty();
        }
    }

    private static TextColor[][] toTextColorArray(BufferedImage image, int height) {
        double ratio = ((double) image.getHeight() / image.getWidth());
        int width = (int) (height / ratio);
        if (width > 10)
            width = 10;
        TextColor[][] chatImg = new TextColor[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                TextColor.color(rgb);
                chatImg[x][y] = TextColor.color(rgb);
                ;
            }
        }
        return chatImg;
    }

    private static Component toImgComponent(TextColor[][] colors, Class<? extends ComponentFont> aClass) throws NoSuchFieldException, IllegalAccessException {
        Component component = Component.empty();
//        String[] lines = new String[(colors[0]).length];
        for (int y = 0; y < (colors[0]).length; y++) {
            Component line = Component.empty();
//            Component line = Component.empty();
            for (int x = 0; x < colors.length; x++) {
                TextColor color = colors[x][y];
//                line = line.append(Component.text((Character) aClass.getDeclaredField("PIXEL_" + (x + 1)).get(null)).color(color))
//                        .append(Component.text((Character) aClass.getDeclaredField("PIXEL_BACKSPACE").get(null)));
                line = line.append(Component.text((Character) aClass.getDeclaredField("PIXEL_" + (x + 1)).get(null)).color(color));
                if(x < 7)
                    line = line.append(Component.text((Character) aClass.getDeclaredField("PIXEL_BACKSPACE").get(null)));
            }
            component = component.append(line);
            if(y < 7)
                component = component.append(Component.text((Character) aClass.getDeclaredField("PIXEL_NEW_ROW").get(null)));
//            component = component.append(Component.newline());
//            line.append((Character) aClass.getDeclaredField("PIXEL_NEW_ROW").get(null));
//            lines[y] = String.valueOf(String.valueOf(line)) + ChatColor.RESET;
        }
        return component;
    }

    private static BufferedImage processImageLayers(BufferedImage bufferedImage) {
        ImageUtils imageUtils = new ImageUtils();
        BufferedImage headCrop = imageUtils.cropImageToHead(bufferedImage);
        BufferedImage overlayCrop = imageUtils.cropImageToHeadOverlay(bufferedImage);
        return imageUtils.rotateImage(imageUtils.combineImages(headCrop, overlayCrop), -90);
    }
}
