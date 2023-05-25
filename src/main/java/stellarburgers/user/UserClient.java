package stellarburgers.user;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import stellarburgers.RestClient;

public class UserClient extends RestClient {
    private static final String USER_CREATE = "/api/auth/register";
    private static final String USER_LOGIN = "/api/auth/login";
    public static final String USER_UPDATE = "/api/auth/user";
   // private static final String USER_PATH = "/api/auth/";
    static String accessToken = "";

    @Step("Создание пользователя")
    public static ValidatableResponse createUser(UserRegistrationPojo userRegistrationPojo) {
        return given()
                .spec(RestClient.getBaseSpec())
                .when()
                .body(userRegistrationPojo).log().all()
                .post(USER_CREATE).then().log().all();
    }

    @Step("Авторизация пользователя")

    public static ValidatableResponse loginUser(UserRegistrationPojo userRegistrationPojo) {
        return given()
                .spec(RestClient.getBaseSpec())
                .when()
                .body(userRegistrationPojo).log().all()
                .post(USER_LOGIN).then().log().all();
    }

    @Step("Получение информации о пользователе")
    public static ValidatableResponse getDataUser(String accessToken) {

        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .get(USER_UPDATE)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Step("Изменение данных пользователя")
    public static Response changingUserData(String accessToken, UserRegistrationPojo userRegistrationPojo) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .body(userRegistrationPojo)
                .patch(USER_UPDATE);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser() {
        if (accessToken.equals("")) {
            return given()
                    .spec(getBaseSpec())
                    .auth().oauth2(accessToken)
                    .delete("/api/auth/user");
        }
        return null;

    }

}
