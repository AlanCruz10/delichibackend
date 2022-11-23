package com.delichi.delichibackend.repositories;

import com.delichi.delichibackend.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IRestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = "select r.* "+
            "from restaurants r", nativeQuery = true)
    Optional<List<Restaurant>> findAllRestaurants();

    Optional<List<Restaurant>> findAllByName(String name);

}