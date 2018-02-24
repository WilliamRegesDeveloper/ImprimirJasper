package br.com.relatorio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

public class RelatorioFactory {

	public static String PATH_DIRETORIO = "path_diretorio";
	public static String PATH_ARQUIVO = "path_arquivo";
	public static String PATH_CACHE = "cache_relatorio";
	
	private static String relatorioPesquisa;

	private static ThreadLocal<HashMap<String, String>> inputRel = new ThreadLocal<HashMap<String, String>>() {

		@Override
		protected HashMap<String, String> initialValue() {

			HashMap<String,String> map = new HashMap<String, String>();

			try {
				FileInputStream fileInputStream;
				fileInputStream = new FileInputStream(new File("dados.properties"));
				Properties properties = new Properties();
				properties.load(fileInputStream);
				
				
				String endRelatorio = properties.getProperty("relatorio");
				
				Path path = Paths.get(endRelatorio);
				Path path2 = path.resolve(relatorioPesquisa);
				Path path3 = path.resolve("cache");
				
				if(Files.exists(path2, LinkOption.NOFOLLOW_LINKS)){
					
					map.put(PATH_DIRETORIO, path.toString());
					map.put(PATH_ARQUIVO, path2.toString());
					
					
					if(!Files.isDirectory(path3, LinkOption.NOFOLLOW_LINKS)){
						File file = new File(path3.toUri());
						file.mkdir();						
					}
					
					map.put(PATH_CACHE, path3.toString());
				}
				

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return map;

		};

	};

	public static HashMap<String,String> getNomeRelatorioReport(String nomeRelatorio) {
		relatorioPesquisa = nomeRelatorio;
		return inputRel.get();
	}
	

}
