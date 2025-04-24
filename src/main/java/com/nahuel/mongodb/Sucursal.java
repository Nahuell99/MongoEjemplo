package com.nahuel.mongodb;

import java.util.Objects;

public class Sucursal {
    private String idSucursal;
    private String domicilioCalle;
    private String domicilioNumero;
    private String localidad;
    private String provincia;
    
	public Sucursal(String idSucursal, String domicilioCalle, String domicilioNumero, String localidad,
			String provincia) {
		super();
		this.idSucursal = idSucursal;
		this.domicilioCalle = domicilioCalle;
		this.domicilioNumero = domicilioNumero;
		this.localidad = localidad;
		this.provincia = provincia;
	}
	
	public String getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(String idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getDomicilioCalle() {
		return domicilioCalle;
	}
	public void setDomicilioCalle(String domicilioCalle) {
		this.domicilioCalle = domicilioCalle;
	}
	public String getDomicilioNumero() {
		return domicilioNumero;
	}
	public void setDomicilioNumero(String domicilioNumero) {
		this.domicilioNumero = domicilioNumero;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	@Override
	public int hashCode() {
		return Objects.hash(domicilioCalle, domicilioNumero, idSucursal, localidad, provincia);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sucursal other = (Sucursal) obj;
		return Objects.equals(domicilioCalle, other.domicilioCalle)
				&& Objects.equals(domicilioNumero, other.domicilioNumero)
				&& Objects.equals(idSucursal, other.idSucursal) && Objects.equals(localidad, other.localidad)
				&& Objects.equals(provincia, other.provincia);
	}
	@Override
	public String toString() {
		return "Sucursal [idSucursal=" + idSucursal + ", domicilioCalle=" + domicilioCalle + ", domicilioNumero="
				+ domicilioNumero + ", localidad=" + localidad + ", provincia=" + provincia + "]";
	}
	
}
