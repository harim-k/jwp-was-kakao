package webserver.controller;

import utils.FileIoUtils;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class FileController implements Controller {
    private final String INDEX = "/index.html";
    private final String TEMPLATE_BASE_PATH = "./templates";
    private final String STATIC_BASE_PATH = "./static";

    public String getPath() {
        return null;
    }

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {


        String path = httpRequest.getPath();
        if(path.equals("/")) {
            path = INDEX;
        }

        String body = null;
        try {
            body = FileIoUtils.loadFileStringFromClasspath(addBasePath(path));
        }   catch (IOException e) {
        } catch (URISyntaxException e) {
        }

        httpResponse.addHeader("Content-Type", findContentType(path) + "; charset=utf-8");
        httpResponse.addHeader("Content-Length", String.valueOf(body.length()));
        httpResponse.addHeader("Content-Location", path);
        httpResponse.addBody(body);
        httpResponse.forward(path);
    }

    private String findContentType(String path) {
        if (path.startsWith("/css")) {
            return "text/css";
        }
//        if (path.startsWith("/fonts")
//                || path.startsWith("/images")
//                || path.startsWith("/js")) {
//
//        }
        return "text/html";
    }

    private String addBasePath(String path) {
        if (path.startsWith("/css")
                || path.startsWith("/fonts")
                || path.startsWith("/images")
                || path.startsWith("/js")) {
            return STATIC_BASE_PATH + path;
        }
        return TEMPLATE_BASE_PATH + path;
    }
}
