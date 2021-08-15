package com.algaworks.logistica.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.logistica.exception.NegocioException;
import com.algaworks.logistica.model.Cliente;
import com.algaworks.logistica.repository.ClienteRepository;

@Service
public class CatalogoClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Transactional
	public Cliente salvar (Cliente cliente) {
		boolean emailEmUso = clienteRepository.findByEmail(cliente.getEmail())
				.stream()
				.anyMatch(clienteExistente -> !clienteExistente.equals(cliente));
		
		if (emailEmUso) {
			throw new NegocioException("Já existe um usuario cadastrado com este e-mail."); 
		}
		
		return clienteRepository.save(cliente); 
	}
	
	@Transactional
	public void excluir(Long clienteId) {
		clienteRepository.deleteById(clienteId); 
	}
}
