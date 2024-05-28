package com.github.eqs;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Player extends Sprite {

    /** 移動速度の最低値 */
    public static final double SPEED = 4;

    /** 移動速度の最大値 */
    public static double MAX_SPEED = 10;

    /** コントローラ状態の保持 */
    private ControllerState cont;

    /** マップ */
    private Map map;

    /**
     * コンストラクタ
     * @param game ゲーム
     */
    public Player(Game game) {
        this.game = game;
        active = false;
    }

    /**
     * プレイヤーの初期化
     * @param ID ID
     * @param HP 耐久力
     * @param x x座標
     * @param y y座標
     * @param angle 角度
     * @param file 画像ファイル
     * @param cont コントローラ
     * @param game ゲーム
     */
    public void init(int ID, int HP, double x, double y, double angle, String file, ControllerState cont) {
        this.cont = cont;
        this.ID = ID;

        // HP
        this.HP = HP;

        // 座標
        this.x = x;
        this.y = y;

        // 画像読み込み
        loadImage(file);

        // サイズ関連
        size = img.getWidth(null);
        radius = size / 2;

        // スピード, 角度
        setSpeed(SPEED);
        setAngle(angle);

        // まっぷ
        this.map = game.getMap();

        super.init();
    }

    /**
     * プレイヤーの更新
     */
    public void update() {
        if (cont != null) {
            int xKey = cont.getXkeyValue();
            int yKey = cont.getYkeyValue();
            int sKey = cont.getButtonValue(0);
            int aKey = cont.getButtonValue(2);

            if (xKey < 0) {
                setAngle(getAngle()-4);
            }
            if (xKey > 0) {
                setAngle(getAngle()+4);
            }
            if (sKey % 4 == 1) {
                game.makeBullet(
                    ID,
                    x + radius * Math.cos(Math.toRadians(angle)),
                    y + radius * Math.sin(Math.toRadians(angle)),
                    12,
                    angle,
                    0
                );
            }

            if (aKey > 2) {
                // 加速
                setSpeed(Math.min(getSpeed()+2, MAX_SPEED));
            }
        }

        x += speedx;
        y += speedy;

        // マップ内に入るように補正
        x = Math.min(Math.max(0, x), map.getWidth());
        y = Math.min(Math.max(0, y), map.getHeight());

        // 減速
        setSpeed(Math.max(getSpeed()-1, SPEED));
    }

    /**
     * プレイヤーの描画
     */
    public void draw(Graphics2D g, int offsetX, int offsetY) {
        int x = (int)(this.x - radius) + offsetX;
        int y = (int)(this.y - radius) + offsetY;

        BufferedImage _buf_img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);// ラスターイメージのバッファを確保する
        Graphics2D g2 = _buf_img.createGraphics();  // バッファへの描画準備
        g.setBackground(new Color(255, 255, 255, 255));
        g2.drawImage(img, at, null);
        g.drawImage(_buf_img, x, y, null);
    }
}
