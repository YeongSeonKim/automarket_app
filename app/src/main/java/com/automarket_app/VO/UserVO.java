package com.automarket_app.VO;

// 회원가입 유저

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO {

    private String email; // 이메일
    private String name; // 이름
    private String pwd; // 비밀번호
    private String adminflag; // 관리자 여부
    private String deviceid; // 디바이스 mac 주소
    private int cashamt; // 캐시

    public UserVO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAdminflag() {
        return adminflag;
    }

    public void setAdminflag(String adminflag) {
        this.adminflag = adminflag;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public int getCashamt() {
        return cashamt;
    }

    public void setCashamt(int cashamt) {
        this.cashamt = cashamt;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", adminflag='" + adminflag + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", cashamt=" + cashamt +
                '}';
    }
}
