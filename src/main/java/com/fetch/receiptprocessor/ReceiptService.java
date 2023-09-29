package com.fetch.receiptprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class ReceiptService {
    @Autowired
    ReceiptRepository receiptRepository;

    public Long processReceipt(Receipt receipt) {
        this.calculatePoints(receipt);
        this.receiptRepository.save(receipt);
        return receipt.getId();
    }

    public Optional<Receipt> findReceiptById(Long id) {
        return this.receiptRepository.findById(id);
    }

    public void calculatePoints(Receipt receipt) {
        int pts = 0;
        //one pt for every alphanumeric character in the retailer name
        int count = 0;
        for (int i = 0; i < receipt.getRetailer().length(); i++) {
            if (Character.isLetterOrDigit(receipt.getRetailer().charAt(i))) {
                count++;
            }
        }
        pts += count;

        //50 pts if total is round dollar amt with no cents
        if (receipt.getTotal() % 1 == 0) {
            pts += 50;
        }
        //25 pts if total is a multiple of .25
        if (receipt.getTotal() % .25 == 0) {
            pts += 25;
        }
        //5 points for every two items on the receipt
        pts += receipt.getItems().size() / 2 * 5;

        //if trimmed length of item description is multiple of 3, multiply price by 0.2 and round to nearest int, \n
        // the result is num of pts earned
        if (!receipt.getItems().isEmpty()) {
            for (ReceiptItem item : receipt.getItems()) {
                if (item.getShortDescription().trim().length() % 3 == 0) {
                    pts += Math.ceil(item.getPrice() * 0.2);
                }
            }
        }

        //6 points if the day in the purchase date is odd
        String purchaseDay = receipt.getPurchaseDate().split("-")[2];
        if (Integer.parseInt(purchaseDay) % 2 != 0){
            pts += 6;
        }

        //10 pts if the time of purchase is after 2PM and before 4pm
        LocalTime parsedTime = LocalTime.parse(receipt.getPurchaseTime());
        LocalTime start = LocalTime.parse("14:00");
        LocalTime end = LocalTime.parse("16:00");
        if (!parsedTime.isBefore(start) && !parsedTime.isAfter(end)){
            pts += 10;
        }

        receipt.setPoints(pts);
    }
}
