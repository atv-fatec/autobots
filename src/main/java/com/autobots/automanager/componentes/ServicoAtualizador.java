package com.autobots.automanager.componentes;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Servico;

@Component

public class ServicoAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Servico servico, Servico atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getDescricao())) {
				servico.setDescricao(atualizacao.getDescricao());
			}
			if (!verificador.verificar(atualizacao.getNome())) {
				servico.setNome(atualizacao.getNome());
			}
		}
	}

	public void atualizar(Set<Servico> servicos, Set<Servico> atualizacoes) {
		for (Servico atualizacao : atualizacoes) {
			for (Servico servico : servicos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == servico.getId()) {
						atualizar(servico, atualizacao);
					}
				}
			}
		}
	}

}
