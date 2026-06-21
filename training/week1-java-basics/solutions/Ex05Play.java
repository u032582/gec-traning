/**
 * 課題05 動かしてみる用ランナー
 * 実行: javac Ex05.java Ex05Play.java && java Ex05Play
 */
public class Ex05Play {

    static String formatArray(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Ex05 ex = new Ex05();
        int[] nums = {3, 1, 4, 1, 5};

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        System.out.println("配列 " + formatArray(nums));
        System.out.println("  合計 → " + ex.sum(nums));
        System.out.println("  最大 → " + ex.max(nums));
        System.out.println("  1 の個数 → " + ex.count(nums, 1));
        System.out.println("  逆順 → " + formatArray(ex.reverse(nums)));
        System.out.println();

        System.out.println("✨ 配列の数字が変換されて見えたら、配列処理は動いています！");
        System.out.println("次に Ex05Test で正解判定してください。");
    }
}
