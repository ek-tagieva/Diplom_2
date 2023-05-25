package stellarburgers.user;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import java.util.Locale;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class CreateUserTest {

    @Test
    @DisplayName("Создать уникального пользователя")
    public void createUniqueUser(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        ValidatableResponse response = UserClient.createUser(userRegistrationPojo);
        response.assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);

    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    public void createUserWhoIsAlreadyRegistered(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        UserClient.createUser(userRegistrationPojo);
        ValidatableResponse response = UserClient.createUser(userRegistrationPojo);
        response.assertThat().body("success", equalTo(false)).and().statusCode(SC_FORBIDDEN);

    }

    @Test
    @DisplayName("Создать пользователя и не заполнить поле email")
    public void createUserLeaveOutTheEmailField(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .password(faker.internet().password())
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        UserClient.createUser(userRegistrationPojo);
        ValidatableResponse response = UserClient.createUser(userRegistrationPojo);
        response.assertThat().body("success", equalTo(false)).and().statusCode(SC_FORBIDDEN);

    }

    @Test
    @DisplayName("Создать пользователя и не заполнить поле password")
    public void createUserLeaveOutThePasswordField(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        UserClient.createUser(userRegistrationPojo);
        ValidatableResponse response = UserClient.createUser(userRegistrationPojo);
        response.assertThat().body("success", equalTo(false)).and().statusCode(SC_FORBIDDEN);


    }

    @Test
    @DisplayName("Создать пользователя и не заполнить поле name")
    public void createUserLeaveOutTheNameField(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
        UserClient.createUser(userRegistrationPojo);
        ValidatableResponse response = UserClient.createUser(userRegistrationPojo);
        response.assertThat().body("success", equalTo(false)).and().statusCode(SC_FORBIDDEN);

    }
    @After
    public void cleanUp(){
        UserClient.deleteUser();
    }
}
