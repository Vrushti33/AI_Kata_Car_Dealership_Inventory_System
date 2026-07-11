package com.cardealership.repository;

import com.cardealership.entity.Category;
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
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle mcqueen;
    private Vehicle doc;
    private Vehicle mater;

    @BeforeEach
    public void setUp() {
        vehicleRepository.deleteAll();

        mcqueen = new Vehicle();
        mcqueen.setMake("Lightning");
        mcqueen.setModel("McQueen Special");
        mcqueen.setCategory(Category.COUPE);
        mcqueen.setPrice(new BigDecimal("95000.00"));
        mcqueen.setQuantity(5);
        mcqueen.setYear(2024);
        mcqueen.setDescription("Ka-Chow! Speed. I am speed.");

        doc = new Vehicle();
        doc.setMake("Hudson");
        doc.setModel("Hornet Classic");
        doc.setCategory(Category.SEDAN);
        doc.setPrice(new BigDecimal("45000.00"));
        doc.setQuantity(3);
        doc.setYear(1951);
        doc.setDescription("The Fabulous Hudson Hornet.");

        mater = new Vehicle();
        mater.setMake("Mater");
        mater.setModel("Tow Truck Deluxe");
        mater.setCategory(Category.TRUCK);
        mater.setPrice(new BigDecimal("32000.00"));
        mater.setQuantity(7);
        mater.setYear(2006);
        mater.setDescription("Like Tomater without the To.");

        vehicleRepository.save(mcqueen);
        vehicleRepository.save(doc);
        vehicleRepository.save(mater);
    }

    @Test
    public void shouldSaveAndFindVehicleById() {
        Optional<Vehicle> found = vehicleRepository.findById(mcqueen.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getModel()).isEqualTo("McQueen Special");
    }

    @Test
    public void shouldFindAllVehicles() {
        List<Vehicle> all = vehicleRepository.findAll();
        assertThat(all).hasSize(3);
    }

    @Test
    public void shouldSearchByMakeContaining() {
        List<Vehicle> found = vehicleRepository.search(
                "Light", null, null, null, null
        );
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getMake()).isEqualTo("Lightning");
    }

    @Test
    public void shouldSearchByCategory() {
        List<Vehicle> found = vehicleRepository.search(
                null, null, Category.SEDAN, null, null
        );
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getMake()).isEqualTo("Hudson");
    }

    @Test
    public void shouldSearchByPriceRange() {
        List<Vehicle> found = vehicleRepository.search(
                null, null, null, new BigDecimal("30000.00"), new BigDecimal("50000.00")
        );
        assertThat(found).hasSize(2); // Doc (45000) and Mater (32000)
    }

    @Test
    public void shouldSearchByMultipleCriteria() {
        List<Vehicle> found = vehicleRepository.search(
                "M", "Special", Category.COUPE, new BigDecimal("80000.00"), new BigDecimal("100000.00")
        );
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getModel()).isEqualTo("McQueen Special");
    }
}
