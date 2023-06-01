package stellarburgers.user;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Locale;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
public class LoginUserTest {
    String accessToken;
    UserRegistrationPojo userRegistrationPojo;
    ValidatableResponse response;
    @Before
    public void setUp(){
        Faker faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
    }
    @Test
    @DisplayName("Авторизация пользователя с существующим логином и паролем")
    public void userAuthorizationWithLoginAndPassword(){
        response = UserClient.createUser(userRegistrationPojo);
        accessToken = response.extract().path("accessToken");
        UserClient.loginUser(userRegistrationPojo)
                .assertThat().body("success", is(true))
                .assertThat().body("accessToken", notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Авторизация пользователя с неверным логином и паролем")
    public void userAuthorizationWithInvalidLogin(){
        UserClient.loginUser(userRegistrationPojo)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
    @After
    public void cleanUp(){
        UserClient.deleteUser(accessToken);
    }
}
