package com.autobots.automanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.modelos.PerfilUsuario;
import com.autobots.automanager.modelos.TipoDocumento;
import com.autobots.automanager.modelos.TipoVeiculo;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

	@Autowired
	private RepositorioEmpresa repositorioEmpresa;

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		List<Perfil> perfilzinho = new ArrayList<>();

		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
		String senha = "123";
		
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Car service toyota ltda");
		empresa.setNomeFantasia("Car service manutenção veicular");
		empresa.setCadastro(new Date());

		Endereco enderecoEmpresa = new Endereco();
		enderecoEmpresa.setEstado("São Paulo");
		enderecoEmpresa.setCidade("São Paulo");
		enderecoEmpresa.setBairro("Centro");
		enderecoEmpresa.setRua("Av. São João");
		enderecoEmpresa.setNumero("00");
		enderecoEmpresa.setCodigoPostal("01035-000");

		empresa.setEndereco(enderecoEmpresa);

		Telefone telefoneEmpresa = new Telefone();
		telefoneEmpresa.setDdd("011");
		telefoneEmpresa.setNumero("986454527");

		empresa.getTelefones().add(telefoneEmpresa);

		Usuario funcionario = new Usuario();
		perfilzinho.add(Perfil.ROLE_ADMIN);
		funcionario.getNivelDeAcesso().addAll(perfilzinho);
		funcionario.setNome("Pedro Alcântara de Bragança e Bourbon");
		funcionario.setNomeSocial("Dom Pedro");
		funcionario.getPerfis().add(PerfilUsuario.FUNCIONARIO);

		Email emailFuncionario = new Email();
		emailFuncionario.setEndereco("a@a.com");

		funcionario.getEmails().add(emailFuncionario);

		Endereco enderecoFuncionario = new Endereco();
		enderecoFuncionario.setEstado("São Paulo");
		enderecoFuncionario.setCidade("São Paulo");
		enderecoFuncionario.setBairro("Jardins");
		enderecoFuncionario.setRua("Av. São Gabriel");
		enderecoFuncionario.setNumero("00");
		enderecoFuncionario.setCodigoPostal("01435-001");

		funcionario.setEndereco(enderecoFuncionario);

		empresa.getUsuarios().add(funcionario);

		Telefone telefoneFuncionario = new Telefone();
		telefoneFuncionario.setDdd("011");
		telefoneFuncionario.setNumero("9854633728");

		funcionario.getTelefones().add(telefoneFuncionario);

		Documento cpf = new Documento();
		cpf.setDataEmissao(new Date());
		cpf.setNumero("856473819229");
		cpf.setTipo(TipoDocumento.CPF);

		funcionario.getDocumentos().add(cpf);

		CredencialUsuarioSenha credencialFuncionario = new CredencialUsuarioSenha();
		credencialFuncionario.setInativo(false);
		credencialFuncionario.setNomeUsuario("dompedrofuncionario");
		credencialFuncionario.setSenha(codificador.encode(senha));
		credencialFuncionario.setCriacao(new Date());
		credencialFuncionario.setUltimoAcesso(new Date());

		funcionario.getCredenciais().add(credencialFuncionario);

		Usuario fornecedor = new Usuario();
		perfilzinho.clear();
		perfilzinho.add(Perfil.ROLE_VENDEDOR);
		fornecedor.getNivelDeAcesso().addAll(perfilzinho);
		fornecedor.setNome("Componentes varejo de partes automotivas ltda");
		fornecedor.setNomeSocial("Loja do carro, vendas de componentes automotivos");
		fornecedor.getPerfis().add(PerfilUsuario.FORNECEDOR);

		Email emailFornecedor = new Email();
		emailFornecedor.setEndereco("f@f.com");

		fornecedor.getEmails().add(emailFornecedor);

		CredencialUsuarioSenha credencialFornecedor = new CredencialUsuarioSenha();
		credencialFornecedor.setInativo(false);
		credencialFornecedor.setNomeUsuario("dompedrofornecedor");
		credencialFornecedor.setSenha(codificador.encode(senha));
		credencialFornecedor.setCriacao(new Date());
		credencialFornecedor.setUltimoAcesso(new Date());

		fornecedor.getCredenciais().add(credencialFornecedor);

		Documento cnpj = new Documento();
		cnpj.setDataEmissao(new Date());
		cnpj.setNumero("00014556000100");
		cnpj.setTipo(TipoDocumento.CNPJ);

		fornecedor.getDocumentos().add(cnpj);

		Endereco enderecoFornecedor = new Endereco();
		enderecoFornecedor.setEstado("Rio de Janeiro");
		enderecoFornecedor.setCidade("Rio de Janeiro");
		enderecoFornecedor.setBairro("Centro");
		enderecoFornecedor.setRua("Av. República do chile");
		enderecoFornecedor.setNumero("00");
		enderecoFornecedor.setCodigoPostal("20031-170");

		fornecedor.setEndereco(enderecoFornecedor);

		empresa.getUsuarios().add(fornecedor);
		
		Mercadoria rodaLigaLeve = new Mercadoria();
		rodaLigaLeve.setCadastro(new Date());
		rodaLigaLeve.setFabricao(new Date());
		rodaLigaLeve.setNome("Roda de liga leva modelo toyota etios");
		rodaLigaLeve.setValidade(new Date());
		rodaLigaLeve.setQuantidade(30);
		rodaLigaLeve.setValor(300.0);
		rodaLigaLeve.setDescricao("Roda de liga leve original de fábrica da toyota para modelos do tipo hatch");

		empresa.getMercadorias().add(rodaLigaLeve);

		fornecedor.getMercadorias().add(rodaLigaLeve);

		Usuario cliente = new Usuario();
		perfilzinho.clear();
		perfilzinho.add(Perfil.ROLE_CLIENTE);
		cliente.getNivelDeAcesso().addAll(perfilzinho);
		cliente.setNome("Pedro Alcântara de Bragança e Bourbon");
		cliente.setNomeSocial("Dom pedro cliente");
		cliente.getPerfis().add(PerfilUsuario.CLIENTE);

		Email emailCliente = new Email();
		emailCliente.setEndereco("c@c.com");

		cliente.getEmails().add(emailCliente);

		Documento cpfCliente = new Documento();
		cpfCliente.setDataEmissao(new Date());
		cpfCliente.setNumero("12584698533");
		cpfCliente.setTipo(TipoDocumento.CPF);

		cliente.getDocumentos().add(cpfCliente);

		CredencialUsuarioSenha credencialCliente = new CredencialUsuarioSenha();
		credencialCliente.setInativo(false);
		credencialCliente.setNomeUsuario("dompedrocliente");
		credencialCliente.setSenha(codificador.encode(senha));
		credencialCliente.setCriacao(new Date());
		credencialCliente.setUltimoAcesso(new Date());

		cliente.getCredenciais().add(credencialCliente);

		Endereco enderecoCliente = new Endereco();
		enderecoCliente.setEstado("São Paulo");
		enderecoCliente.setCidade("São José dos Campos");
		enderecoCliente.setBairro("Centro");
		enderecoCliente.setRua("Av. Dr. Nelson D'Ávila");
		enderecoCliente.setNumero("00");
		enderecoCliente.setCodigoPostal("12245-070");

		cliente.setEndereco(enderecoCliente);
		
		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca("ABC-0000");
		veiculo.setModelo("corolla-cross");
		veiculo.setTipo(TipoVeiculo.SUV);
		veiculo.setProprietario(cliente);
		
		cliente.getVeiculos().add(veiculo);
		
		empresa.getUsuarios().add(cliente);

		Servico trocaRodas = new Servico();
		trocaRodas.setDescricao("Troca das rodas do carro por novas");
		trocaRodas.setNome("Troca de rodas");
		trocaRodas.setValor(50);

		Servico alinhamento = new Servico();
		alinhamento.setDescricao("Alinhamento das rodas do carro");
		alinhamento.setNome("Alinhamento de rodas");
		alinhamento.setValor(50);

		empresa.getServicos().add(trocaRodas);
		empresa.getServicos().add(alinhamento);

		Venda venda = new Venda();
		venda.setCadastro(new Date());
		venda.setCliente(cliente);
		venda.getMercadorias().add(rodaLigaLeve);
		venda.setIdentificacao("1234698745");
		venda.setFuncionario(funcionario);
		venda.getServicos().add(trocaRodas);
		venda.getServicos().add(alinhamento);
		venda.setVeiculo(veiculo);
		veiculo.getVendas().add(venda);

		empresa.getVendas().add(venda);

		repositorioEmpresa.save(empresa);

	}
}