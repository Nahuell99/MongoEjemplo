package com.nahuel.mongodb;

import java.util.Objects;

public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleVenta(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario * cantidad;
    }

	public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = precioUnitario * cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	    this.subtotal = precioUnitario * cantidad;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(cantidad, precioUnitario, producto, subtotal);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleVenta other = (DetalleVenta) obj;
		return cantidad == other.cantidad
				&& Double.doubleToLongBits(precioUnitario) == Double.doubleToLongBits(other.precioUnitario)
				&& Objects.equals(producto, other.producto)
				&& Double.doubleToLongBits(subtotal) == Double.doubleToLongBits(other.subtotal);
	}

	@Override
	public String toString() {
		return "DetalleVenta [producto=" + producto + ", cantidad=" + cantidad + ", subtotal=" + subtotal
				+ ", precioUnitario=" + precioUnitario + "]";
	}

	
}
