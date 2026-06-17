# Try-with-resources: por que você não deveria mais usar finally para fechar recursos

Série: Tratamento de Exceções em Java — post 4 de 8

Nos posts anteriores vimos as palavras-chave que controlam o fluxo de exceções, incluindo o `finally`. Hoje vamos ver por que, quando o assunto é fechar recursos, o `finally` tem um substituto mais seguro e mais limpo desde o Java 7.

Roteiro da série:

1. Checked e Unchecked
2. A hierarquia do Throwable
3. try, catch, finally, throw e throws
4. Try-with-resources — você está aqui
5. Exceções customizadas
6. Multi-catch e encadeamento
7. A pegadinha do finally com return
8. @ControllerAdvice no Spring Boot

---

## O problema — finally para fechar recursos

Qualquer recurso que você abre — arquivo, conexão com banco, stream — precisa ser fechado depois. Sem isso, o recurso fica preso em memória. Em uma API com múltiplas requisições simultâneas, esse vazamento derruba o sistema em produção.

O jeito clássico era fechar no `finally`. O problema é que o método `close()` também pode lançar exceção — o que obriga um `try-catch` dentro do `finally`:

```java
BufferedReader reader = null;
try {
    reader = new BufferedReader(new FileReader("dados.txt"));
    System.out.println(reader.readLine());
} catch (IOException e) {
    System.out.println("Erro: " + e.getMessage());
} finally {
    if (reader != null) {
        try {
            reader.close(); // close() também lança IOException
        } catch (IOException e) {
            System.out.println("Erro ao fechar: " + e.getMessage());
        }
    }
}
```

Verboso, propenso a erros e fácil de esquecer o `close()`.

---

## A solução — try-with-resources

Introduzido no Java 7, o `try-with-resources` fecha o recurso automaticamente ao final do bloco — com sucesso ou com exceção — sem precisar de `finally`:

```java
// O BufferedReader é fechado automaticamente
try (BufferedReader reader = new BufferedReader(new FileReader("dados.txt"))) {
    System.out.println(reader.readLine());
} catch (IOException e) {
    System.out.println("Erro: " + e.getMessage());
}
```

O recurso é declarado dentro dos parênteses do `try`. O Java garante que `close()` será chamado — sem código extra, sem risco de esquecer.

---

## A única exigência — AutoCloseable

Para usar no `try-with-resources`, o recurso precisa implementar a interface `AutoCloseable`. As classes do próprio Java já implementam: `BufferedReader`, `Connection`, `InputStream`, `PreparedStatement` e outras.

Você pode criar as suas próprias:

```java
public class ConexaoBanco implements AutoCloseable {

    public ConexaoBanco() {
        System.out.println("Conexão aberta");
    }

    @Override
    public void close() {
        System.out.println("Conexão fechada automaticamente");
    }
}

// Uso
try (ConexaoBanco conexao = new ConexaoBanco()) {
    // usa a conexão
}
// close() é chamado aqui automaticamente
```

---

## Múltiplos recursos

Você pode declarar mais de um recurso separando por ponto e vírgula. Eles são fechados na ordem inversa da declaração:

```java
try (
    Connection conexao = dataSource.getConnection();
    PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM contas")
) {
    ResultSet rs = stmt.executeQuery();
} catch (SQLException e) {
    throw new RuntimeException("Erro na consulta", e);
}
// stmt é fechado primeiro, depois conexao
```

---

## Para quem já tem uma bagagem maior em Java

O `try-with-resources` tem um comportamento diferente do `finally` clássico quando ambos — o `try` e o `close()` — lançam exceção ao mesmo tempo. No `finally` clássico, a exceção do `finally` engolia a do `try`. No `try-with-resources`, a exceção do `close()` é suprimida e fica acessível via `getSuppressed()` — sem perder a exceção principal:

```java
} catch (RuntimeException e) {
    Throwable[] suprimidas = e.getSuppressed();
    // suprimidas[0] = exceção do close()
}
```

Esse comportamento é mais seguro porque não perde informação — a causa raiz permanece acessível para diagnóstico.

---

No próximo post vamos ver como criar exceções customizadas para expressar erros de negócio com clareza — e como elas se conectam ao `@ControllerAdvice` no Spring Boot.

Você já se deparou com um vazamento de recursos por esquecer o `close()`? Como resolveu? Bora trocar experiência aqui embaixo.
