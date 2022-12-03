package com.autobots.automanager.componentes;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Empresa;

@Component

public class EmpresaSelecionador implements Selecionador<Empresa, Long>  {
	@Override
	public Empresa selecionar(List<Empresa> empresa, Long id) {
		Empresa selecionado = null;
		for(Empresa selecionador: empresa) {
			if(selecionador.getId().longValue() == id.longValue()) {
				selecionado = selecionador;
				break;
			}
		}
		return selecionado;
	}

}
