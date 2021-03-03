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
 * 		1. Executar o sonar em todos os projetos analisados
 * 		2. Configurar resultado para exibir Resource, Path e Rule key
 * 		3. Copiar resultado para um arquivo com extensão .sonar no diretório em que o normalizador realizará a leitura
 * 		4. Executar esta classe passando como parâmetros o diretório de leitura e o diretório de destino
 * 
 * @author Ramon
 *
 */
public class NormalizadorSonar extends Normalizador {

	public NormalizadorSonar(String diretorioLeitura, String diretorioEscrita) {
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
            	String item = extrairItem(st.nextToken());
            	st.nextToken();
            	TipoCodeSmellEnum tipoSmell = extrairSmell(st.nextToken());
            	
            	if (Objects.nonNull(tipoSmell)) {
            		smells.add(new CodeSmellInstance(tipoSmell, item));
            	}
            }
            
            ProjetoEnum projeto = ProjetoEnum.fromSubprojeto(arquivo.getName());
			
            Resultado resultado = new Resultado();
            resultado.setNomeArquivo(arquivo.getName());
    		resultado.setProjeto(projeto);
    		resultado.setListaDeSmells(smells);
    		
    		return Arrays.asList(resultado);
            
		} catch (IOException e) {
	            System.err.format("Erro ao ler arquivo do Sonar.", e);
	            e.printStackTrace();
        }
		
		return resultados;
	
	}

	protected List<Resultado> extrairResultadosANTIGO(File arquivo) {
		
		List<Resultado> resultados = new ArrayList<Resultado>();
		
		try (FileReader reader = new FileReader(arquivo)) {

			BufferedReader br = new BufferedReader(reader);
			
			String line;
            while ((line = br.readLine()) != null) {
            	StringTokenizer st = new StringTokenizer(line, "\t");
            	String item = extrairItem(st.nextToken());
            	ProjetoEnum projeto = ProjetoEnum.fromSubprojeto(st.nextToken());
            	TipoCodeSmellEnum tipoSmell = extrairSmell(st.nextToken());
            	
            	CodeSmellInstance codeSmell = new CodeSmellInstance(tipoSmell, item);
            	Resultado resultado = resultados
            		.stream()
            		.filter((resultadoElement) -> resultadoElement.getProjeto().equals(projeto))
            		.findFirst().orElse(null);
            	
            	if (Objects.isNull(resultado)) {
            		resultado = new Resultado();
            		resultado.setProjeto(projeto);
            		resultados.add(resultado);
            	}
            	
            	resultado.adicionarCodeSmellInstance(codeSmell);
            }
			
		} catch (IOException e) {
	            System.err.format("Erro ao ler arquivo do Sonar.", e);
	            e.printStackTrace();
        }
		
		return resultados;
	
	}
	
	@Override
	public TipoFerramentaEnum getTipoNormalizador() {
		return TipoFerramentaEnum.SONAR;
	}
	
	private String extrairItem(String item) {
		return item.substring(0, item.indexOf(".java"));
	}


	private TipoCodeSmellEnum extrairSmell(String rule) {
		if (rule.equalsIgnoreCase("squid:ClassCyclomaticComplexity")) {
			return TipoCodeSmellEnum.GOD_CLASS;
		}
		
		if (rule.equalsIgnoreCase("squid:S00107")) {
			return TipoCodeSmellEnum.LONG_PARAMETER_LIST;
		}
		
		if (rule.equalsIgnoreCase("squid:S1448") || rule.equalsIgnoreCase("squid:S1820")) {
			return TipoCodeSmellEnum.LARGE_CLASS;
		}
		
		return null;
	}

}
