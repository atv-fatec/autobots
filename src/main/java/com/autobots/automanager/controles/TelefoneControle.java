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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	
	@Autowired
	private TelefoneRepositorio repositorioTelefone;

	@Autowired
	private ClienteRepositorio repositorio;
	
	@GetMapping("/telefones")
	public List<Telefone> obterTelefone() {
		return repositorioTelefone.findAll();
	}
	
	@GetMapping("/telefones/{id}")
	public List<Telefone> telefonesCliente(@PathVariable Long id) {
		Cliente alvo = repositorio.getById(id);
		
		return alvo.getTelefones();
	}

	@PutMapping("/cadastrar")
	public void cadastrarTelefone(@RequestBody Cliente atualizacao) {
		Cliente alvo = repositorio.getById(atualizacao.getId());
		
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		
		alvo.getTelefones().addAll(atualizacao.getTelefones());
		
		repositorio.save(alvo);
	}
	
	@PutMapping("/atualizar/{id}")
	public void atualizarTelefone(@PathVariable long id, @RequestBody Telefone atualizacao) {
		Cliente alvo = repositorio.getById(atualizacao.getId());

		List<Telefone> telefones = alvo.getTelefones();

		for (Telefone telefone : telefones) {
			if (telefone.getId() == id) {
				TelefoneAtualizador atualizador = new TelefoneAtualizador();
					
				//Telefone tel = atualizacao.getTelefones().get(0);
					
				atualizador.atualizar(telefone, atualizacao);
			}
		}

		repositorio.save(alvo);
	}
	
	@DeleteMapping("/excluir/{id}")
	public void excluirDocumento(@PathVariable long id, @RequestBody Cliente exclusao) {
		Telefone alvo = repositorioTelefone.getById(id);
		
		Cliente cliente = repositorio.getById(exclusao.getId());
		
		cliente.getTelefones().remove(alvo);
		
		repositorioTelefone.delete(alvo);
	}
}
