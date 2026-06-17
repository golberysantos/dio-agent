# ☕ Desmistificando Java Collections: Guia de Consulta Rápida

Este material serve como guia de consulta didático sobre o funcionamento e uso do **Java Collections Framework**.

---

## 🛑 Por que não usar Arrays comuns (`[]`)?

Antes de falar de Collections, pense no Array tradicional do Java. Ele tem uma limitação física crucial: **o tamanho é fixo**. Se você cria um `String[] nomes = new String[5]`, você nunca poderá colocar um 6º nome ali sem criar um novo array e copiar tudo manualmente.

As **Collections** surgiram para resolver isso. Elas são estruturas dinâmicas que crescem e diminuem sozinhas na memória.

---

## 🗺️ O Mapa Mental da Hierarquia

Para entender Collections sem decoreba, precisamos separar o framework em duas grandes estruturas de dados:

### 1️⃣ Hierarquia de Collection (Valores Individuais)
Usada para armazenar elementos simples (um valor por posição).
* **List (`«interface»`)**: Aceita elementos repetidos e garante a ordem de inserção.
  * `ArrayList` (`«class»`): Busca rápida por índice. Ótima para leitura.
  * `LinkedList` (`«class»`): Inserção e remoção rápida no início ou fim.
* **Set (`«interface»`)**: Não aceita duplicados e (por padrão) não garante ordem.
  * `HashSet` (`«class»`): A mais rápida de todas, sem ordem definida.
  * `LinkedHashSet` (`«class»`): Sem duplicados, mas mantém a ordem em que você adicionou os itens.
  * `TreeSet` (`«class»`): Mantém os elementos ordenados automaticamente.

### 2️⃣ Hierarquia de Map (Chaves e Valores)
Usada para associar uma chave única a um valor (como um dicionário de busca).
* **Map (`«interface»`)**: Tabela de associação chave-valor.
  * `HashMap` (`«class»`): Extremamente veloz, chaves sem ordem definida.
  * `LinkedHashMap` (`«class»`): Mantém as chaves na ordem de inserção.
  * `TreeMap` (`«class»`): Mantém as chaves ordenadas automaticamente.

---

## ⚖️ Comparação Direta: List vs Set

Para ficar fácil de lembrar na hora de escolher qual usar:

* **List (Exemplo: ArrayList)**
  * Permite elementos duplicados? **Sim**
  * Mantém a ordem em que você adicionou? **Sim** (ordem de inserção)
  * Permite pegar um item direto pela posição usando o método `.get(índice)`? **Sim**

* **Set (Exemplo: HashSet)**
  * Permite elementos duplicados? **Não** (itens repetidos são ignorados)
  * Mantém a ordem em que você adicionou? **Não** (a ordem é aleatória ou gerida internamente)
  * Permite pegar um item direto pela posição usando o método `.get(índice)`? **Não** (você precisa percorrer o conjunto para achar o item)

---

## 💎 O segredo do operador Diamond `<>` (Generics)

Quando você vê códigos modernos em Java, sempre se depara com isto:
```java
List<String> linguagens = new ArrayList<>();
```
O `<String>` (operador Diamond) serve para colocar uma **etiqueta de segurança** na sua lista. Com isso, o Java sabe que essa lista só aceita textos e avisa você de qualquer erro **antes** do programa compilar. 

> ⚠️ **Regra de ouro:** Generics não aceita tipos primitivos. Use as classes Wrapper correspondentes (`<Integer>` em vez de `<int>`, `<Double>` em vez de `<double>`).

---

## 💻 Exemplo Prático de Código

```java
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TesteCollections {
    public static void main(String[] args) {
        // Criando uma lista (Aceita duplicados e mantém a ordem)
        List<String> minhaLista = new ArrayList<>();
        minhaLista.add("Java");
        minhaLista.add("Spring Boot");
        minhaLista.add("Java"); // Repetido

        System.out.println("Lista: " + minhaLista); 
        // Saída: [Java, Spring Boot, Java]

        // Criando um conjunto (Rejeita duplicados)
        Set<String> meuConjunto = new HashSet<>();
        meuConjunto.add("Java");
        meuConjunto.add("Spring Boot");
        meuConjunto.add("Java"); // Será ignorado silenciosamente

        System.out.println("Conjunto: " + meuConjunto); 
        // Saída: [Java, Spring Boot] (ou ordem aleatória)
    }
}
```
