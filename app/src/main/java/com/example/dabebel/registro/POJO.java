package com.example.dabebel.registro;

public class POJO {
    private String Fecha;
    private String Foto;
    private String Coste;
    private String Duracion;
    private String Estacion;

    public POJO() {}  // Imprescindible si queremos usar el POJO en Firestore

    public POJO(String fecha,String duracion,String coste, String estacion, String foto){
        this.Fecha = fecha;
        this.Foto = foto;
        this.Coste = coste;
        this.Estacion = estacion;
        this.Duracion = duracion;
    }

    public String getCoste() {
        return Coste;
    }

    public void setCoste(String coste) {
        Coste = coste;
    }

    public String getDuracion() {
        return Duracion;
    }

    public void setDuracion(String duracion) {
        Duracion = duracion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getEstacion() {
        return Estacion;
    }

    public void setEstacion(String estacion) {
        Estacion = estacion;
    }

    @Override
    public String toString() {
        return "POJO{" +
                "Fecha='" + Fecha + '\'' +
                ", Foto='" + Foto + '\'' +
                ", Coste='" + Coste + '\'' +
                ", Duracion='" + Duracion + '\'' +
                ", Estacion='" + Estacion + '\'' +
                '}';
    }
}
