package de.krasosu.docker;

import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.PullImageResultCallback;

public class SimpleDockerClient {

  private static final Logger logger = LoggerFactory.getLogger(SimpleDockerClient.class);

  private DockerClient dockerClient;

  private static final int PULL_TIMEOUT = 90;

  private static final DefaultDockerClientConfig DOCKER_CLIENT_CONFIG =
      DefaultDockerClientConfig.createDefaultConfigBuilder().build();

  public SimpleDockerClient() {
    this.dockerClient = DockerClientBuilder.getInstance(DOCKER_CLIENT_CONFIG).build();
  }

  public boolean pull(String image, String tag) throws InterruptedException {

    logger.info("pull image: " + image + ":" + tag);
    return this.dockerClient
        .pullImageCmd(image)
        .withTag(tag)
        .exec(new PullImageResultCallback())
        .awaitCompletion(PULL_TIMEOUT, TimeUnit.SECONDS);
  }

  public InputStream save(String image, String tag) {

    logger.info("save image " + image + ":" + tag);
    return dockerClient.saveImageCmd(image).withTag(tag).exec();
  }

  public void remove(String image, String tag) {

    String repoTag = image + ":" + tag;

    this.dockerClient
        .listImagesCmd()
        .withImageNameFilter(image)
        .exec()
        .stream()
        .filter(filteredImage -> Arrays.asList(filteredImage.getRepoTags()).contains(repoTag))
        .map(filteredImage -> filteredImage.getId())
        .findAny()
        .ifPresent(
            id -> {
              logger.info("remove image: " + id);
              this.dockerClient.removeImageCmd(id).withForce(Boolean.TRUE).exec();
            });
  }
}
