package com.serookie.product.service.impl;

import com.serookie.product.service.CategoryBrandRelationService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.product.dao.CategoryDao;
import com.serookie.product.entity.CategoryEntity;
import com.serookie.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private RedissonClient redissonClient;

    //创建一个本地缓存
//    private Map<String,Object> cacheMap=new HashMap<>();
    //集成redis来做缓存
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查询所有的结果，将树形结构组装起来
     *
     * @return
     */
//    @Override
//    public List<CategoryEntity> queryWithTree() throws InterruptedException {
//        //怕删除的不是自己的锁，所以需要加上uuid来继续校验
//        String uuid = UUID.randomUUID().toString();
//        //先去缓存中查，如果没有查到再去数据库中查
//        List<CategoryEntity> category =(List<CategoryEntity>) redisTemplate.opsForValue().get("category");
//        //加锁redis
//        Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock",uuid,30,TimeUnit.SECONDS);//setNx命令
//        if(flag){
//            //加锁成功，来执行代码
////            redisTemplate.expire("lock",30,TimeUnit.SECONDS);
//            if(category==null) {
//                //为了解决数据库，缓存问题穿透问题，需要加锁
//                List<CategoryEntity> cacheCategory = (List<CategoryEntity>) redisTemplate.opsForValue().get("category");
//                if (StringUtils.isEmpty(cacheCategory)) {
//                    System.out.println("查询了数据库.....当前线程为:" + Thread.currentThread().getName());
//                    List<CategoryEntity> categoryEntitiesList = categoryDao.selectList(null);
//                    //查询所有的一级分类
//                    List<CategoryEntity> levelMenu = categoryEntitiesList.stream().filter((v) -> {
//                        return v.getParentCid() == 0;
//                    }).map(menu -> {
//                        menu.setCategoryChildren(queryChildren(menu, categoryEntitiesList));
//                        return menu;
//                    }).sorted((menu1, menu2) -> {
//                        return menu1.getSort() == null ? 0 : menu1.getSort() - (menu2.getSort() == null ? 0 : menu2.getSort());
//                    }).collect(Collectors.toList());
//                    redisTemplate.opsForValue().set("category", levelMenu, 1, TimeUnit.DAYS);//给来一个过期时间，日期为1天
//                    return levelMenu;//将从数据库中拿到的数据进行返回
//                }
//            }
//            List<CategoryEntity> cacheCategoryEntities =(List<CategoryEntity>) redisTemplate.opsForValue().get("category");
//            //执行完成之后要进行删锁
////            String lockVal =(String) redisTemplate.opsForValue().get("lock");
////            if(uuid.equals(lockVal)){
////                //进行删除
////                redisTemplate.delete("lock");//进行解锁操作
////            }
//            //使用lua脚本来实现原子性操作,就是查询和删除的原子性
//            String script="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//            this.redisTemplate.execute(new DefaultRedisScript<>(script), Arrays.asList("lock"), uuid);
//            return cacheCategoryEntities;
//        }else{
//            //加锁失败，进行等待
//            TimeUnit.SECONDS.sleep(2);
//            return queryWithTree();//进行重试
//        }
//    }

//    @Override
//    public List<CategoryEntity> queryWithTree() {
//        //先去缓存中查，如果没有查到再去数据库中查
//        List<CategoryEntity> category =(List<CategoryEntity>) redisTemplate.opsForValue().get("category");
////        List<CategoryEntity> category =(List<CategoryEntity>) cacheMap.get("category");
////        if(category==null){
//        //查出所有的数据
//        if(category==null){
//            //为了解决数据库，缓存问题穿透问题，需要加锁
//            synchronized(this){
//                List<CategoryEntity> cacheCategory =(List<CategoryEntity>) redisTemplate.opsForValue().get("category");
//                if(StringUtils.isEmpty(cacheCategory)){
//                    System.out.println("查询了数据库.....当前线程为:"+Thread.currentThread().getName());
//                    List<CategoryEntity> categoryEntitiesList = categoryDao.selectList(null);
//                    //查询所有的一级分类
//                    List<CategoryEntity> levelMenu = categoryEntitiesList.stream().filter((v) -> {
//                        return v.getParentCid()==0;
//                    }).map(menu->{
//                        menu.setCategoryChildren(queryChildren(menu,categoryEntitiesList));
//                        return menu;
//                    }).sorted((menu1,menu2)->{
//                        return menu1.getSort()==null?0:menu1.getSort()-(menu2.getSort()==null?0:menu2.getSort());
//                    }).collect(Collectors.toList());
//                    redisTemplate.opsForValue().set("category",levelMenu,1, TimeUnit.DAYS);//给来一个过期时间，日期为1天
//                }
//                return cacheCategory;
//            }
//        }
////            cacheMap.put("category",levelMenu);
////        }
////        List<CategoryEntity> cache=(List<CategoryEntity>)cacheMap.get("category");
//        List<CategoryEntity> cacheCategoryEntities =(List<CategoryEntity>) redisTemplate.opsForValue().get("category");
//        return cacheCategoryEntities;
//    }
    @Override
    public List<CategoryEntity> queryWithTree() {
        //使用redisson来加分布式锁
        RLock lock = redissonClient.getLock("product-lock");//分布式锁
        List<CategoryEntity> category =(List<CategoryEntity>) redisTemplate.opsForValue().get("category");
        if(category==null){
            lock.lock();//加锁
            try{
                List<CategoryEntity> categoryEntitiesList = categoryDao.selectList(null);
                //查询所有的一级分类
                List<CategoryEntity> levelMenu = categoryEntitiesList.stream().filter((v) -> {
                    return v.getParentCid() == 0;
                }).map(menu -> {
                    menu.setCategoryChildren(queryChildren(menu, categoryEntitiesList));
                    return menu;
                }).sorted((menu1, menu2) -> {
                    return menu1.getSort() == null ? 0 : menu1.getSort() - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());
                redisTemplate.opsForValue().set("category",levelMenu,1,TimeUnit.DAYS);
                return levelMenu;
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();//解锁
            }
        }
        return category;
    }

    @Override
    public Boolean removeMenuIds(Long[] catIds) {
        if (StringUtils.isEmpty(catIds)) {
            return false;
        }
        //TODO 要检索是否有人对它进行了引用
        return categoryDao.deleteBatchIds(Arrays.asList(catIds)) > 0;
    }

    @Override
    public Long[] findByCatelogPath(Long attrGroupId) {
        List<Long> array = new ArrayList<>();
        CategoryEntity category = this.getById(attrGroupId);
        List<Long> categoryPath = findCategoryPath(category, array);
        Collections.reverse(categoryPath);
        return categoryPath.toArray(new Long[categoryPath.size()]);
    }

    /**
     * 同步更新操作
     *
     * @param category
     */
    @CacheEvict(value = "category",key = "'getLevel1Category'") //一但修改了数据，就会去删除缓存
    @Transactional
    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
//        if (!StringUtils.isEmpty(category)) {
//            //同步进行更新
//            categoryBrandRelationService.updateByCategory(category.getCatId(), category.getName());
//        }
    }

    /**
     * 查询所有的一级分类
     *
     * @return
     */
    //给缓存进行分区
    @Cacheable(value = {"category"},key = "#root.methodName")
    @Override
    public List<CategoryEntity> getLevel1Category() {
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cat_level", 1);
        List<CategoryEntity> categoryEntities = categoryDao.selectList(queryWrapper);
        return categoryEntities;
    }

    private List<Long> findCategoryPath(CategoryEntity category, List<Long> array) {
        array.add(category.getCatId());
        CategoryEntity categoryEntity = categoryDao.selectById(category.getParentCid());
        if (category.getParentCid() != 0) {//结束
            return findCategoryPath(categoryEntity, array);
        }
        return array;
    }

    /**
     * 实现通过查询出一级目录循环递归出所有的二级目录和三级目录
     *
     * @param categoryEntities
     * @param categoryRoot
     * @return
     */
    private List<CategoryEntity> queryChildren(CategoryEntity categoryRoot, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> collect = categoryEntities.stream().filter(item -> {
            return categoryRoot.getCatId() == item.getParentCid();
        }).map(item -> {
            item.setCategoryChildren(queryChildren(item, categoryEntities));
            return item;
        }).sorted((menu1, menu2) -> {
//        使用的是CompareTo的比较，如果是0就等于不排序，如果是1就是升序，小于0就是降序
            return menu1.getSort() == null ? 0 : menu1.getSort() - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return collect;
    }
}