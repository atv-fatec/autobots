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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

	// primeiro vem o tipo da variável
	@Autowired
	private DocumentoRepositorio repositorioDocumento;
	
	@Autowired
	private ClienteRepositorio repositorio;
	
	@GetMapping("/documentos")
	public List<Documento> docs() {
		return repositorioDocumento.findAll();
	}
	
	@GetMapping("/documentos/{id}")
	public List<Documento> documentosCliente(@PathVariable long id) {
		Cliente alvo = repositorio.getById(id);
		return alvo.getDocumentos();
	}
	
	@PutMapping("/cadastrar")
	public void cadastrarDocumento(@RequestBody Cliente atualizacao) {
		Cliente alvo = repositorio.getById(atualizacao.getId());

		//DocumentoAtualizador atualizador = new DocumentoAtualizador();
		
		alvo.getDocumentos().addAll(atualizacao.getDocumentos());

		repositorio.save(alvo);
	}

	@PutMapping("/atualizar/{id}")
	public void atualizarDocumento(@PathVariable long id, @RequestBody Documento atualizacao) {
		Cliente alvo = repositorio.getById(atualizacao.getId());

		List<Documento> documentos = alvo.getDocumentos();

		for (Documento documento : documentos) {
			if (documento.getId() == id) {
				DocumentoAtualizador atualizador = new DocumentoAtualizador();
				
				//Documento doc = atualizacao.getDocumentos().get(0);
				
				atualizador.atualizar(documento, atualizacao);
			}
		}

		repositorio.save(alvo);
	}
	
	@DeleteMapping("/excluir/{id}")
	public void excluirDocumento(@PathVariable long id, @RequestBody Cliente exclusao) {
		Cliente alvo = repositorio.getById(exclusao.getId()); 
		
		List<Documento> documentos =  alvo.getDocumentos(); //pega todos os documentos e salva numa variável
		
		for (Documento documento : documentos) { // vai percorrer pela variável inteira
			if (documento.getId() == id) {
				alvo.getDocumentos().remove(documento);
				break;
			}
		}
		repositorio.save(alvo); // atualiza no banco
	}
}