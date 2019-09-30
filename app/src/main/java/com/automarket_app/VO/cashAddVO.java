package com.automarket_app.VO;

// 캐시충전
public class cashAddVO {

    private String userId;   // 사용자 아이디
    private int chargePrice; // 충전금액
    private  int balance;    // 잔액

    public cashAddVO() {
    }

    public cashAddVO(String userId, int chargePrice, int balance) {
        this.userId = userId;
        this.chargePrice = chargePrice;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getChargePrice() {
        return chargePrice;
    }

    public void setChargePrice(int chargePrice) {
        this.chargePrice = chargePrice;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "cashAddVO{" +
                "userId='" + userId + '\'' +
                ", chargePrice=" + chargePrice +
                ", balance=" + balance +
                '}';
    }
}
