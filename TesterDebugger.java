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
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    @CLASS TesterDebugger

    @OVERVIEW: Classe che implementa una serie di funzioni che testano il funzionamento
    del gioco, in particolare del calcolo dei potenziali di ogni carta mediante DFS
    e della ricerca della soluzione ottima via algoritmo MiniMax con potatura Alfa-Beta
*/
public class TesterDebugger 
{
    /*----VARIABILI D'ISTANZA----*/
    
    Gioco Sessione;
    
    CPU Computer;
    Giocatore P1;
    
    ArrayList<Carta> Mz;
    ArrayList<Carta> Tav;
    
    /*
        Intero rappresentante il numero di volte in cui la configurazione
        Validity Configuration, che testa la "regola dei quattro re" è stata
        chiamata, utilizzato per impedire al gioco di andare in loop sul test
        della configurazione in questione
    */
    int Attempts;
    
    Board B;
    
    /*----FINE VARIABILI D'ISTANZA----*/
    
    /*----METODI COSTRUTTORI----*/
    
    /**
     * @METHOD TesterDebugger(game)
     * 
     * @OVERVIEW Metodo che istanzia un oggetto di tipo TesterDebugger volto a testare
     *           il corretto funzionamento dei metodi e l'integrità delle proprietà
     *           di un'istanza della classe Gioco
     * 
     * @param game : Istanza della classe Gioco rappresentante la sessione di gioco corrente
     */
    public TesterDebugger(Gioco game)
    {
        Sessione = game;
        Computer = game.CPU;
        P1 = game.Player;
        Mz = game.Carte;
        Tav = game.Tavolo;
        
        Attempts = 0;
    }
    
       /**
     * @METHOD TesterDebugger(board)
     * 
     * @OVERVIEW Metodo che istanzia un oggetto di tipo TesterDebugger volto a testare
     *           il corretto funzionamento dei metodi e l'integrità delle proprietà
     *           di un'istanza della classe Board
     * 
     * @param board : Istanza della classe Board rappresentante l'interfaccia su cui l'utente
     *        interagisce al momento della chiamata di questo metodo
     */
    public TesterDebugger(Board board)
    {
        B = board;
    }
    
    /*----FINE METODI COSTRUTTORI----*/
    
    /*----METODI DI INIZIALIZZAZIONE----*/
    
    /**
     * @METHOD Set
     * 
     * @OVERVIEW Metodo che inizializza l'oggetto di tipo TesterDebgger in questione
     *           assegnandogli il tavolo da gioco ed il mazzo della sessione in cui questo
     *           è stato precedentemente istanziato come Tavolo e Mazzo su cui chiamare
     *           i propri metodi
     * 
     * @param Tavolo : ArrayList di oggetti di tipo Carta rappresentante il tavolo da gioco 
     *        e la sua configurazione corrente
     * 
     * @param Mazzo : ArrayList di oggetti di tipo Carta rappresentante il mazzo di carte del gioco 
     *        e la sua configurazione corrente
    */
    public void Set(ArrayList<Carta> Tavolo,ArrayList<Carta> Mazzo)
    {
        Mz = Mazzo;
        Tav = Tavolo;
    }
    
    /*----FINE METODI DI INIZIALIZZAZIONE----*/
    
    /*----METODI DI CONFIGURAZIONE DEL GIOCO----*/
        
    /**
     * @METHOD IdemConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              *Carte distribuite casualmente*
     * 
     *           CARTE SUL TAVOLO:
     *              *Uguale valore delle carte nella mano del giocatore ma diverso seme*
     * 
     *           MANO DELLA CPU:
     *              *Uguale valore delle carte sul tavolo ma diverso seme*
     * 
     * La configurazione viene utilizzata principalmente per verificare che è possibile, nel caso
     * sul tavolo siano presenti carte di uguale valore, effettuare prese prendendo solo ed esclusivamente
     * queste ultime
     */
    public void IdemConfiguration()
    {
        ClearCards();
        
        RandomHand(P1);
        
        while(P1.GetMano().get(0).IsAnAce() || P1.GetMano().get(1).IsAnAce() || P1.GetMano().get(2).IsAnAce())
        {
            RandomHand(P1);
        }
        
        char a,b,c;
        
        a = DifferenziaSeme(P1.GetMano().get(0));
        b = DifferenziaSeme(P1.GetMano().get(1));
        c = DifferenziaSeme(P1.GetMano().get(2));
       
        
        Tav.add(0,new Carta(a,P1.GetMano().get(0).GetValue()));
        Tav.add(1,new Carta(b,P1.GetMano().get(1).GetValue()));
        Tav.add(2,new Carta(c,P1.GetMano().get(2).GetValue()));
        
        a = DifferenziaSeme(Tav.get(0));
        b = DifferenziaSeme(Tav.get(1));
        c = DifferenziaSeme(Tav.get(2));
        
        Computer.mano.add(0,new Carta(a,Tav.get(0).GetValue()));
        Computer.mano.add(1,new Carta(b,Tav.get(1).GetValue()));
        Computer.mano.add(2,new Carta(c,Tav.get(2).GetValue()));
    }   

    /**
     * @METHOD IdealCombinationConf
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              *Carte distribuite casualmente*
     *              
     *              -VMAX = Valore massimo delle carte presenti nella mano del giocatore (parte intera superiore)
     * 
     *           CARTE SUL TAVOLO:
     *              - | Denari , (VMAX) | 
     * 
     *              - | Bastoni , (VMAX/2) |
     *              - | Coppe , ( (VMAX) - (VMAX/2)) |
     * 
     *              - | Bastoni , (VMAX / 3) |
     *              - | Coppe , (VMAX / 3) |
     *              - | Denari , (VMAX) - (VMAX / 3) |
     * 
     *           MANO DELLA CPU:
     *              *Carte distribuite casualmente*
     * 
     * La configurazione viene utilizzata principalmente per elencare le prese possibili nel caso nel tavolo
     * siano presenti insiemi rispettivamente di due e tre carte la cui somma è uguale al
     * valore della carta di valore massimo nella mano del giocatore
     */
    public void IdealCombinationConf()
    {       
        ClearCards();
        
        RandomHand(P1);
        
        int V1 = P1.GetMano().get(0).GetValue();
        int V2 = P1.GetMano().get(1).GetValue();
        int V3 = P1.GetMano().get(2).GetValue();
        
        int Value1 = Math.max(V1,V2);
        Value1 = Math.max(Value1, V3);

        int Value2 = (int) Math.ceil(Value1 / 2);
        int Value3 = (int) Math.ceil(Value1 / 3);
        
        RandomHand(Computer);

        Tav.add(new Carta('b',Value2));
        Tav.add(new Carta('c',(Value1 - Value2)));
        
        Tav.add(new Carta('b',Value3));
        Tav.add(new Carta('c',Value3));
        Tav.add(new Carta('d',(Value1 - (2*Value3))));
        
    }
    
    /**
     * @METHOD MaxCombinationConf
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              - | Denari , 10 |
     *              - | Spade , 10 |
     *              - | Bastoni , 10 |
     *              
     *              -VMAX = Valore massimo delle carte presenti nella mano del giocatore (parte intera superiore)
     * 
     *           CARTE SUL TAVOLO:
     *               -| Coppe , 5 |
     *               -| Denari , 5 |
     *               -| Spade , 5 |
     *               -| Bastoni , 5 |
     *               -| Coppe , 3 |
     *               -| Denari , 3 |
     *               -| Spade , 3 |
     *               -| Bastoni , 1 |
     *               -| Coppe , 2 |
     *               -| Spade , 2|
     * 
     *           MANO DELLA CPU:
     *              *Carte distribuite casualmente*
     * 
     * La configurazione viene utilizzata principalmente per determinare qual è, dato il modo in cui il tavolo è stato configurato
     * il massimo numero di combinazioni ricavabili da una carta (ovvero 38) quando sul tavolo sono state gettate tutte le carte
     * delle mani degli avversari durante la mano precedente e non sono state effettuate prese (caso molto improbabile 
     * ma che lo sviluppatore ha ritenuto opportuno considerare)
     *
     */
    public void MaxCombinationConf()
    {
        Carta C1 = new Carta('d',10);
        Carta C2 = new Carta('s',10);
        Carta C3 = new Carta('b',10);
        
        Mz.remove(C1);
        Mz.remove(C2);
        Mz.remove(C3);

        ClearCards();
        
        P1.mano.add(0,C1);  
        P1.mano.add(1,C2);  
        P1.mano.add(2,C3);
        
        RandomHand(Computer);
        
        Tav.clear();
        
        Tav.add(new Carta('c',5));
        Tav.add(new Carta('d',5));
        Tav.add(new Carta('s',5));
        Tav.add(new Carta('b',5));
        Tav.add(new Carta('c',3));
        Tav.add(new Carta('d',3));
        Tav.add(new Carta('s',3));
        Tav.add(new Carta('b',1));
        Tav.add(new Carta('c',2));
        Tav.add(new Carta('s',2));
        
        Collections.sort(Tav);
    }
    
    /**
     * @METHOD AceConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              - | Denari , 1 |
     *              
     *           CARTE SUL TAVOLO:
     *               -| Bastoni , 1 |
     *               -| Coppe , 1 |
     *               -| Denari , 4 |
     * 
     *           MANO DELLA CPU:
     *              - | Spade , 1 |
     * 
     * La configurazione viene utilizzata per verificare che, nel caso gli avversari abbiano un asso nelle proprie mani
     * ed in tavola siano presenti assi, è possibile prendere esclusivamente questi ultimi effettuando una scopa per ognuno 
     * di essi
     *
     */
    public void AceConfiguration()
    {   
        ClearCards();
        
        Carta C1 = new Carta('d',1);
        Carta C2 = new Carta('s',1);
        Carta C3 = new Carta('b',1);
        Carta C4 = new Carta('c',1);       
        Carta C5 = new Carta ('d',4);
        
        Mz.remove(C1);
        Mz.remove(C2);
        Mz.remove(C3);
        Mz.remove(C4);
        Mz.remove(C5);
        
        P1.mano.add(0,C1);     
        
        Computer.mano.add(0,C2);
        
        Tav.add(0,C3);
        Tav.add(0,C4);
        Tav.add(0,C5);
        
        Collections.sort(Tav); 
    }
    
     /**
     * @METHOD ReverseAceConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              - | Denari , 1 |
     *               -| Bastoni , 1 |
     * 
     *           CARTE SUL TAVOLO:
     *               -| Denari , 10 |
     *               -| Spade , 5 |
     *               -| Coppe , 7 |
     *               -| Denari , 7 |
     * 
     *           MANO DELLA CPU:
     *               -| Coppe , 1 |
     *              - | Spade , 1 |
     * 
     * La configurazione viene utilizzata per verificare che, nel caso gli avversari abbiano un asso nelle proprie mani
     * ed in tavola non siano presenti assi, è possibile prendere tutte le carte senza effettuare scopa
     *
     */
    public void ReverseAceConfiguration()
    {
        ClearCards();
        
        Carta C1 = new Carta('d',1);
        Carta C2 = new Carta('b',1);
        Carta C3 = new Carta('c',1);
        Carta C4 = new Carta('s',1);
        
        Mz.remove(C1);
        Mz.remove(C2);
        Mz.remove(C3);
        Mz.remove(C4);

        P1.mano.add(0,C1);      
        P1.mano.add(1,C2); 
        
        Computer.mano.add(0,C3);
        Computer.mano.add(1,C4);
        
        Tav.add(new Carta('d',10));
        Tav.add(new Carta('s',5));
        Tav.add(new Carta('c',7));
        Tav.add(new Carta('d',7));

        Collections.sort(Tav); 
    }
    
    /**
     * @METHOD ScopaConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              - | Denari , 10 |
     *              - | Spade , 7 |
     *               -| Bastoni , 1 |
     * 
     *           CARTE SUL TAVOLO:
     *               -| Spade , 10 |
     *               -| Denari , 7 |
     * 
     *           MANO DELLA CPU:
     *               *Carte distribuite casualmente*
     * 
     * La configurazione viene utilizzata per testare la possibilità da parte del giocatore di effettuare
     * scopa nel caso decida di non giocare l'asso
     *
     */
    public void ScopaConfiguration()
    {
        Carta C1 = new Carta('d',10);
        Carta C2 = new Carta('s',7);
        Carta C3 = new Carta('b',1);
        
        ClearCards();
        
        Mz.remove(C1);
        Mz.remove(C2);
        Mz.remove(C3);

        P1.mano.add(0,C1);      
        P1.mano.add(1,C2);    
        P1.mano.add(2,C3);
        
        RandomHand(Computer);
        
        Tav.add(new Carta('s',10));
        Tav.add(new Carta('d',7));
     
        Collections.sort(Tav); 
    }
    
    /**
     * @METHOD ValidityConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:

     *           CARTE SUL TAVOLO:
     *               -| Coppe , 10 |
     *               -| Bastoni , 10 |
     *               -| Denari , 10 |

     * 
     * La configurazione viene utilizzata per testare la "regola dei tre re" una ed una volta sola
     * in modo da non mandare il gioco in loop sul metodo, a tal scopo viene testata la variabile
     * "Attempts" dell'istanza TesterDebugger che invoca il metodo.
     * 
     *  ----NOTA----
     *  REGOLA DEI TRE RE: Affinché la prima mano della partita sia valida devono esserci meno
     *  di tre carte re (valore 10) sul tavolo
     *
     */
    public void ValidityConfiguration()
    {
        if(Attempts == 0)
        {
            if(!Tav.isEmpty())
            {
                Tav.clear();
            }

            Tav.add(new Carta('c',10));
            Tav.add(new Carta('b',10));
            Tav.add(new Carta('d',10));
            
            Attempts++;
        }
    }

     /**
     * @METHOD EmptyTableConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              *Carte distribuite casualmente*
     * 
     *           CARTE SUL TAVOLO:
     *              *Tavolo vuoto*
     * 
     *           MANO DELLA CPU:
     *               *Carte distribuite casualmente*
     * 
     * La configurazione viene utilizzata principalmente al fine di testare che, se il tavolo è vuoto, non 
     * è possibile effettuare alcuna presa
     *
     */
    public void EmptyTableConfiguration()
    {
        ClearCards();
        
        RandomHand(P1);
        RandomHand(Computer);
    }
    
      /**
     * @METHOD EmptyPlayerHandConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              *Mano vuota*
     * 
     *           CARTE SUL TAVOLO:
     *              *Carte distribuite casualmente*
     * 
     *           MANO DELLA CPU:
     *               *Carte distribuite casualmente*
     * 
     * La configurazione viene utilizzata principalmente al fine di testare che, se la sua mano è vuota, il giocatore
     * non può effettuare prese
     *
     */
    public void EmptyPlayerHandConfiguration()
    {
        ClearCards();
        
        RandomTable();
        RandomHand(Computer);
    }
    
       /**
     * @METHOD EmptyCPUHandConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              *Carte distribuite casualmente*
     * 
     *           CARTE SUL TAVOLO:
     *              *Carte distribuite casualmente*
     * 
     *           MANO DELLA CPU:
     *               *Mano vuota*
     * 
     * La configurazione viene utilizzata principalmente al fine di testare che, se la sua mano è vuota, la CPU
     * non può effettuare prese
     *
     */
    public void EmptyCPUHandConfiguration()
    {
        ClearCards();
        
        RandomTable();
        RandomHand(P1);
    }

      /**
     * @METHOD NoPotentialsConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              - | Bastoni , 2 |
     *              - | Spade , 2 |
     * 
     *           CARTE SUL TAVOLO:
     *              - | Spade , 3 |
     *              - | Denari , 3 |
     * 
     *           MANO DELLA CPU:
     *              - | Denari , 2 |
     *              - | Spade , 4 |
     * 
     * La configurazione viene utilizzata per testare che, quando il tavolo è configurato in modi simili a questo
     * nè il giocatore, nè la CPU possono effettuare prese
     *
     */
    public void NoPotentialsConfiguration()
    {
        ClearCards();
        
        Carta C1 = new Carta('s',3);
        Carta C2 = new Carta('d',3);
        Carta C3 = new Carta('b',2);
        Carta C4 = new Carta('s',2);
        Carta C5 = new Carta('d',2);
        Carta C6 = new Carta('s',4);
        
        Mz.remove(C1);
        Mz.remove(C2);
        Mz.remove(C3);
        Mz.remove(C4);
        Mz.remove(C5);
        Mz.remove(C6);
        
        Tav.add(C1);
        Tav.add(C2);
        
        P1.mano.add(C3);
        P1.mano.add(C4);
        
        Computer.mano.add(C5);
        Computer.mano.add(C6);
        
    }
    
     /**
     * @METHOD AIConfiguration
     * 
     * @OVERVIEW Metodo che configura il gioco nel seguente modo:
     * 
     *           MANO DEL GIOCATORE:
     *              - | Coppe , 3 |
     *              - | Coppe , 9 |
     *              - | Bastoni , 5 |
     *
     *           CARTE SUL TAVOLO:
     *              - | Coppe , 8 |
     *              - | Coppe , 5 |
     *              - | Bastoni , 4 |
     *              - | Spade , 3 |
     * 
     *           MANO DELLA CPU:
     *              - | Denari , 8|
     *              - | Spade , 5 |
     *              - | Denari , 10 |
     * 
     * Questa è una particolare configurazione che è stata utilizzata per ottimizzare al meglio
     * la generazione degli stati dell'albero di decisione ed è stata infine scelta come configurazione
     * standard per testare l'intelligenza artificiale della CPU
     *
     */
    public void AIConfiguration()
    {
        ClearCards();
        
        Carta C1 = new Carta('d',8);
        Carta C2 = new Carta('s',5);
        Carta C3 = new Carta('d',10);
        
        Carta C4 = new Carta('c',8); 
        Carta C5 = new Carta('c',5);
        Carta C6 = new Carta('b',4);
        Carta C7 = new Carta('s',3);
        
        Carta C8 = new Carta('c',3);
        Carta C9 = new Carta('c',9);
        Carta C10 = new Carta('b',5);
        
        Mz.remove(C1);
        Mz.remove(C2);
        Mz.remove(C3);
        Mz.remove(C4);
        Mz.remove(C5);
        Mz.remove(C6);
        Mz.remove(C7);
        Mz.remove(C8);
        Mz.remove(C9);
        Mz.remove(C10);
        
        Computer.mano.add(C1);
        Computer.mano.add(C2);
        Computer.mano.add(C3);
        
        Tav.add(C4);
        Tav.add(C5);
        Tav.add(C6);
        Tav.add(C7);
        
        P1.mano.add(C8);
        P1.mano.add(C9);
        P1.mano.add(C10);
    }
    
    /*----FINE METODI DI CONFIGURAZIONE DEL GIOCO----*/
    
    /*----METODI DI TESTING FINALE-----*/
    
    /**
    * @METHOD FinalTest
    * 
    * @OVERVIEW Metodo di testing definitivo utilizzato all'interno della classe gioco
    *           che consiste nell'esecuzione in sequenza di un test della visita DFS
    *           per il calcolo delle prese possibili e di un test dell' intelligenza
    *           artificiale della CPU e della sua capacità di inferenza
    */
    public synchronized void FinalTest()
    {
        DFSTest();

        AITest();
    }
    
    /**
     * @METHOD DFSTest
     * 
     * @OVERVIEW Metodo che effettua un test della visita DFS per il calcolo delle prese possibili configurando
     *           il gioco secondo diverse configurazioni notevoli volte a testare il maggior numero possibile di 
     *           configurazioni e di risultati della visita DFS
     *           
    */
    public void DFSTest()
    {   
        /*CASO 0-A: La tavola è vuota (se le mani del giocatore o della CPU sono vuote è banale 
                  il fatto che i due giocatori non possano prendere carte)*/
        System.out.println("-----TEST DFS, CASO 0-A: TAVOLO VUOTO----");
        
        EmptyTableConfiguration();
        
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
        
        System.out.println("VERIFICHIAMO CHE IL GIOCATORE NON PUO' PRENDERE NULLA: ");
        
        for(Carta C : P1.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("VERIFICHIAMO CHE LA CPU NON PUO' PRENDERE NULLA: ");
        
        for(Carta C : Computer.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("---------------------------------------------------\n");
        
        SpaceOutputConsole();
        
        PauseBetweenTests();
        
        /*CASO 0-B: Nessuna presa effettuabile da parte della CPU o del giocatore*/
        System.out.println("-----TEST DFS, CASO 0-B: NESSUNA PRESA POSSIBILE----");
        
        NoPotentialsConfiguration();
        
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
        
        System.out.println("VERIFICHIAMO CHE IL GIOCATORE NON PUO' PRENDERE NULLA: ");
        
        for(Carta C : P1.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("VERIFICHIAMO CHE LA CPU NON PUO' PRENDERE NULLA: ");
        
        for(Carta C : Computer.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("---------------------------------------------------\n");
        
        PauseBetweenTests();
        
        SpaceOutputConsole();
         
        /*CASO 1: Il giocatore può prendere unicamente carte dello stesso valore all'interno del tavolo di gioco*/
        System.out.println("-----TEST DFS, CASO 1: CARTE DELLO STESSO VALORE----");

        IdemConfiguration();
        
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
        
        System.out.println("VERIFICHIAMO CHE IL GIOCATORE PUO' PRENDERE ESATTAMENTE LE CARTE DELLO STESSO VALORE ");
        
        for(Carta C : P1.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("VERIFICHIAMO CHE LA CPU PUO' PRENDERE ESATTAMENTE LE CARTE DELLO STESSO VALORE  ");
        
        for(Carta C : Computer.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("---------------------------------------------------\n");
        
        PauseBetweenTests();
        
        SpaceOutputConsole();
        
       /*CASO 2: Il giocatore può prendere unicamente gli assi all'interno del tavolo di gioco*/ 
        System.out.println("-----TEST DFS, CASO 2: ASSO PIGLIA ASSO----");
        
        AceConfiguration();
        
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
        
        System.out.println("VERIFICHIAMO CHE IL GIOCATORE PUO' PRENDERE ESCLUSIVAMENTE GLI ASSI: ");
        
        for(Carta C : P1.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("VERIFICHIAMO COSA IL GIOCATORE PUO' PRENDERE ESCLUSIVAMENTE GLI ASSI: ");
        
        for(Carta C : Computer.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("---------------------------------------------------\n");
        
        PauseBetweenTests();
        
        SpaceOutputConsole();
        
        /*CASO 3: Il giocatore può prendere tutte le carte sul tavolo grazie ad un asso all'interno della sua mano*/
        System.out.println("-----TEST DFS, CASO 3: ASSO PIGLIA TUTTO----");
        System.out.println("Non ci sono assi in campo, quindi un asso può prendere tutte le carte");
        
        ReverseAceConfiguration();
        
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
        
        System.out.println("VERIFICHIAMO CHE IL GIOCATORE PUO' PRENDERE TUTTE LE CARTE SUL TAVOLO: ");
        
        for(Carta C : P1.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("VERIFICHIAMO COSA IL GIOCATORE PUO' PRENDERE TUTTE LE CARTE SUL TAVOLO: ");
        
        for(Carta C : Computer.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("---------------------------------------------------\n");
        
        PauseBetweenTests();
        
        SpaceOutputConsole();
        
        /*CASO 4: Configurazione con insiemi rispettivamente di due e di tre carte come prese possibili dalla carta di 
                  valore massimo*/
        System.out.println("-----TEST DFS, CASO 4: CONFIGURAZIONE STANDARD----");
        
        IdealCombinationConf();
        
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
        
        System.out.println("VERIFICHIAMO LE CARTE CHE IL GIOCATORE PUO' PRENDERE: ");
        
        for(Carta C : P1.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("VERIFICHIAMO LE CARTE CHE LA CPU PUO' PRENDERE: ");
        
        for(Carta C : Computer.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("---------------------------------------------------\n");
        
        PauseBetweenTests();
        
        SpaceOutputConsole();
        
        /*CASO 5: Stress-test con la configurazione che genera il numero massimo di prese possibili*/
        System.out.println("-----TEST DFS, CASO 5: CONFIGURAZIONE STRESSING----");
        System.out.println("Questa configurazione mostra il limite di configurazioni possibili all'interno del gioco");
        
        MaxCombinationConf();
        
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
        
        System.out.println("VERIFICHIAMO LE CARTE CHE IL GIOCATORE PUO' PRENDERE: ");
        
        for(Carta C : P1.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("VERIFICHIAMO LE CARTE CHE LA CPU PUO' PRENDERE: ");
        
        for(Carta C : Computer.mano)
        {
            C.StampaPotenziali();
        }
        
        System.out.println("---------------------------------------------------\n");
        
        PauseBetweenTests();
        
        SpaceOutputConsole();
    }
    
      /**
     * @METHOD AITest
     * 
     * @OVERVIEW Metodo che effettua  test dell' intelligenza artificiale della CPU e della sua capacità di inferenza 
     *           configurando il gioco secondo diverse configurazioni notevoli volte a testare il maggior numero possibile di 
     *           configurazioni e di risultati della generazione dello spazio degli stati e della valutazione della carta
     *           di guadagno maggiore
     *           
    */
    public void AITest()
    {
        /*CASO 0: Generazione degli stati in una tavola vuota, uno stato con guadagno nullo*/
         System.out.println("-----TEST AI, CASO 0: TAVOLA VUOTA----");
         
         EmptyTableConfiguration();
         
         Sessione.RivalutaPotenziale();
         
         PrintAllCardSets();
         
         ComputerPerceptionCycle();
         
         PrintDecisionTree();
         
        System.out.println("---------------------------------------------------\n");       
                
        SpaceOutputConsole();
        
        PauseBetweenTests();

        /*CASO 1: Generazione degli stati con la mano della CPU vuota, uno stato con guadagno nullo*/
        System.out.println("-----TEST AI, CASO 1: MANO CPU VUOTA----");
         
        EmptyCPUHandConfiguration();
         
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
         
        ComputerPerceptionCycle();
        
        PrintDecisionTree();
         
        System.out.println("---------------------------------------------------\n");
                      
        SpaceOutputConsole();      
        
        PauseBetweenTests();
        
        /*CASO 2: Generazione degli stati con la mano del giocatore vuota, tre stati con relativo guadagno*/
        System.out.println("-----TEST AI, CASO 2: MANO GIOCATORE VUOTA----");
         
        EmptyPlayerHandConfiguration();
         
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
         
        ComputerPerceptionCycle();
        
        PrintDecisionTree();
         
        System.out.println("---------------------------------------------------\n");      
                
        SpaceOutputConsole();      
        
        PauseBetweenTests();
        
        /*CASO 3: Generazione dell'albero degli stati in una configurazione standard*/
        System.out.println("----TEST AI, CASO 3: CONFIGURAZIONE STANDARD");
        
        AIConfiguration();
        
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
        
        ComputerPerceptionCycle();
        
        PrintDecisionTree();
         
        System.out.println("---------------------------------------------------\n");        
                
        SpaceOutputConsole();      
        
        PauseBetweenTests();
        
        /*CASO 4: Stress-test, generazione dell'albero degli stati nella configurazione che genera il massimo potenziale*/
        System.out.println("----TEST AI, CASO 4: CONFIGURAZIONE STRESSING");
        
        MaxCombinationConf();
        
        Sessione.RivalutaPotenziale();
        
        PrintAllCardSets();
        
        ComputerPerceptionCycle();
        
        PrintDecisionTree();
    }
    
    /*----FINE METODI DI TESTING FINALE-----*/
    
    /*----METODI DI TESTING SECONDARI----*/
    
    /**
     * @METHOD SlotTest
     * 
     * @OVERVIEW Metodo che testa l'effettivo assegnamento delle carte della mano
     *           del giocatore, della mano della CPU e del tavolo di gioco alle
     *           rispettive slot dell'interfaccia
     */
    public void SlotTest()
    {
        ArrayList<Slot> Pl = B.PlayerHand;
        ArrayList<Slot> Cpu = B.CPUHand;
        ArrayList<Slot> T = B.CardsOnTable;
        
        System.out.println("SLOT MANO GIOCATORE: ");
        
        for(Slot S : Pl)
        {         
            if(S.HasCard())
            {
                Carta C = S.GetCard();
                
                System.out.print("NOME CARTA: "+C.nome);
                C.StampaPotenziali();
            }
        }
        
        System.out.println("SLOT CPU: ");
        
        for(Slot S : Cpu)
        {          
            if(S.HasCard())
            {
                Carta C = S.GetCard();
                
                System.out.print("NOME CARTA: "+C.nome);
                C.StampaPotenziali();
            }
        }
        
        System.out.println("SLOT TAVOLO: ");
        
        for(Slot S : T)
        {           
            if(S.HasCard())
            {
                Carta C = S.GetCard();
                
                System.out.print("NOME CARTA: "+C.nome);
                C.StampaPotenziali();
            }
        }
    }
   
    /**
     * @METHOD TestDistribuzioneMano
     * 
     * @OVERVIEW Metodo che testa il funzionamento del metodo Distribuisci della classe Gioco
     *           iterando fino a quando non ha distribuito tutte le carte del mazzo sul tavolo
     *           tracciando di volta in volta il numero di carte residue all'interno del mazzo e
     *           sgombrando le mani degli avversari ed il tavolo da gioco in modo da stampare
     *           le carte di quest'ultimo in output senza creare stringhe troppo lunghe
     */
    public void TestDistribuzioneMano()
    {
        int i=0;
        
        while(!Sessione.MazzoVuoto())
        {
            System.out.println("MANO NUMERO: "+i);
            
            System.out.println("CARTE NEL MAZZO: "+Sessione.Carte.size());
            
            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(TesterDebugger.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            StampaTavolo();
            Sessione.ManiScoperte();
            
            Sessione.SgomberaTavolo();
            Sessione.GettaMani();
            Sessione.Distribuisci();
            
            i++;
           
        }
    }
    
    /**
     * @METHOD TestDistribuzioneMano
     * 
     * @OVERVIEW Metodo che testa l'effettivo riconoscimento della fine della partita da parte del gioco
     *           iterando più volte il metodo TestDistribuzioneMano sopra implementato e dichiarando infine
     *           il punteggio dei giocatori in maniera fittizia (non viene giocata alcuna carta dato che le mani
     *           degli avversari ed il tavolo di gioco vengono svuotati di continuo) e stampandolo in output
     *           Alla fine, sgombera questi ultimi per un'altra volta e termina
     */
    public void TestFinePartita()
    {
        TestDistribuzioneMano();
        
        Punteggio CPUPoints = Sessione.DichiaraPunteggioCPU();
        Punteggio PlayerPoints = Sessione.DichiaraPunteggioGiocatore();
        
        CPUPoints.SetCount(0,22);
        CPUPoints.SetCount(1,7);
        CPUPoints.SetCount(3,2);
        CPUPoints.SetCount(4,0);
        CPUPoints.SetCount(5,2);
        
        PlayerPoints.SetCount(0,18);
        PlayerPoints.SetCount(1,3);
        PlayerPoints.SetCount(3,4);
        PlayerPoints.SetCount(4,1);
        PlayerPoints.SetCount(5,4);
        
        
        System.out.println("PUNTEGGIO CPU\n");
        CPUPoints.ScorePrint();
        
        System.out.println("PUNTEGGIO GIOCATORE\n");
        PlayerPoints.ScorePrint();
        
        if(Sessione.MazzoVuoto())
        {
            Sessione.SgomberaTavolo();
            Sessione.GettaMani();
            Sessione.Distribuisci();
        }
    }

    /**
     * @METHOD HaltTest
     * 
     * @OVERVIEW Metodo che testa il funzionamento effettivo del metodo di arresto del gioco
     *           invocando il metodo Halt della classe Gioco ed arrestando così il programma
    */
    public void HaltTest()
    {
        Sessione.Halt();
    }
    
    /*----FINE METODI DI TESTING SECONDARI----*/
    
    /*----METODI AUSILIARI----*/
    
    /**
    * @METHOD ComputerPerceptionCycle
    * 
    * @OVERVIEW Metodo che esegue in sequenza il ciclo percepisci - pianifica - agisci
    *           della CPU aggiornandone di volta in volta la Knowledge Base e consentendole
    *           di valutare ad ogni chiamata le prese possibili
    */
    public void ComputerPerceptionCycle()
    {
        Stato State = Computer.Observe();
         
         Sessione.AggiornaKB();
         
         Sessione.RivalutaPotenziale();
         
         Computer.Plan(State);
    }
    
    /**
     * @METHOD DifferenziaSeme
     * 
     * @OVERVIEW Metodo che, data una carta in input, ne cambia il seme in accordo
     *           ad uno schema definito nel seguente modo:
     * 
     *           |Seme originale| -> |Seme nuovo|
     *              Bastoni             Spade
     *              Spade               Coppe
     *              Coppe               Denari
     *              Denari              Bastoni
     * 
     *           Il metodo viene utilizzato per garantire che in alcuni casi alle carte
     *           all'interno delle mani degli avversari e del tavolo di gioco siano assegnate
     *           carte di diverso seme, in modo che non compaiano carte uguali fra nessuno
     *           di questi insiemi di carte
     * 
     * @param C : Carta in esame
     * @return SemeNuovo : Carattere rappresentante il nuovo seme attraverso cui istanziare un'altra carta
     *         preferibilmente di valore uguale a quella data in input
     */
    public char DifferenziaSeme(Carta C)
    {
        String Seme = C.seme;
        
        if(Seme.equals("bastoni"))
        {
            return 's';
        }
        else if(Seme.equals("spade"))
        {
            return 'c';
        }
        else if(Seme.equals("coppe"))
        {
            return 'd';
        }
        else
        {
            return 'b';
        }
        
    }
    
    /**
     * @METHOD PauseBetweenTests
     * 
     * @OVERVIEW Metodo che mette in pausa il thread per meno di un minuto tra un caso di test e l'altro
     *           in modo da garantire un certo interleaving ed aumentare la leggibilità e la comprensibilità
     *           dei risultati dei test in output
     */
    public void PauseBetweenTests()
    {
        try 
        {
            Thread.sleep(750);
        } 
        catch (InterruptedException ex) 
        {
            System.out.println("ERRORE: INTERRUZIONE ANOMALA DELL'ESECUZIONE, IL PROGRAMMA VERRA' CHIUSO");
            Sessione.Halt();
        }
    }
    
    /**
    * @METHOD ClearCards
    * 
    * @OVERVIEW Metodo che rimuove tutte le carte dalla mano del giocatore, dalla mano della CPU e dal tavolo di gioco
    */
    public void ClearCards()
    {
          if(!Tav.isEmpty())
            {
                Tav.clear();
            }
            
            if(!Computer.mano.isEmpty())
            {
                Computer.mano.clear();
            }
            
            if(!P1.mano.isEmpty())
            {
                P1.mano.clear();
            }
    }
    
    /**
     * @METHOD GeneraRandom
     * 
     * @OVERVIEW Metodo che genera una carta da un seme ed un valore scelti in maniera randomizzata
     *           tra quelli possibili
     * 
     * @return C : Carta generata da un seme ed un valore scelti in maniera randomizzata tra quelli possibili
     */
    public Carta GeneraRandom()
    {
        int indice, valore = 0;
        
        char seme;
        
        Random R = new Random();
        
        char[] S = {'s','c','b','d'};
        
        indice = R.nextInt(4);
        
        seme = S[indice];
        
        valore = 1 + R.nextInt(10);
        
        Carta C = new Carta(seme,valore);

        return C;
    }
    
    /**
     * @METHOD RandomTable
     * 
     * @OVERVIEW Metodo che aggiunge quattro carte random al tavolo di gioco
     */
    public void RandomTable()
    {
        Tav.clear();
        
        Tav.add(0,GeneraRandom());
        Tav.add(1,GeneraRandom());
        Tav.add(2,GeneraRandom());
        Tav.add(3,GeneraRandom());
        
        for(Carta C : Tav)
        {
            Mz.remove(C);
        }
    }
    
    /**
     * @METHOD RandomHand
     * 
     * @OVERVIEW Metodo che aggiunge quattro carte random alla mano di un giocatore 
     *           dato in input dopo aver rimosso le carte presenti all'interno di quest'ultima
     * 
     * @param G : Giocatore alla cui mano vanno aggiunte quattro carte random
     */
    public void RandomHand(Giocatore G)
    {
        G.mano.clear();
        
        G.mano.add(0,GeneraRandom());
        G.mano.add(1,GeneraRandom());
        G.mano.add(2,GeneraRandom());
        
        for(Carta C : G.mano)
        {
            Mz.remove(C);
        }
    }
        
    /**
     * @METHOD ResetAttempts
     * 
     * @OVERVIEW Metodo che imposta il numero di volte in cui il metodo ValidityConfiguration è stato
     *           invocato a zero, in modo da permettere un'ulteriore eventuale chiamata di quest'ultimo
     */
    public void ResetAttempts()
    {
        Attempts = 0;
    }
    
    /**
     * @METHOD SpaceOutputConsole
     * 
     * @OVERVIEW Metodo che inserisce quattro linee vuote tra un test e l'altro in modo da aumentare la leggibilità
     *           e la comprensibilità dei risultati in output
     */
    public void SpaceOutputConsole()
    {
        System.out.println("\n\n\n\n");
    }
    
    
    /*----FINE METODI AUSILIARI----*/
    
    /*-----METODI DI STAMPA E DI DEBUG----*/
    
    /**
     * @METHOD StampaConsole
     * 
     * @OVERVIEW Metodo che stampa i valori delle principali istanze della classe Gioco
     *           le carte presenti all'interno delle mani degli avversari, del tavolo
     *           da gioco e del mazzo in output
     */   
    public void StampaConsole()
    {        
        StampaMazzo();
        
        System.out.println("\n");

        System.out.println("GIOCATORI: "+Computer.GetName()+" MAZZIERE: "+Computer.mazziere+" || "+P1.GetName()+" MAZZIERE: "+P1.mazziere);

        System.out.println("\n");
        
        Computer.VisualizzaMano();
        
        System.out.println("\n");
        
        P1.VisualizzaMano();
        
        System.out.println("\n");
        
        StampaTavolo();
        
        StampaMazzo();
    }
    
    /**
     * @METHOD StampaMazzo
     * 
     * @OVERVIEW Metodo che stampa le carte presenti all'interno del mazzo in output
     *           
     */   
    public void StampaMazzo()
    {
        System.out.println("ANALIZZIAMO IL MAZZO\n");
        System.out.println("SONO PRESENTI "+Mz.size()+" CARTE");
        
        for(int i=0; i<Mz.size(); i++)
        {
            Carta C = Mz.get(i);
            System.out.println("CARTA: "+C.nome+" |VALORE: "+C.valore+" |SEME: "+C.seme+" |CODICE: "+C.codice);
        } 
    }
    
     /**
     * @METHOD StampaTavolo
     * 
     * @OVERVIEW Metodo che stampa le carte presenti all'interno del tavolo da gioco in output
     *           
     */ 
    public void StampaTavolo()
    {
        System.out.println("ANALIZZIAMO IL TAVOLO\n");
        System.out.println("SONO PRESENTI "+Tav.size()+" CARTE");
        
        for(int i=0; i<Tav.size(); i++)
        {
            Carta C = Tav.get(i);
            System.out.println("CARTA: "+C.nome+" |VALORE: "+C.valore+" |SEME: "+C.seme+" |CODICE: "+C.codice);
        } 
    }
    
    /**
     * @METHOD TestPotenziali
     * 
     * @OVERVIEW Metodo che stampa tutte le possibili prese effettuabili da tutte le carte presenti nella mano del giocatore
     */
    public void TestPotenziali()
    {
        for(Carta c : P1.GetMano())
        {
            c.StampaPotenziali();
        }
    }
    
    /**
     * @METHOD PrintCardSet
     * 
     * @OVERVIEW Metodo che stampa in output il nome delle carte appartenteni
     *           ad un determinato ArrayList intitolandolo in base ad una stringa
     *           data in input
     * 
     * @param CardSet : ArrayList di oggetti di tipo carta rappresentante la lista
     *        da stampare
     * 
     * @param Title : Stringa rappresentante il titolo assegnato alla lista 
     * 
    */
    public void PrintCardSet(ArrayList<Carta> CardSet,String Title)
    {
        if(CardSet.isEmpty())
        {
            System.out.println("      "+Title+" è vuoto");
        }
        else
        {
        System.out.println("      "+Title+"\n");
        
        for(Carta C : CardSet)
        {
            System.out.print("| "+C.nome+" |");
        }
        }
        
        System.out.println("\n");
    }
    
    /**
     * @METHOD PrintAllCardSets
     * 
     * @OVERVIEW Metodo che stampa, tramite diverse chiamate del metodo PrintCardSet
     *           le mani del giocatore e dell'avversario e la configurazione corrente
     *           del tavolo opportunamente intitolate
     * 
    */
    public void PrintAllCardSets()
    {
        PrintCardSet(P1.mano,"MANO DEL GIOCATORE");
        PrintCardSet(Computer.mano,"MANO DEL COMPUTER");
        PrintCardSet(Tav,"TAVOLO DI GIOCO");
    }

    /**
     * @METHOD PrintDecisionTree
     * 
     * @OVERVIEW Metodo che stampa in output il numero di stati, il guadagno massimo, l'etichetta dello stato
     *           relativo a quest'ultimo, la carta migliore da giocare e l'indice della presa migliore da effettuare
     *           stimate dall'intelligenza artificiale della CPU
     */
    public void PrintDecisionTree()
    {
        System.out.println("----------------------------------------------------------------------------------------------------\n");
        System.out.println("----STAMPA DELL'ALBERO DI GIOCO GENERATO DAL COMPUTER----\n");
        System.out.println("-NUMERO DI STATI: "+Computer.Intelligence.SizeOfDecisionTree());
        
        Stato DecisionTree = Computer.Intelligence.DecisionTree;
        
        Computer.Intelligence.SintesiAlberoDiDecisione();
        
        Double Gain = Computer.Intelligence.MaxGain;
        
        String EstablishedState = Computer.Intelligence.EstablishedLabel;
        int EstablishedCard = Computer.Intelligence.EstablishedCard;
        int EstablishedPotential = Computer.Intelligence.EstablishedPotential;
        
        System.out.println("MASSIMO GUADAGNO: "+Gain+" NELLO STATO: "+EstablishedState);
        System.out.println("STATO OTTENUTO SCEGLIENDO LA CARTA: "+EstablishedCard+" CON LA COMBINAZIONE: "+EstablishedPotential);
        
        System.out.println("----------------------------------------------------------------------------------------------------");
    }
    
    /*-----FINE METODI DI STAMPA E DI DEBUG----*/
    
}
