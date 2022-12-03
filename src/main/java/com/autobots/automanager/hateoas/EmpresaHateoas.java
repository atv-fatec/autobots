package com.autobots.automanager.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entidades.Empresa;

@Component

public class EmpresaHateoas implements Hateoas <Empresa> {
	
	@Override
	public void AdicionarLink(List<Empresa> link) {
		for (Empresa empresa : link ) {
			long Id = empresa.getId();
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(Id)
		).withSelfRel();
			empresa.add(linkProprio);
		}	
	}

	@Override
	public void AdicionarLink(Empresa link) {
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas()).withRel("empresas");
		link.add(linkTodos);
		
	}

}
