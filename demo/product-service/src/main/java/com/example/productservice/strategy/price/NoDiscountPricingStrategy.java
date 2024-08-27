package com.example.productservice.strategy.price;

import org.springframework.stereotype.Component;

@Component
public class NoDiscountPricingStrategy implements PricingStrategy {
    @Override
    public Double calculatePrice(Double basePrice) {
        return basePrice;
    }
}
