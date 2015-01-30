package com.epitech.Model;

/**
 * Created by favre_q on 29/01/15.
 */
public class Token {

    public String token;

    public Token(){
        token = "";
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
