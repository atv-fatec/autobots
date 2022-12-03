package com.autobots.automanager.componentes;

import java.util.List;
import java.util.Set;

import com.autobots.automanager.entitades.Telefone;

public class TelefoneAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Telefone telefone, Telefone atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getDdd())) {
				telefone.setDdd(atualizacao.getDdd());
			}
			if (!verificador.verificar(atualizacao.getNumero())) {
				telefone.setNumero(atualizacao.getNumero());
			}
		}
	}

	public void atualizar(Set<Telefone> telefones, Set<Telefone> atualizacoes) {
		for (Telefone atualizacao : atualizacoes) {
			for (Telefone telefone : telefones) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == telefone.getId()) {
						atualizar(telefone, atualizacao);
					}
				}
			}
		}
	}
}
