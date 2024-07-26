package com.empoweru.empowerupasswordrecoveryservice.repositories;

import com.empoweru.empowerupasswordrecoveryservice.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    @Transactional
    @Modifying
    @Query("UPDATE users u SET u.password = :password WHERE u.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);

    Boolean existsByEmail(String email);

}
