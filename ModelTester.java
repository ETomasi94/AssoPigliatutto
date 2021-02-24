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

import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.github.chen0040.rl.utils.IndexValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Enrico Tomasi
 * 
 * @CLASS DatabaseTest
 * 
 * @OVERVIEW Classe adibita al testing delle funzionalità del database in termini
 *           di ricerca ed aggiornamento degli stati memorizzati.
 */
public class ModelTester 
{
    static Random Randomizer;
    
    static StateDatabase DB;//Database degli stati in analisi.
    
    static Profile Pr;
    
    static Calendar Start;
    
    static ArrayList<Double> CumulativeRewards;
    
    static ArrayList<Double> MeanRewards;
    
    static double MeanTrainingReward;
    
    /**
     * @METHOD main
     * 
     * @OVERVIEW Metodo che implementa il ciclo di testing principale.
     * 
     * @param args Argomenti da passare al programma, nel nostro caso non sono
     *        necessari.
     */
    public static void main(String[] args)
    {   
        TrainingSession(2,100,"Player","Player");
    }
    
    public static void TrainingSession(int Mode,int SessionNum,String Gamer,String DojoName)
    {   
        Randomizer = new Random();
        CumulativeRewards = new ArrayList();
        MeanRewards = new ArrayList();
        
        QLearner Learner = LoadLearner(Gamer);

        switch(Mode)
        {
            case 0 -> Learner = IncrementalTraining(SessionNum,10,Learner);
            case 1 -> Learner = RandomTraining(SessionNum,Learner);
            case 2 -> Learner = KCV(SessionNum,Learner);
            case 3 -> GameTraining(SessionNum,Gamer,DojoName,Learner);
            default -> throw new IllegalArgumentException();
        }
        
        Pr.Learner = Learner;
        
        Pr.Save();
    }
    
    public static QLearner LoadLearner(String name)
    {
        Pr = new Profile(name);
        Pr.Load(name);
        
        return Pr.GetLearner();
    }

    /**
     * @METHOD AddRandomMovesToDatabase
     * 
     * @OVERVIEW Metodo che, dato in input un database di stati, genera un numero in input
     *           di stati random serializzati dal rispettivo codice e li aggiunge
     *           al database, salvandolo alla fine del procedimento.
     * 
     * @param DB Database di stati in input.
     * @param name Nome del giocatore associato al database.
     * @param SessionNum Numero di stati da generare.
     */
    public static QLearner RandomTraining(int SessionNum,QLearner Learner)
    {
        int i=0;
        double ActualPercentage = 0.0;
        
        for(i=0; i<SessionNum;i++)
        {
            ReportProgress(ActualPercentage,i,SessionNum);
            Learner = RandomSession(Learner);
            UpdateTrainingData();     
        }     
        return Learner;
    }
    
    public static QLearner IncrementalTraining(int SessionNum,int StateNum,QLearner Learner)
    {
        int i=0;
        double ActualPercentage = 0.0;
        for(i=0; i<SessionNum;i++)
        {
            int Percentage = ( i / SessionNum) * 100;
            ReportProgress(ActualPercentage,i,SessionNum);
            Learner = IncrementalSession(Learner,StateNum);
            UpdateTrainingData();
        }     
       
        return Learner;
    }
    
    public static QLearner KCV(int SessionNum,QLearner Learner)
    {
        int index = 0;
        int SizeOfSet = SessionNum / 10;
        int ActualIndex = 0;
        double Progress = 0.0;
        
        ArrayList<ArrayList<Carta>> TrainingHands = new ArrayList();
        ArrayList<ArrayList<Carta>> TrainingTables = new ArrayList();
        ArrayList<ArrayList<Carta>> TrainingDecks = new ArrayList();
        ArrayList<Punteggio> TrainingScores = new ArrayList();

        ArrayList<ArrayList<Carta>> TestHands = new ArrayList();
        ArrayList<ArrayList<Carta>> TestTables = new ArrayList();
        ArrayList<ArrayList<Carta>> TestDecks = new ArrayList();
        ArrayList<Punteggio> TestScores = new ArrayList();
        
        Pr.ExploredActions();
        
        //TEST PHASE
        for(index=0;index<SessionNum;index++)
        {
            InitializeDataFolds(TrainingDecks,TrainingScores,TrainingHands,TrainingTables);
        }
        
        int Index = 0;
        
        for(index=0;index<SessionNum;index++)
        {
            for(int i=0;i<SizeOfSet;i++)
            {
                Index = Randomizer.nextInt(TrainingDecks.size());
                ArrayList<Carta> Choosen = TrainingDecks.get(i);
                
                TestDecks.add(Choosen);
            }
            
            for(int i=0;i<SizeOfSet;i++)
            {
                Index = Randomizer.nextInt(TrainingScores.size());
                Punteggio Choosen = TrainingScores.get(i);
                
                TestScores.add(Choosen);
            }
            
            for(int i=0;i<SizeOfSet;i++)
            {
                Index = Randomizer.nextInt(TrainingHands.size());
                ArrayList<Carta> Choosen = TrainingHands.get(i);
                
                TestHands.add(Choosen);
            }
            
            for(int i=0;i<SizeOfSet;i++)
            {
                Index = Randomizer.nextInt(TrainingTables.size());
                ArrayList<Carta> Choosen = TrainingTables.get(i);
                
                TestTables.add(Choosen);
            }
            
            for(int i=0;i<SessionNum - SizeOfSet;i++)
            {
                Learner = UpdateExperience(Learner,TrainingHands.get(i),TrainingTables.get(i),TrainingScores.get(i));
                UpdateTrainingData();
            }
            
            for(int i=0;i<SizeOfSet;i++)
            {
                Learner = UpdateExperience(Learner,TestHands.get(i),TestTables.get(i),TestScores.get(i));

                UpdateTrainingData();
            }
        }
        
        ActualIndex++;

        ReportProgress(Progress,ActualIndex,SessionNum);
        
        return Learner;
    }
    
    public static void InitializeDataFolds(ArrayList<ArrayList<Carta>> TrainingDecks,ArrayList<Punteggio> TrainingScores,ArrayList<ArrayList<Carta>> TrainingHands,ArrayList<ArrayList<Carta>> TrainingTables)
    {
            ArrayList<Carta> TrainingDeck = new ArrayList();
            Punteggio TrainingScore = new Punteggio();
            ArrayList<Carta> TrainingHand = new ArrayList();
            ArrayList<Carta> TrainingTable = new ArrayList();
            
            TrainingDeck = InitializeDeck();
        
            int CardsOnTable = 1 + Randomizer.nextInt(10);

            TrainingScore = RandomScore(TrainingDeck);
            TrainingHand = RandomCardArrayFromDeck(TrainingDeck,4);
            TrainingTable = RandomCardArrayFromDeck(TrainingDeck,CardsOnTable);
            
            TrainingDecks.add(TrainingDeck);
            TrainingScores.add(TrainingScore);
            TrainingHands.add(TrainingHand);
            TrainingTables.add(TrainingTable);
    }
    
    public static void GameTraining(int Gamenum,String Prfl,String DojoName,QLearner Learner)
    {
        Profile Profilo = new Profile(Prfl);
        Profile Dojo = new Profile(DojoName);
        
        StubGame TrainingGame = new StubGame(false,true,Profilo,Dojo,Gamenum,0);
        TrainingGame.NuovoGioco();
        Pr.Statistics.PlotGameDispersion();
    }
    
     public static char RandomSeed()
    {
        Random Rndmz = new Random();   
        
        char[] seeds = {'b','d','c','s'};
        
        int i = Rndmz.nextInt(4);
        char seme = seeds[i];
        
        return seme;
    }
    
    public static Carta RandomCard()
    {
        Random Rnd = new Random();
        
        int Value = 1+Rnd.nextInt(10);
        
        char Seed = RandomSeed();
        
        Carta C = new Carta(Seed,Value);
        
        return C;
    }
    
    public static ArrayList<Carta> InitializeDeck()
    {
        ArrayList<Carta> Deck = new ArrayList();
        
         for(int i=1; i<=10; i++)
        {
            Deck.add(new Carta('s',i));
            Deck.add(new Carta('b',i));
            Deck.add(new Carta('c',i));
            Deck.add(new Carta('d',i));
            Collections.shuffle(Deck);
        }    
         
        return Deck;
    }
    
    public static ArrayList<Carta> RandomCardArrayFromDeck(ArrayList<Carta> Deck,int Size)
    {
        Random Randomizer = new Random();
        
        ArrayList<Carta> Result = new ArrayList();
        
        int Index;
        
        for(int j=0; j<Size; j++)
        {
             Index = Randomizer.nextInt(Deck.size());
             Carta C = Deck.get(Index);

             Result.add(C);
        }
        
        return Result;
    }
    
    public static Punteggio RandomScore(ArrayList<Carta> Deck)
    {
        Random Randomizer = new Random();
        
        Punteggio Score = new Punteggio();
        
        Random rnd;
        rnd = new Random();

        UpdateRandomScore(Score,Deck);

        Score.Primiera = Score.PunteggioDiPrimiera();
        
        return Score;
    }
    
    public synchronized static void UpdateRandomScore(Punteggio Score,ArrayList<Carta> Deck)
    {
        int CardsObtained = Randomizer.nextInt(25);
        ArrayList<Carta> Cards = RandomCardArrayFromDeck(Deck,CardsObtained);
        
        for(Carta C : Cards)
        {
            Score.AddCard(C);
        }
    }
    
    public static QLearner UpdateExperience(QLearner Learner,ArrayList<Carta> RandomHand,ArrayList<Carta> RandomTable,Punteggio Score)
    {
        int Serial = RandomSerial(RandomHand,RandomTable);

        for(Carta C : RandomHand)
        {
            C.CalcolaPotenziale(RandomHand, Score);
        }
        
        Action A = Choose(Learner,RandomHand);

        int Card = A.GetCard();
        int Combination = A.GetCombination();
        
        double Reward = RandomHand.get(Card).ValoriPotenziale.get(Combination);

        int Future = EvaluateFuture(RandomTable,RandomHand,Card,Combination);

        double Result;
        
        Result = Reward * Math.pow(10,4); 
        Result = Math.floor(Result); 
        Result = Result / Math.pow(10,4);

        Learner.update(Serial, A.GetCode(),Future, Result);
        
        Pr.Statistics.AddTrainingReward(GetBinding(Learner,Serial,A.GetCode()));
        Pr.Statistics.AddTrainingQReward(Reward);
        
        return Learner;
    }
    
    public static double GetBinding(QLearner Learner,int State,int Action)
    {
        double Binding = Learner.getModel().getQ(State, Action);
        return Binding;
    }
    
    /**
     * @METHOD RandomDatabaseAdd
     * 
     * @OVERVIEW Metodo che genera uno stato in maniera del tutto casuale e lo inserisce
     *           in un database di stati in input.
     * 
     * @param DB Database di stati in input.
     */
    public static QLearner RandomSession(QLearner Learner)
    {              
        Punteggio Score = new Punteggio();
      
        ArrayList<Carta> RandomTable = new ArrayList();
        ArrayList<Carta> RandomHand = new ArrayList();     
        ArrayList<Carta> Deck = new ArrayList();

        Deck = InitializeDeck();
        
        int CardsOnTable = 1 + Randomizer.nextInt(10);
        
        Score = RandomScore(Deck);
        RandomHand = RandomCardArrayFromDeck(Deck,4);
        RandomTable = RandomCardArrayFromDeck(Deck,CardsOnTable);
        
        Learner = UpdateExperience(Learner,RandomHand,RandomTable,Score);
        
        return Learner;
    } 
    
    public static QLearner IncrementalSession(QLearner Learner,int Size)
    {   
        int Count = 0;
        int Action = 0;
        for(int i=0; i<Size; i++)
        {
                Punteggio Score = new Punteggio();

                ArrayList<Carta> RandomTable = new ArrayList();
                ArrayList<Carta> RandomHand = new ArrayList();

                ArrayList<Carta> Deck = new ArrayList();
                
                Deck = InitializeDeck();

                Score = RandomScore(Deck);
                RandomHand = RandomCardArrayFromDeck(Deck,4);
                RandomTable = RandomCardArrayFromDeck(Deck,Size);

                Learner = UpdateExperience(Learner,RandomHand,RandomTable,Score);           
            }
        
        return Learner;
    }
    
    public static Action Choose(QLearner Suggester,ArrayList<Carta> H)
    {
        Set<Integer> PossibleActions = GetPossibleActions(H);
        
        List<Integer> Actions = RetrieveActions(H);
        
        int Result = SelectAnAction(Suggester,PossibleActions,Actions);
        
        Action A = new Action(Result,0.0);
        
        return A;
    }
    
    public static int SelectAnAction(QLearner Suggester,Set<Integer> Actions,List<Integer> PossibleActions)
    {
        int StateCount = Suggester.getModel().getStateCount();
        int Action = 0;
        
        IndexValue IV = Suggester.selectAction(StateCount,Actions);
        Action = IV.getIndex();
        
        double Value = IV.getValue();

        if(Value <= 0)
        {
            Action = PossibleActions.get(Randomizer.nextInt(PossibleActions.size()));
        }
        
        return Action;
    }
    
    public static Set<Integer> GetPossibleActions(ArrayList<Carta> H)
    {   
        List<Integer> Actions = new LinkedList<>();

        int Action;
        
        if(!H.isEmpty())
        {
            for(Carta C : H)
            {
               if(C.IsMarked() || C.IsAnAce())
               {
                   for(int i=1;i<=C.Potenziale.size();i++)
                   {
                       Action = ComposeAction(H.indexOf(C)+1,i);
                       Actions.add(Action);
                   }
               }
               else
               {
                   Action = ComposeAction(H.indexOf(C)+1,0);
                   Actions.add(Action);
               }
            }
        }

        Set<Integer> Result = Actions.stream().collect(Collectors.toSet());
        
        return Result;
    }
     
    public static synchronized List<Integer> RetrieveActions(ArrayList<Carta> H)
    {          
        Set<Integer> Actions = GetPossibleActions(H);

        List<Integer> PossibleActions = new ArrayList<>(Actions);
        
        return PossibleActions;
    }
    
    public static int ComposeAction(int i,int j)
    {
        int Result = (i*100)+j;
        
        return Result;      
    }

    
    public static int EvaluateFuture(ArrayList<Carta> T, ArrayList<Carta> H,int Card,int Comb)
    {
        ArrayList<Carta> HND = new ArrayList();
        
        Carta C = H.get(Card);
        
        HND.addAll(H);
        
        HND.remove(H.get(Card));

        if(C.HasPotential(Comb))
        {
            ArrayList<Carta> TB = new ArrayList();
        
            TB.addAll(T);
        
            for(Carta C1 : C.Potenziale.get(Comb))
            {
                TB.remove(C1);
            }                    
        }
        else
        {
            ArrayList<Carta> TB = new ArrayList();
            
            TB.addAll(T);
            TB.add(C);
        }
        
        int Result = SerialFromArrayLists(T,H);
        
        return Result;
    }
    
    /**
     * @METHOD RandomSerial
     * 
     * @OVERVIEW Metodo che, dati in input due strutture ArrayList contenenti
     *           oggetti di tipo carta rappresentanti un ipotetico tavolo ed
     *           un'ipotetica mano, genera un ipotetico stato composto da queste ultime
     *           ed il rispettivo codice seriale.
     * 
     * @param RandomTable ArrayList contenente oggetti di tipo carta rappresentante
     *                    il tavolo in ipotesi.
     * @param RandomHand ArrayList contenente oggetti di tipo carta rappresentante
     *                   la mano in ipotesi.
     * 
     * @return Result oggetto di tipo BigInteger rappresentante il seriale associato
     *                all'ipotetico stato risultante dall'esecuzione del metodo.
     */
    public static int RandomSerial(ArrayList<Carta> RandomTable,ArrayList<Carta> RandomHand)
    {               
        int Serial = SerialFromArrayLists(RandomHand,RandomTable);
        
        return Serial;
    }           
    
    /**
     * @METHOD SerialFromArrayLists
     * 
     * @OVERVIEW Metodo che, dati in input due strutture ArrayList contenenti
     *           oggetti di tipo carta rappresentanti un ipotetico tavolo ed
     *           un'ipotetica mano, genera il codice seriale del rispettivo
     *           stato composto da queste ultime.
     * 
     * @param H ArrayList contenente oggetti di tipo carta rappresentante
     *                    la mano in ipotesi.
     * @param T ArrayList contenente oggetti di tipo carta rappresentante
     *                    il tavolo in ipotesi.
     * @return 
     */
       public static int SerialFromArrayLists(ArrayList<Carta> H,ArrayList<Carta> T)
    {
        StringBuilder SB = new StringBuilder();
        
        int Sum = 0;
        int Code = 0;
        
        Sum = HandSerial(H);

        SB.append(Sum);

        Sum = 0;

        Sum = TableSerial(T);

        SB.append(Sum);
    
        String FinalCode = SB.toString();
        int Result = Integer.valueOf(FinalCode);

        return Result;
    }
       
        public static int DecodeSerialCard(Carta C)
    {
        int Code = 0;

        if(C.IsSettebello())
        {
            Code = 11;
        }
        else
        {
         Code = C.GetValue();
        }
        
        return Code;
    }

    
    public static int HandSerial(ArrayList<Carta> H)
    {
        int Sum = 0;
        int Code = 0;
        
        if(H.isEmpty())
        {
            Code = 13;
            Sum += Code;
        }
        else
        {
            ArrayList<Carta> SH = new ArrayList();
            SH.addAll(H);
            Collections.sort(SH);
            Collections.reverse(SH);

            Code = DecodeSerialCard(SH.get(0));

            Sum += Code;

            if(SH.size() > 1)
            {
                Sum *= 100;
                
                Sum += (SH.get(1).GetValue()) - 1;
                
                if(SH.size() > 2)
                {
                    Sum += (SH.get(2).GetValue()) + 2;
                }
            }

        }
        
        return Sum;
    }
    
    public static int TableSerial(ArrayList<Carta> T)
    {
        int Sum = 0;
        int Code = 0;
        
            if(T.isEmpty())
            {
                Code = 12;
                Sum += Code;
            }
            else
            {
                for(Carta C : T)
                {                  
                      if(C.IsSettebello())
                        {
                            Code = 11;
                        }
                        else if(C.IsMaxPrimiera)
                        {
                            Code = C.GetPrimieraValue();
                        }
                        else
                        {
                            Code = C.GetValue();
                        }
                      
                      Sum += Code;
                }
            }
            
            return Sum;
    }
    
       /**
     * @METHOD CheckDatabase
     * 
     * @OVERVIEW Metodo che controlla che un determinato stato, descritto dal suo numero
     *           seriale, sia presente all'interno di un database di stati in input.
     * 
     * @param Database database di stati su cui effettuare il controllo.
     * @param Serial stato da ricercare all'interno del database.
     */
     public static synchronized void CheckDatabase(StateDatabase Database,int Serial)
    {
        if(Database.HasID(Serial))
        {
            Action A = Database.GetCorrespondingAction(Serial);
            System.out.println("AZIONE TROVATA: "+A.Code+" REWARD DELL'AZIONE: "+A.Gain+"\n");
        }
        else
        {
            System.out.println("AZIONE NON TROVATA \n");
        }
    }
    
     /**
      * @METHOD TestKnowledgeOfStates
      * 
      * @OVERVIEW Metodo che controlla che il database sia in grado di riconoscere quando
      *           ad uno stato già presente è associata un'azione con reward minore,uguale 
      *           o maggiore rispetto a quello di un'azione in input, procedendo al mantenimento
      *           nei primi due casi o all'aggiornamento dell'azione memorizzata nell'ultimo caso.
      * 
      * @param Database database di stati su cui effettuare il test.
      * @param NewAction nuova azione da confrontare.
      * @param Serial  seriale dello stato a cui è associata la nuova azione.
      */
    public static void TestKnowledgeOfStates(StateDatabase Database,Action NewAction,int Serial)
    {
        double StoredGain = Database.GetCorrespondingAction(Serial).GetGain();
        double CurrentGain = NewAction.GetGain();

        System.out.println("GUADAGNO ATTUALE: "+CurrentGain+" GUADAGNO REGISTRATO: "+StoredGain);

        if(CurrentGain > StoredGain)
        {
            System.out.println("AGGIORNAMENTO AZIONE PER LO STATO CORRENTE");
            Database.ChangeSerial(Serial, NewAction);
        }
        else
        {
            System.out.println("NESSUN AGGIORNAMENTO NECESSARIO PER LO STATO CORRENTE");
        }
    }
    
    /**
     * @METHOD DataBaseTest
     * 
     * @OVERVIEW Metodo principale di testing del database di stati che verifica la correttezza
     *           dei metodi di controllo e aggiornamento del database, procede nel 
     *           seguente modo:
     * 
     *           -Genera uno stato random, identificato dal suo seriale, che con grande
     *            probabilità, vista la vastità dell'insieme deli stati del gioco asso pigliatutto
     *            non sarà presente all'interno del database e verifica che il database sia in grado
     *            di riconoscere uno stato non presente al suo interno.
     * 
     *           -Scansiona gli stati interni al database e controlla che quest'ultimo riconosca
     *            che effettivamente sono presenti
     * 
     *           -Alla fine, per ogni stato preleva la rispettiva azione associata e, a seconda di un
     *            valore booleano scelto in maniera casuale, crea una nuova azione identica a quella prelevata
     *            ma con il rispettivo valore di reward incrementato o decrementato a seconda del valore booleano
     *            scelto,testando la capacità del database di aggiornare gli stati memorizzati nel seguente modo:
     * 
     *                  -Se il reward è stato decrementato, il database non si aggiorna e mantiene
     *                   l'associazione memorizzata Stato->Azione.
     * 
     *                  -Nel caso invece il reward sia stato incrementato, il database aggiorna per
     *                   lo stato in esame la rispettiva azione associandovi quella con il reward
     *                   incrementato.
     */
    public static void DataBaseTest()
    {
        Random Randomizer = new Random();
        
        ArrayList<Carta> RandomTable = new ArrayList();
        ArrayList<Carta> RandomHand = new ArrayList();
        
        StateDatabase DB = new StateDatabase("Player");
        System.out.println("SIZE OF LOADED DATABASE: "+DB.SizeOfDatabase()+"\n");
        
        int RnSerial = RandomSerial(RandomTable,RandomHand);
        
        System.out.println("----GENERIAMO UNO STATO A CASO MOLTO PROBABILMENTE NON PRESENTE NEL DATABASE----\n");
        
        CheckDatabase(DB,RnSerial);
        
        Set<Entry<Integer,Action>> DBSet = DB.GetDBView();
        
        System.out.println("----VERIFICHIAMO CHE IL DATABASE RICONOSCA GLI STATI SERIALIZZATI IN ESSO----\n");
        
        for(Entry<Integer,Action> E : DBSet)
        {
            int Serial = E.getKey();
            CheckDatabase(DB,Serial);
        }
        
        System.out.println("----VERIFICHIAMO CHE IL DATABASE SIA RESPONSIVO A NUOVI REWARD DA MEMORIZZARE----\n");
        
        for(Entry<Integer,Action> E : DBSet)
        {
            int Serial = E.getKey();
            Action A = E.getValue();
            boolean Increase = Randomizer.nextBoolean();
            Action B = new Action(A.Code,(A.Gain - 1.0));
            Action C = new Action(A.Code,(A.Gain + 1.0));
            
            if(Increase)
            {
                System.out.println("--REWARD INCREMENTATO\n");
                TestKnowledgeOfStates(DB,C,Serial);
            }
            else
            {
                System.out.println("--REWARD DECREMENTATO\n");
                A.DecreaseGain();
                TestKnowledgeOfStates(DB,B,Serial);
            }
        }
    }
    
    public static void ResetRewardStatsBeforeLastGame(String Player)
    {
        LoadLearner(Player);
        
        Stats ST = Pr.Statistics;
        Pr.Statistics.MeanRewardsForAllGames = new ArrayList();
        
        for(int i=0; i<ST.GamesPlayed;i++)
        {
            ST.MeanRewardsForAllGames.add(0.0);
        }
        
        for(int i=0; i<ST.GamesPlayed; i++)
        {
            System.out.println("GAME: "+i+"MEAN REWARD:"+ST.GetMeanRewardForGame(i));
        }
        
        Pr.Save();
        
    }
    
    public static void UpdateTrainingData()
    {
        Pr.Statistics.UpdateTrainingStats();
        Pr.Statistics.UpdateTrainingSingleRewardStats();
        
        try 
        {
            Pr.Statistics.PlotTrainingStats();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public static double Mean(ArrayList<Double> Array)
    {
        double Sum = 0.0;
        double Size = Array.size() * 1.0;
        
        for(Double D : Array)
        {
            Sum += D;
        }
        
        double Result = (Sum / Size);
        
        return Result;
    }
    
    public static void RecoverMeanExplorationRatios(String Name)
    {
        FileReader Reader = null;
        try 
        {
            String Path = "src/main/statisticsdumps/plottable/PlottableStats_"+Name+".txt";
            File PlotFile = new File(Path);
            Reader = new FileReader(PlotFile);
            char[] A = {};
            Reader.read(A);
            
            String[] Division = A.toString().split("|");
            
            int i=0;
            
            for(String S : Division)
            {
                System.out.println("i:"+i+" Division[i]:"+Division[i]+"\n");
                i++;
            }
            
            Reader.close();
        } 
        catch (FileNotFoundException ex) 
        {
            ex.printStackTrace();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } 
        
    }
    
    public static long BinomialCoefficient(int n, int k) 
    {
        if ((n == k) || (k == 0))
            return 1;
        else
            return BinomialCoefficient(n - 1, k) + BinomialCoefficient(n - 1, k - 1);
    }
    
    public static double ReportProgress(double ActualPercentage,int i,int SessionNum)
    {
        double Percentage = (((double) i / (double) SessionNum) * 100.0);
        
        if((Percentage - ActualPercentage) >= 1)
        {
            System.out.println("|"+Percentage+"%|");
        }
        
        ActualPercentage = Percentage;
        
        return ActualPercentage;
    }
    
}
