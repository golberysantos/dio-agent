# O que é uma Exceção e por que o Java tem duas categorias — Checked e Unchecked

Série: Tratamento de Exceções em Java — post 1 de 8

Fala, pessoal! Vou iniciar uma série curta sobre tratamento de exceções em Java no contexto do bootcamp. Cada post cobre um conceito de forma direta, sem enrolar. O primeiro é o mais fundamental: o que é uma exceção e por que o Java divide em duas categorias.

Roteiro da série:

1. Checked e Unchecked — você está aqui
2. A hierarquia do Throwable
3. try, catch, finally, throw e throws
4. Try-with-resources
5. Exceções customizadas
6. Multi-catch e encadeamento
7. A pegadinha do finally com return
8. @ControllerAdvice no Spring Boot

---

## O que é uma exceção?

Uma exceção é um evento anômalo que ocorre durante a execução do programa e interrompe o fluxo normal de instruções. Ela não é um erro de compilação — o código compila normalmente, mas algo inesperado acontece em runtime.

O objetivo de tratar exceções é capturar essa anomalia e permitir que a aplicação continue rodando de forma estável — ou, no mínimo, falhe de forma controlada e com uma mensagem clara.

---

## Por que o Java tem duas categorias?

O Java divide as exceções em duas categorias com base em uma pergunta: o compilador consegue prever que esse erro pode acontecer?

Se sim — o compilador obriga você a tratar. São as **Checked Exceptions**.

Se não — o compilador não interfere. São as **Unchecked Exceptions**.

---

## Checked Exception — o compilador obriga

São erros previsíveis que dependem de fatores externos ao seu código — abrir um arquivo que pode não existir, conectar a um banco que pode estar fora do ar. O compilador sabe que isso pode falhar e exige que você trate antes de rodar.

```java
// O compilador não deixa compilar sem tratamento
public void lerArquivo() throws IOException {
    Files.readString(Path.of("dados.txt"));
}

// Ou com try-catch
try {
    Files.readString(Path.of("dados.txt"));
} catch (IOException e) {
    System.out.println("Arquivo não encontrado: " + e.getMessage());
}
```

---

## Unchecked Exception — o compilador não interfere

São erros causados por falhas de lógica ou programação — acessar um objeto nulo, dividir por zero, passar um argumento inválido. O compilador não consegue prever porque dependem do estado em runtime.

```java
// Compila normalmente — falha apenas em execução
String nome = null;
nome.length(); // NullPointerException

int resultado = 10 / 0; // ArithmeticException
```

> A melhor prática para Unchecked Exceptions não é usar try-catch — é corrigir a lógica. Se um objeto pode ser nulo, valide antes de usar. Se um argumento pode ser inválido, verifique antes de processar.

---

## A regra que define a categoria

A divisão depende de herança. Se a exceção estende `RuntimeException`, é Unchecked — o compilador não obriga tratamento. Se estende `Exception` diretamente, é Checked — o compilador obriga.

```java
// Checked — estende Exception diretamente
public class SaldoInsuficienteException extends Exception { }

// Unchecked — estende RuntimeException
public class ContaNaoEncontradaException extends RuntimeException { }
```

---

## Para quem já tem uma bagagem maior em Java

No Spring Boot, a convenção dominante é usar Unchecked Exceptions para exceções de domínio — como `ContaNaoEncontradaException`. O motivo é prático: o `@ControllerAdvice` centraliza o tratamento globalmente, tornando desnecessário forçar `try-catch` em cada Service. Checked Exceptions fazem mais sentido em operações de infraestrutura — leitura de arquivo, conexão JDBC — onde quem chama precisa decidir explicitamente como reagir à falha.

---

Você já teve dúvida sobre quando criar uma exceção Checked ou Unchecked no seu projeto? Qual foi o contexto? Bora trocar experiência aqui embaixo.
