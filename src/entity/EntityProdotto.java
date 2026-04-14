package entity;
public class EntityProdotto {
	
	
	private int codice;
	private String nome;
	private float prezzo;
	private String descrizione;
	private int qtaDisponibile;
	
	//costruttore con codice
    public EntityProdotto(int codice, String nome, float prezzo, String descrizione, int qtaDisponibile) {
        this.codice = codice;
        if (nome != null) {
            this.nome = nome.trim();
        } else {
            this.nome = "";
        }
        this.prezzo = Math.max(0, prezzo);
        if (descrizione != null)
        	this.descrizione = descrizione.trim();
        else
        	this.descrizione = "";
        this.qtaDisponibile = Math.max(0, qtaDisponibile);
    }
    
    //costruttore senza codice
    public EntityProdotto(String nome, float prezzo, String descrizione, int qtaDisponibile) {
    	if (nome != null) {
    	    this.nome = nome.trim();
    	} else {
    	    this.nome = "";
    	}
        this.prezzo = Math.max(0, prezzo);
        if (descrizione != null)
        	this.descrizione = descrizione.trim();
        else
        	this.descrizione = "";
        this.qtaDisponibile = Math.max(0, qtaDisponibile);
    }
	
	public int getCodice() {
		return codice;
	}
	public String getNome() {
		return nome;
	}
	public float getPrezzo() {
		return prezzo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public int getQtaDisponibile() {
		return qtaDisponibile;
	}
	public void setCodice(int codice) {
		this.codice = codice;
	}
	public void setNome(String nome) {
        if (nome != null) {
            this.nome = nome.trim();
        } else {
            this.nome = "";
        }
	}
	public void setPrezzo(float prezzo) {
		this.prezzo = Math.max(0, prezzo);
	}
	public void setDescrizione(String descrizione) {
        if (descrizione != null)
        	this.descrizione = descrizione.trim();
        else
        	this.descrizione = "";
	}
	public void setQtaDisponibile(int qtaDisponibile) {
		this.qtaDisponibile = Math.max(0, qtaDisponibile);
	}
	
	public String toString() {
        return "Prodotto: " +
               "codice=" + codice +
               ", nome='" + nome + '\'' +
               ", prezzo=" + prezzo +
               ", descrizione='" + descrizione + '\'' +
               ", qtaDisponibile=" + qtaDisponibile +
               ' ';
    }
	
}
