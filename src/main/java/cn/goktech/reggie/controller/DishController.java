package cn.goktech.reggie.controller;

import cn.goktech.reggie.common.R;
import cn.goktech.reggie.dto.DishDto;
import cn.goktech.reggie.entity.Category;
import cn.goktech.reggie.entity.Dish;
import cn.goktech.reggie.entity.DishFlavor;
import cn.goktech.reggie.service.CategoryService;
import cn.goktech.reggie.service.DishFlavorService;
import cn.goktech.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// 菜品管理

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息的分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null, Dish::getName,name);

        queryWrapper.orderByDesc(Dish::getUpdateTime).orderByDesc(Dish::getPrice);

        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) ->{

            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);

            String categoryName = category.getName();

            dishDto.setCategoryName(categoryName);

            return dishDto;

        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据ID查询菜品信息和口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品与口味
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * 删除菜品与对应口味
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    public R<String> delete(Long[] ids){

        for (Long id:ids) {
            log.info("需要修改的id为：{}" ,id);
        }

        for (Long id :ids) {
            // 更新dish表
            dishService.removeById(id);
            // 清理当前菜品对应的口味数据

            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(DishFlavor::getDishId,id);

            dishFlavorService.remove(queryWrapper);
        }
        return R.success("删除成功");
    }

    /**
     * 将传来的id菜品status改为0
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> changeStatusToStop(Long[] ids){

        for (Long id:ids) {
            log.info("需要修改的id为：{}" ,id);
        }

        for (Long id:ids) {
            Dish dish = dishService.getById(id);

            dish.setStatus(0);

            dishService.updateById(dish);
        }


        return  R.success("已修改为停售状态");
    }


    /**
     * 将传来的id菜品status改为1
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> changeStatusToStart(Long[] ids){

        for (Long id:ids) {
            log.info("需要修改的id为：{}" ,id);
        }

        for (Long id:ids) {
            Dish dish = dishService.getById(id);

            dish.setStatus(1);

            dishService.updateById(dish);
        }

        return R.success("已修改为起售状态");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//
//        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
//
//        queryWrapper.eq(Dish::getStatus,1);
//
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());

        queryWrapper.eq(Dish::getStatus,1);

        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) ->{

            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);

            String categoryName = category.getName();

            dishDto.setCategoryName(categoryName);

            // 当前菜品id
            Long id = item.getId();
            
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            
            lambdaQueryWrapper.eq(DishFlavor::getDishId,id);

            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);

            dishDto.setFlavors(dishFlavorList);

            return dishDto;

        }).collect(Collectors.toList());



        return R.success(dishDtoList);

    }
}
