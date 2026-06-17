package banco_dados;

public class ContaBancaria {
    private String numeroConta;
    private String titular;
    private double saldo;

    // Construtor para novas contas (saldo inicial)
    public ContaBancaria(String numeroConta, String titular, double saldoInicial) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    // Métodos de Regras de Negócio (Clean Code: métodos pequenos e focados)
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor de depósito deve ser maior que zero.");
        }
        this.saldo += valor;
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor de saque deve ser maior que zero.");
        }
        if (valor > this.saldo) {
            throw new IllegalStateException("Saldo insuficiente para realizar o saque.");
        }
        this.saldo -= valor;
    }

    // Getters
    public String getNumeroConta() {
        return numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }
}
