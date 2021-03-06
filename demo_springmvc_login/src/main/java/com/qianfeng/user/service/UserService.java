package com.qianfeng.user.service;

import com.qianfeng.user.exception.PasswordErrorException;
import com.qianfeng.user.mapper.UserMapper;
import com.qianfeng.user.po.TbUser;
import com.qianfeng.user.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param username
     * @param password 密码明文
     */
    public TbUser login(String username,String password) throws Exception{
        if (null == username) {
            throw new NullPointerException("username is null");
        }

        //1、检验用户名在数据库是否存在
        TbUser tbUser = userMapper.checkUsername(username);
        if (tbUser == null) {
            throw new NullPointerException("username not found");
        }

        //2、密码校验
        //a、先对用户传入的密码进行加密
        String passwordSalt = tbUser.getPasswordSalt();
        String md5Password = MD5Utils.md5(password, passwordSalt);
        //b、同数据库的密文进行对比
        if (!md5Password.equals(tbUser.getPassword())){
            throw new PasswordErrorException("password is error");
        }


        return tbUser;

    }
}
