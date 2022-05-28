package modulos.produto;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.Usuariopojo;

import java.security.KeyStore;

import static io.restassured.RestAssured.*; //conforme informado em https://github.com/rest-assured/rest-assured/wiki/GettingStarted Static Imports do Rest Assured
import static io.restassured.matcher.RestAssuredMatchers.*; // O import static elimina a necessidade de ficar escrevendo RestAssured."nome do atributo...como no caso do RestAssure.baseURI
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de API Rest do modulo de Produto")
public class ProdutoTest {
    private String token; //criando um atributo da classe...isso ira subistituir a variavel token que estava dentro dos metodos com "this.token"

    @BeforeEach
    public void beforeEach(){
        // Configurando os dados da API Rest da Lojinha
        baseURI = "http://165.227.93.41";
        //port = 8080; // Porta onde a aplicacao esta rodando...dependendo da empresa
        basePath = "/lojinha";

        Usuariopojo usuario = new Usuariopojo();
        usuario.setUsuarioLogin("admin");
        usuario.setUsuarioSenha("admin");


        // Obter o token do usuario admin
        this.token =
            given()
                .contentType(ContentType.JSON) //header...given e when eh na requisicao e o then eh na resposta
                .body(usuario)
            .when() // Qual metodo quero usar
                .post("/v2/login")
            .then()
                .extract()
                    .path("data.token"); // o que se coloca dentro do path eh a estrutura hierarquica do que tem na resposta... Extract é extracao de dados

    }


    @Test
    @DisplayName("Validar que o valor do produto igual a 0.00 não eh permitido")
    public void testValidarLimitesZeradoProibidoValorProduto(){

    // Tentar inserir um produto com o valor 0.00 e validar que a mensagem de erro foi apresentada e o
    // status code retornado foi 422

        given()
            .header("token", this.token)
            .contentType(ContentType.JSON)
            .body("{\n" +
                    "  \"produtoNome\": \"PlayStation 11\",\n" +
                    "  \"produtoValor\": 0.00,\n" +
                    "  \"produtoCores\": [\n" +
                    "    \"verde\", \"rosa\"\n" +
                    "  ],\n" +
                    "  \"produtoUrlMock\": \"\",\n" +
                    "  \"componentes\": [\n" +
                    "    {\n" +
                    "      \"componenteNome\": \"Controle\",\n" +
                    "      \"componenteQuantidade\": 2\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"componenteNome\": \"Jogo de Aventura\",\n" +
                    "      \"componenteQuantidade\": 1\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}")
        .when()
            .post("/v2/produtos")
        .then()
            .assertThat() //Valide que:....aqui é Teste
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                .statusCode(422);

    }


    @Test
    @DisplayName("Validar que o valor do produto igual a 7000.01 não eh permitido")
    public void testValidarLimitesMaiorSeteMilProibidoValorProduto(){

        // Tentar inserir um produto com o valor 7000.00 e validar que a mensagem de erro foi apresentada e o
        // status code retornado foi 422

        given()
            .header("token", this.token)
            .contentType(ContentType.JSON)
            .body("{\n" +
                    "  \"produtoNome\": \"PlayStation 11\",\n" +
                    "  \"produtoValor\": 7000.01,\n" +
                    "  \"produtoCores\": [\n" +
                    "    \"verde\", \"rosa\"\n" +
                    "  ],\n" +
                    "  \"produtoUrlMock\": \"\",\n" +
                    "  \"componentes\": [\n" +
                    "    {\n" +
                    "      \"componenteNome\": \"Controle\",\n" +
                    "      \"componenteQuantidade\": 2\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"componenteNome\": \"Jogo de Aventura\",\n" +
                    "      \"componenteQuantidade\": 1\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}")
        .when()
            .post("/v2/produtos")
        .then()
            .assertThat() //Valide que:....aqui é Teste
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                .statusCode(422);

    }

}
