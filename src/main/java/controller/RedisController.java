/*
 * @(#) RedisController
 * 版权声明 黄志军， 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:黄志军
 * <br> @author selfImpr
 * <br> 2018-06-22 09:16:02
 * <br> @description
 *
 *
 */

package controller;

import model.redis.RedisUser;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 为了方便使用，直接把到层的操作放在这里面
 */
 @Controller
public class RedisController {

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;


    /**
     *  非复杂对象的操作
     * 参考地址： http://www.cnblogs.com/qlqwjy/p/8562703.html
     */

    @RequestMapping(value = "/redis/simple", method = RequestMethod.GET)
    public String simpleOperation() {
        //String操作
        redisTemplate.opsForValue().set("myStr", "skyLine");
        redisTemplate.delete("myStr");
        redisTemplate.opsForValue().set("myStr", "skyLine");
        System.out.println(redisTemplate.opsForValue().get("myStr"));
        System.out.println("---------------");
        // List读写
        redisTemplate.delete("myList");
        redisTemplate.opsForList().rightPush("myList", "T");
        redisTemplate.opsForList().rightPush("myList", "L");
        redisTemplate.opsForList().leftPush("myList", "A");
        List<String> listCache = redisTemplate.opsForList().range("myList", 0, -1);
        for (String s : listCache) {
            System.out.println(s);
        }
        System.out.println("---------------");
        // Set读写
        redisTemplate.delete("mySet");
        redisTemplate.opsForSet().add("mySet", "A");
        redisTemplate.opsForSet().add("mySet", "B");
        redisTemplate.opsForSet().add("mySet", "C");
        Set<String> setCache = redisTemplate.opsForSet().members("mySet");
        for (String s : setCache) {
            System.out.println(s);
        }
        System.out.println("---------------");

        // Hash读写
        redisTemplate.delete("myHash");
        redisTemplate.opsForHash().put("myHash", "BJ", "北京");
        redisTemplate.opsForHash().put("myHash", "SH", "上海");
        redisTemplate.opsForHash().put("myHash", "HN", "河南");
        Map<String, String> hashCache = redisTemplate.opsForHash().entries("myHash");
        for (Map.Entry<String, String> entry : hashCache.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
        System.out.println("---------------");
        return "";
    }

    //操作复杂的数据类型：自定义的数据类型
    @RequestMapping(value = "/redis/complex", method = RequestMethod.GET)
    public void conplexOperation() {
        //新增两个用户
        RedisUser user = new RedisUser();
        user.setId("aaaaaa");
        user.setSex("男");
        user.setUserName("小胖子");
        user.setAge(25);
        RedisUser user1 = new RedisUser();
        user1.setId("bbbbbb");
        user1.setSex("男");
        user1.setUserName("大胖子");
        user1.setAge(50);

        add(user);
        add(user1);
        delete(user.getId());
        user1.setAge(100);
        update(user1);

        RedisUser user2 = get(user1.getId());
        System.out.print("获取到的对象信息" + user2.toString());
    }


    public boolean add(final RedisUser user) {
        return (boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            ValueOperations<String, Serializable> valueOper = redisTemplate.opsForValue();
            valueOper.set(user.getId(), user);
            return true;
        }, false, true);
    }

    public boolean batchAdd(final List<RedisUser> users) {
        return (boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            ValueOperations<String, Serializable> valueOper = redisTemplate.opsForValue();
            for (RedisUser user : users) {
                valueOper.set(user.getId(), user);
            }
            return true;
        }, false, true);
    }

    public void delete(String key) {
        List<String> list = new ArrayList<>();
        list.add(key);
        delete(list);
    }

    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public boolean update(final RedisUser user) {
        String id = user.getId();
        if (get(id) == null) {
            throw new NullPointerException("数据行不存在, key = " + id);
        }
        return (boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            ValueOperations<String, Serializable> valueOper = redisTemplate.opsForValue();
            valueOper.set(user.getId(), user);
            return true;
        });
    }

    public RedisUser get(final String keyId) {
        return (RedisUser) redisTemplate.execute((RedisCallback<RedisUser>) connection -> {
            ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
            RedisUser user = (RedisUser) operations.get(keyId);
            return user;
        });
    }
}
