package com.jwillservices.mediastore.downloader;

import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

@Slf4j
@Component
public class VideoHelper {
    private final VideoService videoService;
    private final DownloadVideo downloadVideo;

    public VideoHelper(VideoService videoService, DownloadVideo downloadVideo) {
        this.videoService = videoService;
        this.downloadVideo = downloadVideo;
    }

    public Runnable backgroundDownloader() {
        return () -> {
            while(true) {
                Video video = videoService.getNextVideoToDownload();
                if (video != null) {
                    String videoPath = downloadVideo.downloadVideo(video.getLink());
                    long durationSecondsVideo = getSeconds(video.getLink());
                    if (videoPath == null) {
                        videoService.updateVideoErr(video);
                    } else {
                        videoService.updateVideo(
                                Paths.get(videoPath).getFileName().toString(),
                                videoPath,
                                durationSecondsVideo,
                                new File(videoPath).length()
                        );
                    }
                    try {
                        log.info("Waiting {} time to download next video", durationSecondsVideo);
                        Thread.sleep(durationSecondsVideo);
                    } catch (InterruptedException e) {
                        log.error("Error waiting the next video to download: ", e);
                    }
                    //break;
                } else {
                    try {
                        log.info("There are no videos to download, sleeping 5 seconds");
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        log.error("Error trying to sleep: ", e);
                    }
                }
            }
        };
    }

    public static int getSeconds(String link) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Process process;
            if (os.equals("win")) {
                process = Runtime.getRuntime().exec(new String[]{"./downloaders/yt-dlp.exe", "-j", link, "--skip-download", "--print", "%(duration)s"});
            } else {
                process = Runtime.getRuntime().exec(new String[]{"downloaders/yt-dlp", "-j", link, "--skip-download", "--print", "%(duration)s"});
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null && line.matches("^[0-9]+([0-9]\\.[0-9]+)?$")) {
                process.destroyForcibly();
                line = line.trim();
                double d = Double.parseDouble(line);
                return (int) Math.round(d);
            }
        } catch (IOException e) {
            log.error("Failed to parse duration: ", e);
        }
        return 0;
    }
}
