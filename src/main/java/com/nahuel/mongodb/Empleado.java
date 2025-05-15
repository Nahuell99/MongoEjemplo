package com.nahuel.mongodb;

import java.util.Objects;

public class Empleado {
    private String idEmpleado;
    private String nombre;
    private String apellido;
    private String dni;
    private String cuil;
    private String direccion_calle_numero;
    private String direccion_localidad;
    private String direccion_provincia;
    private ObraSocial obraSocial;
    private String nroAfiliado;
    private Sucursal sucursal;
    private Boolean encargado;
    
	public Empleado(String idEmpleado, String nombre, String apellido, String dni, String cuil,
			String direccion_calle_numero, String direccion_localidad, String direccion_provincia,
			ObraSocial obraSocial, String nroAfiliado, Sucursal sucursal, Boolean encargado) {
		super();
		this.idEmpleado = idEmpleado;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.cuil = cuil;
		this.direccion_calle_numero = direccion_calle_numero;
		this.direccion_localidad = direccion_localidad;
		this.direccion_provincia = direccion_provincia;
		this.obraSocial = obraSocial;
		this.nroAfiliado = nroAfiliado;
		this.sucursal = sucursal;
		this.encargado = encargado;
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
	public String getCuil() {
		return cuil;
	}
	public void setCuil(String cuil) {
		this.cuil = cuil;
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
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public Boolean getEncargado() {
		return encargado;
	}

	public void setEncargado(Boolean encargado) {
		this.encargado = encargado;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apellido, cuil, dni, idEmpleado, nombre, nroAfiliado, obraSocial, sucursal);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empleado other = (Empleado) obj;
		return Objects.equals(apellido, other.apellido) && Objects.equals(cuil, other.cuil)
				&& Objects.equals(dni, other.dni) && Objects.equals(idEmpleado, other.idEmpleado)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(nroAfiliado, other.nroAfiliado)
				&& Objects.equals(obraSocial, other.obraSocial) && Objects.equals(sucursal, other.sucursal);
	}
	
	@Override
	public String toString() {
		return "Empleado [idEmpleado=" + idEmpleado + ", nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni
				+ ", cuil=" + cuil + ", obraSocial=" + obraSocial + ", nroAfiliado=" + nroAfiliado + ", sucursal="
				+ sucursal + "]";
	}
    
}
