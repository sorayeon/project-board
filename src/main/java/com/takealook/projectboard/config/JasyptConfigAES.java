package com.takealook.projectboard.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 민감한 PASSWORD, SECRET KEY 등 설정에 노출되면 안되는 정보 암호화 설정
 * 부트실행시 암호화 키 주입필요(VM option) -Djasypt.encryptor.password=암호화키
 * 1.gradle
 * implementation 'com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4'
 * implementation 'org.bouncycastle:bcprov-jdk15on:1.70'
 * 2.application.yml
 * spring:
 *   datasource:
 *     url: ENC(j/dMi1B25PwM1qxtt...==)
 *     username: ENC(HRDWRmd...=)
 *     password: ENC(I4HXNaq...=)
 */
@Configuration
@EnableEncryptableProperties
public class JasyptConfigAES {
    @Value("${jasypt.encryptor.password}")
    private String password;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPoolSize(2);
        encryptor.setPassword(password);
        encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
        return encryptor;
    }

}
