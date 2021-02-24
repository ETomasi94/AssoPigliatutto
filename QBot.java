/*
ASSO PIGLIATUTTO
TESI DI LAUREA A.A 2020 - 2021

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di reinforcement learner che adatta la modalita' di gioco
in base alle scelte dell'utente ed all'esperienza di quest'ultimo nel giocare.
*/
package assopigliatutto;

import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.github.chen0040.rl.utils.IndexValue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/*
    @CLASS QBot

    @OVERVIEW Classe che implementa le proprieta' ed i comportamenti di un Bot
              che analizza lo stato corrente ed impara a giocare secondo un
              algoritmo di Q-Learning.
*/
public final class QBot extends Bot
{
    QLearner Suggester;//Learner associato al bot
    
    private static FileWriter File;//FileWriter utile a serilaizzare i dati del bot
    
    boolean Training;

/*----METODO COSTRUTTORE----*/
    
    public QBot(Gioco G,QLearner S,boolean T)
    {
        super(G);
        Suggester = S;
        Card = -1;
        Comb = 0;
        
        ExploredMoves = 0;
        
        ExploitedMoves = 0;
        
        Training = T;
    }
/*----FINE METODO COSTRUTTORE----*/    
/*----METODI DI GIOCO----*/
    
    /**
     * @METHOD Act
     * 
     * @OVERVIEW Metodo che implementa il ciclo di azione del Bot, suddiviso nelle
     *           seguenti fasi:
     *              1 - Analisi dello stato corrente
     *              2 - Ricerca dello stato nella tabella delle associazioni
     *              3 - Esplorazione di nuove mosse o sfruttamento di mosse già
     *                  presenti nella tabella per lo stato corrente
     *              4 - Aggiornamento del database degli stati confrontando nel
     *                  caso sia necessario la nuova mossa con quella memorizzata
    * 
     *          In caso non siano possibili nuove mosse, il Bot decidera' di giocare in maniera
     *          greedy scegliendo la carta con la presa massima tra quelle possibili
     * 
     * @param DB Database degli stati in cui memorizzare le azioni trovate
     * @return LastAction azione stabilita
     */
    public synchronized int Act()
    {
        Action NewAction = null;
        
        int Action = 0;       
        double Reward = 0.0;
        Carta C = null;
        
        if(LastState == null)
        {
            ExploredMoves++;
            NoPossibleActions();
            return 0;
        }
        else
        {
            try
            {
                ExploreNewActions(Action, Reward, C, NewAction);
            }
            catch(NullPointerException ex)
            {
                ex.printStackTrace();
                System.out.println("NullPointerException Reset");
                Game.ResetGame();          
            }
        }
  
        return LastAction;
    }
    
    /**
     * @METHOD ExploreNewActions
     * 
     * @OVERVIEW Metodo che implementa il comportamento del Bot nel caso esso
     *           decida di sperimentare nuove azioni per lo stato corrente
     * 
     * @param Action Codice dell'azione trovata da memorizzare
     * @param Reward Reward associato all'azione NewAction da memorizzare
     * @param C Carta trovata da memorizzare
     * @param NewAction Azione trovata da memorizzare
     * @param DB Database degli stati attuale in cui memorizzare l'associazione
     *           LastState -> NewAction (Stato corrente,azione trovata).
     */
    public synchronized void ExploreNewActions(int Action,double Reward,Carta C,Action NewAction)
    {
        List<Integer> PossibleActions = RetrieveActions();
        
        if(!MyActions.isEmpty())
        {          
            Action = SelectAnAction(MyActions,PossibleActions);

            Card = GetCard(Action);
            Comb = GetCombination(Action);

            C = LastState.Hand.get(Card);

            Reward = CalculateReward(C,Comb);

            NewAction = new Action(Action,Reward);
        
            int FutureStateCode = LastState.Future(Card,Comb);
            
            if(IsExplored(LastState.SerialNumber(),Action))
            {
                ExploredMoves++;
                
            }
            else
            {
                ExploitedMoves++;
            }
              
            UpdateBot(LastState.SerialNumber(),Action,FutureStateCode,Reward);

            if(Action != -1) 
            {
                UpdateMoves(Card,Comb,Reward);
            } 
        }
        else
        {
            NoPossibleActions();
        }
    }
    
    public double GetBinding(int State,int Action)
    {
        return Suggester.getModel().getQ().get(State,Action);
    }
    
    public boolean IsExplored(int State,int Action)
    {
        return (GetBinding(State,Action) == 0.1);
    }
    
    /**
     * @METHOD RetrieveActions
     * 
     * @OVERVIEW Metodo che restituisce la lista delle possibili azioni nello
     *           stato corrente
     * 
     * @return PossibleActions Lista di interi rappresentanti le azioni possibili
     *         nello stato corrente.
     */
    public synchronized List<Integer> RetrieveActions()
    {          
        Set<Integer> Actions = GetPossibleActions();

        MyActions = Actions;

        List<Integer> PossibleActions = new ArrayList<>(Actions);
        
        return PossibleActions;
    }
    
    /**
     * @METHOD SelectAnAction
     * 
     * @OVERVIEW Metodo che implementa la scelta di un'azione da parte del bot
     * 
     * @param Actions Lista di azioni da selezionare in base all'indice IV
     * @param PossibleActions Lista delle possibili azioni
     * 
     * @return Action Intero rappresentante l'azione selezionata
     */
    public synchronized int SelectAnAction(Set<Integer> Actions,List<Integer> PossibleActions)
    {
        int Action;
        
        IndexValue IV = Suggester.selectAction(StateCount,Actions);
        Action = IV.getIndex();
        double Value = IV.getValue();

        if(Value <= 0)
        {
            Action = PossibleActions.get(RandomSelector.nextInt(PossibleActions.size()));
        }
        
        return Action;
    }
    
    
    /**
     * @METHOD GreedyPlay
     * 
     * @OVERVIEW Metodo che implementa il comportamento del bot nel caso questi
     *           decida di giocare in maniera greedy per impossibilità di valutare
     *           correttamente lo stato corrente.
     * 
     * @param DB Database degli stati in cui memorizzare l'associazione
     *           Stato Corrente->Azione trovata
     */
    public synchronized void GreedyPlay(StateDatabase DB)
    {
        if(!Player.mano.isEmpty())
        {
            CardsPlayedGreedily++;
            
            double Max = 0.0;
            int Comb = 0;
            int Index = 0;
            Carta Card = null;
            
            for(Carta C : Player.mano)
            {
                if(C.MaxGain >= Max)
                {
                    Index = Player.mano.indexOf(C);
                    Max = C.MaxGain;
                    Comb = C.MaxPotential;
                    Card = C;
                }
            }
            
            Action NewAction = new Action(Index,Comb,Max);
            
            this.Card = NewAction.GetCard();
            this.Comb = NewAction.GetCombination();

            UpdateMoves(Index,Comb,Max);

            int FutureStateCode = LastState.Future(Index,Comb);
            
            UpdateBot(LastState.SerialNumber(),NewAction.Code,FutureStateCode,Max);
            
            Game.GiocaCarta(Player, Comb, Card);
        }
    }
    
    /**
     * @METHOD Play
     * 
     * @OVERVIEW Metodo che implementa il comportamento del bot al momento di giocare
     *           una carta sul tavolo, che sia in maniera accuratamente analizzata o greedy.
     */
    public synchronized void Play()
    {
       if(Card != -1)
       {
            Game.GiocaIndexCard(Player, Comb, Card);
       }
       else
       {
            GreedyPlay(Game.Database);
       }
       
       Game.RivalutaPotenziale();
    }
    
    /**
     * @METHOD CalculateReward
     * 
     * @OVERVIEW Metodo che, data una carta ed un intero rappresentante l'indice
     *           della presa scelta (quindi nel complesso un'azione) ne calcola
     *           il reward associato.
     * 
     * @param C Carta da analizzare.
     * @param comb Indice della presa di cui calcolare il reward.
     * 
     * @return Reward Numero in virgola mobile rappresentante il reward associato
     *                all'azione (gioco della carta e presa indicate). 
     */
    public synchronized double CalculateReward(Carta C,int comb)
    {
        double Reward = 0.0;

        if(C.HasPotential(Comb))
        {
            ArrayList<Carta> ChosenCards = new ArrayList();

            ChosenCards.addAll(C.Potenziale.get(Comb));

            Reward = C.Pesa(ChosenCards,LastState.Score);
        }
        
        Reward = Round(Reward,3);

        return Reward;
    }
/*----FINE METODI DI GIOCO----*/
/*----METODI DI AGGIORNAMENTO----*/    
    /**
     * @METHOD UpdateMoves
     * 
     * @OVERVIEW Metodo che aggiorna le mosse trovate dal bot durante la partita
     *           in corso, creando un nuovo stato a seconda della carta e della presa
     *           scelti e del reward ottenuto.
     * 
     * @param C Carta scelta.
     * @param Combination Intero rappresentante l'indice della presa effettuata.
     * @param Reward Numero in virgola mobile rappresentante il reward ottenuto.
     */
    public synchronized void UpdateMoves(int C,int Combination,double Reward)
    {
        int NewState = LastState.Future(C,Combination);
        Action PerformedAction = new Action(C,Combination,Reward);
   
        Mosse.add(new Mossa(LastState.SerialNumber(),PerformedAction.Code,NewState,Reward));
        
    }
    
    public synchronized void UpdateBot(int State,int Action,int NextState,double Reward)
    {
        System.out.println("OLD BINDING FOR STATE: "+State+" ACTION: "+Action+"REWARD: "+GetBinding(State,Action));
        Suggester.update(State, Action, NextState, Reward);
        System.out.println("NEW BINDING FOR STATE: "+State+" ACTION: "+Action+"REWARD: "+GetBinding(State,Action));

        Game.Gamer.Statistics.AddGameReward(GetBinding(State,Action));
        Game.Gamer.Statistics.AddGameQReward(Reward);
        Game.Gamer.Statistics.AddTrainingReward(GetBinding(State,Action));
        Game.Gamer.Statistics.AddTrainingQReward(Reward);
        Game.AddMoveReward(GetBinding(State,Action));
        Game.AddMeanRewardUntilLastMove();
    }
    
    /**
     * @METHOD UpdateStrategy
     * 
     * @OVERVIEW Metodo che, a fine partita, aggiorna la strategia del learner
     *           analizzando le mosse effettuare ed il reward ottenuto.
     * 
    * @param Winner Intero rappresentante il vincitore della partita come segue:
     *                      - Winnings = 1 se ha vinto il giocatore
     *                      - Winnings = 0 se la partita è finita in un pareggio
     *                      - Winnings = -1 se ha vinto la CPU
     */
      public synchronized void UpdateStrategy() 
     {
        double Reward = 0.0;

        for(int i=Mosse.size()-1; i >=0; --i)
        {
            Mossa Move = Mosse.get(i);
            
            if(i >= Mosse.size()-2) 
            {
                Reward = Move.Reward;
            }
            
            Suggester.update(Move.OldState,Move.Action,Move.NewState,Reward);
        }
     }
    
      /**
       * @METHOD Reset
       * 
       * @OVERVIEW Metodo che reimposta il bot settando le opportune variabili
       *           d'istanza ai valori null, -1 o 0.
       */
      public synchronized void Reset() 
      {
        SaveLearner(Player.nome);  
          
        LastState = null;
        LastAction = -1;
        Player = null;
        Suggester = null;
        ExploredMoves = 0;
        ExploitedMoves = 0;
        
        Mosse.clear();
      }
 /*----FINE METODI DI AGGIORNAMENTO----*/      
      
/*----METODI DI CONTROLLO----*/

    /**
     * @METHOD NoPossibleAction
     * 
     * @OVERVIEW: Metodo che dichiara che non è possibile effettuare alcuna azione
     *            e che indica al Bot di giocare in maniera greedy.
     */
    public synchronized void NoPossibleActions()
    {
        LastAction = 0;
        Card = 0;
        Comb = 0;
    }
    
/*----FINE METODI DI CONTROLLO----*/    
    
/*----METODI GETTERS E SETTERS----*/
    
    /**
     * @METHOD SetPlayer
     * 
     * @OVERVIEW Metodo che associa al bot un giocatore  dato in input.
     * 
     * @param P Giocatore da associare al bot.
     */
    public void SetPlayer(Giocatore P)
    {
        Player = P;
    }

    /**
     * @METHOD ReturnCardIndex
     * 
     * @OVERVIEW Metodo che restituisce l'indice rappresentante la posizione della
     *           carta consigliata dal bot all'interno della mano del giocatore.
     * @return Card Indice della posizione della carta consigliata all'interno
     *              della mano del giocatore.
     */
    public synchronized int ReturnCardIndex()
    {
        return Card;
    }
    
     /**
     * @METHOD ReturnCombination
     * 
     * @OVERVIEW Metodo che restituisce l'indice rappresentante la presa 
     *           consigliata effettuabile con la carta consigliata dal bot.
     * 
     * @return Comb Indice della presa consigliata effettuabile con la carta
     *              consigliata
     */
    public synchronized int ReturnCombination()
    {
       return Comb;
    }
    
    public void IsTraining()
    {
        Training = true;
    }
    
    public void IsNotTraining()
    {
        Training = false;
    }
/*----FINE METODI GETTERS E SETTERS----*/ 
/*----METODI DI SERIALIZZAZIONE----*/
    /**
     * @METHOD SaveLearner
     * 
     * @OVERVIEW Metodo che serializza il learner attuale e lo salva in formato
     *           JSON su un file di testo.
     * 
     * @param name Stringa rappresentante il nome da dare al file di testo su cui
     *             salvare il learner serializzato in formato Json.
     */
      public synchronized void SaveLearner(String name)
    {
        String profile = Suggester.toJson();
        
        try 
        {  
            File = new FileWriter("src/main/profiles/"+name+".txt");
            File.write(profile);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            Game.Halt();
 
        } 
        finally 
        {
 
            try 
            {
                File.flush();
                File.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
                Game.Halt();
            }
        } 
    }
    
    /**
     * @METHOD LoadLearner
     * 
     * @OVERVIEW Metodo che, data una stringa in input, carica nel programma il
     *           learner nominato secondo quella stringa.
     * 
     * @param n Stringa rappresentante il nome del learner da caricare
     */
    public synchronized void LoadLearner(String n)
    {
        String path = "src/main/profiles/"+n+".txt";
        FileReader Loader;
        String json = "";
        
        try 
        {
            Loader = new FileReader(path);
            char[] chars = new char[1024];
            int success = Loader.read(chars, 0, chars.length);
            json = new String(chars);
            
            System.out.println("FILE CARICATO : "+path);
            System.out.println("LEARNER CARICATO: "+json);

            Suggester = QLearner.fromJson(json);
        } 
        catch (FileNotFoundException ex) 
        {
            SaveLearner(n);
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Game.Halt();
        } 
    }
/*----FINE METODI DI SERIALIZZAZIONE----*/   
/*----METODI DI STAMPA----*/    
    
    /**
     * @METHOD PrintChoice
     * 
     * @OVERVIEW Metodo che stampa la carta scelta e la lista delle carte prese
     *           con la combinazione scelta associata.
     * 
     * @param C Carta scelta
     * @param ChosenCards ArrayList di oggetti di tipo carta rappresentante le 
     *                    carte prese con la combinazione scelta.
     */
    public synchronized void PrintChoice(Carta C,ArrayList<Carta> ChosenCards)
    {
        System.out.println("----\n");
        System.out.println("CARTA SCELTA: "+C.nome+" PRENDE:");

        for(Carta Cr : ChosenCards)
        {
            System.out.println(Cr.nome);
        }
                    
    }
    
    public synchronized void PrintSelectedAction(int C,int Comb)
    {
        System.out.println("CARTA SCELTA:"+C+" COMBINAZIONE SCELTA"+Comb);
    }
    
        public int GetCard(int Code)
    {
        String Scomposizione = String.valueOf(Code);
        String Sc2 = Scomposizione.substring(0,1);
        int Card = Integer.valueOf(Sc2)-1;
        
        return Card;
    }
    
      /**
     * @METHOD GetCombination
     * 
     * @OVERVIEW Metodo che scompone il codice di un'azione per restituire in 
     *           output la combinazione da esso indicizzata.
     * 
     * @param Value Intero rappresentante il codice di un'azione dato in input.
     * 
     * @return Combination Indice della combinazione denotato dal codice dell'azione. 
     */
    public int GetCombination(int Code)
    {
        String Scomposizione = String.valueOf(Code);
        String Sc2 = Scomposizione.substring(1);
        
        int Combination = Integer.valueOf(Sc2);
        
        return Combination;
    }
   
    /**
     * @METHOD PrintFutureHandAndTable
     * 
     * @OVERVIEW Metodo che, dato uno stato in input, stampa in output le carte
     *           presenti nella mano del giocatore e sul tavolo di quest'ultimo.
     * 
     * @param NewState Stato di cui stampare la mano del giocatore ed il tavolo.
     */
    public synchronized void PrintFutureHandAndTable(Stato NewState)
    {
        System.out.println("----STATO FUTURO:----\n");
        NewState.PrintCardSet(NewState.Hand,"Mano");
        NewState.PrintCardSet(NewState.Table,"Tavolo");
        
    }
    
    public static double Round(double D, int DecimalPlaces) 
    {
        if (DecimalPlaces < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(D));
        bd = bd.setScale(DecimalPlaces, RoundingMode.HALF_UP);
        double result = bd.doubleValue();
        
        return result;
    }
    
    
/*----FINE METODI DI STAMPA----*/
}
