package com.automarket_app.VO;

// 회원가입 유저

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
//public class UserVO implements Parcelable{
public class UserVO {

    private String userid; // 유저 아이디
    private String email; // 이메일
    private String pwd; // 비밀번호
    private int cashamt; // 캐시
    private String adminflag; // 관리자 여부
    private String regdate; // 가입일
    private String deviceid; // 디바이스 mac 주소
    private String name; // 이름


//    // CREATOR 라고 불리는 static 상수를 반드시 정의
//    public static final Parcelable.Creator<UserVO> CREATOR = new Parcelable.Creator<UserVO>() {
//        @Override
//        public UserVO createFromParcel(Parcel parcel) {
//            // 마샬링된 데이터를 언마샬링(복원)할 때 사용되는 method
//            return new UserVO(parcel);
//        }
//
//        @Override
//        public UserVO[] newArray(int i) {
//            return new UserVO[i];
//        }
//    };

    // default constructor
    public UserVO() {
    }

    public UserVO(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }

    // 모든 field를 인자로 받는 constructor
    public UserVO(String userid, String email, String pwd, int cashamt, String adminflag, String regdate, String deviceid, String name) {
        this.userid = userid;
        this.email = email;
        this.pwd = pwd;
        this.cashamt = cashamt;
        this.adminflag = adminflag;
        this.regdate = regdate;
        this.deviceid = deviceid;
        this.name = name;
    }

//    // 복원작업 할때 사용되는 생성자.
//    // 복원시 제일 신경 써야되는 부분은 순서...
//    // (마샬링 순서와 언마샬링 순서가 동일해야 한다.)
//    protected UserVO(Parcel parcel){
//        userid = parcel.readString();
//        email = parcel.readString();
//        pwd = parcel.readString();
//        cashamt = parcel.readInt();
//        adminflag = parcel.readString();
//        regdate = parcel.readString();
//        deviceid = parcel.readString();
//        name = parcel.readString();
//    }
//
//    // override method
//
//    // 수정할 필요 없다!!
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    // 마샬링하는 역할을 하는 method / 데이터 변환
//    // 데이터를 변화하는 순서와 복원하는 순서가 반드시 같아야 한다.
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//
//        try{
//            // 순서 맞춰가면서 해야함!!
//            parcel.writeString(userid);
//            parcel.writeString(email);
//            parcel.writeString(pwd);
//            parcel.writeInt(cashamt);
//            parcel.writeString(adminflag);
//            parcel.writeString(regdate);
//            parcel.writeString(deviceid);
//            parcel.writeString(name);
//
//        }catch (Exception e){
//            Log.i("automarket_app", e.toString());
//        }
//    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getCashamt() {
        return cashamt;
    }

    public void setCashamt(int cashamt) {
        this.cashamt = cashamt;
    }

    public String getAdminflag() {
        return adminflag;
    }

    public void setAdminflag(String adminflag) {
        this.adminflag = adminflag;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public static Creator<UserVO> getCREATOR() {
//        return CREATOR;
//    }

    @Override
    public String toString() {
        return "UserVO{" +
                "userid='" + userid + '\'' +
                ", email='" + email + '\'' +
                ", pwd='" + pwd + '\'' +
                ", cashamt=" + cashamt +
                ", adminflag='" + adminflag + '\'' +
                ", regdate='" + regdate + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
