package br.ufba.pesquisa.ramon.normalizador.model;

public class CodeSmellInstance implements Cloneable {
	
	private TipoCodeSmellEnum tipo;
	private String item;
	
	public CodeSmellInstance(TipoCodeSmellEnum tipo, String item) {
		this.tipo = tipo;
		this.item = item;
	}
	
	public String getItem() {
		return item;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
	
	public TipoCodeSmellEnum getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoCodeSmellEnum tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return tipo.getNome() + "\t" + item;
	}

	@Override
	public CodeSmellInstance clone() throws CloneNotSupportedException {
		return new CodeSmellInstance(this.tipo, this.item);
	}
}
