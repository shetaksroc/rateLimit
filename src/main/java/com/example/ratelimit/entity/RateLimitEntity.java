package com.example.ratelimit.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateLimitEntity {

    private Long requestTime;
    private Integer bucket;

    public RateLimitEntity(Integer bucket, Long requestTime) {
        this.requestTime = requestTime;
        this.bucket = bucket;
    }
}
