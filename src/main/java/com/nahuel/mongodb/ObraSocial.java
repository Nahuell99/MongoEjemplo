package com.nahuel.mongodb;
import java.util.Objects;

public class ObraSocial {
    private String idObraSocial;
    private String nombre;   
    
	public ObraSocial(String idObraSocial, String nombre) {
		super();
		this.idObraSocial = idObraSocial;
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idObraSocial, nombre);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObraSocial other = (ObraSocial) obj;
		return Objects.equals(idObraSocial, other.idObraSocial) && Objects.equals(nombre, other.nombre);
	}
	
	@Override
	public String toString() {
		return "ObraSocial [idObraSocial=" + idObraSocial + ", nombre=" + nombre + "]";
	}
	

}
