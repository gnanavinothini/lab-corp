import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

@Test
public class EmployeeTest {

    private String baseURI = "https://6143a99bc5b553001717d06a.mockapi.io/testapi/v1";
    private JSONObject userObject = new JSONObject("{\"createdAt\":1631824933,\"employee_firstname\":\"employee_firstname 16\",\"employee_lastname\":\"employee_lastname 16\",\"employee_phonenumbe\":\"436-788-7030\",\"ademployee_emaildress\":\"ademployee_emaildress 16\",\"citemployee_addressy\":\"citemployee_addressy 16\",\"stateemployee_dev_level\":\"stateemployee_dev_level 16\",\"employee_gender\":\"employee_gender 16\",\"employee_hire_date\":\"2009-02-12T20:19:18.873Z\",\"employee_onleave\":false,\"tech_stack\":[],\"project\":[],\"id\":\"16\"}");

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = baseURI;
    }

    private String getCreateRequestBody() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("createdAt", "1631825833");
        requestParams.put("employee_firstname", "FirstTestData12345");
        requestParams.put("employee_lastname", "LastTestData12345");
        requestParams.put("employee_phonenumbe", "264-783-9453");
        requestParams.put("ademployee_emaildress", "ademployee_emaildress 1");
        requestParams.put("citemployee_addressy", "citemployee_addressy 1");
        requestParams.put("stateemployee_dev_level", "stateemployee_dev_level 1");
        requestParams.put("employee_hire_date", "2025-10-31T16:35:45.426Z");
        requestParams.put("employee_onleave", "true");
        requestParams.put("tech_stack", "[]");
        requestParams.put("project", "[]");

        userObject = requestParams;

        return requestParams.toString();
    }

    @Test(priority = 1, enabled = false)
    public void createUserTest(){
        Response response = RestAssured.given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(getCreateRequestBody())
                .post("/Users")
                .then()
                .extract()
                .response();

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test(priority = 2)
    public void validateUserDetailsTest() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/Users")
                .then()
                .extract()
                .response();

        Assert.assertEquals(200, response.getStatusCode());
        List<Map> users = response.jsonPath().getList("$");
        Assert.assertEquals(100, users.size());
        boolean found = false;
        for (Map u : users) {
            if (u.get("employee_firstname").toString().equalsIgnoreCase(userObject.get("employee_firstname").toString())
            && u.get("employee_lastname").toString().equalsIgnoreCase(userObject.get("employee_lastname").toString())) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found);
    }
}
