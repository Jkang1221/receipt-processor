package com.example.receiptprocessor.model;

public class PointsResponse {
    private int points;

    public PointsResponse(int points) {
        this.points = points;
    }

    // Getter and Setter
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
}