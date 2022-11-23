package com.delichi.delichibackend.repositories;

import com.delichi.delichibackend.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IZoneRepository extends JpaRepository<Zone, Long> {

}
