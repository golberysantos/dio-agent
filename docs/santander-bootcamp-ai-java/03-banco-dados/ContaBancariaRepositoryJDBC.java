package banco_dados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContaBancariaRepositoryJDBC implements ContaBancariaRepository {

    @Override
    public void salvar(ContaBancaria conta) {
        String sql = "INSERT INTO contas_bancarias (numero_conta, titular, saldo) VALUES (?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, conta.getNumeroConta());
            stmt.setString(2, conta.getTitular());
            stmt.setDouble(3, conta.getSaldo());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            // Clean Code: Lançamos uma RuntimeException para não sujar a assinatura da Interface
            throw new RuntimeException("Erro ao salvar a conta bancária no banco de dados.", e);
        }
    }

    @Override
    public ContaBancaria buscarPorNumero(String numeroConta) {
        String sql = "SELECT numero_conta, titular, saldo FROM contas_bancarias WHERE numero_conta = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroConta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ContaBancaria(
                        rs.getString("numero_conta"),
                        rs.getString("titular"),
                        rs.getDouble("saldo")
                    );
                }
            }
            return null; // Retorna null caso a conta não exista
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar a conta bancária por número.", e);
        }
    }

    @Override
    public void atualizar(ContaBancaria conta) {
        String sql = "UPDATE contas_bancarias SET saldo = ? WHERE numero_conta = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, conta.getSaldo());
            stmt.setString(2, conta.getNumeroConta());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar os dados da conta bancária.", e);
        }
    }

    @Override
    public List<ContaBancaria> listarTodas() {
        String sql = "SELECT numero_conta, titular, saldo FROM contas_bancarias";
        List<ContaBancaria> contas = new ArrayList<>();
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                contas.add(new ContaBancaria(
                    rs.getString("numero_conta"),
                    rs.getString("titular"),
                    rs.getDouble("saldo")
                ));
            }
            return contas;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todas as contas bancárias.", e);
        }
    }
}
