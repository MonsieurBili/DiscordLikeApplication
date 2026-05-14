package org.example.issproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/turn")
@CrossOrigin(origins = {"http://localhost:5173","https://uiiss.onrender.com"})
@RequiredArgsConstructor
public class TurnController {

    @Value("${metered.secret.key}")
    private String secretKey;

    @GetMapping("/credentials")
    public ResponseEntity<?> getCredentials() throws Exception {
        String username = String.valueOf(System.currentTimeMillis() / 1000 + 3600);

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(secretKey.getBytes(), "HmacSHA1"));
        String credential = Base64.getEncoder()
                .encodeToString(mac.doFinal(username.getBytes()));

        return ResponseEntity.ok(Map.of(
                "username", username,
                "credential", credential,
                "url", "turn:issproject.metered.live:80"
        ));
    }
}
