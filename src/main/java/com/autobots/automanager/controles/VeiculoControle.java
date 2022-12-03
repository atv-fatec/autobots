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

import com.autobots.automanager.componentes.VeiculoAtualizador;
import com.autobots.automanager.componentes.VeiculoSelecionador;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.hateoas.VeiculoHateoas;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/veiculo")

public class VeiculoControle {
	
	@Autowired
	private RepositorioVeiculo repositorioVeic;
	
	@Autowired
	private RepositorioUsuario repositorioUsu;
	
	@Autowired
	private RepositorioVenda repositorioVenda;
	
	@Autowired
	private VeiculoSelecionador selecionadorVeic;
	
	@Autowired
	private VeiculoHateoas veiculoLink;
	
	@Autowired
	private VeiculoAtualizador veiculoAtualizador;
	
	@GetMapping("/pegarTodosVeiculos")
	public ResponseEntity<List<Veiculo>> obterVeiculos() {
		List<Veiculo> veiculos = repositorioVeic.findAll();
		if (veiculos.isEmpty()) {
			ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			veiculoLink.AdicionarLink(veiculos);
			ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<>(veiculos, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@GetMapping("/veiculo/{id}")
	public ResponseEntity<Veiculo> obterVeiculo(@PathVariable long id) {
		List<Veiculo> veiculos = repositorioVeic.findAll();
		Veiculo veiculo = selecionadorVeic.selecionar(veiculos, id);
		if (veiculo == null) {
			ResponseEntity<Veiculo> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			veiculoLink.AdicionarLink(veiculo);
			ResponseEntity<Veiculo> resposta = new ResponseEntity<Veiculo>(veiculo, HttpStatus.FOUND);
			return resposta;
		}
	}

    @PostMapping("/cadastro/{id}")
	public ResponseEntity<?> cadastroVeiculo(@PathVariable Long id, @RequestBody Veiculo veiculo){
		List<Usuario> usuarios = repositorioUsu.findAll();
		Usuario alvo = selecionador.selecionar(usuarios, id);
		if(alvo != null) {
			alvo.getVeiculos().add(veiculo);
			veiculo.setProprietario(alvo);
			repositorioVeic.save(veiculo);
			return new ResponseEntity<>("Veiculo cadastrado no usuario: " + alvo.getNome(), HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("Usuario não encontrado", HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/veiculo/atualizar")
	public ResponseEntity<?> atualizarVeiculo(@RequestBody Veiculo atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Veiculo veiculo = repositorioVeic.getById(atualizacao.getId());
		if (veiculo != null) {
			VeiculoAtualizador atualizador = new VeiculoAtualizador();
			atualizador.atualizar(veiculo, atualizacao);
			repositorioVeic.save(veiculo);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}	
	
	@DeleteMapping("/veiculo/excluir/{idVeiculo}")
	public ResponseEntity<?> excluirVeiculo(@PathVariable Long idVeiculo){
		List<Usuario> usuarios = repositorioUsu.findAll();
		Veiculo verificacao = repositorioVeic.findById(idVeiculo).orElse(null);
		
		if(verificacao == null) {

			return new ResponseEntity<>("Veículo não encontrado!",HttpStatus.NOT_FOUND);

		}else {
			for(Usuario usuario: repositorioUsu.findAll()) {
				if(!usuario.getVeiculos().isEmpty()) {
					for(Veiculo veiculoUsuario: usuario.getVeiculos()) {
						if(veiculoUsuario.getId() == idVeiculo) {
							for(Usuario usuarioRegistrado: usuarios) {
								usuarioRegistrado.getVeiculos().remove(veiculoUsuario);
							}
						}
					}
				}
			}
			
			for(Venda venda: repositorioVenda.findAll()) {
				if(venda.getVeiculo() != null) {
					if(venda.getVeiculo().getId() == idVeiculo) {
						venda.setVeiculo(null);
					}
				}
			}

			usuarios = repositorioUsu.findAll();
			repositorioVenda.deleteById(idVeiculo);
			return new ResponseEntity<>("Veículo excluido com sucesso!",HttpStatus.ACCEPTED);
		}
	}

}
