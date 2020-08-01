/*
ASSO PIGLIATUTTO
PROGETTO DI ESPERIENZE DI PROGRAMMAZIONE A.A 2019-2020

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

import java.util.ArrayList;

/*
    @CLASS Giocatore

    @OVERVIEW Classe che implementa le proprietà ed i comportamenti del giocatore
              impersonato dall'utente
*/
public class Giocatore extends Thread
{
    /*--------VARIABILI D'ISTANZA----------*/
    
    String nome;
    
    public Punteggio points;
    
    boolean mazziere;//Booleano che indica se il giocatore è il mazziere della partita o meno
    
    boolean turn;//Booleano che indica se è il turno del giocatore di giocare o meno
    
    boolean active;//Booleano che indica se il giocatore è attivo o meno
    
    boolean hastakenlast;//Booleano che indica se il giocatore è stato l'ultimo a fare una presa o meno
    
    boolean turnstart;//Booleano indicante il turno iniziale
    
    ArrayList<Carta> mano;
    
    public ArrayList<Carta> Table;//Tavolo su cui il giocatore gioca le sue carte ed effettua le sue prese
    
    /*-------------------------------------------*/
    
    /*-------METODO COSTRUTTORE--*/
    
    /*
        @METHOD Giocatore
        
        @OVERVIEW Metodo costruttore del giocatore
    
        @PAR n : Stringa rappresentante il nome del giocatore
        @PAR decision : Booleano che rappresenta se il giocatore è il mazziere o meno
        @PAR Tavolo : ArrayList di oggetti di tipo carta che rappresenta il tavolo su cui il giocatore
                      gioca le sue carte ed effettua le sue prese
    
        @RETURNS Player : Istanza della classe Giocatore
    */
    public Giocatore(String n,boolean decision, ArrayList<Carta> Tavolo)
    {
        nome = n;
        points = new Punteggio();
        mazziere = decision;
        mano = new ArrayList(3);
        
        active = true;
        hastakenlast = false;
        
        Table = Tavolo;
    }
    
    /*---------------------------*/
    
    /*-------METODI DI GIOCO-----*/

    /*
        @METHOD PickHand
    
        @OVERVIEW Metodo attraverso cui il giocatore pesca quattro carte dal mazzo
    
        @PAR Mazzo : ArrayList di oggetti di tipo Carta rappresentante il mazzo
                     da cui la CPU ed il giocatore pescano le carte
    */
    public synchronized void PickHand(ArrayList<Carta> Mazzo)
    {
        Carta C;
        
        for(int i=0; i<3; i++)
        {
            C = Mazzo.get(1);
            mano.add(C);
            Mazzo.remove(1);  
        }
    }
    
    /*
        @METHOD GetTotalPotential
    
        @OVERVIEW Metodo che calcola tutte le prese possibili da un tavolo dato in input
                  con la mano corrente
    
        @PAR Tav : ArrayList di carte rappresentante il tavolo da cui cercare di effettuare
             prese con la sua configurazione corrente
    */
    public synchronized void GetTotalPotential(ArrayList<Carta> Tav)
    {
            for(Carta c : mano)
            {
                c.CalcolaPotenziale(Tav,points);
            }
    }
    
    /*
        @METHOD CallScopa
    
        @OVERVIEW Metodo che incrementa il numero di scope effettuate chiamando l'opportuno
                  metodo della classe punteggio
    */
    public synchronized void Scopa()
    {
        points.CallScopa();
    }
    
    /*
        @METHOD  Score
    
        @OVERVIEW Metodo che aggiunge una carta in input a quelle ottenute aggiornando il 
                  punteggio in base alle sue caratteristiche
    
        @PAR card : Carta ottenuta da aggiungere alla lista delle carte ottenute
    */
    public synchronized void Score(Carta card)
    {
        points.AddCard(card);
    }
    
    /*----------------------*/
    
    /*-------METODI DI DEBUG----*/
    
    /*
        @METHOD VisualizzaMano
        
        @OVERVIEW Metodo che stampa in output le carte presenti nella mano del giocatore
    */
    public synchronized void VisualizzaMano()
    {
        System.out.println("CARTE POSSEDUTE DA "+nome+":\n");
        
        for(Carta c : mano)
        {
            System.out.println(c.GetName());
        }
    }
    
    /*
        @METHOD HandDrop
    
        @OVERVIEW Metodo che elimina tutte le carte presenti nella mano del giocatore
    */
    public synchronized void HandDrop()
    {
        mano.clear();
    }
    
    /*
        @METHOD Reset
    
        @OVERVIEW Metodo che resetta il giocatore eliminando tutte le carte presenti nella sua mano
                  e resettandone il punteggio
    */
    public synchronized void Reset()
    {
        if(!mano.isEmpty())
        {
            mano.clear();
        }
        
        points.ResetScore();
    }
    
    /*-------FINE METODI DI DEBUG----*/
    
    /*------------METODI GETTERS E SETTERS-------------------*/
    
    /*
        @METHOD GetHandCards
    
        @OVERVIEW Metodo che restituisce il numero di carte presenti nella mano del giocatore
    
        @RETURNS HandCards : Intero che rappresenta il numero di carte presenti nella mano del giocatore
    */
    public int GetHandCards()
    {
        return mano.size();
    }
            
    
    /*
        @METHOD GetName
    
        @OVERVIEW Metodo che restituisce il nome del giocatore
    
        @RETURNS PlayerName : Stringa che rappresenta il nome del giocatore
    */
    public String GetName()
    {
        return this.nome;
    }
    
    /*
        @METHOD GetPoints
    
        @OVERVIEW Metodo che restituisce il punteggio del giocatore
    
        @RETURNS ActualPoints : Punteggio del giocatore (istanza della classe Punteggio)
    */
    public Punteggio GetPoints()
    {
        return this.points;
    }
    
    /*
        @METHOD GetMano
    
        @OVERVIEW Metodo che restituisce la mano del giocatore
    
        @RETURNS PlayerHand : ArrayList di oggetti di tipo carta che rappresenta la mano del giocatore
    */
    public ArrayList<Carta> GetMano()
    {
        return this.mano;
    }
    
    /*
        @METHOD SetMazziere
    
        @OVERVIEW Metodo che setta la variabile d'istanza mazziere ad un valore booleano M in input, dichiarando quindi 
                  se il giocatore è il mazziere della partita o meno
    
        @PAR M : Booleano che rappresenta il fatto che il giocatore è il mazziere della partita o meno 
    */
    public void SetMazziere(boolean M)
    {
        this.mazziere = M;
    }  
    
    /*
        @METHOD IsActive
    
        @OVERVIEW Metodo che indica se il giocatore è attivo o meno
    
        @RETURNS Active : Booleano che indica se il giocatore è attivo o meno
    */
    public boolean IsActive()
    {
        return active;
    }
    
    /*
        @METHOD YourTurn
    
        @OVERVIEW Metodo che indica se è il turno del giocatore o meno
    
        @RETURNS Active : Booleano che indica se è il turno del giocatore o meno
    */
    public synchronized boolean YourTurn()
    {
        return turn;
    }
    
    /*
        @METHOD AssignTable
    
        @OVERVIEW Metodo che assegna un tavolo in input al giocatore consentendogli
                  di valutare le possibili prese e giocare le sue carte su di esso
    
        @PAR Tv : ArrayList di oggetti di tipo carta rappresentante il tavolo dato in input
    */
      public synchronized void AssignTable(ArrayList<Carta> Tv)
    {
        Table = Tv;
    }
    
    /*
        @METHOD AssegnaTurno
    
        @OVERVIEW Metodo che decide se è il turno del giocatore o meno asseganndo ad un'opportuna variabile
                  d'istanza un valore booleano
    
        @PAR Tv : Booleano che indica se è il turno del giocatore o meno
    */
    public synchronized void AssegnaTurno(boolean a)
    {
        turn = a;
    }
    
    /*
        @METHOD CambioTurno
    
        @OVERVIEW Metodo che inverte il turno del giocatore per indicare che è arrivato o non è più il suo turno
                  a seconda dei casi
    */
    public synchronized void CambioTurno()
    {
        turn = !turn;
        
        turnstart = turn;
    }
    
    /*
        @METHOD SetTaken
    
        @OVERVIEW Metodo che indica se il giocatore è stato l'ultimo ad effettuare una presa, impostando l'opportuna
                  variabile d'istanza a true
        
    */
    public void SetTaken()
    {
        hastakenlast = true;
    }
    
    /*
        @METHOD UnsetTaken
    
        @OVERVIEW Metodo che indica se il giocatore non è stato l'ultimo ad effettuare una presa, impostando l'opportuna
                  variabile d'istanza a false
    */
    public void UnsetTaken()
    {
        hastakenlast = false;
    }
    
    /*
        @METHOD GetLastToTake
    
        @OVERVIEW Metodo che verifica se il giocatore è stato l'ultimo ad effettuare una prese o meno restituendo in output
                  il valore dell'opportuna variabile d'istanza
    
        @RETURNS HasTakenLast : Booleano che indica se il giocatore è stato l'ultimo ad effettuare una presa o meno
    */
    public boolean GetLastToTake()
    {
        return hastakenlast;
    }
    
    /*-------FINE METODI GETTERS E SETTERS-----------*/
    
    /*------METODI CICLO DI VITA DEL THREAD---------*/
    
    /*
        @METHOD StopTurn
    
        @OVERVIEW Metodo che ferma il giocatore in caso esso possa interferire con il gioco
                  (ad esempio durante il metodo FinalTest della classe gioco) impostando
                  la variabile d'istanza rappresentante il turno del giocatore a false
    */
    public void StopTurn()
    {
        turn = false;
    }
    
    /*
        @METHOD ShutDown
    
        @OVERVIEW Metodo che arresta il thread del giocatore impostando il valore della variabile
                  d'istanza active a false
    */
    public void ShutDown()
    {
        active = false;
    }
    
    @Override
    /*
        @METHOD run
    
        @OVERVIEW Metodo che implementa il ciclo di vita del giocatore, e consente all'utente di 
                  effettuare una mossa solo quando è il turno del giocatore
    */
    public void run()
    {
        while(active)
        {    
            if(YourTurn())
            {
            }
        }
        
        System.out.println("GIOCATORE TERMINA ESECUZIONE\n");
    }
    
    /*------FINE METODI CICLO DI VITA DEL THREAD---------*/
   
}
