package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import boundary.BoundaryCliente;

import database.ProdottoDAO;
import database.SpesaDAO;
import database.ClienteDAO;
import database.DettaglioSpesaDAO;
import database.ScontoDAO;
import entity.StatoSpesa;

import entity.EntityProdotto;
import entity.EntityCliente;
import entity.EntityDettaglioSpesa;
import entity.EntitySpesa;
import exception.DAOException;
import exception.DBConnectionException;
import exception.OperationException;


public class GestioneNegozio {
	
    private static GestioneNegozio gN = null;
    private ArrayList<EntityProdotto> prodottiTrovati;
    private static final float PREZZO_MAX = 999_999f;
    private static final int QTA_MAX = 999_999;
	private static final int SOGLIA_CLIENTE_ABITUALE = 3;  //parametro scelto dall'impiegato '
    
    //singleton
	protected GestioneNegozio() {
		
	}
	
	public static GestioneNegozio getInstanceOf() {
		if (gN == null) 
			gN = new GestioneNegozio(); 

		return gN;
	}
	
	    //caso d'uso: AggiungiAlCatalogo
	
		//Cerca prodotti nel database con lo stesso nome (case-insensitive)
	   public ArrayList<String> cercaProdottiOmonimi(String nome) throws OperationException {
	        validateNomeProdotto(nome);
	        
	        try {
	            prodottiTrovati = ProdottoDAO.trovaProdottoPerNome(nome.trim());
	            ArrayList<String> risultati = new ArrayList<>();
	            
	            for (EntityProdotto p : prodottiTrovati) {
	                if (p != null) {
	                    risultati.add(p.getCodice() + "|" + p.getNome() + "|" + 
	                                p.getDescrizione() + "|" + p.getPrezzo() + "|" + 
	                                p.getQtaDisponibile());
	                }
	            }
	            return risultati;
	            
	        } catch (DBConnectionException dbEx) {
	            throw new OperationException("Problema di connessione al database!");
	        } catch (DAOException ex) {
	            throw new OperationException("Non riesco a eseguire operazioni sul database!");
	        }
	    }
	    
	   	//Controlla che il codice prodotto passato sia presenta nella lista prodottiTrovati, se non è vuota
	    public boolean isValidCodice(int codice) {
	        if (prodottiTrovati == null || prodottiTrovati.isEmpty()) {
	            return false;
	        }
	        
	        for (EntityProdotto p : prodottiTrovati) {
	            if (p != null && p.getCodice() == codice) {
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    //Aggiorna un prodotto già esistente nel database, identificato dal codice, con nuove informazioni
	    
	    public boolean aggiornaProdotto(int codice, String nuovaDescrizione, 
	                                  float nuovoPrezzo, int nuovaQta) throws OperationException {
	        if (prodottiTrovati == null || prodottiTrovati.isEmpty()) {
	            throw new OperationException("Nessun prodotto caricato");
	        }
	        validateDescrizione(nuovaDescrizione);
	        validatePrezzoQta(nuovoPrezzo, nuovaQta);
	        
	        EntityProdotto prodottoDaAggiornare = null;
	        for (EntityProdotto p : prodottiTrovati) {
	            if (p != null && p.getCodice() == codice) {
	                prodottoDaAggiornare = p;
	                break;
	            }
	        }
	        
	        if (prodottoDaAggiornare == null) {
	            throw new OperationException("Prodotto con codice " + codice + " non trovato");
	        }
	        
	        try {
	            prodottoDaAggiornare.setDescrizione(nuovaDescrizione);
	            prodottoDaAggiornare.setPrezzo(nuovoPrezzo);
	            prodottoDaAggiornare.setQtaDisponibile(nuovaQta);
	            
	            return ProdottoDAO.updateProdotto(prodottoDaAggiornare);
	            
	        } catch (DBConnectionException dbEx) {
	            throw new OperationException("Riscontrato problema interno applicazione!");
	        } catch (DAOException ex) {
	            throw new OperationException("Errore aggiornamento prodotto");
	        }
	    }
	    
	    //Aggiunge un nuovo prodotto nel database
	    
	    public boolean aggiungiNuovoProdotto(String nome, String descrizione, 
	                                       float prezzo, int qtaDisponibile) throws OperationException {
	        validateNomeProdotto(nome);
	        validatePrezzoQta(prezzo, qtaDisponibile);
	        validateDescrizione(descrizione);
	        
	        try {
	            EntityProdotto nuovoProdotto = new EntityProdotto(nome.trim(), prezzo, 
	                                                             descrizione != null ? descrizione.trim() : "", 
	                                                             qtaDisponibile);
	            ProdottoDAO.createProdotto(nuovoProdotto);
	            return true;
	            
	        } catch (DBConnectionException dbEx) {
	            throw new OperationException("Riscontrato problema interno applicazione!");
	        } catch (DAOException ex) {
	            throw new OperationException("Errore creazione prodotto");
	        }
	    }
	    
	    private void validateNomeProdotto(String nome) throws OperationException {
	        if (nome == null || nome.trim().isEmpty()) {
	            throw new OperationException("Il nome del prodotto è obbligatorio");
	        }
	        
	        if (nome.trim().length() > 100) {
	            throw new OperationException("Il nome del prodotto non può superare i 100 caratteri");
	        }
	    }
	    
	    private void validateDescrizione(String descrizione) throws OperationException {
	    	if (descrizione.length() > 400) {
	    		throw new OperationException("La descrizione del prodotto non può superare i 400 caratteri.");
	    	}
	    }
	    
	    private void validatePrezzoQta(float prezzo, int quantita) throws OperationException {
	        if (prezzo < 0) {
	            throw new OperationException("Il prezzo non può essere negativo");
	        }
	        
	        if (prezzo > PREZZO_MAX) {
	            throw new OperationException("Il prezzo non può superare " + PREZZO_MAX);
	        }
	        
	        if (quantita < 0) {
	            throw new OperationException("La quantità non può essere negativa");
	        }
	        
	        if (quantita > QTA_MAX) {
	            throw new OperationException("La quantità non può superare " + QTA_MAX);
	        }
	    }
	    
	    
	

	
	
	// Caso d'uso: AggiungiProdottoCarrello

	// Metodo che verifica l'esistenza di un prodotto nel database dato il suo ID.
	public static String EsistenzaProdotto (String idProdotto) {
	    
	    // Converte l'ID da stringa a intero
	    int idProd = Integer.parseInt(idProdotto);
	    
	    // Recupera il prodotto dal database tramite DAO
	    EntityProdotto EP = ProdottoDAO.readProdotto(idProd);
	    
	    // Se il prodotto non esiste, restituisce un messaggio di errore
	    if (EP == null) {
	        return "Il Prodotto non è stato trovato";
	    } 
	    
	    // Altrimenti, restituisce i dati del prodotto
	    return EP.toString();
	}
	
	// Metodo che gestisce l'aggiunta di un prodotto al carrello (dettaglio spesa) per un cliente specifico
	public static String AggiungiProdottoCarrello(String codiceProdotto, String IdCliente, int quantitaRichiesta) {
	    try {
	        // 1. Recupera la lista delle spese "aperte" o valide del cliente
	        ArrayList<String> ListaSpese = SpesaDAO.readSpeseWithProdottiByCliente(IdCliente);
	
	        // Mostra le spese disponibili e lascia scegliere al cliente tramite il boundary
	        String idspesa = BoundaryCliente.mostraSpese(ListaSpese);
	
	        // Recupera la spesa selezionata
	        EntitySpesa ES = SpesaDAO.readSpesa(idspesa);
	
	        // Se la spesa non è stata trovata, interrompe l'esecuzione
	        if (ES == null) {
	            return "La Spesa non è stata trovata";   
	        }
	
	        // Log: conferma che la richiesta di aggiunta è stata ricevuta
	        System.out.println("Ricevuta richiesta per aggiungere prodotto: " + codiceProdotto);
	
	        // Crea un oggetto DAO per gestire i dettagli della spesa (il carrello)
	        DettaglioSpesaDAO carrello = new DettaglioSpesaDAO();
	
	        // Converte il codice del prodotto in intero
	        int idProdotto = Integer.parseInt(codiceProdotto);
	
	        // Recupera il prodotto dal database
	        EntityProdotto EP = ProdottoDAO.readProdotto(idProdotto);
	
	        // Se il prodotto non esiste, restituisce un messaggio di errore
	        if (EP == null) {
	            return "Il Prodotto non è stato trovato";
	        }
	
	        // Recupera la quantità disponibile in magazzino
	        int quantitaDisponibile = EP.getQtaDisponibile();
	
	        // Se la quantità richiesta supera la disponibilità, restituisce errore
	        if (quantitaDisponibile < quantitaRichiesta) {
	            return "ERRORE: Quantità richiesta superiore alla disponibilità.";
	        }
	
	        // Verifica se il prodotto è già presente nel carrello della spesa
	        String IDSpesa = Long.toString(ES.getIdSpesa());
	        boolean EsistenzaProdotto = DettaglioSpesaDAO.productExists(EP.getCodice(), IDSpesa);
	
	        if (!EsistenzaProdotto) {
	            // Se non è presente, crea una nuova voce nel dettaglio spesa (carrello)
	            DettaglioSpesaDAO.createDettaglioSpesa(ES.getIdSpesa(), Integer.parseInt(codiceProdotto), quantitaRichiesta);
	            return "ACK: Il prodotto è stato aggiunto correttamente al carrello.";
	        } else {
	            // Se il prodotto è già presente, chiede all'utente se vuole modificare la quantità
	            String risposta = BoundaryCliente.ProponiModifica();
	            if (risposta.equalsIgnoreCase("si")) {
	                // Se l'utente accetta, chiede la nuova quantità da aggiornare
	                int QuantitaModificata = BoundaryCliente.ProponiModificaQta();
	                DettaglioSpesaDAO.updateDettaglioSpesa(IDSpesa, codiceProdotto, QuantitaModificata);
	                return "ACK: La quantità del prodotto è stata modificata correttamente.";
	            } else {
                    return "AVVISO: La modifica non è stata effettuata";
                }
	        }
	
	    } catch (Exception e) {
	        // Gestione generica degli errori
	        System.err.println("Errore durante l'aggiunta del prodotto: " + e.getMessage());
	        return "ERRORE: " + e.getMessage();
	    }
	
	   
	}
	
		
	
	
	
	
	/**
	  * Preleva lo sconto e lo restituisce 
	  * @param idCliente
	  * @param idSpesa 
	  * @return LA percentuale di sconto disponibile
	  * 	  * 
	  * */
	public float VerificaSconto(int idCliente, int idSpesa) throws DAOException, DBConnectionException {
	       //richiama la verifica del cliente abituale
	        if (VerificaClienteAbituale(idCliente)) {
	            try {
	                int idSconto = SpesaDAO.ReadidSconto(idSpesa);
	                if (idSconto > 0) { 
                        // Verifica che esista uno sconto associato
	                    float percentuale = ScontoDAO.readPercentualeSconto(idSconto);
	                    return percentuale;
	                }
	                return 0; // Nessuno sconto disponibile
	            } catch (Exception e) {
	                // Se non riesce a recuperare lo sconto, restituisce 0
	                return 0;
	            }
	        } else {
	            return -1; // Cliente non abituale
	        }
	    }
	/**
	     * Verifica se un cliente è abituale
	     * @param idCliente ID del cliente da verificare
	     * @return true se il cliente ha effettuato almeno SOGLIA_CLIENTE_ABITUALE spese
	     */	 
		 public boolean VerificaClienteAbituale(int idCliente) throws DAOException, DBConnectionException {
		        int numSpese = SpesaDAO.countSpese(idCliente);
		        return numSpese >= SOGLIA_CLIENTE_ABITUALE;
		    }
		    
		  /**
	     * Verifica la disponibilità di un singolo prodotto nel magazzino
	     * @param idProdotto ID del prodotto da verificare
	     * @param QtaRichiesta, la qunatita richiesta dal cliente
	     * @return true se il prodotto è disponibile nella quantità richiesta
	     */
		public Boolean verificaDisponibilitaProdotto(int idProdotto, int QtaRichiesta) throws DAOException, DBConnectionException{
			 int qtaDisponibile = ProdottoDAO.readQtaDisponibile(idProdotto);
		     return qtaDisponibile >= QtaRichiesta;
		}
		
		/**
	     *Scorre la lista dei prodotti agginuti al'interno della spesa e ne verifica la disponibilità
	     * @param idSpesa
	     * @param listaProdotti
	     * @return true se tutti i prodotti sono disponibili
	     */
		public Boolean verificaDisponibilita(int idSpesa, List<EntityDettaglioSpesa> listaProdotti) throws DAOException, DBConnectionException{
			
			for (EntityDettaglioSpesa prodCarrello : listaProdotti) {
				
				if(!verificaDisponibilitaProdotto(prodCarrello.getIdProd(), prodCarrello.getQtaCarrello())) {
					return false;
				}
	        }
	        
	        return true;						
		}
	 
	 /**Verifica se il cliente esiste
	 * @param idCliente
	 * @return true se il cliente esiste, false altrimenti 
	 */
	public boolean CheckCliente(int idCliente) {
        	 
        	 EntityCliente ec = ClienteDAO.CheckCliente(idCliente);
        	 if (ec != null) {
        		 return true;
        	 }
        	 else 
        		 return false;
         }
         
	/**
     * Calcola il costo totale di una spesa
     * @param listaProdotti lista dei prodotti all'interno del carrello
     * @param IdSpesa della spesa di cui aggiornare il Costo totale 
     * @param percentualeSconto 
     * @return il costo totale della spesa come float
     */
	
	 public float calcolaCostoTotale(List<EntityDettaglioSpesa> listaProdotti, int idSpesa, float percentualeSconto) 
	            throws DAOException, DBConnectionException {
	        float costoTotale = 0;
	        
	        for (EntityDettaglioSpesa prodCarrello : listaProdotti) {
	            float prezzoProdotto = ProdottoDAO.readPrezzo(prodCarrello.getIdProd());
	            costoTotale += prezzoProdotto * prodCarrello.getQtaCarrello();
	        }
	        
	        SpesaDAO.updatePrezzoSpesa(costoTotale, idSpesa);
	        
	        //Se richiesto lo sconto restituisce il prezzo già scontato 
	        if (percentualeSconto > 0) {
	        	return calcolaPrezzoScontato(costoTotale, percentualeSconto);
	        }
	        else {
	        	return costoTotale;
	        }
	    }
	
	/**
	  * Applica lo sconto al prezzo totale calcolato 
	  * @param costoTotale 
	  * @param percentualeSconto
	  * @return Restituisce il prezzo totale scontato 
	  * 
	  * */
	 public float calcolaPrezzoScontato(float costoTotale, float percentualeSconto) {
	        float parteScontata = costoTotale * (percentualeSconto / 100);
	        return costoTotale - parteScontata;
	    }
	 
	 /**Aggiorna il magazzino con le nuove quantità 
	  * @param idSpesa della spesa 
	  * @return un aggiornamento delle quantità all'interno del database 
	  * */
	 public void aggiornaMagazzino(int idSpesa) throws DAOException, DBConnectionException {
	        List<EntityDettaglioSpesa> prodottiCarrello = DettaglioSpesaDAO.readProdottiSpesa(idSpesa);
	        
	        for (EntityDettaglioSpesa prodCarrello : prodottiCarrello) {
	            // Calcola la nuova quantità
	        	
	            int qtaProdottoMagazzino = ProdottoDAO.readQtaDisponibile(prodCarrello.getIdProd());
	            int nuovaQta = qtaProdottoMagazzino - prodCarrello.getQtaCarrello();
	            
	            // Aggiorna la quantità nel database
	 
	            setQta(prodCarrello.getIdProd(), nuovaQta);
	        }
	    }
	    
	 	/**
	     * Imposta la nuova quantità per un prodotto
	     * @param nuovaQta la uanittà aggioranta da inserire all'interno del db
	     */
	 	private void setQta(int idProdotto, int nuovaQta) throws DAOException, DBConnectionException {
	        ProdottoDAO.aggiornaQtaDisponibile(idProdotto, nuovaQta);
	    }
	 	
	 	/**
	     * Imposta il prezzo calcolato della speas 
	     * @param idSpesa id della spesa di cui aggiornare il prezzo
	     * @param prezzo Nuvo prezzo da assegnare alla spesa 
	     */
	 	private void setPrezzoSpesa(int idSpesa, float prezzo) {
          	SpesaDAO.updatePrezzoSpesa(prezzo, idSpesa);
         }
         
         /**
	     * Imposta imposta lo stato di una spesa 
	     * @param idSpesa id della spesa di cui aggiornare il prezzo
	     * @param StatoNuovo stato da assegnare alla spesa 
	     */
         public void setStatoSpesa(String stato, int idSpesa){
             StatoSpesa state;
             if (stato == "IN_CORSO"){
                 state = StatoSpesa.IN_CORSO;
             }
             else if (stato == "CONSEGNATA"){
                 state = StatoSpesa.CONSEGNATA;
             }
             else 
             	state = StatoSpesa.ORDINATA;
             
             
             SpesaDAO.UpdateStatoSpesa(idSpesa, state);
         }
		
		 /**
	     * Completa l'acquisto di una spesa da parte di un cliente
	     * @param clienteId ID del cliente che effettua l'acquisto
	     * @param spesaId ID della spesa da completare
	     * @param percentuale se è maggiore di zero il cliente ha richiesto di applicare lo sconto 
	     * @param indirizzo indirizzo di consegna, se è null il cliente non ha richiesto la consegna
	     * @return ArrayList contenente: [listaProdottiFormatted, percentualeSconto, consegnata, prezzoTotale]
	     * @throws OperationException se si verificano errori durante l'acquisto
	     */
	    public ArrayList<Object> completaAcquisto(int idCliente, int idSpesa, float percentuale, String indirizzo) 
	            throws OperationException {
			//Verifica se richiesta la consegna 
			boolean richiestaConsegna;
			if (indirizzo != null)  richiestaConsegna = true;
			else 	richiestaConsegna = false;
		
	        try {
	            
	            // Ottiene una lista di prodotti
	            List<EntityDettaglioSpesa> listaProdotti = DettaglioSpesaDAO.readProdottiSpesa(idSpesa);
	            
				if (listaProdotti == null || listaProdotti.isEmpty()) {
	                throw new OperationException("Nessun prodotto trovato nella spesa");
	            }

	            // Verifica disponibilità dei prodotti
	            if (!verificaDisponibilita(idSpesa, listaProdotti)) {
	                throw new OperationException("Transazione annullata per prodotto non disponibile");
	            }


	            // Calcola il totale
	            float totale = calcolaCostoTotale(listaProdotti, idSpesa, percentuale);

	            ArrayList<String> lp = new ArrayList<>();
	            for (EntityDettaglioSpesa prodCarrello : listaProdotti) {
	                // Ottieni dettagli prodotto
	                EntityProdotto prodotto = ProdottoDAO.readProdotto(prodCarrello.getIdProd());
	                float prezzoProdotto = ProdottoDAO.readPrezzo(prodCarrello.getIdProd());

	                String prodottoFormattato = prodotto.getNome() + 
	                                         " (Qty: " + prodCarrello.getQtaCarrello() + 
	                                         ", Prezzo unitario: €" + String.format("%.2f", prezzoProdotto) + 
	                                         ", Subtotale: €" + String.format("%.2f", prezzoProdotto * prodCarrello.getQtaCarrello()) + ")";
	                
	                lp.add(prodottoFormattato);
	            }

				
				//Imposta il nuovo prezzo della spesa (scontato se richiesto)
				setPrezzoSpesa(idSpesa, totale);
								

	            // risultato formattato per il boundary
	            ArrayList<Object> risultato = new ArrayList<>();
	            risultato.add(lp);                           // 0: Lista prodotti formattata
	            risultato.add((double) percentuale );   // 1: Percentuale sconto
	            risultato.add(richiestaConsegna);                     // 2: Consegna 
	            risultato.add((double) totale);              // 3: Prezzo totale

	            return risultato;

	        } catch (DAOException | DBConnectionException e) {
	            throw new OperationException("Errore di accesso ai dati: " + e.getMessage());
	        } catch (Exception e) {
	            throw new OperationException("Errore imprevisto durante il completamento dell'acquisto: " + e.getMessage());
	        }
	    }
	




 // caso d'uso: RichiediReport
	    public HashMap<Integer, HashMap<Integer, Float>> GeneraMappa(int sogliaSpese)  {
	    HashMap<Integer, HashMap<Integer, Float>> ClientiReport = new HashMap<>();

	    try {
	        List<EntityCliente> clienti = ClienteDAO.getListaClienti();

	        for (EntityCliente cliente : clienti) {
	            int id = cliente.getIdCliente();
	            int numeroSpese = SpesaDAO.countSpese(id);

	            if (numeroSpese >= sogliaSpese) {
	                float importoSpese = SpesaDAO.readImportoSpese(id);
	                   

	                HashMap<Integer, Float> InnerMap = new HashMap<>();
	                InnerMap.put(numeroSpese, importoSpese);
	                ClientiReport.put(id, InnerMap);
	            }
	        }

	    } catch (DAOException | DBConnectionException e) {
	        System.err.println("Errore: " + e.getMessage());
	    }

	    return ClientiReport;
	
}
}



	