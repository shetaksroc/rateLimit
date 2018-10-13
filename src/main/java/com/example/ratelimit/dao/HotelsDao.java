package com.example.ratelimit.dao;

import com.example.ratelimit.entity.HotelInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ratelimit.constants.RateLimiterConstants.HOTELS_DATA;

@Component
@Getter
@Setter
public class HotelsDao {

    private Map<String, List<HotelInfo>> hotels;

    public HotelsDao() throws IOException {
        hotels=new HashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
        File file=new File(classLoader.getResource(HOTELS_DATA).getFile());
        BufferedReader br=new BufferedReader(new FileReader(file));
        br.readLine();
        String line=null;
        while ((line=br.readLine()) != null){
            String[] ar=line.split(",");
            readHotelData(ar);
        }
    }

    private void readHotelData(String[] ar) {
        if(hotels.containsKey(ar[0])){
            List<HotelInfo> list=hotels.get(ar[0]);
            list.add(new HotelInfo(ar[1],ar[2],Double.parseDouble(ar[3])));
            hotels.put(ar[0],list);
        }else{
            List<HotelInfo> list=new ArrayList<>();
            list.add(new HotelInfo(ar[1],ar[2],Double.parseDouble(ar[3])));
            hotels.put(ar[0],list);
        }
    }

    @Override
    public String toString() {
        return "HotelsDao{" +
                "hotels=" + hotels +
                '}';
    }
}
