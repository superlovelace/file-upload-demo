//package com.demo.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
///**
// * Redis配置类
// * @author peter
// * @date 2025/5/16
// */
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        //创建RedisTemplate对象，指定泛型一般直接使用<String,Object>,key为String类型，value为Object类型
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        //设置连接工厂（固定配置）RedisConnectionFactory由Spring提供，直接用就可以
//        template.setConnectionFactory(redisConnectionFactory);
//        //创建Json序列化工具
//        Jackson2JsonRedisSerializer<Object> ojjrs = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        ojjrs.setObjectMapper(om);
//        //创建StringRedisSerializer，其实创建StringRedisTemplate有默认值，可以直接注入使用的，就是得自己手动将数据序列化。
//        StringRedisSerializer srs = new StringRedisSerializer();
//        // 设置key和HashKey的序列化
//        // 这里参数内等同于RedisSerializer.string()都是指向了StringRedisSerializer里的静态常量UTF_8
//        template.setKeySerializer(srs);
//        template.setHashKeySerializer(srs);
//        //设置Value和HashValue的序列化
//        template.setValueSerializer(ojjrs);
//        template.setHashValueSerializer(ojjrs);
//        return template;
//    }
//}
