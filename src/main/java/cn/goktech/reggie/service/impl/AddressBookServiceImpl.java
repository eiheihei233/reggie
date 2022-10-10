package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.entity.AddressBook;
import cn.goktech.reggie.mapper.AddressBookMapper;
import cn.goktech.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
