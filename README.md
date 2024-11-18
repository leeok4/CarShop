# CarShop - Sistema de Gerenciamento de Carros

## Descrição
CarShop é um sistema de gerenciamento de carros desenvolvido com Spring Boot que permite cadastrar, visualizar, editar e excluir informações de veículos. O sistema integra-se com a API FIPE para obter informações precisas sobre marcas e modelos, além de utilizar IA para sugerir preços baseados no mercado atual.
Por se tratar de um projeto de conclusão de curso tentei executar de forma simples, sem muitas funcionalidades.
Utilizei algumas tecnologias que possuia um conhecimento avançado, como Java, Spring Boot, Thymeleaf, Bootstrap, jQuery e Maven.
Sobre o processo de desenvolvimento, tentei utilizar boas praticas de programação, seguindo alguns padrões de projeto para organização do código, como MVC, Repository, Service, DTO, etc.
Para a persistência dos dados utilizei o banco de dados H2, por ser um banco de dados em memória, facilitando a execução do projeto.
Realizei a integração com a FIPE para obter informações sobre marcas e modelos de veículos, e com a OpenAI para sugerir preços baseados no mercado atual.
Alguns desafios sobre o processo de desenvolvimento é sempre manter o código limpo e organizado, e a integração com a API da OpenAI, pois foi a primeira vez que utilizei a API.

## Funcionalidades
- Cadastro completo de veículos
- Integração com API FIPE para marcas e modelos
- Upload de imagens dos veículos
- Sugestão de preços usando IA
- Listagem com filtros e ordenação
- Interface responsiva e amigável

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- Thymeleaf
- H2 Database
- Bootstrap 5
- jQuery
- OpenAI API
- Maven

## Pré-requisitos
- Java 17 ou superior
- Maven 3.6 ou superior
- Docker (opcional)
- Chave da API OpenAI

## Configuração

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/CarShop.git
cd CarShop
```

2. Configure as propriedades da aplicação:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

3. Edite o arquivo `application.properties` com suas configurações:
```properties
# OpenAI API
openai.api.key=sua-chave-aqui

# Outras configurações necessárias
```

4. Crie a pasta para upload de imagens:
```bash
mkdir uploads
```

## Executando o Projeto

### Via Maven:
```bash
mvn spring-boot:run
```

### Via Docker:
```bash
docker-compose up
```

A aplicação estará disponível em: `http://localhost:8080`

## 🧪 Testes
Para executar os testes:
```bash
mvn test
```

## 📁 Estrutura do Projeto
```
src/
├── main/
│   ├── java/
│   │   └── br.com.carshop/
│   │       ├── controllers/
│   │       ├── models/
│   │       ├── services/
│   │       └── CarShopApplication.java
│   └── resources/
│       ├── templates/
│       └── application.properties
└── test/
    └── java/
        └── br.com.carshop/
```

## 📝 API Endpoints
- `GET /cars` - Lista todos os carros
- `GET /cars/{id}` - Detalhes de um carro específico
- `POST /cars/save` - Cadastra um novo carro
- `POST /cars/{id}/update` - Atualiza um carro existente
- `GET /cars/{id}/delete` - Remove um carro
- `POST /cars/suggest-price` - Sugere preço baseado em IA

## Segurança
- As chaves de API são armazenadas em variáveis de ambiente
- Upload de imagens com validação de tipo
- Sanitização de inputs
- Proteção contra CSRF

## Exemplos de Funcionalidades que Podem Ser Implementadas
- [ ] Autenticação de usuários
- [ ] Dashboard administrativo
- [ ] Integração com APIs de pagamento
- [ ] Sistema de reservas
- [ ] Exportação de relatórios
- [ ] Módulo de análise de mercado

## Licença
Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## Autores
- [Leonardo Aquino](https://github.com/leeok4)
