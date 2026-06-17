# A pegadinha do finally com return — o comportamento que pega até dev experiente

Série: Tratamento de Exceções em Java — post 7 de 8

Nos posts anteriores vimos multi-catch e encadeamento de exceções. Hoje chegamos a uma das pegadinhas favoritas do OCP — e que aparece em entrevistas técnicas com frequência: o que acontece quando o `finally` tem um `return`.

---

Roteiro da série:

1. Checked e Unchecked
2. A hierarquia do Throwable
3. try, catch, finally, throw e throws
4. Try-with-resources
5. Exceções customizadas
6. Multi-catch e encadeamento
7. A pegadinha do finally com return <----- aqui estamos
8. @ControllerAdvice no Spring Boot

---

## A regra fundamental

O bloco `finally` sempre executa — e se ele tiver um `return`, ele sobrescreve qualquer `return` que estava no `try` ou no `catch`. Sem exceção.

```java
public int calcular() {
    try {
        return 1; // este return é ignorado
    } finally {
        return 2; // sempre retorna 2
    }
}
```

Mesmo que o `try` execute normalmente e chegue no `return 1`, o `finally` entra em ação antes da saída do método e substitui o valor. O método sempre retorna `2`.

---

## A situação mais perigosa — exceção engolida

Se o `finally` tiver um `return` e o `try` lançar uma exceção, a exceção é engolida silenciosamente:

```java
public int calcular() {
    try {
        throw new RuntimeException("erro grave");
    } finally {
        return 2; // a exceção é ENGOLIDA — nenhum erro é propagado
    }
}
// Retorna 2 como se nada tivesse acontecido
```

Esse comportamento é o mais perigoso: o código parece funcionar, mas uma exceção real foi silenciada. Em produção, esse tipo de bug é muito difícil de rastrear.

---

## A variação mais cobrada no OCP

```java
public String obterStatus() {
    try {
        return "try";
    } catch (Exception e) {
        return "catch";
    } finally {
        return "finally"; // sempre este
    }
}
```

A resposta é sempre `"finally"` — independente do que acontece no `try` ou no `catch`. O `finally` vence sempre.

---

## As regras para não errar

`finally` sempre executa — exceto se `System.exit()` for chamado ou a JVM travar.

Se `finally` tem `return`, ele sobrescreve qualquer outro `return` do `try` ou do `catch`.

Se `finally` tem `return`, ele engole qualquer exceção em andamento silenciosamente.

Nunca coloque `return` dentro de `finally`. Use-o apenas para liberar recursos — e mesmo assim, prefira o `try-with-resources` que vimos no post 4.

---

## Para quem já tem uma bagagem maior em Java

O comportamento de engolir exceções com `return` no `finally` é consistente entre Java 17 e Java 21 — não houve mudança. O compilador não emite nenhum aviso quando você coloca `return` dentro de `finally`, o que torna a pegadinha ainda mais traiçoeira. Algumas IDEs como o IntelliJ emitem um warning nesse caso — vale prestar atenção quando aparecer.

---

Entendeu por que o `return` dentro do `finally` é perigoso?

Porque ele não apenas sobrescreve o valor de retorno — ele silencia exceções reais sem nenhum aviso. O código parece funcionar normalmente enquanto um erro grave está sendo ignorado. A regra é simples: `finally` é para liberar recursos, não para retornar valores.

---

No próximo e último post da série vamos ver como centralizar o tratamento de todas essas exceções no Spring Boot com o `@ControllerAdvice` — o fechamento perfeito para tudo que construímos até aqui.

Você já caiu nessa pegadinha ou conhecia o comportamento? Bora trocar experiência aqui embaixo.
