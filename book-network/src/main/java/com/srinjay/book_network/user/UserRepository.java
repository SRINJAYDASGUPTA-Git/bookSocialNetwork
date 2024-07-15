package com.srinjay.book_network.user;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaAttributeConverter<User, Long>{
    Optional<User> findByEmail(String email);
}
