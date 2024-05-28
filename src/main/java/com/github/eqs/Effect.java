package com.github.eqs;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Effect extends Sprite {

    /** マップ */
    private Map map;

    /** 出現時間 */
    private int time;
    private int timeCnt;

    /** 拡大速度 */
    private int sizeSpeed;

    static Color[] bomb;
    static {
        bomb = new Color[14];
        for (int i = 0;i < bomb.length;i++) {
            bomb[i] = new Color(255, 10*i, 0, 255);
            // 64+i*2
        }
    }

    /**
     * コンストラクタ
     * @param game ゲーム
     */
    public Effect(Game game) {
        this.game = game;
        active = false;
    }

    /**
     * エフェクトの初期化
     * @param ID ID
     * @param x x座標
     * @param y y座標
     * @param speed 速度
     * @param angle 角度
     * @param sizeSpeed 拡大速度
     * @param time 出現時間
     */
    public void init(double x, double y, double speed, double angle, int sizeSpeed, int time) {
        this.ID = ID;

        // 座標
        this.x = x;
        this.y = y;

        // サイズ関連
        this.sizeSpeed = sizeSpeed;
        this.size = 0;

        // スピード, 角度
        setSpeed(speed);
        setAngle(angle);

        // 時間
        this.time = time;
        timeCnt = 0;

        // まっぷ
        this.map = game.getMap();

        super.init();
    }

    /**
     * エフェクトの更新
     */
    public void update() {
        x += speedx;
        y += speedy;
        size += sizeSpeed;
        timeCnt++;
        if (time <= timeCnt) {
            delete();
        }
    }

    /**
     * エフェクトの描画
     */
    public void draw(Graphics2D g, int offsetX, int offsetY) {
        int x = (int)(this.x-size/2) + offsetX;
        int y = (int)(this.y-size/2) + offsetY;
        for (int i = 0;i < bomb.length;i++) {
            g.setColor(bomb[i]);
            g.fillOval(x+i, y+i, size-i*2, size-i*2);
        }
    }
}
