package com.example.ratelimit.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelInfo implements Comparable{

    private String hotelId;
    private String roomType;
    private double price;

    public HotelInfo(String hotelId, String roomType, double price) {
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.price = price;
    }

    @Override
    public int compareTo(Object o) {
        return  (this.getPrice() < ((HotelInfo) o).getPrice() ? -1 : (this.getPrice() == ((HotelInfo) o).getPrice() ? 0 : 1));
    }
}
