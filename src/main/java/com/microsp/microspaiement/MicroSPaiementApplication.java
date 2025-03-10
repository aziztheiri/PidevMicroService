package com.microsp.microspaiement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.microsp.microspaiement")
public class MicroSPaiementApplication {


    public static void main(String[] args) {
        SpringApplication.run(MicroSPaiementApplication.class, args);
    }

}
