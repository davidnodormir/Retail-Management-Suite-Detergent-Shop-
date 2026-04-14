package entity;

public class EntityDettaglioSpesa {
    
    private int idProd;
	private int idSpesa;
	private int QtaCarrello;
	
	
	public EntityDettaglioSpesa (int idSpesa, int idProd, int qtaCarrello) {
	    super();
	    this.idProd = idProd;
	    this.idSpesa = idSpesa;
	    this.QtaCarrello = qtaCarrello;
	}
	public int getIdProd() {
		return idProd;
	}
	public void setIdProd(int idProd) {
		this.idProd = idProd;
	}
	public int getIdSpesa() {
		return idSpesa;
	}
	public void setIdSpesa(int idSpesa) {
		this.idSpesa = idSpesa;
	}
	public int getQtaCarrello() {
		return QtaCarrello;
	}
	public void setQtaCarrello(int qtaCarrello) {
		QtaCarrello = qtaCarrello;
	}
}
