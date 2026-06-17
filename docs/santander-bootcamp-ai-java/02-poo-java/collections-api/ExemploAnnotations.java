package collections_api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// =================================================================
// 1. CRIAÇÃO DE UMA ANOTAÇÃO CUSTOMIZADA (Metadados do Bootcamp)
// =================================================================
@Target(ElementType.TYPE) // ElementType.TYPE indica que a anotação é para Classes/Interfaces
@Retention(RetentionPolicy.RUNTIME) // Existirá em tempo de execução
@interface BootcampInfo {
    String nome();
    String trilha() default "Java Back-end";
}

// =================================================================
// 2. APLICAÇÃO DA ANOTAÇÃO CUSTOMIZADA
// =================================================================
@BootcampInfo(nome = "Santander Bootcamp 2026")
public class ExemploAnnotations {

    public static void main(String[] args) {
        
        System.out.println("==================================================");
        System.out.println("1. LENDO ANOTAÇÃO CUSTOMIZADA VIA REFLECTION");
        System.out.println("==================================================");

        // Usamos reflexão (Reflection) para ler os metadados da classe em tempo de execução
        Class<ExemploAnnotations> classe = ExemploAnnotations.class;
        
        if (classe.isAnnotationPresent(BootcampInfo.class)) {
            BootcampInfo info = classe.getAnnotation(BootcampInfo.class);
            System.out.println("Bootcamp: " + info.nome());
            System.out.println("Trilha: " + info.trilha());
        }

        System.out.println("\n==================================================");
        System.out.println("2. ANOTAÇÃO DE DEPRECATED E SUPPRESSWARNINGS");
        System.out.println("==================================================");

        // Chamada de método descontinuado (o editor riscará esta chamada se o plugin estiver ativo)
        executarMetodoAntigo();
        
        // Exemplo de uso de subclasse para testar o Override
        Filho filho = new Filho();
        filho.fazerBarulho();
    }

    // Marcando o método como obsoleto/descontinuado
    @Deprecated
    public static void executarMetodoAntigo() {
        System.out.println("Este método é antigo. Use métodos mais modernos.");
    }
}

// =================================================================
// 3. ANOTAÇÃO DE OVERRIDE (Sobrescrita de Classe Pai)
// =================================================================
class Pai {
    public void fazerBarulho() {
        System.out.println("Barulho do pai.");
    }
}

class Filho extends Pai {
    
    // O compilador valida se o nome e assinatura batem exatamente com o Pai.
    // Se você escrever "fazerBarulhoooo" por engano, o compilador dará erro.
    @Override
    public void fazerBarulho() {
        System.out.println("Barulho do filho.");
    }
}
