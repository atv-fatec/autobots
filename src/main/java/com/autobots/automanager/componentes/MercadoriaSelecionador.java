package com.autobots.automanager.componentes;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Mercadoria;

@Component

public class MercadoriaSelecionador implements Selecionador<Mercadoria, Long>  {
	@Override
	public Mercadoria selecionar(List<Mercadoria> mercadoria, Long id) {
		Mercadoria selecionado = null;
		for(Mercadoria selecionador: mercadoria) {
			if(selecionador.getId().longValue() == id.longValue()) {
				selecionado = selecionador;
				break;
			}
		}
		return selecionado;
	}

}
