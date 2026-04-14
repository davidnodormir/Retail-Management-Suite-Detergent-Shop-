package entity;
import java.io.Serializable;

public class EntityCliente implements Serializable {
	
	private int idCliente;
    private String nomeUtente;
	private String password;
	private String cellulare;
	private String cartaDiCredito;
	
	
    public int getIdCliente() {
		return idCliente;
	}
	public String getNomeUtente() {
		return nomeUtente;
	}
	public String getPassword() {
		return password;
	}
	public String getCellulare() {
		return cellulare;
	}
	public String getCartaDiCredito() {
		return cartaDiCredito;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
	public void setCartaDiCredito(String cartaDiCredito) {
		this.cartaDiCredito = cartaDiCredito;
	}
	public EntityCliente(int idCliente, String nomeUtente, String password, String cellulare, String cartaDiCredito) {
		super();
		this.idCliente = idCliente;
		this.nomeUtente = nomeUtente;
		this.password = password;
		this.cellulare = cellulare;
		this.cartaDiCredito = cartaDiCredito;
	}

	
    
}
