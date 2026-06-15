package com.example.training.customer;

import org.springframework.stereotype.Service;

/**
 * 顧客の業務処理（Service層）。登録（create）を追加した版。
 */
@Service
public class CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    /** IDで1件取得。見つからなければ null。 */
    public CustomerResponse findById(Long customerId) {
        Customer customer = customerMapper.findById(customerId);
        if (customer == null) {
            return null;
        }
        return CustomerResponse.from(customer);
    }

    /**
     * 顧客を登録し、採番されたidを含むレスポンスDTOを返す。
     * リクエストDTO → エンティティ(Customer) に詰め替えてからINSERTする。
     */
    public CustomerResponse create(CustomerCreateRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());

        customerMapper.insert(customer); // ここで customer.customerId に採番結果が入る

        return CustomerResponse.from(customer);
    }
}
