package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.entity.User;
import cn.goktech.reggie.mapper.UserMapper;
import cn.goktech.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
