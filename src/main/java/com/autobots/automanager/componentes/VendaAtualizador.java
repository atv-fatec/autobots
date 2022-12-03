package com.autobots.automanager.componentes;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Venda;

@Component

public class VendaAtualizador {
	
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Venda venda, Venda atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getIdentificacao())) {
				venda.setIdentificacao(atualizacao.getIdentificacao());
			}
		}
	}

	public void atualizar(Set<Venda> vendas, Set<Venda> atualizacoes) {
		for (Venda atualizacao : atualizacoes) {
			for (Venda venda : vendas) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == venda.getId()) {
						atualizar(venda, atualizacao);
					}
				}
			}
		}
	}
	
}
