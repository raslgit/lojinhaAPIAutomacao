package modulos.produto;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.ComponentePojo;
import pojo.ProdutoPojo;
import pojo.UsuarioPojo;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*; //conforme informado em https://github.com/rest-assured/rest-assured/wiki/GettingStarted Static Imports do Rest Assured
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

        // Obter o token do usuario admin
        this.token =
            given()
                .contentType(ContentType.JSON) //header...given e when eh na requisicao e o then eh na resposta
                .body(UsuarioDataFactory.criarUsuarioComLoginESenhaIgualA("admin", "admin"))
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
            .body(ProdutoDataFactory.criarProdutoComOValorIgualA(0.00)) //como criei um metodo static consigo fazer isso: chamo a classe.metodo(passo o valor)
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

        // Tentar inserir um produto com o valor 7000.01 e validar que a mensagem de erro foi apresentada e o
        // status code retornado foi 422

        given()
            .header("token", this.token)
            .contentType(ContentType.JSON)
            .body(ProdutoDataFactory.criarProdutoComOValorIgualA(7000.01))
        .when()
            .post("/v2/produtos")
        .then()
            .assertThat() //Valide que:....aqui é Teste
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                .statusCode(422);

    }

}
