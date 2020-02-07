package com.example.demo.service.impl;

import com.example.demo.dao.CustomerRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.vo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Mono<Customer> save(Customer customer) {
        return Mono.just(customerRepository.save(customer));
    }
    //...
}