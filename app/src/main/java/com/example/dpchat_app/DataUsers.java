package com.example.dpchat_app;

public class DataUsers {
    private String thuocTinh;
    private String descripsion;

    public DataUsers(String thuocTinh, String descripsion) {
        this.thuocTinh = thuocTinh;
        this.descripsion = descripsion;
    }

    public String getThuocTinh() {
        return thuocTinh;
    }

    public void setThuocTinh(String thuocTinh) {
        this.thuocTinh = thuocTinh;
    }

    public void setDescripsion(String descripsion) {
        this.descripsion = descripsion;
    }

    public String getDescripsion() {
        return descripsion;
    }
}
