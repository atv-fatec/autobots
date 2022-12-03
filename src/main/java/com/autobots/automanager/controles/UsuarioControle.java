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

import com.autobots.automanager.componentes.EmpresaSelecionador;
import com.autobots.automanager.componentes.UsuarioAtualizador;
import com.autobots.automanager.componentes.UsuarioSelecionador;
import com.autobots.automanager.componentes.VeiculoSelecionador;
import com.autobots.automanager.componentes.VendaSelecionador;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.hateoas.UsuarioHateoas;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/usuario")

public class UsuarioControle {

	@Autowired 
	private RepositorioUsuario repositorioUsu;
	
	@Autowired 
	private RepositorioVenda repositorioVenda;
	
	@Autowired 
	private RepositorioEmpresa repositorioEmp;
	
	@Autowired 
	private RepositorioVeiculo repositorioVeic;
	
	@Autowired
	private UsuarioSelecionador selecionador;
	
	@Autowired
	private VeiculoSelecionador selecionadorVeic;
		
	@Autowired
	private UsuarioHateoas usuarioLink;
	
	@GetMapping("/pegarTodosUsuarios")
		public ResponseEntity<List<Usuario>> obterUsuarios() {
			List<Usuario> usuarios = repositorioUsu.findAll();
			if (usuarios.isEmpty()) {
				ResponseEntity<List<Usuario>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return resposta;
			} else {
				usuarioLink.AdicionarLink(usuarios);
				ResponseEntity<List<Usuario>> resposta = new ResponseEntity<>(usuarios, HttpStatus.FOUND);
				return resposta;
			}
	}

	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
		List<Usuario> usuarios = repositorioUsu.findAll();
		Usuario usuario = selecionador.selecionar(usuarios, id);
		if (usuario == null) {
			ResponseEntity<Usuario> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			usuarioLink.AdicionarLink(usuario);
			ResponseEntity<Usuario> resposta = new ResponseEntity<Usuario>(usuario, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/usuario/cadastro")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (usuario.getId() == null) {
			repositorioUsu.save(usuario);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/usuario/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Usuario usuario = repositorioUsu.getById(atualizacao.getId());
		if (usuario != null) {
			UsuarioAtualizador atualizador = new UsuarioAtualizador();
			atualizador.atualizar(usuario, atualizacao);
			repositorioUsu.save(usuario);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/usuario/criarCredencial/{Id}")
	public ResponseEntity<?> criarCredencial(@RequestBody CredencialUsuarioSenha Cred, @PathVariable Long Id) {
		HttpStatus status = HttpStatus.CONFLICT;
		Usuario usuario = repositorioUsu.getById(Id);
		if (usuario != null) {
			CredencialUsuarioSenha credencial = new CredencialUsuarioSenha(); 
			credencial.setNomeUsuario(Cred.getNomeUsuario());
			credencial.setSenha(Cred.getSenha());
			credencial.setCriacao(new Date());
			credencial.setInativo(false);
			credencial.setUltimoAcesso(new Date());
			usuario.getCredenciais().add(credencial);
			repositorioUsu.save(usuario);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/deletar/{idUsuario}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Long idUsuario){
		Usuario verificacao = repositorioUsu.findById(idUsuario).orElse(null);
		if(verificacao == null) {
			return new ResponseEntity<String>("Usuario não encontrado!",HttpStatus.NOT_FOUND);
		}else {
			
			//venda
			for(Venda venda: repositorioVenda.findAll()) {
				if(venda.getCliente() != null) {		
					if(venda.getCliente().getId() == idUsuario) {
						venda.setCliente(null);
						repositorioVenda.save(venda);
					}
				}
				if(venda.getFuncionario() != null) {	
					if(venda.getFuncionario().getId() == idUsuario) {
						venda.setFuncionario(null);
						repositorioVenda.save(venda);
					}
				}
			}
			
			//veiculo
			for(Veiculo veiculo: repositorioVeic.findAll()) {
				if(veiculo.getProprietario() != null) {	
					if(veiculo.getProprietario().getId() == idUsuario) {
						veiculo.setProprietario(null);
						repositorioVeic.save(veiculo);
					}
				}
			}
			
			for(Empresa empresa: repositorioEmp.findAll()) {
				if(!empresa.getUsuarios().isEmpty()) {
					for(Usuario usuario: empresa.getUsuarios()) {
						if(usuario.getId() == idUsuario) {
							empresa.getUsuarios().remove(usuario);
							repositorioEmp.save(empresa);
						}
						break;
					}
				}
			}
			
			repositorioUsu.deleteById(idUsuario);
			return new ResponseEntity<>("Usuario deletado com sucesso!", HttpStatus.OK);
		}
	}
}
