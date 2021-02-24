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

import java.util.concurrent.Callable;

/*
    @CLASS AI

    @OVERVIEW Classe che implementa i metodi di ricerca in uno spazio di stati
    per trovare la carta migliore da giocare in base al risultato dell'esecuzione
    dell'algoritmo di decisione MiniMax applicato ad un albero i cui nodi sono i
    possibili stati derivati da un'azione del giocatore o della CPU.
*/
public class AI implements Callable<Double>
{
    /*----VARIABILI D'ISTANZA----*/
    
    Stato DecisionTree;//Albero di decisione su cui effettuare la ricerca
    
    double MaxGain;//Guagdagno massimo
    int EstablishedCard;//Indice della carta stabilita come la migliore da giocare
    int EstablishedPotential;//Combinazione associata alla presa con maggio guadagno possibile giocando la carta migliore
    String EstablishedLabel;//Etichetta che caratterizza lo stato in cui la CPU guadagna maggiormente
    
    String EstablishedTurn;//Etichetta per indicare in che turno si trova lo stato di massimo guadagno
    
    boolean Timeout;//Timeout per trovare una soluzione secondo l'algoritmo MiniMax
    
    double EstablishedGain;//Guadagno trovato dopo la ricerca
    
    int CurrentSize;//Numero di carte presenti nella mano della CPU
    
    Stato CurrentState;//Stato attuale
    
    /*----FINE VARIABILI D'ISTANZA----*/

    /*----METODO COSTRUTTORE----*/
    
    /*
        @METHOD AI
        
        @OVERVIEW Metodo costruttore della classe AI che setta le variabili d'istanza
        ad un valore iniziale predefinito ed inizializza l'albero di decisione a null
    
        @RETURNS Intelligence AI che rappresenta l'intelligenza artificiale della CPU
    */
    public AI()
    {
        MaxGain = 0.0;
        EstablishedCard = -1;
        EstablishedPotential = -1;
        EstablishedGain = 0.0;
        EstablishedLabel = "ATTUALE";
        DecisionTree = null;
        Timeout = true;
    }
    
     /*----METODO COSTRUTTORE----*/
    
    /*----METODI DI DECISIONE DELLA CARTA MIGLIORE----*/
    
    /*
        @METHOD BuildTree
    
        @OVERVIEW Metodo che costruisce l'albero di decisione a partire dallo stato
        attuale percepito dalla CPU durante il suo ciclo di esecuzione e dalla profondità
        (cut-off) dell'algoritmo a cui l'espansione e l'analisi degli stati si ferma per non
        dover generare un numero esponenzialmente elevato di stati
    
        @PAR State : Stato attuale osservato dalla CPU
        @PAR depth : Profondità massima (cut-off) a cui si estende la ricerca
    */
    public void BuildTree(Stato State,int depth)
    { 
       if(depth < 0)
       {
           throw new IllegalArgumentException();
       }
       
       /*Se la profondità è nulla (mano della CPU vuota), allora l'albero sarà
         composto solo dalla carta attuale
       */
       if(depth == 0)
       {
           State.Results = null;
       }
       
       /*
         Se la profondità è maggiore, genera ricorsivamente i successori dello 
         stato in input
       */
       if(depth > 0)
       {
           State.GeneraSuccessore(depth,true);
       }
       
       /*
         L'albero di gioco ha come radice lo stato in input, ovvero quello effettivamente
         attuale
       */
       DecisionTree = State;
    }   
    
    /*
        @METHOD MiniMaxAlfaBetaPruning
        
        @OVERVIEW Implementazione dell'algoritmo di decisione MiniMax implementato
        con l'ausilio dell'algoritmo di ricerca Potatura Alfa-Beta (McCarthy,1958) per diminuire 
        il numero di nodi da valutare.
    
        @PAR state : Stato da cui avviare la ricerca
        @PAR depth : Profondità (cut-off) dell'albero di ricerca
        @PAR alpha : Valore massimo incontrato durante la ricerca, inizializzato a -inf (Double.NEGATIVE_INFINITY)
        @PAR beta :  Valore minimo incontrato durante la ricerca, inizializzato a +inf (Double.POSITIVE_INFINITY)
        @PAR Player : Valore booleano che indica il giocatore che deve giocare la sua carta nel 
                      nodo corrente state
         
        @RETURNS Gain : Numero in virgola mobile con doppia precisione rappresentante il massimo guadagno in termini
                        di punteggio che il giocatore può effettuare giocando una delle sue carte ed effettuando
                        la miglior presa ad essa associata      
    */
    public double MiniMaxAlfaBetaPruning(Stato state,int depth,double alpha,double beta,boolean IsTurn)
    {
        /*----DICHIARAZIONE DELLE VARIABILI INTERNE AL METODO----*/
        
        double Max; 
        double Min; 

        /*CASO BASE: PROFONDITA' NULLA O NODO FOGLIA*/
        /*Vengono registrati la carta associata a quello stato, il relativo
          potenziale, l'etichetta dello stato ed il guadagno associato
        */
           if(depth == 0 || state.Results.isEmpty())
           {                 
                EstablishTurn(state);
                
                EstablishState(state);

                return state.Gain;
           }

           if(IsTurn)
           {   
              Max = Double.NEGATIVE_INFINITY; 
              
              Max = MaxBranch(Max, state, depth, alpha, beta,IsTurn);
              
              return Max;
           }
           else
           {          
              Min = Double.POSITIVE_INFINITY; 
              
              Min = MinBranch(Min, state, depth, alpha, beta, IsTurn);
              
              return Min;
           }
       }   
    
    /**
     * @METHOD MaxBranch
     * 
     * @OVERVIEW Metodo che implementa il ramo Max dell'algoritmo di ricerca MiniMax
     * 
     * @param Max Valore Max da confrontare con quelli riscontrati durante la ricerca in profondita'
     * @param state Stato da cui viene avviata la ricerca
     * @param depth Profondita' a cui limitare la ricerca
     * @param alpha Valore massimo incontrato durante la ricerca
     * @param beta Valore minimo incontrato durante la ricerca
     * @param IsTurn Valore booleano rappresentante il turno corrente, uguale a true se nello stato
     *               in esame e' il turno della CPU, false altrimenti
     * @return Max Valore in virgola mobile rappresentante il guadagno ottenibile dallo stato in esame
     */
    public double MaxBranch(Double Max,Stato state,int depth,double alpha, double beta, boolean IsTurn)
    {
              for(Stato S : state.Results)
              {
                  double Evaluation = MiniMaxAlfaBetaPruning(S, depth-1, alpha, beta,false); 

                  Max = Math.max(Max,Evaluation);

                  alpha = Math.max(alpha, Max);

                   if(beta <= alpha)  
                    {
                        break;
                    }
               }

               return Max;
    }
    
        /**
     * @METHOD MinBranch
     * 
     * @OVERVIEW Metodo che implementa il ramo Min dell'algoritmo di ricerca MiniMax
     * 
     * @param Min Valore Min da confrontare con quelli riscontrati durante la ricerca in profondita'
     * @param state Stato da cui viene avviata la ricerca
     * @param depth Profondita' a cui limitare la ricerca
     * @param alpha Valore massimo incontrato durante la ricerca
     * @param beta Valore minimo incontrato durante la ricerca
     * @param IsTurn Valore booleano rappresentante il turno corrente, uguale a true se nello stato
     *               in esame e' il turno della CPU, false altrimenti
     * @return Max Valore in virgola mobile rappresentante il guadagno ottenibile dallo stato in esame
     */
    public double MinBranch(Double Min,Stato state,int depth,double alpha, double beta, boolean IsTurn)
    {
       for(Stato S : state.Results)
              {
                   double Evaluation =  MiniMaxAlfaBetaPruning(S, depth-1, alpha, beta,true);  

                   Min = Math.min(Min,Evaluation);

                   beta = Math.min(beta,Min);

                   if(beta <= alpha)
                   {
                       break;
                   }     
              }

              return Min; 
    }
    
    /**
     * @METHOD EstablishTurn
     * 
     * @OVERVIEW Metodo ausiliario che esamina il turno associato allo stato in esame e chiarisce
     *           all'utilizzatore chi sia effettivamente il giocatore associato a quel turno
     * @param state Stato in esame
     */
    public void EstablishTurn(Stato state)
    {
        if(state.IsMax)
           {
               EstablishedTurn = "CPU";
           }
           else
           {
               EstablishedTurn = "Player";
           }
    }
    
    /**
     * @METHOD EstablishState
     * 
     * @OVERVIEW Metodo che setta i parametri dell'AI corrente secondo le rispettive variabili d'istanza
     *           di uno stato in esame
     * 
     * @param state Lo stato in esame 
     */
    public void EstablishState(Stato state)
    {
        EstablishedCard = state.SuggestedCard;
        EstablishedPotential = state.SuggestedPotential;
        EstablishedLabel = state.Label;
        EstablishedGain = state.Gain;
    }
    
    /*
        @METHOD Decide
        
        @OVERVIEW Funzione che restuisce il risultato dell'algoritmo MiniMax applicato ad una determinata profondità
    
        @PAR depth : Profondità arrivato alla quale l'algoritmo si arresta
    
        @RETURNS MaxGain : Numero in virgola mobile con doppia precisione rappresentante il massimo guadagno riscontrato
                dall'algoritmo di ricerca MiniMax
    */
    public double Decide(int depth)
    {
        MaxGain = MiniMaxAlfaBetaPruning(DecisionTree,depth,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,DecisionTree.IsMax);

        //System.out.println("MASSIMO GUADAGNO: "+EstablishedGain+" CARTA: "+EstablishedCard+" PRESA: "+EstablishedPotential+ " STATO: "+EstablishedLabel+" TURNO: "+EstablishedTurn);

        return MaxGain;
    }
 
    /*----FINE METODI DI DECISIONE DELLA CARTA MIGLIORE----*/

    /*----METODI GETTERS E SETTERS----*/
    
    /*
        @METHOD SetEstablishedCard
    
        @OVERVIEW Metodo che imposta come indice della Carta stimata di guadagno massimo
                  un numero intero in input
    
        @PAR CardIndex : Numero intero rappresentante l'indice dove è collocata all'interno
             della mano della CPU la carta stimata di guadagno massimo
    
        @THROWS IllegalArgumentException se viene passato in input un numero non valido, ovvero
                un intero negativo o un numero naturale maggiore di due
    */
    public void SetEstablishedCard(int CardIndex)
    {
        /*Non ci sono più di tre carte nella mano del giocatore o della CPU*/
        if(CardIndex < 0 || CardIndex > 2)
        {
            throw new IllegalArgumentException();
        }
        
        EstablishedCard = CardIndex;
    }
    
    /*
        @METHOD GetEstablishedCard
    
        @OVERVIEW Metodo che restituisce un intero rappresentante l'indice della carta
                  stimata di massimo guadagno
    
        @RETURNS EstablishedCard : Numero intero rappresentante l'indice dove è collocata all'interno
             della mano della CPU la carta stimata di guadagno massimo
    */
    public int GetEstablishedCard()
    {
        return EstablishedCard;
    }
    
    /*
        @METHOD SetEstablishedPotential
    
        @OVERVIEW Metodo che imposta come indice del potenziale stimato di guadagno massimo
                  per la carta migliore un numero intero dato in input
    
        @PAR CardIndex : Numero intero rappresentante l'indice del potenziale massimo trovato
                         per la carta stimata di guadagno massimo
    
        @THROWS IllegalArgumentException se viene passato in input un numero non valido, ovvero
                un intero negativo o zero (i potenziali esistono dall'indice 1)
    */
    public void SetEstablishedPotential(int PotentialIndex)
    {
        if(PotentialIndex <= 0)
        {
            throw new IllegalArgumentException();
        }
        
        EstablishedPotential = PotentialIndex;
    }
    
       /*
        @METHOD GetEstablishedPotential
    
        @OVERVIEW Metodo che restituisce un intero rappresentante l'indice del potenziale stimato come di massimo guadagno
                  della carta stimata di massimo guadagno
    
        @RETURNS EstablishedCard : Numero intero rappresentante l'indice del potenziale di massimo guadagno
    */
    public int GetEstablishedPotential()
    {
        return EstablishedPotential;
    }
    
    /**
     * @METHOD SetState
     * 
     * @OVERVIEW Metodo che imposta lo stato corrente a seconda di un dato stato in input, impostando
     *           inoltre il numero di carte nella mano della CPU
     * 
     * @param S Stato in esame
     * @param size Numero di carte nella mano della CPU
     */
    public void SetState(Stato S,int size)
    {
        CurrentState = S;
        CurrentSize = size;
    }
    
    /**
     * @METHOD UnsetState
     * 
     * @OVERVIEW Metodo che elimina lo stato corrente dalla memoria della CPU di gioco
     * 
     */
    public void UnsetState()
    {
        CurrentState = null;
        CurrentSize = 0;
    }
    
    /**
     * @METHOD SetTimeout
     * 
     * @OVERVIEW Metodo che indica lo scattare del timeout per la ricerca secondo
     *           l'algoritmo MiniMax con potatura alfa-beta implementato
     */
    public void SetTimeout()
    {
        Timeout = true;
    }
    
    /**
     * @METHOD UnsetTimeout
     * 
     * @OVERVIEW Metodo che reimposta il timeout per la ricerca secondo l'algoritmo
     *           MiniMax con potatura alfa-beta implementato
     */
    public void UnsetTimeout()
    {
        Timeout = false;
    }
    
    /*----FINE METODI GETTERS E SETTERS----*/
    
    /*-------METODI DI STAMPA E DEBUG-------*/   
    
    /*
        @METHOD SizeOfTree
    
        @OVERVIEW Metodo che conta ricorsivametne il numero dei nodi di un albero di radice S
                  e lo restituisce in output sotto forma di intero positivo
    
        @PAR S : Radice dell'albero di cui contare il numero di nodi
    
        @RETURNS Size : Numero dei nodi dell'albero di radice S
    */
    private int SizeOfTree(Stato S)
    {
        if(S == null)
        {
            return 0;
        }

        int Count = 0;
        Count++;
        
        /*
            Applichiamo ricorsivamente il metodo a tutti i figli del nodo
            S dato in input
        */
        if(!S.Results.isEmpty())
        {
          for(Stato S1 : S.Results)
          {
            Count += SizeOfTree(S1);
          }
        }   
        
        return Count;
    }
    
    /*
        @METHOD SizeOfDecisionTree
    
        @OVERVIEW Funzione Stub che calcola il numero dei nodi dell'albero di decisione
                  generato da una generica AI
    */
    public int SizeOfDecisionTree()
    {
        int dim = 0;
        
        dim = SizeOfTree(DecisionTree);
        
        return dim;
    }
    
    /*
        @METHOD PrintAlbero
    
        @OVERVIEW Metodo che visita ricorsivamente l'albero stampando le informazioni
                  complete di ogni nodo
    */
    public void PrintAlbero()
    {
        Stato S = DecisionTree;
        
        if(S == null)
        {
            return;
        }
        
        int depth = 0;

        S.PrintStato(depth);    

        System.out.println("STATI DELL'ALBERO: "+SizeOfTree(DecisionTree));      
    }
    
    /*
        @METHOD PrintGuadagniAlbero
    
        @OVERVIEW Metodo che visita ricorsivamente l'albero stampando il guadagno,
                  le carte ed i potenziali di presa considerati massimi e le carte
                  sul tavolo e nelle mani di CPU e Giocatore in output
    */
    public void PrintGuadagniAlbero()
    {
        Stato S = DecisionTree;

        if(S == null)
        {
            return;
        }
        
        int depth = 0;
        
        S.PrintGuadagnoStato(depth);
    }
    
    /*
        @METHOD SintesiAlberoDiDecisione
    
        @OVERVIEW Metodo che visita ricorsivamente l'albero stampando le informazioni
                  essenziali dei nodi in maniera sintetica
    */
    public void SintesiAlberoDiDecisione()
    {
        Stato S = DecisionTree;
        
        if(S == null)
        {
            return;
        }
        
        int depth = 0;
        
        S.SynthesisPrint(depth);
    }
    
    /*
        @METHOD NoCardsToTake
    
        @OVERVIEW Metodo di verifica che serve a stabilire se è possibile guadagnare
                  punti giocando una carta della mano della CPU, in modo da permetterle
                  di decidere se giocare a caso per mancanza di una presa possibile o meno
    */
    public boolean NoCardsToTake()
    {
        return (DecisionTree.Results.isEmpty());
    }
    
    /*
        @METHOD PrintDecision
    
        @OVERVIEW Metodo che stampa in output le variabili d'istanza rappresentanti i risultati dell'esecuzione
                  dell'algoritmo MiniMax con potatura Alfa-Beta
    */
    public void PrintDecision()
    {           
       System.out.println("MASSIMO GUADAGNO: "+MaxGain);
        
       System.out.println("ESTABLISHED LABEL: "+EstablishedLabel);
        
       System.out.println("ESTABLISHED CARD: "+EstablishedCard);
        
       System.out.println("ESTABLISHED POTENTIAL: "+EstablishedPotential);
    }
     /*-------FINE METODI DI STAMPA E DEBUG-------*/


    /**
     * @METHOD call
     * 
     * @OVERVIEW Metodo che effettua una chiamata all'intelligenza artificiale
     *           della CPU istanziata dandole il compito di effettuare la ricerca
     *           della miglior mossa possibile tramite l'algoritmo MiniMax prima dello
     *           scadere di un timeout impostato dalla CPU
     * 
     * @return Guadagno attuale nel caso la ricerca vada a buon fine, -1 altrimenti
     * @throws Exception Se non vi sono carte valide da giocare (viene propagato null dalla ricerca)
     */
    @Override
    public Double call() throws Exception 
    {
        double res;
        
        try
        {
            BuildTree(CurrentState,CurrentSize);
            res = Decide(CurrentSize);
        }
        catch(NullPointerException e)
        {
            return -1.0;
        }
        
        return res;
    }

}
