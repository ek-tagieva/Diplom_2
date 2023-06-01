package stellarburgers.order;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.ingredient.Ingredient;
import stellarburgers.ingredient.IngredientClient;
import stellarburgers.user.UserClient;
import stellarburgers.user.UserRegistrationPojo;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
public class GetOrderTest {
    public OrderClient orderClient;
    public OrderPojo orderPojo;
    public Ingredient allIngredient;
    private String accessToken;
    public List<String> ingredients;
    Response response;
    @Before
    public void setUp(){
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
        IngredientClient ingredientClient = new IngredientClient();
        allIngredient = ingredientClient.getIngredient();
        ingredients = new ArrayList<>();
        ingredients.add(allIngredient.data.get(0).get_id());
        ingredients.add(allIngredient.data.get(1).get_id());
        ingredients.add(allIngredient.data.get(2).get_id());
        orderPojo = new OrderPojo(ingredients);
        orderClient = new OrderClient();
        orderClient.createOderWithAuthorization(accessToken, orderPojo);
    }
    @Test
    @DisplayName("Получить заказ авторизованного пользователя")
    public void getOrderAuthorizedUser(){
        response = orderClient.getOderUserWithAuthorization(accessToken);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);
    }
    @Test
    @DisplayName("Получить заказ не авторизованного пользователя")
    public void getOrderAnAuthorizedUser(){
        response = orderClient.getOderUserWithoutAuthorization();
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(SC_UNAUTHORIZED);
    }
    @After
    public void cleanUp(){
        UserClient.deleteUser(accessToken);
    }
}
