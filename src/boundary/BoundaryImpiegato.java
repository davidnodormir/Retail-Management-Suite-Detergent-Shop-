package boundary;

import java.util.ArrayList;
import java.util.Scanner;
import control.GestioneNegozio;
import exception.OperationException;
import exception.ReportVuotoException;
import java.util.Map;
import java.util.HashMap;

public class BoundaryImpiegato {
    public static Scanner scan = new Scanner(System.in);
    
        private static String leggiInput(String messaggio) {
        System.out.println(messaggio);
        return scan.nextLine().trim();
    }
    
    private static float leggiFloat(String messaggio) {
        while (true) {
            try {
                System.out.println(messaggio);
                String input = scan.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("Valore richiesto.");
                    continue;
                }
                
                return Float.parseFloat(input);
                
            } catch (NumberFormatException e) {
                System.out.println("Valore numerico non valido.");
            }
        }
    }
    
    private static int leggiInt(String messaggio) {
        while (true) {
            try {
                System.out.println(messaggio);
                String input = scan.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("Valore richiesto.");
                    continue;
                }
                
                return Integer.parseInt(input);
                
            } catch (NumberFormatException e) {
                System.out.println("Numero intero non valido.");
            }
        }
    }
    
    
    //caso d'uso: AggiungiAlCatalogo
    
    public static void aggiungiAlCatalogo() {
        GestioneNegozio gestioneNegozio = GestioneNegozio.getInstanceOf();
        
        try {
        	String nome;
        	while (true) {
                nome = leggiInput("Inserisci il nome del prodotto:");
                if (!nome.isEmpty()) {
                    break;
                } else {
                    System.out.println("Il nome del prodotto non può essere vuoto! Riprova.");
                }
            }
            
            ArrayList<String> prodottiOmonimi = gestioneNegozio.cercaProdottiOmonimi(nome);
            
            if (!prodottiOmonimi.isEmpty()) {
                mostraProdottiTrovati(prodottiOmonimi);
                
                if (confermaAggiornamento()) {
                    int codiceSelezionato = selezionaCodice(gestioneNegozio);
                    if (codiceSelezionato != -1) {
                        aggiornaEsistente(gestioneNegozio, codiceSelezionato);
                        return;
                    }
                }
            }
            
            creaNuovoProdotto(gestioneNegozio, nome);
            
        } catch (OperationException oe) {
            System.out.println("Errore: " + oe.getMessage());
            System.out.println("Riprovare...\n");
        } catch (Exception e) {
            System.out.println("Errore imprevisto. Riprovare!");
        }
    }
    
    private static void mostraProdottiTrovati(ArrayList<String> prodottiOmonimi) {
        System.out.println("\nProdotti simili trovati:");
        for (String prodotto : prodottiOmonimi) {
            String[] dati = prodotto.split("\\|");
            if (dati.length >= 5) {
                System.out.println("Codice: " + dati[0] + "\n" + dati[1] + 
                                 " - " + dati[2] + "\n - €" + dati[3] + 
                                 "\n - Qta: " + dati[4] + "\n");
            }
        }
    }
    
    private static boolean confermaAggiornamento() {
        while (true) {
            System.out.println("\nVuoi aggiornare uno di questi prodotti? (S/N)");
            String risposta = scan.nextLine().trim();
            
            if (risposta.equalsIgnoreCase("s")) {
                return true;
            } else if (risposta.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("Risposta non valida. Inserire S o N.");
            }
        }
    }
    
    private static int selezionaCodice(GestioneNegozio gestioneNegozio) {
        while (true) {
            try {
                System.out.println("Inserisci il codice del prodotto da aggiornare:");
                String input = scan.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("Inserire un codice valido.");
                    continue;
                }
                
                int codiceSelezionato = Integer.parseInt(input);
                
                if (gestioneNegozio.isValidCodice(codiceSelezionato)) {
                    return codiceSelezionato;
                } else {
                    System.out.println("Codice non valido. Seleziona uno dei codici mostrati sopra.");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("Inserire un numero valido.");
            }
        }
    }
    
    private static void aggiornaEsistente(GestioneNegozio gestioneNegozio, int codice) 
            throws OperationException {
        String descrizione = leggiInput("Inserisci la nuova descrizione:");
        float prezzo = leggiFloat("Inserisci il nuovo prezzo:");
        int quantita = leggiInt("Inserisci la nuova quantità disponibile:");
        
        if (gestioneNegozio.aggiornaProdotto(codice, descrizione, prezzo, quantita)) {
            System.out.println("Prodotto aggiornato con successo!");
        }
    }
    
    private static void creaNuovoProdotto(GestioneNegozio gestioneNegozio, String nome) 
            throws OperationException {
        String descrizione = leggiInput("Inserisci la descrizione:");
        float prezzo = leggiFloat("Inserisci il prezzo:");
        int quantita = leggiInt("Inserisci la quantità disponibile:");
        
        if (gestioneNegozio.aggiungiNuovoProdotto(nome, descrizione, prezzo, quantita)) {
            System.out.println("Nuovo prodotto aggiunto con successo!");
        }
    }
    

    
    //caso d'uso: RichiediReport
    private static void StampaReport(HashMap<Integer, HashMap<Integer,Float>> mappa) {
		if(mappa.isEmpty()) {
			throw new ReportVuotoException("Il Report è vuoto.");
			
		}
		else {
		for (Map.Entry<Integer, HashMap<Integer,Float>> i : mappa.entrySet()) {
			Integer id = i.getKey();
			HashMap<Integer, Float> valori = i.getValue();
			
			for (Map.Entry<Integer, Float> dati : valori.entrySet()) {
				Integer numeroSpese = dati.getKey();
				Float SommaTotale = dati.getValue();
				
				System.out.println("Il cliente (ID "+ id+ ") ha effettuato "+ numeroSpese+ 
						" spese totali, pagando un importo finale di €"+ SommaTotale);
				}
			
		}
		}
    }
	
	
    public static void richiediReport() {
       
    	try {
	        	int sogliaSpese = -1;
	        	while (sogliaSpese <= 0) {
	        	    sogliaSpese = leggiInt("Inserisci il numero di spese minimo per cliente abituale:");
	        	    if (sogliaSpese <= 0) {
	        	        System.out.println("Errore: il valore deve essere maggiore di 0. Riprova.");
	        	    }
	        	}
	            
	            System.out.println("Generazione report clienti con almeno " + sogliaSpese + " spese...");

	        	GestioneNegozio gestioneNegozio = GestioneNegozio.getInstanceOf();
	           
	            HashMap<Integer, HashMap<Integer, Float>> report = gestioneNegozio.GeneraMappa(sogliaSpese);

	            // Stampa il report
	           StampaReport(report);

	        }catch (Exception e) {
	        	
	            System.err.println("Errore durante la generazione del report: " + e.getMessage());
	        }
	    }
}