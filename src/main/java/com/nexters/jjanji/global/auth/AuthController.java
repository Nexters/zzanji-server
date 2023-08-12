package com.nexters.jjanji.global.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private static final double REQUIRED_IOS_APP_VERSION = 0.5;
    private static final double REQUIRED_ANDROID_APP_VERSION = 0.5;

    @GetMapping("/version")
    public ResponseEntity<String> validateRequiredAppVersion(@RequestParam String os, @RequestParam double appVersion) {
        if (!validateVersion(os, appVersion)) {
            return  ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).build();
        }
        return ResponseEntity.ok().build();
    }

    private boolean validateVersion(String os, double appVersion) {
        if (os.equals("iOS")) {
            return appVersion >= REQUIRED_IOS_APP_VERSION;
        } else if (os.equals("Android")) {
            return appVersion >= REQUIRED_ANDROID_APP_VERSION;
        } else {
            return false;
        }
    }
}
