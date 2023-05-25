package stellarburgers.user;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.Locale;

public class ChangingUserDataTest {
    String accessToken;

    @Test
    @DisplayName("Изменение email с авторизацией")
    public void changeEmailWithAuthorization() {
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
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
        faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .build();
        UserClient.changingUserData(accessToken, userRegistrationPojo);
        response.assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);

    }

    @Test
    @DisplayName("Изменение пароля с авторизацией")
    public void changePasswordWithAuthorization(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
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
        faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                .password(faker.internet().password())
                .build();
        UserClient.changingUserData(accessToken, userRegistrationPojo);
        response.assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);

    }
    @Test
    @DisplayName("Изменение имени с авторизацией")
    public void changeNameWithAuthorization(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
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
        faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        UserClient.changingUserData(accessToken, userRegistrationPojo);
        response.assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);


    }


    @Test
    @DisplayName("Изменение email без авторизации")
    public void changeEmailWithoutAuthorization(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        ValidatableResponse response = UserClient.createUser(userRegistrationPojo);
       accessToken = response.extract().path("accessToken");
       UserClient.getDataUser(accessToken);
        faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .build();
        Response response1 = UserClient.changingUserData("",userRegistrationPojo);
        response1.then().assertThat().body("success", is(false)).and().statusCode(SC_UNAUTHORIZED);

    }

    @Test
    @DisplayName("Изменение пароля без авторизации")
    public void changePasswordWithoutAuthorization(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        ValidatableResponse response = UserClient.createUser(userRegistrationPojo);
        accessToken = response.extract().path("accessToken");
        UserClient.getDataUser(accessToken);
        faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                .password(faker.internet().password())
                .build();
        Response response1 = UserClient.changingUserData("",userRegistrationPojo);
        response1.then().assertThat().body("success", is(false)).and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение имени без авторизации")
    public void changeNameWithoutAuthorization(){
        Faker faker = new Faker();
        UserRegistrationPojo userRegistrationPojo = UserRegistrationPojo.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        ValidatableResponse response = UserClient.createUser(userRegistrationPojo);
        accessToken = response.extract().path("accessToken");
        UserClient.getDataUser(accessToken);
        faker = new Faker();
        userRegistrationPojo = UserRegistrationPojo.builder()
                .name(new Faker(Locale.forLanguageTag("ru")).name().firstName())
                .build();
        Response response1 = UserClient.changingUserData("",userRegistrationPojo);
        response1.then().assertThat().body("success", is(false)).and().statusCode(SC_UNAUTHORIZED);
    }
    @After
    public void cleanUp(){
    UserClient.deleteUser();
    }
}

