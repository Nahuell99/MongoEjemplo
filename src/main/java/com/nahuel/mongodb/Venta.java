package com.nahuel.mongodb;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Venta {
    private String idVenta;
    private String nroTicket; // Ej: 0001-000123
    private Date fecha;
    private double total;
    private FormaPago formaPago;
    private Cliente cliente;
    private Empleado empleadoVenta;
    private Empleado empleadoAtencion;
    private List<DetalleVenta> productosVendidos;
    private Sucursal sucursalVenta;

    
	public Venta(String idVenta, String nroTicket, Date fecha, FormaPago formaPago, Cliente cliente,
			Empleado empleadoVenta, Empleado empleadoAtencion, List<DetalleVenta> productosVendidos,
			Sucursal sucursalVenta) {
		super();
		this.idVenta = idVenta;
		this.nroTicket = nroTicket;
		this.fecha = fecha;
		this.formaPago = formaPago;
		this.cliente = cliente;
		this.empleadoVenta = empleadoVenta;
		this.empleadoAtencion = empleadoAtencion;
		this.productosVendidos = productosVendidos;
		this.sucursalVenta = sucursalVenta;
		recalcularTotal(); // 🔁 Calcula total al crear
	}
	

	public Sucursal getSucursalVenta() {
		return sucursalVenta;
	}

	public void setSucursalVenta(Sucursal sucursalVenta) {
		this.sucursalVenta = sucursalVenta;
	}

	public String getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(String idVenta) {
		this.idVenta = idVenta;
	}

	public enum FormaPago {
        EFECTIVO, TARJETA, DEBITO
    }

	public String getNroTicket() {
		return nroTicket;
	}

	public void setNroTicket(String nroTicket) {
		this.nroTicket = nroTicket;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getTotal() {
		return total;
	}

	public FormaPago getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPago formaPago) {
		this.formaPago = formaPago;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Empleado getEmpleadoVenta() {
		return empleadoVenta;
	}

	public void setEmpleadoVenta(Empleado empleadoVenta) {
		this.empleadoVenta = empleadoVenta;
	}
	
	public Empleado getEmpleadoAtencion() {
		return empleadoAtencion;
	}

	public void setEmpleadoAtencion(Empleado empleadoAtencion) {
		this.empleadoAtencion = empleadoAtencion;
	}

	public List<DetalleVenta> getProductosVendidos() {
		return productosVendidos;
	}

	public void setProductosVendidos(List<DetalleVenta> productosVendidos) {
        this.productosVendidos = productosVendidos;
        recalcularTotal(); // 🔁 Calcula al asignar productos
    }

	// 🔁 Este método recalcula el total sumando los subtotales
    public void recalcularTotal() {
        if (productosVendidos == null) {
            total = 0;
        } else {
            total = productosVendidos.stream()
                    .mapToDouble(DetalleVenta::getSubtotal)
                    .sum();
        }
    }
    
    // ➕ Método extra para actualizar un ítem y recalcular el total si hace falta
    public void actualizarItem(int index, DetalleVenta nuevoDetalle) {
        productosVendidos.set(index, nuevoDetalle);
        recalcularTotal();
    }
	
	
	@Override
	public int hashCode() {
		return Objects.hash(cliente, empleadoAtencion, empleadoVenta, fecha, formaPago, idVenta, nroTicket,
				productosVendidos, sucursalVenta, total);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Venta other = (Venta) obj;
		return Objects.equals(cliente, other.cliente) && Objects.equals(empleadoAtencion, other.empleadoAtencion)
				&& Objects.equals(empleadoVenta, other.empleadoVenta) && Objects.equals(fecha, other.fecha)
				&& formaPago == other.formaPago && Objects.equals(idVenta, other.idVenta)
				&& Objects.equals(nroTicket, other.nroTicket)
				&& Objects.equals(productosVendidos, other.productosVendidos)
				&& Objects.equals(sucursalVenta, other.sucursalVenta)
				&& Double.doubleToLongBits(total) == Double.doubleToLongBits(other.total);
	}

	@Override
	public String toString() {
		return "Venta [idVenta=" + idVenta + ", nroTicket=" + nroTicket + ", fecha=" + fecha + ", total=" + total
				+ ", formaPago=" + formaPago + ", cliente=" + cliente + ", empleadoVenta=" + empleadoVenta
				+ ", empleadoAtencion=" + empleadoAtencion + ", productosVendidos=" + productosVendidos
				+ ", sucursalVenta=" + sucursalVenta + "]";
	}

	
    
}
