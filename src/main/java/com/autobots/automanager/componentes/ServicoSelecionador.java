package com.autobots.automanager.componentes;

import java.util.List;

import org.springframework.stereotype.Component;



import com.autobots.automanager.entitades.Servico;

@Component

public class ServicoSelecionador implements Selecionador<Servico, Long>  {
	@Override
	public Servico selecionar(List<Servico> servico, Long id) {
		Servico selecionado = null;
		for(Servico selecionador: servico) {
			if(selecionador.getId().longValue() == id.longValue()) {
				selecionado = selecionador;
				break;
			}
		}
		return selecionado;
	}
}
