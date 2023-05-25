package stellarburgers.order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarburgers.RestClient;
import static io.restassured.RestAssured.given;


public class OrderClient extends RestClient {

    private static final String ORDERS = "/api/orders";

    @Step("Создать заказ с авторизацией")
    public Response createOderWithAuthorization(String accessToken, OrderPojo orderPojo) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .body(orderPojo)
                .post(ORDERS);
    }

    @Step("Создать заказ без авторизации")
    public Response createOderWithoutAuthorization(OrderPojo orderPojo) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(orderPojo)
                .post(ORDERS);
    }
    @Step("Получение заказа конкретного пользователя c авторизацией")
    public Response getOderUserWithAuthorization(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .get(ORDERS);
    }

    @Step("Получение заказа конкретного пользователя без авторизации")
    public Response getOderUserWithoutAuthorization() {
        return given()
                .spec(getBaseSpec())
                .get(ORDERS);
    }

}
