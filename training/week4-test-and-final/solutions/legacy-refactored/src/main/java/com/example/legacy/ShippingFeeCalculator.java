package com.example.legacy;

import java.util.List;

/**
 * 配送料を計算するクラス（改修後）。
 *
 * <p>改修方針: <b>外から見た振る舞いは一切変えない</b>（characterization test が
 * 全部緑のまま）。そのうえで、読みやすさ・重複・命名・例外処理を改善した。
 *
 * <p>主な改善点（詳しい解説は solutions/legacy-refactored/README.md 参照）:
 * <ul>
 *   <li>calc と calcWithDiscount で完全コピペだった集計ロジックを feeForLine に1本化。</li>
 *   <li>マジックナンバー（500/800/2000…）を意味のある定数・enumに整理。</li>
 *   <li>a / tmp / data2 など無意味な変数名を、意味の分かる名前に変更。</li>
 *   <li>空の catch（例外の握りつぶし）をやめ、原因を残しつつ「壊れた行はスキップ」
 *       という現状の振る舞いだけは保った（=戻り値は変わらない）。</li>
 * </ul>
 */
public class ShippingFeeCalculator {

    /** 重量の区切り（グラム）。これ以下なら「軽い」「中くらい」と判定する。 */
    private static final int LIGHT_WEIGHT_LIMIT = 2000;
    private static final int MEDIUM_WEIGHT_LIMIT = 5000;

    /**
     * 配送地域ごとの料金表。
     * light = 2000g以下 / medium = 5000g以下 / heavy = それ超。
     */
    private enum Region {
        EAST("east", 500, 800, 1200),
        WEST("west", 600, 900, 1300),
        OTHER("", 700, 1000, 1500); // east/west 以外はすべてこれ（その他扱い）

        private final String code;
        private final int lightFee;
        private final int mediumFee;
        private final int heavyFee;

        Region(String code, int lightFee, int mediumFee, int heavyFee) {
            this.code = code;
            this.lightFee = lightFee;
            this.mediumFee = mediumFee;
            this.heavyFee = heavyFee;
        }

        /** 地域コード文字列から Region を引く。未知のコードは OTHER 扱い（元の挙動と同じ）。 */
        static Region from(String code) {
            for (Region r : values()) {
                if (r.code.equals(code)) {
                    return r;
                }
            }
            return OTHER;
        }

        /** この地域・この重量のときの配送料を返す。 */
        int feeFor(int weight) {
            if (weight <= LIGHT_WEIGHT_LIMIT) {
                return lightFee;
            } else if (weight <= MEDIUM_WEIGHT_LIMIT) {
                return mediumFee;
            } else {
                return heavyFee;
            }
        }
    }

    /** 会員ランクごとの割引率（%）。0は割引なし。元のif連鎖を表に置き換えた。 */
    private static int discountRateFor(int memberRank) {
        return switch (memberRank) {
            case 3 -> 20;
            case 2 -> 10;
            case 1 -> 5;
            default -> 0;
        };
    }

    /**
     * 全行の配送料を合算する（割引なし）。
     */
    public int calc(List<String> lines) {
        int total = 0;
        for (String line : lines) {
            total += feeForLine(line);
        }
        return total;
    }

    /**
     * 全行の配送料を合算したあと、会員ランクに応じた割引を適用する。
     */
    public int calcWithDiscount(List<String> lines, int memberRank) {
        int total = calc(lines);
        int discountRate = discountRateFor(memberRank);
        return total - (total * discountRate / 100);
    }

    /**
     * 1行（"地域,重量"）を解釈して配送料を返す。
     * 形式が壊れている行は 0 を返してスキップ扱いにする（元の握りつぶしと同じ結果）。
     * ただし握りつぶさず、原因を標準エラーに残しておく（現場での調査の手がかり）。
     */
    private int feeForLine(String line) {
        String[] columns = line.split(",");
        try {
            String regionCode = columns[0].trim();
            int weight = Integer.parseInt(columns[1].trim());
            return Region.from(regionCode).feeFor(weight);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            // 元コードは catch を空にして黙って捨てていた。
            // 改修後は「この行は集計に含めない」という振る舞いは保ちつつ、
            // 何が起きたかは記録に残す（無言の握りつぶしをやめる）。
            System.err.println("配送料の計算をスキップした不正な行: \"" + line + "\" 理由: " + e);
            return 0;
        }
    }
}
