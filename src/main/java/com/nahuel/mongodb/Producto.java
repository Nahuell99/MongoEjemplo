package com.nahuel.mongodb;

import java.util.Objects;

public class Producto {
    private String idProducto;
    private String codigo;
    private String descripcion;
    private TipoProducto tipo; // Enum
    private String laboratorio;
    private double precioUnitario;
    private int cantidad;
    
    public Producto(String idProducto, String codigo, String descripcion, TipoProducto tipo, String laboratorio,
			double precioUnitario, int cantidad) {
		super();
		this.idProducto = idProducto;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.laboratorio = laboratorio;
		this.precioUnitario = precioUnitario;
		this.cantidad = cantidad;
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}
	
	public enum TipoProducto {
        MEDICAMENTO, PERFUMERIA
    }

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoProducto getTipo() {
		return tipo;
	}

	public void setTipo(TipoProducto tipo) {
		this.tipo = tipo;
	}

	public String getLaboratorio() {
		return laboratorio;
	}

	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, codigo, descripcion, idProducto, laboratorio, precioUnitario, tipo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return cantidad == other.cantidad && Objects.equals(codigo, other.codigo)
				&& Objects.equals(descripcion, other.descripcion) && Objects.equals(idProducto, other.idProducto)
				&& Objects.equals(laboratorio, other.laboratorio)
				&& Double.doubleToLongBits(precioUnitario) == Double.doubleToLongBits(other.precioUnitario)
				&& tipo == other.tipo;
	}

	@Override
	public String toString() {
		return "Producto [idProducto=" + idProducto + ", codigo=" + codigo + ", descripcion=" + descripcion + ", tipo="
				+ tipo + ", laboratorio=" + laboratorio + ", precioUnitario=" + precioUnitario + ", cantidad="
				+ cantidad + "]";
	}
    
}

