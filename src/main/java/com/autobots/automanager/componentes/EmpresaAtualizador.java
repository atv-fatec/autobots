package com.autobots.automanager.componentes;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Empresa;

@Component

public class EmpresaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Empresa empresa, Empresa atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getNomeFantasia())) {
				empresa.setNomeFantasia(atualizacao.getNomeFantasia());
			}
			if (!verificador.verificar(atualizacao.getRazaoSocial())) {
				empresa.setRazaoSocial(atualizacao.getRazaoSocial());
			}
		}
	}

	public void atualizar(Set<Empresa> empresas, Set<Empresa> atualizacoes) {
		for (Empresa atualizacao : atualizacoes) {
			for (Empresa empresa : empresas) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == empresa.getId()) {
						atualizar(empresa, atualizacao);
					}
				}
			}
		}
	}

}
