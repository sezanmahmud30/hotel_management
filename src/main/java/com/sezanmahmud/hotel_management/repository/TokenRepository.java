package com.sezanmahmud.hotel_management.repository;

import com.sezanmahmud.hotel_management.entity.Token;
import com.sezanmahmud.hotel_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {


    Optional<Token> findByToken(String token);


    @Query("""
    Select t from Token t inner join User u on t.user.id=u.id
    where t.user.id = :userId and t.logout=false
""")

    List<Token> findAllTokenByUser(Long userId);


}
