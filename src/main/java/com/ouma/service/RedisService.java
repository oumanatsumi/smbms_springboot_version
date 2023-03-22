package com.ouma.service;

public interface RedisService {
    void set(String key, Object value);

    void set(String key, Object value, long expire);

    Object get(String key);

    void expire(String key, long expire);

    void delete(String key);
}
