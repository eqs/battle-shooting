package com.github.eqs;

import javax.swing.*;


public class Main extends JFrame {
    public Main() {
        // タイトル指定
        setTitle("Game");

        // リサイズ禁止
        setResizable(false);

        // パネルを乗せる
        add(new MainPanel());
        pack();

        // ×ボタンでプログラム終了
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // フレーム表示
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
