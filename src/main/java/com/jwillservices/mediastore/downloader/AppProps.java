package com.jwillservices.mediastore.downloader;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppProps {
    @Value("${ytDl}")
    private String ytDl;

    @Value("${videoTemporalPath}")
    @Getter
    private String videoTemporalPath;

    @Value("${videoOutputPath}")
    @Getter
    private String videoOutputPath;

    public String getYtDl() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.equals("win"))
            return ytDl + ".exe";
        return ytDl;
    }
}
