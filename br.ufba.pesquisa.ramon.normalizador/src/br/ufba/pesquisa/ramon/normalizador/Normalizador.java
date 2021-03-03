package br.ufba.pesquisa.ramon.normalizador;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import br.ufba.pesquisa.ramon.normalizador.model.CodeSmellInstance;
import br.ufba.pesquisa.ramon.normalizador.model.ProjetoEnum;
import br.ufba.pesquisa.ramon.normalizador.model.Resultado;
import br.ufba.pesquisa.ramon.normalizador.model.TipoFerramentaEnum;

public abstract class Normalizador {
	
	protected String diretorioLeitura;
	protected String diretorioEscrita;
	protected List<Resultado> resultados = new ArrayList<Resultado>();
	protected List<Resultado> resultadosAgrupados = new ArrayList<Resultado>();
	
	protected Normalizador(String diretorioLeitura, String diretorioEscrita) {
		this.diretorioLeitura = diretorioLeitura;
		this.diretorioEscrita = diretorioEscrita;
	}
	
	
	public void normalizar() {
		System.out.println();
		System.out.println("Normalizando resultados do " + getTipoNormalizador().getNome());
		System.out.println();
		
		List<File> arquivos = recuperarArquivos();
		
		resultados = new ArrayList<Resultado>();
		arquivos.forEach((arquivo) -> {
			System.out.println("	Extraindo resultados do arquivo " + arquivo.getName());
			resultados.addAll(extrairResultados(arquivo));
			System.out.println("	Extração finalizada com sucesso.");
		});
		
		agruparResultadosPorProjeto();
		incluirResultadosVazios();
		escreverResultado();
	}
	
	private void incluirResultadosVazios() {
		for (ProjetoEnum projeto : ProjetoEnum.values()) {
			Optional<Resultado> resultadoEncontrado = this.resultados.stream().filter((resultado) -> resultado.getProjeto().equals(projeto)).findAny();
			if (!resultadoEncontrado.isPresent()) {
				this.resultadosAgrupados.add(new Resultado(projeto));
			}
		}
	}

	protected List<File> recuperarArquivos() {
		File folder = new File(diretorioLeitura);
		
		File[] listaDeArquivos = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				return fileName.endsWith(getTipoNormalizador().getExtensaoArquivos());
			}
		});
		
		return Arrays.asList(listaDeArquivos);
	}
	
	protected void escreverResultado() {
		
		System.out.println("");
		
		resultados.forEach(resultado -> {
        	try {
        		
        		String nomeArquivo = resultado.getNomeArquivo();
        		System.out.println("	Escrevendo resultado do projeto " + nomeArquivo + "(" + resultado.getProjeto().getNome() + ")");
        		List<String> smells = resultado.getListaDeSmells().stream().map((smell) -> smell.toString()).collect(Collectors.toList());
				Files.write(Paths.get(diretorioEscrita + "/nao-agrupados/" + nomeArquivo), smells);
				System.out.println("	Escrita de arquivo " + nomeArquivo + " realizada com sucesso");
			} catch (IOException e) {
				System.err.format("Erro ao escrever arquivo do " + getTipoNormalizador().getNome() + ".", e);
				e.printStackTrace();
			}
        });

		System.out.println("");
		
		resultadosAgrupados.forEach(resultado -> {
        	try {
        		
        		System.out.println("	Escrevendo resultado de projetos agrupados " + resultado.getProjeto().getNome());
        		List<String> smells = resultado.getListaDeSmells().stream().map((smell) -> smell.toString()).collect(Collectors.toList());
        		String nomeArquivoAgrupados = resultado.getProjeto().getNome() + "." + getTipoNormalizador().getExtensaoArquivos();
				Files.write(Paths.get(diretorioEscrita + "/agrupados/" + nomeArquivoAgrupados), smells);
				System.out.println("	Escrita de arquivo " + nomeArquivoAgrupados + " realizada com sucesso");
			} catch (IOException e) {
				System.err.format("Erro ao escrever arquivo do " + getTipoNormalizador().getNome() + ".", e);
				e.printStackTrace();
			}
        });
	}
	
	private void agruparResultadosPorProjeto() {
		
		resultadosAgrupados = new ArrayList<Resultado>();
		
		for (Resultado resultado : resultados) {
			Resultado resultadoEncontrado = resultadosAgrupados
					.stream()
					.filter((result) -> result.getProjeto().equals(resultado.getProjeto()))
					.findFirst()
					.orElse(null);
			try {
			
				if (Objects.nonNull(resultadoEncontrado)) {
					List<CodeSmellInstance> smellsClonados = new ArrayList<>();
					for (CodeSmellInstance codeSmellInstance : resultadoEncontrado.getListaDeSmells()) {
						smellsClonados.add(codeSmellInstance.clone());
					}
					resultadoEncontrado.getListaDeSmells().addAll(smellsClonados);
				} else {
					Resultado resultadoAgrupado = new Resultado();
					resultado.setProjeto(resultado.getProjeto());
					resultado.setNomeArquivo(resultado.getProjeto().getNome());
					List<CodeSmellInstance> smellsClonados = new ArrayList<>();
					for (CodeSmellInstance codeSmellInstance : resultado.getListaDeSmells()) {
						smellsClonados.add(codeSmellInstance.clone());
					}
					resultado.setListaDeSmells(smellsClonados);
					resultadosAgrupados.add(resultadoAgrupado);
				}
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			
		}
		
		this.resultados.forEach((resultado) -> {
			Resultado resultadoEncontrado = resultadosAgrupados
				.stream()
				.filter((result) -> result.getProjeto().equals(resultado.getProjeto()))
				.findFirst()
				.orElse(null);
			
			if (Objects.nonNull(resultadoEncontrado)) {
				resultadoEncontrado.getListaDeSmells().addAll(resultado.getListaDeSmells());
			} else {
				resultadosAgrupados.add(resultado);
			}
		});
	}
	
	protected abstract List<Resultado> extrairResultados(File arquivo);
	public abstract TipoFerramentaEnum getTipoNormalizador();
}
