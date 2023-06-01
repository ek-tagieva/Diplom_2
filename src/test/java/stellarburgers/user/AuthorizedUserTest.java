package stellarburgers.user;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.Locale;
public class AuthorizedUserTest {
    String accessToken;
    UserRegistrationPojo userRegistrationPojo;
    Response response;
    @Before
    public void setUp(){
        Faker faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                    .email(faker.internet().emailAddress())
                    .password(faker.internet().password())
                    .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                    .build();
        ValidatableResponse response = UserClient.createUser(userRegistrationPojo);
        accessToken = response.extract().path("accessToken");
        ValidatableResponse loginResponse = UserClient.loginUser(userRegistrationPojo);
        loginResponse.assertThat().body("success", is(true))
                    .assertThat().body("accessToken", notNullValue())
                    .and()
                    .statusCode(SC_OK);
        UserClient.getDataUser(accessToken);
    }
    @Test
    @DisplayName("Изменение email с авторизацией")
    public void changeEmailWithAuthorization(){
        Faker faker = new Faker();
        UserRegistrationPojo.builder()
                    .email(faker.internet().emailAddress())
                    .build();
        response = UserClient.changingUserData(accessToken, userRegistrationPojo);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);
    }
    @Test
    @DisplayName("Изменение пароля с авторизацией")
    public void changePasswordWithAuthorization(){
        Faker faker = new Faker();
        UserRegistrationPojo.builder()
                    .password(faker.internet().password())
                    .build();
        response = UserClient.changingUserData(accessToken, userRegistrationPojo);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);
    }
    @Test
    @DisplayName("Изменение имени с авторизацией")
    public void changeNameWithAuthorization(){
        UserRegistrationPojo.builder()
                    .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                    .build();
        response = UserClient.changingUserData(accessToken, userRegistrationPojo);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);
    }
    @After
    public void cleanUp(){
        UserClient.deleteUser(accessToken);
    }
}




