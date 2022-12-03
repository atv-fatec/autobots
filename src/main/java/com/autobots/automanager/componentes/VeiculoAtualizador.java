package com.autobots.automanager.componentes;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Veiculo;

@Component

public class VeiculoAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getModelo())) {
				veiculo.setModelo(atualizacao.getModelo());
			}
			if (!verificador.verificar(atualizacao.getPlaca())) {
				veiculo.setPlaca(atualizacao.getPlaca());
			}
		}
	}

}
