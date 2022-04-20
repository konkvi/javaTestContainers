package org.konkvistador.springboot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
    private static int portDev = 8080;
    private static int portProd = 8081;

    @Autowired
    TestRestTemplate restTemplate;
    //надо создать два GenericContainer в полях класса - каждый под свой образ
    public static GenericContainer<?> devapp = new GenericContainer<>("devapp").withExposedPorts(portDev);
    public static GenericContainer<?> prodapp = new GenericContainer<>("prodapp").withExposedPorts(portProd);

    @BeforeAll
    public static void setUp() {
        //в методе setUp() стартуйте контейнеры своих образов
        devapp.start();
        prodapp.start();
    }

    //два юнит теста для проверки корректности того, что вернет приложение
    //используйте объект класса TestRestTemplate. С помощью него сделайте запрос.
    // Для того, чтобы понять на каком порту запущен ваш контейнер, воспользуйтесь методом getMappedPort.
    // Для проверки корректности ответа проверьте с помощью метода assertEquals.
    @Test
    void contextLoadsDevapp() {
        final String expected = "Current profile is dev";
        String url = String.format("http://localhost:%d/profile",devapp.getMappedPort(portDev));
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(expected, forEntity.getBody());
    }
    @Test
    void contextLoadsProdapp() {
        final String expected = "Current profile is production";
        String url = String.format("http://localhost:%d/profile", prodapp.getMappedPort(portProd));
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(expected, forEntity.getBody());
    }
}
