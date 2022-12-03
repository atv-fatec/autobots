package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.modelos.EnderecoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	
	@Autowired
	private EnderecoRepositorio repositorioEndereco;
	
	@Autowired
	private EnderecoSelecionador selecionadorEndereco;
	
	@Autowired
	private ClienteRepositorio repositorio;
	
	@Autowired
	private ClienteSelecionador selecionador;
	
	@Autowired
	private AdicionadorLinkEndereco adicionadorLink;
	
	@GetMapping("/enderecos")
	public ResponseEntity<List<Endereco>> obterEndereco() {
		List<Endereco> enderecos = repositorioEndereco.findAll();
		
		if(enderecos.isEmpty()) {
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
			return resposta;
			
		} else {
			adicionadorLink.adicionarLink(enderecos);
			
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(enderecos, HttpStatus.FOUND);
			
			return resposta;
		}
	}
	
	@GetMapping("/endereco/{id}")
	public ResponseEntity<Endereco> enderecosCliente(@PathVariable long id) {
		List<Endereco> enderecos = repositorioEndereco.findAll();
		
		Endereco endereco = selecionadorEndereco.selecionar(enderecos, id);
		
		if (endereco == null) {
			ResponseEntity<Endereco> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
			return resposta;
			
		} else {
			adicionadorLink.adicionarLink(endereco);
			
			ResponseEntity<Endereco> resposta = new ResponseEntity<Endereco>(endereco, HttpStatus.FOUND);
			
			return resposta;
		}
	}
	
	@PutMapping("/cadastrar") // só pra atualizacao
	public ResponseEntity<?> cadastrarEndereco(@RequestBody Cliente atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		
		Cliente alvo = repositorio.getById(atualizacao.getId());
		
		if (alvo.getEndereco() == null) {
			alvo.setEndereco(atualizacao.getEndereco());
			
			repositorio.save(alvo);
			
			status = HttpStatus.OK;
			
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/atualizar") // só pra atualizacao
	public ResponseEntity<?> atualizarEndereco(@RequestBody Cliente atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		
		Cliente alvo = repositorio.getById(atualizacao.getId());
		
		if (alvo.getEndereco() != null) {
			alvo.setEndereco(atualizacao.getEndereco());
			
			repositorio.save(alvo);
			
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEndereco(@RequestBody Cliente exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		Cliente alvo = repositorio.getById(exclusao.getId());
		
		if (alvo != null) {
			repositorioEndereco.delete(alvo.getEndereco());
			
			alvo.setEndereco(null);
			
			repositorio.save(alvo); // atualiza no banco
			
			status = HttpStatus.OK;
			
		}
		
		return new ResponseEntity<>(status);
	}
}