/**
 * 課題05: 配列（模範解答）
 *
 * 仕様（すべて int[] を引数に取る。配列は null でないとする）:
 *  - sum(int[] arr)     : 合計を返す（空配列は0）
 *  - max(int[] arr)     : 最大値を返す（空配列は Integer.MIN_VALUE）
 *  - count(int[] arr, int target) : target と一致する要素の個数を返す
 *  - reverse(int[] arr) : 要素を逆順にした「新しい配列」を返す（元の配列は変更しない）
 */
public class Ex05 {

    public int sum(int[] arr) {
        int total = 0;
        for (int v : arr) {
            total += v;
        }
        return total;
    }

    public int max(int[] arr) {
        if (arr.length == 0) {
            return Integer.MIN_VALUE;
        }
        int max = arr[0];
        for (int v : arr) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public int count(int[] arr, int target) {
        int c = 0;
        for (int v : arr) {
            if (v == target) {
                c++;
            }
        }
        return c;
    }

    public int[] reverse(int[] arr) {
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[arr.length - 1 - i];
        }
        return result;
    }
}
