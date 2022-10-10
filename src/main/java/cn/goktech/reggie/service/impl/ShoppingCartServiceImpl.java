package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.entity.ShoppingCart;
import cn.goktech.reggie.mapper.ShoppingCartMapper;
import cn.goktech.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
