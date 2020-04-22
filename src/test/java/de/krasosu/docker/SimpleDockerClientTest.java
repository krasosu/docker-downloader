package de.krasosu.docker;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestMethodOrder(OrderAnnotation.class)
class SimpleDockerClientTest {

  private static final Logger logger = LoggerFactory.getLogger(SimpleDockerClientTest.class);

  @Test
  @Order(1)
  public void clientTest() {

    SimpleDockerClient simpleDockerClient = new SimpleDockerClient();

    assertNotNull(simpleDockerClient);
  }

  @Test
  @Order(2)
  public void pullTest() throws InterruptedException {

    boolean pull = new SimpleDockerClient().pull("hello-world", "latest");

    assertTrue(pull);
  }

  @Test
  @Order(3)
  public void removeTest() {

    new SimpleDockerClient().remove("hello-world", "latest");
  }
}
