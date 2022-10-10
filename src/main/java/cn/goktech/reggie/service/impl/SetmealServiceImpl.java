package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.dto.DishDto;
import cn.goktech.reggie.dto.SetmealDto;
import cn.goktech.reggie.entity.Setmeal;
import cn.goktech.reggie.entity.SetmealDish;
import cn.goktech.reggie.mapper.SetmealMapper;
import cn.goktech.reggie.service.SetMealDishService;
import cn.goktech.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {
    @Autowired
    private SetMealDishService setMealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息
        this.save(setmealDto);
        // 保存关联信息

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());


        setMealDishService.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {

        SetmealDto setmealDto = new SetmealDto();

        Setmeal setmeal = this.getById(id);
        // 将基本信息进行一个拷贝
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());

        List<SetmealDish> setmealDishes = setMealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWishDish(SetmealDto setmealDto) {

        this.updateById(setmealDto);

        // 删除原来的套餐对应菜品信息

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());

        setMealDishService.remove(queryWrapper);


        // 更新新的套餐对应菜品信息

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setMealDishService.saveBatch(setmealDishes);


    }


}
