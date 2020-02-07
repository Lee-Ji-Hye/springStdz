package com.example.demo.service;

import com.example.demo.vo.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {
    public Mono<Customer> save(Customer customer);
}
