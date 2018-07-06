package framework;

import java.util.List;
import java.util.Observable;

import javax.swing.SwingWorker;

/**
 * シュミュレーションのModelクラスを表す。 
 * @author 柴田航平 & 鈴木大河
 */
public class SimulationModel extends Observable {

	/**
	 * プレイヤー1の情報を表すフィールドです。
	 */
	private Player player1;
	/**
	 * プレイヤー2の情報を表すフィールドです。
	 */
	private Player player2;
	/**
	 * プレイヤー3の情報を表すフィールドです。
	 */
	private Player player3;
	/**
	 * カードの情報を表すフィールドです。
	 */
	private Cards tableCards = new AdjustCards();
	/**
	 * 成績の情報を表すフィールドです。
	 */
	private Score score = new Score();
	/**
	 * 勝敗判定の情報を表すフィールドです。
	 */
	private Judgement judge = new SubJudgement();
	/**
	 * ゲーム情報を表すフィールドです。
	 */
	private InfoGame info = new InfoGame();
	/**
	 * 全てのプレイヤー名を表すフィールドです。
	 */
	private String[] playerName = new String[3];
	/**
	 * 全てのプレイヤーの利用したカードを表すフィールドです。
	 */
	private int[] putOutArray = new int[3];

	/**
	 * シュミュレーションクラスのコンストラクタです。
	 * @param player1 Player1の戦略
	 * @param player2 Player2の戦略
	 * @param player3 Player3の戦略
	 */
	public SimulationModel(Player player1, Player player2, Player player3) {
		this.player1 = player1;
		this.player2 = player2;
		this.player3 = player3;
	}

	/**
	 * ボタン1が押したイベントの処理を記したメソッドです。
	 */
	public void clickedButton1() {
		setChanged();
		notifyObservers("hide");
	}

	/**
	 * シュミュレーションで描画するための計算処理を行うメソッドです。
	 */
	public void simulation() {

		SwingWorker<Object, Object[]> sw = new SwingWorker<Object, Object[]>() {
			@Override
			protected Object doInBackground() throws Exception {
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 15; j++) {

						// ターン数とラウンド数の設定をする。
						info.setTurnNum(j + 1);
						info.setRoundNum(i + 1);

						// 手札
						int p1Hand;
						int p2Hand;
						int p3Hand;

						// 手札を配布する。
						if (j == 0) {
							tableCards.clearAllHands();
							tableCards.dealCards();
						}

						// プレイヤーの戦略から使用するカードを取得する。
						p1Hand = player1.strategy(tableCards.getDeepHands(0), score, 0, info);
						p2Hand = player2.strategy(tableCards.getDeepHands(1), score, 1, info);
						p3Hand = player3.strategy(tableCards.getDeepHands(2), score, 2, info);

						if(!tableCards.getDeepHands(0).contains(p1Hand)||
								!tableCards.getDeepHands(1).contains(p2Hand)||
								!tableCards.getDeepHands(2).contains(p3Hand)) {
							info.setRoundNum(5);
							publish();
							return null;
						}else {

							// プレイヤーの使用したカードの削除をする。
							tableCards.removeAndRecordCards(tableCards.getDeepHands(0), 0, p1Hand);
							tableCards.removeAndRecordCards(tableCards.getDeepHands(1), 1, p2Hand);
							tableCards.removeAndRecordCards(tableCards.getDeepHands(2), 2, p3Hand);

							// ターンの勝敗判定をする。
							judge.turnJudgement(p1Hand, p2Hand, p3Hand);
							score.setTurnScore(judge.geTurntWinner(), judge.getPoint());

							// ターンの得点順位を設定する。
							judge.turnRanking(score);
							score.setTurnRanking(judge.getTurnRanking());

							// フレームへ描画の設定をする
							playerName[0] = cutPlayerName(player1.getName());
							playerName[1] = cutPlayerName(player2.getName());
							playerName[2] = cutPlayerName(player3.getName());

							putOutArray[0] = p1Hand;
							putOutArray[1] = p2Hand;
							putOutArray[2] = p3Hand;
							publish();

							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
						}

					}
					judge.roundJudgement(score);
					score.setRoundScore(judge.getRoundWinner());

					judge.roundRanking(score);
					score.setRoundRanking(judge.getRoundRanking());

					// 全てのプレイヤーのターン毎に得た得点を0クリアする。
					score.clearTurnScore();
				}
				info.setRoundNum(4);

				// 最終的な勝敗判定をする。
				judge.finalJudgement(score);
				score.setFinalScore(judge.getFinalWinner());

				return null;
			}

			@Override
			protected void process(List<Object[]> chunks) {
				setChanged();
				notifyObservers();
			}

			@Override
			protected void done() {
				setChanged();
				notifyObservers();
			}
		};
		sw.execute();

	}

	/**
	 * 全てプレイヤーの名前を取得するメソッドです。
	 * @return 全てのプレイヤー配列 
	 */
	protected String[] getAllPlayerName() {
		return playerName;
	}
	/**
	 * アスキーコードで記された文字列から一定の文字数をカットするメソッドです。
	 * @param s 編集元の文字列
	 * @return 編集後の文字列
	 */
	protected String cutPlayerName(String s) {
		int b = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            String s1 = s.substring(i, i + 1);
            b += s1.getBytes().length;
            if (b > 10) {
                break;
            }else sb.append(s1);
        }
		return sb.toString();
	}
	/**
	 * ターンで使用したカードの配列を取得するメソッドです。
	 * @return 利用したカードの配列
	 */
	protected int[] getPutOutArray() {
		return putOutArray;
	}
	/**
	 * 成績のインスタンスを取得するメソッドです。
	 * @return 成績インスタンス
	 */
	protected Score getScore() {
		return score;
	}
	/**
	 * 勝敗判定のインスタンスを取得するメソッドです。
	 * @return 勝敗判定のインスタンス
	 */
	protected Judgement getJudge() {
		return judge;
	}
	/**
	 * ゲーム情報のインスタンスを取得するメソッドです。
	 * @return ゲーム情報のインスタンス
	 */
	protected InfoGame getInfo() {
		return info;
	}
}