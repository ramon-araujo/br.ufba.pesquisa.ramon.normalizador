package br.ufba.pesquisa.ramon.normalizador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import br.ufba.pesquisa.ramon.normalizador.model.CodeSmellInstance;
import br.ufba.pesquisa.ramon.normalizador.model.ProjetoEnum;
import br.ufba.pesquisa.ramon.normalizador.model.Resultado;
import br.ufba.pesquisa.ramon.normalizador.model.TipoCodeSmellEnum;
import br.ufba.pesquisa.ramon.normalizador.model.TipoFerramentaEnum;


public class NormalizadorJDeodorant extends Normalizador {

	public NormalizadorJDeodorant(String diretorioLeitura, String diretorioEscrita) {
		super(diretorioLeitura, diretorioEscrita);
	}

	@Override
	protected List<Resultado> extrairResultados(File arquivo) {
		
		List<CodeSmellInstance> smells = new ArrayList<CodeSmellInstance>();
		
		try (FileReader reader = new FileReader(arquivo)) {
			BufferedReader br = new BufferedReader(reader);
			
			String line;
            while ((line = br.readLine()) != null) {
            	StringTokenizer st = new StringTokenizer(line, "\t");

            	if (st.hasMoreElements()) {
            		
            		TipoCodeSmellEnum tipoSmell = convertToType(st.nextToken());
            		
            		String item = st.nextToken();
            		int inicioNomeClasse = item.lastIndexOf(".") + 1;
            		String nomeClasse = item.substring(inicioNomeClasse);
            		
            		if (Objects.nonNull(tipoSmell)) {
            			smells.add(new CodeSmellInstance(tipoSmell, nomeClasse));
            		}
            	}
            }
            
            ProjetoEnum projeto = ProjetoEnum.criar(arquivo.getName().substring(0, arquivo.getName().indexOf("." + getTipoNormalizador().getExtensaoArquivos())));
            
            Resultado resultado = new Resultado();
            resultado.setNomeArquivo(arquivo.getName());
            resultado.setProjeto(projeto);
            resultado.setListaDeSmells(smells);
            
            return Arrays.asList(resultado);
            
		} catch (IOException e) {
			System.out.println("Erro ao ler aquivo do PMD.");
			e.printStackTrace();
		}
		
		return null;
	}

	
	
	private TipoCodeSmellEnum convertToType(String smell) {
		if (smell.equalsIgnoreCase("God Class")) {
			return TipoCodeSmellEnum.GOD_CLASS;
		}
		return null;
	}

	@Override
	public TipoFerramentaEnum getTipoNormalizador() {
		return TipoFerramentaEnum.JDEODORANT;
	}

}
