package com.automarket_app.VO;

// 사용자목록

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO {

    private int cashamt;
    private String name;
    private String userid;
    private String email;
//    private String


}
