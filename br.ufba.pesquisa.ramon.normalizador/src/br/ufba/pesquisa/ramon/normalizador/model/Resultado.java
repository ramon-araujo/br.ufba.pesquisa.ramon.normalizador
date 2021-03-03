package br.ufba.pesquisa.ramon.normalizador.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Resultado {

	private ProjetoEnum projeto;
	private String nomeArquivo;
	private List<CodeSmellInstance> listaDeSmells = new ArrayList<>();
	
	public Resultado() {
	}
	
	public Resultado(ProjetoEnum projeto) {
		this.projeto = projeto;
	}

	public ProjetoEnum getProjeto() {
		return projeto;
	}
	
	public void setProjeto(ProjetoEnum projeto) {
		this.projeto = projeto;
	}
	
	public List<CodeSmellInstance> getListaDeSmells() {
		return listaDeSmells;
	}
	
	public void setListaDeSmells(List<CodeSmellInstance> listaDeSmells) {
		this.listaDeSmells = listaDeSmells;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public void adicionarCodeSmellInstance(CodeSmellInstance instance) {
		if (Objects.isNull(listaDeSmells)) {
			this.listaDeSmells = new ArrayList<CodeSmellInstance>();
		}
		this.listaDeSmells.add(instance);
	}
	
}
