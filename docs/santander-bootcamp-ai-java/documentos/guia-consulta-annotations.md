# 🏷️ Anotações em Java (Annotations): Guia de Consulta Rápida

Este material serve como guia de consulta didático sobre o funcionamento, as categorias de uso e a implementação de anotações personalizadas na linguagem Java.

---

## 1. O que são Annotations?

No Java, **Annotations** (Anotações) são metadados associados a classes, interfaces, métodos, atributos ou variáveis. Representadas pelo caractere **`@`**, elas não alteram de forma direta o fluxo ou a lógica de processamento do código compilado, mas fornecem informações adicionais que podem ser lidas por ferramentas externas, pelo compilador ou pela própria aplicação em tempo de execução.

---

## 2. As Três Finalidades Principais das Anotações

As anotações servem para instruir diferentes componentes do fluxo de desenvolvimento do software:

1. **Informação para o Compilador:** Permitem que o compilador realize validações estáticas e acuse erros ou ignore avisos (ex: verificar assinaturas de métodos com `@Override`).
2. **Processamento em Tempo de Compilação (Build-Time):** Ferramentas de build (como Maven/Gradle) ou bibliotecas (como o Lombok) utilizam processadores de anotações para gerar códigos repetitivos adicionais automaticamente (como getters, setters, ou arquivos XML).
3. **Processamento em Tempo de Execução (Runtime):** Frameworks (como o Spring Boot ou Hibernate) utilizam a reflexão (`Reflection`) do Java para ler as anotações e injetar comportamentos dinamicamente na aplicação em execução.

---

## 3. Anotações Embutidas (Nativas do Java JDK)

As anotações mais comuns utilizadas no desenvolvimento Java padrão para apoiar o compilador são:

* **`@Override`**: Informa ao compilador que o método marcado deve sobrescrever a assinatura de um método da classe pai ou interface. Evita erros de digitação de assinaturas.
* **`@Deprecated`**: Sinaliza que a classe, método ou atributo está descontinuado e não deve mais ser utilizado. O compilador emite um alerta de aviso sempre que a linha marcada for usada no projeto.
* **`@SuppressWarnings("alerta")`**: Instrui o compilador a silenciar avisos específicos de compilação naquele bloco (ex: `@SuppressWarnings("unchecked")`).

---

## 4. Anotações Personalizadas (Custom Annotations)

Para criar sua própria anotação, usa-se a palavra-chave **`@interface`**. Ela deve ser configurada por **Meta-anotações** (anotações que configuram o comportamento de outras anotações):

* **`@Target`**: Define onde a anotação pode ser aplicada (ex: em classes, métodos, parâmetros, variáveis).
* **`@Retention`**: Define até qual estágio do ciclo de vida a anotação existirá. Os níveis de retenção são:
  * `RetentionPolicy.SOURCE`: Descartada pelo compilador (não vai para o arquivo `.class`).
  * `RetentionPolicy.CLASS`: Gravada no arquivo `.class`, mas descartada pela JVM em execução.
  * `RetentionPolicy.RUNTIME`: Preservada na execução pela JVM (pode ser lida via Reflexão).

---

## 5. Exemplo Prático Completo

O código abaixo exemplifica a criação de uma anotação customizada e a sua leitura dinâmica via Reflexão (`Reflection`):

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// =================================================================
// Declaração da Anotação Customizada
// =================================================================
@Target(ElementType.TYPE) // Aplicável apenas em classes/interfaces
@Retention(RetentionPolicy.RUNTIME) // Mantida em tempo de execução
@interface InformacaoProjeto {
    String autor();
    String versao() default "1.0.0";
}

// =================================================================
// Uso da Anotação e Leitura via Reflexão
// =================================================================
@InformacaoProjeto(autor = "Golbery Oliveira")
public class App {
    public static void main(String[] args) {
        
        // Acessando os metadados da classe em tempo de execução
        Class<App> objetoClasse = App.class;
        
        if (objetoClasse.isAnnotationPresent(InformacaoProjeto.class)) {
            InformacaoProjeto anotacao = objetoClasse.getAnnotation(InformacaoProjeto.class);
            System.out.println("Autor do Projeto: " + anotacao.autor());
            System.out.println("Versão: " + anotacao.versao());
        }
    }
}
```
