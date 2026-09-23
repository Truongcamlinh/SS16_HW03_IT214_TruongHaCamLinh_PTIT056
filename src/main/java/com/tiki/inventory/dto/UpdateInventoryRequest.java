package com.tiki.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateInventoryRequest(
        @NotNull(message = "newQuantity không được null")
        @Min(value = 0, message = "newQuantity phải lớn hơn hoặc bằng 0")
        Integer newQuantity) {}
