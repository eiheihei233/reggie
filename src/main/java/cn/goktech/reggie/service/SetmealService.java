package cn.goktech.reggie.service;

import cn.goktech.reggie.dto.DishDto;
import cn.goktech.reggie.dto.SetmealDto;
import cn.goktech.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SetmealService extends IService<Setmeal> {
    // 新增套餐，同时保存套餐和菜品之间的关系
    public void saveWithDish(SetmealDto setmealDto);
    // 通过id查询到套餐以及对应菜品
    public SetmealDto getByIdWithDish(Long id);

    public void updateWishDish(SetmealDto setmealDto);
}
