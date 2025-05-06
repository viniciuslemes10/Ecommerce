package com.vinicius.ecommerce.records.vo;

import java.io.Serializable;
import java.util.Objects;

public class AccountCredentialsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private String password;

    public AccountCredentialsVO() {}

    public AccountCredentialsVO(String userName, String password) {
        this.email = userName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AccountCredentialsVO that)) return false;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
