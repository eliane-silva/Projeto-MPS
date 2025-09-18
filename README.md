# Sistema de Agendamento de Turnos para Taxistas

Este projeto é um sistema de agendamento de turnos destinado a taxistas que prestam serviço em um supermercado. Ele permite que taxistas escolham turnos disponíveis através de um calendário, com suporte a múltiplos taxistas por turno.

---

## 📌 Funcionalidades principais

* Cadastro de taxistas
* Visualização de calendário de turnos
* Agendamento de presença em turnos
* Painel administrativo para o supermercado
* Limite opcional de taxistas por turno

---

## 🚀 Como executar o projeto

Se você estiver rodando em um novo computador, siga estes passos:

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd agendamento-taxi
```

### 2. Compilar o projeto

```bash
mvn clean package
```

### 3. Executar a aplicação

#### Opção A — Usando Maven Exec (recomendado)

```bash
mvn -q exec:java "-Dexec.mainClass=projetomps.Application" "-Dapp.logger=SLF4J"
```

Ou para usar o backend JUL:

```bash
mvn -q exec:java "-Dexec.mainClass=projetomps.Application" "-Dapp.logger=JUL"
```

#### Opção B — Usando classes compiladas diretamente

```bash
java -Dapp.logger=SLF4J -cp target/classes projetomps.Application
```

#### Opção C — Gerando um JAR executável (opcional)

Se quiser rodar apenas com `java -jar`, configure no `pom.xml` o plugin `maven-shade-plugin` para gerar um **fat jar** com a classe principal. Depois, basta:

```bash
java -Dapp.logger=SLF4J -jar target/agendamento-taxi-1.0-SNAPSHOT.jar
```

---

## 📚 Diagramas

Link com todas as versões dos diagramas:
[https://drive.google.com/file/d/1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG\_/view?usp=sharing](https://drive.google.com/file/d/1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG_/view?usp=sharing)

### 📌 Diagrama de Casos de Uso

Diagrama de Casos de Uso - Página 1

[https://viewer.diagrams.net/index.html?tags=%7B%7D\&lightbox=1\&highlight=0000ff\&edit=\_blank\&layers=1\&nav=1\&title=casos\_uso\_mps.drawio\&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG\_%26export%3Ddownload#%7B%22pageId%22%3A%22E6nLn4tzgp77ZIwUhhSh%22%7D](https://viewer.diagrams.net/index.html?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=casos_uso_mps.drawio&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG_%26export%3Ddownload#%7B%22pageId%22%3A%22E6nLn4tzgp77ZIwUhhSh%22%7D)

Este diagrama mostra os principais casos de uso do sistema, incluindo as ações dos taxistas e do administrador.

---

### 📌 Diagrama de Classes

Página 2 - Versão 1 do diagrama de classes:

[https://viewer.diagrams.net/index.html?tags=%7B%7D\&lightbox=1\&highlight=0000ff\&edit=\_blank\&layers=1\&nav=1\&title=casos\_uso\_mps.drawio\&page-id=fCWObkfd2ajQvfXFF1W4\&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG\_%26export%3Ddownload](https://viewer.diagrams.net/index.html?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=casos_uso_mps.drawio&page-id=fCWObkfd2ajQvfXFF1W4&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG_%26export%3Ddownload)

Página 3 - Versão 2 do diagrama de classes:

[https://viewer.diagrams.net/?tags=%7B%7D\&lightbox=1\&highlight=0000ff\&edit=\_blank\&layers=1\&nav=1\&title=casos\_uso\_mps.drawio\&page-id=HUowIE4yDC5HB\_rYqbEx\&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG\_%26export%3Ddownload](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=casos_uso_mps.drawio&page-id=HUowIE4yDC5HB_rYqbEx&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG_%26export%3Ddownload)

Página 4 - Versão 3 do diagrama de classes:

[https://viewer.diagrams.net/?tags=%7B%7D\&lightbox=1\&highlight=0000ff\&edit=\_blank\&layers=1\&nav=1\&title=casos\_uso\_mps.drawio\&page-id=FyTWmG0w\_ouTMJRqWyir\&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG\_%26export%3Ddownload](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=casos_uso_mps.drawio&page-id=FyTWmG0w_ouTMJRqWyir&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG_%26export%3Ddownload)

Página 5 - Versão 4 do diagrama de classes (Versão mais atual):

https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=casos_uso_mps.drawio&page-id=l51aFDUm2rJoQA-2TEDH&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1rlWsZLTiEm6y4cQvllX5Dk3r6GLCnoG_%26export%3Ddownload


Este diagrama representa a estrutura básica do sistema, incluindo as entidades principais como Taxista, Turno e Agendamento.


