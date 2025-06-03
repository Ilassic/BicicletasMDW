package com.empresa.bicicleta.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    public List<Cliente> listaClientes(){
        List<Cliente> cliente = clienteRepository.findAll();
        return cliente;
    }

    public Cliente registro(Cliente cliente){
        Cliente cl = new Cliente();
        cl.setDni(cliente.getDni());

        clienteRepository.save(cl);
        return cl;
    }

    public boolean login(String correo, String contra) {
   
        return true;
    }
}
