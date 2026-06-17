package banco_dados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ExemploJDBC {
    public static void main(String[] args) {
        
        // URL de conexão para o banco de dados H2 rodando totalmente na memória RAM
        String urlConexao = "jdbc:h2:mem:bootcamp_db;DB_CLOSE_DELAY=-1";
        String usuario = "sa";
        String senha = "";

        System.out.println("==================================================");
        System.out.println("INICIANDO FLUXO JDBC COM BANCO EM MEMÓRIA (H2)");
        System.out.println("==================================================");

        // 1. Estabelecendo a conexão física com o banco de dados
        try (Connection conexao = DriverManager.getConnection(urlConexao, usuario, senha)) {
            System.out.println("Conexão estabelecida com sucesso!");

            // 2. Criando uma tabela de produtos
            try (Statement stmtTabela = conexao.createStatement()) {
                String sqlCriarTabela = "CREATE TABLE produtos (" +
                                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "nome VARCHAR(255) NOT NULL, " +
                                        "preco DOUBLE NOT NULL)";
                stmtTabela.execute(sqlCriarTabela);
                System.out.println("Tabela 'produtos' criada com sucesso.");
            }

            // 3. Inserindo dados usando PreparedStatement (Seguro contra SQL Injection)
            String sqlInserir = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
            try (PreparedStatement stmtInserir = conexao.prepareStatement(sqlInserir)) {
                
                // Inserindo o primeiro produto
                stmtInserir.setString(1, "Notebook Gamer");
                stmtInserir.setDouble(2, 4500.00);
                stmtInserir.executeUpdate(); // Executa o comando de atualização/inserção

                // Inserindo o segundo produto
                stmtInserir.setString(1, "Mouse Sem Fio");
                stmtInserir.setDouble(2, 150.00);
                stmtInserir.executeUpdate();

                System.out.println("Produtos cadastrados com sucesso via PreparedStatement.");
            }

            // 4. Consultando os dados usando SELECT e ResultSet
            String sqlConsultar = "SELECT id, nome, preco FROM produtos";
            try (PreparedStatement stmtConsultar = conexao.prepareStatement(sqlConsultar);
                 ResultSet rs = stmtConsultar.executeQuery()) { // executeQuery retorna o ResultSet
                
                System.out.println("\n--- Lista de Produtos Cadastrados ---");
                // Iterando pelas linhas de resultados retornadas do banco
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");
                    
                    System.out.println("ID: " + id + " | Nome: " + nome + " | Preço: R$ " + preco);
                }
            }

        } catch (SQLException e) {
            System.out.println("Ocorreu um erro no banco de dados: " + e.getMessage());
        }
    }
}
