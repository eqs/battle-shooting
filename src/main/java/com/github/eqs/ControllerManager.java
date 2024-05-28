package com.github.eqs;

import java.util.*;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


/**
 * コントローラを管理するクラス
 */
public class ControllerManager {

    /** コントローラの状態の配列 */
    private ControllerState[] conts;

    /**
     * コンストラクタ
     * @param num コントローラの数
     */
    public ControllerManager(int num) {

        // コントローラの取得
        ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();
        Controller[] c = env.getControllers();

        // コントローラの状態配列の初期化
        conts = new ControllerState[num];

        for (int i = 0, cnt = 0; i < c.length && cnt < num; i++) {
            if (c[i].getType() == Controller.Type.STICK && c[i].poll()) {
                conts[cnt++] = new ControllerState(c[i]);
            }
        }
    }

    /**
     * コントローラの更新
     */
    public void update() {
        for (ControllerState co : conts) {
            if (co != null) {
                co.update();
            }
        }
    }

    /**
     * コントローラの配列を返す
     * @return コントローラ状態の配列
     */
    public ControllerState[] getControllers() {
        return conts;
    }

}

/**
 * コントローラの状態を保持するクラス
 * @author chikuwa
 */
class ControllerState {

    /** コントローラ */
    private Controller con;

    /** ボタンが押されてますかフラグ */
    private int[] buttonsValues;

    /** キーが押されてますかフラグ */
    private int xKeyValue, yKeyValue;

    /** ボタンを保存しとく */
    private Component[] buttons;

    /** 方向キーの保存 */
    private Component xKey, yKey;

    /**
     * コンストラクタ
     * @param con コントローラ
     */
    public ControllerState(Controller con) {
        this.con = con;

        // コントローラのボタンと方向キーの数を数えて, ボタンと方向キーのそれぞれのフラグ配列をつくる
        int cntB = 0;
        int cntK = 0;
        this.con.poll();
        Component[]comp = con.getComponents();
        ArrayList<Component>listB = new ArrayList<Component>(0);
        ArrayList<Component>listK = new ArrayList<Component>(0);

        for (Component c : comp) {
            Component.Identifier tmp = c.getIdentifier();
            if (tmp instanceof Component.Identifier.Button) {
                listB.add(c);
                cntB++;
            } else if (tmp == Component.Identifier.Axis.X) {
                if (xKey == null) {
                    xKey = c;
                }
            } else if (tmp == Component.Identifier.Axis.Y) {
                if (yKey == null) {
                    yKey = c;
                }
            }
        }

        buttonsValues = new int[cntB];
        buttons = listB.toArray(new Component[1]);
        xKeyValue = 0;
        yKeyValue = 0;
    }

    /**
     * コントローラを更新する
     */
    public void update() {
        // コントローラの更新
        con.poll();

        // ぼたん
        for (int i = 0; i < buttons.length; i++) {
            float val = buttons[i].getPollData();
            if (val == 1.0f) {
                buttonsValues[i]++;
            } else {
                buttonsValues[i] = 0;
            }
        }

        // ほうこうきー

        float val = xKey.getPollData();
        if (val == 1.0f) {
            if (xKeyValue < 0) {
                xKeyValue = 0;
            }
            xKeyValue++;
        } else if (val == -1.0f) {
            if (0 < xKeyValue) {
                xKeyValue = 0;
            }
            xKeyValue--;
        } else {
            xKeyValue = 0;
        }

        val = yKey.getPollData();
        if (val == 1.0f) {
            if (yKeyValue < 0) {
                yKeyValue = 0;
            }
            yKeyValue++;
        } else if (val == -1.0f) {
            if (0 < yKeyValue) {
                yKeyValue  = 0;
            }
            yKeyValue--;
        } else {
            yKeyValue = 0;
        }
    }

    /**
     * index番目のボタンが何フレーム連続で押されてるかを返す
     * @param index
     * @return 押してる時間
     */
    public int getButtonValue(int index) {
        return buttonsValues[index];
    }

    /**
     * X方向の方向キーが何フレーム連続で押されてるかを返す
     * @return 押してる時間
     */
    public int getXkeyValue() {
        return xKeyValue;
    }

    /**
     * Y方向の方向キーが何フレーム連続で押されてるかを返す
     * @return 押してる時間
     */
    public int getYkeyValue() {
        return yKeyValue;
    }

    /**
     * index番目のボタンが押されてるかどうかを返す
     * @param index
     * @return 押されていればtrue, そうでなければfalseを返す
     */
    public boolean isButtonPressed(int index) {
        return 0 < buttonsValues[index];
    }


    /**
     * ボタンの数を返す
     * @return ボタンの数
     */
    public int getNumberOfButtons() {
        return buttons.length;
    }


    /**
     * コントローラを返す
     * @return コントローラ
     */
    public Controller getController() {
        return con;
    }
}
