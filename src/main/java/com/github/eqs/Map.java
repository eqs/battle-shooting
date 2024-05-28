package com.github.eqs;

import java.awt.*;
import javax.swing.*;


public class Map {

    /** マップの横幅, 縦幅 */
    private int width, height;

    /** マップの背景 */
    private Image img;

    /**
     * コンストラクタ
     * @param width 横幅
     * @param height 縦幅
     * @param file 画像ファイルパス
     */
    public Map(int width, int height, String file) {
        this.width = width;
        this.height = height;
        loadImage(file);
    }

    /**
     * 画像を読み込む
     * @param file 読み込む画像ファイルのパス
     */
    protected void loadImage(String file) {
        img = Utils.getResourceAsImage(file);
    }

    /**
     * マップの横幅を取得
     * @return 横幅
     */
    public int getWidth() { return width; }

    /**
     * マップの縦幅を取得
     * @return 縦幅
     */
    public int getHeight() { return height; }

    /**
     * マップの背景画像を取得
     * @return 背景画像
     */
    public Image getBackimage() { return img; }
}
