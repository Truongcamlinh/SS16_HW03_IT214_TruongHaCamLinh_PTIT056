package com.tiki.inventory.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tiki.inventory.entity.ProductInventory;
import com.tiki.inventory.repository.ProductInventoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
    @Mock ProductInventoryRepository repository;
    @InjectMocks InventoryService service;

    @Test
    void rejectsNegativeQuantityBeforeWritingDatabase() {
        assertThatThrownBy(() -> service.updateInventory("IPHONE-15", -10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("lớn hơn hoặc bằng 0");
        verifyNoInteractions(repository);
    }

    @Test
    void updatesDatabaseAndReturnsDto() {
        ProductInventory entity = new ProductInventory("IPHONE-15", 100);
        when(repository.findById("IPHONE-15")).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        var result = service.updateInventory("IPHONE-15", 95);

        assertThat(result.quantity()).isEqualTo(95);
        verify(repository).save(entity);
    }

    @Test
    void readsInventoryFromDatabaseOnCacheMiss() {
        when(repository.findById("IPHONE-15"))
                .thenReturn(Optional.of(new ProductInventory("IPHONE-15", 100)));

        assertThat(service.getInventory("IPHONE-15").quantity()).isEqualTo(100);
    }
}
