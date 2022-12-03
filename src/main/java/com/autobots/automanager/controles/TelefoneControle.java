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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLink;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.modelos.TelefoneSelecionador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	
	@Autowired
	private TelefoneRepositorio repositorioTelefone;
	
	@Autowired
	private TelefoneSelecionador selecionadorTelefone;

	@Autowired
	private ClienteRepositorio repositorio;
	
	@Autowired
	private ClienteSelecionador selecionador;
	
	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;
	
	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefone() {
		List<Telefone> telefones = repositorioTelefone.findAll();
		
		if (telefones.isEmpty()) {
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
			return resposta;
			
		} else {
			adicionadorLink.adicionarLink(telefones);
			
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(telefones, HttpStatus.FOUND);
			
			return resposta;
		}
	}
	
	@GetMapping("/telefone/{id}")
	public ResponseEntity<Telefone> telefonesCliente(@PathVariable Long id) {
		List<Telefone> telefones = repositorioTelefone.findAll();
		
		Telefone telefone = selecionadorTelefone.selecionar(telefones, id);
		
		if (telefone == null) {
			ResponseEntity<Telefone> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
			return resposta;
			
		} else {
			adicionadorLink.adicionarLink(telefone);
			
			ResponseEntity<Telefone> resposta = new ResponseEntity<Telefone>(telefone, HttpStatus.FOUND);
			
			return resposta;
		}
	}

	@PutMapping("/cadastrar") // só pra atualizacao
	public ResponseEntity<?> cadastrarTelefone(@RequestBody Cliente atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		
		Cliente alvo = repositorio.getById(atualizacao.getId());
		
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		
		if (alvo != null) {
			alvo.getTelefones().addAll(atualizacao.getTelefones());
			
			repositorio.save(alvo);
			
			status = HttpStatus.OK;
			
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarTelefone(@PathVariable long id, @RequestBody Telefone atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;

		Telefone alvo = repositorioTelefone.getById(id);
		
		if (alvo != null) {
			TelefoneAtualizador atualizador = new TelefoneAtualizador();
							
			//Telefone tel = atualizacao.getTelefones().get(0);
				
			atualizador.atualizar(alvo, atualizacao);
			
			repositorioTelefone.save(alvo);
			
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}

		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirDocumento(@PathVariable long id, @RequestBody Cliente exclusao) {
		HttpStatus status = HttpStatus.CONFLICT;
		
		Cliente alvo = repositorio.getById(exclusao.getId()); // informar o id por json
		
		if (alvo != null) {
			List<Telefone> telefones =  alvo.getTelefones(); //pega todos os documentos e salva numa variável
			
			for (Telefone telefone: telefones) { // vai percorrer pela variável inteira
			
				if (telefone.getId() == id) {
					alvo.getTelefones().remove(telefone);
				
					break;
				}
			}
			
			repositorio.save(alvo); // atualiza no banco
			
			status = HttpStatus.OK;
		} 
		
		return new ResponseEntity<>(status);
	}
}
