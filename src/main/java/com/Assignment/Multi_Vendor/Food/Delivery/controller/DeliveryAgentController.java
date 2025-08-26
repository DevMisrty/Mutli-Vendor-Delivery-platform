package com.Assignment.Multi_Vendor.Food.Delivery.controller;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.ApiResponse;
import com.Assignment.Multi_Vendor.Food.Delivery.dto.DeliveryAgentDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.DeliveryAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
public class DeliveryAgentController {


    @PostMapping("/newAgent")
    public RequestEntity<ApiResponse<DeliveryAgentDto>> addNewDeliveryAgent(@RequestBody DeliveryAgent agent){
        return null;
    }
}
