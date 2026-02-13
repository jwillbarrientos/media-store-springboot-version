package com.jwillservices.mediastore.downloader;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FileParser {
    public String parseBody(String body) {
        Pattern urlPattern = Pattern.compile("(https?://[\\w./?=&%-]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = urlPattern.matcher(body);
        List<String> links = new ArrayList<>();
        while (matcher.find()) {
            String url = matcher.group(1);
            Platform platform = Platform.fromUrl(url);
            if (platform.equals(Platform.YOU_TUBE)) {
                // todo explicar que se hace aca con ejemplos
                int qIndex = url.indexOf('?');
                if (qIndex != -1) {
                    String base = url.substring(0, qIndex);
                    String query = url.substring(qIndex + 1);
                    int ampIndex = query.indexOf('&');
                    if (ampIndex != -1) {
                        query = query.substring(0, ampIndex);
                    }
                    links.add(base + "?" + query);
                }
            } else if (!platform.equals(Platform.INVALID_PLATFORM)){
                // eliminar todo lo viene despues del ?
                int qIndex = url.indexOf('?');
                if (qIndex != -1) {
                    links.add(url.substring(0, qIndex));
                }
            }
        }
        return String.join("\n", links);
    }
}
