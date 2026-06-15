package com.example.training.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外からのリクエストを最初に受け取る「窓口」（Controller層）。3層構造のいちばん外側。
 *
 * <p>URL（どんなアドレスに来たか）とメソッド（GET/POSTなど）を見て、
 * 対応する処理を Service に渡し、その結果を返す。
 * Controller自身は“判断”を持たず、受け取って渡すだけにするのがきれいな書き方。
 *
 * <p>{@code @RestController} … 「これはREST APIの窓口です」という目印。
 *   メソッドが返したオブジェクトを、自動でJSON（データの文字表現）に変換して返してくれる。
 * {@code @RequestMapping("/api/customers")} … このクラスの担当URLの共通の頭。
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    /** Service を外から差し込んでもらう（DI）。 */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * GET /api/customers/{id} … 顧客を1件取得する窓口。
     *
     * <p>{@code @GetMapping("/{id}")} … GETで /api/customers/数字 に来たらこのメソッドを呼ぶ。
     * {@code @PathVariable} … URLの中の {id} の部分を、引数 id として受け取る。
     * 例: /api/customers/1 にアクセスすると id=1 が渡ってくる。
     */
    @GetMapping("/{id}")
    public CustomerResponse getCustomer(@PathVariable("id") Long id) {
        return customerService.findById(id);
    }
}
