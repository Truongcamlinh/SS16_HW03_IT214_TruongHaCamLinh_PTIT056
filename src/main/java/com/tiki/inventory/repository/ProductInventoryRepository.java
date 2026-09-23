package com.tiki.inventory.repository;

import com.tiki.inventory.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, String> {}
