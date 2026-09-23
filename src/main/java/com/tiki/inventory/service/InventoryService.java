package com.tiki.inventory.service;

import com.tiki.inventory.dto.ProductInventoryDTO;
import com.tiki.inventory.entity.ProductInventory;
import com.tiki.inventory.exception.InventoryNotFoundException;
import com.tiki.inventory.repository.ProductInventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final ProductInventoryRepository repository;

    public InventoryService(ProductInventoryRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "productInventory", key = "#productId", sync = true)
    public ProductInventoryDTO getInventory(String productId) {
        String validId = validateProductId(productId);
        log.info("Cache miss hoặc Redis lỗi - đọc {} từ database", validId);
        return repository.findById(validId).map(this::toDto)
                .orElseThrow(() -> new InventoryNotFoundException(validId));
    }

    @Transactional
    @CacheEvict(cacheNames = "productInventory", key = "#productId", beforeInvocation = false)
    public ProductInventoryDTO updateInventory(String productId, Integer newQuantity) {
        String validId = validateProductId(productId);
        if (newQuantity == null || newQuantity < 0) {
            throw new IllegalArgumentException("newQuantity phải lớn hơn hoặc bằng 0");
        }
        ProductInventory inventory = repository.findById(validId)
                .orElseThrow(() -> new InventoryNotFoundException(validId));
        inventory.setQuantity(newQuantity);
        ProductInventory saved = repository.save(inventory);
        log.info("Đã cập nhật DB: productId={}, quantity={}; cache sẽ được evict", validId, newQuantity);
        return toDto(saved);
    }

    private String validateProductId(String productId) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId không được null hoặc rỗng");
        }
        return productId.trim();
    }

    private ProductInventoryDTO toDto(ProductInventory entity) {
        return new ProductInventoryDTO(entity.getProductId(), entity.getQuantity());
    }
}
