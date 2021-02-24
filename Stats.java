/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assopigliatutto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author Enrico Tomasi
 * 
 * @CLASS Stats
 * 
 * @OVERVIEW Classe che implementa le statistiche relative ad un giocatore durante
 *           le varie partite giocate.
 *           Include le statistiche della CPU e dell'apprendista per rinforzo.
 */
public class Stats implements Serializable
{
    String TimeOfCreation;//Data di creazione del profilo, e dunque anche delle statistiche.
    
    String PlayerName;//Nome del giocatore associato alle statistiche.
    
    int GamesPlayed;//Numero di partite giocate.
    
    int PlayerWinned;//Numero di partite vinte dal giocatore umano o dall'apprendista per rinforzo.
    
    int Draws;//Numero di pareggi nel corso delle varie partite.
    
    int CPUWinned;//Numero di partite vinte dalla CPU.
    
    int CPUCardsPlayed;//Numero di carte giocate dalla CPU.
    
    int CPUCardsGreedy;//Numero di carte giocate dalla CPU in maniera greedy-
    
    int LearnerCardsPlayed;//Numero di carte giocate dall'apprendista.
    
    int LearnerCardsGreedy;//Numero di carte giocate dall'apprendista in maniera greedy.
    
    int LearnerExploredMoves;//Numero di volte che l'apprendista ha deciso di esplorare nuove mosse.
    
    int LearnerExploitedMoves;//Numero di volte che l'apprendista ha utilizzato mosse già memorizzate.
    
    int SeenStates;//Numero di stati visualizzati nel corso delle varie partite.
    
    int TriedActions;
    
    int TrainingSessions;
    
    int TrainingMovesCount;
    
    int GameMovesCount;
    
    double CPUMeanPlanningAccuracy;//Percentuale di volte che la CPU ha giocato in maniera non greedy, quindi relativamente accurata.
    
    double CPUWinningAccuracy;//Percentuale di partite vinte dalla CPU.

    double PolicyMeanPlanningAccuracy;//Percentuale di volte in cui l'apprendista ha selezionato accuratamente una mossa.
    
    double PolicyWinningAccuracy;//Percentuale di partite vinte dal giocatore e dall'apprendista.
    
    double MeanExplorationRatio;//Percentuale di nuove mosse esplorate.
    
    double MeanExploitationRatio;//Percentuale di utilizzo di mosse memorizzate.
    
    ArrayList<Double> CPUAccuracyDuringGames;/*Media della percentuale di carte giocate in maniera non greedy
                                               dalla CPU nel corso di tutte le partite giocate.*/
    
    ArrayList<Double> PolicyAccuracyDuringGames;/*Media della percentuale di carte giocate in maniera non greedy
                                               dall'apprendista per rinforzo nel corso di tutte le partite giocate.*/
 
    ArrayList<Double> CPUDecisionTimes;
    
    ArrayList<Double> LearnerDecisionTimes;
    
    double CPUMean;
    
    double LearnerMean;
        
    ArrayList<Long> GameActualTimes;
    
    ArrayList<Double> RewardsDuringLastGame;
    
    ArrayList<Double> MeanRewardsForAllGames;
    
    ArrayList<Double> RewardsDuringLastTraining;
    
    ArrayList<Double> MeanRewardsForAllTrainingSessions;
    
    ArrayList<Double> TrainingSingleRewards;
    
    ArrayList<Double> TrainingSingleRewardsVariances;
    
    ArrayList<Double> GameSingleRewards;
    
    ArrayList<Double> GameSingleRewardsVariances;
    
    long GameTime;
        
    double MeanTrainingQReward;  
    
    double MeanGameQReward;
    
    double MeanRewardForLastGame;
    
    double MeanRewardForLastTrainingSession;
    
    double ModelTrainingRewardVariance;
    
    double ModelGameRewardVariance;
    
    double TotalMeanTrainingReward;
    
    double TotalMeanGameReward;
    
    

    /*----MEDOTO COSTRUTTORE----*/
    /**
     * @METHOD Stats
     * 
     * @OVERVIEW Metodo costruttore di un'istanza della classe Stats a cui è
     *           delegata solo l'istanziazione, in quanto l'impostazione delle
     *           statistiche è compito di tutti gli altri metodi della classe.
     */
    public Stats()
    {
    }
    
    /*----FINE METODO COSTRUTTORE----*/
    
    /*----METODI DI CREAZIONE DELLE STATISTICHE----*/
    /**
     * @METHOD CreateStats
     * 
     * @OVERVIEW Metodo che, data in input una stringa rappresentante il nome
     *           del giocatore corrente, ne crea da zero le relative statistiche
     *           impostando come data di creazione la data attuale in formato
     *           "Giorno/Mese/Anno".
     * 
     * @param Nome stringa rappresentante il nome del giocatore corrente. 
     */
    public void CreateStats(String Nome)
    {
        TimeOfCreation = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                
        PlayerName = Nome;
        
        ResetStats();
        
        CreatePlotFileGame();
        
        CreateTrainingPlotFile();
        
        CreateTrainingDispersion();
        
        CreateGameDispersion();
 
    }
    
    /*----FINE METODI DI CREAZIONE DELLE STATISTICHE----*/
 
    /*----METODI DI AGGIORNAMENTO DELLE STATISTICHE----*/
    
    /**
     * @METHOD ResetStats
     * 
     * @OVERVIEW Metodo che reinizializza tutte le variabili delle statistiche
     *           correnti.
     */
    public void ResetStats()
    {
        GamesPlayed = 0;
        
        TrainingSessions = 0;
        
        PlayerWinned = 0;
        
        CPUWinned = 0;
        
        CPUCardsPlayed = 0;
        
        CPUCardsGreedy = 0;
        
        LearnerCardsPlayed = 0;
        
        LearnerCardsGreedy = 0;
        
        CPUMeanPlanningAccuracy = 0;
        
        PolicyWinningAccuracy = 0;
        
        PolicyMeanPlanningAccuracy = 0;
        
        SeenStates = 0;
        
        MeanExplorationRatio = 0;
        
        MeanExploitationRatio = 0;
        
        CPUMean = 0;
        
        LearnerMean = 0;
        
        TriedActions = 0;
        
        MeanRewardForLastGame = 0;
    
        TotalMeanGameReward = 0;
        
        MeanTrainingQReward = 0.0;
        
        MeanRewardForLastTrainingSession = 0;
    
        TotalMeanTrainingReward = 0;
        
        TrainingMovesCount = 0;
        
        ModelTrainingRewardVariance = 0;
    
        ModelGameRewardVariance = 0;
        
        GameMovesCount = 0;
        
        MeanGameQReward = 0;
        
        CPUAccuracyDuringGames = new ArrayList();
        
        PolicyAccuracyDuringGames = new ArrayList();
        
        CPUDecisionTimes = new ArrayList();
        
        LearnerDecisionTimes = new ArrayList();
        
        RewardsDuringLastGame = new ArrayList();
    
        MeanRewardsForAllGames = new ArrayList();
    
        GameActualTimes = new ArrayList();
        
        TrainingSingleRewards = new ArrayList();
        
        TrainingSingleRewardsVariances = new ArrayList();
        
        RewardsDuringLastTraining = new ArrayList();
    
        MeanRewardsForAllTrainingSessions = new ArrayList();
        
        GameSingleRewards = new ArrayList();
    
        GameSingleRewardsVariances = new ArrayList();
    }
    
    /**
     * @METHOD UpdateStatsManual
     * 
     * @OVERVIEW Metodo che aggiorna le statistiche correnti dopo una partita
     *           giocata da un giocatore umano.
     * 
     * @param Winnings Intero rappresentante il vincitore della partita come segue:
     *                      - Winnings = 1 se ha vinto il giocatore
     *                      - Winnings = 0 se la partita è finita in un pareggio
     *                      - Winnings = -1 se ha vinto la CPU
     * @param Cpl Intero rappresentante il numero di carte giocate dalla CPU.
     * @param Gcpl Intero rappresentante il numero di carte giocate dalla CPU
     *             in maniera greedy.
     */
    public void UpdateStatsManual(int Winnings,int Cpl,int Gcpl)
    {
        UpdateGamesPlayed();
        UpdateWinnings(Winnings);
        UpdateCPUStats(Cpl,Gcpl);
    }
    
    /**
     * @METHOD UpdateStatsAutomatic
     * 
     * @OVERVIEW Metodo che aggiorna le statistiche correnti dopo una partita
     *           giocata da un apprendista per rinforzo.
     * 
     * @param Winning Intero rappresentante il vincitore della partita come segue:
     *                      - Winnings = 1 se ha vinto il giocatore
     *                      - Winnings = 0 se la partita è finita in un pareggio
     *                      - Winnings = -1 se ha vinto la CPU
     * @param Cpl Intero rappresentante il numero di carte giocate dalla CPU.
     * @param Gcpl Intero rappresentante il numero di carte giocate dalla CPU
     *             in maniera greedy.
     * @param Lpl Intero rappresentante il numero di carte giocate dall'apprendista.
     * @param Lgpl Intero rappresentante il numero di carte giocate dall'apprendista
     *             in maniera greedy.
     * @param Expl Intero rappresentante il numero di nuove mosse esplorate.
     * @param Expt Intero rappresentante il numero di mosse memorizzate utilizzate.
     */
    public void UpdateStatsAutomatic(int Winning,int Cpl,int Gcpl,int Lpl,int Lgpl,int Expl,int Expt,double CPUTime,double LearnerTime)
    {
        UpdateStatsManual(Winning,Cpl,Gcpl);
        UpdateLearnerStats(Lpl,Lgpl,Expl,Expt);
        UpdateTime(CPUTime,LearnerTime);
    }
    
    public void UpdateTrainingStats()
    {
        TrainingSessions++;
        SetMeanRewardForLastTraining();
        CalculateMeanTrainingReward();
    }
    
    public void UpdateTrainingSingleRewardStats()
    {
        MeanTrainingQReward();
        CalculateTrainingVariances();
    }
    
    public void UpdateGameSingleRewardStats()
    {
        MeanGameQReward();
        CalculateGameVariances();
    }
    
    /**
     * @METHOD UpdateGamesPlayed
     * 
     * @OVERVIEW Metodo che incrementa di uno il numero di partite giocate
     *           alla fine di una sessione di gioco.
     */
    public void UpdateGamesPlayed()
    {
        GamesPlayed++;
    }
    
    /**
     * @METHOD UpdateWinning
     * 
     * @OVERVIEW Metodo che incrementa il numero di partite vinte dalla CPU o dal
     *           giocatore oppure il numero di pareggi in base ad un numero intero
     *           dato in input
     * @param Winnings Intero rappresentante il vincitore della partita come segue:
     *                      - Winnings = 1 se ha vinto il giocatore
     *                      - Winnings = 0 se la partita è finita in un pareggio
     *                      - Winnings = -1 se ha vinto la CPU
     */
    public void UpdateWinnings(int Winnings)
    {
        switch(Winnings)
        {
            case -1:
                CPUWinned++;
                break;
            case 1:
                PlayerWinned++;
                break;
            default:
                Draws++;
        }
    }
    
    /**
     * @METHOD UpdateCPUStats
     * 
     * @OVERVIEW Metodo che aggiorna le statistiche relative all'accuratezza di
     *           gioco (carte giocate in maniera non greedy) e la percentuale
     *           di vittorie della CPU.
     * @param CardsPlayed Intero rappresentante le carte giocate dalla CPU.
     * @param GreedyPlayed Intero rappresentante le carte giocate dalla CPU in
     *                     maniera greedy.
     */
    public void UpdateCPUStats(int CardsPlayed,int GreedyPlayed)
    {
        double PlanningAccuracy = ComputePlanningAccuracy(CardsPlayed,GreedyPlayed);
        
        CPUCardsPlayed += CardsPlayed;      
        CPUCardsGreedy += GreedyPlayed;   
        CPUAccuracyDuringGames.add(PlanningAccuracy);
        CPUMeanPlanningAccuracy = MeanValue(CPUAccuracyDuringGames);
        
        double WinningAccuracy = ((double) CPUWinned + (double) (0.5 * Draws)) / ((double) GamesPlayed);       
        CPUWinningAccuracy = WinningAccuracy * 100;     
    }
    
     /**
     * @METHOD UpdateLearnerStats
     * 
     * @OVERVIEW Metodo che aggiorna le statistiche relative all'accuratezza di
     *           gioco (carte giocate in maniera non greedy), la percentuale
     *           di vittorie e la percentuale di mosse esplorate o meno
     *           dell'apprendista per rinforzo.
     * @param CardsPlayed Intero rappresentante le carte giocate dall'apprendista.
     * @param GreedyPlayed Intero rappresentante le carte giocate dall'apprendista
     *                     in maniera greedy.
     * @param Explored Intero rappresentante le nuove mosse esplorate
     *                 dall'apprendista.
     * @param Exploited Intero rappresentante le mosse memorizzate utilizzate
     *                 dall'apprendista.
     */
    public void UpdateLearnerStats(int CardsPlayed,int GreedyPlayed,int Explored,int Exploited)
    {
        double PlanningAccuracy = ComputePlanningAccuracy(CardsPlayed,GreedyPlayed);
        
        LearnerCardsPlayed += CardsPlayed;
        LearnerCardsGreedy += GreedyPlayed;
        LearnerExploredMoves += Explored;
        LearnerExploitedMoves += Exploited;
        
        PolicyAccuracyDuringGames.add(PlanningAccuracy);
        PolicyMeanPlanningAccuracy = MeanValue(PolicyAccuracyDuringGames);
        
        double WinningAccuracy = ((double) PlayerWinned + (0.5 * (double) Draws)) / ((double) GamesPlayed);
        PolicyWinningAccuracy = WinningAccuracy * 100;
        
        ComputeExploRatio(Explored,Exploited,CardsPlayed);   
        
        UpdateRewardStats();
        UpdateGameSingleRewardStats();
        MeanGameTime();
    }
    
    /**
     * @METHOD UpdateSeenStates
     * 
     * @OVERVIEW Metodo che incrementa il numero di stati visualizzati nel corso
     *           delle partite a seconda di un numero intero in input.
     * 
     * @param StatesCount Intero rappresentante gli stati visualizzati durante
     *                    l'ultima sessione di gioco.
     */
    public void UpdateSeenStates(int StatesCount,int ActionsCount)
    {
        SeenStates = StatesCount;
        TriedActions = ActionsCount;
    }
    
    public void UpdateTime(double CPULearnerTime,double PlayerLearnerTime)
    {       
        CPUMean = CPULearnerTime;
        
        LearnerMean = PlayerLearnerTime;
    }
    
    public void UpdateRewardStats()
    {
        SetMeanRewardForLastGame();
        CalculateMeanGameReward();
    }
    
    /*----FINE METODI DI AGGIORNAMENTO DELLE STATISTICHE----*/
    
    /*----METODI DI CALCOLO DELLE STATISTICHE----*/
    
    /**
     * @METHOD ComputePlanningAccuracy
     * 
     * @OVERVIEW Metodo che calcola la percentuale di carte giocate in maniera
     *           non greedy, e quindi l'accuratezza della CPU nella ricerca di una
     *           mossa ottima da giocare.
     * 
     * @param CPl Intero rappresentante le carte giocate dalla CPU.
     * @param GPl Intero rappresentante le carte giocate dalla CPU in maniera greedy.
     * @return PlanningAccuracy accuratezza della CPU nella sessione attuale
     */
    public double ComputePlanningAccuracy(int CPl,int GPl)
    {
        double Ratio = (double) GPl / (double) CPl;
        
        double PlanningAccuracy = (1 - Ratio) * 100;
        
        return PlanningAccuracy;
    }
    
       /**
     * @METHOD ComputePolicyAccuracy
     * 
     * @OVERVIEW Metodo che calcola la percentuale di partite vinte dall'apprendista
     *           per rinforzo, e quindi l'accuratezza della sua strategia (o policy).
     * 
     * @param Cpl Intero rappresentante le carte giocate dall'apprensita.
     * @param GPl Intero rappresentante le carte giocate dall'apprendist
     *            in maniera greedy.
     * @return PlanningAccuracy accuratezza della policy dell'apprendista
     */
    public double ComputePolicyAccuracy(int Cpl,int GPl)
    {
        double WinningRatio = ( (double) PlayerWinned + (0.5 * (double) Draws))  / (double) GamesPlayed ;
        
        double WinningAccuracy = (WinningRatio) * 100;
        
        double Ratio = (double) GPl / (double) Cpl;
        
        double PlanningAccuracy = (1 - Ratio) * 100;
        
        PolicyAccuracyDuringGames.add(PlanningAccuracy);
        
        return WinningAccuracy;
    }
    
    /**
     * @METHOD ComputeExploRatio
     * 
     * @OVERVIEW Metodo che calcola le percentuali rispettivamente di 
     *           nuove mosse esplorate e mosse memorizzate utilizzate
     *           durante l'ultima sessione di gioco.
     * 
     * @param Explored Intero rappresentante il numero di mosse esplorate.
     * @param Exploited Intero rappresentante il numero di mosse memorizzate
     *                  che sono state utilizzate.
     * @param ActualPlayed Intero rappresentante il numero di carte giocate e
     *                     di conseguenza il numero totale di mosse effettuate.
     */
    public void ComputeExploRatio(int Explored,int Exploited,int ActualPlayed)
    {     
        double ExplRatio = ((double) Explored / (double) ActualPlayed) * 100;
        
        double ExpTatio = ((double) Exploited / (double) ActualPlayed) * 100;
        
        MeanExplorationRatio = ExplRatio;
        
        MeanExploitationRatio = ExpTatio;
    }
    
    /**
     * @MEHOD MeanValue
     * 
     * @OVERVIEW Metodo ausiliario che calcola la media dei valori all'interno
     *           di un array di interi in virgola mobile, utilizzato per calcolare
     *           la precisione di ricerca delle mosse migliori della CPU 
     *           e dell'apprendista per rinforzo.
     * 
     * @param Array Struttura ArrayList contente interi in virgola mobile
     *              rappresentante i valori di cui calcolare la media.
     * 
     * @return Result Numero in virgola mobile rappresentante la media
     *                dei valori all'interno dell'ArrayList in input.
     */
    public double MeanValue(ArrayList<Double> Array)
    {
        double Sum = 0.0;
        
        for(Double Value : Array)
        {
            Sum += Value;
        }
        
        int Size = Array.size();
        
        double Result = Sum / Size;
        
        return Result;
    }
    
    public void AddGameTime(long Time)
    {
        GameActualTimes.add(Time);
    }
    
    public long MeanGameTime()
    {
        long Sum = 0;
        
        for(Long Value : GameActualTimes)
        {
            Sum += Value;
        }
        
        int Size = GameActualTimes.size();
        
        long Result = Sum / Size;
        
        return Result;
    }
    
    public void AddGameReward(double Reward)
    {
        RewardsDuringLastGame.add(Reward);
    }
    
    public void SetMeanRewardForLastGame()
    {
        double Mean = MeanValue(RewardsDuringLastGame);
        MeanRewardForLastGame = Mean;
        MeanRewardsForAllGames.add(Mean);
    }
    
    public double GetMeanRewardForGame(int Index)
    {   
        return MeanRewardsForAllGames.get(Index);
    }
 
    public void CalculateMeanGameReward()
    {
        TotalMeanGameReward = MeanValue(MeanRewardsForAllGames);
    }
    
    public void AddTrainingReward(double Reward)
    {
        RewardsDuringLastTraining.add(Reward);
    }
    
    public void AddTrainingQReward(double Reward)
    {
        TrainingMovesCount++;
        TrainingSingleRewards.add(Reward);
    }
    
    public void AddGameQReward(double Reward)
    {
        GameMovesCount++;
        GameSingleRewards.add(Reward);
    }
    
    public void MeanTrainingQReward()
    {
        MeanTrainingQReward = MeanValue(TrainingSingleRewards);
    }
    
    public void MeanGameQReward()
    {
        MeanGameQReward = MeanValue(GameSingleRewards);
    }
    
    public void SetMeanRewardForLastTraining()
    {
        double Mean = MeanValue(RewardsDuringLastTraining);
        MeanRewardForLastTrainingSession = Mean;
        MeanRewardsForAllTrainingSessions.add(Mean);
    }
    
    public double GetMeanRewardForTrainingSession(int Index)
    {
        return MeanRewardsForAllTrainingSessions.get(Index);
    }
    
    public void CalculateMeanTrainingReward()
    {
        TotalMeanTrainingReward = MeanValue(MeanRewardsForAllTrainingSessions);
    }
    
    public void CalculateTrainingVariances()
    {
        int Size = TrainingSingleRewards.size();
        double Mean = MeanTrainingQReward;
        double Sum = 0.0;
        double Variance = 0.0;
        
        for(int i=0;i<Size;i++)
        {
            double Reward = TrainingSingleRewards.get(i);
            Variance = Math.abs(Reward - Mean);
            Sum += Variance;
        }
        
        ModelTrainingRewardVariance = ((Sum / Size) * 1.0);
        TrainingSingleRewardsVariances.add(Variance);
    }
    
    public void CalculateGameVariances()
    {
        int Size = GameSingleRewards.size();
        double Mean = MeanGameQReward;
        double Variance = 0.0;
        double Sum = 0.0;
        
        for(int i=0;i<Size;i++)
        {
            double Reward = GameSingleRewards.get(i);
            Variance = Math.abs(Reward - Mean);
            Sum += Variance;
        }
        
        ModelGameRewardVariance = ((Sum / Size) * 1.0);
        GameSingleRewardsVariances.add(Variance);
    }
   
    /*----FINE METODI DI CALCOLO DELLE STATISTICHE----*/
    
    /*----METODI DI STAMPA SU FILE DELLE STATISTICHE----*/
    
    /**
     * @MEHOD DumpStats
     * 
     * @OVERVIEW Metodo che stampa tutte le variabili delle statistiche correnti
     *           all'interno della cartella "src/main/statisticsdumps" dandogli
     *           un nome secondo la data e l'ora esatta della creazione.
     */
    public void DumpGameStats()
    {
        FileWriter StatFile = null;

        String AccuracyOfCPU = String.format("%.2f",CPUMeanPlanningAccuracy);
        String WinningAccuracyOfCPU = String.format("%.2f",CPUWinningAccuracy);
        String WinningAccuracyOfPolicy = String.format("%.2f",PolicyWinningAccuracy);
        String ExplorationRatio = String.format("%.2f",MeanExplorationRatio);
        String ExploitationRatio = String.format("%.2f",MeanExploitationRatio);
        String CPUTime = String.format("%.3f",CPUMean);
        String LearnerTime = String.format("%.3f",LearnerMean);
        String RewardInLastGame = String.format("%.4f",MeanRewardForLastGame);
        String MeanRewardForAllGames = String.format("%.4f",TotalMeanGameReward);
        
        try 
        {
            String CalendarDay = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
            String TimeStamp = new SimpleDateFormat("HH.mm.ss").format(new Date());
            StatFile = new FileWriter("src/main/statisticsdumps/Statistiche del "+CalendarDay+" alle ore "+TimeStamp+".txt");
            
            StatFile.write("PROFILE NAME: "+PlayerName+"\n");
            StatFile.write("DATE OF CREATION: "+TimeOfCreation+"\n");
            StatFile.write("------------------------------------------------\n \n");
            StatFile.write("GAMES PLAYED: "+GamesPlayed+"\n");
            StatFile.write("WINNED BY PLAYER: "+PlayerWinned+"\n");
            StatFile.write("WINNED BY CPU: "+CPUWinned+"\n");
            StatFile.write("DRAWS: "+Draws+"\n");
            StatFile.write("----CPU STATISTICS----\n \n");
            StatFile.write("CPU CARDS PLAYED: "+CPUCardsPlayed+"\n");
            StatFile.write("CPU CARDS PLAYED GREEDILY: "+CPUCardsGreedy+"\n");
            StatFile.write("CPU PLANNING ACCURACY: "+AccuracyOfCPU+"% \n");
            StatFile.write("CPU WINS "+WinningAccuracyOfCPU+"% OF GAMES PLAYED \n");
            StatFile.write("CPU MEAN DECISION TIME: "+CPUTime+"SECONDS\n");
            StatFile.write("----LEARNER POLICY STATISTICS----\n \n");
            StatFile.write("LEARNER CARDS PLAYED: "+LearnerCardsPlayed+"\n");
            StatFile.write("LEARNER EXPLORED STATES: "+LearnerExploredMoves+"\n");
            StatFile.write("LEARNER EXPLOITED STATES: "+LearnerExploitedMoves+"\n");
            StatFile.write("LEARNER EXPLORE NEW STATES: "+ExplorationRatio+"% OF TURNS\n");
            StatFile.write("LEARNER EXPLOITS KNOWN STATES: "+ExploitationRatio+"% OF TURNS\n");
            StatFile.write("LEARNER MEAN DECISION TIME: "+LearnerTime+"SECONDS\n");
            StatFile.write("LEARNER MEAN REWARD DURING LAST GAME: "+RewardInLastGame+"\n");
            StatFile.write("LEARNER TOTAL MEAN REWARD: "+MeanRewardForAllGames+"\n");
            StatFile.write("POLICY WINS "+WinningAccuracyOfPolicy+"% OF GAMES PLAYED \n");
            StatFile.write("----------------------------------------------------\n \n");
            StatFile.write("SIZE OF CPU ACCURACY REGISTER: "+CPUAccuracyDuringGames.size()+"\n");
            StatFile.write("SIZE OF PLAYER ACCURACY REGISTER: "+PolicyAccuracyDuringGames.size()+"\n");
            StatFile.write("SIZE OF DATABASE: "+SeenStates);
            
            StatFile.close();
            
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } 
        finally 
        {
            try 
            {
                StatFile.close();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
    }
    
    public void CreatePlotFileGame()
    {
        FileWriter PlotFile = null;
        String path = "src/main/statisticsdumps/plottable/";
        File Plot = new File(path+"PlottableStats_"+PlayerName+".txt");
        try 
        {
            PlotFile = new FileWriter(Plot);
                PlotFile.write("GamesPlayed|SeenStates|CPUWinned|PlayerWinned|Draws|CPUAccuracy|CPUTime|ExplorationRatio|LearnerTime|PWA|CWA|MeanRewardForLastGame|TotalMeanReward|GameActualTime|MeanGameTime\n");
            PlotFile.close();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
        

    }
    
    public void PlotGameStats() throws IOException
    {
        String ExplorationRatio = String.format("%.2f",MeanExplorationRatio);
        String CPUTime = String.format("%.3f",CPUMean);
        String LearnerTime = String.format("%.3f",LearnerMean);
        String CPUAccuracy = String.format("%.3f",CPUMeanPlanningAccuracy);
        String WinningAccuracyOfCPU = String.format("%.2f",CPUWinningAccuracy);
        String WinningAccuracyOfPolicy = String.format("%.2f",PolicyWinningAccuracy);
        String RewardInLastGame = String.format("%.4f",MeanRewardForLastGame);
        String MeanRewardForAllGames = String.format("%.4f",TotalMeanGameReward);
        
        FileWriter PlotFile = null;
        String path = "src/main/statisticsdumps/plottable/";
        try
        {
            File Plot = new File(path+"PlottableStats_"+PlayerName+".txt");
            PlotFile = new FileWriter(Plot,true);
            PlotFile.write(GamesPlayed+"|"+SeenStates+"|"+CPUWinned+"|"+PlayerWinned+"|"+Draws+"|"+CPUAccuracy+"|"+CPUTime+"|"+ExplorationRatio+"|"+LearnerTime+"|"+WinningAccuracyOfPolicy+"|"+WinningAccuracyOfCPU+"|"+RewardInLastGame+"|"+MeanRewardForAllGames+"|"+GameTime+"\n");
            PlotFile.close();
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } 
        finally 
        {
            try 
            {
                PlotFile.close();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
        
    }
    
    public void CreateTrainingPlotFile()
    {
        FileWriter PlotFile = null;
        String path = "src/main/statisticsdumps/plottable/";
        File Plot = new File(path+"TrainingStats_"+PlayerName+".txt");
        try 
        {
            PlotFile = new FileWriter(Plot);
            PlotFile.write("TrainingSession|LastTrainingReward|TrainingMeanReward\n");
            PlotFile.close();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }     
    }
    
    public void PlotTrainingStats() throws IOException
    {
        String RewardInLastTraining = String.format("%.4f",MeanRewardForLastTrainingSession);
        String TotalTrainingRewardMean = String.format("%.4f",TotalMeanTrainingReward);
        
        
        FileWriter PlotFile = null;
        String path = "src/main/statisticsdumps/plottable/";
        try
        {
            File Plot = new File(path+"TrainingStats_"+PlayerName+".txt");
            PlotFile = new FileWriter(Plot,true);
            PlotFile.write(TrainingSessions+"|"+RewardInLastTraining+"|"+TotalTrainingRewardMean+"\n");
            PlotFile.close();
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } 
        finally 
        {
            try 
            {
                PlotFile.close();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
        
    }
    
    public void PlotTrainingDispersion()
    {
        FileWriter PlotFile = null;
        String path = "src/main/statisticsdumps/plottable/";

        
        try
        {
            File Plot = new File(path+"TrainingDispersion_"+PlayerName+".txt");
            PlotFile = new FileWriter(Plot);
            int i = 0;
            for(Double D : TrainingSingleRewards)
            {
                String Mean = String.format("%.4f",MeanTrainingQReward);
                String Variance = String.format("%.4f",ModelTrainingRewardVariance);
                String ImmediateReward = String.format("%.4f",D);
                String ImmediateVariance = String.format("%.4f",Math.abs(D - MeanTrainingQReward));
                PlotFile.write(i+"|"+ImmediateReward+"|"+Mean+"|"+ImmediateVariance+"|"+Variance+"\n");
                i++;
            }
            PlotFile.close();
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } 
        finally 
        {
            try 
            {
                PlotFile.close();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
    }
    
     public void CreateTrainingDispersion()
    {
              FileWriter PlotFile = null;
        String path = "src/main/statisticsdumps/plottable/";
        try
        {
            File Plot = new File(path+"TrainingDispersion_"+PlayerName+".txt");
            PlotFile = new FileWriter(Plot,true);
            PlotFile.write("TrainingMove|ImmediateReward|Mean|ImmediateVariance|Variance\n");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void CreateGameDispersion()
    {
        FileWriter PlotFile = null;
        String path = "src/main/statisticsdumps/plottable/";
        try
        {
            File Plot = new File(path+"GameDispersion_"+PlayerName+".txt");
            PlotFile = new FileWriter(Plot,true);
            PlotFile.write("GameMove|ImmediateReward|Mean|ImmediateVariance|Variance\n");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

    }
    
     public void PlotGameDispersion()
    {
        FileWriter PlotFile = null;
        String path = "src/main/statisticsdumps/plottable/";

        
        try
        {
            File Plot = new File(path+"GameDispersion_"+PlayerName+".txt");
            PlotFile = new FileWriter(Plot);
            int i = 0;
            for(Double D : GameSingleRewards)
            {
                String Mean = String.format("%.4f",MeanGameQReward);
                String Variance = String.format("%.4f",ModelGameRewardVariance);
                String ImmediateReward = String.format("%.4f",D);
                String ImmediateVariance = String.format("%.4f",Math.abs(D - MeanGameQReward));
                PlotFile.write(i+"|"+ImmediateReward+"|"+Mean+"|"+ImmediateVariance+"|"+Variance+"\n");
                i++;
            }
            
            PlotFile.close();
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } 
        finally 
        {
            try 
            {
                PlotFile.close();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
    }

    
  /*----FINE METODI DI STAMPA SU FILE DELLE STATISTICHE----*/
            
}
