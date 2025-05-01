package com.vinicius.ecommerce.config;

import com.vinicius.ecommerce.properties.EncoderProperties;
import com.vinicius.ecommerce.token.JwtTokenFilter;
import com.vinicius.ecommerce.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder(EncoderProperties encoderProperties) {
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                encoderProperties.getSecret(),
                8,
                encoderProperties.getIterations(),
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );

        encoders.put("pbkdf2", pbkdf2Encoder);

        DelegatingPasswordEncoder passwordEncoder =
                new DelegatingPasswordEncoder(encoderProperties.getId(), encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return pbkdf2Encoder;
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita a autenticação básica HTTP (não necessária com JWT)
                .httpBasic(AbstractHttpConfigurer::disable)

                // Desabilita CSRF (não é necessário com JWT, pois não utilizamos sessões)
                .csrf(AbstractHttpConfigurer::disable)

                // Adiciona o filtro JWT antes do filtro padrão de autenticação
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)

                // Configuração de gerenciamento de sessões: sem sessão (stateless) para não usar sessões no servidor
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuração de permissões para endpoints específicos
                .authorizeHttpRequests(authorize -> authorize
                        // Permite o acesso a certas rotas sem autenticação
                        .requestMatchers("/api/v1/auth/**").permitAll())
                .csrf(c -> {})
                .build();
    }

    @Bean
    AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
