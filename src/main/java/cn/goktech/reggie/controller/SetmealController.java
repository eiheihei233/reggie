package cn.goktech.reggie.controller;

import cn.goktech.reggie.common.R;
import cn.goktech.reggie.dto.SetmealDto;
import cn.goktech.reggie.entity.Category;
import cn.goktech.reggie.entity.Setmeal;
import cn.goktech.reggie.entity.SetmealDish;
import cn.goktech.reggie.service.CategoryService;
import cn.goktech.reggie.service.SetMealDishService;
import cn.goktech.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetMealDishService setMealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     *  套餐的分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Setmeal> pageInfo = new Page(page, pageSize);

        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null,Setmeal::getName,name);

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        // 最后要传出的为setmealDtoPage，进行一个复制操作，其中records重写
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list =
                records.stream().map((item) ->{

                    SetmealDto setmealDto = new SetmealDto();

                    BeanUtils.copyProperties(item,setmealDto);
                    Long categoryId = item.getCategoryId();
                    // 根据分类id查询
                    Category category = categoryService.getById(categoryId);

                    String categoryName = category.getName();

                    setmealDto.setCategoryName(categoryName);

                    return setmealDto;

                }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * 在修改时动态获取到套餐全部信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<SetmealDto> get(@PathVariable Long id){

        SetmealDto setmealDto = setmealService.getByIdWithDish(id);

        return R.success(setmealDto);
    }

    /**
     * 修改套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){

        setmealService.updateWishDish(setmealDto);

        return R.success("成功更改信息");
    }
    @DeleteMapping
    public R<String> delete(Long[] ids){

        for (Long id :ids) {
            setmealService.removeById(id);

            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(SetmealDish::getSetmealId,id);

            setMealDishService.remove(queryWrapper);
        }
        return R.success("成功删除");
    }

    @PostMapping("/status/0")
    public R<String> changeStatusToStop(Long[] ids){
        for (Long id :ids) {
            Setmeal setmeal = setmealService.getById(id);

            setmeal.setStatus(0);

            setmealService.updateById(setmeal);
        }
        return R.success("套餐停售成功");
    }
    @PostMapping("/status/1")
    public R<String> changeStatusToStart(Long[] ids){
        for (Long id :ids) {
            Setmeal setmeal = setmealService.getById(id);

            setmeal.setStatus(1);

            setmealService.updateById(setmeal);
        }
        return R.success("套餐起售成功");
    }
    //根据条件查询套餐数据
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());

        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,1);

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }
    @GetMapping("/dish/{id}")
    public R<Setmeal> oneMeal(@PathVariable Long id){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(id != null , Setmeal::getId,id);

        Setmeal setmeal = setmealService.getOne(queryWrapper);

        return R.success(setmeal);

    }
}
