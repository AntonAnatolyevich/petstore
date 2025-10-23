package ru.akhramenko.yandex.petstore;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Suite
@SelectPackages("ru.akhramenko.yandex.petstore")
class PetstoreApplicationTests {

}
