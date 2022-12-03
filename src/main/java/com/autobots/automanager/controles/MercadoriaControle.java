package com.autobots.automanager.controles;

import java.util.Date;

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

import com.autobots.automanager.componentes.EmpresaSelecionador;
import com.autobots.automanager.componentes.MercadoriaAtualizador;
import com.autobots.automanager.componentes.MercadoriaSelecionador;
import com.autobots.automanager.componentes.UsuarioSelecionador;
import com.autobots.automanager.componentes.VendaSelecionador;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.hateoas.MercadoriaHateoas;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/mercadoria")

public class MercadoriaControle {
	
	@Autowired
	private RepositorioMercadoria repositorioMerc;
	
	@Autowired
	private MercadoriaSelecionador selecionadorMerc;
	
	@Autowired
	private EmpresaSelecionador selecionadorEmp;
	
	@Autowired
	private MercadoriaHateoas mercadoriaLink;
	
	@Autowired
	private RepositorioServico repositorioServ;
	
	@Autowired
	private RepositorioUsuario repositorioUsu;
	
	@Autowired
	private RepositorioEmpresa repositorioEmp;
	
	@Autowired
	private RepositorioVenda repositorioVenda;
	
	@Autowired
	private UsuarioSelecionador selecionadorUsuario;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
	@GetMapping("/pegarTodasMercadorias")
	public ResponseEntity<List<Mercadoria>> obterMercadorias() {
		List<Mercadoria> mercadorias = repositorioMerc.findAll();
		if (mercadorias.isEmpty()) {
			ResponseEntity<List<Mercadoria>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			mercadoriaLink.AdicionarLink(mercadorias);
			ResponseEntity<List<Mercadoria>> resposta = new ResponseEntity<>(mercadorias, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
	@GetMapping("/mercadoria/{id}")
	public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable long id) {
		List<Mercadoria> mercadorias = repositorioMerc.findAll();
		Mercadoria mercadoria = selecionadorMerc.selecionar(mercadorias, id);
		if (mercadorias == null) {
			ResponseEntity<Mercadoria> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			mercadoriaLink.AdicionarLink(mercadoria);
			ResponseEntity<Mercadoria> resposta = new ResponseEntity<Mercadoria>(mercadoria, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@PostMapping("/mercadoria/cadastro")
	public ResponseEntity<?> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (mercadoria.getId() == null) {
			mercadoria.setCadastro(new Date());
			repositorioMerc.save(mercadoria);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@PutMapping("/mercadoria/atualizar")
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Mercadoria atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Mercadoria mercadoria = repositorioMerc.getById(atualizacao.getId());
		if (mercadoria != null) {
			MercadoriaAtualizador atualizador = new MercadoriaAtualizador();
			atualizador.atualizar(mercadoria, atualizacao);
			repositorioMerc.save(mercadoria);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@DeleteMapping("/deletar/{idMercadoria}")
	  public ResponseEntity<?> deletarMercadoria(@PathVariable Long idMercadoria) {
	    List<Mercadoria> mercadorias = repositorioMerc.findAll();
	    List<Usuario> usuarios = repositorioUsu.findAll();
	    List<Empresa> empresas = repositorioEmp.findAll();
	    List<Venda> vendas = repositorioVenda.findAll();
	    Mercadoria selecionado = selecionadorMerc.selecionar(
	      mercadorias,
	      idMercadoria
	    );
	    if (selecionado == null) {
	      return new ResponseEntity<>(
	        "Essa mercadoria é inexistente, tente outro ID",
	        HttpStatus.NOT_FOUND
	      );
	    } else {
	      for (Usuario mercadoriaUsuario : usuarios) {
	        for (Mercadoria userMercadoria : mercadoriaUsuario.getMercadorias()) {
	          if (userMercadoria.getId() == idMercadoria) {
	            deletarMercadoria(
	              mercadoriaUsuario.getId(),
	              idMercadoria
	            );
	          }
	        }
	      }
	      for (Empresa mercadoriaEmpresa : empresas) {
	        for (Mercadoria empresaMercadoria : mercadoriaEmpresa.getMercadorias()) {
	          if (empresaMercadoria.getId() == idMercadoria) {
	        	  deletarMerc(
	              mercadoriaEmpresa.getId(),
	              idMercadoria
	            );
	          }
	        }
	      }
	      for (Venda mercadoriaVenda : vendas) {
	        for (Mercadoria vendaMercadoria : mercadoriaVenda.getMercadorias()) {
	          if (vendaMercadoria.getId() == idMercadoria) {
	            deletaMercadoria(
	              mercadoriaVenda.getId(),
	              idMercadoria
	            );
	          }
	        }
	      }
	      repositorioMerc.deleteById(idMercadoria);
	      return new ResponseEntity<>("Mercadoria deletada com sucesso!", HttpStatus.OK);
	    }
	  }
	
	private void deletarMercadoria (Long UsuarioId, Long MercadoriaId) {
		List<Usuario> todos = repositorioUsu.findAll();
		UsuarioSelecionador select = new UsuarioSelecionador();
		Usuario selecionado = select.selecionar(todos, UsuarioId);
		Mercadoria mercadoria = repositorioMerc.getById(MercadoriaId); 
		if(selecionado.getId() == UsuarioId) {
			selecionado.getMercadorias().remove(mercadoria);
		}
	}
	
	private void deletarMerc(Long EmpresaId, Long MercadoriaId) {
		List<Empresa> todos = repositorioEmp.findAll();
		EmpresaSelecionador select = new EmpresaSelecionador();
		Empresa selecionado = select.selecionar(todos, EmpresaId);
		Mercadoria mercadoria = repositorioMerc.getById(MercadoriaId); 
		if(selecionado.getId() == EmpresaId) {
			selecionado.getMercadorias().remove(mercadoria);
		}
	}
	
	private void deletaMercadoria(Long VendaId, Long MercadoriaId) {
		List<Venda> todos = repositorioVenda.findAll();
		VendaSelecionador select = new VendaSelecionador();
		Venda selecionado = select.selecionar(todos, VendaId);
		Mercadoria mercadoria = repositorioMerc.getById(MercadoriaId); 
		if(selecionado.getId() == VendaId) {
			selecionado.getMercadorias().remove(mercadoria);
		}
	}
	
}
