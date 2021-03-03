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
 * 1. Rodar plugin que analisa todos os projetos abertos com o JSpirit
 * 
 * obs: não é necessário processamento na normalização os dados pois o plugin citado já gera os
 * dados normalizados. Sendo assim, este normalizador apenas extrairá os resultados para o modelo
 * estruturado e escreverá um arquivo igual ao de origem no diretório de destino.
 * 
 * @author Ramon
 *
 */
public class NormalizadorJspirit extends Normalizador {

	public NormalizadorJspirit(String diretorioLeitura, String diretorioEscrita) {
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
					String nomeClasse = st.nextToken();

					if (Objects.nonNull(tipoSmell)) {
						smells.add(new CodeSmellInstance(tipoSmell, nomeClasse));
					}
				}
			}

			ProjetoEnum projeto = ProjetoEnum.criar(arquivo.getName().substring(0,
					arquivo.getName().indexOf("." + getTipoNormalizador().getExtensaoArquivos())));

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

		switch (smell) {

		case "God Class":
			return TipoCodeSmellEnum.GOD_CLASS;

		case "Shotgun Surgery":
			return TipoCodeSmellEnum.SHOTGUN_SURGERY;

		case "Data Class":
			return TipoCodeSmellEnum.DATA_CLASS;

		case "Tradition Breaker":
			return TipoCodeSmellEnum.TRADITION_BREAKER;

		default:
			return null;
		}
	}

	@Override
	public TipoFerramentaEnum getTipoNormalizador() {
		return TipoFerramentaEnum.JSPIRIT;
	}

}
