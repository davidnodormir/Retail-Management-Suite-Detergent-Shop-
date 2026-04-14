package boundary;

import database.SpesaDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.List;

import control.GestioneNegozio;
import exception.DBConnectionException;
import exception.OperationException;
import exception.DAOException;



public class BoundaryCliente {
		public static Scanner scan = new Scanner(System.in);
		
		//inizio boundary caso d'uso: AggiungiProdottoCarrello

		public static void AggiungiProdottoCarrello() {
		    
		    // 1. Richiede all'utente l'ID del cliente
		    System.out.print("Inserisci l'ID cliente: ");
		    String idCliente = scan.nextLine();
		    
		    // Se l'ID cliente è vuoto, lancia errore e interrompe
		    if (idCliente.isEmpty()) {
		        System.out.println("Codice prodotto non valido");
		        return; 
		    }
		    
		    // 2. Richiede il codice del prodotto da aggiungere
		    System.out.print("\nInserisci il codice del prodotto: ");
		    String codiceProdotto = scan.nextLine().trim();
		    
		    // Se il codice è vuoto, blocca l'operazione
		    if (codiceProdotto.isEmpty()) {
		        System.out.println("Codice prodotto non valido");
		        return; 
		    }
		    
		    // 3. Verifica l'esistenza del prodotto tramite il control
		    String esistenzaProdotto = GestioneNegozio.EsistenzaProdotto(codiceProdotto);
		    System.out.println(esistenzaProdotto);
		    
		    // Se il prodotto non esiste, termina l'operazione
		    if (esistenzaProdotto.compareTo("Il Prodotto non è stato trovato") == 0) {
		        return;
		    }
		    
		    // 4. Richiede la quantità desiderata all'utente
		    System.out.println("Inserisci la quantità richiesta: ");
		    try {
		        int quantitaRichiesta = Integer.parseInt(scan.nextLine().trim());
		        
		        // Controlla che la quantità sia maggiore di 0
		        if (quantitaRichiesta <= 0) {
		            System.out.println("La quantità deve essere maggiore di 0");
		            return; 
		        }
		        
		        // 5. Passa i dati al control per effettuare l’aggiunta al carrello
		        String risultato = GestioneNegozio.AggiungiProdottoCarrello(codiceProdotto, idCliente, quantitaRichiesta);
		        System.out.println("\nRisultato: " + risultato);
		        
		    } catch (NumberFormatException e) {
		        // In caso di input non numerico
		        System.out.println("Quantità non valida. Inserire un numero intero.");
		        return; 
		    }
		}

	    
	    

	    
	    /**
	     * Mostra la lista delle spese del cliente e permette di selezionarne una
	     * prende in ingresso la listaSpese ArrayList delle spese del cliente
	     * e restituisce EntitySpesa selezionata o null se nessuna selezione valida
	     */
		public static String mostraSpese(ArrayList<String> listaSpese) {
		    try {
		        // Se la lista è vuota, informa l’utente e termina
		        if (listaSpese == null || listaSpese.isEmpty()) {
		            System.out.println("Nessuna spesa trovata per questo cliente.\n");
		            return null;
		        }
		
		        // Stampa tutte le spese trovate
		        for (String spesa : listaSpese) {
		            System.out.println(spesa); // ogni stringa dovrebbe già contenere idSpesa e dettagli
		        }
		
		        // Richiesta di selezione ID spesa
		        System.out.print("Inserisci l'ID della spesa che vuoi selezionare (oppure 0 per annullare): ");
		        int idInserito = scan.nextInt();
		
		        // Se l’utente inserisce 0, annulla l’operazione
		        if (idInserito == 0) {
		            System.out.println("Operazione annullata.");
		            return null;
		        }
		
		        // Controlla che l'ID inserito sia presente tra le spese stampate
		        for (String spesa : listaSpese) {
		            if (spesa.contains("idSpesa: " + idInserito)) {
		                System.out.println("Spesa selezionata:\n" + spesa);
		                return String.valueOf(idInserito);
		            }
		        }
		
		        // ID non trovato
		        System.out.println("ID Spesa non trovato.");
		        return null;
		
		    } catch (Exception e) {
		        System.err.println("Errore nella selezione della spesa: " + e.getMessage());
		        return null;
		    }
		}
		
	    
	    /**
	     * Propone all'utente di modificare la quantità di un prodotto già presente nel carrello
	     * restituisce String "si" o "no" in base alla scelta dell'utente
	     */
	    public static String ProponiModifica() {
		    try {
		        System.out.println("\n=== PRODOTTO GIÀ PRESENTE NEL CARRELLO ===");
		        System.out.println("Il prodotto è già presente nel carrello.");
		        System.out.print("Vuoi modificare la quantità? (si/no): ");
		        
		        String risposta = scan.nextLine().trim().toLowerCase();
		        
		        // Finché la risposta non è "si" o "no", continua a chiedere
		        while (!risposta.equals("si") && !risposta.equals("no")) {
		            System.out.print("Risposta non valida. Inserisci 'si' o 'no': ");
		            risposta = scan.nextLine().trim().toLowerCase();
		        }
		        
		        return risposta;
		
		    } catch (Exception e) {
		        System.err.println("Errore nella richiesta di modifica: " + e.getMessage());
		        return "no"; // Default: no in caso di errore
		    } 
		}
	    
	    
	    /**
	     * Chiede all'utente la nuova quantità per il prodotto
	     * restituisce una nuova quantità inserita dall'utente
	     */
	    public static int ProponiModificaQta() {
		    try {
		        System.out.print("Inserisci la nuova quantità: ");
		        int nuovaQuantita = scan.nextInt();
		
		        // Continua a chiedere finché la quantità non è maggiore di 0
		        while (nuovaQuantita <= 0) {
		            System.out.print("La quantità deve essere maggiore di 0. Inserisci nuovamente: ");
		            nuovaQuantita = scan.nextInt();
		        }
		
		        System.out.println("Nuova quantità impostata: " + nuovaQuantita);
		        return nuovaQuantita;
		
		    } catch (Exception e) {
		        System.err.println("Errore nell'inserimento della quantità: " + e.getMessage());
		        return 1; // Default: 1 se qualcosa va storto
		    } 
		}
	       



		
		//caso d'uso: completaAcquisto
		
	    public static void completaAcquisto () {

	    	GestioneNegozio gn = GestioneNegozio.getInstanceOf();
	    	
	    	int idCliente = 0; 
	    	int idSpesa = 0;
	    	float  percentuale = 0;
	    	boolean inputValido = false;
	    	
	    	//Autenticazione dell'utente
	    	try {

	    		while (!inputValido) {
	    		    try {
	    		        System.out.println("Inserisci il tuo ID Cliente:");
	    		        idCliente = Integer.parseInt(scan.nextLine());

	    		        if (gn.CheckCliente(idCliente)) {
	    		            inputValido = true;
	    		        } else {
	    		            System.out.println("ID cliente non valido");
	    		        }
	    		    } catch (NumberFormatException e) {
	    		        System.out.println("Errore: inserire un numero valido per l'ID Cliente");
	    		    }
	    		}

	    	//Stampa tutte le spese associate all'id del cliente'
	    	stampaSpese(idCliente);
	    	
	    	//Input spesa id
	    	inputValido = false;
	    	while (!inputValido) { 		
	    		try {
                    System.out.println("Inserisci l'ID della Spesa:");
                    idSpesa = Integer.parseInt(scan.nextLine());
                    
                    //Verifica che la spesa sia associata al cliente
                    int idClAss = SpesaDAO.ReadCliente(idSpesa);
                    if (idClAss != idCliente) {
                    	inputValido = false;
                    	System.out.println("ID spesa non valido");
                    	
                    }
                    else
                    	inputValido = true;
                } catch (NumberFormatException e) {
                    System.out.println("Errore: inserire un numero valido per l'ID Spesa");
                }
	    	}
	    		
	    	// Richiesta consegna a domicilio
			System.out.println("Vuoi richiedere la consegna a domicilio? (S/N):");
			String consegnaRichiesta = scan.nextLine();
			boolean richiestaConsegna = consegnaRichiesta.equalsIgnoreCase("S");
			
			String indirizzo = null;
			boolean esitoConsegna = false; 
			
			if (richiestaConsegna) {
			    System.out.println("Inserisci l'indirizzo di consegna:");
			    indirizzo = scan.nextLine();
			
			    if (indirizzo.trim().isEmpty()) {
			        System.out.println("Indirizzo non valido. Consegna a domicilio disabilitata.");
			        esitoConsegna = false;
			    } else {
			        esitoConsegna = true; 
			        System.out.println("Consegna a domicilio confermata.");
			    }
			} else {
				//Se l'indirizzo non è valido la consegna è disabilitata'
			    esitoConsegna = false;
			}

            // Gestione dello sconto
			boolean esitoSconto = false;
			try {
			     percentuale = gn.VerificaSconto(idCliente, idSpesa);
			
			    if (percentuale > 0) {
			        System.out.println("Percentuale di sconto a disposizione: " + percentuale + "%");
			        System.out.println("Vuole applicare lo sconto disponibile alla spesa? (S/N)");
			
			        String rs = scan.nextLine().trim();
			        if (rs.equalsIgnoreCase("S")) {
			            System.out.println("Sconto del " + percentuale + "% verrà applicato alla spesa.");
			            esitoSconto = true;
			        } else if (rs.equalsIgnoreCase("N")) {
			            System.out.println("Sconto non applicato.");
			            esitoSconto = false;

			        } else {
			            System.out.println("Input non valido. Sconto non applicato.");
			            esitoSconto = false;

			        }
			
			    } else if (percentuale == -1) {
			        System.out.println("Il cliente non è abituale e non può usufruire degli sconti.");
			        esitoSconto = false;

			    } else {
			        System.out.println("Non sono disponibili sconti attualmente.");
			        esitoSconto = false;

			    }
			} catch (DAOException | DBConnectionException e) {
			    System.out.println("Errore durante il recupero dello sconto: " + e.getMessage());
			    System.out.println("Procedendo senza sconto...");
			    esitoSconto = false;

			} catch (Exception e) {
			    System.out.println("Errore imprevisto durante il controllo sconti: " + e.getMessage());
			    System.out.println("Procedendo senza sconto...");
			    esitoSconto = false;
			}

            
            if (esitoSconto == false){
				 percentuale = 0;
			}
			if (esitoConsegna == false){
				indirizzo = null;
			}
			
	    	ArrayList<Object> risAcquisto = gn.completaAcquisto(idCliente, idSpesa,  percentuale, indirizzo);	
            
            if (risAcquisto != null && !risAcquisto.isEmpty()) {
            	
            	 // Mostra dettagli spesa
                ArrayList<String> listaProdotti = (ArrayList<String>) risAcquisto.get(0);
                Double percentualeSconto = (Double) risAcquisto.get(1);
                Boolean consegnaAbilitata = (Boolean) risAcquisto.get(2);
                Double prezzoTotale = (Double) risAcquisto.get(3);
                
                mostraDettaglioSpesa(listaProdotti, percentualeSconto, consegnaAbilitata, prezzoTotale);
                
                // Richiesta conferma
                System.out.println("\nConfermi l'acquisto? (S/N):");
                String conferma = scan.nextLine();       
                
                if (conferma.equalsIgnoreCase("S")) {
                	boolean esitoPagamento = effettuaPagamento();
                	
                	
                
                 	if (esitoPagamento) {
                 		System.out.println("Acquisto completato con successo");
                 		//Invoca la funzione che aggiorna il magazzino
                    	gn.aggiornaMagazzino(idSpesa);
                    	//Imposta lo stato corretto della spesa
                    	String STATO = "ORDINATA";
                    	gn.setStatoSpesa(STATO, idSpesa);
                    	stampaScontrino(idCliente, idSpesa, listaProdotti, percentualeSconto, consegnaAbilitata, prezzoTotale, indirizzo);
                 	}
                 	else 
                 		System.out.println("Acquisto non andato a buon fine");
                 		
                 	
                	
                }  else {
                	System.out.println("ERROR: Transazione non andata a buon fine");
                    showErrorMessage("Pagamento fallito. Riprovare.");
                }
            } else {
            	System.out.println("ERROR: Transazione annullata");
                showErrorMessage("Acquisto annullato dall'utente.");
           }
            
	    }catch (OperationException oe) {
	    		System.out.println("ERROR: " + oe.getMessage());
	    		showErrorMessage("Operazione fallita: " + oe.getMessage());
	    } catch (Exception e) {
	    		System.out.println("ERROR: Errore imprevisto durante l'acquisto");
	            showErrorMessage("Errore di sistema. Riprovare più tardi.");
	            e.printStackTrace();
	        }
	    	
	    }



		/**
	     * VisualizzaSpeseAssociate, stampa tutte le spese assocaite ad un cliente
	     * @param idCliente
	     * @return restituisce una lista di spese associate ad un cliente
	     */
	    public static List<Integer> VisualizzaSpeseAssociate(int idCliente) {
	        List<Integer> listaSpese = new ArrayList<Integer>(); 
	        
	        listaSpese = SpesaDAO.ReadIdSpeseByCliente(idCliente);
	        return listaSpese;
	    }

	    public static void stampaSpese (int idCliente) {
	    	List<Integer> listaSpese = VisualizzaSpeseAssociate(idCliente);
	    	
	    	if (listaSpese.isEmpty()) {
	            System.out.println("Nessuna spesa trovata per il cliente con ID: " + idCliente);
	            return;
	        }
	    	
	    	System.out.println("=== SPESE DEL CLIENTE ID: " + idCliente + " ===");
	        
	        
	        for (Integer spesa : listaSpese) {
	            System.out.println("- " + spesa);
	            
	        }
	        
	        System.out.println("=== FINE LISTA SPESE ===");
	    	
	    }
	    
	    /**
	     * Stampa uno scontrino dettagliato dell'acquisto
	     * @param idCliente ID del cliente
	     * @param idSpesa ID della spesa
	     * @param listaProdotti Lista dei prodotti acquistati
	     * @param percentualeSconto Percentuale di sconto applicata
	     * @param consegnaAbilitata Se la consegna a domicilio è abilitata
	     * @param prezzoTotale Prezzo totale dell'acquisto
	     * @param indirizzo Indirizzo di consegna (se presente)
	     */ 
	    public static void stampaScontrino(int idCliente, int idSpesa, ArrayList<String> listaProdotti,
	            Double percentualeSconto, Boolean consegnaAbilitata,
	            Double prezzoTotaleScontato, String indirizzo) {


			double tot;
	        // Formato data e ora
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	        System.out.println("================================================================");
	        System.out.println("                        NEGOZIO DETERSIVI                      ");
	        System.out.println("                            Napoli                             ");
	        System.out.println("                       Tel: +39-123456789                     ");
	        System.out.println("================================================================");
	        System.out.println();

	        // Intestazione scontrino
	        System.out.println("SCONTRINO FISCALE");
	        System.out.println("Data e Ora: " + now.format(formatter));
	        System.out.println("Cliente ID: " + idCliente);
	        System.out.println("Spesa ID: " + idSpesa);
	        System.out.println("----------------------------------------------------------------");

	        // Lista prodotti
	        System.out.println("PRODOTTI ACQUISTATI:");
	        System.out.println("----------------------------------------------------------------");

	        if (listaProdotti != null && !listaProdotti.isEmpty()) {
	            int numeroProgressivo = 1;
	            for (String prodotto : listaProdotti) {
	                System.out.printf("%-3d %s%n", numeroProgressivo, prodotto);
	                numeroProgressivo++;
	            }
	        } else {
	            System.out.println("Nessun prodotto trovato");
	        }

	        System.out.println("----------------------------------------------------------------");

	        if (consegnaAbilitata != null && consegnaAbilitata) {
	            double costoConsegna = 5.00; // Example delivery cost
	            System.out.printf("Consegna a domicilio: €%8.2f%n", costoConsegna);
	            System.out.println("----------------------------------------------------------------");
	            System.out.println("INDIRIZZO DI CONSEGNA:");
	            System.out.println(indirizzo != null ? indirizzo : "Non specificato");
	            System.out.println("----------------------------------------------------------------");
	        }

	        // Se lo sconto viene aplicato mostra il prezzo con e senza sconto 
			if (percentualeSconto != null && percentualeSconto > 0) {
			    tot = prezzoTotaleScontato / (1 - percentualeSconto / 100);
			    double importoSconto = tot - prezzoTotaleScontato;
			
			    System.out.printf("Sconto (%.1f%%): -€%8.2f%n", percentualeSconto, importoSconto);
			    System.out.printf("TOTALE: €%8.2f%n", tot);
			    System.out.printf("TOTALE SCONTATO: €%8.2f%n", prezzoTotaleScontato);
			    System.out.println("================================================================");
			}

			

	        // Metodo di pagamento
	        System.out.println("Metodo di pagamento: Contanti/Carta");
	        System.out.println("Importo pagato: €" + String.format("%.2f", prezzoTotaleScontato));
	        System.out.println("Resto: €0.00");

	        System.out.println();
	        System.out.println("                    GRAZIE PER L'ACQUISTO!                     ");
	        System.out.println("                   Arrivederci e a presto!                    ");
	        System.out.println("================================================================");
	        System.out.println();
	    }


		//Simulazione del pagamento un pagamento  
	    public static boolean effettuaPagamento() {
	    	try {
	            System.out.println("Pagamento in corso...");
	            TimeUnit.SECONDS.sleep(2);
	            
	            boolean esitoTransazione = true;
	            
	            if (esitoTransazione) {
	                System.out.println("Pagamento autorizzato!");
	                return true;
	            } else {
	                System.out.println("Pagamento rifiutato!");
	                return false;
	            }
	            
	        } catch (InterruptedException e) {
	            System.out.println("Errore durante il pagamento");
	            return false;
	        }
	    }
	    
	    
	    /**
	    *Stampa le informazioni riguardo la spesa per confermare l'acquisto
	    * @param listaProdottti i prodotti all'interno della spesa'
	    * @param percentualesconto
	    * @param consegna se il clienet ha richiesto o meno la consegna 
	    * @param prezzototale il prezzo totale della spesa
	    * @return stampa a schermo le informazioni riguardanti la spesa
	    */
	   private static void mostraDettaglioSpesa(ArrayList<String> listaProdotti,
                                            Double percentualeSconto,
                                            Boolean consegna,
                                            Double prezzoTotale) {

		    System.out.println("\n=== DETTAGLIO SPESA ===");
		
		    if (listaProdotti != null && !listaProdotti.isEmpty()) {
		        System.out.println("Prodotti nel carrello:");
		        for (String prodottoInfo : listaProdotti) {
		            System.out.println("- " + prodottoInfo);
		        }
		    }
		
		    System.out.println("\nPrezzo totale: €" + String.format("%.2f", prezzoTotale));
		
		    if (percentualeSconto != null && percentualeSconto > 0) {
		        System.out.println("Sconto applicato: " + percentualeSconto + "%");
		    }
		
		    if (consegna != null && consegna) {
		        System.out.println("Consegna a domicilio: Abilitata");
		    } else {
		        System.out.println("Consegna a domicilio: Non richiesta");
		    }
		}		    
	    
	    
	    private static void showErrorMessage(String messaggio) {
	        System.out.println("\n*** ERRORE ***");
	        System.out.println(messaggio);
	        System.out.println("**************\n");
	    }
	    
	    }
	    
	    
	    
	    

