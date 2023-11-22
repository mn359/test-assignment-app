package com.example.testassignmentapp.transaction;

public record TransactionReportRequest(String from,
                                       String to,
                                       String currency) {
}
