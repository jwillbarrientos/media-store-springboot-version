package com.jwillservices.mediastore;

import com.jwillservices.mediastore.downloader.VideoHelper;
import com.jwillservices.mediastore.service.CustomUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication()
@EnableScheduling
@EnableTransactionManagement
@EnableWebSecurity
//@EntityScan(basePackages="com.jwillservices.mediastore.entity")  //SpringBoot ya hace esto automaticamente
//@EnableJpaRepositories(basePackages="com.jwillservices.mediastore.repository")    //SpringBoot ya hace esto automaticamente
public class MediaStore extends WebSecurityConfigurerAdapter {
    static ConfigurableApplicationContext run;
    private final CustomUserDetailsService customUserDetailsService;

    public MediaStore(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    public static void main(String[] args) {
        run = SpringApplication.run(MediaStore.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // Public endpoints
                .antMatchers("/", "/public/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                // Protected API endpoints
                .antMatchers(HttpMethod.POST, "/api/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/api/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                // Protected MVC endpoints
                .antMatchers("/welcome", "/reels").authenticated()
                .antMatchers(HttpMethod.GET, "/api/videoactions/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }
}
