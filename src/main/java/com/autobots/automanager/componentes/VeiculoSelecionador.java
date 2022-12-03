package com.autobots.automanager.componentes;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Veiculo;

@Component

public class VeiculoSelecionador implements Selecionador<Veiculo, Long> {
	  
		@Override
		public Veiculo selecionar(List<Veiculo> veiculo, Long id) {
			Veiculo selecionado = null;
			for(Veiculo selecionador: veiculo) {
				if(selecionador.getId().longValue() == id.longValue()) {
					selecionado = selecionador;
					break;
				}
			}
			return selecionado;
		}

}
