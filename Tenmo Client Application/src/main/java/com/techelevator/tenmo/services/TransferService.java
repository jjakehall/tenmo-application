package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class TransferService {
    private String baseUrl;
    RestTemplate restTemplate = new RestTemplate();
    private String authToken;
    private AuthenticatedUser currentUser;

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void sendBucks() {
        User[] users = null;
        Transfer transfer = new Transfer();
        Account account = new Account();

        try {
            Scanner scanner = new Scanner(System.in);
            users = restTemplate.exchange(baseUrl + "account/listUsers", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println("------------------------------\n"
                    + "Users\n" + "ID\tName\n" +
                    "------------------------------");
            for (User i : users) {
                if (i.getId() != currentUser.getUser().getId()) {
                    System.out.println(i.getId() + "\t\t" + i.getUsername());
                }
            }
            System.out.println("-----------------------------\n"
                    + "Enter Id of user you are sending to (0 to cancel): ");
            transfer.setUserTo(Integer.parseInt(scanner.nextLine()));
            transfer.setUserFrom(currentUser.getUser().getId());
            if (transfer.getUserTo() != 0); {
                System.out.println("Enter amount: ");



                try {
                    transfer.setAmount(new BigDecimal(Double.parseDouble(scanner.nextLine())));
                } catch (NumberFormatException e) {
                    System.out.println("Error when entering amount: ");
                }
                restTemplate.exchange(baseUrl + "transfer", HttpMethod.POST, makeTransferEntity(transfer), boolean.class);
                System.out.println("Transfer Successful");
            }
        } catch (Exception e) {
            System.out.println("Invalid" + e.getMessage());
        }
    }

    public void viewTransferHistory() {
        DisplayTransfer[] transfers = null;
//            try {


        Scanner scanner = new Scanner(System.in);
        transfers = restTemplate.exchange(baseUrl + "account/transfer/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), DisplayTransfer[].class).getBody();
        System.out.println("------------------------------\n" +
                "Transfers\n" + "ID\t\tFrom/To" + "\t\t\t" + "Amount\n" +
                "------------------------------");
        String fromOrTo = "";
        String name = "";

        for (DisplayTransfer i : transfers) {
            if( currentUser.getUser().getUsername().equals(i.getUserFrom())) {
                fromOrTo = "From: ";
                name = i.getUserTo();
                System.out.println(i.getTransferId() + "\t" + fromOrTo + name + "\t\t" + i.getAmount());
            } else {
                fromOrTo = "To: ";
                name = i.getUserFrom();
                System.out.println(i.getTransferId() + "\t" + fromOrTo + name + "\t\t\t" + i.getAmount());
            }
        }
        System.out.println("------------------------------\n" +
                "Enter transfer ID to view details (0 to cancel): ");

                Scanner scanner1 = new Scanner(System.in);
                String input = scanner1.nextLine();
                if (Integer.parseInt(input) != 0) {
                    boolean foundTransferId = false;
                    for (DisplayTransfer i : transfers) {
                        if (Integer.parseInt(input) == i.getTransferId()) {
                            DisplayTransfer temp = restTemplate.exchange(baseUrl + "transfer/" + i.getTransferId(), HttpMethod.GET, makeAuthEntity(), DisplayTransfer.class).getBody();
                            foundTransferId = true;
                            System.out.println("------------------------------\n +" +
                            "Transfer Details\n" +
                            "------------------------------\n" +
                            "Id: " + temp.getTransferId() + "\n" +
                            "From: " + temp.getUserFrom() + "\n" +
                            "To : " + temp.getUserTo() + "\n" +
                            "Type: " + temp.getTransferTypeId() + "\n" +
                            "Status: " + temp.getTransferStatusId() + "\n" +
                            "Amount: $" + temp.getAmount());
                        }
                    }
                    if (!foundTransferId) {
                        System.out.println("not a valid transfer ID");
                    }
                }
//            }catch (Exception e){
//                System.out.println("Something went wrong");
            }





    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }
}
