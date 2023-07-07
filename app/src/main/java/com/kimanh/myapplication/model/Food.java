package com.kimanh.myapplication.model;

public class Food {
    private String mamonan;
    private String tenmonan;
    private String diachi;
    private String chongoi;
    private String loai;
    private String anhmonan;

    public Food(String mamonan, String tenmonan, String diachi, String chongoi, String loai, String anhmonan) {
        this.mamonan = mamonan;
        this.tenmonan = tenmonan;
        this.diachi = diachi;
        this.chongoi = chongoi;
        this.loai = loai;
        this.anhmonan = anhmonan;
    }

    public Food() {

    }

    public String getMamonan() {
        return mamonan;
    }

    public void setMamonan(String mamonan) {
        this.mamonan = mamonan;
    }

    public String getTenmonan() {
        return tenmonan;
    }

    public void setTenmonan(String tenmonan) {
        this.tenmonan = tenmonan;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getChongoi() {
        return chongoi;
    }

    public void setChongoi(String chongoi) {
        this.chongoi = chongoi;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getAnhmonan() {
        return anhmonan;
    }

    public void setAnhmonan(String anhmonan) {
        this.anhmonan = anhmonan;
    }

    @Override
    public String toString() {
        return mamonan+"-"+tenmonan+"-"+diachi+"-"+chongoi+"-"+loai+"-"+anhmonan;
    }
}

