import facts.NetflixPlans;
import models.users.Datum;
import models.users.RegisterUserInfo;
import models.users.UpdateUserInfo;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import questions.GetUsersQuestion;
import questions.ResponseCode;
import tasks.GetUsers;
import tasks.RegisterUser;
import tasks.UpdateUser;

import static net.serenitybdd.rest.SerenityRest.restAssuredThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.isEmptyString;

@RunWith(SerenityRunner.class)
public class SerenityBddTests {

    private static final String restApIUrl = "http://localhost:5000/api";

    @Test
    public void initialTest(){

        Actor julian = Actor.named("Julian the trainer")
                .whoCan(CallAnApi.at(restApIUrl));

        julian.attemptsTo(
            GetUsers.fromPage(1)
        );

        //assertThat(SerenityRest.lastResponse().statusCode()).isEqualTo(200);
        julian.should(
                seeThat("el codigo de respuesta", ResponseCode.was(), equalTo(200))
        );

        Datum user = new GetUsersQuestion().answeredBy(julian)
                .getData().stream().filter(x -> x.getId() == 1).findFirst().orElse(null);

        julian.should(
                seeThat("usuario no es nulo", act ->  user, notNullValue())
        );

        julian.should(
                seeThat("El email del usuario", act ->  user.getEmail(), equalTo("george.bluth@reqres.in")),
                seeThat("El avatar del usuario", act ->  user.getAvatar(), equalTo("https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg"))
        );
    }

    @Test
    public void registerUserTest(){

        Actor julian = Actor.named("Julian the trainer")
                .whoCan(CallAnApi.at(restApIUrl));

        String registerUserInfo = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\",\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"pistol\"\n" +
                "}";

        julian.attemptsTo(
                RegisterUser.withInfo(registerUserInfo)
        );

        julian.should(
                seeThat("El codigo de respuesta", ResponseCode.was(), equalTo(200))
        );
    }

    @Test
    public void registerUserTest2(){

        Actor julian = Actor.named("Julian the trainer")
                .whoCan(CallAnApi.at(restApIUrl));

        RegisterUserInfo registerUserInfo = new RegisterUserInfo();

        registerUserInfo.setName("morpheus");
        registerUserInfo.setJob("leader");
        registerUserInfo.setEmail("eve.holt@reqres.in");
        registerUserInfo.setPassword("pistol");

        julian.attemptsTo(
                RegisterUser.withInfo(registerUserInfo)
        );

        julian.should(
                seeThat("El codigo de respuesta", new ResponseCode(), equalTo(200))
        );
    }

    @Test
    public void UpdateUserTest(){

        Actor julian = Actor.named("Julian the trainer")
                .whoCan(CallAnApi.at(restApIUrl));

        UpdateUserInfo updateUserInfo = new UpdateUserInfo();

        updateUserInfo.setName("morpheus");
        updateUserInfo.setJob("leader");

        julian.attemptsTo(
                UpdateUser.withInfo(updateUserInfo)
        );

        julian.should(
                seeThat("El codigo de respuesta", ResponseCode.was(), equalTo(200))
        );

        restAssuredThat(lastResponse -> lastResponse.body(containsString("updatedAt")));

    }

    @Test
    public void factTest(){

        Actor julian = Actor.named("Julian the trainer")
                .whoCan(CallAnApi.at(restApIUrl));

        julian.has(NetflixPlans.toViewsSeries());
    }

}
