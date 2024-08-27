package com.example.productservice.strategy.price;

import org.springframework.stereotype.Component;

@Component
public class DiscountPricingStrategy implements PricingStrategy {
    private Double discountRate;


    @Override
    public Double calculatePrice(Double basePrice) {
        return basePrice * (1 - discountRate);
    }
}
