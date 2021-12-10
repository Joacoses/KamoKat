package com.example.dabebel.registro;

public class POJO_MONEDERO {

    private double saldo;
    private double puntos;
    private double puntosDinero;
    private double total;

    public POJO_MONEDERO() {}

    public POJO_MONEDERO(double saldo,double puntos,double puntosDinero, double total){
        this.puntos = puntos;
        this.saldo = saldo;
        this.puntosDinero = puntosDinero;
        this.total = total;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }

    public double getPuntosDinero() {
        return puntosDinero;
    }

    public void setPuntosDinero(double puntosDinero) {
        this.puntosDinero = puntosDinero;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
