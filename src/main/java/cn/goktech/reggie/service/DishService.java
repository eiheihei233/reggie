package cn.goktech.reggie.service;

import cn.goktech.reggie.dto.DishDto;
import cn.goktech.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品对应的口味数据
    public void saveWithFlavor(DishDto dishDto);
    // 根据id查询菜品信息和对应口味信息
    public DishDto getByIdWithFlavor(Long id);
    //更新菜品信息和口味信息
    public void updateWithFlavor(DishDto dishDto);
}
