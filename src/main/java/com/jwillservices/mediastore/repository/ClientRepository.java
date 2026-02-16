package com.jwillservices.mediastore.repository;

import com.jwillservices.mediastore.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // no es necesario en springboot si es que esta debajo o al lado de la clase anotada con @SpringBootApplication
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
}
