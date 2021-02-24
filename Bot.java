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
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Enrico Tomasi
 * 
 * @CLASS Bot
 * 
 * @OVERVIEW Classe che implementa un generico bot che impara a giocare tramite
 *           apprendimento di rinforzo
 */
public class Bot 
{
    Gioco Game;//Sessione di gioco in cui il bot opera
    
    Giocatore Player;//Giocatore aiutato dal bot
    
    Stato LastState;//Stato corrente
    int LastAction;//Azione scelta per lo stato corrente
    
    List<Mossa> Mosse = new ArrayList<>();//Mosse effettuabili nello stato corrente
    Set<Integer> MyActions;
    
    int StateCount = 98000;//Stati possibili (coefficiente binomiale 20 su 40)
    int ActionCount = 112;//Azioni possibili (3 carte per 34 combinazioni massimo per carta)

    int Card;//Indice della carta scelta
    int Comb;//Indice della combinazione di carte scelta
    
    int ExploredMoves;//Contatore delle nuove mosse esplorate
    int ExploitedMoves;//Contatore delle mosse memorizzate utilizzate
    
    int CardsPlayedGreedily;//Carte giocate quando non è possibile pensare ad un'azione in maniera efficiente
      
    Calendar Start;
    
    ArrayList<Long> DecisionsTimeDuration;
    
    public double MeanDecisionTime;
    
    Random RandomSelector;
    
    /**
     * @METHOD Bot
     * 
     * @OVERVIEW Metodo costruttore di un'istanza della classe Bot
     * 
     * @param G Sessione di gioco in cui il bot opera 
     */
    public Bot(Gioco G)
    {
        Game = G;
        Player = G.Player;
        LastState = null;
        LastAction = -1;
        RandomSelector = new Random();
        
        ExploredMoves = 0;
        ExploitedMoves = 0;
        
        CardsPlayedGreedily = 0;
        
        DecisionsTimeDuration = new ArrayList();
        
        MeanDecisionTime = 0;
    }
    
    /**
     * @METHOD ObserveLearnerState
     * 
     * @OVERVIEW Metodo che "osserva" lo stato corrente copiandolo nell'apposita variabile
     *           d'istanza della classe
     * 
     * @param St Stato corrente da osservare 
     */
    public void ObserveLearnerState(Stato St)
    {
        LastState = St;
    }
    
    /**
     * @METHOD GetPossibleActions
     * 
     * @OVERVIEW Metodo che reperisce le azioni effettuabili nello stato corrente
     *           analizzando le carte presenti sul tavolo.
     * 
     * @return Result Set di interi codificanti ognuno una specifica azione istanza
     *         della classe Action.
     */
    public Set<Integer> GetPossibleActions()
    {   
        List<Integer> Actions = new LinkedList<>();
        
        ArrayList<Carta> CurrentHand = LastState.Hand;
        
        int Action;
        
        if(!CurrentHand.isEmpty())
        {
            for(Carta C : CurrentHand)
            {
               if(C.IsMarked() || C.IsAnAce())
               {
                   for(int i=1;i<=C.Potenziale.size();i++)
                   {
                       Action = ComposeAction(CurrentHand.indexOf(C)+1,i);
                       Actions.add(Action);
                   }
               }
               else
               {
                   Action = ComposeAction(CurrentHand.indexOf(C)+1,0);
                   Actions.add(Action);
               }
            }
        }

        Set<Integer> Result = Actions.stream().collect(Collectors.toSet());
        
        return Result;
    }
    
    /**
     * @METHOD ClearMoves
     * 
     * @OVERVIEW Metodo che svuota l'array delle possibili mosse trovate dal bot
     */
    public void ClearMoves()
    {
        Mosse.clear();
    }
    
    /**
     * @METHOD ComposeAction
     * 
     * @OVERVIEW Metodo che, dati due numeri interi rappresentanti l'indice della
     *           carta giocabile e della combinazione possibile, li unisce formando
     *           un codice che identifica la suddetta azione mediante questa formula:
     * 
     *              (Indice carta * 100) + Indice Combinazione
     * 
     * @param i Intero rappresentante l'indice della carta giocabile
     * @param j Intero rappresentante l'indice della combinazione possibile
     * 
     * @return Result Intero rappresentante l'azione costituente nel giocare la
     *         carta indicizzata dall'intero i ed effettuare la presa denotata 
     *         dall'intero j.
     */
    public int ComposeAction(int i,int j)
    {
        int Result = (i*100)+j;
        
        return Result;      
    }
    
    public void PrintActions(List<Integer> Actions)
    {
        for(Integer I : Actions)
        {
            System.out.println("ACTION AVAILABLE:"+I);
        }
    }
    
    public void CalculateMeanDecisionTime()
    {
        long Sum = 0;
        int Decisions = DecisionsTimeDuration.size();
        
        for(long L : DecisionsTimeDuration)
        {
            Sum += L;
        }
        
        long FinalValue = Sum / Decisions;
        double Result = FinalValue / 1000.00;
        
        MeanDecisionTime = Result;
    }
  
}
