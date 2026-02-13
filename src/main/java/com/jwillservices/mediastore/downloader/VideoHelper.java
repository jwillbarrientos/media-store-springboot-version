package com.jwillservices.mediastore.downloader;

import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.repository.VideoRepository;
import com.jwillservices.mediastore.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

@Slf4j
@Component
public class VideoHelper {
    private final DownloadVideo downloadVideo;
    VideoRepository videoRepository;

    public VideoHelper(DownloadVideo downloadVideo, VideoRepository videoRepository) {
        this.downloadVideo = downloadVideo;
        this.videoRepository = videoRepository;
    }

    private static final long executionDelay = 10_000;

    @Scheduled(fixedDelay = executionDelay)
    public void backgroundDownloader() throws InterruptedException {
        Video video = videoRepository.findFirstByStateIs(Video.State.SUBMITTED);
        if (video == null) {
            return;
        }

        String videoPath = downloadVideo.downloadVideo(video.getLink());
        if (videoPath == null) {
            video.setState(Video.State.ERROR_DOWNLOADING);
            videoRepository.save(video);
            return;
        }

        long durationSecondsVideo = getSeconds(video.getLink());
        video.setName(Paths.get(videoPath).getFileName().toString());
        video.setPath(videoPath);
        video.setDurationSeconds(durationSecondsVideo);
        video.setFileSize(new File(videoPath).length());
        video.setState(Video.State.DOWNLOADED);
        videoRepository.save(video);

        Thread.sleep(Math.max(durationSecondsVideo - executionDelay, 1));
    }

    public static int getSeconds(String link) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Process process = Runtime.getRuntime().exec(
                    os.equals("win") ?
                        new String[]{"downloaders/yt-dlp.exe", "-j", link, "--skip-download", "--print", "%(duration)s"} :
                        new String[]{"downloaders/yt-dlp",     "-j", link, "--skip-download", "--print", "%(duration)s"}
            );

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null && line.matches("^[0-9]+([0-9]\\.[0-9]+)?$")) {
                log.debug("Read line from yt-dlp: {}", line);
                process.destroyForcibly();
                line = line.trim();
                double d = Double.parseDouble(line);
                log.debug("Found {} seconds for video: {}", line, link);
                return (int) Math.round(d);
            }
        } catch (IOException e) {
            log.error("Failed to parse duration: ", e);
        }
        return 0;
    }
}
