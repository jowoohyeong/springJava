package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth

                .requestMatchers("/", "/h2-console/**", "/login*.do","/*").permitAll()
//                .requestMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated()); // 모든 요청은 인증 필요

        http.formLogin(login -> login
            .loginPage("/loginForm.do")
            .loginProcessingUrl("/loginChk.do")
            .usernameParameter("username")
            .passwordParameter("password"));

        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout.do"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/loginForm.do"));

        http.sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/loginForm.do")
                .maxSessionsPreventsLogin(false));
//                .sessionRegistry(sessionRegistry())


        http.exceptionHandling(ex -> ex
                .accessDeniedPage("/loginAccessDenied.do"));
//                .authenticationEntryPoint(authenticationEntryPoint));

        http.headers(headers -> headers
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)      //헤더를 설정하여 클릭재킹 공격을 방지합니다. sameOrigin은 같은 출처에서만 페이지를 iframe으로 표시할 수 있도록 설정
            .cacheControl(HeadersConfigurer.CacheControlConfig::disable)
        );
        return http.build();
    }
}

