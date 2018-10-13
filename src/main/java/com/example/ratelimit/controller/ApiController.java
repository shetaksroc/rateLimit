package com.example.ratelimit.controller;

import com.example.ratelimit.entity.RateLimitEntity;
import com.example.ratelimit.service.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.util.*;

@RestController
@ComponentScan

public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private HotelsDao hotelsDao;
    @Autowired
    private RateLimitService rateLimitService;

    private Properties props=new Properties();

    public ApiController() throws IOException {
        InputStream input = new FileInputStream("/Users/boomerang/tutorials_and_courses/ratelimit/src/main/resources/api_key_request_limit.properties");
        props.load(input);
    }

    private Map<String,Object> suspendMap=new HashMap<>();
    private Map<String, RateLimitEntity> rateLimiter=new HashMap<>();

    @RequestMapping(value ="/hotels/{cityId}",method = RequestMethod.GET)
    public Object searchHotels(@PathVariable @Valid String cityId, @RequestParam(value = "order",defaultValue = "asc") String order, @RequestHeader("api_key") String apiKey) {

        System.out.println("Rate Limit"+ props.getProperty(apiKey,"5"));

        if(suspendMap.containsKey(apiKey) && !isSuspendTimeElapsed(apiKey)){
            return "API key suspended for 5 mins";
        }else if(suspendMap.containsKey(apiKey) && isSuspendTimeElapsed(apiKey)){
            suspendMap.remove(apiKey);
        }

        if(checkRateLimiter(apiKey)){
            List<HotelInfo> list =hotelsDao.getHotels().get(cityId);
            if(order.equalsIgnoreCase("desc")){
                Collections.sort(list,Collections.reverseOrder());
            }else{
                Collections.sort(list);
            }
            return list;
        }else{
            return "Rate limit exceeded";
        }

    }

    private boolean checkRateLimiter(String apiKey) {
        if(!rateLimiter.containsKey(apiKey)){
            rateLimiter.put(apiKey,new RateLimitEntity(Integer.parseInt(props.getProperty(apiKey,"5")),System.currentTimeMillis()));
        }

        RateLimitEntity helper =rateLimiter.get(apiKey);
        if(System.currentTimeMillis()-helper.getRequestTime() < (60*1000) && helper.getBucket()>0){
            helper.setBucket(helper.getBucket()-1);
            return true;
        }else if(System.currentTimeMillis()-helper.getRequestTime() < (60*1000) && helper.getBucket()<=0){
            suspendMap.put(apiKey, System.currentTimeMillis());
            rateLimiter.remove(apiKey);
            return false;
        }
        else if(System.currentTimeMillis()-helper.getRequestTime() > (60*1000)){
            helper.setRequestTime(System.currentTimeMillis());
            helper.setBucket(Integer.parseInt(props.getProperty(apiKey,"5")));
            helper.setBucket(helper.getBucket()-1);
            return true;
        }

        return false;
    }

    private boolean isSuspendTimeElapsed(String apiKey) {
        Long suspendTime = (Long) suspendMap.get(apiKey);
        if(System.currentTimeMillis() - suspendTime >= (5 * 60 * 1000)){
            return true;
        }
        return false;
    }

}
