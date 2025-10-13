package com.rashmita.loandisbursement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class RedisTestController {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    @GetMapping("/redis/test")
    public void testRedis() {
        Set<String> keys = template.keys("*");
        for (String key : keys) {
            Object value = template.opsForValue().get(key);
            System.out.println(key + " -> " + value);
        }
    }
}
