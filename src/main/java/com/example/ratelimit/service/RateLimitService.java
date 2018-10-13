package com.example.ratelimit.service;

import com.example.ratelimit.entity.RateLimitEntity;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class RateLimitService {

    private Map<String,Object> suspendMap=new HashMap<>();
    private Map<String, RateLimitEntity> rateLimiter=new HashMap<>();
    private Properties props=new Properties();

    public RateLimitService() throws FileNotFoundException {
        InputStream input = new FileInputStream("/Users/boomerang/tutorials_and_courses/ratelimit/src/main/resources/api_key_request_limit.properties");
        props.load(input);
    }
}
