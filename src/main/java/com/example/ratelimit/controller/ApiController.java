package com.example.ratelimit.controller;

import com.example.ratelimit.dao.HotelsDao;
import com.example.ratelimit.entity.HotelInfo;
import com.example.ratelimit.service.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static com.example.ratelimit.constants.RateLimiterConstants.SUSPEND_TIME;

@RestController
@ComponentScan("com.example.*")
public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private HotelsDao hotelsDao;
    @Autowired
    private RateLimitService rateLimitService;

    @RequestMapping(value ="/hotels/{cityId}",method = RequestMethod.GET)
    public Object searchHotels(@PathVariable @Valid String cityId, @RequestParam(value = "order",defaultValue = "asc") String order, @RequestHeader("api_key") String apiKey) {

        LOG.info("Inside the search hotels method");
        LOG.info("CityId {}, Ordering {}, API Key {}",cityId,order,apiKey);

        if(rateLimitService.getSuspendMap().containsKey(apiKey) && !rateLimitService.isSuspendTimeElapsed(apiKey)){
            return String.format("API key suspended for %s mins",(SUSPEND_TIME/(1000*60)));
        }else if(rateLimitService.getSuspendMap().containsKey(apiKey) && rateLimitService.isSuspendTimeElapsed(apiKey)){
            rateLimitService.getSuspendMap().remove(apiKey);
        }

        if(rateLimitService.checkRateLimiter(apiKey)){
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

}
