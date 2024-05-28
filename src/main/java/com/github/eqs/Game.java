package com.github.eqs;

import java.awt.*;
import java.awt.image.*;


public class Game {

    /** パネルの横幅, 縦幅定数 */
    public static final int WIDTH = MainPanel.WIDTH;
    public static final int HEIGHT = MainPanel.HEIGHT;

    /** プレイヤーの1画面の大きさ */
    public static final int M_WIDTH = WIDTH / 2;
    public static final int M_HEIGHT = HEIGHT / 2;

    /** フォント */
    private static final Font FONT = new Font("Consolas", Font.PLAIN, 32);
    private static final Font BIG_FONT = new Font("Consolas", Font.PLAIN, 160);

    /** ゲームの状態 */
    private GameState gameState;

    /** マップ */
    private Map map;

    /** コントローラ配列 */
    private ControllerState[] conts;

    /** プレイヤー配列 */
    private Player[] players;

    /** 弾配列 */
    private Bullet[] bullets;

    /** エフェクト配列 */
    private Effect[] effects;

    /**
     * コンストラクタ
     */
    public Game(ControllerState[] conts) {
        this.conts = conts;

        // マップ初期化
        map = new Map(1280, 960, "./pic/Back.png");

        // スプライト初期化
        players = new Player[4];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(this);
        }

        bullets = new Bullet[512];
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet(this);
        }

        effects = new Effect[512];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = new Effect(this);
        }

        // ゲームの状態をTitleに
        gameState = GameState.Title;
    }

    /**
     * ゲームを準備して開始する
     */
    private void gameStart() {
        deleteSprite();

        for (int i = 0; i < players.length; i++) {
            makePlayer(
                i,
                40,
                (int)(Math.random()*map.getWidth()),
                (int)(Math.random()*map.getHeight()),
                Math.random()*360,
                "./pic/Fighter"+i+".png",
                conts[i]
            );
        }

        gameState = GameState.MultiGame;
    }

    /**
     * ゲームを後始末して終了する
     */
    private void gameEnd() {
        gameState = GameState.Title;
        deleteSprite();
    }

    /**
     * 全てのスプライトを消し去るメソッド
     */
    public void deleteSprite() {
        for (Player pl : players) pl.delete();
        for (Bullet bl : bullets) bl.delete();
        for (Effect ef : effects) ef.delete();
    }

    /**
     * ゲームの更新
     */
    public void update() {
        switch (gameState) {
            // 対戦中
            case MultiGame:
                //makeBullet(4, 300, 300, 4 + (int)(Math.random()*6), 45 + (int)(Math.random()*45), 0);

                for (Player p : players) {
                    if (p.isActive()) {
                        p.update();
                    }
                }
                for (Bullet b : bullets) {
                    if (b.isActive()) {
                        b.update();
                    }
                }
                for (Effect e : effects) {
                    if (e.isActive()) {
                        e.update();
                    }
                }

                // プレイヤーが2以上残ってるなら当たり判定
                if (getNumberOfPlayers() > 1) {
                    boolean f;
                    for (Bullet b : bullets) {
                        f = false;
                        for (Player p : players) {
                            if (p.isHit(b)) {
                                p.damage();
                                f = true;
                            }
                        }
                        if (f) {
                            b.delete();
                        }
                    }
                } else {
                    // 1機だけのときPlayer1の射撃ボタンがおされたら終了
                    if (conts[0].getButtonValue(0) == 8) {
                        gameEnd();
                    }
                }
                break;
                // タイトル画面
            case Title:
                if (conts[0].getButtonValue(0) == 4) {
                    gameStart();
                }
                break;
        }
    }

    /** 画面の描画位置(左上、右上、左下、右下の順番) */
    private static int[] dx = {0, M_WIDTH, 0, M_WIDTH};
    private static int[] dy = {0, 0, M_HEIGHT, M_HEIGHT};
    /** 画面の描画色配列 */
    private static final Color[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};
    /** HP表示色 */
    public static final Color HP_COLOR = new Color(208, 255, 208);

    /**
     * ゲームの描画
     * @param g
     */
    public void draw(Graphics2D g) {
        switch (gameState) {
            // 対戦中
            case MultiGame:
                int offsetX = 0;
                int offsetY = 0;
                int MapWidth = map.getWidth();
                int MapHeight = map.getHeight();

                // とりあえずバッファに描画
                BufferedImage _buf_img = new BufferedImage(MapWidth, MapHeight, BufferedImage.TYPE_INT_ARGB);// ラスターイメージのバッファを確保する
                Graphics2D g2 = _buf_img.createGraphics();  // バッファへの描画準備

                g2.drawImage(map.getBackimage(), 0, 0, null);

                for(Player p : players) {
                    if (p.isActive()) {
                        p.draw(g2, 0, 0);
                    }
                }
                for(Bullet b : bullets) {
                    if (b.isActive()) {
                        b.draw(g2, 0, 0);
                    }
                }
                for(Effect e : effects) {
                    if (e.isActive()) {
                        e.draw(g2, 0, 0);
                    }
                }

                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);

                for (int i = 0;i < players.length;i++) {
                    int px = (int)players[i].getX();
                    int py = (int)players[i].getY();

                    // X方向のオフセットを計算
                    offsetX = M_WIDTH / 2 - px;
                    // マップの端ではスクロールしないようにする
                    offsetX = Math.min(offsetX, 0);
                    offsetX = Math.max(offsetX,M_WIDTH - MapWidth);

                    // X方向のオフセットを計算
                    offsetY = M_HEIGHT / 2 - py;
                    // マップの端ではスクロールしないようにする
                    offsetY = Math.min(offsetY, 0);
                    offsetY = Math.max(offsetY, M_HEIGHT - MapHeight);

                    g.drawImage(
                        _buf_img,
                        dx[i], dy[i], dx[i]+M_WIDTH, dy[i]+M_HEIGHT,
                        -offsetX, -offsetY, -offsetX+M_WIDTH, -offsetY+M_HEIGHT,
                        null
                    );

                    if (!players[i].isActive()) {
                        // 負け組です
                        g.setColor(new Color(0, 0, 0, 128));
                        g.fillRect(dx[i], dy[i], M_WIDTH, M_HEIGHT);
                        g.setFont(BIG_FONT);
                        g.setColor(Color.BLUE);
                        g.drawString("LOSE", dx[i]+32, dy[i]+256);
                    } else if (getNumberOfPlayers() <= 1) {
                        // 勝ち組です
                        g.setColor(new Color(0, 0, 0, 128));
                        g.fillRect(dx[i], dy[i], M_WIDTH, M_HEIGHT);
                        g.setFont(BIG_FONT);
                        g.setColor(Color.RED);
                        g.drawString("WIN", dx[i]+32, dy[i]+256);
                    }
                    // 各プレイヤーのステータス
                    g.setFont(FONT);
                    g.setColor(colors[i]);
                    g.drawString((i+1)+"P", dx[i]+16, dy[i]+32);
                    g.setColor(HP_COLOR);
                    g.drawString("HP:"+players[i].getHP(), dx[i]+16, dy[i]+64);
                }
                break;
                // タイトル画面
            case Title:
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                g.setFont(BIG_FONT);
                g.setColor(Color.GRAY);
                g.drawString("Battle", 32, 128+48);
                g.drawString("Shooting", 32, 256+48);

                g.setFont(FONT);
                g.drawString("Please press start button.", WIDTH/2-WIDTH/4, HEIGHT/2+HEIGHT/4);
        }
    }


    /**
     * アクティブなプレイヤーの数を数えて返すメソッド
     * @return アクティブなプレイヤーの数
     */
    public int getNumberOfPlayers() {
        int cnt = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i].isActive()) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * プレイヤーを作成
     * @param ID	勢力を示すID
     * @param HP	耐久力
     * @param x		X座標
     * @param y		Y座標
     * @param angle	角度
     * @return プレイヤーが追加された場所の番号
     */
    public int makePlayer(int ID, int HP, double x, double y, double angle, String file) {
        return makePlayer(ID, HP, x, y, angle, file, null);
    }

    /**
     * プレイヤーを作成
     * @param ID	勢力を示すID
     * @param HP	耐久力
     * @param x		X座標
     * @param y		Y座標
     * @param angle	角度
     * @param file  ファイル名
     * @param cont  コントローラ
     * @return プレイヤーが追加された場所の番号
     */
    public int makePlayer(int ID, int HP, double x, double y, double angle, String file, ControllerState cont) {
        int idx = searchInactive(players);
        if (idx != -1) {
            players[idx].init(ID, HP, x, y, angle, file, cont);
        }
        return idx;
    }

    /**
     * 弾を作成
     * @param ID	勢力を示すID
     * @param x		X座標
     * @param y		Y座標
     * @param speed 速度
     * @param angle 角度
     * @param imgNum 画像番号
     * @return 弾が追加された場所の番号
     */
    public int makeBullet(int ID, double x, double y, double speed, double angle, int imgNum) {
        int idx = searchInactive(bullets);
        if (idx != -1) {
            bullets[idx].init(ID, x, y, speed, angle, imgNum);
        }
        return idx;
    }

    /**
     * エフェクトを作成
     * @param x		X座標
     * @param y		Y座標
     * @param speed 速度
     * @param angle 角度
     * @param sizeSpeed 大きくなる速さ
     * @param time 出現時間
     * @return 弾が追加された場所の番号
     */
    public int makeEffect(double x, double y, double speed, double angle, int sizeSpeed, int time) {
        int idx = searchInactive(effects);
        if (idx != -1) {
            effects[idx].init(x, y, speed, angle, sizeSpeed, time);
        }
        return idx;
    }

    /**
     * アクティブでないスプライトを探して番号を返す
     * @return アクティブでないスプライトの要素番号、無ければ-1
     */
    private int searchInactive(Sprite[]sprites) {
        for (int i = 0;i < sprites.length;i++) {
            if (!sprites[i].isActive()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * マップを返す
     * @return マップ
     */
    public Map getMap() {
        return map;
    }
}

enum GameState {
    Title, MultiGame
}
