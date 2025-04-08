package com.my.shippingrate.dto.response.citylink;

import lombok.Data;

@Data
public class DataDTO {
    private int rate;
    private String code;
    private int api_days;
    private int final_days;
    private String dayString;
    private int weekendDays;
    private String message;
}
