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
public class NotAuthorizedUserTest {
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
        UserClient.getDataUser(accessToken);
    }
    @Test
    @DisplayName("Изменение email без авторизации")
    public void changeEmailWithoutAuthorization(){
        Faker faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .build();
        response = UserClient.changingUserData("",userRegistrationPojo);
        response.then().assertThat().body("success", is(false)).and().statusCode(SC_UNAUTHORIZED);
    }
    @Test
    @DisplayName("Изменение пароля без авторизации")
    public void changePasswordWithoutAuthorization(){
        Faker faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                .password(faker.internet().password())
                .build();
        response = UserClient.changingUserData("",userRegistrationPojo);
        response.then().assertThat().body("success", is(false)).and().statusCode(SC_UNAUTHORIZED);
    }
    @Test
    @DisplayName("Изменение имени без авторизации")
    public void changeNameWithoutAuthorization(){
        userRegistrationPojo = UserRegistrationPojo.builder()
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        response = UserClient.changingUserData("",userRegistrationPojo);
        response.then().assertThat().body("success", is(false)).and().statusCode(SC_UNAUTHORIZED);
    }
    @After
    public void cleanUp(){UserClient.deleteUser(accessToken);}
}

