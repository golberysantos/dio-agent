package banco_dados;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

public class AppBanco {
    public static void main(String[] args) {
        
        // Inicializa a tabela no banco em memória antes de rodar as operações
        inicializarBancoDados();

        System.out.println("==================================================");
        System.out.println("SISTEMA BANCÁRIO SANTANDER - OPERAÇÕES JDBC");
        System.out.println("==================================================");

        // Princípio da Inversão de Dependências (DIP): Dependemos da interface
        ContaBancariaRepository repository = new ContaBancariaRepositoryJDBC();

        // 1. Criando e persistindo duas novas contas bancárias
        System.out.println("-> Criando contas de teste...");
        ContaBancaria contaGolbery = new ContaBancaria("12345-X", "Golbery Oliveira", 1000.00);
        ContaBancaria contaAna = new ContaBancaria("67890-Y", "Ana Beatriz", 500.00);
        
        repository.salvar(contaGolbery);
        repository.salvar(contaAna);
        System.out.println("Contas persistidas no banco de dados.");

        // 2. Realizando operações de negócio em memória e atualizando no banco de dados
        System.out.println("\n-> Executando depósito e saque...");
        
        // Buscando a conta do Golbery do banco de dados
        ContaBancaria contaDbGolbery = repository.buscarPorNumero("12345-X");
        if (contaDbGolbery != null) {
            contaDbGolbery.depositar(250.00); // Saldo vai para 1250.00
            repository.atualizar(contaDbGolbery); // Persiste a atualização
            System.out.println("Depósito de R$ 250,00 realizado na conta de Golbery.");
        }

        // Buscando a conta da Ana e efetuando saque
        ContaBancaria contaDbAna = repository.buscarPorNumero("67890-Y");
        if (contaDbAna != null) {
            contaDbAna.sacar(100.00); // Saldo vai para 400.00
            repository.atualizar(contaDbAna); // Persiste a atualização
            System.out.println("Saque de R$ 100,00 realizado na conta de Ana.");
        }

        // 3. Listando todas as contas para validar a persistência dos dados atualizados
        System.out.println("\n-> Listando contas salvas no banco de dados:");
        List<ContaBancaria> todasContas = repository.listarTodas();
        for (ContaBancaria conta : todasContas) {
            System.out.println("Conta: " + conta.getNumeroConta() + 
                               " | Titular: " + conta.getTitular() + 
                               " | Saldo Atual: R$ " + conta.getSaldo());
        }
        System.out.println("==================================================");
    }

    // Método auxiliar com responsabilidade exclusiva de inicializar a tabela no banco
    private static void inicializarBancoDados() {
        String sql = "CREATE TABLE IF NOT EXISTS contas_bancarias (" +
                     "numero_conta VARCHAR(20) PRIMARY KEY, " +
                     "titular VARCHAR(255) NOT NULL, " +
                     "saldo DOUBLE NOT NULL)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Banco de dados inicializado.");
        } catch (SQLException e) {
            throw new RuntimeException("Falha crítica ao inicializar a tabela de contas.", e);
        }
    }
}
