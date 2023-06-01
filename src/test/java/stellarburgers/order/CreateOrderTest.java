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
public class CreateOrderTest {
    String accessToken;
    public Ingredient allIngredient;
    public List<String> ingredients;
    public OrderPojo orderPojo;
    public OrderClient orderClient;
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
    }
    @Test
    @DisplayName("Создать заказ с авторизацией и с ингридиентами")
    public void createOrderWithAuthorization(){
        orderPojo = new OrderPojo(ingredients);
        orderClient = new OrderClient();
        response = orderClient.createOderWithAuthorization(accessToken, orderPojo);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);
    }
    @Test
    @DisplayName("Создать заказ без авторизации")
    public void createOrderWithoutAuthorization(){
        orderPojo = new OrderPojo(ingredients);
        orderClient = new OrderClient();
        response = orderClient.createOderWithoutAuthorization(orderPojo);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(SC_OK);
    }
    @Test
    @DisplayName("Создать заказ без ингридиентов")
    public void createOrderWithoutIngredients(){
        ingredients.clear();
        orderPojo = new OrderPojo(ingredients);
        orderClient = new OrderClient();
        response = orderClient.createOderWithAuthorization(accessToken, orderPojo);
        response.then().assertThat().statusCode(SC_BAD_REQUEST);
    }
    @Test
    @DisplayName("Создать заказ с неверным хешем ингредиентов")
    public void createOrderWithInvalidHashIngredients(){
        ingredients.add("85gugerj");
        orderPojo = new OrderPojo(ingredients);
        orderClient = new OrderClient();
        response = orderClient.createOderWithAuthorization(accessToken, orderPojo);
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
    @After
    public void cleanUp(){
        UserClient.deleteUser(accessToken);
    }
}
