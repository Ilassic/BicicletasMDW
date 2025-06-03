package com.empresa.bicicleta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    
}
