package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class EntitySpesa {
    private int idSpesa;
	private Date Data;
	private float costoTotale;
	private StatoSpesa Stato;
	private int idSconto;
	private int idCliente;
	
    private List<EntityDettaglioSpesa> prodotti = new ArrayList<>();
    
  //Costruttore 
    public EntitySpesa(int idSpesa, Date data, float costoTotale, StatoSpesa stato, int idSconto, int idCliente,
			List<EntityDettaglioSpesa> prodotti) {
		super();
		this.idSpesa = idSpesa;
		this.Data = data;
		this.costoTotale = costoTotale;
		this.Stato = stato;
		this.idSconto = idSconto;
		this.idCliente = idCliente;
		this.prodotti = prodotti;
	}

	
    
    
	public int getIdSpesa() {
		return idSpesa;
	}

	public Date getData() {
		return Data;
	}

	public float getCostoTotale() {
		return costoTotale;
	}

	public StatoSpesa getStato() {
		return Stato;
	}

	public int getIdSconto() {
		return idSconto;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public List<EntityDettaglioSpesa> getProdotti() {
		return prodotti;
	}

	public void setIdSpesa(int idSpesa) {
		this.idSpesa = idSpesa;
	}

	public void setData(Date data) {
		Data = data;
	}

	public void setCostoTotale(float costoTotale) {
		this.costoTotale = costoTotale;
	}

	public void setStato(StatoSpesa stato) {
		Stato = stato;
	}

	public void setIdSconto(int idSconto) {
		this.idSconto = idSconto;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public void setProdotti(List<EntityDettaglioSpesa> prodotti) {
		this.prodotti = prodotti;
	}
    
    
}
