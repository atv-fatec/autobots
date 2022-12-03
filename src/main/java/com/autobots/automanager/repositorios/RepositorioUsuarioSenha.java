package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;

public interface RepositorioUsuarioSenha extends JpaRepository<CredencialUsuarioSenha, Long> {

}
