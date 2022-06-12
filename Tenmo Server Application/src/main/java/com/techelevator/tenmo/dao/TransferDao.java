package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.DisplayTransfer;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    public void sendTransfer(Transfer transfers);

    List<DisplayTransfer> getAllTransfers(int id);

    public Transfer getSelectedTransfer(int transferId);
}
