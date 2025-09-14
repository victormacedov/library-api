# Library API

API para gerenciamento de uma biblioteca, desenvolvida em Java com Spring Boot, utilizando autenticação OAuth2/JWT, documentação Swagger, monitoramento Actuator, testes automatizados, e deploy via Docker e AWS (EC2 + RDS).

---

## Tecnologias Utilizadas:

- **Java 21**  
  Linguagem principal da aplicação, robusta e amplamente utilizada para aplicações corporativas.

- **Spring Boot**  
  Framework para facilitar o desenvolvimento de APIs REST, gerenciando dependências, injeção de dependências, configuração e inicialização rápida.

- **Spring Security + OAuth2 + JWT**  
  Segurança da API via autenticação e autorização, usando tokens JWT e padrão OAuth2, garantindo proteção dos endpoints.

- **Spring Data JPA**  
  Abstração para persistência e consultas avançadas em banco relacional com uso de repositórios.

- **Hibernate**  
  ORM para mapeamento objeto-relacional e manipulação das entidades no banco de dados.

- **Swagger (OpenAPI v3)**  
  Documentação interativa dos endpoints da API, facilitando testes e entendimento das rotas.

- **Spring Actuator**  
  Monitoramento e métricas da aplicação, endpoints para saúde, métricas e gerenciamento.

- **Lombok**  
  Redução de boilerplate no Java, gerando getters/setters/constructores automaticamente.

- **Docker**  
  Containerização da aplicação para ambientes replicáveis e escaláveis.

- **AWS EC2 & AWS RDS**  
  Deploy em nuvem, com instância EC2 para o backend e RDS para o banco de dados, garantindo alta disponibilidade e escalabilidade.

---

## Endpoints Principais

- **Swagger:**
    - Documentação interativa:
      ```
      /swagger-ui/index.html
      ```
    - Exemplo:
      ```
      http://<SEU_HOST>/swagger-ui/index.html
      ```

- **Spring Actuator:**
    - Monitoramento:
      ```
      /actuator
      ```
    - Exemplos de endpoints:
        - Saúde: `/actuator/health`
        - Métricas: `/actuator/metrics`
        - Informações: `/actuator/info`

- **Autenticação/Login:**
    - Página de login:
      ```
      /login
      ```
    - Fluxo OAuth2/JWT:
        - Autorizar: `/oauth2/authorize`
        - Obter token: `/oauth2/token`
        - Introspecção de token: `/oauth2/introspect`
        - Logout: `/oauth2/logout`

---

## Principais Funcionalidades

- Cadastro, busca, atualização e deleção de livros e autores
- Validações automáticas com feedback detalhado por erros
- Filtros de pesquisa avançados por título, ISBN, autor e gênero
- Controle de acesso por perfis (OPERADOR, GERENTE)
- Testes automatizados para repositórios e serviços
- Logging detalhado das operações via SLF4J

---

## Licença

Distribuído sob a Licença MIT.

---

## Observações Adicionais

- As URLs podem variar conforme o ambiente (local, EC2, etc.)
- Para acessar endpoints protegidos, obtenha um token JWT via fluxo de autenticação.
- O projeto está pronto para CI/CD e escalabilidade via Docker e AWS.
