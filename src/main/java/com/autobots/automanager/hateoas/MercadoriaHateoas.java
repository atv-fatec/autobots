package com.autobots.automanager.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.entitades.Mercadoria;

@Component

public class MercadoriaHateoas implements Hateoas <Mercadoria> {
	
	@Override
	public void AdicionarLink(List<Mercadoria> link) {
		for (Mercadoria mercadoria : link ) {
			long Id = mercadoria.getId();
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(Id)
		).withSelfRel();
			mercadoria.add(linkProprio);
		}	
	}

	@Override
	public void AdicionarLink(Mercadoria link) {
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias()).withRel("mercadorias");
		link.add(linkTodos);
		
	}

}
