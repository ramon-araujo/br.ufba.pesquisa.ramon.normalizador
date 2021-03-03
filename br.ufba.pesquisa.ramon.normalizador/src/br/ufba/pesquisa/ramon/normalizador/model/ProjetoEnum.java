package br.ufba.pesquisa.ramon.normalizador.model;

import java.util.Arrays;

public enum ProjetoEnum {
	
	ASPIREFID("Aspirefid"),
	OPENDDS("OpenDDS"),
	SOUNDGATES("Soundgates"),
	HYPERFLEX("Hyperflex"),
	IUMLB("iUML-b"),
	REUSEWARE("Reuseware"),
	HENSHIN("Henshin"),
	EMF_PROFILE("EMF-profile"),
	SERVICE("Service"),
	NAO_ENCONTRADO("INEXISTENTE");
	
	private String nome;
	
	private ProjetoEnum(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
	
	public static ProjetoEnum criar(String nomeProjeto) {
		return Arrays.asList(ProjetoEnum.values())
				.stream()
				.filter((projeto) -> projeto.getNome().equalsIgnoreCase(nomeProjeto))
				.findFirst()
				.orElse(NAO_ENCONTRADO);
	}
	
	public static ProjetoEnum fromSubprojeto(String nomeSubProjeto) {
		
		if (nomeSubProjeto.contains("aspire")) {
			return ProjetoEnum.ASPIREFID;
		}
		
		if (nomeSubProjeto.contains("opendds")) {
			return ProjetoEnum.OPENDDS;
		}
		
		if (nomeSubProjeto.contains("Soundgates")) {
			return ProjetoEnum.SOUNDGATES;
		}
		
		if (nomeSubProjeto.contains("hyperflex")) {
			return ProjetoEnum.HYPERFLEX;
		}
		
		if (nomeSubProjeto.contains("soton")) {
			return ProjetoEnum.IUMLB;
		}
		
		if (nomeSubProjeto.contains("reuseware")) {
			return ProjetoEnum.REUSEWARE;
		}
		
		if (nomeSubProjeto.contains("henshin")) {
			return ProjetoEnum.HENSHIN;
		}
		
		if (nomeSubProjeto.contains("emfprofile")) {
			return ProjetoEnum.EMF_PROFILE;
		}
		
		if (nomeSubProjeto.contains("hub.top")) {
			return ProjetoEnum.SERVICE;
		}
		
		System.out.println("PROJETO NÃO ENCONTRADO: " + nomeSubProjeto);
		return null;
	}
}
