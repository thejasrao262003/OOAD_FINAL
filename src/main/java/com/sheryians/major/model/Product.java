package com.sheryians.major.model;

import lombok.Data;

import javax.persistence.*;

// Define an interface for products that can calculate discounts
interface Discountable {
    double calculateDiscount();
}

// Concrete implementation of Discountable for physical products
class PhysicalProductDiscount implements Discountable {
    @Override
    public double calculateDiscount() {
        // Example discount logic for physical products
        return 0.1; // 10% discount for physical products
    }
}

// Concrete implementation of Discountable for digital products
class DigitalProductDiscount implements Discountable {
    @Override
    public double calculateDiscount() {
        // Example discount logic for digital products
        return 0.2; // 20% discount for digital products
    }
}

// Modify the Product class to utilize the Discountable abstraction
@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;
    private double price;
    private double weight;
    private String description;
    private String imageName;

    // The Product class delegates discount calculation to a Discountable instance
    private Discountable discountable;

    // Method to set the discountable instance based on the type of product
    public void setDiscountable(Discountable discountable) {
        this.discountable = discountable;
    }

    // Method to calculate the discount using the assigned Discountable instance
    public double calculateDiscount() {
        if (discountable != null) {
            return discountable.calculateDiscount() * price;
        }
        return 0; // Default: No discount
    }

    // Getters and setters as before
}
