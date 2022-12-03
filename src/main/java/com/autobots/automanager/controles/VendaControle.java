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

import com.autobots.automanager.componentes.VendaAtualizador;
import com.autobots.automanager.componentes.VendaSelecionador;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.hateoas.VendaHateoas;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/venda")


public class VendaControle {
	
	@Autowired
	private RepositorioVenda repositorioVenda;
	
	@Autowired
	private RepositorioEmpresa repositorioEmp;
	
	@Autowired
	private RepositorioVeiculo repositorioVeic;
	
	@Autowired
	private RepositorioUsuario repositorioUsu;
	
	@Autowired
	private VendaHateoas vendaLink;
	
	@Autowired
	private VendaSelecionador selecionadorVenda;
	
	@Autowired
	private VendaAtualizador vendaAtualizador;

	@GetMapping("/pegarTodasVendas")
	public ResponseEntity<List<Venda>> obterVendas() {
		List<Venda> vendas = repositorioVenda.findAll();
		if (vendas.isEmpty()) {
			ResponseEntity<List<Venda>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			vendaLink.AdicionarLink(vendas);
			ResponseEntity<List<Venda>> resposta = new ResponseEntity<>(vendas, HttpStatus.FOUND);
			return resposta;
		}
    }

	@GetMapping("/venda/{id}")
	public ResponseEntity<Venda> obterVenda(@PathVariable long id) {
		List<Venda> vendas = repositorioVenda.findAll();
		Venda venda = selecionadorVenda.selecionar(vendas, id);
		if (vendas == null) {
			ResponseEntity<Venda> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			vendaLink.AdicionarLink(venda);
			ResponseEntity<Venda> resposta = new ResponseEntity<Venda>(venda, HttpStatus.FOUND);
			return resposta;
		}
	}

    @PostMapping("/cadastro/{id}")
	  public ResponseEntity<?> cadastroVenda(@PathVariable Long id, @RequestBody Venda vendas) {
	    List<Empresa> selecionarEmpresa = repositorioEmp.findAll();
	    
	    Empresa selecionada = selecionadorEmpresa.selecionar(selecionarEmpresa, id);
	    
	    if (selecionada != null) {
	      Usuario clienteSelecionado = repositorioUsu.findById(vendas.getCliente().getId()).orElse(null);
	        
	      Usuario funcionarioSelecionado = repositorioUsu.findById(vendas.getFuncionario().getId()).orElse(null);
	      
	      Veiculo veiculoSelecionador = repositorioVeic.findById(vendas.getVeiculo().getId()).orElse(null);
	      
	      for (Mercadoria merc : vendas.getMercadorias()) {
	    	  vendas.getMercadorias().clear();
		        
		      Mercadoria novaMercadoria = new Mercadoria();
		        
		      novaMercadoria.setDescricao(merc.getDescricao());
		        
		      novaMercadoria.setCadastro(merc.getCadastro());
		        
		      novaMercadoria.setFabricao(merc.getFabricao());
		        
		      novaMercadoria.setNome(merc.getNome());
		        
		      novaMercadoria.setQuantidade(merc.getQuantidade());
		        
		      novaMercadoria.setValidade(merc.getValidade());
		        
		      novaMercadoria.setValor(merc.getValor());
		        
		      vendas.getMercadorias().add(novaMercadoria);
		 }
		      
		 for (Servico serv : vendas.getServicos()) {
			  Servico novoServico = new Servico();
		        
		      novoServico.setDescricao(serv.getDescricao());
		        
		      novoServico.setNome(serv.getNome());
		        
		      novoServico.setValor(serv.getValor());
		        
		      vendas.getServicos().add(novoServico);
		 }
		      
		 funcionarioSelecionado.getVendas().add(vendas);
		      
		 vendas.setCliente(clienteSelecionado);
		      
		 vendas.setFuncionario(funcionarioSelecionado);
		      
		 vendas.setVeiculo(veiculoSelecionador);
		      
		 selecionada.getVendas().add(vendas);
		      
		 repositorioEmp.save(selecionada);
		      
		 return new ResponseEntity<>("Serviço cadastrado na empresa: " + selecionada.getNomeFantasia(), HttpStatus.CREATED);
	  } else {
	     return new ResponseEntity<>(
	      "Empresa não encontrada",
	        HttpStatus.NOT_FOUND
	      );
	  }
	 }
	
	@PutMapping("/venda/atualizar")
	public ResponseEntity<?> atualizarVenda(@RequestBody Venda atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Venda venda = repositorioVenda.getById(atualizacao.getId());
		if (venda != null) {
			VendaAtualizador atualizador = new VendaAtualizador();
			atualizador.atualizar(venda, atualizacao);
			repositorioVenda.save(venda);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}	
	
	@DeleteMapping("venda/excluir/{idVenda}")
	public ResponseEntity<?> excluirVenda(@PathVariable Long idVenda) {
		List<Empresa> empresas = repositorioEmp.findAll();
		List<Usuario> usuarios = repositorioUsu.findAll();
		List<Veiculo> veiculos = repositorioVeic.findAll();
		Venda verificador = repositorioVenda.findById(idVenda).orElse(null);

		if (verificador == null) {
			return new ResponseEntity<>("Venda não encontrada!", HttpStatus.NOT_FOUND);
		} else {

			for (Empresa empresa : repositorioEmp.findAll()) {
				if (!empresa.getVendas().isEmpty()) {
					for (Venda vendaEmpresa : empresa.getVendas()) {
						if (vendaEmpresa.getId() == idVenda) {
							for (Empresa empresaRegistrada : empresas) {
								empresaRegistrada.getVendas().remove(vendaEmpresa);
							}
						}
					}
				}
			}

			for (Usuario usuario : repositorioUsu.findAll()) {
				if (!usuario.getVendas().isEmpty()) {
					for (Venda vendaUsuario : usuario.getVendas()) {
						if (vendaUsuario.getId() == idVenda) {
							for (Usuario usuarioRegistrado : usuarios) {
								usuarioRegistrado.getVendas().remove(vendaUsuario);
							}
						}
					}
				}
			}

			for (Veiculo veiculo : repositorioVeic.findAll()) {
				if (!veiculo.getVendas().isEmpty()) {
					for (Venda vendaVeiculo : veiculo.getVendas()) {
						if (vendaVeiculo.getId() == idVenda) {
							for (Veiculo veiculoRegistrado : veiculos) {
								veiculoRegistrado.getVendas().remove(vendaVeiculo);
							}
						}
					}
				}
			}

			empresas = repositorioEmp.findAll();
			usuarios = repositorioUsu.findAll();
			veiculos = repositorioVeic.findAll();
			repositorioVenda.deleteById(idVenda);
			return new ResponseEntity<>("Venda excluída com sucesso!", HttpStatus.ACCEPTED);
		}
	}
	
}
