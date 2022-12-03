package com.autobots.automanager.entidades;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Telefone {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String ddd;
	
	@Column
	private String numero;

	public List<Telefone> getTelefones() {
		// TODO Auto-generated method stub
		return null;
	}
}