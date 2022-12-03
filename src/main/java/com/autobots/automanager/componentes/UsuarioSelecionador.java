package com.autobots.automanager.componentes;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Usuario;

@Component

public class UsuarioSelecionador implements Selecionador<Usuario, Long> {
	  
		@Override
		public Usuario selecionar(List<Usuario> usuario, Long id) {
			Usuario selecionado = null;
			for(Usuario selecionador: usuario) {
				if(selecionador.getId().longValue() == id.longValue()) {
					selecionado = selecionador;
					break;
				}
			}
			return selecionado;
		}

}
