package com.example.ratelimit.controller;

import java.io.Serializable;
import java.util.Map;

public class APIResponse implements Serializable {

    private String status;
    private String error;
    private Map<String,Object> result;
}
