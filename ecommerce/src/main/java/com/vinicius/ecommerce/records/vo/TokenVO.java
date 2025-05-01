package com.vinicius.ecommerce.records.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


public class TokenVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private Boolean authentication;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;

    public TokenVO(){}

    public TokenVO(
            String userName,
            Boolean authentication,
            Date created,
            Date expiration,
            String accessToken,
            String refreshToken
    ) {
        this.userName = userName;
        this.authentication = authentication;
        this.created = created;
        this.expiration = expiration;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Boolean authentication) {
        this.authentication = authentication;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenVO tokenVO)) return false;
        return Objects.equals(userName, tokenVO.userName) && Objects.equals(authentication, tokenVO.authentication) && Objects.equals(created, tokenVO.created) && Objects.equals(expiration, tokenVO.expiration) && Objects.equals(accessToken, tokenVO.accessToken) && Objects.equals(refreshToken, tokenVO.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, authentication, created, expiration, accessToken, refreshToken);
    }
}
