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

    private static final int VERSION_SIZE = 3;
    private static final String REQUIRED_IOS_APP_VERSION = "0.5.0";
    private static final String REQUIRED_ANDROID_APP_VERSION = "0.5.0";

    @GetMapping("/version")
    public ResponseEntity<String> validateRequiredAppVersion(@RequestParam String os, @RequestParam String appVersion) {
        if (!validateVersion(os, appVersion)) {
            return  ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).build();
        }
        return ResponseEntity.ok().build();
    }

    private boolean validateVersion(String os, String appVersion) {
        if (os.equals("iOS")) {
            return compareVersions(appVersion, REQUIRED_IOS_APP_VERSION);
        } else if (os.equals("Android")) {
            return compareVersions(appVersion, REQUIRED_ANDROID_APP_VERSION);
        }
        return false;
    }

    private boolean compareVersions(String appVersion, String requiredVersion) {
        String[] appVersionSplitString = appVersion.split("\\.");
        String[] requiredVersionSplitString = requiredVersion.split("\\.");
        final int inputVersionSize = appVersionSplitString.length;

        int[] appVersionSplit = new int[VERSION_SIZE];
        int[] requiredVersionSplit = new int[VERSION_SIZE];

        for (int i = 0; i < inputVersionSize; i++) {
            appVersionSplit[i] = Integer.parseInt(appVersionSplitString[i]);
            requiredVersionSplit[i] = Integer.parseInt(requiredVersionSplitString[i]);
        }

        int matched = 0;
        for (int i = 0; i < VERSION_SIZE; i++) {
            if (appVersionSplit[i] > requiredVersionSplit[i]) {
                return true;
            } else if (appVersionSplit[i] == requiredVersionSplit[i]) {
                matched++;
            }
        }
        return matched == VERSION_SIZE;
    }
}
