package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	
	@Autowired
	private EnderecoRepositorio repositorioEndereco;

	@Autowired
	private ClienteRepositorio repositorio;
	
	@GetMapping("/enderecos")
	public List<Endereco> obterEndereco() {
		return repositorioEndereco.findAll();
	}
	
	@GetMapping("/endereco/{id}")
	public Endereco enderecosCliente(@PathVariable long id) {
		Cliente alvo = repositorio.getById(id);
		return alvo.getEndereco();
	}
	
	@PutMapping("/cadastrar") // só pra atualizacao
	public void cadastrarEndereco(@RequestBody Cliente atualizacao) {
		Cliente alvo = repositorio.getById(atualizacao.getId());
		
		if (alvo.getEndereco() == null) {
			alvo.setEndereco(atualizacao.getEndereco());
			
			repositorio.save(alvo);
		}
	}
	
	@PutMapping("/atualizar") // só pra atualizacao
	public void atualizarEndereco(@RequestBody Cliente atualizacao) {
		Cliente alvo = repositorio.getById(atualizacao.getId());
		
		alvo.setEndereco(atualizacao.getEndereco());
		
		repositorio.save(alvo);
	}
	
	@DeleteMapping("/excluir")
	public void excluirEndereco(@RequestBody Cliente exclusao) {
		Cliente alvo = repositorio.getById(exclusao.getId()); 
		
		repositorioEndereco.delete(alvo.getEndereco());
		
		alvo.setEndereco(null);
		
		repositorio.save(alvo); // atualiza no banco
	}
}
