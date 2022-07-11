package com.serookie.product.web;

import com.alibaba.fastjson.JSON;
import com.serookie.product.entity.CategoryEntity;
import com.serookie.product.service.CategoryService;
import jdk.jfr.Category;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/25
 */
@Controller
@Slf4j
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping({"/index.html","/"})
    public String index(Model model, HttpSession session){
        // TODO: 2022/6/25 前端首页
       ModelAndView modelAndView = new ModelAndView();
        //查询所有的一级目录
       List<CategoryEntity> categories=categoryService.getLevel1Category();
       model.addAttribute("categoryList",categories);
       log.info("session:{}",session.getAttribute("user"));
        return "index";
    }
    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        RLock lock = redissonClient.getLock("lock");
        lock.lock(10, TimeUnit.SECONDS);
        try{
            System.out.println("加锁成功+"+Thread.currentThread().getName());
        }finally {
            System.out.println("释放锁+"+Thread.currentThread().getName());
            lock.unlock();
        }
        return "hello";
    }

    @GetMapping("/read")
    @ResponseBody
    public String readLock(){
        RReadWriteLock readWrite = redissonClient.getReadWriteLock("readWrite");//读写锁
        RLock rLock = readWrite.readLock();//读锁
        rLock.lock();
        String readWriteLock="";
        try{
            readWriteLock = redisTemplate.opsForValue().get("readWriteLock");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            rLock.unlock();//释放锁
        }
        return readWriteLock;
    }
    @GetMapping("/write")
    @ResponseBody
    public String writeLock(){

        RReadWriteLock readWrite = redissonClient.getReadWriteLock("readWrite");//读写锁
        RLock wLock = readWrite.writeLock();
        wLock.lock();
        String s="";
        try{
             s= UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("readWriteLock",s);
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
           wLock.unlock();//进行解锁
        }
        return s;//读取锁
    }
    /**
     *semaphore 信号量
     * acquire 获取锁
     */
    @GetMapping("/park")
    @ResponseBody
    public String park(){
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        try{
            semaphore.acquire();//去获取锁 阻塞
//            semaphore.tryAcquire();//会给一个返回值，true表示可以获取，false表示不能获取，不会进行阻塞
        }catch(Exception e){
           e.printStackTrace();
        }
        System.out.println("抢占到了车位");
        return "park";
    }
    @GetMapping("/go")
    @ResponseBody
    public String go(){
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        semaphore.release();//去获取锁 瑞里斯
        System.out.println("释放了车位");
        return "park";
    }
    //CountDownLatch 关门，学生都走完了，你才能关门。
//    @GetMapping("/door")
//    @ResponseBody
//    public String door(){
//        RCountDownLatch latch = redissonClient.getCountDownLatch("countDownLatch");
//        try {
//            latch.trySetCount(5);//5个学生
//            latch.await();//等待学生走完
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return "关门";
//    }
//    @GetMapping("/gogo")
//    @ResponseBody
//    public String gogo(){
//        RCountDownLatch latch = redissonClient.getCountDownLatch("countDownLatch");
//        latch.countDown();//计数减-
//        return "学生走了一个";
//    }
    @GetMapping("/door")
    @ResponseBody
    public String door(){
        RCountDownLatch latch = redissonClient.getCountDownLatch("countDownLatch");
        try {
            latch.trySetCount(5);//5个学生
            latch.await();//等待学生走完
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "关门";
    }
    @GetMapping("/gogo")
    @ResponseBody
    public String gogo(){
        RCountDownLatch latch = redissonClient.getCountDownLatch("countDownLatch");
        latch.countDown();//计数减-
        return "学生走了一个";
    }

}
