package com.example.demo;

import com.example.demo.vo.Customer;
import com.sun.glass.ui.Application;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes= Application.class
        , webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private Customer newCustomer = new Customer();

    @Test
    public void test2_add_customer_done() {
        WebClient webClient = WebClient.create("http://localhost:8778");
        Customer customer = webClient.post().uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(newCustomer))
                .retrieve()
                .bodyToMono(Customer.class)
                .block();

        String id = customer.getId();
        assertThat(customer.getName(), is(newCustomer.getName()) );
    }

    @Test
    public void test3_find_customer_done() {
        webTestClient.get().uri("/customers/{id}", "id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name")
                .isEqualTo(newCustomer.getName());
    }
}