package br.com.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ImplConexaoFirebird implements InterfaceConexao {

	private String url;
	private String user;
	private String password;

	public ImplConexaoFirebird(String url, String user, String password) {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
	}

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		Connection connection = null;

		try {
			Class.forName("org.firebirdsql.jdbc.FBDriver");

			connection = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

}
