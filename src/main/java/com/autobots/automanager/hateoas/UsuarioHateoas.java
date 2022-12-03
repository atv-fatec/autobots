package com.autobots.automanager.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entitades.Usuario;

@Component
public class UsuarioHateoas implements Hateoas<Usuario> {

	@Override
	public void AdicionarLink(List<Usuario> link) {
		for (Usuario usuario : link ) {
			long Id = usuario.getId();
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(Id)
		).withSelfRel();
			usuario.add(linkProprio);
		}	
	}

	@Override
	public void AdicionarLink(Usuario link) {
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios()).withRel("usuarios");
		link.add(linkTodos);
		
	}
}
