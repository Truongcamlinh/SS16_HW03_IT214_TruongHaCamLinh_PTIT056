package com.tiki.inventory.dto;

import java.io.Serializable;

public record ProductInventoryDTO(String productId, int quantity) implements Serializable {}
