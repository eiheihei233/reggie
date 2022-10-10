package cn.goktech.reggie.service;

import cn.goktech.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {


    /**
     * 根据id删除分类，删除之前进行判断
     */
    public void remove(Long id);
}
