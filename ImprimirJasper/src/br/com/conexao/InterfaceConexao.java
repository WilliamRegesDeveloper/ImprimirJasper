package br.com.conexao;

import java.sql.Connection;

/**
 * Interface de conex�o ao banco de dados
 * @author willi
 *
 */
public interface InterfaceConexao {
	
	public Connection getConnection();

}
