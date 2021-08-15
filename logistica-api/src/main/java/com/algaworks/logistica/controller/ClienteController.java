package com.algaworks.logistica.controller;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.logistica.model.Cliente;
import com.algaworks.logistica.repository.ClienteRepository;
import com.algaworks.logistica.service.CatalogoClienteService;

@RestController
@RequestMapping("/clientes") 
public class ClienteController {

	@PersistenceContext
	private EntityManager manager; 
	
	@Autowired
	private ClienteRepository clienteRepository; 
	
	@Autowired
	private CatalogoClienteService catalogoClienteService; 
	
	@GetMapping 
	public List<Cliente> listar(){
		return clienteRepository.findAll();
	}
	
	/*@GetMapping("/clientes/{clienteId}")
	public Cliente buscar(@PathVariable Long clienteId) {
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);
		
		return cliente.orElse(null); 
		// nesse caso se pesquisar por um ID que não exista o programa não retorna nada e o codigo fica 200, o correto é deixar o codigo 400
		// para isso é usuado o ResponseEntity que permite que manipulemos os retornos, conforme abaixo. 
	*/
	
	/*@GetMapping("/clientes/{clienteId}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);
		
		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get()); 
		}
		
		return ResponseEntity.notFound().build(); 
	}*/
	
	// deixando o codigo de cima mais enxuto e utilizando lambida. 
	
	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
		return clienteRepository.findById(clienteId)
				//.map(cliente -> ResponseEntity.ok(cliente)) // o .map vai mapear o cliente e caso esteja vazio ele vai serguir pro orElse
				.map(ResponseEntity::ok) // mesma coisa que o .map acima 
				.orElse(ResponseEntity.notFound().build()); 
	}
		
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) 
	public Cliente adiconar (@Valid @RequestBody Cliente cliente) {
//		return clienteRepository.save(cliente); 
		return catalogoClienteService.salvar(cliente); // utlizando o metodo criado na classe Service. 
	}
	
	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> atualizar (@PathVariable Long clienteId, @Valid @RequestBody Cliente cliente){
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build(); 
		}
		
		cliente.setId(clienteId);
		cliente = catalogoClienteService.salvar(cliente); 
		return ResponseEntity.ok(cliente); 
	}
	
	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Void> remover (@PathVariable Long clienteId){
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build(); 
		}
		
//		clienteRepository.deleteById(clienteId);
		catalogoClienteService.excluir(clienteId); 
		
		return ResponseEntity.noContent().build(); 
	}
}
