package com.example.ratelimit.service;

import com.example.ratelimit.controller.ApiController;
import com.example.ratelimit.entity.RateLimitEntity;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.example.ratelimit.constants.RateLimiterConstants.*;

@Component
@Getter
@Setter
public class RateLimitService {

    private static final Logger LOG = LoggerFactory.getLogger(RateLimitService.class);
    private Map<String,Object> suspendMap;
    private Map<String, RateLimitEntity> rateLimiter;
    private Properties props=new Properties();


    public RateLimitService() throws IOException {
        suspendMap=new HashMap<>();
        rateLimiter=new HashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream input = new FileInputStream(classLoader.getResource(API_REQUEST_LIMIT_PROPS).getFile());
        props.load(input);
    }

    public boolean isSuspendTimeElapsed(String apiKey) {
        Long suspendTime = (Long) suspendMap.get(apiKey);
        if(now() - suspendTime >= SUSPEND_TIME){
            return true;
        }
        return false;
    }

    public boolean checkRateLimiter(String apiKey) {
        LOG.info("Inside the check rate limiter method");
        if(!rateLimiter.containsKey(apiKey)){
            rateLimiter.put(apiKey,new RateLimitEntity(Integer.parseInt(props.getProperty(apiKey,DEFAULT_ALLOWED_RATE)),now()));
        }
        LOG.info("{}",rateLimiter.get(apiKey).toString());

        RateLimitEntity helper =rateLimiter.get(apiKey);
        if(now()-helper.getRequestTime() < DEFAULT_RATE_LIMIT_TIME && helper.getBucket()>0){
            helper.setBucket(helper.getBucket()-1);
            return true;
        }else if(now()-helper.getRequestTime() < DEFAULT_RATE_LIMIT_TIME && helper.getBucket()<=0){
            suspendMap.put(apiKey, System.currentTimeMillis());
            rateLimiter.remove(apiKey);
            return false;
        }
        else if(now()-helper.getRequestTime() > DEFAULT_RATE_LIMIT_TIME){
            resetValues(helper,apiKey);
            helper.setBucket(helper.getBucket()-1);
            return true;
        }
        return false;
    }

    private void resetValues(RateLimitEntity helper, String apiKey){
        helper.setRequestTime(System.currentTimeMillis());
        helper.setBucket(Integer.parseInt(props.getProperty(apiKey,DEFAULT_ALLOWED_RATE)));
    }

    private Long now(){
        return System.currentTimeMillis();
    }



}
