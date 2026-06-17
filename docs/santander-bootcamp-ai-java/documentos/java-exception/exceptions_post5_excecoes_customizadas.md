# Exceções customizadas: como criar erros de negócio claros no seu projeto Spring Boot

Série: Tratamento de Exceções em Java — post 5 de 8

Nos posts anteriores construímos a base — o que são exceções, a hierarquia do Throwable, as palavras-chave e o try-with-resources. Agora chegou a hora de criar as nossas próprias exceções. Esse é o ponto onde o tratamento de erros deixa de ser técnico e passa a falar a língua do negócio.

---

Exceções customizadas são exceções criadas por você, com um nome e uma mensagem que descrevem o problema do seu domínio — não da JVM. Em vez de receber uma `NullPointerException` que não diz nada, você recebe uma `ContaNaoEncontradaException` que diz exatamente o que falhou.

---

Roteiro da série:

1. Checked e Unchecked
2. A hierarquia do Throwable
3. try, catch, finally, throw e throws
4. Try-with-resources
5. Exceções customizadas <----- aqui estamos
6. Multi-catch e encadeamento
7. A pegadinha do finally com return
8. @ControllerAdvice no Spring Boot

---

## Por que criar exceções customizadas?

Imagine que um cliente tenta fazer um Pix e a conta não existe. Sem exceção customizada, o sistema lançaria algo como `NoSuchElementException` ou `NullPointerException` — mensagens que não dizem nada para quem consome a API e dificultam o diagnóstico em produção.

Com uma exceção customizada, a mensagem é clara, rastreável e pode ser capturada de forma específica pelo `@ControllerAdvice` para retornar um HTTP 404 limpo.

---

## Como criar — a regra de herança

A escolha de qual classe estender segue a mesma regra do post 1:

```java
// Unchecked — não exige try-catch em quem chama
// Use para erros de domínio e regras de negócio
public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException(String id) {
        super("Conta não encontrada: " + id);
    }
}

// Checked — exige try-catch ou throws em quem chama
// Use quando quem chama precisa decidir como reagir à falha
public class SaldoInsuficienteException extends Exception {

    public SaldoInsuficienteException(double saldo) {
        super("Saldo insuficiente: R$ " + saldo);
    }
}
```

---

## Usando no Service

```java
@Service
public class ServicoTransferencia {

    public void transferir(String cpf, double valor) {
        Conta conta = repository.findByCpf(cpf);

        if (conta == null) {
            throw new ContaNaoEncontradaException(cpf);
        }

        if (conta.getSaldo() < valor) {
            throw new ContaNaoEncontradaException(
                "Saldo insuficiente para transferência de R$ " + valor);
        }

        conta.debitar(valor);
    }
}
```

O Service lança a exceção com uma mensagem clara. O Controller não precisa saber nada disso — a exceção sobe automaticamente para o `@ControllerAdvice`, que veremos no post final da série.

---

## Preservando a causa raiz

Quando você relança uma exceção customizada a partir de outra exceção, sempre preserve a causa original no segundo argumento. Sem isso, o stack trace perde a raiz do problema e o diagnóstico em produção fica muito mais difícil:

```java
try {
    transacaoRepository.save(transacao);
} catch (DataAccessException e) {
    throw new FalhaDeTransferenciaException(
        "Erro ao registrar transferência Pix", e); // e = causa original preservada
}
```

---

## Para quem já tem uma bagagem maior em Java

No Spring Boot, a convenção dominante é usar Unchecked Exceptions para exceções de domínio. O motivo é prático: o `@ControllerAdvice` já centraliza o tratamento globalmente — forçar `try-catch` em cada Service apenas adiciona ruído sem benefício real. Checked Exceptions fazem mais sentido em operações de infraestrutura onde quem chama precisa decidir explicitamente como reagir à falha — leitura de arquivo, conexão JDBC, chamada a serviço externo.

---

Entendeu a importância de usar Exceções customizadas?

Sem elas, sua API fala a língua da JVM — `NullPointerException`, `NoSuchElementException` — e quem consome não entende o que falhou. Com elas, sua API fala a língua do negócio — `ContaNaoEncontradaException`, `SaldoInsuficienteException` — e qualquer desenvolvedor entende o problema sem precisar abrir o código.

---

No próximo post vamos ver dois recursos que muita gente não conhece: o multi-catch, que elimina blocos `catch` repetidos, e o encadeamento de exceções, que garante que a causa raiz nunca se perca.

Você já usou exceções customizadas no seu projeto? Como ficou a estrutura? Bora trocar experiência aqui embaixo.
