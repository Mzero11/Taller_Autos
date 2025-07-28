package trabajofinal.app.base.domain.controller;


import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;

import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

  public static final String LOGOUT_URL = "/";
  
  @Value("${jwt.auth.secret}")
  private String authSecret;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    super.configure(http);

    setLoginView(http, "/login","/");

    setStatelessAuthentication(http, new SecretKeySpec(Base64.getDecoder().decode(authSecret),JwsAlgorithms.HS256), LOGOUT_URL); //OJO AQUI
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
  web.ignoring()
  .requestMatchers(
    VaadinWebSecurity.getDefaultWebSecurityIgnoreMatcher())
    .requestMatchers(new AntPathRequestMatcher("/images/**"))
    .requestMatchers(antMatchers("static/**"));
    super.configure(web);
  }
}