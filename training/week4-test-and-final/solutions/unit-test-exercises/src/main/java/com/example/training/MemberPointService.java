package com.example.training;

/**
 * 課題1-2（Mockitoでモック化）のテスト対象クラス。
 *
 * <p>このクラスは「ポイントを加算する」という判断・処理を担当する
 * Service（サービス）役。DBの出し入れは自分ではやらず、
 * {@link MemberMapper} に任せる（=依存している）。
 *
 * <p>単体テストでは、この MemberMapper を Mockito のモックに置き換え、
 * 「Mapper がこう返したら、Service はこう振る舞うか」を確認する。
 */
public class MemberPointService {

    private final MemberMapper memberMapper;

    /**
     * コンストラクタで MemberMapper を受け取る（コンストラクタインジェクション）。
     *
     * <p>こうしておくと、テスト時にモックを差し込める。これが
     * 「テストしやすい設計」の基本形。
     */
    public MemberPointService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    /**
     * 指定した会員にポイントを加算する。
     *
     * <p>仕様:
     * <ul>
     *   <li>会員を id で取得する。いなければ IllegalArgumentException を投げる。</li>
     *   <li>加算ポイントが0以下なら IllegalArgumentException を投げる
     *       （0や負のポイント加算は不正とする）。</li>
     *   <li>「今のポイント + 加算分」を計算し、Mapper の updatePoint で更新する。</li>
     *   <li>更新後の合計ポイントを戻り値として返す。</li>
     * </ul>
     *
     * @param memberId 会員番号
     * @param addPoint 加算するポイント（1以上）
     * @return 加算後の合計ポイント
     * @throws IllegalArgumentException 会員が存在しない、または加算ポイントが0以下のとき
     */
    public int addPoint(int memberId, int addPoint) {
        if (addPoint <= 0) {
            throw new IllegalArgumentException("加算ポイントは1以上で指定してください: " + addPoint);
        }
        Member member = memberMapper.findById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("会員が見つかりません: id=" + memberId);
        }
        int newPoint = member.getPoint() + addPoint;
        memberMapper.updatePoint(memberId, newPoint);
        return newPoint;
    }
}
