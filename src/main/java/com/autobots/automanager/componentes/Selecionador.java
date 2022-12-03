package com.autobots.automanager.componentes;

import java.util.List;

public interface Selecionador<Selecionador, ID> {
	public Selecionador selecionar(List<Selecionador> entidade, ID id);

}
