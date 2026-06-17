# 🐘 Estrutura Padrão do `build.gradle.kts` (Kotlin DSL)

Este guia apresenta a estrutura padrão, ordem recomendada de blocos e convenções de sintaxe para arquivos de build do Gradle utilizando a **Kotlin DSL** (`.kts`).

---

## 1. Ordem Padrão de Blocos (Convenção de Cima para Baixo)

O Gradle exige que certas diretivas (como os plugins) sejam declaradas no início do arquivo para que ele possa carregar e validar o restante das instruções. A ordem recomendada pelo mercado é:

1. **`plugins {}`**: Carrega as extensões que adicionam funcionalidades ao Gradle (ex: Java, Spring Boot).
2. **Metadados do Projeto**: Configuração do `group`, `version` e a versão do compilador Java (`sourceCompatibility`).
3. **`repositories {}`**: Configura de quais servidores o Gradle deve buscar as dependências externas.
4. **`dependencies {}`**: Lista das bibliotecas que serão baixadas e utilizadas nas etapas de compilação, execução e testes.
5. **Configurações de Tarefas (`tasks`)**: Customização de execução (ex: configurar testes com JUnit 5).

---

## 2. Esqueleto de Referência do Arquivo

```kotlin
// =================================================================
// 1. PLUGINS (Sempre no topo do arquivo)
// =================================================================
plugins {
    java // Plugin nativo do Java
    id("org.springframework.boot") version "3.2.0" // Plugin de terceiros
    id("io.spring.dependency-management") version "1.1.4"
}

// =================================================================
// 2. METADADOS E VERSÃO DO JAVA
// =================================================================
group = "com.santander"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21 // Define Java 21 como versão de compilação
}

// =================================================================
// 3. REPOSITÓRIOS (Onde buscar as dependências)
// =================================================================
repositories {
    mavenCentral() // Busca no repositório padrão e oficial do Maven Central
}

// =================================================================
// 4. DEPENDÊNCIAS (Bibliotecas do Projeto)
// =================================================================
dependencies {
    // Dependências de produção
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // Dependências exclusivas para execução de testes (não vão para o pacote final)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// =================================================================
// 5. TAREFAS (Configurações de Execução)
// =================================================================
tasks.withType<Test> {
    useJUnitPlatform() // Configura a tarefa de testes para usar o JUnit 5
}
```

---

## 3. Principais Regras de Sintaxe da Kotlin DSL

A Kotlin DSL é baseada na linguagem Kotlin e, por isso, possui regras de sintaxe mais rígidas que a Groovy DSL tradicional:

* **Uso obrigatório de aspas duplas (`"..."`):** Textos e coordenadas de dependências devem obrigatoriamente estar entre aspas duplas. Aspas simples (`'...'`) são destinadas apenas a caracteres individuais (tipo `Char`) e causarão erro de compilação.
* **Uso de parênteses em chamadas:** Diferente da Groovy DSL, funções e métodos no Kotlin DSL exigem parênteses. Use `implementation("grupo:artefato:versão")` em vez de `implementation 'grupo:artefato:versão'`.
* **Declaração de Variáveis locais:** Se precisar criar uma constante para gerenciar uma versão de biblioteca centralizada, declare com a palavra-chave **`val`** e utilize interpolação de strings com o prefixo `$`:
  ```kotlin
  val junitVersion = "5.10.1"
  
  dependencies {
      testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
  }
  ```
