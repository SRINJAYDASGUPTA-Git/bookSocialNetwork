package com.srinjay.book_network.user;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;

import java.util.Optional;

public interface UserRepository extends JpaAttributeConverter<User, Long>{
    Optional<User> findByEmail(String email);
}
