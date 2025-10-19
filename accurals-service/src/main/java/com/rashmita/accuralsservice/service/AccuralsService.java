package com.rashmita.accuralsservice.service;
import java.time.LocalDate;

public interface AccuralsService {
 void saveInterest(String loanNumber, Long emiId, LocalDate today,double dailyInterest,int emiMonth);
 void savePenalty(String loanNumber, Long emiId, LocalDate today,double penaltyInterest,int emiMonth);
 void saveLateFee(String loanNumber, Long emiId, LocalDate today,double lateFee,int emiMonth);
 void saveOverDue(String loanNumber, Long emiId, LocalDate today,double overdueInterest,int emiMonth);
}