# Multi-catch e encadeamento de exceções — dois recursos que o OCP adora cobrar

Série: Tratamento de Exceções em Java — post 6 de 8

Nos posts anteriores vimos as bases do tratamento de exceções e como criar exceções customizadas. Hoje vamos ver dois recursos que andam juntos na prática e que muita gente não conhece bem: o multi-catch, que elimina repetição de blocos `catch`, e o encadeamento de exceções, que garante que a causa raiz nunca se perca.

---

Roteiro da série:

1. Checked e Unchecked
2. A hierarquia do Throwable
3. try, catch, finally, throw e throws
4. Try-with-resources
5. Exceções customizadas
6. Multi-catch e encadeamento <----- aqui estamos
7. A pegadinha do finally com return
8. @ControllerAdvice no Spring Boot

---

## Multi-catch

Introduzido no Java 7, o multi-catch resolve um problema muito comum: quando um bloco `try` pode lançar dois ou mais tipos diferentes de exceção que você quer tratar da mesma forma. Antes, você era obrigado a repetir o bloco `catch` para cada tipo:

```java
// Antes do Java 7 — repetição desnecessária
try {
    // operação de risco
} catch (IOException e) {
    log.error("Erro: " + e.getMessage());
} catch (SQLException e) {
    log.error("Erro: " + e.getMessage());
}
```

Com multi-catch, os dois blocos viram um só usando o operador `|`:

```java
// Java 7+ — limpo e sem repetição
try {
    // operação de risco
} catch (IOException | SQLException e) {
    log.error("Erro: " + e.getMessage());
}
```

---

## A regra que o OCP cobra — herança proibida no multi-catch

Você não pode combinar exceções que têm relação de herança entre si. `FileNotFoundException` é subclasse de `IOException` — isso não compila:

```java
// Erro de compilação — FileNotFoundException já é uma IOException
} catch (IOException | FileNotFoundException e) { }
```

O compilador acusa redundância — capturar `IOException` já captura `FileNotFoundException` automaticamente. Nesse caso, capture apenas a mais genérica:

```java
// Correto
} catch (IOException e) { }
```

---

## Encadeamento de exceções

O encadeamento resolve outro problema crítico: não perder a causa raiz do erro quando você precisa relançar uma exceção.

Sem encadeamento, a causa original some do stack trace:

```java
// Errado — a causa raiz é perdida
try {
    Files.readString(Path.of("config.txt"));
} catch (IOException e) {
    throw new RuntimeException("Falha ao carregar configuração");
    // quem receber esse erro não saberá o que causou
}
```

Com encadeamento, a causa original é preservada no segundo argumento:

```java
// Correto — a IOException fica encadeada como causa raiz
try {
    Files.readString(Path.of("config.txt"));
} catch (IOException e) {
    throw new RuntimeException("Falha ao carregar configuração", e);
}
```

O log exibe o stack trace completo:

```
RuntimeException: Falha ao carregar configuração
    at ServicoConfig.carregar(ServicoConfig.java:22)
Caused by: IOException: config.txt (No such file or directory)
    at Files.readString(Files.java:...)
```

Sem encadeamento, o `Caused by` desaparece — e debugar em produção vira um pesadelo.

---

## Os dois juntos no contexto do bootcamp

```java
try {
    transacaoRepository.save(transacao);
    notificacaoService.enviar(transacao);

} catch (DataAccessException | NotificacaoException e) {
    // multi-catch — trata os dois da mesma forma
    throw new FalhaDeTransferenciaException(
        "Erro ao processar transferência Pix", e); // encadeamento — causa preservada
}
```

---

## Para quem já tem uma bagagem maior em Java

Uma variação do encadeamento que aparece no OCP: o método `initCause()`. Antes do Java 1.4, as exceções não tinham o segundo argumento no construtor — o encadeamento era feito assim:

```java
RuntimeException ex = new RuntimeException("Falha");
ex.initCause(e); // encadeia a causa depois da criação
throw ex;
```

Hoje o construtor com `Throwable` é o caminho padrão e mais limpo. O `initCause()` ainda existe mas raramente aparece em código moderno — vale conhecer para não estranhar quando encontrar em bases de código legado.

---

Entendeu por que o encadeamento importa?

Sem ele, você troca uma exceção clara pela outra e perde a raiz do problema. Com ele, o log mostra toda a cadeia de causas — do erro de negócio até o problema de infraestrutura que o originou. Em produção, essa informação vale ouro.

---

No próximo post vamos ver uma das pegadinhas favoritas do OCP: o que acontece quando o `finally` tem um `return` — e por que o resultado surpreende até desenvolvedores experientes.

Você já perdeu a causa raiz de um erro por esquecer o encadeamento? Como descobriu o problema? Bora trocar experiência aqui embaixo.
