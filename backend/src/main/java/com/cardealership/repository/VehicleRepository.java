package com.cardealership.repository;

import com.cardealership.entity.Category;
import com.cardealership.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v FROM Vehicle v WHERE " +
            "(cast(:make as string) IS NULL OR LOWER(v.make) LIKE LOWER(CONCAT('%', cast(:make as string), '%'))) AND " +
            "(cast(:model as string) IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', cast(:model as string), '%'))) AND " +
            "(:category IS NULL OR v.category = :category) AND " +
            "(:minPrice IS NULL OR v.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR v.price <= :maxPrice)")
    List<Vehicle> search(
            @Param("make") String make,
            @Param("model") String model,
            @Param("category") Category category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );
}
