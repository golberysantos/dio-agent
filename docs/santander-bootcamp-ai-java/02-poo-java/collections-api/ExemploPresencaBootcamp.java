package collections_api;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ExemploPresencaBootcamp {
    public static void main(String[] args) {
        
        System.out.println("==================================================");
        System.out.println("REQUISITO A: Lista de Presença (Ordem e Sem Duplicados)");
        System.out.println("==================================================");
        
        // 1. Criamos um LinkedHashSet para garantir ordem de chegada e exclusividade
        Set<String> listaPresenca = new LinkedHashSet<>();
        
        // Alunos chegam na sequência
        listaPresenca.add("Golbery");
        listaPresenca.add("Ana");
        listaPresenca.add("Carlos");
        
        // Golbery tenta assinar a presença de novo por engano
        listaPresenca.add("Golbery"); 
        
        // Exibindo a lista de presença
        System.out.println("Alunos presentes na ordem de chegada:");
        for (String aluno : listaPresenca) {
            System.out.println("- " + aluno);
        }
        // Saída esperada: Golbery, Ana, Carlos (na ordem correta e sem repetir Golbery)


        System.out.println("\n==================================================");
        System.out.println("REQUISITO B: Matrículas Únicas (Sem Ordem e Sem Duplicados)");
        System.out.println("==================================================");
        
        // 2. Criamos um HashSet simples onde a ordem não importa, apenas a velocidade e unicidade
        Set<String> matriculasInscritas = new HashSet<>();
        
        matriculasInscritas.add("MAT-1020");
        matriculasInscritas.add("MAT-3040");
        matriculasInscritas.add("MAT-5060");
        
        // Tentativa de cadastrar uma matrícula repetida
        matriculasInscritas.add("MAT-1020"); 
        
        System.out.println("Matrículas registradas (a ordem pode variar):");
        for (String matricula : matriculasInscritas) {
            System.out.println("- " + matricula);
        }
        // Saída esperada: as 3 matrículas únicas, mas em uma ordem que o Java gerenciar internamente
    }
}
