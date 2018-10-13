package com.example.ratelimit.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

public class APIRequest implements Serializable {

    @NotNull
    @NotEmpty
    @JsonProperty("cityId")
    private String cityId;

    @JsonProperty("ascending")
    private boolean ascending=true;

}
