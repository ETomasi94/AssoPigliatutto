/*
ASSO PIGLIATUTTO
PROGETTO DI ESPERIENZE DI PROGRAMMAZIONE A.A 2019-2020

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

/*
    @CLASS AI

    @OVERVIEW Classe che implementa i metodi di ricerca in uno spazio di stati
    per trovare la carta migliore da giocare in base al risultato dell'esecuzione
    dell'algoritmo di decisione MiniMax applicato ad un albero i cui nodi sono i
    possibili stati derivati da un'azione del giocatore o della CPU.
*/
public class AI 
{
    /*----VARIABILI D'ISTANZA----*/
    
    Stato DecisionTree;//Albero di decisione su cui effettuare la ricerca
    
    double MaxGain;//Guagdagno massimo
    int EstablishedCard;//Indice della carta stabilita come la migliore da giocare
    int EstablishedPotential;//Combinazione associata alla presa con maggio guadagno possibile giocando la carta migliore
    String EstablishedLabel;//Etichetta che caratterizza lo stato in cui la CPU guadagna maggiormente
    
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
        EstablishedCard = 0;
        EstablishedPotential = 0;
        EstablishedLabel = "ATTUALE";
        DecisionTree = null;
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
           State.GeneraSuccessore(depth);     
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
    public double MiniMaxAlfaBetaPruning(Stato state,int depth,double alpha,double beta,boolean Player)
    {
        /*----DICHIARAZIONE DELLE VARIABILI INTERNE AL METODO----*/
        
        double Max; 
        double Min; 

        double GlobalGain = 0.0;
        
        
        /*CASO BASE: PROFONDITA' NULLA O NODO FOGLIA*/
        /*Vengono registrati la carta associata a quello stato, il relativo
          potenziale, l'etichetta dello stato ed il guadagno associato
        */
           if(depth == 0 || state.Results.isEmpty())
           {
                   EstablishedCard = state.SuggestedCard;
                   EstablishedPotential = state.SuggestedPotential;
                   EstablishedLabel = state.Label;
                   GlobalGain += state.Gain;
                   return GlobalGain;
           }

           if(Player)
           {   
              Max = Double.NEGATIVE_INFINITY;

              for(Stato S : state.Results)
              {
                  double Evaluation = MiniMaxAlfaBetaPruning(S, depth-1, alpha, beta,false); 

                  S.Label = S.Label.concat("    |CI GUADAGNA LA CPU|     ");

                  Max = Math.max(Max,Evaluation);

                  alpha = Math.max(alpha, Max);

                   if(beta <= alpha)  
                    {
                        break;
                    }
               }

               return Max;
           }
           else// for Minimizer player 
           {          
              Min = Double.POSITIVE_INFINITY; 

              for(Stato S : state.Results)
              {
                   double Evaluation =  MiniMaxAlfaBetaPruning(S, depth-1, alpha, beta,true);  

                   S.Label = S.Label.concat("    |CI GUADAGNA IL GIOCATORE|     ");

                   Min = Math.min(Min,Evaluation);

                   beta = Math.min(beta,Evaluation);

                   if(beta <= alpha)
                   {
                       break;
                   }     
              }

              return Min;
           }
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
        @METHOD PrintGuadagniAlbero
    
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
}
