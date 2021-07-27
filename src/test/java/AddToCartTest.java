import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class AddToCartTest {

    @Test
    void checkAddToCardTestWithAPIAndUI() {


        Map<String, String> authorizationCookieMap =
                given()
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("Email", "aaa@bbb.com")
                        .formParam("Password", "123456")
                        .when()
                        .post("http://demowebshop.tricentis.com/login")
                        .then()
                        .statusCode(302)
                        .extract()
                        .cookies();

        String authorizationCookie = convertWithIteration(authorizationCookieMap);

        String bodyAfterAdd =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("addtocart_31.EnteredQuantity=1")
                        .cookie(authorizationCookie)
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/31/1")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        authorizationCookieMap.forEach((key, value) -> WebDriverRunner.getWebDriver().manage()
                .addCookie(new Cookie(key, value)));

        open("http://demowebshop.tricentis.com");
        $(".account").shouldHave(Condition.text("aaa@bbb.com"));
    }

    public String convertWithIteration(Map<String, String> map) {
        StringBuilder mapAsString = new StringBuilder();
        for (String key : map.keySet()) {
            mapAsString.append(key + "=" + map.get(key) + ", ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length());
        return mapAsString.toString();
    }
}
