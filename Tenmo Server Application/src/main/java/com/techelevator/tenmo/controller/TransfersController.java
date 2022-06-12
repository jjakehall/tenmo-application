package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.DisplayTransfer;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransfersController {

    @Autowired
    private TransferDao transfersDao;

    @PostMapping("/transfer")
    public boolean makeTransfer(@Valid @RequestBody Transfer transfer) {
        System.out.println("Making transfer");
        transfersDao.sendTransfer(transfer);

        return true;
    }

    @GetMapping("account/transfer/{id}")
    public List<DisplayTransfer> getAllTransfers(@PathVariable int id) {
        List<DisplayTransfer> results = transfersDao.getAllTransfers(id);

        return results;
    }

    @GetMapping("transfer/{id}")
    public Transfer getSelectedTransfer(@PathVariable("id") int transferId) {
        Transfer transfer = transfersDao.getSelectedTransfer(transferId);

        return transfer;
    }

}
