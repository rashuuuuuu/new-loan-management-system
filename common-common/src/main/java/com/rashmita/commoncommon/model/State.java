package com.rashmita.commoncommon.model;


import java.util.List;

public enum State {

    // ***** Loan Apply State Start ****
    PROCESSED,
    REVIEWED,
    BOOKED,
    CONFIRMED,
    DISBURSED;
    // ***** Loan Apply State END ****

    // ***** Prepayment State Start ****

    // ***** Prepayment State END ****


    public static List<State> loanApplyState() {
        return List.of(PROCESSED,
                REVIEWED,
                BOOKED,
                CONFIRMED,
                DISBURSED);
    }
}