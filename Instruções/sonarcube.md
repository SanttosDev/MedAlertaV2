# SonarQube

Para rodar o SonarQube localmente, siga os passos abaixo:

---

## 1) Baixar o SonarQube

1. Baixe o SonarQube no site:  
   https://www.sonarsource.com/products/sonarqube/downloads/

2. Selecione a versão **Community Edition**, que é gratuita.

3. Extraia a pasta para algum diretório que **não seja protegido**.  
   Exemplo: Eu criei uma pasta chamada `Sonar` na raiz do disco `C:` e extraí o SonarQube lá dentro. Mas você pode colocar em outro lugar se preferir.

4. Dentro da pasta extraída, navegue até:

   ```text
   sonarqube-25.11.0.114957\bin\windows-x86-64

5. Clique em **startSonar** (isso deve abrir um terminal onde irá mostrar que o sonar está rodando)


## 2) Entrar no SonarQube

1. Abra um navegador e acesse:

    ````text
    http://localhost:9000/
    
2. Será necessário fazer login:
    - Login: **admin**
    - Senha: **admin**

3. Na primeira vez, será necessário criar uma nova senha. É só criar outra
4. Depois disso, vocês estarão dentro do SonarQube (página principal).


## 3) Criar o projeto a ser analisado

1. Na página principal, clique em Create project.

2. A partir daí, serão exibidas algumas opções de configuração.
- O mais importante é garantir que:
    - project key: **MedAlerta**
    - project name: **MedAlerta**

3. Ao final, será gerado o token necessário para rodar a análise no SonarQube.

## 4) Rodar o SonarQube no projeto

1. Estando na raiz do projeto, execute no terminal:
    ````text
    $env:SONAR_TOKEN = "<token gerado>"
    (substitua <token gerado> pelo token criado no passo anterior)

2. Em seguida, rode:
    ````text
    mvn clean verify sonar:sonar

3. Agora a build dando certo, você deve conseguir ver a página do projeto no sonarqube com as informações do projeto