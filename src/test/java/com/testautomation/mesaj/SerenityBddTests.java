package com.testautomation.mesaj;

import com.testautomation.mesaj.abilities.InteractWithDb;
import com.testautomation.mesaj.builders.FooBuilder;
import com.testautomation.mesaj.database.DatabaseConnectionInfo;
import com.testautomation.mesaj.database.DatabaseType;
import com.testautomation.mesaj.database.entity.Example;
import com.testautomation.mesaj.database.entity.ExampleOracle;
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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Test
    public void dataBaseConnectionTest() {

        DatabaseConnectionInfo connectionInfo = DatabaseConnectionInfo
                .builder()
                .username("root")
                .databaseType(DatabaseType.MYSQL)
                .url("jdbc:mysql://localhost/test_automation")
                .password("my-secret-pw")
                .entityNames(Stream.of(
                        Example.class)
                        .map(Class::getName)
                        .collect(Collectors.toList()))
                .build();


        Actor julian = Actor.named("julian");
        julian.can(InteractWithDb.using(connectionInfo));

        EntityManager entityManager = InteractWithDb.as(julian).getManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Example> criteriaQuery = criteriaBuilder.createQuery(Example.class);
        criteriaQuery.from(Example.class);
        List<Example> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        System.out.println("Register count: " + resultList.size());
        resultList.forEach(System.out::println);
        entityManager.close();
    }

    @Test
    public void dataBaseConnectionTestDeleteAll() {

        DatabaseConnectionInfo connectionInfo = DatabaseConnectionInfo
                .builder()
                .username("root")
                .databaseType(DatabaseType.MYSQL)
                .url("jdbc:mysql://localhost/test_automation")
                .password("my-secret-pw")
                .entityNames(Stream.of(
                        Example.class)
                        .map(Class::getName)
                        .collect(Collectors.toList()))
                .build();


        Actor julian = Actor.named("julian");
        julian.can(InteractWithDb.using(connectionInfo));

        EntityManager entityManager = InteractWithDb.as(julian).getManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Example> criteriaDelete = criteriaBuilder.createCriteriaDelete(Example.class);
        criteriaDelete.from(Example.class);
        //The equivalent JPQL: DELETE FROM Employee e
        entityManager.getTransaction().begin();
        int rowsDeleted = entityManager.createQuery(criteriaDelete).executeUpdate();
        System.out.println("entities deleted: " + rowsDeleted);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void dataBaseConnectionTestDeleteWithWhere() {

        DatabaseConnectionInfo connectionInfo = DatabaseConnectionInfo
                .builder()
                .username("root")
                .databaseType(DatabaseType.MYSQL)
                .url("jdbc:mysql://localhost/test_automation")
                .password("my-secret-pw")
                .entityNames(Stream.of(
                        Example.class)
                        .map(Class::getName)
                        .collect(Collectors.toList()))
                .build();


        Actor julian = Actor.named("julian");
        julian.can(InteractWithDb.using(connectionInfo));

        EntityManager entityManager = InteractWithDb.as(julian).getManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Example> criteriaDelete = criteriaBuilder.createCriteriaDelete(Example.class);

        Root<Example> root = criteriaDelete.from(Example.class);
        criteriaDelete.where(criteriaBuilder.equal(root.get("name"), "Lunita"));
        //The equivalent JPQL: DELETE FROM Employee e WHERE e.name = 'Mike'
        entityManager.getTransaction().begin();
        int rowsDeleted = entityManager.createQuery(criteriaDelete).executeUpdate();
        System.out.println("entities deleted: " + rowsDeleted);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void dataBaseConnectionTestOracle() {

        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@10.133.6.10:1528/WSRRQA", "Consulta", "M06a*Th17$pcqe")) {

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dataBaseConnectionTestOracle2() {

        DatabaseConnectionInfo connectionInfo = DatabaseConnectionInfo
                .builder()
                .username("Consulta")
                .databaseType(DatabaseType.ORACLE)
                .url("jdbc:oracle:thin:@10.133.6.10:1528/WSRRQA")
                .password("M06a*Th17$pcqe")
                .entityNames(Stream.of(
                        ExampleOracle.class)
                        .map(Class::getName)
                        .collect(Collectors.toList()))
                .build();


        Actor julian = Actor.named("julian");
        julian.can(InteractWithDb.using(connectionInfo));

        EntityManager entityManager = InteractWithDb.as(julian).getManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<ExampleOracle> criteriaQuery = criteriaBuilder.createQuery(ExampleOracle.class);
        Root<ExampleOracle> postRoot = criteriaQuery.from(ExampleOracle.class);

        Path<Date> dateEntryPath = postRoot.get("MESSAGE_DATE");

        criteriaQuery.select(postRoot).where(
                criteriaBuilder.equal(postRoot.get("REF1"), "500097"));
        TypedQuery<ExampleOracle> qry = entityManager.createQuery(criteriaQuery);

        List<ExampleOracle> resultList = qry.getResultList();
        System.out.println("Register count: " + resultList.size());
        resultList.forEach(System.out::println);
        entityManager.close();
    }

}
