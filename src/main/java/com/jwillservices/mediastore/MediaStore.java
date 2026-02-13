package com.jwillservices.mediastore;

import com.jwillservices.mediastore.downloader.VideoHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
//@EntityScan(basePackages="com.jwillservices.mediastore.entity")  //SpringBoot ya hace esto automaticamente
//@EnableJpaRepositories(basePackages="com.jwillservices.mediastore.repository")    //SpringBoot ya hace esto automaticamente
public class MediaStore {
    public static void main(String[] args) {
        SpringApplication.run(MediaStore.class, args);
    }
}
