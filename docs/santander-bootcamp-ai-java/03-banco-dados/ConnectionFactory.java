package banco_dados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    
    // Configurações do Banco H2 em Memória
    private static final String URL_CONEXAO = "jdbc:h2:mem:santander_bank_db;DB_CLOSE_DELAY=-1";
    private static final String USUARIO = "sa";
    private static final String SENHA = "";

    // Responsabilidade Única: Estabelecer e devolver a conexão
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_CONEXAO, USUARIO, SENHA);
    }
}
