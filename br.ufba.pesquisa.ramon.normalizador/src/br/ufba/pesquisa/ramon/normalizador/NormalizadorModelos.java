package br.ufba.pesquisa.ramon.normalizador;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufba.pesquisa.ramon.normalizador.model.CodeSmellInstance;
import br.ufba.pesquisa.ramon.normalizador.model.ProjetoEnum;
import br.ufba.pesquisa.ramon.normalizador.model.Resultado;
import br.ufba.pesquisa.ramon.normalizador.model.TipoCodeSmellEnum;
import br.ufba.pesquisa.ramon.normalizador.model.TipoFerramentaEnum;

public class NormalizadorModelos extends Normalizador {

	public NormalizadorModelos(String diretorioLeitura, String diretorioEscrita) {
		super(diretorioLeitura, diretorioEscrita);
	}
	
	@Override
	protected List<Resultado> extrairResultados(File arquivo) {
		
		try {			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(arquivo);
			
			doc.getDocumentElement().normalize();
			NodeList smellList = doc.getElementsByTagName("smell");
			
			String nomeArquivo = arquivo.getName();
			
			Resultado resultado = new Resultado(extrairProjeto(nomeArquivo));
			resultado.setNomeArquivo(nomeArquivo);
			
			for (int i=0; i<smellList.getLength(); i++) {
				Node smellItem = smellList.item(i);
				if (smellItem.getNodeType() == Node.ELEMENT_NODE) { 
					Element smellElement = (Element) smellItem;
					TipoCodeSmellEnum tipoSmell = extrairSmell(smellElement.getAttribute("name"));
					
					NodeList smellInstanceList = smellElement.getElementsByTagName("element");
					for (int j = 0; j < smellInstanceList.getLength(); j++) {
						String item = smellInstanceList.item(j).getTextContent();
						resultado.adicionarCodeSmellInstance(new CodeSmellInstance(tipoSmell, item));
					}
				}
			}
			
			return Arrays.asList(resultado);
			
		} catch (Exception e) {
			System.out.println("Não foi possível extrair resultados de modelos.");
			e.printStackTrace();
		}
		
		return null;
	}

	private ProjetoEnum extrairProjeto(String nomeArquivo) {
		nomeArquivo = nomeArquivo.toLowerCase();
		if (nomeArquivo.contains("aspirerfid")) {
			return ProjetoEnum.ASPIREFID;
		}
		
		return null;
	}

	@Override
	public TipoFerramentaEnum getTipoNormalizador() {
		return TipoFerramentaEnum.EMF_REFACTOR;
	}

	private TipoCodeSmellEnum extrairSmell(String name) {
		if (name.equalsIgnoreCase("Long Parameters List")) {
			return TipoCodeSmellEnum.LONG_PARAMETER_LIST;
		}
		
		if (name.equalsIgnoreCase("Data Class")) {
			return TipoCodeSmellEnum.DATA_CLASS;
		}
		
		if (name.equalsIgnoreCase("God Class")) {
			return TipoCodeSmellEnum.GOD_CLASS;
		}
		
		if (name.equalsIgnoreCase("Shotgun Surgery")) {
			return TipoCodeSmellEnum.SHOTGUN_SURGERY;
		}
		
		if (name.equalsIgnoreCase("Large Class")) {
			return TipoCodeSmellEnum.LARGE_CLASS;
		}
		
		return null;
	}
}
