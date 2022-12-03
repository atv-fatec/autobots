package com.autobots.automanager.controles;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.ServicoAtualizador;
import com.autobots.automanager.componentes.ServicoSelecionador;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.hateoas.ServicoHateoas;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/servico")

public class ServicoControle {
	
	@Autowired
	private ServicoSelecionador selecionadorServ;
	
	@Autowired
	private RepositorioServico repositorioServ;
	
	@Autowired
	private RepositorioEmpresa repositorioEmp;
	
	@Autowired
	private RepositorioVenda repositorioVenda;
	
	@Autowired
	private ServicoHateoas servicoLink;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
	@GetMapping("/pegarTodosServicos")
	public ResponseEntity<List<Servico>> obterServicos() {
		List<Servico> servicos = repositorioServ.findAll();
		if (servicos.isEmpty()) {
			ResponseEntity<List<Servico>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			servicoLink.AdicionarLink(servicos);
			ResponseEntity<List<Servico>> resposta = new ResponseEntity<>(servicos, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
	@GetMapping("/servico/{id}")
	public ResponseEntity<Servico> obterServico(@PathVariable long id) {
		List<Servico> servicos = repositorioServ.findAll();
		Servico servico = selecionadorServ.selecionar(servicos, id);
		if (servico == null) {
			ResponseEntity<Servico> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			servicoLink.AdicionarLink(servico);
			ResponseEntity<Servico> resposta = new ResponseEntity<Servico>(servico, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@PostMapping("/servico/cadastro")
	public ResponseEntity<?> cadastrarServico(@RequestBody Servico servico) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (servico.getId() == null) {
			repositorioServ.save(servico);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@PutMapping("/servico/atualizar")
	public ResponseEntity<?> atualizarServico(@RequestBody Servico atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Servico servico = repositorioServ.getById(atualizacao.getId());
		if (servico != null) {
			ServicoAtualizador atualizador = new ServicoAtualizador();
			atualizador.atualizar(servico, atualizacao);
			repositorioServ.save(servico);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@DeleteMapping("servico/excluir/{idServico}")
	public ResponseEntity<?> excluirServico(@PathVariable Long idServico){
		List<Empresa> empresas = repositorioEmp.findAll();
		List<Venda> vendas = repositorioVenda.findAll();
		Servico verificador = repositorioServ.findById(idServico).orElse(null);
		
		if(verificador == null) {
			return new ResponseEntity<>("Servico não encontrado, tente novamente", HttpStatus.NOT_FOUND);
		}else {
			for(Empresa empresa: repositorioEmp.findAll()) {
				if(empresa.getServicos().size() > 0) {
					for(Servico servicoEmpresa: empresa.getServicos()) {
						if(servicoEmpresa.getId() == idServico) {
							for(Empresa empresaRegistrado:empresas) {
								empresaRegistrado.getServicos().remove(servicoEmpresa);
							}
						}
					}
				}
			}

			for(Venda venda: repositorioVenda.findAll()) {
				if(venda.getServicos().size() > 0) {
					for(Servico servicoVenda: venda.getServicos()) {
						if(servicoVenda.getId() == idServico) {
							for(Venda vendaRegistrada: vendas) {
								vendaRegistrada.getServicos().remove(servicoVenda);
							}
						}
					}
				}
			}
			
			repositorioServ.deleteById(idServico);
			return new ResponseEntity<>("Serviço excluído com sucesso!",HttpStatus.ACCEPTED);
		}
	}
	
	

}
