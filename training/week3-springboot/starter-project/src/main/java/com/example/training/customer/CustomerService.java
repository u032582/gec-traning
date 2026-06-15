package com.example.training.customer;

import org.springframework.stereotype.Service;

/**
 * 業務の判断・処理を受け持つ「Service層」。3層構造のまんなか。
 *
 * <p>Controller（窓口）から呼ばれ、Mapper（DBアクセス）を使ってデータを取り、
 * 必要なら加工して返す。今は単純だが、現場ではここに「条件による分岐」や
 * 「複数のDB操作のとりまとめ」などの“判断”が入っていく。
 *
 * <p>{@code @Service} … 「これはService層の部品（Bean）です」という目印。
 * これを付けておくと、Springが自動でこのクラスのオブジェクトを1つ用意し、
 * 必要な場所（Controller）に差し込んで（=DI）くれる。
 * 用語メモ: DI（Dependency Injection / 依存性の注入）… 必要な部品を自分で new せず、
 *   外から差し込んでもらう仕組み。テストや差し替えがしやすくなる。
 */
@Service
public class CustomerService {

    private final CustomerMapper customerMapper;

    /**
     * コンストラクタ。引数の CustomerMapper は、Springが自動で差し込んでくれる（=DI）。
     * 自分で new CustomerMapper() しないのがポイント。
     */
    public CustomerService(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    /**
     * IDで顧客を1件取得し、外向けのDTOに詰め替えて返す。
     * 見つからない場合は null を返す（404の出し方は課題4で学ぶ）。
     */
    public CustomerResponse findById(Long customerId) {
        Customer customer = customerMapper.findById(customerId);
        if (customer == null) {
            return null;
        }
        return CustomerResponse.from(customer);
    }
}
