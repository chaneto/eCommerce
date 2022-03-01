package com.example.ecommerce.model.bindings;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


public class ProductBuyBindingModel {

        @NotNull(message = "Id cannot be null!!!")
        private Long id;

        @NotNull(message = "Quantity buy cannot be null!!!")
        @DecimalMin(value = "1", message = "Тhe quantity buy cannot be a negative value!!!")
        private BigDecimal quantityBuy;

    public ProductBuyBindingModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantityBuy() {
        return quantityBuy;
    }

    public void setQuantityBuy(BigDecimal quantityBuy) {
        this.quantityBuy = quantityBuy;
    }
}
