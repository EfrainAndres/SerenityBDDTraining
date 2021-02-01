package com.testautomation.mesaj.stepdefinitions;

import com.testautomation.mesaj.questions.ResponseCode;
import com.testautomation.mesaj.tasks.RegisterUser;

import io.cucumber.java.en.*;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class registerUserStepDefiniton {

    private final String restApIUrl = "http://localhost:5000/api";
    Actor efrain;

    @Given("Efrain es un cliente que quiere poder administrar sus productos bancarios")
    public void efrainEsUnClienteQueQuierePoderAdministrarSusProductosBancarios() {
        efrain = Actor.named("Julian the trainer")
                .whoCan(CallAnApi.at(restApIUrl));
    }

    @When("el envia la informacion requerida para el registro")
    public void elEnviaLaInformacionRequeridaParaElRegistro() {
        /*String registerUserInfo = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\",\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"pistol\"\n" +
                "}";

        efrain.attemptsTo(
                RegisterUser.withInfo(registerUserInfo)
        );*/

        efrain.attemptsTo(
                RegisterUser
                        .withName("morpheus")
                        .andEmail("eve.holt@reqres.in")
                        .andPassword("pistol")
                        .andJob("leader")
        );
    }

    @Then("el debe obtener una cuenta virtual para poder ingresar cuando lo requiera")
    public void elDebeObtenerUnaCuentaVirtualParaPoderIngresarCuandoLoRequiera() {
        efrain.should(
                seeThat("El codigo de respuesta", ResponseCode.was(), equalTo(200))
        );
    }

}
