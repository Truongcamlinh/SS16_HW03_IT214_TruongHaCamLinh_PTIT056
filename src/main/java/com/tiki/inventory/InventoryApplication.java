package com.tiki.inventory;

import com.tiki.inventory.entity.ProductInventory;
import com.tiki.inventory.repository.ProductInventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class InventoryApplication {
    public static void main(String[] args) { SpringApplication.run(InventoryApplication.class, args); }

    @Bean
    CommandLineRunner seed(ProductInventoryRepository repository) {
        return args -> repository.findById("IPHONE-15")
                .orElseGet(() -> repository.save(new ProductInventory("IPHONE-15", 100)));
    }
}
