package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccountDaoJdbc implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public AccountDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int UserId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sql, UserId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");

        }
        return balance;

    }

    @Override
    public BigDecimal addBalance(BigDecimal amount, int UserId) {

        try {
            String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ?";
            jdbcTemplate.update(sql, amount, UserId);
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");

        }
        return getBalance(UserId);


    }

    @Override
    public BigDecimal subtractBalance(BigDecimal amount, int UserId) {

        try {
            String sql = "UPDATE account SET balance = balance - ? WHERE user_id = ?";
            jdbcTemplate.update(sql, amount, UserId);
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");

        }
        return getBalance(UserId);
    }


    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;

    }
}
