import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.ufba.pesquisa.ramon.normalizador.Normalizador;
import br.ufba.pesquisa.ramon.normalizador.NormalizadorCheckstyle;
import br.ufba.pesquisa.ramon.normalizador.NormalizadorJDeodorant;
import br.ufba.pesquisa.ramon.normalizador.NormalizadorJspirit;
import br.ufba.pesquisa.ramon.normalizador.NormalizadorModelos;
import br.ufba.pesquisa.ramon.normalizador.NormalizadorPMD;
import br.ufba.pesquisa.ramon.normalizador.NormalizadorSonar;

public class Main {
	
	public static void main(String[] args) {
		
		if (args.length < 2 || (Objects.isNull(args[0])) || Objects.isNull(args[1])) {
			throw new RuntimeException("Argumentos não passados corretamente.");
		}
		
		String diretorioLeitura = args[0];
		String diretorioEscrita = args[1];
		
		List<Normalizador> normalizadores = new ArrayList<>();
		
		normalizadores.add(new NormalizadorSonar(diretorioLeitura, diretorioEscrita));
		normalizadores.add(new NormalizadorPMD(diretorioLeitura, diretorioEscrita));
		normalizadores.add(new NormalizadorJDeodorant(diretorioLeitura, diretorioEscrita));
		normalizadores.add(new NormalizadorJspirit(diretorioLeitura, diretorioEscrita));
		normalizadores.add(new NormalizadorCheckstyle(diretorioLeitura, diretorioEscrita));
		normalizadores.add(new NormalizadorModelos(diretorioLeitura, diretorioEscrita));
		
		normalizadores.forEach(Normalizador::normalizar);
	}

}
