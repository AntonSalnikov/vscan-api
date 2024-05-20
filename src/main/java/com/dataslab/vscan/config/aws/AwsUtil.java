package com.dataslab.vscan.config.aws;

import org.apache.commons.lang3.StringUtils;
import io.awspring.cloud.autoconfigure.core.Profile;
import software.amazon.awssdk.regions.Region;

import java.util.Optional;

public class AwsUtil {

    public static Region region(String region) {

        return Optional.ofNullable(region)
                .filter(StringUtils::isNotBlank)
                .map(Region::of)
                .orElseThrow(() -> new IllegalArgumentException("Region should not be null"));
    }

    public static String profile(Profile profile) {
        return  Optional.ofNullable(profile)
                .map(Profile::getName)
                .orElse("no-profile");
    }


    private AwsUtil() {
    }
}
