package com.fetch.receiptprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RequestMapping("receipts")
@RestController
public class ReceiptController {
    @Autowired
    ReceiptService receiptService;

    @PostMapping("process")
    public ResponseEntity processReceipt(@RequestBody Receipt receipt) {
        if (receipt.getId() != null) {
            logger.error("Failed to process receipt. No ID should be supplied with request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long newId = this.receiptService.processReceipt(receipt);
        logger.info(receipt.getRetailer() + " receipt successfully processed with ID: " + newId);
        Map<String, Long> resp = new HashMap<>();
        resp.put("id",newId);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("{id}/points")
    public ResponseEntity getPointsFromReceipt(@PathVariable Long id) {
        Optional<Receipt> receipt = this.receiptService.findReceiptById(id);
        if (receipt.isPresent()) {
//            this.receiptService.calculatePoints(id);
            logger.info("Successfully retrieved points");
            Map<String, Integer> resp = new HashMap<>();
            resp.put("points",receipt.get().getPoints());
//            return new ResponseEntity<>(receipt.get().getPoints(), HttpStatus.OK);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } else {
            logger.info("Could not find receipt in database");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    Logger logger = LoggerFactory.getLogger(ReceiptController.class);
}
