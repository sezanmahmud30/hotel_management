package com.sezanmahmud.hotel_management.repository;


import com.sezanmahmud.hotel_management.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,Integer>{

}
