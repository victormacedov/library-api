# Library API

API para gerenciamento de uma biblioteca, desenvolvida em Java com Spring Boot, utilizando autenticação OAuth2/JWT, documentação Swagger, monitoramento Actuator, testes automatizados, e deploy via Docker e AWS (EC2 + RDS).

---

## Tecnologias Utilizadas

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

## Estudo de Segurança com Spring Security, OAuth2 e JWT

Este projeto foi desenvolvido com o objetivo de estudar e aplicar na prática os conceitos de **segurança** em aplicações Java utilizando o Spring Security. O foco principal está em entender, testar e demonstrar os principais fluxos de **autenticação** e **autorização** com OAuth2 e JWT, contemplando cenários reais de APIs REST seguras.

### 🛡️ Como funciona o fluxo de segurança, autenticação e autorização nesta API?

#### 1. Segurança

- **Configuração centralizada:** Por meio das classes `SecurityConfiguration` e `AuthorizationServerConfiguration`, com uso intensivo do Spring Security.
- **Criptografia de senhas:** Senhas dos usuários são criptografadas usando BCrypt.
- **Tokens JWT:** Toda autenticação e autorização é feita via tokens JWT, assinados com chave RSA.
- **OAuth2 Authorization Server:** Implementação completa do fluxo OAuth2, incluindo endpoints para autorizar, obter, validar e revogar tokens.
- **Proteção dos endpoints:** Só endpoints públicos (login, documentação, etc.) são liberados. O restante exige autenticação via token.
- **Customização de perfis:** Remoção dos prefixos padrão nos papéis (roles) para facilitar a checagem de permissões.

#### 2. Autenticação

- **Login tradicional:** Usuário acessa `/login` e envia login/senha. As credenciais são verificadas e, se válidas, um JWT é gerado e devolvido.
- **Login social (OAuth2):** Possibilidade de autenticação via Google. Usuários autenticados por terceiros são registrados automaticamente.
- **JWT no header:** Após o login, o token JWT é enviado no cabeçalho Authorization em cada requisição.

#### 3. Autorização

- **Por perfil:** Cada endpoint tem restrições de acesso via anotação `@PreAuthorize(...)` nos controllers (ex: apenas GERENTE pode cadastrar autores).
- **Validação do token:** O backend valida o JWT, extrai os papéis do usuário e permite ou bloqueia o acesso.
- **Filtros customizados:** Uso de filtros personalizados para garantir que o contexto do usuário seja carregado corretamente.

#### 4. Exemplos práticos de controle de acesso

- `@PreAuthorize("hasRole('GERENTE')")`: Apenas gerentes podem criar, atualizar ou deletar autores.
- `@PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")`: Operadores e gerentes podem visualizar dados.

#### 5. Fluxo resumido

1. Usuário faz login (formulário ou OAuth2).
2. Backend valida credenciais e gera JWT.
3. Usuário consome endpoints protegidos, enviando JWT no header.
4. Backend valida token, verifica permissões e executa ou bloqueia a ação.

---

*Este projeto é ideal para quem deseja estudar e dominar na prática o **Spring Security**, **OAuth2** e **JWT** aplicados em APIs modernas e seguras!*

---

## Licença

Distribuído sob a Licença MIT.

---

## Observações Adicionais

- As URLs podem variar conforme o ambiente (local, EC2, etc.)
- Para acessar endpoints protegidos, obtenha um token JWT via fluxo de autenticação.
- O projeto está pronto para CI/CD e escalabilidade via Docker e AWS.