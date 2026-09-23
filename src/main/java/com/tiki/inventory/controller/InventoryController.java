package com.tiki.inventory.controller;

import com.tiki.inventory.dto.ProductInventoryDTO;
import com.tiki.inventory.dto.UpdateInventoryRequest;
import com.tiki.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService service;
    public InventoryController(InventoryService service) { this.service = service; }

    @GetMapping("/{productId}")
    ProductInventoryDTO get(@PathVariable String productId) {
        return service.getInventory(productId);
    }

    @PutMapping("/{productId}")
    ProductInventoryDTO update(@PathVariable String productId,
                               @Valid @RequestBody UpdateInventoryRequest request) {
        return service.updateInventory(productId, request.newQuantity());
    }
}
