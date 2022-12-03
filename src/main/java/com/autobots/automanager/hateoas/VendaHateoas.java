package com.autobots.automanager.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.entidades.Venda;

@Component
public class VendaHateoas implements Hateoas<Venda> {

	@Override
	public void AdicionarLink(List<Venda> link) {
		for (Venda venda : link ) {
			long Id = venda.getId();
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(Id)
		).withSelfRel();
			venda.add(linkProprio);
		}	
	}

	@Override
	public void AdicionarLink(Venda link) {
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas()).withRel("vendas");
		link.add(linkTodos);
		
	}

}
