/*
ASSO PIGLIATUTTO
TESI DI LAUREA A.A 2020 - 2021

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
da parte della CPU ed un learner di rinforzo apprende a giocare per riuscire a 
suggerire la mossa migliore da effettuare al giocatore
*/
package assopigliatutto;

import java.util.ArrayList;

/**
 * @author Enrico Tomasi
 * 
 * @CLASS LearningPlayer
 * 
 * @OVERVIEW Classe che implementa un giocatore apprendista che impara a giocare
 *           all'asso pigliatutto tramite apprendimento di rinforzo.
 */
public class LearningPlayer extends CPU
{
    int SuggestedCard;
    int SuggestedComb;
    
    QBot Bot;

/*----METODO COSTRUTTORE----*/    
    public LearningPlayer(String n, boolean decision,Gioco g,ArrayList<Carta> Tavolo) 
    {
        super(n, decision,g, Tavolo);
        
        LearnerCardsPlayed = 0;
        
        LearnerGreedyPlayed = 0;
        
        KB = new KnowledgeBase(this);
    }
/*----FINE METODO COSTRUTTORE----*/    

/*----METODI DI GIOCO----*/
    /**
     * @METHOD Act
     * 
     * @OVERVIEW Metodo che implementa l'azione dell'apprendista, suddivisa nelle
     *           seguenti fasi:
     *          
     *           -Attivazione e consultazione del bot
     * 
     *           -Ricezione suggerimento relativo alla carta ed alla combinazione
     *            da giocare
     * 
     *          -Gioco della carta suggerisa e presa delle carte indicate dalla
     *           combinazione suggerita
     */
    @Override
    public synchronized void Act()
    {
        try
        {
            if(mano.isEmpty())
            {
                Sessione.Switch();
            }
            else
            {
                ActivateBot();

                GetCurrentSuggestion();

                PlayCard();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @METHOD ActivateBot
     * 
     * @OVERVIEW Metodo che consulta il bot facendo in modo che questi osservi lo
     *           stato corrente e decida in base ad esso.
     */
    public synchronized void ActivateBot()
    {
        Bot = Sessione.BotQ;
        
        Sessione.AggiornaBot();
    }
    
    /**
     * @METHOD GetCurrentSuggestion
     * 
     * @OVERVIEW Metodo che recepisce l'indice della carta e della combinazione
     *           suggerita e li memorizza in apposite variabili d'istanza.
     */
    public synchronized void GetCurrentSuggestion()
    {
        SuggestedCard = Sessione.CardSuggestion();
        
        SuggestedComb = Sessione.CombinationSuggestion();
        
        RegisterLastCards(SuggestedCard,SuggestedComb);
    }
    
    /**
     * @METHOR PlayCard
     * 
     * @OVERVIEW Metodo che comanda al bot di giocare la carta suggerita da questi
     *           e prendere le carte indicate dalla combinazione che questi ha
     *           precedentemente suggerito.
     */
    public synchronized void PlayCard()
    {
       LearnerCardsPlayed++; 
        
       Sessione.BotQ.Play();
       
       this.AggiornaKB(Sessione.Carte,mano);
    }
/*----FINE METODI DI GIOCO----*/    

/*----METODO DI ESECUZIONE----*/    
    /*
        @METHOD run
    
        @OVERVIEW Metodo che implementa il ciclo di vita del reinforcement learner che viene eseguito secondo
                  un ciclo state - action - reward quando arriva il suo turno
    */
    @Override
    public void run()
    {
        while(active)
        {
            if(YourTurn() && !Testing())
            {
                try
                {                                       
                    Thread.sleep(100);
                     Act();
                }
                catch(InterruptedException e)
                {
                    System.out.println("IL REINFORCEMENT LEARNER E' STATO INTERROTTA DURANTE LA SUA ESECUZIONE");
                    Sessione.Halt();
                }
            }
        }
        
        System.out.println("CPU TERMINA ESECUZIONE\n");
    }
/*----FINE METODO DI ESECUZIONE----*/        
}
