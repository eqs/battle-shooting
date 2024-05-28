package com.github.eqs;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.net.URL;
import java.net.URI;


public class Utils {
    public static File getResourceAsFile(String path) {
        try {
            URL url = Utils.class.getResource(path);
            URI uri = new URI(url.toString());
            return new File(uri);
        } catch (Exception e) {
            System.out.printf("画像 %s の読み込みに失敗 (%s)\n", path, e);
        }

        return null;
    }

    public static Image getResourceAsImage(String path) {
        try {
            return ImageIO.read(getResourceAsFile(path));
        } catch (Exception e) {
            System.out.printf("画像 %s の読み込みに失敗 (%s)\n", path, e);
        }

        return null;
    }
}
