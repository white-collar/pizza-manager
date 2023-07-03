package com.pizza.manager.it

import com.pizza.manager.dto.request.OrderPizzaDto
import com.pizza.manager.enum.Pizza
import com.pizza.manager.enum.Topping
import com.pizza.manager.persist.model.OrderTopping
import com.pizza.manager.persist.repository.ToppingRepository
import io.restassured.RestAssured
import io.restassured.internal.RestAssuredResponseImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIT(@LocalServerPort private val randomPort: String,
                        @Autowired private val toppingRepository: ToppingRepository) {

    val baseUrl: String = "http://localhost:$randomPort/api/orders"

    val toppings = mutableListOf(Topping.BACON, Topping.TOMATOES, Topping.GARLIC);
    val toppings2 = mutableListOf(Topping.BACON, Topping.TOMATOES, Topping.BELL_PEPPERS);
    val toppings3 = mutableListOf(Topping.BACON, Topping.FRESH_BASIL, Topping.JALAPENOS);

    @BeforeEach
    fun init() {
        toppingRepository.deleteAll();
    }

    @Test
    fun `test that pizza order created successfully`() {

        RestAssured.given()
            .contentType("application/json")
            .body(OrderPizzaDto(
                email = "sherlock@holmes.com",
                pizza = Pizza.FOUR_CHEESE
            ))
            .expect()
            .statusCode(201)
            .`when`()
            .post("$baseUrl/pizza")
            .thenReturn()
            .body
    }

    @Test
    fun `test that pizza order with wrong email is not possible`() {

        val body = RestAssured.given()
            .contentType("application/json")
            .body(OrderPizzaDto(
                email = "wrongemail",
                pizza = Pizza.FOUR_CHEESE
            ))
            .expect()
            .statusCode(400)
            .`when`()
            .post("$baseUrl/pizza")
            .thenReturn()
            .body

        val errorMessage = (body as RestAssuredResponseImpl).body.jsonPath().get<String>("errorMessage")

        assertThat("Invalid email").isEqualTo(errorMessage)
    }

    @Test
    fun `test that topping order is successful`() {

        val email = "good@email.com";
        val toppings = mutableListOf(Topping.BACON, Topping.TOMATOES);

        RestAssured.given()
            .contentType("application/json")
            .body(OrderTopping(
                email = email,
                toppings = toppings
            ))
            .expect()
            .statusCode(201)
            .`when`()
            .post("$baseUrl/topping")
            .thenReturn()
            .body

        val toppingsByEmail = toppingRepository.findByEmail(email = email);
        assertThat(email).isEqualTo(toppingsByEmail?.email);
        assertThat(toppingsByEmail?.toppings).hasSameElementsAs(toppings)
    }

    @Test
    fun `test that when toppings are too much that order is impossible`() {

        val email = "good@email.com";
        val toppings = mutableListOf(Topping.BACON, Topping.TOMATOES, Topping.GARLIC, Topping.BELL_PEPPERS,
            Topping.FRESH_BASIL, Topping.JALAPENOS);

        val body = RestAssured.given()
            .contentType("application/json")
            .body(OrderTopping(
                email = email,
                toppings = toppings
            ))
            .expect()
            .statusCode(400)
            .`when`()
            .post("$baseUrl/topping")
            .thenReturn()
            .body

        val errorMessage = (body as RestAssuredResponseImpl).body.jsonPath().get<String>("errorMessage")

        assertThat("Max count of topping achieved").isEqualTo(errorMessage);
    }

    @Test
    fun `test to get list of submitted toppings`(){

        populateToppings();

        val uniqueToppings = (toppings + toppings2 + toppings3)
            .distinct()
            .map { topping -> topping.name };

        val toppingsResponse = RestAssured.given()
            .contentType("application/json")
            .expect()
            .statusCode(200)
            .`when`()
            .get("$baseUrl/toppings")
            .thenReturn()
            .body


        val submittedToppings = ((toppingsResponse as RestAssuredResponseImpl).body.jsonPath()
            .get<List<String>>("$"));

        assertThat(uniqueToppings).hasSameElementsAs(submittedToppings);
    }

    @Test
    fun `test to get count of customers which requested that topping`() {

        populateToppings();

        // BACON

        val baconRequestedCount = RestAssured.given()
            .contentType("application/json")
            .expect()
            .statusCode(200)
            .`when`()
            .get("$baseUrl/count?topping=BACON")
            .thenReturn()
            .body

        val countBaconRequester = ((baconRequestedCount as RestAssuredResponseImpl).body.jsonPath()).getInt("count");

        val  baconRequesters = (toppings + toppings2 + toppings3)
            .count { topping -> topping == Topping.BACON }

        assertThat(countBaconRequester).isEqualTo(baconRequesters);

        // TOMATOES

        val tomatoesRequestedCount = RestAssured.given()
            .contentType("application/json")
            .expect()
            .statusCode(200)
            .`when`()
            .get("$baseUrl/count?topping=TOMATOES")
            .thenReturn()
            .body

        val countTomatoesRequester = ((tomatoesRequestedCount as RestAssuredResponseImpl).body.jsonPath()).getInt("count");

        val  tomatoesRequesters = (toppings + toppings2 + toppings3)
            .count { topping -> topping == Topping.TOMATOES }

        assertThat(countTomatoesRequester).isEqualTo(tomatoesRequesters);

        // FRESH BASIL

        val freshBasilRequestedCount = RestAssured.given()
            .contentType("application/json")
            .expect()
            .statusCode(200)
            .`when`()
            .get("$baseUrl/count?topping=FRESH_BASIL")
            .thenReturn()
            .body

        val countFreshBasilRequester = ((freshBasilRequestedCount as RestAssuredResponseImpl).body.jsonPath()).getInt("count");

        val  freshBasilRequesters = (toppings + toppings2 + toppings3)
            .count { topping -> topping == Topping.FRESH_BASIL }

        assertThat(countFreshBasilRequester).isEqualTo(freshBasilRequesters);
    }

    @Test
    fun `test that only last toppings request of customer is recorded`() {

        val email = "first@email.com";

        RestAssured.given()
            .contentType("application/json")
            .body(OrderTopping(
                email = email,
                toppings = toppings
            ))
            .expect()
            .statusCode(201)
            .`when`()
            .post("$baseUrl/topping")
            .thenReturn()
            .body

        val toppingsOfCustomerFirstOrder = toppingRepository.findByEmail(email);

        assertThat(toppingsOfCustomerFirstOrder?.toppings).hasSameElementsAs(toppings);

        RestAssured.given()
            .contentType("application/json")
            .body(OrderTopping(
                email = email,
                toppings = toppings3
            ))
            .expect()
            .statusCode(201)
            .`when`()
            .post("$baseUrl/topping")
            .thenReturn()
            .body

        val toppingsOfCustomerSecondOrder = toppingRepository.findByEmail(email);

        assertThat(toppingsOfCustomerSecondOrder?.toppings).hasSameElementsAs(toppings3);

    }

    fun populateToppings() {
        val email = "first@email.com";

        RestAssured.given()
            .contentType("application/json")
            .body(OrderTopping(
                email = email,
                toppings = toppings
            ))
            .expect()
            .statusCode(201)
            .`when`()
            .post("$baseUrl/topping")
            .thenReturn()
            .body

        // second person

        val email2 = "second@email.com";


        RestAssured.given()
            .contentType("application/json")
            .body(OrderTopping(
                email = email2,
                toppings = toppings2
            ))
            .expect()
            .statusCode(201)
            .`when`()
            .post("$baseUrl/topping")
            .thenReturn()
            .body

        // third person

        val email3 = "third@email.com";

        RestAssured.given()
            .contentType("application/json")
            .body(OrderTopping(
                email = email3,
                toppings = toppings3
            ))
            .expect()
            .statusCode(201)
            .`when`()
            .post("$baseUrl/topping")
            .thenReturn()
            .body
    }
}