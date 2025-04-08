package com.my.shippingrate.dto.response.citylink;

import lombok.Data;

import java.util.List;

@Data
public class RequestDTO {
    private DataDTO data;
    private int status;
    private List<String> headers;
}
