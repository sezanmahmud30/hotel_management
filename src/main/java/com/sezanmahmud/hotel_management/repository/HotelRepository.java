package com.sezanmahmud.hotel_management.repository;

import com.sezanmahmud.hotel_management.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {


    @Query("Select h from Hotel h where h.location.name= :locationName")
    List<Hotel> findHotelByLocationName(@Param("locationName") String locationName);


    Optional<Hotel> findHotelByName(String name);

}
