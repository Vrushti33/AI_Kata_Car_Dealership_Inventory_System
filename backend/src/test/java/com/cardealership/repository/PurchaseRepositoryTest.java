package com.cardealership.repository;

import com.cardealership.entity.Category;
import com.cardealership.entity.Purchase;
import com.cardealership.entity.Role;
import com.cardealership.entity.User;
import com.cardealership.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PurchaseRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private User customer;
    private Vehicle car;
    private Purchase purchase;

    @BeforeEach
    public void setUp() {
        purchaseRepository.deleteAll();
        userRepository.deleteAll();
        vehicleRepository.deleteAll();

        customer = new User();
        customer.setEmail("buyer@route66.com");
        customer.setPassword("password123");
        customer.setName("Sally Carrera");
        customer.setRole(Role.USER);
        customer = userRepository.save(customer);

        car = new Vehicle();
        car.setMake("Hudson");
        car.setModel("Hornet Classic");
        car.setCategory(Category.SEDAN);
        car.setPrice(new BigDecimal("45000.00"));
        car.setQuantity(3);
        car.setYear(1951);
        car.setDescription("The Fabulous Hudson Hornet.");
        car = vehicleRepository.save(car);

        purchase = new Purchase();
        purchase.setUser(customer);
        purchase.setVehicle(car);
        purchase.setQuantity(1);
        purchase.setTotalPrice(new BigDecimal("45000.00"));
        purchase = purchaseRepository.save(purchase);
    }

    @Test
    public void shouldSaveAndFindPurchaseByUserId() {
        List<Purchase> purchases = purchaseRepository.findByUserId(customer.getId());
        assertThat(purchases).hasSize(1);
        assertThat(purchases.get(0).getVehicle().getModel()).isEqualTo("Hornet Classic");
    }

    @Test
    public void shouldFindAllPurchases() {
        List<Purchase> allPurchases = purchaseRepository.findAll();
        assertThat(allPurchases).hasSize(1);
    }
}
