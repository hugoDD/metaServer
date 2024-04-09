package cn.granitech.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/8/21 11:03
 * @Version: 1.0
 */

@Service
public class TransactionTestService {

    @Autowired
    DataSource dataSource;

    /**
     * 未添加事务
     */
    public void test1() {
        new JdbcTemplate(dataSource).execute(" INSERT INTO `t_test` (`num`) VALUES (1) ");
        int i = 1 / 0;
    }

    @Transactional
    public void test2() {
        new JdbcTemplate(dataSource).execute(" INSERT INTO `t_test` (`num`) VALUES (2) ");
        int i = 1 / 0;
    }

    @Transactional
    public void test3() {
        new JdbcTemplate(dataSource).execute(" INSERT INTO `t_test` (`num`) VALUES (3) ");
    }

    @Transactional
    public void test4() {
        new JdbcTemplate(dataSource).execute(" INSERT INTO `t_test` (`num`) VALUES (4) ");
    }

}
