package com.green.greengram4.common;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;
@Slf4j
@Configuration
public class FirebaseConfiguration {

    @Value("${fcm.certification}")
    private String googleApplicationCredentials;
    @PostConstruct
    // 등록해야 아랫게 호출됩니다.
    // di가 받기전에 실행되면 안되는 것들은 @PostConstruct를 주면 di가 다 아루어지고나서 실행됩니다.
    public void init() {
        try {
            InputStream serviceAccount =
                    new ClassPathResource(googleApplicationCredentials).getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) { // 비어있다면 이니셜을 집어넣습니다.
                log.info("FirebaseApp Initialization Complete !!!");
                FirebaseApp.initializeApp(options);// 객체생성을 합니다.
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
