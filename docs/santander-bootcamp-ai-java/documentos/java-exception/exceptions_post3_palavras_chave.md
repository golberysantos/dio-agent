# try, catch, finally, throw e throws — o que cada palavra-chave faz de verdade

Série: Tratamento de Exceções em Java — post 3 de 8

Nos posts anteriores vimos o que é uma exceção e como funciona a hierarquia do `Throwable`. Hoje vamos às cinco palavras-chave que controlam o fluxo de exceções no Java — e o que cada uma faz de verdade.

Roteiro da série:

1. Checked e Unchecked
2. A hierarquia do Throwable
3. try, catch, finally, throw e throws — você está aqui
4. Try-with-resources
5. Exceções customizadas
6. Multi-catch e encadeamento
7. A pegadinha do finally com return
8. @ControllerAdvice no Spring Boot

---

## As cinco palavras-chave

**try** — delimita o bloco onde o código de risco é monitorado. Se uma exceção ocorrer dentro dele, o fluxo é interrompido imediatamente e o controle passa para o `catch`.

**catch** — captura a exceção lançada pelo `try` e executa a lógica de contingência. Você pode ter múltiplos blocos `catch` para tipos diferentes de exceção.

**finally** — executado sempre após o `try-catch`, independente de sucesso ou erro. Ideal para liberar recursos como conexões e arquivos.

**throw** — lança manualmente uma exceção no fluxo do código.

**throws** — declara na assinatura de um método que ele pode propagar uma Checked Exception para quem o chamou.

---

## Estrutura completa

```java
public void processarPix(String cpf) throws ContaNaoEncontradaException {

    try {
        // código de risco monitorado aqui
        Conta conta = buscarConta(cpf);
        conta.debitar(500.0);

    } catch (NullPointerException e) {
        // mais específico — captura primeiro
        throw new ContaNaoEncontradaException("Conta nula para: " + cpf);

    } catch (IllegalArgumentException e) {
        // mais genérico — captura depois
        log.error("Argumento inválido", e);

    } finally {
        // sempre executa — libere recursos aqui
        log.info("Processamento finalizado para: " + cpf);
    }
}
```

---

## throw vs throws — a confusão mais comum

São palavras parecidas com funções completamente diferentes.

`throw` age dentro do método — lança uma exceção no momento em que é executado:

```java
public void validar(double valor) {
    if (valor <= 0) {
        throw new IllegalArgumentException("Valor deve ser positivo");
    }
}
```

`throws` age na assinatura do método — avisa quem chama que essa exceção pode ser propagada e precisa ser tratada:

```java
public void conectarBanco() throws SQLException {
    Connection conn = DriverManager.getConnection(url, user, pass);
}
```

---

## A ordem dos catch blocks importa

Capture sempre do mais específico para o mais genérico. Se inverter, o compilador acusa erro de código inacessível — a exceção genérica captura tudo antes da específica ter chance de agir.

```java
// Correto — específico antes do genérico
} catch (FileNotFoundException e) {
    // captura primeiro
} catch (IOException e) {
    // captura depois
}

// Erro de compilação — IOException captura FileNotFoundException antes
} catch (IOException e) {
} catch (FileNotFoundException e) { // inacessível
}
```

---

## Para quem já tem uma bagagem maior em Java

O `finally` tem um comportamento que vai aparecer no próximo post e que vale antecipar: ele sempre executa — inclusive quando há um `return` no `try`. E se o próprio `finally` tiver um `return`, ele sobrescreve o `return` do `try` silenciosamente. É uma das pegadinhas favoritas do OCP — e o tema completo do post 7 da série.

---

No próximo post vamos ver o `try-with-resources` — a forma moderna de garantir que recursos sejam fechados automaticamente, sem depender do `finally`.

Alguma dúvida sobre as palavras-chave? Já confundiu `throw` com `throws` em algum momento? Bora trocar experiência aqui embaixo.
