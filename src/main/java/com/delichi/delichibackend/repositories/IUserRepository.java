package com.delichi.delichibackend.repositories;

import com.delichi.delichibackend.entities.Ceo;
import com.delichi.delichibackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailOrPhoneNumber(String email, Long phoneNumber);

    Optional<User> findByEmail(String email);

}
