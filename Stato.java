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

/**
 * @CLASS Stato
 * 
 * @OVERVIEW Classe che implementa uno stato dell'albero di gioco contrassegnato da alcune
 *           variabili che rappresentano la situazione attuale o una situazione ipotizzata
 *           da un susseguirsi di scelte contrassegnate anch'esse da stati dell'albero di gioco
*/
public final class Stato 
{   
    /*----CONFIGURAZIONE RELATIVA AD UNO STATO----*/
    KnowledgeBase Actual;//Knowledge Base dello stato
    
    ArrayList<Carta> Table;//Configurazione del tavolo
    
    ArrayList<Carta> Hand;//Mano del giocatore nello stato in questione
    
    ArrayList<Carta> Opponent;//Mano dell'avversario nello stato in questione
    
    Punteggio OpponentScore;

    Punteggio Score;//Punteggio del giocatore (contrassegnato dal booleano IsMax) nello stato in questione
    
    boolean IsMax;//Booleano che indica se nello stato in questione è il turno del computer (caso true) o del giocatore (caso false)
    
    String Label;//Etichetta che contraddistingue lo stato in questione
    
    double Gain;//Guadagno massimo che contrassegna lo stato in questione
    
    int SuggestedCard;//Carta con cui si è arrivati dallo stato precedente a quello in questione
    
    int SuggestedPotential;//Indice della presa associata alla carta con cui si è arrivati dallo stato precedente a quello in questione
    
    int PlayerCards;//Carte residue nella mano del giocatore
    
    /*----FINE CONFIGURAZIONE RELATIVA AD UNO STATO----*/
    
    /*----IMPLEMENTAZIONE DEI NODI PER L'USO DELLA RICORSIONE----*/
    
    Stato Parent;//Stato padre
    
    ArrayList<Stato> Results;//Lista degli stati figli raggiungibili dallo stato in questione
    
    
    /*----FINE IMPLEMENTAZIONE DEI NODI PER L'USO DELLA RICORSIONE----*/
    
    /*----METODO COSTRUTTORE----*/
    
    /**
     * @METHOD Stato
     * 
     * @OVERVIEW Metodo costruttore che restituisce uno Stato istanza della classe
     *           a partire da alcuni valori
     * 
     * @param L : Stringa rappresentante l'etichetta dello stato
     * @param T : ArrayList di oggetti di tipo Carta rappresentante il tavolo di gioco osservato
     * @param H : ArrayList di oggetti di tipo carta rappresentante la mano del giocatore osservata in quel momento
     * @param K : Knowledge Base della CPU osservata in quel momento
     * @param S : Punteggio del giocatore osservato in quel momento
     * @param Q : Booleano che indica se è il turno della CPU o del giocatore
     * @param PC : Intero che indica il numero di carte residue nella mano dell'avversario
     * 
     * @RETURNS State : Stato corrente
     */
    public Stato(String L,ArrayList<Carta> T,ArrayList<Carta> H,KnowledgeBase K,Punteggio S,boolean Q,int PC,int InitialCard,int InitialPotential)
    {
        Actual = K;
        
        Table = T;
        
        Hand = H;
        
        Label = L;

        Score = S;
        
        IsMax = Q;
        
        Parent = null;
        
        Gain = 0;
        
        Results = new ArrayList();
        
        SuggestedCard = InitialCard;
        
        SuggestedPotential = InitialPotential;
        
        PlayerCards = PC;
                     
        //Ricaviamo le carte più promettenti dell'avversario
        GetOpponent();
    }  
    
    /*----FINE METODO COSTRUTTORE----*/
    
    /*----METODI RELATIVI ALLA GENERAZIONE DEGLI STATI SUCCESSORI----*/
    
    /**
     *
     * 
     * @METHOD GeneraSuccessore
     * 
     * @OVERVIEW Metodo che espande uno stato generando i suoi successori fino ad 
     *           una certa profondità (cut-off).
     * 
     *           Ogni successore è generato tenendo conto delle carte che vengono
     *           eliminate o inserite sul tavolo una volta giocata una carta con
     *           e scelta, se esiste, una determinata presa associata
     * 
     *           Per non generare troppi stati, e quindi non impiegare tempo e spazio
     *           esponenzialmente grandi, sono state adottate le seguenti misure:
     *          
     *              -Uno stato viene espanso solo nei casi in cui è effettivamente
     *               possibile effettuare una presa (gli stati con guadagno nullo
     *               non vengono considerati)
     * 
     *              -Nel caso giocando una carta sia possibile effettuare più di una
     *               presa, viene scelta per l'espansione la presa di guadagno massimo
     *               in modo da espandere tre nodi per ogni carta presente nella mano
     *               del giocatore nello stato in considerazione
     * 
     *              TEMPO : O(3^depth)
     * 
     * @param depth : Intero rappresentante la massima profondita arrivati alla quale 
     *      l'espansione dei vari stati si arresta
     * 
     * @param start : Valore booleano che indica se il metodo è stato chiamato per la prima volta 
     * o meno, in modo da decidere se la carta attuale che porterà allo stato con guadagno massimo
     * deve essere istanziata (nel caso start sia uguale a true) oppure propagata allo stato successore
     * (nel caso start sia uguale a false)
    */
    public synchronized void GeneraSuccessore(int depth,boolean start)
    {
        if(depth >= 0)
        {
            boolean turn = !this.IsMax;

            if(turn)//CASO MAX (TURNO CPU)
            {
              if(!Hand.isEmpty())
              {
                for(Carta C : Hand)//Vengono considerate le carte presenti all'interno della mano del giocatore
                {
                    if(C.IsMarked() && !Table.isEmpty())
                    {                
                        int Sc = C.MaxPotential;
                        int CardToPropagate = Hand.indexOf(C);
                        
                        Stato S1;
                        
                        if(start)
                        {
                            S1 = new Stato(Label+"-> "+C.nome+"/"+Sc,FT(Table,C,Sc),FH(Hand,C),Actual,FS(Score,C),turn,PlayerCards,CardToPropagate,Sc);
                            SetSuggestedCard(Hand,S1,C,Hand.indexOf(C),Sc);
                        }
                        else
                        {
                            S1 = new Stato(Label+"-> "+C.nome+"/"+Sc,FT(Table,C,Sc),FH(Hand,C),Actual,FS(Score,C),turn,PlayerCards,SuggestedCard,SuggestedPotential);
                            SetSuggestedCard(Hand,S1,C,SuggestedCard,SuggestedPotential);
                        }

                        Results.add(S1);
                    }
                   }
                }
            }
            else//CASO MIN (TURNO GIOCATORE)
            {
              if(!Opponent.isEmpty())
              {
                for(Carta C : Opponent)//Vengono considerate le tre carte più promettenti per l'avversario
                {
                    if(C.IsMarked() && !Table.isEmpty())
                    {                

                               Stato S1;
                                int Sc = C.MaxPotential;
                                int CardToPropagate = Opponent.indexOf(C);
              
                                if(start)
                                {
                                    S1 = new Stato(Label+"-> "+C.nome+"/"+Sc,FT(Table,C,Sc),Hand,FKB(Actual,C),Score,turn,PlayerCards-1,CardToPropagate,Sc);
                                    SetSuggestedCard(Opponent,S1,C,Opponent.indexOf(C),Sc);
                                }
                                else
                                {
                                    S1 = new Stato(Label+"-> "+C.nome+"/"+Sc,FT(Table,C,Sc),Hand,FKB(Actual,C),Score,turn,PlayerCards-1,SuggestedCard,SuggestedPotential);
                                    SetSuggestedCard(Opponent,S1,C,SuggestedCard,SuggestedPotential);
                                }
                                
                                S1.Opponent = FOPP(Opponent,C);
                                S1.OpponentScore = FS(OpponentScore,C);

                                Results.add(S1);

                    }
                }
              }
            }

            if(Results != null)
            {
                for(Stato S1 : Results)
                {
                    S1.GeneraSuccessore(depth-1,false);
                }
            }
        }
    }
    
    /**
    * @METHOD SetSuggestedCard
    * 
    * @OVERVIEW Metodo che imposta la carta e la presa (nel caso questa sia possibile) con cui
    *           si genera ad uno stato in input dall'espansione dello stato in questione
    * 
    * @param CardSet : ArrayList di oggetti di tipo carta rappresentante il set di carte da cui proviene la carta
    *        giocata per generare lo stato figlio
    * @param S : Lo stato figlio dello stato in questione
    * @param C : Carta giocata nello stato in questione per generare lo stato figlio
    * @param Sc : Indice della presa giocata nello stato in questione per generare lo stato figlio
    */
    public void SetSuggestedCard(ArrayList<Carta> CardSet,Stato S,Carta C,int Index,int Sc)
    {
     if(C.HasPotential(Sc))
     {
        S.Gain = (C.ValoriPotenziale.get(Sc));
        S.SuggestedCard = Index;
        S.SuggestedPotential = Sc;
     }
     else
     {
         S.Gain = 0.0;
     }
    }
            
    /**
        @METHOD FT
    
        @OVERVIEW Metodo che genera la configurazione del tavolo realativa allo stato successore 
                  ottenuto giocando una determinata carta ed effettuando nel caso sia possibile
                  una determinata presa facendo inferenza sulla configurazione attuale del tavolo da 
                  gioco e togliendo le carte relative alla presa dal tavolo nel caso essa esista 
                  o aggiungendo la carta al tavolo in caso contrario, simulando di fatto la giocata
    
    *   @param T : ArrayList di oggetti di tipo carta rappresentante il tavolo da gioco e la sua configurazione
    *              nello stato attuale
    *   @param C : Carta da giocare per generare lo stato successore
    *   @param Scelta : Intero rappresentante la presa da effettuare per generare lo stato successore
    * 
    *   @return FutureTable : ArrayList di oggetti di tipo carta rappresentante la configurazione del tavolo da gioco 
    *           nello stato successore ottenuto giocando la carta ed effettuando la presa desiderata
    */
    public ArrayList<Carta> FT(ArrayList<Carta> T,Carta C,int Scelta)
    {
        if(C.HasPotential(Scelta))
        {
            ArrayList<Carta> TB = new ArrayList();
        
            TB.addAll(T);
        
            for(Carta C1 : C.Potenziale.get(Scelta))
            {
                TB.remove(C1);
            }
            
            return TB;
        }
        else
        {
            ArrayList<Carta> TB = new ArrayList();
            
            TB.addAll(T);
            TB.add(C);
            
            return TB;
        }
    }
        
    /**
     * @METHOD FH 
     *             
     * 
     * @OVERVIEW   Metodo che genera la mano della CPU relativa allo stato successore 
     *             ottenuto giocando una determinata cartafacendo inferenza sulla configurazione 
     *             attuale della mano e togliendo da essa la carta giocata
     *             
     * 
     * @param H : ArrayList di oggetti di tipo carta rappresentante la configurazione attuale della mano
     *            della CPU
     * @param C : Carta da giocare
     * 
     * @return FutureHand : ArrayList di carte rappresentante la configurazione della mano nello stato
    *           successore ottenuto giocando la carta desiderata
     */
    public ArrayList<Carta> FH(ArrayList<Carta> H,Carta C)
    {
        ArrayList<Carta> HND = new ArrayList();
        
        HND.addAll(H);
        
        HND.remove(C);

        return HND;
    }
    
    /**
     * @METHOD FOPP
     *             
     * 
     * @OVERVIEW   Metodo che genera la mano dell'avversario relativa allo stato successore 
     *             ottenuto giocando una determinata carta facendo inferenza sulla configurazione
     *             attuale della mano e togliendo da essa la carta giocata      
     * 
     *             La mano dell'avversario è definita in termini delle tre carte più promettenti 
     *             stimate in termini di guadagno 
     * 
     * @param OPP : ArrayList di oggetti di tipo carta rappresentante la configurazione attuale della mano
     *              dell'avversario
     * @param C : Carta da giocare
     * 
     * @return FutureOpponent : ArrayList di carte rappresentante la configurazione della mano nello stato
    *           successore ottenuto giocando la carta desiderata
     */
    public ArrayList<Carta> FOPP(ArrayList<Carta> OPP,Carta C)
    {
        ArrayList<Carta> OPPN = new ArrayList();
        
        OPPN.addAll(OPP);
        
        OPPN.remove(C);

        return OPPN;
    }
    
    /**
     * @METHOD FKB           
     * 
     * @OVERVIEW   Metodo che genera la Knowledge Base della CPU relativa allo stato successore 
     *             ottenuta rimuovendo dalla Knowledge Base attuale una determinata carta ed
     *             aggiornando le statistiche
    
     * @param K : Knowledge Base relativa allo stato corrente
     * @param C : Carta da rimuovere
     * 
     * @return FutureKB : Knowledge Base relativa allo stato successore ottenuta 
     *         rimuovendo dalla Knowledge Base attuale la carta desiderata
     */
    public KnowledgeBase FKB(KnowledgeBase K,Carta C)
    {
        KnowledgeBase KB1 = K;
        
        KB1.RimuoviCarta(C);
        
        return KB1;
    }
    
   /**
     * @METHOD FS          
     * 
     * @OVERVIEW   Metodo che genera il punteggio della CPU relativo allo stato successore 
     *             ottenuto aggiungendo al punteggio attuale una determinata carta ed aggiornando
     *             quest'ultimo a seconda delle caratteristiche della carta
    
     * @param S : Punteggio relativo allo stato corrente
     * @param C : Carta da aggiungere
     * 
     * @return FutureScore : punteggio relativo allo stato successore ottenuto
     *         aggiungendo al punteggio attuale la carta desiderata ed aggiornando
     *         quest'ultimo a seconda delle caratteristiche di quest'ultima
     */
    public Punteggio FS(Punteggio S,Carta C)
    {
        Punteggio SCR = S;
        
        S.AddCard(C);
        
        return SCR;
    }
    
    /*----FINE METODI RELATIVI ALLA GENERAZIONE DEGLI STATI SUCCESSORI----*/
    
    /*----METODI GETTERS E SETTERS----*/

    /**
     * @METHOD SetKB
     * 
     * @OVERVIEW Metodo che imposta la Knowledge Base relativa allo stato corrente
     *           uguale ad una Knowledge Base data in input
     * 
     * @param  KB : Knowledge Base in input da assegnare alla Knowledge Base dello 
     *              stato corrente
     * 
    */
    public void SetKB(KnowledgeBase KB)
    {
        Actual = KB;
    }
    
     /**
     * @METHOD SetTable
     * 
     * @OVERVIEW Metodo che imposta il tavolo da gioco relativo allo stato corrente
     *           uguale ad un ArrayList di oggetti di tipo carta dato in input
     * 
     * @param  Tv : ArrayList di oggetti di tipo carta da assegnare al Tavolo relativo
     *              relativo allo stato corrente
     * 
    */
    public void SetTable(ArrayList<Carta> Tv)
    {
        Table = Tv;
    }
    
     /**
     * @METHOD SetHand
     * 
     * @OVERVIEW Metodo che imposta la mano del giocatore relativa allo stato corrente
     *           uguale ad un ArrayList di oggetti di tipo carta dato in input
     * 
     * @param  KnownHand : ArrayList di oggetti di tipo carta da assegnare alla mano relativa
     *              relativo allo stato corrente
     * 
    */
    public void SetHand(ArrayList<Carta> KnownHand)
    {
        Hand = KnownHand;
    }
    
    /**
     * @METHOD GetKB
     * 
     * @OVERVIEW Metodo che restituisce la Knowledge Base associata allo stato corrente
     * 
     * @return Actual : Knowledge Base dello stato corrente
     * 
    */
    public KnowledgeBase GetKB()
    {
        return Actual;
    }
    
   /**
     * @METHOD GetKB
     * 
     * @OVERVIEW Metodo che restituisce il tavolo da gioco associato allo stato corrente
     * 
     * @return Table : ArrayList di oggetti di tipo carta rappresentante il tavolo da gioco dello stato corrente
     * 
    */
    public ArrayList<Carta> GetTable()
    {
        return Table;
    }
    
      /**
     * @METHOD GetHand
     * 
     * @OVERVIEW Metodo che restituisce la mano del giocatore associata allo stato corrente
     * 
     * @return Hand : ArrayList di oggetti di tipo carta rappresentante la mano del giocatore dello stato corrente
     * 
    */
    public ArrayList<Carta> GetHand()
    {
        return Hand;
    }
    
     /**
     * @METHOD GetOpponent
     * 
     * @OVERVIEW Metodo che restituisce la mano dell'avversario associata allo stato corrente
     * 
     * @return Opponent : ArrayList di oggetti di tipo carta rappresentante la mano dell'avversario dello stato corrente
     * 
    */
    public ArrayList<Carta> GetOpponent()
    {
        Opponent = Actual.GetMostValuableCards(Table,Score,PlayerCards);
        
        OpponentScore = Score.GetOpponentScore();

        return Opponent;
    }
    
    /**
     * @METHOD AddResult
     * 
     * @OVERVIEW Metodo che aggiunge uno stato alla lista degli stati successori dello stato corrente
     * 
     * @param  Res : Stato da aggiungere alla lista dei successori
     * 
     * @return Res : Stato da aggiungere alla lista dei successori
     * 
    */
    public Stato AddResult(Stato Res) 
    {
        Res.SetParent(this);
        this.Results.add(Res);
        return Res;
    }
    
  /**
     * @METHOD AddResultsList
     * 
     * @OVERVIEW Metodo che aggiunge una lista di stati alla lista degli stati successori dello stato corrente
     * 
     * @param  ListOfStates : Lista di stati da aggiungere alla lista dei successori
 
    */
    public void AddResultsList(ArrayList<Stato> ListOfStates) 
    {
        for(Stato Res : ListOfStates)
        {
            Res.SetParent(this);
        }
            
        Results.addAll(ListOfStates);
    }

    /**
     * @METHOD GetResults
     * 
     * @OVERVIEW Metodo che restituisce la lista di stati generati dallo stato corrente
     * 
     * @return Results : Lista di stati generati dallo stato corrente
     * 
    */
    public ArrayList<Stato> GetResults()
    {
        return Results;
    }
 
    /**
     * @METHOD SetParent
     * 
     * @OVERVIEW Metodo che imposta come stato padre dello stato corrente un determinato
     *           stato dato in input
     * 
     * @param Previous : Stato da impostare come stato padre dello stato corrente
     * 
    */
    protected void SetParent(Stato Previous) 
    {
        this.Parent = Previous;
    }
 
        /**
     * @METHOD GetParent
     * 
     * @OVERVIEW Metodo che restituisce lo stato padre dello stato corrente
     * 
     * @return Parent : Stato padre dello stato corrente
     * 
    */
    public Stato GetParent() 
    {
        return Parent;
    }
    
    /**
     * @METHOD SetLabel
     * 
     * @OVERVIEW Metodo che imposta come etichetta dello stato corrente una determinata
     *           stringa data in input
     * 
     * @param L : Stringa da impostare come etichetta dello stato corrente
     * 
    */
    public void SetLabel(String L)
    {
        Label = L;
    }
    
    /**
     * @METHOD GetLabel
     * 
     * @OVERVIEW Metodo che restituisce l'etichetta dello stato corrente
     * 
     * @return L : Stringa rappresentante l'etichetta dello stato corrente
     * 
    */
    public String GetLabel()
    {
        return Label;
    }
    
    /**
     * @METHOD GetSuggestion
     * 
     * @OVERVIEW Metodo che restituisce un array di interi contenenti gli interi
     *           associati alle posizioni della carta all'interno della mano
     *           e della sua presa di valore massimo all'interno della lista delle prese
     *           che sono state scelte per generare lo stato corrente
     *           
     * 
     * @return Suggested : Array di interi composto da
     *                      - POSIZIONE 0 : Carta giocata per generare lo stato corrente
     *                      - POSIZIONE 1 : Presa di massimo valore scelta per generare lo stato corrente
    */
    public Integer[] GetSuggestion()
    {
        Integer[] Suggested = {SuggestedCard,SuggestedPotential};
        
        return Suggested;
    }
       
    /**
     * @METHOD SetPunteggio
     * 
     * @OVERVIEW Metodo che imposta come punteggio dello stato corrente un determinato
     *           punteggio dato in input
     * 
     * @param S : Punteggio da impostare come etichetta dello stato corrente
     * 
    */
    public void SetPunteggio(Punteggio S)
    {
        Score = S;
    }
    
    /**
     * @METHOD GetPunteggio
     * 
     * @OVERVIEW Metodo che restituisce il punteggio dello stato corrente
     * 
     * @return Score : Punteggio dello stato corrente
     * 
    */
    public Punteggio GetPunteggio()
    {
        return Score;
    }
    
    /**
     * @METHOD ResetGain
     * 
     * @OVERVIEW Metodo che imposta il guadagno complessivo dato dalla carta giocata
     *           e dalla presa effettuata a 0.0 (Numero in virgola mobile a doppia precisione)
     * 
    */
    public void ResetGain()
    {
        Gain = 0.0;
    }
    
    /**
     * @METHOD SetGain
     * 
     * @OVERVIEW Metodo che imposta il guadagno complessivo dello stato dato dalla carta giocata
     *           e dalla presa effettuata ad un determinato numero in virgola mobile a
     *           doppia precisione dato in input
     * 
     * @param D : Numero in virgola mobile a doppia precisione a cui impostare il guadagno
     *            complessivo dello stato corrente
     * 
    */
    public void SetGain(Double D)
    {
        Gain = D;
    }
    
     /**
     * @METHOD GetGain
     * 
     * @OVERVIEW Metodo che restituisce il guadagno complessivo dello stato dato dalla carta giocata
     *           e dalla presa effettuata
     * 
     * @return Gain : Numero in virgola mobile a doppia precisione rappresentante il guadagno complessivo 
     *                dello stato dato dalla carta giocata e dalla presa effettuata
    */
    public double GeiGain()
    {
        return Gain;
    }
    
    /*----FINE METODI GETTERS E SETTERS----*/

    /*----METODI DI STAMPA E DI DEBUG----*/
    
    /**
        @METHOD DeclarePlayer
    
        @OVERVIEW Metodo che, in base al valore di un valore booleano dato in input
        *         indicante il turno da giocare, indica se è il turno della CPU oppure
        *         del giocatore restituendo una stringa in output
        * 
        * @param player : Valore booleano che rappresenta il turno di gioco corrente
        * 
        * @return Declaration : Stringa che decodifica il valore booleano dato in input
    */
    public String DeclarePlayer(boolean player)
    {
        if(player == true)
        {
            return "CPU";
        }
        else
        {
            return "PLAYER";
        }
    }
    
    /**
     * @METHOD SynthesisPrint
     * 
     * @OVERVIEW Metodo che stampa sinteticamente le informazioni degli stati
     *           appartenenti all'albero che ha come radice lo stato corrente
     *           e di profondità uguale ad un numero intero in input
     * 
     * @param depth : Intero che rappresenta la profondità arrivato alla quale
     *        il metodo si arresta e le informazioni degli stati non vengono
     *        più stampate
     * 
    */
    public void SynthesisPrint(int depth)
    {
        if(this == null)
        {
            return;
        }
        
        String Player = DeclarePlayer(IsMax);

        System.out.println("---STATO: "+Label+"\n");
        
        System.out.println("-GIOCATORE: "+Player+"\n");
        System.out.println("-GUADAGNO: "+Gain+"\n");
        System.out.println("-CARTA CONSIGLIATA: "+SuggestedCard+"\n");
        System.out.println("-COMBINAZIONE CONSIGLIATA: "+SuggestedPotential+"\n");
        System.out.println("----\n");
        
        for(Stato S1 : Results)
        {
            S1.SynthesisPrint(depth+1);
        }
        
    }
    
    /**
     * @METHOD PrintStato
     * 
     * @OVERVIEW Metodo che stampa le informazioni complete degli stati
     *           appartenenti all'albero che ha come radice lo stato corrente
     *           e di profondità uguale ad un numero intero in input
     * 
     * @param depth : Intero che rappresenta la profondità arrivato alla quale
     *        il metodo si arresta e le informazioni degli stati non vengono
     *        più stampate
     * 
    */
    public void PrintStato(int depth)
    {        
        if(this == null)
        {
            return;
        }
        
        String Player = DeclarePlayer(IsMax);
        
        System.out.println("@@@@@@@@@@LIVELLO: "+depth);
        
        System.out.println("---------STATO: "+Label+"---------");
        
        System.out.println("TURNO: "+Player+"\n");
        
        System.out.println("CARTA SUGGERITA: "+SuggestedCard+" COMBINAZIONE SUGGERITA: "+SuggestedPotential);

        System.out.println("KNOWLEDGE BASE:");
        
        Actual.KBPrint();
        
        System.out.print("TABLE: ");

        if(!Table.isEmpty())
        {
            PrintCardSet(Table,"TAVOLO");
        }
        else
        {
            System.out.println("TABLE IS EMPTY!");
        }
        
        System.out.println("\n");
        
        if(!Hand.isEmpty())
        {
            PrintCardSet(Hand,"HAND");
        }
        else
        {
            System.out.println("HAND IS EMPTY!");
        }
        
        if(!Opponent.isEmpty())
        {
            PrintCardSet(Opponent,"OPPONENT");
        }
        else
        {
            System.out.println("OPPONENT IS EMPTY!");
        }
        
        System.out.println("\n");

        System.out.println("GUADAGNO: "+Gain);
        
        System.out.println("--------------------------------------------------\n");
        
        if(!Results.isEmpty())
        {
            for(Stato S1: Results)
            {
                S1.PrintStato(depth+1);
            }
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
        System.out.println(Title+"\n");
        
        if(!CardSet.isEmpty())
        {
            for(Carta C : CardSet)
            {
                System.out.print("| "+C.nome+" |");
            }

            System.out.println("\n");
        }
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
        PrintCardSet(Opponent,"MANO DEL GIOCATORE");
        PrintCardSet(Hand,"MANO DEL COMPUTER");
        PrintCardSet(Table,"TAVOLO DI GIOCO");
    }
    
    /**
     * @METHOD PrintStatiCPU
     * 
     * @OVERVIEW Metodo che stampa le informazioni più rilevanti degli stati
     *          appartenenti all'albero che ha come radice lo stato corrente
     *           e di profondità uguale ad un numero intero in input 
     *           relativi ad un turno di gioco rappresentato da un booleano in input
     *           e le relative configurazioni della mano del giocatore, 
     *           della mano dell'avversario e del tavolo da gioco
     * 
     * @param depth : Intero che rappresenta la profondità arrivato alla quale
     *        il metodo si arresta e le informazioni degli stati non vengono
     *        più stampate
     * 
     * @param IsPlayer : Valore booleano che rappresenta un determinato turno di gioco
     *        che individua gli stati da stampare in output
     * 
    */
    public void PrintStatiCPU(int depth,boolean IsPlayer)
    {
            if(this == null)
          {
              return;
          }

          if(IsMax = !IsPlayer)
          {
            String Player = DeclarePlayer(IsMax);

            System.out.println("LIVELLO STATO: "+depth+" STATO: "+Label+" GUADAGNO: "+Gain+" TURNO: "+Player+"\n");

            System.out.println("CARTA SUGGERITA: "+SuggestedCard+" COMBINAZIONE SUGGERITA: "+SuggestedPotential);

            PrintAllCardSets();

            System.out.println("\n");
          }

            for(Stato S1 : Results)
            {
                S1.PrintStatiCPU(depth+1,S1.IsMax);
            }      
    }
    
      /**
     * @METHOD PrintGuadagnoStato
     * 
     * @OVERVIEW Metodo che stampa sinteticamente le informazioni relative
     *           al guadagno ottenuto nei vari stati appartenenti all'albero
     *           che ha come radice lo stato corrente e di profondità uguale 
     *           ad un numero intero in input
     * 
     * @param depth : Intero che rappresenta la profondità arrivato alla quale
     *        il metodo si arresta e le informazioni degli stati non vengono
     *        più stampate
     * 
    */
     public void PrintGuadagnoStato(int depth)
    {
        if(this == null)
        {
            return;
        }
        
        String Player = DeclarePlayer(IsMax);
        
        System.out.println("--------------------------------------------------------------");
        System.out.println("--------------------STATO: "+Label+"-----------------------|");
        System.out.println("--------------------------------------------------------------");
        
        System.out.println("LIVELLO STATO: "+depth);
        System.out.println("GUADAGNO: "+Gain+" TURNO: "+Player+"");       
        System.out.println("CARTA SUGGERITA: "+SuggestedCard+" COMBINAZIONE SUGGERITA: "+SuggestedPotential+"\n");

        System.out.println("\n");
        
        PrintAllCardSets();
        
        System.out.println("--------------------------------------------------------------");
        
        for(Stato S1 : Results)
        {
            S1.PrintGuadagnoStato(depth+1);
        }
    }
     
    /*----FINE METODI DI STAMPA E DI DEBUG----*/
}
