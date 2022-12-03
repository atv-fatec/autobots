package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Servico;

public interface RepositorioServico extends JpaRepository<Servico, Long> {

}
