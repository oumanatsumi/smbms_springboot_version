package com.ouma.aspects;

import com.ouma.annotation.RedisCache;
import com.ouma.service.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RedisCacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheAspect.class);

    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(com.ouma.annotation.RedisCache)")
    public void redisCachePointcut() {
    }

    @Around("redisCachePointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder redisKeySb = new StringBuilder("AOP").append("::");

        // 类
        String className = joinPoint.getTarget().toString().split("@")[0];
        redisKeySb.append(className).append("::");

        // 方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        redisKeySb.append(methodName);

        // 参数
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                redisKeySb.append(":").append(args[i]);
            }
        }

        String redisKey = redisKeySb.toString();
        logger.info("Redis Key：{}", redisKey);

        Object result = redisService.get(redisKey);
        if (result != null) {
            logger.info("从Redis中获取数据：{}", result);
            return result;
        } else {
            try {
                result = joinPoint.proceed();
                logger.info("从MySQL中获取数据：{}", result);
            } catch (Throwable e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            // 获取失效时间
            RedisCache redisCache = signature.getMethod().getAnnotation(RedisCache.class);
            long expire = redisCache.expire();
            redisService.set(redisKey, result, expire);
        }

        return result;
    }
}