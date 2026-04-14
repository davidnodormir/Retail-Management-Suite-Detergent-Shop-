package entity;

import java.util.Date;

public class EntitySconto {
	
	private int idSconto;
	private float Percentuale;
	private Date Scadenza;
	
	//Costruttore
	public EntitySconto(int idSconto, float percentuale, Date scadenza) {
		super();
		this.idSconto = idSconto;
		Percentuale = percentuale;
		Scadenza = scadenza;
	}

	
	//Metodi get e set
	public int getIdSconto() {
		return idSconto;
	}

	public void setIdSconto(int idSconto) {
		this.idSconto = idSconto;
	}

	public float getPercentuale() {
		return Percentuale;
	}

	public void setPercentuale(float percentuale) {
		Percentuale = percentuale;
	}

	public Date getScadenza() {
		return Scadenza;
	}

	public void setScadenza(Date scadenza) {
		Scadenza = scadenza;
	}
	

}
