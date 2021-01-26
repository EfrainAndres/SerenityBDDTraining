package com.testautomation.mesaj;

import com.testautomation.mesaj.builders.FooBuilder;
import com.testautomation.mesaj.facts.NetflixPlans;
import com.testautomation.mesaj.models.users.Datum;
import com.testautomation.mesaj.models.users.Foo;
import com.testautomation.mesaj.models.users.RegisterUserInfo;
import com.testautomation.mesaj.models.users.UpdateUserInfo;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.testautomation.mesaj.questions.GetUsersQuestion;
import com.testautomation.mesaj.questions.ResponseCode;
import com.testautomation.mesaj.tasks.GetUsers;
import com.testautomation.mesaj.tasks.RegisterUser;
import com.testautomation.mesaj.tasks.UpdateUser;

import static net.serenitybdd.rest.SerenityRest.restAssuredThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.contains;

@RunWith(SerenityRunner.class)
public class SerenityBddTests {

    private static final String restApIUrl = "http://localhost:5000/api";

    @Test
    public void initiala(){
        /*Foo foo = new Foo();
        foo.setName("algo");
        foo.setAge(10);
        foo.setLastName("algomas");

        Foo foo1 = new Foo();
        foo1.setName("algo");
        foo1.setAge(10);
        System.out.println(foo.equals(foo1));*/

        Foo foo2 = FooBuilder
                .withName("pepito")
                .build();

        Foo foo3 = FooBuilder
                .withName("pepito")
                .andAge(10)
                .andLastName("feo")
                .build();

        System.out.println(foo2.toString());
        System.out.println(foo3.toString());
    }

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

        /*String registerUserInfo = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\",\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"pistol\"\n" +
                "}";

        RegisterUserInfo registerUserInfo = new RegisterUserInfo
                .builder().email("").build();


        julian.attemptsTo(
                RegisterUser.withInfo(registerUserInfo)
        );*/

        julian.attemptsTo(
                RegisterUser
                        .withName("morpheus")
                        .andEmail("eve.holt@reqres.in")
                        .andPassword("pistol")
                        .andJob("leader")
        );

        julian.should(
                seeThat("El codigo de respuesta", ResponseCode.was(), equalTo(200))
        );
    }

    /*
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
     */

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
