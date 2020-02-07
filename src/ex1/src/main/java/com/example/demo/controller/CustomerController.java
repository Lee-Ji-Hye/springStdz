package com.example.demo.controller;

import com.example.demo.service.CustomerService;
import com.example.demo.vo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> save(@RequestBody Customer customer) {
        return customerService.save(customer);
    }
    //...
}