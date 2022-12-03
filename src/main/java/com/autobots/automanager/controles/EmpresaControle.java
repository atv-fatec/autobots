package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.EmpresaAtualizador;
import com.autobots.automanager.componentes.EmpresaSelecionador;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.hateoas.EmpresaHateoas;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/empresa")

public class EmpresaControle {
	
	@Autowired
	private RepositorioEmpresa repositorioEmp;
	
	@Autowired
	private EmpresaSelecionador selecionadorEmp;
	
	@Autowired
	
	private EmpresaHateoas empresaLink;
	
	@GetMapping("/pegarTodasEmpresas")
	public ResponseEntity<List<Empresa>> obterEmpresas() {
		List<Empresa> empresas = repositorioEmp.findAll();
		if (empresas.isEmpty()) {
			ResponseEntity<List<Empresa>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			empresaLink.AdicionarLink(empresas);
			ResponseEntity<List<Empresa>> resposta = new ResponseEntity<>(empresas, HttpStatus.FOUND);
			return resposta;
		}

	}
	
	@GetMapping("/empresa/{id}")
	public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
		List<Empresa> empresas = repositorioEmp.findAll();
		Empresa empresa = selecionadorEmp.selecionar(empresas, id);
		if (empresa == null) {
			ResponseEntity<Empresa> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			empresaLink.AdicionarLink(empresa);
			ResponseEntity<Empresa> resposta = new ResponseEntity<Empresa>(empresa, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PostMapping("/empresa/cadastro")
	public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (empresa.getId() == null) {
			empresa.setCadastro(new Date());
			repositorioEmp.save(empresa);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/empresa/atualizar")
	public ResponseEntity<?> atualizarEmpresa(@RequestBody Empresa atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Empresa empresa = repositorioEmp.getById(atualizacao.getId());
		if (empresa != null) {
			EmpresaAtualizador atualizador = new EmpresaAtualizador();
			atualizador.atualizar(empresa, atualizacao);
			repositorioEmp.save(empresa);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("empresa/excluir/{idEmpresa}")
	public ResponseEntity<?> excluirEmpresa(@PathVariable Long idEmpresa){
		Empresa verificacao = repositorioEmp.findById(idEmpresa).orElse(null);
		
		if(verificacao == null) {
			return new ResponseEntity<>("Empresa não econtrada, tente novamente", HttpStatus.NOT_FOUND);
		}else {
			repositorioEmp.deleteById(idEmpresa);
			return new ResponseEntity<>("Empresa excluida com sucesso!", HttpStatus.ACCEPTED);
		}
	}
	
	
}
	
