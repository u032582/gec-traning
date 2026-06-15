package com.example.training.customer;

import com.example.training.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 顧客の業務処理（Service層）。課題3の create に加え、
 * 「見つからなければ例外を投げる」(404の出どころ)に修正した版。
 */
@Service
public class CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    /**
     * IDで1件取得。見つからなければ ResourceNotFoundException を投げる。
     * 例外は共通ハンドラ（GlobalExceptionHandler）が捕まえて404に変換する。
     * Controllerは何も変えなくてよい（例外が自動で上に飛ぶ）。
     */
    public CustomerResponse findById(Long customerId) {
        Customer customer = customerMapper.findById(customerId);
        if (customer == null) {
            throw new ResourceNotFoundException("customer not found: id=" + customerId);
        }
        return CustomerResponse.from(customer);
    }

    /** 顧客を登録し、採番idを含むレスポンスを返す。 */
    public CustomerResponse create(CustomerCreateRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());

        customerMapper.insert(customer);

        return CustomerResponse.from(customer);
    }
}
