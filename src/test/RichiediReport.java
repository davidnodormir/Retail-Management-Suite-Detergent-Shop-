package test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import boundary.BoundaryImpiegato;

import java.io.*;
import java.util.*;
import control.GestioneNegozio;
import exception.ReportVuotoException;


public class RichiediReport{
    
    // Test Case 1: Tutti input validi
	@Test
	public void testTuttiInputValidi() {
		InputStream originalIn = System.in;
		PrintStream originalOut = System.out;
		try {
			
			String input = "3\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
			System.setIn(new ByteArrayInputStream(input.getBytes()));
			if (BoundaryImpiegato.scan != null) {            
                BoundaryImpiegato.scan.close();    
                    }
  	        BoundaryImpiegato.scan = new Scanner(System.in);        
            ByteArrayOutputStream output = new ByteArrayOutputStream(); 
            System.setOut(new PrintStream(output));       
            BoundaryImpiegato.richiediReport();
            String result = output.toString();        
                      
            assertTrue("Dovrebbe stampare report con clienti",                
            result.contains("cliente") && result.contains("spese"));    
                 } 
                 finally {        
                     System.setIn(originalIn);        
                     System.setOut(originalOut);        
                     BoundaryImpiegato.scan = new Scanner(System.in);   
                      }
		}
	

	//Test Case 2: sogliaSpese = 0
	@Test 
	public void testSogliaSpeseNulla() {
    InputStream originalIn = System.in;   
 PrintStream originalOut = System.out;    
	try {        
	     
	 String input = "0\n\n\n\n\n\n\n\n\n\n";        
		System.setIn(new ByteArrayInputStream(input.getBytes()));        
		if (BoundaryImpiegato.scan != null) {            
		
		BoundaryImpiegato.scan.close();       
		
		 }       
		BoundaryImpiegato.scan = new Scanner(System.in);       
	    ByteArrayOutputStream output = new ByteArrayOutputStream();       
	    System.setOut(new PrintStream(output));        
	    BoundaryImpiegato.richiediReport();       
	    String result = output.toString();       
	    assertTrue("Dovrebbe mostrare il messaggio di errore per input valido",                
	    result.contains("Errore: il valore deve essere maggiore di 0"));    
	     } finally {   
     		
     		     System.setIn(originalIn);       
	     		 System.setOut(originalOut);       
	       		 BoundaryImpiegato.scan = new Scanner(System.in);   
	      	  }
	}
	
    //Test Case 3: sogliaSpese negativa
    
    @Test
    
    public void testSogliaSpeseNegativa() {
        
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        
        try {
           
            String input = "-1\n1\n\n\n\n\n\n\n\n\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            if (BoundaryImpiegato.scan != null) {
                BoundaryImpiegato.scan.close();
            }
            BoundaryImpiegato.scan = new Scanner(System.in);
            
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            BoundaryImpiegato.richiediReport();

            String result = output.toString();
           
            
            assertTrue("Dovrebbe mostrare il messaggio di errore", 
                       result.contains("Errore: il valore deve essere maggiore di 0"));
            
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            BoundaryImpiegato.scan = new Scanner(System.in);
        }
    }

    //Test Case 4: Lista clienti vuota
    @Test
    public void testListaClientiVuota() {
        
        HashMap<Integer, HashMap<Integer, Float>> report = 
            GestioneNegozio.getInstanceOf().GeneraMappa(2);
        
        try {
            StampaReport(report);
            if (report.isEmpty()) {
                fail("Dovrebbe lanciare ReportVuotoException");
            }
        } catch (ReportVuotoException e) {
            assertEquals("Il Report è vuoto.", e.getMessage());
        }
    }
    
    //Test Case 5: numeroSpese < sogliaSpese
    @Test
    public void testNumeroSpeseMInoreSoglia() {
        HashMap<Integer, HashMap<Integer, Float>> report = 
            GestioneNegozio.getInstanceOf().GeneraMappa(50); // soglia alta
        
        try {
            StampaReport(report);
            if (report.isEmpty()) {
                fail("Dovrebbe lanciare ReportVuotoException");
            }
        } catch (ReportVuotoException e) {
            assertEquals("Il Report è vuoto.", e.getMessage());
        }
    }
    
    //Test Case 6: ClientiReport vuoto
    @Test
    public void testClientiReportVuoto() {
        HashMap<Integer, HashMap<Integer, Float>> report = 
            GestioneNegozio.getInstanceOf().GeneraMappa(100); // soglia molto alta
        
        try {
            StampaReport(report);
            if (report.isEmpty()) {
                fail("Dovrebbe lanciare ReportVuotoException");
            }
        } catch (ReportVuotoException e) {
            assertEquals("Il Report è vuoto.", e.getMessage());
        }
    }
    
    // richiamo del metodo StampaReport
    private void StampaReport(HashMap<Integer, HashMap<Integer, Float>> mappa) {
        if (mappa.isEmpty()) {
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
}