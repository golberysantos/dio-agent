# ☕ Tratamento de Exceções em Java: Guia de Consulta Rápida

Este material serve como guia de consulta didático sobre o funcionamento, a hierarquia e as melhores práticas para o tratamento de erros e exceções no Java.

---

## 1. O que é uma Exceção?

Uma **Exceção** (`Exception`) no Java é um evento anômalo que ocorre durante a execução do programa (runtime) e interrompe o fluxo normal de instruções. Tratar exceções serve para capturar essas anomalias e permitir que a aplicação continue rodando de forma estável.

---

## 2. A Hierarquia da Classe `Throwable`

No Java, todos os tipos de erros e exceções estendem a classe raiz `java.lang.Throwable`. Ela se divide em dois ramos principais:

* **`Error`**: Falhas graves no nível da máquina virtual Java (JVM) das quais a aplicação não consegue se recuperar (ex: falta de memória `OutOfMemoryError` ou estouro de pilha `StackOverflowError`). **Não devem ser capturados**.
* **`Exception`**: Condições de erro previsíveis causadas por falha de lógica ou fatores externos. É dividida em **Checked** e **Unchecked**.

---

## 3. Checked vs Unchecked Exceptions

A divisão depende da herança direta da classe **`RuntimeException`**.

### A. Checked Exceptions (Exceções Checadas)
* **Definição:** São todas as classes que estendem `Exception`, mas que **não** estendem `RuntimeException` (ex: `IOException`, `SQLException`).
* **Regra do Compilador:** O compilador Java exige obrigatoriamente que o desenvolvedor trate o erro (com bloco `try-catch`) ou propague a exceção (com `throws` na assinatura do método). O código **não compila** sem isso.
* **Uso sugerido:** Erros esperados que dependem de fatores externos fora do controle do seu código (ex: abrir um arquivo que não existe ou falha de conexão de rede).

### B. Unchecked Exceptions (Exceções Não Checadas)
* **Definição:** São todas as classes que estendem **`RuntimeException`** (ex: `NullPointerException`, `ArithmeticException`).
* **Regra do Compilador:** O compilador não obriga o desenvolvedor a tratar ou declarar estas exceções. O programa compilará mesmo sem `try-catch`.
* **Uso sugerido:** Erros causados por falhas de programação ou lógica do código. A melhor prática é corrigir a lógica (ex: adicionar uma verificação de valor nulo) e não apenas usar `try-catch`.

---

## 4. Estruturas de Tratamento e Palavras-Chave

O controle de exceções utiliza cinco blocos e palavras-chave específicas:

* **`try`**: Delimita o bloco onde a execução do código de risco é monitorada.
* **`catch`**: Bloco que captura a exceção específica lançada pelo `try` e executa a lógica de contingência.
* **`finally`**: Bloco opcional que é executado **sempre** após a finalização do `try-catch`, independentemente do sucesso ou erro da operação. Ideal para fechar conexões ou liberar recursos.
* **`throw`**: Lança manualmente uma exceção no fluxo (ex: `throw new IllegalArgumentException();`).
* **`throws`**: Declara na assinatura de um método que ele pode propagar uma Checked Exception.

---

## 5. Gerenciamento Automático de Recursos (Try-with-Resources)

Introduzido no Java 7, evita a verbosidade do bloco `finally` ao fechar recursos automaticamente no final do bloco `try`, desde que o recurso implemente a interface `AutoCloseable`:

```java
// O BufferedReader será fechado automaticamente ao fim da execução
try (BufferedReader reader = new BufferedReader(new FileReader("dados.txt"))) {
    System.out.println(reader.readLine());
} catch (IOException e) {
    System.out.println("Erro na leitura do arquivo: " + e.getMessage());
}
```

---

## 6. Boas Práticas de Desenvolvimento

1. **Evite Capturas Genéricas:** Não use `catch (Exception e)` ou `catch (Throwable t)` se puder capturar exceções específicas (ex: `FileNotFoundException`). Capturar de forma genérica mascara erros de lógica de programação.
2. **Não Silencie Exceções:** Deixar o bloco `catch` vazio ou apenas imprimir uma mensagem genérica sem rastrear a raiz do problema dificulta a manutenção do software.
3. **Ordem dos Catch Blocks:** Sempre capture primeiro as exceções mais específicas (subclasses) e depois as mais genéricas (superclasses). Do contrário, o código não compilará devido à inacessibilidade.
