package com.autobots.automanager.componentes;

import java.util.List;
import java.util.Set;

import com.autobots.automanager.entitades.Documento;

public class DocumentoAtualizador {

	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Documento documento, Documento atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getTipo().toString())) {
				documento.setTipo(atualizacao.getTipo());
			}
			if (!verificador.verificar(atualizacao.getNumero())) {
				documento.setNumero(atualizacao.getNumero());
			}
		}
	}

	public void atualizar(Set<Documento> documentos, Set<Documento> atualizacoes) {
		for (Documento atualizacao : atualizacoes) {
			for (Documento documento : documentos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == documento.getId()) {
						atualizar(documento, atualizacao);
					}
				}
			}
		}
	}
}
