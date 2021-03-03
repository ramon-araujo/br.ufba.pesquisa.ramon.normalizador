package br.ufba.pesquisa.ramon.normalizador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import br.ufba.pesquisa.ramon.normalizador.model.CodeSmellInstance;
import br.ufba.pesquisa.ramon.normalizador.model.ProjetoEnum;
import br.ufba.pesquisa.ramon.normalizador.model.Resultado;
import br.ufba.pesquisa.ramon.normalizador.model.TipoCodeSmellEnum;
import br.ufba.pesquisa.ramon.normalizador.model.TipoFerramentaEnum;

/**
 * Instruções de execução
 * 
 * 		1. Rodar plugin do eclipse que extrai comandos do Checkstyle para todos os projetos abertos
 * 		2. Rodar comandos Checkstyle gerados
 * 		3. Rodar normalizador passando diretório onde os arquivos do passo 2 foram escritos e o diretório onde devem ser colocados os resultados normalizados
 * 
 * @author Ramon
 *
 */
public class NormalizadorCheckstyle extends Normalizador {

	public static final String TAG_SMELL = "[ERROR]";
	
	public NormalizadorCheckstyle(String diretorioLeitura, String diretorioEscrita) {
		super(diretorioLeitura, diretorioEscrita);
	}

	@Override
	protected List<Resultado> extrairResultados(File arquivo) {
		
		
		List<CodeSmellInstance> smells = new ArrayList<CodeSmellInstance>();

		try (FileReader reader = new FileReader(arquivo)) {
			BufferedReader br = new BufferedReader(reader);

			String line;
			while (Objects.nonNull(line = br.readLine())) {
				
				if (line.startsWith(TAG_SMELL)) {
					
					int inicioClasse = line.lastIndexOf("\\") + 1;
					int fimClasse = line.indexOf(".java");
					String nomeClasse = line.substring(inicioClasse, fimClasse);
					
					int inicioSmell = line.lastIndexOf("[");
					TipoCodeSmellEnum tipoSmell = convertToType(line.substring(inicioSmell));
					
					if (Objects.nonNull(tipoSmell)) {
						smells.add(new CodeSmellInstance(tipoSmell, nomeClasse));
					}
				}
			}

			ProjetoEnum projeto = ProjetoEnum.criar(arquivo.getName().substring(0,
					arquivo.getName().indexOf("." + getTipoNormalizador().getExtensaoArquivos())));

			Resultado resultado = new Resultado();
			resultado.setProjeto(projeto);
			resultado.setNomeArquivo(arquivo.getName());
			resultado.setListaDeSmells(smells);

			return Arrays.asList(resultado);
		} catch (IOException e) {
			System.out.println("Erro ao ler aquivo do PMD.");
			e.printStackTrace();
		}

		return null;
	}

	private TipoCodeSmellEnum convertToType(String smell) {
		
		if (smell.contains("FileLength")) {
			return TipoCodeSmellEnum.LARGE_CLASS;
		}
		
		if (smell.contains("ParameterNumber")) {
			return TipoCodeSmellEnum.LONG_PARAMETER_LIST;
		}
		return null;
	}

	@Override
	public TipoFerramentaEnum getTipoNormalizador() {
		return TipoFerramentaEnum.CHECKSTYLE;
	}

}
