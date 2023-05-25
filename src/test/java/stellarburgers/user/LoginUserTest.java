package stellarburgers.user;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import java.util.Locale;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class LoginUserTest {

    @Test
    @DisplayName("Авторизация пользователя с существующим логином и паролем")
    public void userAuthorizationWithLoginAndPassword(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
         UserClient.createUser(userRegistrationPojo);
         UserClient.loginUser(userRegistrationPojo)
                .assertThat().body("success", is(true))
                .assertThat().body("accessToken", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином и паролем")
    public void userAuthorizationWithInvalidLogin(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
        UserClient.loginUser(userRegistrationPojo)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @After
    public void cleanUp(){
        UserClient.deleteUser();
    }
}
