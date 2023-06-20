package com.william.gofiit.models;

public class Gym {
    private String nomorBooking;
    private String tanggalBooking;
    private String tanggalGym;
    private String waktuGym;
    private String waktuPresensi;

    public Gym(String nomorBooking,  String tanggalBooking,String tanggalGym, String waktuGym, String waktuPresensi) {
        this.nomorBooking = nomorBooking;
        this.tanggalBooking = tanggalBooking;
        this.tanggalGym = tanggalGym;
        this.waktuGym = waktuGym;
        this.waktuPresensi = waktuPresensi;
    }

    public String getNomorBooking() {
        return nomorBooking;
    }

    public String getTanggalBooking() {
        return waktuGym;
    }

    public String getTanggalGym() {
        return tanggalGym;
    }

    public String getWaktuGym() {
        return tanggalGym;
    }

    public String getWaktuPresensi() {
        return waktuPresensi;
    }
}

