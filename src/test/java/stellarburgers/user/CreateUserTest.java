package stellarburgers.user;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Locale;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;
public class CreateUserTest {
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
    @DisplayName("Создать уникального пользователя")
    public void createUniqueUser(){
        response = UserClient.createUser(userRegistrationPojo);
        accessToken = response.extract().path("accessToken");
        response.assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);
    }
    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    public void createUserWhoIsAlreadyRegistered(){
        UserClient.createUser(userRegistrationPojo);
        response = UserClient.createUser(userRegistrationPojo);
        response.assertThat().body("success", equalTo(false)).and().statusCode(SC_FORBIDDEN);
    }
    @Test
    @DisplayName("Создать пользователя и не заполнить поле email")
    public void createUserLeaveOutTheEmailField(){
        userRegistrationPojo.setEmail(null);
        response = UserClient.createUser(userRegistrationPojo);
        response.assertThat().body("success", equalTo(false)).and().statusCode(SC_FORBIDDEN);
    }
    @Test
    @DisplayName("Создать пользователя и не заполнить поле password")
    public void createUserLeaveOutThePasswordField(){
        userRegistrationPojo.setPassword(null);
        response = UserClient.createUser(userRegistrationPojo);
        response.assertThat().body("success", equalTo(false)).and().statusCode(SC_FORBIDDEN);
    }
    @Test
    @DisplayName("Создать пользователя и не заполнить поле name")
    public void createUserLeaveOutTheNameField(){
        userRegistrationPojo.setName(null);
        response = UserClient.createUser(userRegistrationPojo);
        response.assertThat().body("success", equalTo(false)).and().statusCode(SC_FORBIDDEN);
    }
    @After
    public void cleanUp(){UserClient.deleteUser(accessToken);}
}
