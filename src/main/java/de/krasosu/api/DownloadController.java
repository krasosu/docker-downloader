package de.krasosu.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.krasosu.docker.SimpleDockerClient;

@RestController
public class DownloadController {

  private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

  @CrossOrigin
  @RequestMapping("/download")
  public void downloadImage(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(name = "image", required = false) String image,
      @RequestParam(name = "tag", required = false, defaultValue = "latest") String tag)
      throws InterruptedException {

    logger.info("request image  " + image + ":" + tag);

    SimpleDockerClient simpleDockerClient = new SimpleDockerClient();

    if (simpleDockerClient.pull(image, tag)) {
      response.setContentType("application/gzip");
      response.addHeader("Content-Disposition", "attachment; filename=" + image + ".tar.gz");

      try {
        IOUtils.copy(simpleDockerClient.save(image, tag), response.getOutputStream());
        response.getOutputStream().flush();
        simpleDockerClient.remove(image, tag);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}
