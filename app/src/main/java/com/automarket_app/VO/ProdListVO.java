package com.automarket_app.VO;

import java.util.List;

// 상품목록조회
public class ProdListVO {

    private List<ProductVO> prodList; // prodList : List

    // alt + insert
    public ProdListVO(){
    }

    public ProdListVO(List<ProductVO> prodList) {
        this.prodList = prodList;
    }

    public List<ProductVO> getProdList() {
        return prodList;
    }

    public void setProdList(List<ProductVO> prodList) {
        this.prodList = prodList;
    }

    @Override
    public String toString() {
        return "ProdListVO{" +
                "prodList=" + prodList +
                '}';
    }
}