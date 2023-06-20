package com.william.gofiit.models;

public class Kelas {
    private String nomorBooking;
    private int idJadwalHarian;
    private int idKelas;
    private String tanggalJadwal;
    private String tanggalBooking;
    private String waktuPresensi;

    public Kelas(String nomorBooking, int idJadwalHarian, int idKelas, String tanggalJadwal, String tanggalBooking, String waktuPresensi) {
        this.nomorBooking = nomorBooking;
        this.idJadwalHarian = idJadwalHarian;
        this.idKelas = idKelas;
        this.tanggalJadwal = tanggalJadwal;
        this.tanggalBooking = tanggalBooking;
        this.waktuPresensi = waktuPresensi;
    }

    public String getNomorBooking() {
        return nomorBooking;
    }

    public int getIdJadwalHarian() {
        return idJadwalHarian;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public String getTanggalJadwal() {
        return tanggalJadwal;
    }

    public String getTanggalBooking() {
        return tanggalBooking;
    }

    public String getWaktuPresensi() {
        return waktuPresensi;
    }
}

