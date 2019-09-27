package com.automarket_app.VO;

import java.util.List;

// 상품목록조회
public class ProdListVO {

    private List<ProdDtailVO> prodList; // prodList : List

    // alt + insert
    public ProdListVO(){
    }

    public ProdListVO(List<ProdDtailVO> prodList) {
        this.prodList = prodList;
    }

    public List<ProdDtailVO> getProdList() {
        return prodList;
    }

    public void setProdList(List<ProdDtailVO> prodList) {
        this.prodList = prodList;
    }

    @Override
    public String toString() {
        return "ProdListVO{" +
                "prodList=" + prodList +
                '}';
    }
}