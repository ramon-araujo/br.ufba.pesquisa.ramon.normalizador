package br.ufba.pesquisa.ramon.normalizador.model;

public enum TipoCodeSmellEnum {
	
	LARGE_CLASS("Large Class"),
	LONG_PARAMETER_LIST("Long Parameter List"),
	SHOTGUN_SURGERY("Shotgun Surgery"),
	DATA_CLASS("Data Class"),
	SPECULATIVE_GENERALITY("Speculative Generality"),
	GOD_CLASS("God Class"),
	TRADITION_BREAKER("Tradition Breaker");
	

	private String nome;
	
	private TipoCodeSmellEnum(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
}
