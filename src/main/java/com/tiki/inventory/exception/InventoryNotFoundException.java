package com.tiki.inventory.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String productId) {
        super("Không tìm thấy tồn kho của sản phẩm " + productId);
    }
}
