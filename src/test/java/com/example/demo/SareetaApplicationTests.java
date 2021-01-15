package com.example.demo;

import com.example.demo.controllers.CartControllerTests;
import com.example.demo.controllers.ItemControllerTests;
import com.example.demo.controllers.OrderControllerTests;
import com.example.demo.controllers.UserControllerTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@SpringBootTest
@Suite.SuiteClasses({
		UserControllerTests.class,
		ItemControllerTests.class,
		CartControllerTests.class,
		OrderControllerTests.class
})
public class SareetaApplicationTests {
}
