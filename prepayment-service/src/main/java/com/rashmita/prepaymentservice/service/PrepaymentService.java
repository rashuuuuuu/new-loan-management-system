package com.rashmita.prepaymentservice.service;

import com.rashmita.commoncommon.model.PrepaymentInquiryRequestModel;
import com.rashmita.commoncommon.model.PrepaymentInquiryResponseModel;
import com.rashmita.commoncommon.model.PrepaymentRequest;
import com.rashmita.commoncommon.model.PrepaymentResponse;

public interface PrepaymentService {
    PrepaymentInquiryResponseModel prepaymentInquiry(PrepaymentInquiryRequestModel prepaymentInquiryRequestModel);
    PrepaymentResponse createPrepayment(PrepaymentRequest prepaymentRequest);
}
