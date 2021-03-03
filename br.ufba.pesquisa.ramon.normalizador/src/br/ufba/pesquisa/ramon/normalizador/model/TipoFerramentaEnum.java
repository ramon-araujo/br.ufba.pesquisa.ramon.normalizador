package br.ufba.pesquisa.ramon.normalizador.model;

public enum TipoFerramentaEnum {
	
	JSPIRIT("JSpirit", "jspirit"),
	SONAR("Sonar", "sonar"),
	PMD("PMD", "pmd"), 
	JDEODORANT("JDeodorant", "jdeodorant"), 
	CHECKSTYLE("Checkstyle", "checkstyle"),
	EMF_REFACTOR("EMF-Refactor", "modelos");
	
	private String nome;
	private String extensaoArquivos;
	
	private TipoFerramentaEnum(String nomeFerramenta, String extensaoArquivos) {
		this.nome = nomeFerramenta;
		this.extensaoArquivos = extensaoArquivos;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getExtensaoArquivos() {
		return extensaoArquivos;
	}
}
