package com.onlineauctions.onlineauctions.pojo.config;

import lombok.Data;

import java.util.Set;

@Data
public class NoLogin{
    private Set<String> path;
    private Set<String> contains;

}
