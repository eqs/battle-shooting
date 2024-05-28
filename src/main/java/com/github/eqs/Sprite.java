package com.github.eqs;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;


abstract class Sprite {

    /** ゲームの音 */
    public static GameAudio sound = new GameAudio();

    /** 移動速度の最大値 */
    public static double MAX_SPEED = 20;

    /** ゲーム */
    protected Game game;

    /** 画像回転用 */
    protected AffineTransform at = new AffineTransform();

    /** 自分のID */
    protected int ID;

    /** 自分の画像 */
    protected Image img;

    /** 自分の耐久力 */
    protected int HP;

    /** 自分の位置 */
    protected double x,y;

    /** サイズ */
    protected int size;

    /** 半径 */
    protected int radius;

    /** 自分の向き */
    protected double angle;

    /** 角速度 */
    protected double ang_vel;

    /** 自分の移動速度とその縦、横成分 */
    protected double speed;
    protected double speedx,speedy;

    /** 自分がアクティブかどうか */
    protected boolean active;

    /** HPを表示するかどうか */
    protected boolean visibleHP;

    /**
     * 画像を読み込む
     * @param file 読み込む画像ファイルのパス
     */
    protected void loadImage(String file) {
        img = new ImageIcon(file).getImage();
    }

    /**
     * スプライトの初期化
     */
    public void init() {
        active = true;
    }

    /**
     * スプライトの更新
     */
    abstract public void update();

    /**
     * スプライトの描画
     * @param g
     * @param offsetX
     * @param offsetY
     */
    abstract public void draw(Graphics2D g, int offsetX, int offsetY);

    /**
     * 他のスプライトと当たってるか判定
     * @return ぶつかっていたらtrue
     */
    public boolean isHit(Sprite s) {
        return isActive() && s.isActive() && ID != s.getID() && pow2(x - s.getX()) + pow2(y - s.getY()) < pow2(getRadius() + s.getRadius());
    }

    /**
     * 引数を二乗して返す
     * @param n 二乗する値
     * @return n*n
     */
    protected double pow2(double n) {
        return n*n;
    }

    /**
     * ダメージを受ける
     * @return 死んだらtrueを返す
     */
    public boolean damage() {
        HP--;
        sound.play(1);
        if (HP <= 0) {
            delete();
            sound.play(0);
            for (int i = 0; i < 10; i++) {
                game.makeEffect(x, y, Math.random()*6+1, (int)(Math.random()*360), (int)(Math.random()*3)+2, (int)(Math.random()*10)+20);
            }
            return true;
        }
        game.makeEffect(x, y, Math.random()*3+1, (int)(Math.random()*360), 3, (int)(Math.random()*8)+16);
        return false;
    }

    /**
     * 自分を殺す
     */
    public void delete() {
        active = false;
    }

    /**
     * 速度をセット
     * @param newSpeed 新しい速度
     */
    public void setSpeed(double newSpeed) {
        if (newSpeed < 0) {
            newSpeed = 0;
        }
        if (MAX_SPEED < newSpeed) {
            newSpeed = MAX_SPEED;
        }
        speed = newSpeed;
        speedx = speed*Math.cos(Math.toRadians(angle));
        speedy = speed*Math.sin(Math.toRadians(angle));
    }

    /**
     * 角度をセット
     * @param newAngle 新しい角度
     */
    public void setAngle(double newAngle) {
        angle = newAngle % 360;
        speedx = speed*Math.cos(Math.toRadians(angle));
        speedy = speed*Math.sin(Math.toRadians(angle));
        // 新しい行列を作っておく
        at.setToRotation(Math.toRadians(angle), size/2, size/2);
    }

    /**
     * HPを返す
     * @return HP
     */
    public int getHP() {
        return HP;
    }

    /**
     * X座標を返す
     * @return X座標
     */
    public double getX() { return x; }

    /**
     * Y座標を返す
     * @return Y座標
     */
    public double getY() { return y; }

    public double getAngle() { return angle; }
    public double getSpeed() { return speed; }
    public int getSize() { return size; }
    public int getRadius() { return radius; }
    public int getID() { return ID; }

    /**
     * 自分がアクティブかを返す
     * @return アクティブならtrue
     */
    public boolean isActive() {
        return active;
    }
}
