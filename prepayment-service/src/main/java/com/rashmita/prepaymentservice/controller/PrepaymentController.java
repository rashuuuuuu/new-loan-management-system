package com.rashmita.prepaymentservice.controller;

import com.rashmita.commoncommon.model.PrepaymentInquiryRequestModel;
import com.rashmita.commoncommon.model.PrepaymentInquiryResponseModel;
import com.rashmita.commoncommon.model.PrepaymentRequest;
import com.rashmita.commoncommon.model.PrepaymentResponse;
import com.rashmita.prepaymentservice.service.PrepaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prepayment")
@RequiredArgsConstructor
public class PrepaymentController {
    private final PrepaymentService prepaymentService;
    @PostMapping("/inquiry")
    public PrepaymentInquiryResponseModel prepayment(@RequestBody PrepaymentInquiryRequestModel prepaymentInquiryRequestModel) {
        return prepaymentService.prepaymentInquiry(prepaymentInquiryRequestModel);
    }
    @PostMapping("/create")
    public PrepaymentResponse createPrepayment(@RequestBody PrepaymentRequest prepaymentInquiryRequestModel) {
        return prepaymentService.createPrepayment(prepaymentInquiryRequestModel);
    }

}
