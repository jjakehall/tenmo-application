package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

public class AccountService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;
    private AuthenticatedUser currentUser;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public AccountService(String url, AuthenticatedUser currentUser){
        this.currentUser = currentUser;
        baseUrl = url;
    }

    public BigDecimal getBalance() {
        BigDecimal balance = new BigDecimal(0);
        try{
            balance = restTemplate.exchange(baseUrl + "account/balance/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
            System.out.println("Your current account balance is: $" + balance);
        }catch (RestClientResponseException e) {
            System.out.println("Error, can't retrieve balance");
        }
        return balance;

    }
    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
