package com.dataslab.vscan;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ResourceManager {

    private final Class<?> aClass;

    public ResourceManager(Class<?> aClass) {
        this.aClass = aClass;
    }


    public String load(String resource) {
        try {
            return IOUtils.toString(aClass.getResourceAsStream(resource), UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Error appeared while loading resource", e);
        }
    }
}
