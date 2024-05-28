package com.github.eqs;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Bullet extends Sprite {

    /** 画像 */
    private static Image[] imgs;

    static {
        imgs = new Image[4];
        try {
            for (int i = 0; i < 1; i++) {
                imgs[i] = ImageIO.read(new File("./pic/Bullet"+i+".png"));
            }
        } catch (IOException e) {
            System.out.println("弾の画像の読み込みに失敗:" + e);
            System.exit(1);
        }
    }

    /** マップ */
    private Map map;

    /**
     * コンストラクタ
     * @param game ゲーム
     */
    public Bullet(Game game) {
        this.game = game;
        active = false;
    }

    /**
     * 弾の初期化
     * @param ID ID
     * @param x x座標
     * @param y y座標
     * @param speed 速度
     * @param angle 角度
     * @param imgNum 画像番号
     */
    public void init(int ID, double x, double y, double speed, double angle, int imgNum) {

        this.ID = ID;

        // 座標
        this.x = x;
        this.y = y;

        // 画像指定
        loadImage(imgNum);

        // サイズ関連
        size = img.getWidth(null);
        radius = size / 2;

        // スピード, 角度
        setSpeed(speed);
        setAngle(angle);

        // まっぷ
        this.map = game.getMap();

        super.init();
    }

    /**
     * 画像を配列の中から選んでロード
     * @param imgNum 画像番号
     */
    protected void loadImage(int imgNum) {
        if (0 <= imgNum && imgNum < imgs.length)
            img = imgs[imgNum];
    }

    /**
     * 弾の更新
     */
    public void update() {
        x += speedx;
        y += speedy;
        if (x+radius < 0 || map.getWidth() < x-radius || y+radius < 0 || map.getHeight() < y-radius) {
            delete();
        }
    }

    /**
     * 弾の描画
     */
    public void draw(Graphics2D g, int offsetX, int offsetY) {
        int x = (int)(this.x - radius) + offsetX;
        int y = (int)(this.y - radius) + offsetY;

        BufferedImage _buf_img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);  // ラスターイメージのバッファを確保する

        Graphics2D g2 = _buf_img.createGraphics();  // バッファへの描画準備
        g.setBackground(new Color(255, 255, 255, 255));
        g2.drawImage(img, at, null);
        g.drawImage(_buf_img, x, y, null);
    }
}
