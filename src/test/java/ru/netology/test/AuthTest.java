package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;

class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.browserSize = "1280x720";
        Configuration.headless = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//*[@name=\"login\"]").setValue(registeredUser.getLogin());
        $x("//*[@name=\"password\"]").setValue(registeredUser.getPassword());
        $x("//*[@data-test-id=\"action-login\"]").click();
        $x("//*[contains(@class, \"heading\")]").shouldHave(Condition.ownText("Личный кабинет"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        // TODO: добавить логику теста, в рамках которого будет выполнена попытка входа в личный кабинет с учётными
        //  данными зарегистрированного активного пользователя, для заполнения полей формы используйте
        //  пользователя registeredUser
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//*[@name=\"login\"]").setValue(notRegisteredUser.getLogin());
        $x("//*[@name=\"password\"]").setValue(notRegisteredUser.getPassword());
        $x("//*[@data-test-id=\"action-login\"]").click();
        $x("//*[@class=\"notification__content\"]").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        //  незарегистрированного пользователя, для заполнения полей формы используйте пользователя notRegisteredUser
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//*[@name=\"login\"]").setValue(blockedUser.getLogin());
        $x("//*[@name=\"password\"]").setValue(blockedUser.getPassword());
        $x("//*[@data-test-id=\"action-login\"]").click();
        $x("//*[@class=\"notification__content\"]").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет,
        //  заблокированного пользователя, для заполнения полей формы используйте пользователя blockedUser
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//*[@name=\"login\"]").setValue(wrongLogin);
        $x("//*[@name=\"password\"]").setValue(registeredUser.getPassword());
        $x("//*[@data-test-id=\"action-login\"]").click();
        $x("//*[@class=\"notification__content\"]").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  логином, для заполнения поля формы "Логин" используйте переменную wrongLogin,
        //  "Пароль" - пользователя registeredUser
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//*[@name=\"login\"]").setValue(registeredUser.getLogin());
        $x("//*[@name=\"password\"]").setValue(wrongPassword);
        $x("//*[@data-test-id=\"action-login\"]").click();
        $x("//*[@class=\"notification__content\"]").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  паролем, для заполнения поля формы "Логин" используйте пользователя registeredUser,
        //  "Пароль" - переменную wrongPassword
    }
}
