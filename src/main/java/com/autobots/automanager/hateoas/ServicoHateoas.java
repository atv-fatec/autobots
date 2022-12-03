package com.autobots.automanager.hateoas;

import java.util.List;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.controles.ControleUsuario;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;

@Component

public class ServicoHateoas implements Hateoas <Servico> {
	
	@Override
	public void AdicionarLink(List<Servico> link) {
		for (Servico servico : link ) {
			long Id = servico.getId();
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(Id)
		).withSelfRel();
			servico.add(linkProprio);
		}	
	}

	@Override
	public void AdicionarLink(Servico link) {
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos()).withRel("usuarios");
		link.add(linkTodos);
		
	}

}
