package com.microsp.microspaiement.feign;

import com.microsp.microspaiement.entities.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PIDEVMICROSERVICE", url = "http://localhost:8090/users") // Adjust the URL
public interface UserClient {
    @GetMapping("/find/{cin}")
    User getUserByCin(@PathVariable String cin);
}