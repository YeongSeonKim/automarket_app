package com.automarket_app.VO;

import java.util.List;

public class UserListVO {

    private List<UserVO> userList;

    public UserListVO() {
    }

    public List<UserVO> getUserList() {
        return userList;
    }

    public void setUserList(List<UserVO> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "UserListVO{" +
                "userList=" + userList +
                '}';
    }
}
