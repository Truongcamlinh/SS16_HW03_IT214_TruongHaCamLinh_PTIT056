package com.tiki.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_inventory")
public class ProductInventory {
    @Id
    private String productId;
    private int quantity;

    protected ProductInventory() {}
    public ProductInventory(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
