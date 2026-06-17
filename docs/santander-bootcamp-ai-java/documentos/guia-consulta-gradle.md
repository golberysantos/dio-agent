# 🐘 Guia de Estudo: Entendendo o Gradle

O **Gradle** é uma ferramenta de automação de build e gerenciamento de dependências de última geração, criada para corrigir limitações do Maven, principalmente em relação a desempenho e flexibilidade de customização.

---

## 1. A Filosofia do Gradle

Enquanto o Maven é **declarativo** (baseado em XML rígido e tags fixas), o Gradle é **programável** (baseado em scripts de código). 

No Gradle, o arquivo de configuração é um script executável escrito em uma linguagem de programação de script. Ele suporta duas linguagens principais (chamadas de DSL - Domain Specific Language):
1. **Groovy DSL** (nome do arquivo: `build.gradle`)
2. **Kotlin DSL** (nome do arquivo: `build.gradle.kts`)

---

## 2. A Estrutura de um Arquivo `build.gradle` (Groovy)

Abaixo está o esqueleto padrão de um arquivo de configuração do Gradle para um projeto Java:

```groovy
// 1. Bloco de Plugins (Extensões do Gradle)
plugins {
    id 'java' // Habilita suporte a compilação, testes e empacotamento Java
    id 'org.springframework.boot' version '3.2.0' // Opcional: Plugin do Spring Boot
}

// Coordenadas do Projeto
group = 'com.santander'
version = '1.0.0-SNAPSHOT'
sourceCompatibility = '21' // Versão do Java

// 2. Repositórios (Onde buscar as dependências)
repositories {
    mavenCentral() // Busca no repositório padrão Maven Central
}

// 3. Dependências do Projeto
dependencies {
    // Dependência de compilação e execução padrão (substitui o escopo 'compile')
    implementation 'org.postgresql:postgresql:42.7.1'

    // Dependência exclusiva para execução de testes (substitui o escopo 'test')
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.10.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.10.1'
}

// 4. Configuração de Tarefas (Tasks)
test {
    useJUnitPlatform() // Configura a tarefa de teste para usar o JUnit 5
}
```

---

## 3. Entendendo as Configurações de Dependência

No Gradle, em vez de usarmos a tag `<scope>` do Maven, declaramos a dependência usando palavras-chave específicas que definem como a biblioteca será tratada:

* **`implementation`**: A dependência é compilada e empacotada no projeto, mas ela não é exposta para outros projetos que importarem o seu módulo (evita vazamento de dependências).
* **`api`** (requer o plugin `java-library`): Semelhante ao `implementation`, mas expõe a dependência de forma transitiva para outros módulos.
* **`testImplementation`**: A biblioteca fica disponível apenas para compilar e rodar a pasta de testes (ex: JUnit, Mockito).
* **`runtimeOnly`**: A biblioteca não é necessária na compilação, apenas no momento de execução (ex: drivers JDBC).
* **`compileOnly`**: Necessária apenas na compilação e descartada na execução (ex: biblioteca Lombok, usada para gerar getters/setters em tempo de compilação).

---

## 4. O Gradle Wrapper (A Pasta `gradle/`)

Quando você cria um projeto Gradle, ele gera um utilitário chamado **Gradle Wrapper** (composto pelas pastas `gradle/wrapper/` e pelos scripts `gradlew` e `gradlew.bat`).

* **O que ele faz:** Ele trava a versão exata do Gradle usada no projeto.
* **Por que é crucial:** Você não precisa ter o Gradle instalado fisicamente na sua máquina. O script de inicialização detecta se você tem a versão configurada; caso não tenha, ele faz o download do Gradle em background e roda o build. Isso garante que o projeto compile exatamente da mesma forma em qualquer computador de desenvolvimento ou servidor de integração contínua.
* **Uso no terminal:**
  * No Windows: `.\gradlew.bat build`
  * No Linux/Mac: `./gradlew build`

---

## 5. Performance e Velocidade (O diferencial do Gradle)

O grande trunfo do Gradle frente ao Maven é a velocidade de build. Ele otimiza o tempo de compilação através de três pilares:

1. **Compilação Incremental (Incremental Builds):** O Gradle analisa as entradas e saídas de cada tarefa. Se você alterou apenas um arquivo Java, ele compila apenas esse arquivo e reutiliza o restante que já estava compilado.
2. **Build Cache (Cache de Compilação):** O Gradle pode armazenar os resultados de compilações anteriores (locais ou na nuvem) e evitar reexecutar tarefas se os arquivos de entrada não mudaram.
3. **Gradle Daemon (Daemon do Gradle):** É um processo em background que fica rodando na memória RAM da sua máquina. Ele mantém os arquivos do compilador quentes na memória, evitando o tempo de inicialização (startup) do Java a cada build.

---

## 6. Principais Comandos de Terminal

Sempre execute os comandos utilizando o wrapper (`gradlew` ou `.\gradlew.bat`):

* **`./gradlew clean`**: Apaga a pasta `build/` (limpa compilações antigas).
* **`./gradlew compileJava`**: Compila as classes Java principais.
* **`./gradlew test`**: Roda os testes unitários e gera um relatório gráfico em formato HTML na pasta `build/reports/tests/test/index.html`.
* **`./gradlew build`**: Executa o ciclo completo (limpa, compila, testa e gera o arquivo `.jar` final dentro de `build/libs/`).
* **`./gradlew tasks`**: Lista todas as tarefas disponíveis no projeto que você pode executar.
