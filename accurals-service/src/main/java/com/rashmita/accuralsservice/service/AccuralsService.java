package com.rashmita.accuralsservice.service;
import java.time.LocalDate;

public interface AccuralsService {
 void saveInterest(String loanNumber, Long emiId, LocalDate today,double dailyInterest);
 void savePenalty(String loanNumber, Long emiId, LocalDate today,double penaltyInterest);
 void saveLateFee(String loanNumber, Long emiId, LocalDate today,double lateFee);
 void saveOverDue(String loanNumber, Long emiId, LocalDate today,double overdueInterest);
}