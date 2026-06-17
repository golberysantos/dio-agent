package collections_api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContadorNomesWTI {
    public static void main(String[] args) {
        
        System.out.println("==================================================");
        System.out.println("SITUAÇÃO 3: Contando Frequência de Nomes (WTI)");
        System.out.println("==================================================");

        // 1. Criamos uma lista de funcionários que contém nomes repetidos
        List<String> funcionarios = new ArrayList<>();
        funcionarios.add("Ana");
        funcionarios.add("Carlos");
        funcionarios.add("Ana");
        funcionarios.add("João");
        funcionarios.add("Ana");
        funcionarios.add("Carlos");
        funcionarios.add("Mariana");

        System.out.println("Lista de funcionários cadastrados:");
        System.out.println(funcionarios);
        System.out.println("--------------------------------------------------");

        // 2. Criamos o Map para armazenar a contagem:
        // Chave (Key)   = Nome do Funcionário (String)
        // Valor (Value) = Quantidade de Ocorrências (Integer)
        Map<String, Integer> contadorNomes = new HashMap<>();

        // 3. Percorremos a lista e contamos as repetições
        for (String nome : funcionarios) {
            if (contadorNomes.containsKey(nome)) {
                // Se o nome já está no mapa, incrementamos o valor atual em +1
                contadorNomes.put(nome, contadorNomes.get(nome) + 1);
            } else {
                // Se é a primeira vez que vemos o nome, registramos com valor 1
                contadorNomes.put(nome, 1);
            }
        }

        // 4. Exibimos a contagem de popularidade dos nomes
        System.out.println("Popularidade dos nomes para fabricação de brinquedos:");
        
        // No Map, percorremos o EntrySet (conjunto de pares chave/valor)
        for (Map.Entry<String, Integer> entry : contadorNomes.entrySet()) {
            System.out.println("Nome: " + entry.getKey() + " | Quantidade: " + entry.getValue());
        }
    }
}
