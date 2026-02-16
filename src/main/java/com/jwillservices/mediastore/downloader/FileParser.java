package com.jwillservices.mediastore.downloader;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FileParser {
    public String parseBody(String body) {
        //search urls in the file
        Pattern urlPattern = Pattern.compile("(https?://[\\w./?=&%-]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = urlPattern.matcher(body);

        //with the while loop of matcher.find(), it loops every url that it finds
        List<String> links = new ArrayList<>();
        while (matcher.find()) {
            String url = matcher.group(1);
            Platform platform = Platform.fromUrl(url);

            if (platform.equals(Platform.YOU_TUBE)) {
                //remove anything after the v query param
                //https://www.youtube.com/watch?v=abc123&feature=share&t=50 -> https://www.youtube.com/watch?v=abc123
                int qIndex = url.indexOf('?');
                if (qIndex != -1) {
                    String base = url.substring(0, qIndex);
                    String query = url.substring(qIndex + 1);
                    int ampIndex = query.indexOf('&');
                    if (ampIndex != -1) {
                        query = query.substring(0, ampIndex);
                    }
                    //joins the base url with the first query param
                    links.add(base + "?" + query);
                }
            } else if (!platform.equals(Platform.INVALID_PLATFORM)){
                //remove anything from ? onwards
                //https://instagram.com/reel/XYZ123?utm_source=share&utm_medium=copy_link -> https://instagram.com/reel/XYZ123
                int qIndex = url.indexOf('?');
                if (qIndex != -1) {
                    links.add(url.substring(0, qIndex));
                }
            }
        }
        return String.join("\n", links);
    }
}
