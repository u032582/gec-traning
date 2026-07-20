/* 1.CustomerController.java（窓口）
@GetMapping("/{id}")→GET/api/customers/1の{id}部分
自分では判断せず、Serviceに渡すだけ */
@GetMapping("/{id}")
public CustomerResponse getCustomer(@PathVariable("id") Long id) {
    return customerService.findById(id);
}

/* 2.CustomerService（判断・処理）
MapperでDBから取る
Customer→CustomerResponseに詰め替える（DTO化）
new CustomerMapper()していない→DI（springが差し込む）*/
public CustomerResponse findById(Long customerId) {
    Customer customer = customerMapper.findById(customerId);
    if (customer == null) {
        return null;
    }
    return CustomerResponse.from(customer);
}

/* 3.CustomerMappaer.java(java側)
 interface→宣言だけ
 @param("customerId")→XMLの#{customerId}*/
Customer findById(@Param("customerId") Long customerId);

// 4.CustomerMapper.xml
//これはSQL側

//5.Customer(エンティティ)
//DBの1行＝このクラスの１オブジェクト
//customer_id→customrId(application.ymlの設定で自動)

/* 6.customerResponse(DTO) 
外に見せる項目だけ(created_atは返さない)
customerId→JSONではidになる*/
public record CustomerResponse(
        Long id,
        String name,
        String email
)