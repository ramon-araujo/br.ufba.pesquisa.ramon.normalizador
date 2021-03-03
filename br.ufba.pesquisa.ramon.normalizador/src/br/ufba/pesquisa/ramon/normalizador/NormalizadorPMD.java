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

/**
 * Instruções de execução
 * 
 * 		1. Rodar plugin do eclipse que extrai comandos PMD para todos os projetos abertos
 * 		2. Rodar comandos PMD gerados
 * 		3. Rodar normalizador passando diretório onde os arquivos do passo 2 foram escritos e o diretório onde devem ser colocados os resultados normalizados
 * 
 * @author Ramon
 *
 */
public class NormalizadorPMD extends Normalizador {

	public NormalizadorPMD(String diretorioLeitura, String diretorioEscrita) {
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
            		
            		String item = st.nextToken();
            		int inicioNomeClasse = item.lastIndexOf("\\") + 1;
            		int fimNomeClasse = item.indexOf(".java");
            		String nomeClasse = item.substring(inicioNomeClasse, fimNomeClasse);
            		
            		TipoCodeSmellEnum tipoSmell = convertToType(st.nextToken());
            		if (Objects.nonNull(tipoSmell)) {
            			smells.add(new CodeSmellInstance(tipoSmell, nomeClasse));
            		}
            	}
            }
            
            ProjetoEnum projeto = ProjetoEnum.fromSubprojeto(arquivo.getName());
            
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

	@Override
	public TipoFerramentaEnum getTipoNormalizador() {
		return TipoFerramentaEnum.PMD;
	}

	private TipoCodeSmellEnum convertToType(String tipo) {
		
		if (tipo.contains("God Class")) {
			return TipoCodeSmellEnum.GOD_CLASS;
		}
		
		if (tipo.contains("Avoid really long classes")) {
			return TipoCodeSmellEnum.LARGE_CLASS;
		}
		
		if (tipo.contains("Data Class")) {
			return TipoCodeSmellEnum.DATA_CLASS;
		}
		
		if (tipo.contains("long parameter")) {
			return TipoCodeSmellEnum.LONG_PARAMETER_LIST;
		}
		
		return null;
	}
}
