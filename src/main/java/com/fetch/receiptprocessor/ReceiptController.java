package com.fetch.receiptprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("receipts")
@RestController
public class ReceiptController {
    @Autowired
    ReceiptService receiptService;

    @PostMapping("process")
    public ResponseEntity processReceipt(@RequestBody Receipt receipt) {
        if (receipt.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long newId = this.receiptService.processReceipt(receipt);
        return new ResponseEntity<>(newId, HttpStatus.OK);
    }

    @GetMapping("{id}/points")
    public ResponseEntity getPointsFromReceipt(@PathVariable Long id) {
        Optional<Receipt> receipt = this.receiptService.findReceiptById(id);
        if (receipt.isPresent()) {
//            this.receiptService.calculatePoints(id);
            return new ResponseEntity<>(receipt.get().getPoints(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
