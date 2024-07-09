package com.artigo.dota.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomTokenUtil {

    @Value(value = "${custom.token}")
    private String customToken;

    public boolean validateToken(String token) {
        return customToken.equals(token);
    }
}
