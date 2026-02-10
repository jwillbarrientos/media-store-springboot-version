package com.jwillservices.mediastore;

import com.jwillservices.mediastore.controller.ClientController;
import com.jwillservices.mediastore.downloader.VideoHelper;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableTransactionManagement
//@EntityScan(basePackages="com.jwillservices.mediastore.entity")  //SpringBoot ya hace esto automaticamente
//@EnableJpaRepositories(basePackages="com.jwillservices.mediastore.repository")    //SpringBoot ya hace esto automaticamente
public class MediaStore {
    public static void main(String[] args) {
        SpringApplication.run(MediaStore.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(@Autowired VideoHelper videoHelper) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(videoHelper.backgroundDownloader());
        return args -> {
            //repository.save(new Client("Jona@gmail.com", "en la db"));
        };
    }

}
