package examples;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Chapter4Test {

    private static RequestSpecification requestSpec;

    @BeforeClass
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("http://api.zippopotam.us").
                build();
    }

    @Test
    public void requestUsZipCode90210_checkPlaceNameInResponseBody_expectBeverlyHills_withRequestSpec() {

        given().
                spec(requestSpec).
                when().
                get("us/90210").
                then().
                assertThat().
                statusCode(200);
    }

    private static ResponseSpecification responseSpec;

    @BeforeClass
    public static void createResponseSpecification() { //criando método que vaida o status code e se o responde é formato json

        responseSpec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                build();
    }

    @Test
    public void requestUsZipCode90210_checkPlaceNameInResponseBody_expectBeverlyHills_withResponseSpec() {

        given().
                spec(requestSpec).
                when().
                get("http://zippopotam.us/us/90210").
                then().
                spec(responseSpec).  //usando a validação criada acima do beforeclass
                and().
                assertThat().
                body("places[0].'place name'", equalTo("Beverly Hills"));
    }

    @Test //IMPORTANTE
    public void requestUsZipCode90210_extractPlaceNameFromResponseBody_assertEqualToBeverlyHills() {
//EXTRAINDO O RESPONSE PARA REUSO
        String placeName =

                given().
                        log().all().
                        spec(requestSpec).
                        when().
                        log().all().
                        get("http://zippopotam.us/us/90210").
                        then().
                        log().all().
                        extract().
                        path("places[0].'place name'")
                ;

        Assert.assertEquals("Beverly Hills", placeName); //usando a string
    }
}
