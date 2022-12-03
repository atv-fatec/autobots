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
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

	// primeiro vem o tipo da variável
	@Autowired
	private DocumentoRepositorio repositorioDocumento;

	@Autowired
	private DocumentoSelecionador selecionadorDocumento;

	@Autowired
	private ClienteRepositorio repositorio;

	@Autowired
	private ClienteSelecionador selecionador;

	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;

	@GetMapping("/documentos")
	public ResponseEntity<List<Documento>> docs() {
		List<Documento> documentos = repositorioDocumento.findAll();

		if (documentos.isEmpty()) {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);

			return resposta;

		} else {
			adicionadorLink.adicionarLink(documentos);

			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(documentos, HttpStatus.FOUND);

			return resposta;
		}

	}

	@GetMapping("/documentos/{id}")
	public ResponseEntity<Documento> documentosCliente(@PathVariable long id) {
		List<Documento> documentos = repositorioDocumento.findAll();

		Documento documento = selecionadorDocumento.selecionar(documentos, id);

		if (documento == null) {
			ResponseEntity<Documento> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);

			return resposta;

		} else {
			adicionadorLink.adicionarLink(documento);

			ResponseEntity<Documento> resposta = new ResponseEntity<Documento>(documento, HttpStatus.FOUND);

			return resposta;
		}

	}

	@PutMapping("/cadastrar") // só pra atualizacao
	public ResponseEntity<?> cadastrarDocumento(@RequestBody Cliente atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;

		Cliente alvo = repositorio.getById(atualizacao.getId());

		DocumentoAtualizador atualizador = new DocumentoAtualizador();

		if (alvo != null) {
			alvo.getDocumentos().addAll(atualizacao.getDocumentos());

			repositorio.save(alvo);

			status = HttpStatus.OK;

		} else {
			status = HttpStatus.BAD_REQUEST;
		}

		return new ResponseEntity<>(status);
	}

	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarDocumento(@PathVariable long id, @RequestBody Documento atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;

		Cliente alvo = repositorio.getById(atualizacao.getId());

		List<Documento> documentos = alvo.getDocumentos();

		if (alvo != null) {

			for (Documento documento : documentos) {
				if (documento.getId() == id) {
					DocumentoAtualizador atualizador = new DocumentoAtualizador();
					
					//Documento doc = atualizacao.getDocumentos().get(0);
					
					atualizador.atualizar(documento, atualizacao);
				}
			}

			repositorio.save(alvo);
			
			status = HttpStatus.OK;

		} else {
			status = HttpStatus.BAD_REQUEST;
		}

		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirDocumento(@PathVariable long id, @RequestBody Cliente exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		Cliente alvo = repositorio.getById(exclusao.getId()); 

		if (alvo != null) {
			List<Documento> documentos = alvo.getDocumentos(); // pega todos os documentos e salva numa variável

			for (Documento documento : documentos) { // vai percorrer pela variável inteira

				if (documento.getId() == id) {
					alvo.getDocumentos().remove(documento);

					break;
				}
			}

			repositorio.save(alvo); // atualiza no banco

			status = HttpStatus.OK;
		}

		return new ResponseEntity<>(status);
	}
}