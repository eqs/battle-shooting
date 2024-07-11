# Battle Shooting

## About

高専2年のころに作った4人対戦のシューティングゲームです．
ライブラリの管理をMavenに任せるために画像や音声などのリソース管理周り (`Utils.java`) の変更や，コントローラが未接続の場合CPUモードに切り替えるといった仕様変更をしていますが，設計は当時のものです．

## 実行手順

### 必要なもの

DirectInput対応のコントローラ（XInputのみ対応のコントローラは使えません）

### プログラムのコンパイル

1. 公式Webページ (https://jinput.github.io/jinput/) の "Without maven" の節にある `jinput-natives-all.jar` をダウンロードする
1. ダウンロードした `.jar` ファイルの拡張子を `.zip` に変更してZIPファイルを解凍する
1. 解凍して得られたファイルを `libs` ディレクトリにいれる
1. 下記コマンドを実行してコンパイル

```
mvn compile
```

### プログラムの実行

```
mvn exec:exec
```

## 権利表示

### 効果音

* ザ・マッチメイカァズ
    * https://osabisi.sakura.ne.jp/m2/

### コントローラ入力

* JInput
    * The 3-Clause BSD License (The copyright is attributed in each source file committed)
    * https://jinput.github.io/jinput/

