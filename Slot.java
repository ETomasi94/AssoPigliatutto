/*
ASSO PIGLIATUTTO
TESI DI LAUREA A.A 2020 - 2021

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

import java.awt.Image;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * @CLASS Slot
 * 
 * @OVERVIEW Classe che implementa una generica slot della board di gioco con le sue
 *           caratteristiche ed i suoi comportamenti
*/
public class Slot implements Serializable
{
    /*----VARIABILI D'ISTANZA----*/
    
    JLabel Label;//JLabel a cui associare l'immagine della carta assegnata alla slot

    Carta Card;//Carta assegnata alla slot
    
    /*----FINE VARIABILI D'ISTANZA----*/
    
    /*----METODO COSTRUTTORE----*/
    
    /**
     * @METHOD Slot
     * 
     * @OVERVIEW Metodo costruttore che costruisce un'istanza della classe Slot
     *           
     * @param L JLabel associata alla slot
     * 
     * @RETURNS S : Slot della board di gioco istanza della classe
    */
    public Slot(JLabel L)
    {
        Label = L;
    }
    
    /*----FINE METODO COSTRUTTORE----*/
    
    /*----METODI DI MODIFICA DELLA SLOT----*/
    
    /**
     * @METHOD AssignCard
     * 
     * @OVERVIEW Metodo che assegna una carta data in input alla slot in questione
     *           rimuovendo, nel caso ci sia, la precedente
     *
     * @param C : Carta da assegnare alla slot
     * 
     */
    public void AssignCard(Carta C)
    {
        if(HasCard())
        {
            RemoveCard();
        }
        
        Card = C;
        C.AssegnaSlot(this);
        
        int codice = C.GetCodice();
        
        ImageIcon ic = new javax.swing.ImageIcon(getClass().getResource("/CardSkins/"+codice+".png"));
        
        Image image = ic.getImage(); // transform it 
        Image newimg = image.getScaledInstance(100, 149,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ic = new ImageIcon(newimg);  // transform it back
        
        Label.setIcon(ic);
    }

    /**
     * @METHOD Hide
     * 
     * @OVERVIEW Metodo che nasconde una carta modificandone la sua immagine con quella del dorso di una
     *           generica carta (Immagine "0.png" all'interno della cartella CardSkins)
     * 
     *           Utilizzata per nascondere le carte della mano della CPU dalla vista del giocatore
    */
     public void Hide()
     {
        ImageIcon ic = new javax.swing.ImageIcon(getClass().getResource("/CardSkins/"+0+".png"));
        
        Image image = ic.getImage(); // transform it 
        Image newimg = image.getScaledInstance(100, 149,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ic = new ImageIcon(newimg);  // transform it back
        
        Label.setIcon(ic);    
     }
     
     public void Reveal()
     {
        int codice = Card.GetCodice();
        
        ImageIcon ic = new javax.swing.ImageIcon(getClass().getResource("/CardSkins/"+codice+".png"));
        
        Image image = ic.getImage(); // transform it 
        Image newimg = image.getScaledInstance(100, 149,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ic = new ImageIcon(newimg);  // transform it back
        
        Label.setIcon(ic);
     }
     
     /**
      * @METHOD RemoveCard
      * 
      * @OVERVIEW Metodo che rimuove, se esiste, la carta assegnata dalla slot in questione
      */
     public void RemoveCard()
     {
         if(HasCard())
         {
            Card.RimuoviDaSlot();
            
            this.Card = null;
         }
     }
     
     /*----FINE METODI DI MODIFICA DELLA SLOT----*/
     
     /*----METODI DI VERIFICA----*/
     
     /**
      * @METHOD IsChoosable
      * 
      * @OVERVIEW Metodo che indica se con la carta assegnata alla slot corrente 
      *           è possibile effettuare una presa indicizzata da un intero in input
      * 
      * @param n : Indice della presa cercata
      * @return Choosable : Valore booleano che indica se è possibile effettuare la presa
      *         indicizzata con l'indice n giocando la carta
      */
       public boolean IsChoosable(int n)
     {
         if(HasCard())
         {
            return Card.Potenziale.containsKey(n);
         }
         else
         {
             return false;
         }
     }
     
    /**
     * @METHOD HasCard
     * 
     * @OVERVIEW Metodo che verifica se la slot in questione ha una carta associata ad
     *           essa o meno
     * 
     * @RETURNS Result : Valore booleano rappresentante il risultato della verifica
     * 
     */
    public boolean HasCard()
    {
        return !(Card == null);
    }
    
    /*----FINE METODI DI VERIFICA----*/
    
    /*----METODI GETTERS E SETTERS----*/

    /**
     * @METHOD SetEmpty
     * 
     * @OVERVIEW Metodo che toglie la carta assegnata dalla slot in questione e reimposta
     *           la slot per un nuovo utilizzo
     */
    public void SetEmpty()
    {
        Card = null;
        
        Label.setIcon(null);
        
        Label.revalidate();
    }

    /**
     * @METHOD GetCard
     * 
     * @OVERVIEW Metodo che restituisce la carta associata alla slot in questione
     * 
     * @RETURNS Card : Carta associata alla slot in questione
     */
    public Carta GetCard()
    {
        return Card;
    }
    
    
    /**
     * @METHOD GetLabel
     * 
     * @OVERVIEW Metodo che restituisce l'etichetta (JLabel) associata alla slot
     *           in questione
     * 
     * 
     * @RETURNS Label : Etichetta (JLabel) associata alla slot in questione
     */
    public JLabel GetLabel()
    {
        return this.Label;
    }
    
    /*----FINE METODI GETTERS E SETTERS----*/
}
