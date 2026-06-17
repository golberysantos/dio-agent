package banco_dados;

import java.util.List;

public interface ContaBancariaRepository {
    void salvar(ContaBancaria conta);
    ContaBancaria buscarPorNumero(String numeroConta);
    void atualizar(ContaBancaria conta);
    List<ContaBancaria> listarTodas();
}
