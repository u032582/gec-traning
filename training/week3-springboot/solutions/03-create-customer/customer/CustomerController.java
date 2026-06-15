package com.example.training.customer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 顧客APIの窓口（Controller層）。登録（POST）を追加した版。
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /** GET /api/customers/{id} … 1件取得。 */
    @GetMapping("/{id}")
    public CustomerResponse getCustomer(@PathVariable("id") Long id) {
        return customerService.findById(id);
    }

    /**
     * POST /api/customers … 顧客を登録する。
     *
     * <p>@RequestBody … リクエストのJSONを CustomerCreateRequest に変換して受け取る。
     * @Valid … そのDTOにバリデーション（@NotBlank / @Email 等）をかける。これが無いと検査されない。
     *   違反があるとSpringが自動で 400 を返す（整形は課題4で行う）。
     * @ResponseStatus(CREATED) … 成功時のステータスを 201 にする。
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@RequestBody @Valid CustomerCreateRequest request) {
        return customerService.create(request);
    }
}
