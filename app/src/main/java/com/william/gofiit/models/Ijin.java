package com.william.gofiit.models;

public class Ijin {
    private int idInstruktur;
    private int idPengganti;
    private String tanggalPengajuan;
    private String tanggalIjin;
    private String tanggalKonfirmasi;
    private String status;
    private String keterangan;

    public Ijin(int idInstruktur, int idPengganti, String tanggalPengajuan, String tanggalIjin, String tanggalKonfirmasi, String status, String keterangan) {
        this.idInstruktur = idInstruktur;
        this.idPengganti = idPengganti;
        this.tanggalPengajuan = tanggalPengajuan;
        this.tanggalIjin = tanggalIjin;
        this.tanggalKonfirmasi = tanggalKonfirmasi;
        this.status = status;
        this.keterangan = keterangan;
    }

    public int getIdInstruktur() {
        return idInstruktur;
    }

    public int getIdPengganti() {
        return idPengganti;
    }

    public String getTanggalPengajuan() {
        return tanggalPengajuan;
    }

    public String getTanggalIjin() {
        return tanggalIjin;
    }

    public String getTanggalKonfirmasi() {
        return tanggalKonfirmasi;
    }

    public String getStatus() {
        return status;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
