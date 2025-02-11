package com.example.receiptprocessor.service;

import com.example.receiptprocessor.model.Receipt;
import com.example.receiptprocessor.model.Item;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ReceiptService {
    private Map<String, Receipt> receipts = new HashMap<>();

    public String processReceipt(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        receipts.put(id, receipt);
        return id;
    }

    public Integer  getPoints(String id) {
        Receipt receipt = receipts.get(id);
        if (receipt == null) {
            return null;
        }
        return calculatePoints(receipt);
    }

    private int calculatePoints(Receipt receipt) {
        int points = 0;
        // One point for every alphanumeric character in the retailer name.
        points += receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();
        double total = Double.parseDouble(receipt.getTotal());
        // 50 points if the total is a round dollar amount with no cents.
        if (total == Math.floor(total)) {
            points += 50;
        }
        // 25 points if the total is a multiple of 0.25.
        if (total % 0.25 == 0) {
            points += 25;
        }
        // 5 points for every two items on the receipt.
        int itemCount = receipt.getItems().size();
        points += (itemCount / 2) * 5;
        // If the trimmed length of the item description is a multiple of 3,
        // multiply the price by 0.2 and round up to the nearest integer.
        // The result is the number of points earned.
        for (Item item : receipt.getItems()) {
            String desc = item.getShortDescription().trim();
            if (desc.length() % 3 == 0) {
                double price = Double.parseDouble(item.getPrice());
                int itemPoints = (int) Math.ceil(price * 0.2);
                points += itemPoints;
            }
        }
        // 6 points if the day in the purchase date is odd.
        String[] dateParts = receipt.getPurchaseDate().split("-");
        int day = Integer.parseInt(dateParts[2]);
        if (day % 2 != 0) {
            points += 6;
        }
        // 10 points if the time of purchase is after 2:00pm and before 4:00pm.
        String[] timeParts = receipt.getPurchaseTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        if (hour >= 14 && hour < 16) {
            points += 10;
        }

        return points;
    }
}