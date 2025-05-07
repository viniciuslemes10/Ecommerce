package com.vinicius.ecommerce.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vinicius.ecommerce.records.vo.TokenVO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:vinicius_lemes}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @Value("${security.jwt.refresh-token.expire-length:86400000}")
    private long refreshTokenValidityInMilliseconds = 86400000; // 1 dia

    @Autowired
    private UserDetailsService userDetailsService;

    private Algorithm algorithm;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    // Método para criar access token e refresh token
    public TokenVO createAccessToken(String email, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        String accessToken = generateToken(email, roles, now, validity);
        String refreshToken = generateRefreshToken(email, roles, now);
        return new TokenVO(email, true, now, validity, accessToken, refreshToken);
    }

    // Método para atualizar o access token a partir do refresh token
    public TokenVO refreshToken(String refreshToken) {
        if (refreshToken.contains("Bearer "))
            refreshToken = refreshToken.substring("Bearer ".length());

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String email = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("authorities").asList(String.class);
        return createAccessToken(email, roles);
    }

    // Gera o access token
    private String generateToken(String email, List<String> roles, Date issuedAt, Date expiresAt) {

        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("authorities", roles)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(email)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
    }

    // Gera o refresh token
    private String generateRefreshToken(String email, List<String> roles, Date issuedAt) {

        Date validityRefreshToken = new Date(issuedAt.getTime() + refreshTokenValidityInMilliseconds);
        return JWT.create()
                .withClaim("authorities", roles)
                .withIssuedAt(issuedAt)
                .withExpiresAt(validityRefreshToken)
                .withSubject(email)
                .sign(algorithm)
                .strip();
    }

    // Verifica se o token JWT é válido
    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = decodeToken(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            throw new RuntimeException("Expired or invalid JWT token!");
        }
    }

    // Método para resolver o token na requisição HTTP
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    // Método para decodificar o token
    private DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    // Método para obter a autenticação a partir do token
    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        // Altere "roles" para "authorities"
        List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        // Certifique-se de que os authorities estejam sendo passados corretamente para as permissões
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
