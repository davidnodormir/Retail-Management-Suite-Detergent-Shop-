package boundary;

import java.util.Scanner;


public class MainMenu {
	private static Scanner scan = new Scanner(System.in);
	public static void main(String args[]) {
	        boolean uscita = false;
	        System.out.println("Benvenuto!");
	        
	        while (!uscita) {
	            System.out.println("1. Aggiungi prodotto al catalogo.");
	            System.out.println("2. Aggiungi prodotto al carrello.");
	            System.out.println("3. Completa Acquisto");
	            System.out.println("4. Richiedi Report");
	            System.out.println("5. Esci.");
	            
	            String opzione = scan.nextLine();
	            
	            if (opzione.equals("1")) {
					BoundaryImpiegato.aggiungiAlCatalogo();
	            }else if(opzione.equals("2")){
					BoundaryCliente.AggiungiProdottoCarrello();
	            }		                
	             else if(opzione.equals("3")){
	            	BoundaryCliente.completaAcquisto();	                
	            } else if(opzione.equals("4")){
	            	BoundaryImpiegato.richiediReport();
	            	}
	            else if (opzione.equals("5")) {
	                uscita = true;
	            } 
	            else {
	                System.out.println("Operazione non disponibile! \n");
	            }
	        }
	        
	        System.out.println("Grazie!");
	        scan.close();
	    }

}
