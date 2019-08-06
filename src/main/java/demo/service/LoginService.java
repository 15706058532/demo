package demo.service;

import demo.dao.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by Habit on 2017-07-01.
 */
@Component
public class LoginService {
    @Autowired
    @Lazy
    private LoginDao loginDao;
    public LoginService() {
        System.out.println("LoginService.............");
    }

    public LoginDao getLoginDao() {
        loginDao.fun();
        return loginDao;
    }
}
