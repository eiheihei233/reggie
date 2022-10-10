package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.dto.DishDto;
import cn.goktech.reggie.entity.Dish;
import cn.goktech.reggie.entity.DishFlavor;
import cn.goktech.reggie.mapper.DishMapper;
import cn.goktech.reggie.service.DishFlavorService;
import cn.goktech.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        // 将口味信息保存到dishFlavor中
        Long dishId = dishDto.getId(); // 菜品ID
        // JAVA8新特性，使用stream流操作，比for增强要好
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {

        DishDto dishDto = new DishDto();
        // 查询菜品基本信息
        Dish dish = this.getById(id);

        BeanUtils.copyProperties(dish,dishDto);

        // 查询口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(DishFlavor::getDishId,dish.getId());

        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表
        this.updateById(dishDto);
        // 清理当前菜品对应的口味数据

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        // 重新添加口味数据

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
