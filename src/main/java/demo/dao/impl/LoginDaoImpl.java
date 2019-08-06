package demo.dao.impl;

import demo.dao.LoginDao;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by Habit on 2017-07-01.
 */
@Lazy
@Component
public class LoginDaoImpl implements LoginDao{
    public LoginDaoImpl() {
        System.out.println("LoginDaoImpl........");
    }
    public void fun(){
        System.out.println("LoginDaoImpl------fun............");
    }
    public void fun2(){
        System.out.println("LoginDaoImpl------fun............");
    }
}
