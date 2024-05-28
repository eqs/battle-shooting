package com.github.eqs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MainPanel extends JPanel implements Runnable {

    /** パネルの横幅, 縦幅定数 */
    public static final int WIDTH = 1024;// 1024
    public static final int HEIGHT = 854;// 854

    /** ゲームスレッド */
    private Thread thread;

    /** ゲーム */
    private Game game;

    /** コントローラ管理 */
    ControllerManager cm;

    /**
     * コンストラクタ
     */
    public MainPanel() {
        // パネルサイズ設定
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // 背景色を白にする
        setBackground(Color.WHITE);

        // コントローラ初期化
        cm = new ControllerManager(4);

        // ゲームの初期化
        game = new Game(cm.getControllers());

        // スレッド起動
        thread = new Thread(this);
        thread.start();
    }

    /**
     * ゲームのメインループ
     */
    public void run() {
        while (true) {
            cm.update();
            game.update();
            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * パネルの上のものを描画
     * @param g
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        super.paintComponent(g2);
        game.draw(g2);
    }
}
