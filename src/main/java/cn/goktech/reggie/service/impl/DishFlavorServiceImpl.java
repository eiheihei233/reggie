package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.entity.DishFlavor;
import cn.goktech.reggie.mapper.DishFlavorMapper;
import cn.goktech.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
