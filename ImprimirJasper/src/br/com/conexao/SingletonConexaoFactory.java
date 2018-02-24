package br.com.conexao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 * Singleton: Garante que a aplicação mantenha uma única conexão ao banco
 * @author willi
 *
 */
public abstract class SingletonConexaoFactory{
	
	private  static String url;
	private  static String user;
	private  static String password;
	
	private static final ThreadLocal<Connection> conection = new ThreadLocal<Connection>(){
		@Override
		protected Connection initialValue() {
			
			
			try {
				
				FileInputStream fileInputStream = new FileInputStream(new File("dados.properties"));
				Properties properties = new Properties();
				properties.load(fileInputStream);
				
				url = properties.getProperty("url");
				user = properties.getProperty("user");
				password = properties.getProperty("password");
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			InterfaceConexao implConexaoFirebird = new ImplConexaoFirebird(url, user, password);
			return implConexaoFirebird.getConnection();
			
		};
	};
	
	public static Connection get() {
		return conection.get();
	}

}
