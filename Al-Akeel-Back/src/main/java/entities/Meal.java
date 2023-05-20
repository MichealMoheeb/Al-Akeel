package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Meal implements Serializable {

    @Id
    private String name;

    private double price;

    @ManyToOne
    @JsonIgnore
    private AkeelOrder akeelOrder;

    @ManyToOne
    @JsonIgnore
    private Restaurant restaurant;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public AkeelOrder getAkeelOrder() {
        return akeelOrder;
    }

    public void setAkeelOrder(AkeelOrder akeelOrder) {
        this.akeelOrder = akeelOrder;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
