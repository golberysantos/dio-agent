# A hierarquia do Throwable: Error, Exception e RuntimeException — o mapa que você precisa ter na cabeça

Série: Tratamento de Exceções em Java — post 2 de 8

No post anterior vimos que o Java divide as exceções em Checked e Unchecked. Hoje vamos entender de onde essa divisão vem — a hierarquia completa do `Throwable`, que é a classe raiz de tudo que pode ser lançado em Java.

Roteiro da série:

1. Checked e Unchecked
2. A hierarquia do Throwable — você está aqui
3. try, catch, finally, throw e throws
4. Try-with-resources
5. Exceções customizadas
6. Multi-catch e encadeamento
7. A pegadinha do finally com return
8. @ControllerAdvice no Spring Boot

---

## Throwable — a raiz de tudo

No Java, qualquer coisa que pode ser lançada com `throw` ou capturada com `catch` estende `java.lang.Throwable`. Ela se divide em dois ramos principais: `Error` e `Exception`.

```
Throwable
├── Error
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── VirtualMachineError
│
└── Exception
    ├── IOException
    ├── SQLException
    ├── ClassNotFoundException
    │
    └── RuntimeException
        ├── NullPointerException
        ├── ArithmeticException
        ├── IllegalArgumentException
        ├── NumberFormatException
        └── ClassCastException
```

---

## Error — não capture, não trate

`Error` representa falhas graves no nível da JVM das quais a aplicação não consegue se recuperar. Quando um `OutOfMemoryError` acontece, a JVM está sem memória — não há o que fazer no código.

```java
// StackOverflowError — recursão infinita
public void executar() {
    executar(); // chama a si mesmo sem parar
}

// OutOfMemoryError — JVM sem memória disponível
// Java heap space
```

A regra é simples: nunca capture `Error`. Ele indica um problema de infraestrutura ou de lógica grave que precisa ser corrigido na raiz — não contornado com `try-catch`.

---

## Exception — o ramo que você trata

`Exception` representa condições de erro previsíveis. É aqui que vivem as Checked Exceptions — aquelas que o compilador obriga a tratar.

```java
// Checked — o compilador obriga tratamento
public void conectar() throws SQLException {
    Connection conn = DriverManager.getConnection(url, user, pass);
}
```

Tudo que estende `Exception` diretamente — `IOException`, `SQLException`, `ClassNotFoundException` — é Checked.

---

## RuntimeException — o sub-ramo Unchecked

`RuntimeException` é uma subclasse de `Exception`, mas com uma diferença fundamental: o compilador não obriga tratamento. É o ramo das Unchecked Exceptions — erros de lógica e programação.

```java
// Unchecked — compila sem try-catch, falha em runtime
String cpf = null;
cpf.length(); // NullPointerException

Integer.parseInt("abc"); // NumberFormatException
```

---

## A separação em uma frase

```
Throwable
├── Error                    → não capture
└── Exception
    ├── RuntimeException     → Unchecked — compilador não obriga
    └── demais subclasses    → Checked — compilador obriga
```

Se estende `RuntimeException` — Unchecked.
Se estende `Exception` diretamente — Checked.
Se estende `Error` — não trate.

---

## Para quem já tem uma bagagem maior em Java

Um detalhe que a hierarquia esconde: `Error` também é Unchecked — o compilador não obriga tratamento, assim como `RuntimeException`. A diferença não é técnica, é semântica: `RuntimeException` representa falhas de programação que podem ser corrigidas no código. `Error` representa falhas da JVM que estão fora do controle da aplicação. Por isso a regra de não capturar `Error` existe — não porque o compilador proíbe, mas porque não há o que fazer.

---

No próximo post vamos ver as cinco palavras-chave que controlam o fluxo de exceções no Java: `try`, `catch`, `finally`, `throw` e `throws` — o que cada uma faz de verdade e quando usar cada uma.

Alguma dúvida sobre a hierarquia? Bora trocar experiência aqui embaixo.
