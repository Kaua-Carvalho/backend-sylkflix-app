# 🎬 SylkFlix Backend - API RESTful

<br>
<p align="center">
  <img src="https://avatars.githubusercontent.com/u/138880659?v=4" width="100">
  <img src="https://avatars.githubusercontent.com/u/134431788?v=4" width="100">
  <img src="https://avatars.githubusercontent.com/u/159480669?v=4" width="100">
  <img src="https://avatars.githubusercontent.com/u/169946056?v=4" width="100">
</p>

<p align="center">S Y L K</p>

Backend do sistema SylkFlix desenvolvido em **Spring Boot** com autenticação **JWT**, persistência de dados com **MySQL (Aiven)** e documentação **Swagger**.

---

## 🚀 Links do Projeto

- **API em Produção:** [https://backend-sylkflix-app.onrender.com](https://backend-sylkflix-app.onrender.com)
- **Swagger/OpenAPI:** [https://backend-sylkflix-app.onrender.com/swagger-ui.html](https://backend-sylkflix-app.onrender.com/swagger-ui.html)
- **Frontend:** [https://sylkflix-app.vercel.app](https://sylkflix-app.vercel.app)

---

## 📋 Descrição do Projeto

O SylkFlix Backend é uma API REST completa que fornece:

- ✅ Autenticação de usuários com JWT
- ✅ CRUD completo de filmes assistidos
- ✅ Relacionamento usuário-filmes (1:N)
- ✅ Sistema de avaliação (1-5 estrelas)
- ✅ Gerenciamento de perfil de usuário
- ✅ Documentação Swagger/OpenAPI
- ✅ Deploy automatizado com Docker

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
    - Spring Web
    - Spring Data JPA
    - Spring Security
- **MySQL** - Banco de dados em nuvem (Aiven)
- **H2 Database** - Testes locais
- **JWT** (io.jsonwebtoken 0.12.3)
- **Swagger/OpenAPI** (springdoc-openapi)
- **Maven**
- **Docker** - Containerização
- **Render** - Hospedagem

---

## 📊 Modelagem do Banco de Dados

### Entidade: Usuario
```sql
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    profile_picture VARCHAR(50) DEFAULT 'Profile0',
    data_criacao TIMESTAMP NOT NULL
);
```

### Entidade: FilmeAssistido
```sql
CREATE TABLE filmes_assistidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tmdb_id INT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    poster_path VARCHAR(255),
    avaliacao INT NOT NULL CHECK (avaliacao BETWEEN 1 AND 5),
    data_adicionado TIMESTAMP NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    UNIQUE (usuario_id, tmdb_id)
);
```

**Relacionamento:** Um usuário pode ter vários filmes assistidos (1:N) com cascade delete.

---

## 🚀 Endpoints da API

### Autenticação (Público)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/register` | Registrar novo usuário |
| POST | `/api/auth/login` | Fazer login |

### Gerenciamento de Perfil (Protegido - JWT)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| PUT | `/api/auth/update-nome` | Atualizar nome do usuário |
| PUT | `/api/auth/profile-picture` | Atualizar foto de perfil |
| DELETE | `/api/auth/delete-account` | Deletar conta (cascade) |

### Filmes Assistidos (Protegido - JWT)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/assistidos` | Adicionar filme aos assistidos |
| GET | `/api/assistidos` | Listar todos os assistidos do usuário |
| GET | `/api/assistidos/check/{tmdbId}` | Verificar se filme é assistido |
| PUT | `/api/assistidos/{id}/avaliacao` | Atualizar avaliação do filme |
| DELETE | `/api/assistidos/{id}` | Remover filme dos assistidos |

---

## ⚙️ Configuração e Execução Local

### Pré-requisitos

- Java 17+
- Maven 3.6+
- MySQL (ou usar H2 para testes)

### Passo 1: Clonar o Repositório

```bash
git clone https://github.com/lgalvesz/backend-sylkflix-app
cd backend-sylkflix-app
```

### Passo 2: Configurar o Banco de Dados

#### Opção A: MySQL (Aiven - Produção)

1. Crie uma conta no [Aiven](https://aiven.io/)
2. Crie um serviço MySQL
3. Copie as credenciais de conexão
4. Configure as variáveis de ambiente (veja abaixo)

#### Opção B: H2 (Apenas Testes Locais)

Para desenvolvimento local rápido, o projeto já está configurado para usar H2.

### Passo 3: Configurar Variáveis de Ambiente

Crie um arquivo `.env` ou configure as variáveis no sistema:

```bash
# MySQL (Aiven)
SPRING_DATASOURCE_URL=jdbc:mysql://seu-host.aivencloud.com:porta/sylkflix?useSSL=true&requireSSL=true
SPRING_DATASOURCE_USERNAME=seu-username-aiven
SPRING_DATASOURCE_PASSWORD=sua-senha-aiven

# JWT Secret (gere com: openssl rand -base64 64)
JWT_SECRET=sua_chave_secreta_super_longa_minimo_64_caracteres

# CORS (adicione URLs do frontend)
CORS_ALLOWED_ORIGINS=http://localhost:5173,https://seu-frontend.vercel.app
```

### Passo 4: Instalar Dependências e Executar

```bash
# Instalar dependências
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 📚 Documentação Swagger

Após iniciar a aplicação, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs (JSON):** http://localhost:8080/api-docs

### Como usar o Swagger:

1. Acesse o Swagger UI
2. Registre um usuário em `/api/auth/register`
3. Faça login em `/api/auth/login` e copie o token JWT
4. Clique em **"Authorize"** no topo da página
5. Cole o token no formato: `Bearer seu_token_aqui`
6. Clique em **"Authorize"**
7. Agora você pode testar os endpoints protegidos!

---

## 🐳 Deploy no Render com Docker

### Passo 1: Preparar o Projeto

Certifique-se que o `Dockerfile` está na raiz do projeto:

```dockerfile
FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests \
    && apt-get remove -y maven \
    && rm -rf /var/lib/apt/lists/*

RUN cp target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Passo 2: Criar Web Service no Render

1. Acesse [render.com](https://render.com/) e faça login com GitHub
2. Clique em **"New +"** → **"Web Service"**
3. Conecte seu repositório
4. Configure:
    - **Name:** `sylkflix-backend`
    - **Region:** Escolha o mais próximo
    - **Branch:** `main`
    - **Runtime:** `Docker`
    - **Dockerfile Path:** `./Dockerfile`
    - **Docker Command:** (deixe vazio, já está no ENTRYPOINT)

### Passo 3: Configurar Variáveis de Ambiente

Na seção **Environment**, adicione:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://seu-host.aivencloud.com:porta/sylkflix?useSSL=true&requireSSL=true
SPRING_DATASOURCE_USERNAME=seu-username-aiven
SPRING_DATASOURCE_PASSWORD=sua-senha-aiven
JWT_SECRET=sua_chave_jwt_super_longa_64_caracteres_minimo
CORS_ALLOWED_ORIGINS=http://localhost:5173,https://seu-frontend.vercel.app
PORT=8080
```

### Passo 4: Deploy!

1. Clique em **"Create Web Service"**
2. Aguarde o build (~8-15 minutos na primeira vez)
3. Quando terminar, sua API estará em: `https://seu-app.onrender.com`

**🔥 Deploy Automático:** A cada push no GitHub, o Render fará rebuild e deploy automático!

---

## 🧪 Testando com cURL

### Registrar Usuário

```bash
curl -X POST https://backend-sylkflix-app.onrender.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123"
  }'
```

### Login

```bash
curl -X POST https://backend-sylkflix-app.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "senha123"
  }'
```

### Adicionar Filme Assistido (Com Token)

```bash
curl -X POST https://backend-sylkflix-app.onrender.com/api/assistidos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT_AQUI" \
  -d '{
    "tmdbId": 550,
    "titulo": "Clube da Luta",
    "posterPath": "/path/to/poster.jpg",
    "avaliacao": 5
  }'
```

---

## 🔒 Segurança

- **Senhas:** Criptografadas com BCrypt
- **JWT:** Tokens expiram em 24 horas
- **CORS:** Configurado para permitir apenas origens específicas
- **SQL Injection:** Prevenido pelo uso de JPA/Hibernate
- **HTTPS:** Obrigatório em produção (Render fornece SSL grátis)

---

## 📁 Estrutura do Projeto

```
backend-sylkflix/
├── src/
│   ├── main/
│   │   ├── java/com/sylkflix/
│   │   │   ├── SylkflixApplication.java
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── SwaggerConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   └── FilmeAssistidoController.java
│   │   │   ├── dto/
│   │   │   │   ├── AuthRequestDTO.java
│   │   │   │   ├── AuthResponseDTO.java
│   │   │   │   ├── FilmeAssistidoDTO.java
│   │   │   │   ├── FilmeAssistidoResponseDTO.java
│   │   │   │   ├── RegisterRequestDTO.java
│   │   │   │   └── UpdateNomeDTO.java
│   │   │   ├── model/
│   │   │   │   ├── Usuario.java
│   │   │   │   └── FilmeAssistido.java
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   └── FilmeAssistidoRepository.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── UsuarioService.java
│   │   │   │   └── FilmeAssistidoService.java
│   │   │   └── security/
│   │   │       ├── JwtTokenProvider.java
│   │   │       ├── JwtAuthenticationFilter.java
│   │   │       └── UserDetailsServiceImpl.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── Dockerfile
├── .dockerignore
├── pom.xml
└── README.md
```

---

## 🛠️ Troubleshooting

### Erro: "Access denied for user"
- Verifique as credenciais do MySQL no Render

### Erro: "JWT signature does not match"
- Verifique se o `JWT_SECRET` está configurado e é o mesmo em todos ambientes

### Erro: "CORS policy blocked"
- Adicione a URL do frontend em `CORS_ALLOWED_ORIGINS`
- Certifique-se que não há `/` no final da URL

---

## 🔗 Repositórios Relacionados

- **Frontend (React):** https://github.com/lgalvesz/sylkflix-app
- **Aplicação Live:** [sylkflix-app.vercel.app](https://sylkflix-app.vercel.app)

---

## 💻 Desenvolvedores

**Luís Gustavo**
- GitHub: [@lgalvesz](https://github.com/lgalvesz)
- LinkedIn: [Luís Gustavo](https://www.linkedin.com/in/luisgustavoalves/)
- Email: luisgalvessilva@gmail.com

**Kauã Carvalho**
- Github: [@Kaua-Carvalho](https://github.com/Kaua-Carvalho)
- Linkedin: [Kauã Ribeiro Carvalho](https://www.linkedin.com/in/kauã-ribeiro-carvalho/)
- Email: kauarcarvalho@gmail.com

**Stênio Siqueira**
- Github: [@StenioSiq](https://github.com/StenioSiq)
- Linkedin: [Stênio Siqueira](https://www.linkedin.com/in/stenio-siqueira/)
- Email: steniosqr@gmail.com

**Yago Henrique**
- Github: [@YagoHT](https://github.com/YagoHT)
- Linkedin: [Yago Henrique](https://www.linkedin.com/in/yago-henrique-toledo-del-pino-vieira/)
- Email: yagoh686@gmail.com

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos na disciplina de Frameworks Web.

---