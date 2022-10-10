package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.common.CustomException;
import cn.goktech.reggie.entity.Category;
import cn.goktech.reggie.entity.Dish;
import cn.goktech.reggie.entity.Setmeal;
import cn.goktech.reggie.mapper.CategoryMapper;
import cn.goktech.reggie.service.CategoryService;
import cn.goktech.reggie.service.DishService;
import cn.goktech.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {

        // 查询当前分类是否关联菜品，如果关联，抛出异常

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 根据分类的id查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);

        int count1 = dishService.count(dishLambdaQueryWrapper);

        if (count1 > 0){
            // 已经关联菜品，抛异常
            throw new CustomException("当前分类下关联了菜品，无法删除");
        }

        // 查询当前分类是否关联套餐，如果关联，跑出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 根据分类的id查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            // 已经关联套餐，抛异常
            throw new CustomException("当前分类下关联了套餐，无法删除");
        }
        // 正常删除分类
        super.removeById(id);

    }
}
