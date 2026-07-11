package com.cardealership.repository;

import com.cardealership.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p FROM Purchase p JOIN FETCH p.user JOIN FETCH p.vehicle WHERE p.user.id = :userId")
    List<Purchase> findByUserId(@Param("userId") Long userId);
}
