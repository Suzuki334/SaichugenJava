# 最中限プログラム(Java)

コマンドライン引数から戦略アルゴリズムを記したクラスファイルを指定し、対戦シュミュレーションを行うプログラムです。<br>
※設計途中(2018/3/18~)

【ルール】<br>
※本家のルールとは異なります。
1.3人のプレイヤーにジョーカーを除いた52枚のトランプカードから17枚の手札を配布します。余ったカードは盤面に公開しません。<br>
2.各プレイヤーはカードを1枚を盤面に出します。<br>
3.ここでカードの勝敗判定を行います。トランプの数字の中で中位となる者が勝利となります。同一の数字が出された場合、スペード>ハート>ダイヤ>クラブの優先順位を考慮します。A,J,Q,Kはぞれぞれ数値の1,11,12,13を表します。利用したカードは次の判定で使用できません。<br>
4.この判定の勝者は、カードの数字の得点を取得します。<br>
5.その判定を1ターンとして、計15回行ったものを1ラウンドとします。15ラウンドが終了後、各プレイヤーが得たターンの合計点が中位にある者にラウンドの得点に+1が加算されます。勝者が複数いた場合も同様です。<br>
6.新たなラウンド開始時に、手順1-5を繰り返します。<br>
7.3ラウンドを行い、ラウンドの合計点が中位となった物がゲームの勝利者となります。勝者が複数いた場合も同様です。<br>

本家の最中限との最中限との差異は、<br>
-ターン・ラウンド数<br>
-ラウンドの勝利で得られる得点<br>

本家のルールの詳細は、以下を参照してください。<br>
https://ci.nii.ac.jp/naid/110006381118/

