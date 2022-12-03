package com.autobots.automanager.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.entitades.Veiculo;

@Component

public class VeiculoHateoas implements Hateoas<Veiculo> {

	@Override
	public void AdicionarLink(List<Veiculo> link) {
		for (Veiculo veiculo : link ) {
			long Id = veiculo.getId();
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(Id)
		).withSelfRel();
			veiculo.add(linkProprio);
		}	
	}

	@Override
	public void AdicionarLink(Veiculo link) {
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos");
		link.add(linkTodos);
		
	}

}
