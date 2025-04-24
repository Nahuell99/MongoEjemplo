package com.nahuel.mongodb;

import java.util.Objects;

public class Cliente {
    private String idCliente;
    private String nombre;
    private String apellido;
    private String dni;
    private String direccion_calle_numero;
    private String direccion_localidad;
    private String direccion_provincia;
    private ObraSocial obraSocial; // puede ser null
    private String nroAfiliado;
    
	public Cliente(String idCliente, String nombre, String apellido, String dni, String direccion_calle_numero,
			String direccion_localidad, String direccion_provincia, ObraSocial obraSocial, String nroAfiliado) {
		super();
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.direccion_calle_numero = direccion_calle_numero;
		this.direccion_localidad = direccion_localidad;
		this.direccion_provincia = direccion_provincia;
		this.obraSocial = obraSocial;
		this.nroAfiliado = nroAfiliado;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getDireccion_calle_numero() {
		return direccion_calle_numero;
	}
	public void setDireccion_calle_numero(String direccion_calle_numero) {
		this.direccion_calle_numero = direccion_calle_numero;
	}
	public String getDireccion_localidad() {
		return direccion_localidad;
	}
	public void setDireccion_localidad(String direccion_localidad) {
		this.direccion_localidad = direccion_localidad;
	}
	public String getDireccion_provincia() {
		return direccion_provincia;
	}
	public void setDireccion_provincia(String direccion_provincia) {
		this.direccion_provincia = direccion_provincia;
	}
	public ObraSocial getObraSocial() {
		return obraSocial;
	}
	public void setObraSocial(ObraSocial obraSocial) {
		this.obraSocial = obraSocial;
	}
	public String getNroAfiliado() {
		return nroAfiliado;
	}
	public void setNroAfiliado(String nroAfiliado) {
		this.nroAfiliado = nroAfiliado;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(apellido, dni, idCliente, nombre, nroAfiliado, obraSocial);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(apellido, other.apellido) && Objects.equals(dni, other.dni)
				&& Objects.equals(idCliente, other.idCliente) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(nroAfiliado, other.nroAfiliado) && Objects.equals(obraSocial, other.obraSocial);
	}
	@Override
	public String toString() {
		return "Cliente [idCliente=" + idCliente + ", nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni
				+ ", obraSocial=" + obraSocial + ", nroAfiliado=" + nroAfiliado + "]";
	}

    
}