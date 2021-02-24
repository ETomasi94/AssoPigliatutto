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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    @CLASS Gioco

    @OVERVIEW Classe che implementa le meccaniche del gioco "sottostante" l'interfaccia
*/
public class Gioco extends Thread
{
     /*--------VARIABILI D'ISTANZA----------*/
    
    CPU CPU;
    
    Giocatore Player;
    
    LearningPlayer ReinforcementCPU;
    
    int Winning;
    
    ArrayList<Carta> Carte;//Mazzo di carte della sessione
    
    ArrayList<Carta> Tavolo;//Tavolo della sessione su cui vengono distribuite le carte
    
     /*
        Oggetto TesterDebugger che invoca metodi di testing utili a stabilire
        l'efficienza del gioco
    */
    TesterDebugger Test;
    
    /*
        Oggetto Board che implementa l'interfaccia grafica che consente la visualizzazione
        del gioco all'interno di una finestra
    */
    Board Interface;
    
    boolean Turn;//Variabile che indica se è il turno del giocatore o della CPU
    
    boolean Tested;//Variabile che indica se il gioco è stato testato o meno, in modo da consentire l'esecuzione del test finale (FinalTest)
    
    static boolean Halt;//Flag che indica se il gioco va arrestato o meno
    
    boolean Endgame;//Boolean utile al training del bot per verificare che una partita sia finita o meno
               
    Random rnd;//Randomizzatore utile per gli elementi stocastici della partita
    
    boolean TestGame;//Valore booleano che indica se il gioco sta venendo testato o meno (Metodo test)
    
    boolean TrainingGame;//Valore booleano che indica se a giocare è un umano (false) o il bot (true)
    
    int TimesToTrainGame;//Intero che indica il numero di partite da giocare per lasciare che il bot apprenda
       
    /*
        Threadpool per l'esecuzione concorrente dei thread relativi agli avversari
        ovvero il giocatore e la CPU
    */
    ExecutorService Sfidanti = Executors.newFixedThreadPool(2);
    
    ScoreFrame Scores;//Quadro dei punteggi visualizzato a fine partita
    
    Profile Gamer;//Profilo associato al giocatore ed al bot
    
    QLearner Learner;//Learner del bot
    
    QBot BotQ;//Bot che impara a giocare tramite un algoritmo di Q-Learning
    
    StateDatabase Database;//Database degli stati associato al profilo da cui il bot può reperire l'azione consigliata
    
    Calendar GameStart;
    
    Calendar Start;
    
    int TimeMeasuredFor;
    
    ArrayList<Double> RewardsDuringGame;
    
    ArrayList<Double> MeanRewards;
     /*-------------------------------------------*/
    
    /*-------METODO COSTRUTTORE--*/
    
    /*
        @METHOD Gioco
           
        @OVERVIEW Metodo costruttore di una sessione di gioco
    
        @PAR I : Interfaccia (oggetto della classe Board) che consente la visualizzazione
                 del gioco in un contesto grafico differente dalla console di output
    
        @PAR DoTest : Booleano che indica se il metodo di testing FinalTest va chiamato o meno
    
        @RETURNS Sessione : Istanza della classe Gioco rappresentante la sessione di gioco corrente
    */
    public Gioco(Board I,boolean DoTest,boolean IsTraining,Profile P) 
    {              
        rnd = new Random();
        
        TestGame = DoTest;
        
        TrainingGame = IsTraining;
        
        TimesToTrainGame = 1;
        
        InizializzaProfilo(P);
        
        InizializzaMazzo();     
        
        if(!IsTraining)
        {
            InizializzaGiocatori(Gamer.name);
        }
        else
        {
            InizializzaGiocoAutomatico(Gamer.name);
        }
               
        InizializzaTurni();
        
        InizializzaBot();
        
        InizializzaElementiResidui(I,DoTest);
    }
 
    /*---------------------------*/
    
    /*----FINE METODO COSTRUTTORE----*/
    
    /*--------METODI DI INIZIALIZZAZIONE DELLE VARIABILI D'ISTANZA----*/
    
    /*
        @METHOD Inizializza Giocatori
    
        @OVERVIEW Metodo che crea una nuova istanza del giocatore, una nuova istanza
                  della CPU, le sottomette al threadpool in modo che siano pronte per l'esecuzione
                  e setta le principali variabili d'istanza dei due avversari in modo che possano interagire
                  con il gioco, in seguito fa sì che i due avversari peschino la loro prima mano
    */
    public void InizializzaGiocatori(String PlayerName)
    {
        InizializzaCPU(250);
        
        InizializzaGiocatoreUmano(PlayerName);
        
        AttivaSfidanti();
    }
    
    /**
     * @METHOD InizializzaGiocoAutomatico
     * 
     * @OVERVIEW Metodo che inizializza una sessione di gioco in cui la CPU ed 
     *           il bot si sfidano in modo che quest'ultimo apprenda a giocare
     * 
     * @param PlayerName Nome del giocatore associato al bot per i suggerimenti
     */
    public void InizializzaGiocoAutomatico(String PlayerName)
    {
        InizializzaCPU(500);
        
        InizializzaApprendista(PlayerName);
        
        SpeedUp();
        
        AttivaSfidanti();
    }
    
    /**
     * @METHOD Inizializza CPU
     * 
     * @OVERVIEW Metodo che inizializza la CPU del gioco
     * 
     * @param ms Tempo di attesa stabilito per la CPU per favorire l'interleaving
     *           dei processi relativi alla CPU ed al bot.
     */
    public void InizializzaCPU(long ms)
    {
       CPU = new CPU("CPU",true,this,Tavolo);
       
       CPU.SetInterleavingTime(ms);
    }
    
    /**
     * @METHOD InizializzaGiocatoreUmano
     * 
     * @OVERVIEW Metodo che imposta la partita affinche' un giocatore umano giochi
     *           contro la CPU
     * 
     * @param PlayerName Stringa rappresentante il nome del giocatore
     */
    public void InizializzaGiocatoreUmano(String PlayerName)
    {
       Player = new Giocatore(PlayerName,false,Tavolo); 
       Gamer.AssignPlayer(Player);
    }
    
    /**
     * @METHOD AttivaSfidanti
     * 
     * @OVERVIEW Metodo che attiva i thread relativi al giocatore (o al bot) 
     *           ed alla CPU in modo da iniziare la partita.
     */
    public void AttivaSfidanti()
    {
        Sfidanti.submit(CPU);
        Sfidanti.submit(Player);  
    }
    
    /**
     * @METHOD InizializzaApprendista
     * 
     * @OVERVIEW Metodo che imposta la partita affinche' il bot giochi contro 
     *           la CPU
     * 
     * @param PlayerName Nome del giocatore associato al bot
     */
    public void InizializzaApprendista(String PlayerName)
    {
        Player = new LearningPlayer(PlayerName,false,this,Tavolo);
        Gamer.AssignPlayer(Player);
    }

    /**
     * @METHOD InizializzaElementiResidui
     * 
     * @OVERVIEW Metodo che imposta le variabili booleane fondamentali per la partita
     *           dopo aver inizializzato la sessione di gioco e gli sfidanti
    */
    public void InizializzaElementiResidui(Board I,boolean DoTest)
    {
        Halt = false;
        
        Endgame = false;
        
        if(I != null)
        {
            Interface = I;
        }

        Test = new TesterDebugger(this); 
        
        Tested = !DoTest;
        
        Scores = null;
    }
    
    /**
     * @METHOD InizializzaTurni
     * 
     * @OVERVIEW Metodo che determina il turno iniziale dei due sfidanti a seconda
     *           di chi è il mazziere e consente a questi di prendere in mano le carte
     *           della prima mano di gioco
     */
    public void InizializzaTurni()
    {
        boolean Mazziere = rnd.nextBoolean(); 

        CPU.SetMazziere(Mazziere);
        
        Player.SetMazziere(!Mazziere);

        CPU.PickHand(Carte);
        
        Player.PickHand(Carte);
        
        Turn = !Mazziere;
        
        CPU.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
    }
    
    /**
     * @METHOD InizializzaProfilo
     * 
     * @OVERVIEW Metodo che interfaccia il profilo del giocatore alla sessione di gioco
     *           in modo da consentire l'aggiornamento della strategia del bot suggeritore
     *           e del database degli stati con conseguente memorizzazione di questi.
     * 
     * @param P Profilo da interfacciare alla sessione di gioco.
     */
    public void InizializzaProfilo(Profile P)
    {
        Gamer = P;
        Database = P.DB;
        Learner = P.Learner;
    }
    
    public void InizializzaBot()
    {
        BotQ = new QBot(this,Learner,false);
        BotQ.SetPlayer(Player);
    }

    
    /*
        @METHOD RestartPlayers
    
        @OVERVIEW Metodo che completa il reset degli avversari reimpostando le loro
                  variabili d'istanza principali secondo le variabili d'istanza
                  della sessione corrente (da usare per completare il metodo
                  
    */
    public void RestartPlayers()
    {
         boolean Mazziere = rnd.nextBoolean(); 

        CPU.SetMazziere(Mazziere);
        
        Player.SetMazziere(!Mazziere);

        CPU.PickHand(Carte);
        
        Player.PickHand(Carte);
        
        Turn = !Mazziere;
        
        CPU.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
    }
    
    /**
     * @OVERVIEW Metodo che avvia un'altra sessione di gioco quando CPU e bot
     *           giocano molteplici partite consecutive.
     */
    public void ResetGame()
    {               
        Player.ShutDown();
        
        CPU.ShutDown();
        
        Sfidanti.shutdownNow();
        
        this.Interface.dispose();
        
        Board I = new Board();       
                
        I.NewGame(false,TrainingGame,Gamer);
        
        I.Sessione.SetTimesToTrain(TimesToTrainGame);
  
        if(TrainingGame)
        {
            BotQ.ClearMoves();
        }

        
        I.setVisible(true);
        
    }
    
    /**
     * @METHOD SpeedUp
     * 
     * @OVERVIEW Metodo che velocizza il giocatore associato al bot e la CPU in modo
     *           da accelerare i tempi di gioco quando è in corso una partita tra
     *           questi ultimi.
     */
    public void SpeedUp()
    {
        CPU.SetSpeedMode(true);
        
        Player.SetSpeedMode(true);
    }
    
    /*
        @METHOD InizializzaMazzo
    
        @OVERVIEW Metodo che inizializza il mazzo di carte istanziando una carta
                  per ogni combinazione di semi e valori possibili ed inserendola
                  all'interno del mazzo
    */
    public ArrayList<Carta> InizializzaMazzo()
    {
        Carte = new ArrayList();
        
        for(int i=1; i<=10; i++)
        {
            Carte.add(new Carta('s',i));
            Carte.add(new Carta('b',i));
            Carte.add(new Carta('c',i));
            Carte.add(new Carta('d',i));
        }      
        
                
        //MISCHIAMO IL MAZZO
        Collections.shuffle(Carte);
        
        return Carte;
    }
    
    /*
        @METHOD InizializzaTavolo
    
        @OVERVIEW Metodo che inizializza il tavolo da gioco inserendovi all'interno
                  le prime quattro carte (quelle "in cima") al mazzo opportunamente
                  mescolato
    */
    public ArrayList<Carta> InizializzaTavolo()
    {
        ArrayList<Carta> T = new ArrayList();
        
        int i;
        
        for(i=0;i<4;i++)
        {   
            T.add(Carte.get(0));
            Carte.remove(0);
        }
        
        CPU.AssignTable(T);
        Player.AssignTable(T);
        
        Tavolo = T;
        
        Collections.sort(Tavolo);
        
        return T;    
    }
    /*-----------------------------------------------------------*/
    
    /*-----METODI DI GIOCO----*/
    /*
        @METHOD DistribuisciInTavolo
    
        @OVERVIEW Metodo che distribuisce sul tavolo quattro carte ad ogni
                  nuova mano successiva a quella iniziale
    */
    public void DistribuisciInTavolo()
    {
        int i = 0;
        
        while(i<4 && (!MazzoVuoto()))
        {
            Tavolo.add(Carte.get(0));
            Carte.remove(0);
            
            i++;
        }
    }
    
    /*
        @METHOD NuovoGioco
    
        @OVERVIEW Metodo che inizializza una nuova sessione di gioco
    */
    public void NuovoGioco()
    {                  
        InizializzaTavolo();   
        
        //Test finale
        if(!Tested)
        {
            FinalTest();
            
            Tested = true;
            
            RestartGame();
            
            System.out.println("VERIFICA RIAVVIO - CARTE PRESENTI NEL MAZZO: "+Carte.size());    
        }
        
        boolean Valida = ControlloValidità();

        if(Valida)
        {               
            Turn = ControllaTurno();

            Test.Update();
            
            //Test.ReadFromDump("0251-P1");
        }
        else
        {
            InizializzaMazzo();
            
            Player.Reset();
            
            CPU.Reset();
            
            NuovoGioco();
        }
        
        RewardsDuringGame = new ArrayList();
        
        MeanRewards = new ArrayList();
        
        GameStart = Calendar.getInstance();
    }
    
    /*
        @METHOD Distribuisci
    
        @OVERVIEW Metodo che distribuisce le carte in tavola e nelle mani
                  dei giocatori ad ogni nuova mano diversa da quella iniziale
    */
    public synchronized void Distribuisci()
    {
        if(!MazzoVuoto())
        {
            CPU.PickHand(Carte);
            Player.PickHand(Carte);
            
            DistribuisciInTavolo();
            
            if(MazzoVuoto())
            {
                System.out.println("ULTIMA MANO");
            }

            Turn = ControllaTurno();
        }
        else
        {
            FinePartita();
        }
    }
    
    /*
        @METHOD FinePartita
    
        @OVERVIEW Metodo che chiamato alla fine di una partita controlla chi è stato
                  l'ultimo giocatore ad effettuare prese e sulla base di questo dichiara
                  il vincitore
    */
    public void FinePartita()
    {
        CheckUltimaPresa();
        
        DichiaraVincitore();
    }
    
    /*
        @METHOD FineMano
    
        @OVERVIEW Metodo che nel caso le mani del giocatore e della CPU siano vuote
                  distribuisce nuovamente le carte
    */
    public boolean FineMano()
    {
        if(CPU.GetMano().isEmpty() && Player.GetMano().isEmpty())
        {
            Distribuisci();
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /*
        @METHOD DichiaraVincitore
    
        @OVERVIEW Metodo che effettua il confronto dei punteggi dei due avversari
                  e dichiara il vincitore della partita
    */
    public void DichiaraVincitore()
    {
        Calendar GameEnd = Calendar.getInstance();
        long Difference = ((GameEnd.getTimeInMillis() - GameStart.getTimeInMillis()));
        Gamer.Statistics.AddGameTime(Difference);
        
        String Winner = "";
        
        int GLOBALCPU = 0;
        int GLOBALPLAYER  = 0;
        
        System.out.println("----DICHIARAZIONE DEL GIOCATORE VINCITORE----");
        
        Punteggio CPUSCORE = DichiaraPunteggioCPU();
        Punteggio PLAYERSCORE = DichiaraPunteggioGiocatore();
        
        System.out.println("NUMERO DI CARTE PRESE IN TOTALE:");
        System.out.println("CPU: "+CPUSCORE.GetTotal()+"| GIOCATORE: "+PLAYERSCORE.GetTotal());
        
        if((CPUSCORE.GetTotal() > PLAYERSCORE.GetTotal()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetTotal() < PLAYERSCORE.GetTotal())
        {
            GLOBALPLAYER++;
        }
        
        System.out.println("NUMERO DI DENARI PRESI IN TOTALE:");
        System.out.println("CPU: "+CPUSCORE.GetDenari()+"| GIOCATORE: "+PLAYERSCORE.GetDenari());
        
        if((CPUSCORE.GetDenari() > PLAYERSCORE.GetDenari()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetDenari() < PLAYERSCORE.GetDenari())
        {
            GLOBALPLAYER++;
        }
        
        System.out.println("SETTEBELLO OTTENUTO: ");
        System.out.println("CPU: "+CPUSCORE.GetSettebello()+"| GIOCATORE: "+PLAYERSCORE.GetSettebello());
        if(CPUSCORE.GetSettebello())
        {
            GLOBALCPU++;
        }
        else if(PLAYERSCORE.GetSettebello())
        {
            GLOBALPLAYER++;
        }
        
        System.out.println("PUNTI DI PRIMIERA: ");
        System.out.println("CPU: "+CPUSCORE.GetPrimiera()+"| GIOCATORE: "+PLAYERSCORE.GetPrimiera());
        if((CPUSCORE.GetPrimiera() > PLAYERSCORE.GetPrimiera()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetPrimiera() < PLAYERSCORE.GetPrimiera())
        {
            GLOBALPLAYER++;
        }
        
        System.out.println("SCOPE OTTENUTE: ");
        System.out.println("CPU: "+CPUSCORE.GetScope()+"| GIOCATORE: "+PLAYERSCORE.GetScope());
        if((CPUSCORE.GetScope() > PLAYERSCORE.GetScope()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetScope() < PLAYERSCORE.GetScope())
        {
            GLOBALPLAYER++;
        }
        
        if(GLOBALPLAYER > GLOBALCPU)
        {
            Winning = 1;
            
            Winner = Player.nome;
            System.out.println("VINCE IL GIOCATORE");
        }
        else if(GLOBALPLAYER < GLOBALCPU)
        {
            Winning = -1;
            
            Winner = "CPU";
            System.out.println("VINCE LA CPU");
        }
        else
        {
            Winning = 0;
            
            Winner = "DRAW";
            System.out.println("ABBIAMO UN PAREGGIO");
        }
        
        Endgame = true;
        
        TimesToTrainGame--;
        
        if(!TrainingGame)
        {
            Gamer.UpdateStatsManual(Winning,CPU.CardsPlayed,CPU.GreedyPlayed);
        }
        else
        {
            int CPl = CPU.CardsPlayed;
            int CGPl = CPU.GreedyPlayed;
            int LCPl = Player.LearnerCardsPlayed;
            int LCGPl = BotQ.CardsPlayedGreedily;
            int Expl = BotQ.ExploredMoves;
            int Expt = BotQ.ExploitedMoves;
            
            CPU.CalculateMeanDecisionTime();
            Player.CalculateMeanDecisionTime();
            
            double CPUTime = CPU.MeanDecisionTime;
            double LearnerTime = Player.MeanDecisionTime;
            
            Gamer.Learner = Learner;
            Gamer.SaveLearner(Learner);
            Gamer.UpdateStatsAutomatic(Winning,CPl,CGPl,LCPl,LCGPl,Expl,Expt,CPUTime,LearnerTime);
            
            SingleGameStatsPlot();
        }
        
        if(TimesToTrainGame <= 0)
        {
            ShutDownPlayers();
            //Passaggio alla schermata dei punteggi
            Interface.GoToScores(PLAYERSCORE, CPUSCORE, Winner);
            try
            {
                Gamer.Statistics.PlotGameStats();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            ResetGame();
        }
    }
    
    /*
        @METHOD ContorllaTurno
    
        @OVERVIEW Metodo chiamato al passaggio da un turno all'altro che aggiorna
                  il giocatore e la CPU sulla situazione del tavolo e consente ad 
                  entrambi di valutare bene la situazione
    */
    public boolean ControllaTurno()
    {
        Player.AssignTable(Tavolo);
        
        CPU.AssignTable(Tavolo);
        
        AggiornaKB();

        RivalutaPotenziale();
 
        if(CPU.YourTurn())
        {
            TimeMeasuredFor = 0;
            StartTimer(0);
            //System.out.println("E' il turno della CPU\n");
            
            return true;
        }
        else
        {
            TimeMeasuredFor = 1;
            StartTimer(1);
            //System.out.println("E' il turno del giocatore\n");
            
           // VisualizzaTurno();
            
            return false;
        }
    }
    
    /*
        @METHOD UltimaPresa
    
        @OVERVIEW Metodo che avverte un giocatore che ha effettuato l'ultima presa
                  nella mano corrente
    
        @PAR Plr : Giocatore da avvisare dell'ultima presa effettuata
    */
    public void UltimaPresa(Giocatore Plr)
    {
        if(Plr.equals(Player))
        {
            Player.SetTaken();
            
            CPU.UnsetTaken();
        }
        else
        {
            Player.UnsetTaken();
            
            CPU.SetTaken();
        }
    }
    
    /*
        @METHOD CheckUltimaPresa
    
        @OVERVIEW Metodo che verifica chi è stato tra i due avversari ad aver
                  effettuato l'ultima presa consentendogli di prendere anche le 
                  carte rimaste sul tavolo
    */
    public void CheckUltimaPresa()
    {      
        if(Player.GetLastToTake())
        {
            Ripulisci(Player);
            
            System.out.println("Si aggiudica il resto del tavolo: "+Player.nome);
        }
        else
        {
            Ripulisci(CPU);
            
            System.out.println("Si aggiudica il resto del tavolo: "+CPU.nome);
        }
    }
    
    /*
        @METHOD Ripulisci
    
        @OVERVIEW Metodo che consente al giocatore dato in input di prendere le ultime
                  carte rimaste sul tavolo
    
        @PAR Plyr : Giocatore in input
    */
    public synchronized void Ripulisci(Giocatore Plyr)
    {
        for(Carta card : Tavolo)
        {

            Plyr.Score(card);
        }
        
        Tavolo.clear();
    }
    
    /*
        @METHOD CambioTurno
    
        @OVERVIEW Metodo che cambia il turno corrente ed avvisa entrambi
                  gli avversari del cambiamento
    */
    public void CambioTurno()
    {
        Turn = !Turn;
        
        CPU.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
    }
    
    
    /**
     * @METHOD Switch
     * 
     * @OVERVIEW Metodo che implementa il cambio di turni durante la partita e la
     *           conseguente visualizzazione su interfaccia
     */
    public void Switch()
    {       
            CambioTurno();

            ControllaTurno();
            try
            {
                Interface.Display();
            }
            catch(IndexOutOfBoundsException ex)
            {
                ex.printStackTrace();
                System.out.println("RESET");
                ResetGame();
                Interface.NewGame(false,TrainingGame,Gamer);
            }

    }
    /*
        @METHOD RivalutaPotenziale
    
        @OVERVIEW Metodo che ordina le carte presenti sul tavolo in ordine decrescente di
                  valore e ordina agli avversari di valutare la situazione
    */
    public synchronized void RivalutaPotenziale()
    {
        Collections.sort(Tavolo);
        
        Player.GetTotalPotential(Tavolo);
        
        CPU.GetTotalPotential(Tavolo);
    }
    
    public synchronized boolean PunteggiVisibili()
    {
        return (Scores != null);
    }
    
    /*
        @METHOD ControlloValidità
    
        @OVERVIEW Indica, secondo la "Regola dei tre re", se la prima mano è valida o meno
                  in modo da decidere se continuare la partita o ridistribuire le carte
        
        @RETURNS Validità : Booleano che rappresenta la validità della prima mano della partita
    
        ----NOTA----
        REGOLA DEI TRE RE: Affinché la prima mano della partita sia valida devono esserci meno
        di tre carte re (valore 10) sul tavolo
    */
    public synchronized boolean ControlloValidità()
    {
        boolean response = true;
        
        int NumeroDiRe = 0;
        
        for(Carta C : Tavolo)
        {
            if(C.GetValue() == 10)
            {
                NumeroDiRe++;
            }
            
            if(NumeroDiRe >= 3)
            {
                response = false;
                System.out.println("PARTITA NON VALIDA, LE CARTE VERRANNO DISTRIBUITE NUOVAMENTE\n");
                try 
                {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) 
                {
                    System.out.println("ERRORE DURANTE L'ESECUZIONE DEL PROGRAMMA\n");
                }
            }
        }
        
        return response;
    }
    
    /**
     * @METHOD AggiornaBot
     * 
     * @OVERVIEW Metodo che attiva il bot dandogli una copia dello stato iniziale
     *           in modo che questi lo osservi e decida di conseguenza la mossa
     *           da compiere
     */
    public synchronized void AggiornaBot()
    {
        Stato State = new Stato("LEARNER_STATE",Tavolo,Player.mano,CPU.KB,Player.points,Turn,CPU.mano.size(),0,0);
        
        BotQ.ObserveLearnerState(State);
        
        BotQ.Act();
        
        RivalutaPotenziale();
    }
       
    /*
        @METHOD AggiornaKB
    
        @OVERVIEW Metodo che consente alla CPU di aggiornare la propria Knowledge Base
                  sulla base delle carte che non sono nè sul tavolo nè sulla propria mano
    */
    public void AggiornaKB()
    {
        CPU.AggiornaKB(Carte,Player.mano);
    }
    
    /*
        @METHOD GiocaCarta
    
        @OVERVIEW : Metodo che consente di giocare una carta sul tavolo ed effettuare,
                    se possibile, la presa desiderata
    
       @PAR Plr : Giocatore che effettua la giocata
       @PAR combinazione : Combinazione della presa desiderata (se esiste), settato a 0
            nel caso essa non esista
       @PAR C : Carta da giocare sul tavolo
    */
    public synchronized void GiocaCarta(Giocatore Plr,int combinazione,Carta C)
    {
        DisplayOnInterface(C, combinazione, Plr);
        
        EndTimer(TimeMeasuredFor);
        
        if(Plr.equals(CPU))
        {
            CPU.CardsPlayed++;
        }
  
            Plr.mano.remove(C);

            /*
              Nel caso la carta sia un asso, il giocatore può fare scopa se e soltanto se
              vi è un altro asso in campo ed è l'ultimo rimasto
            */
            boolean AssoDoppio = false;

                if(!C.IsMarked())
                {
                    Tavolo.add(C);
                }
                else
                {
                    RivalutaPotenziale();
                    
                    ArrayList<Carta> Ptz;
                    
                    if(C.HasPotential(combinazione))
                    {
                         Ptz = C.Potenziale.get(combinazione);
                    }
                    else
                    {
                        int MaxPot = C.MaxPotential;
                        
                        Ptz = C.Potenziale.get(MaxPot);
                    }

                    for(Carta card : Ptz)
                    {          
                        if(C.IsAnAce() && card.IsAnAce())
                        {
                            AssoDoppio = true;
                        }

                        Tavolo.remove(card);

                        Plr.Score(card);
                    }

                    Plr.Score(C);

                    UltimaPresa(Plr);
                }

                /*
                    Se il tavolo rimane vuoto dopo la giocata, il giocatore fa scopa
                */
                if(Tavolo.isEmpty())
                {
                    if(!C.IsAnAce() || (AssoDoppio))
                    {
                        Plr.Scopa();

                        UltimaPresa(Plr);
                    }
                }
      
        //PrintAction(Plr,C,combinazione);
            
        boolean EndTurn = FineMano();

        
        
        Switch();
    }
    
    /**
     * @METHOD GiocaIndexCard
     * 
     * @OVERVIEW Metodo che, dati due interi rappresentanti un indice ed una combinazione
     *           consente di giocare la carta e la combinazione indicizzata da questi
     * 
     * @param Plr Giocatore che gioca la carta
     * @param Comb Combinazione da giocare
     * @param Index Indice della carta da giocare all'interno della mano del giocatore
     *              dato in input.
     */
    public synchronized void GiocaIndexCard(Giocatore Plr,int Comb,int Index)
    {
        Carta C = Plr.GetCard(Index);
        
        GiocaCarta(Plr,Comb,C);
    }
   
    /*-----FINE METODI DI GIOCO----*/

    /*-------METODI DI DEBUG----*/
    
    /*
        @METHOD SgomberaTavolo
    
        @OVERVIEW Metodo che elimina tutte le carte dal tavolo di gioco
    */
    public void SgomberaTavolo()
    {
        Tavolo.clear();
    }
    
    /*
        @METHOD GettaMani
    
        @OVERVIEW Metodo che ordina ai giocatori di gettare tutte le carte
                  presenti all'interno delle proprie mani
    */
    public void GettaMani()
    {
        CPU.HandDrop();
        Player.HandDrop();
    }
    
    /*
        @METHOD ManiScoperte
    
        @OVERVIEW Metodo che in sequenza visualizza entrambi le mani dei giocatori
    */
    public void ManiScoperte()
    {
        CPU.VisualizzaMano();
        Player.VisualizzaMano();
    }
    
    /*
        @METHOD ContaBastoni
    
        @OVERVIEW Metodo che conta il numero di carte di bastoni all'interno
                  di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
    
       @RETURNS A : Risultato del conteggio
    */
    public int ContaBastoni(ArrayList<Carta> CRDS)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.seme.equals("bastoni"))
            {
                a++;
            }
        }
        
        return a;
    }
    
    /*
        @METHOD ContaSpade
    
        @OVERVIEW Metodo che conta il numero di carte di spade all'interno
                  di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
    
        @RETURNS A : Risultato del conteggio
    */
    public int ContaSpade(ArrayList<Carta> CRDS)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.seme.equals("spade"))
            {
                a++;
            }
        }
        
        return a;
    }
    
    /*
        @METHOD ContaCoppe
    
        @OVERVIEW Metodo che conta il numero di carte di coppe all'interno
                  di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
    
       @RETURNS A : Risultato del conteggio    
    */
    public int ContaCoppe(ArrayList<Carta> CRDS)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.seme.equals("coppe"))
            {
                a++;
            }
        }
        
        return a;
    }
    
    /*
        @METHOD ContaDenari
    
        @OVERVIEW Metodo che conta il numero di carte di denari all'interno
                  di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
    
     @RETURNS A : Risultato del conteggio    
    */  
    public int ContaDenari(ArrayList<Carta> CRDS)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.seme.equals("denari"))
            {
                a++;
            }
        }
        
        return a;
    }
    
    /*
        @METHOD CercaSetteBello
    
        @OVERVIEW Metodo che verifica se la carta Settebello (ovvero il sette di denari)
                  è presente all'interno di un set di carte o meno
    
        @PAR CRDS : ArrayList di oggetti di tipo carta rappresentante il set di carte in questione
    
        @RETURNS ContainsSB : Risultato della verifica
    */
    public boolean CercaSetteBello(ArrayList<Carta> CRDS)
    {
        Carta C = new Carta('d',7);
        
        return CRDS.contains(C);
    }
    
    /*
        @METHOD ContaValore
    
        @OVERVIEW Metodo che conta il numero di carte di un determinato valore in input
                  all'interno di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
       @PAR v1 : Valore delle carte da includere nel conteggio
    
     @RETURNS A : Risultato del conteggio    
    */ 
    public int ContaValore(ArrayList<Carta> CRDS, int vl)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.valore == vl)
            {
                a++;
            }
        }
        
        return a;
    }
    
    /*
        @METHOD VerificaKB
    
        @OVERVIEW Metodo che verifica le proprietà principali della Knowledge Base
                  della CPU, stampandole in output
    
       @PAR Computer : CPU di cui esaminare la KnowledgeBase
    */
    public void VerificaKB(CPU Computer)
    {     
        int Confronto = (ContaBastoni(Carte) + ContaBastoni(Player.mano));
        
        System.out.println("CHECK BASTONI: "+Confronto);
        
        Confronto = (ContaCoppe(Carte) + ContaCoppe(Player.mano));
        
        System.out.println("CHECK COPPE: "+Confronto);
        
        Confronto = (ContaSpade(Carte) + ContaSpade(Player.mano));
        
        System.out.println("CHECK SPADE: "+Confronto);
        
        Confronto = (ContaDenari(Carte) + ContaDenari(Player.mano));
        
        System.out.println("CHECK DENARI: "+Confronto);
        
        boolean Verifica = (Computer.KB.Settebello) == (CercaSetteBello(Carte) || CercaSetteBello(Player.mano));
        
        System.out.println("CHECK SETTEBELLO: "+Verifica);
    }
    /*-------FINE METODI DI DEBUG----*/
    
     /*--------------METODI GETTERS E SETTERS-----------------*/
    
    /*
        @METHOD GetTavolo
    
        @OVERVIEW Metodo che restituisce il tavolo da gioco
    
        @RETURNS Table : ArrayList di oggetti di tipo Carta rappresentante il Tavolo da gioco
    */
    public ArrayList<Carta> GetTavolo()
    {
        return Tavolo;
    }

    /*
        @METHOD MazzoVuoto
    
        @OVERVIEW Metodo che verifica se il mazzo è vuoto o meno
    
        @RETURNS Res : Booleano che indica se il mazzo è vuoto o meno
    */
    public boolean MazzoVuoto()
    {
        return (Carte.isEmpty());
    }
    
    /*
        @METHOD CarteRimaste
    
        @OVERVIEW Metodo che indica quante carte sono rimaste nel mazzo
    
        @RETURNS Res : Intero rappresentante il numero di carte rimaste nel mazzo
    */
    public int CarteRimaste()
    {
        return Carte.size();
    }
    
    /*
        @METHOD GetCPU
    
        @OVERVIEW Metodo che restituisce in output l'istanza della CPU in esecuzione
                  all'interno del gioco
    
        @RETURNS Computer : Istanza della CPU in esecuzione all'interno del gioco
    */
    public Giocatore GetCPU()
    {
        return this.CPU;
    }
    
    /*
        @METHOD GetPlayer
    
        @OVERVIEW Metodo che restituisce in output l'istanza del giocatore in esecuzione
                  all'interno del gioco
    
        @RETURNS Computer : Istanza del gicoatore in esecuzione all'interno del gioco
    */
    public Giocatore GetPlayer()
    {
        return this.Player;
    }
    
    public void SetTimesToTrain(int Times)
    {
        TimesToTrainGame = Times;
    }
    
    /*
        @METHOD DichiaraPunteggioCPU
    
        @OVERVIEW Metoro che restituisce il punteggio della CPU
    
        @RETURNS CPUPoints : Punteggio della CPU
    */
      public Punteggio DichiaraPunteggioCPU()
    {
        return CPU.GetPoints();
    }
    
      /*
        @METHOD DichiaraPunteggioGiocatore
    
        @OVERVIEW Metoro che restituisce il punteggio del Giocatore
    
        @RETURNS PlayerPoints : Punteggio del Giocatore
    */
    public Punteggio DichiaraPunteggioGiocatore()
    {
        return Player.GetPoints();
    }
    /*------------------------------------------------------------------*/
    
    /*------METODI DEL CICLO DI VITA DEL THREAD---------*/
    
    /*
        @METHOD Halt
    
        @OVERVIEW Metodo che pone fine alla partita ed arresta il gioco ed 
                  i thread dei giocatori dopo aver settato il flag di arresto
                  (variabile Halt) a true.
                  Il metodo successivamente ordina all'interfaccia di chiudersi
    */
    public void Halt()
    {
        System.out.println("Fine della partita\n");
        
        Halt = true;
        
        Player.ShutDown();
        
        CPU.ShutDown();
        
        Sfidanti.shutdownNow();     
        
        Interface.Exit();
    }
    
    /*
        @METHOD IsHalted
    
        @OVERVIEW Metodo che verifica se il gioco è in arresto restituendo l'opportuna
                  variabile d'istanza (Halt)
    
       @RETURNS Halt : Variabile d'istanza che indica se il gioco è in arresto o meno
    */
    public static boolean IsHalted()
    {
        return Halt;
    }
    /*-----------------------------------------------------*/
    
    public void DisplayOnInterface(Carta Card,int Comb,Giocatore P)
    {
        try
        {
            Interface.RevealCard(P, Card, Comb);
        }
        catch(IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            ResetGame();
        }
    }
    /*
        @METHOD VisualizzaTurno
    
        @OVERVIEW Metodo che visualizza durante il turno del giocatore le carte nella sua mano
                  e le carte sul tavolo sotto forma di caratteri su console
    */
    public void VisualizzaTurno()
    {
        System.out.println("===================================================");
        System.out.println("----------------TAVOLO-----------------------------");
        for(Carta C : Tavolo)
        {
            System.out.print("| "+C.seme+"-"+C.valore+" |-");
        }
        System.out.println("\n");
        
        System.out.println("------------MANO DEL GIOCATORE----------------------");
        for(Carta C : Player.GetMano())
        {
              System.out.print("| "+C.seme+"-"+C.valore+" |-");
        }
        System.out.println("\n");
        System.out.println("===================================================");
        System.out.println("\n");
    }
    
    /*
        @METHOD StandOff
    
        @OVERVIEW Metodo che verifica se indica se viene riscontrata una delle prime due
                  condizioni di stallo
                   
                  CONDIZIONE DI STALLO 1 : Turno del giocatore e mano del giocatore vuota
                  CONDIZIONE DI STALLO 2 : Turno della CPU e mano della CPU vuota
    
        @RETURNS InStandoff : Valore booleano che indica se una delle due condizioni di cui sopra
                 si è verificata o meno
    */
    public boolean StandOff()
    {
        boolean Condition1 = ((Player.mano.isEmpty()) && (Player.YourTurn()));
        boolean Condition2 = ((CPU.mano.isEmpty()) && (CPU.YourTurn()));
        
        return (Condition1 || Condition2);
    }
    
    /*
        @METHOD NoCardForNoOne
    
        @OVERVIEW Metodo che verifica se viene riscontrata la terza condizione di stallo
    
                 CONDIZIONE DI STALLO 3 : Le mani di entrambi i giocatori sono vuote
    
        @RETURNS NoOneHasCards : Valore booleano che indica se la condizione di cui sopra
                  si è verificata o meno
    */
    public boolean NoCardForNoOne()
    {
        boolean Condition1 = ((Player.mano.isEmpty()) && (CPU.mano.isEmpty()));
        
        return Condition1;
    }

    /*
        @METHOD FinalTest
    
        @OVERVIEW Metodo che prepara l'oggetto TesterDebugger ad effettuare il test
                  finale prima dell'esecuzione del gioco, consistente nell'esecuzione
                  sequenziale del test per casi della visita DFS e del test per casi
                  della generazione dell'albero di gioco e della visita mediante
                  algoritmo MiniMax
    */
    public void FinalTest()
    {        
        StopPlayers();
        
        Tavolo = InizializzaTavolo();
        
        Test.Set(Tavolo, Carte);
        
        Test.DFSTest();
        
        Test.AITest();
    }
    
    /*
        @METHOD StopPlayers
    
        @OVERVIEW Metodo che comanda ai due avversari di fermarsi settando l'opportuna 
                  variabile d'istanza della classe Giocatore (turn) a false.
    
                  Utilizzato per impedire che essi interferiscano con il test finale, dove
                  le loro mosse sono stabilite all'interno dei metodi di testing finale
    */
    public void StopPlayers()
    {
        Player.StopTurn();
        CPU.StopTurn();
    }
    
    public void ShutDownPlayers()
    {
        Player.ShutDown();
        CPU.ShutDown();
        Sfidanti.shutdownNow(); 
    }
    
    /*
        @METHOD RestarGame
    
        @OVERVIEW Metodo che re-inizializza il gioco  da zero
                  Utilizzato dopo l'esecuzione del test finale
    */
    public void RestartGame()
    {
        Player.Reset();
        CPU.Reset();
        
        InizializzaMazzo();     
        InizializzaTavolo();
        RestartPlayers();
    }
    
    /*
        @METHOD GoPlayers
    
        @OVERVIEW Metodo che indica ai due avversari che è possibile riprendere l'esecuzione
                  delle loro mosse settando l'opportuna  variabile d'istanza della classe Giocatore (turn) a false.
    
                  Utilizzato dopo l'esecuzione del test finale
    */
    public void GoPlayers()
    {
        boolean Mazziere = rnd.nextBoolean();
        
        Turn = !Mazziere;
        
        CPU.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
    }
    
    public Stato WatchState(String Label)
    {
        Stato State = new Stato(Label,Tavolo,Player.mano,CPU.KB,Player.points,Turn,CPU.mano.size(),0,0);
        
        return State;
    }
     
    /*--------------------------------------*/
    
    /*----METODI DEL CICLO DI VITA DEL THREAD----*/
    
    /*
        @METHOD run
    
        @OVERVIEW Metodo che implementa il ciclo di vita principale dell'istanza
                  della classe gioco
    */
    @Override
     public void run()
     {
         while(!Halt)
         {
             try
             {
                if(StandOff())
                {
                     CambioTurno();
                }
             
                if(NoCardForNoOne())
                {
                     Distribuisci();
                }
             }
             catch(IndexOutOfBoundsException e)
             {
                 ResetGame();
             }
         }
     }
     
     /**
      * @METHOD CardSuggestion
      * 
      * @OVERVIEW Metodo che restituisce in output la carta suggerita dal bot
      * 
      * @return Suggested Intero rappresentante l'indice di posizione della carta
      *         da giocare all'interno della mano del giocatore
      */
     public int CardSuggestion()
     {
         int Suggested = BotQ.ReturnCardIndex();
         return Suggested;
     }
     
     /**
      * @METHOD CombinationSuggestion
      * 
      * @OVERVIEW Metodo che restituisce in output la combinazione suggerita dal bot
      * 
      * @return Suggested Intero rappresentante l'indice di posizione della combinazione
      *         da giocare tra le combinazioni possibili con la carta suggerita
      *         dal bot
      */
     public int CombinationSuggestion()
     {
         int Suggested = BotQ.ReturnCombination();
         return Suggested;
     }
     
     public void AddMoveReward(double Reward)
     {
         RewardsDuringGame.add(Reward);
     }
     
     public void AddMeanRewardUntilLastMove()
     {
         int i=0;
         double Sum = 0.0;
         double Result = 0.0;
         int MoveIndex = RewardsDuringGame.size();
         for(i=0; i<MoveIndex; i++)
         {
             Sum += RewardsDuringGame.get(i);
         }
         
         Result = Sum / (MoveIndex * 1.0);
        
         MeanRewards.add(Result);
     }
     
    public void StartTimer(int ChronoPlr)
    {
        TimeMeasuredFor = ChronoPlr;
        
        Start = Calendar.getInstance();
    }
    
    public void EndTimer(int ChronoPlr)
    {
        Calendar End = Calendar.getInstance();
        long Difference = (End.getTimeInMillis() - Start.getTimeInMillis());
        
        if(ChronoPlr == 0)
        {
            CPU.DecisionsTimeDuration.add(Difference);
        }
        else
        {
            Player.DecisionsTimeDuration.add(Difference);
        }
        
        System.out.println("TIMEFOR:"+ChronoPlr+" DIFFERENCE:"+Difference);
    }
    
    public void SingleGameStatsPlot()
    {
        FileWriter PlotFile = null;
        String path = "src/main/statisticsdumps/plottable/";
        String TimeStamp = new SimpleDateFormat("HH.mm.ss").format(new Date());
        File Plot = new File(path+"PlottableStatsGame_"+Gamer.name+"_"+TimeStamp+".txt");
        
        try 
        {
            PlotFile = new FileWriter(Plot);
            PlotFile.write("Actions | Reward | MeanReward | Variance\n");
            
            for(int i=0; i<RewardsDuringGame.size(); i++)
            {
                double InstantReward = RewardsDuringGame.get(i);
                double Mean = MeanRewards.get(i);
                double Variance = Math.abs(InstantReward-Mean);
                String Reward = String.format("%.4f",InstantReward);
                String MeanRewardAtThatPoint = String.format("%.4f",Mean);
                String InstantRewardVariance = String.format("%.4f",Variance);
                PlotFile.write(i+"|"+Reward+"|"+MeanRewardAtThatPoint+"|"+InstantRewardVariance+"\n");
            }
            
            PlotFile.close();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
     
     /**
      * @METHOD PrintAction
      * 
      * @OVERVIEW Metodo che stampa in output un resoconto sintetico della carta
      *           giocata da un giocatore ed eventualmente della presa effettuata
      *           tramite quest'ultima da esso
      * 
      * @param Plr Giocatore che effettua l'azione
      * @param C Carta giocata
      * @param PotentialIndex Indice della combinazione corrispondente alla
      *                       presa effettuata dal giocatore Plr
      */
     public void PrintAction(Giocatore Plr,Carta C,int PotentialIndex)
     {
         System.out.println("-----------TURNO: "+Plr.nome+"--------------------");
         System.out.println(Plr.nome+" ha scelto di giocare: "+C.GetName()+"\n");
            
            if(C.IsMarked())
            {
                System.out.println(" Ed ha preso: ");
                
                System.out.print("\t");
                C.StampaSelezione(PotentialIndex);
                System.out.println("\n");
            }
            
          System.out.println("------------FINE TURNO----------------\n");
     }
     
     public void DumpBot()
    {
        File MatrixDump = new File("src/main/Matrix.txt");
        try 
        {
        FileWriter MatrixWr = new FileWriter(MatrixDump);
        

        for(int i=0; i<98000;i++)
        {
            for(int j=0; j<112;j++)
            {
                if(Learner.getModel().getQ(i,j) != 0.1)
                {
                    MatrixWr.append("i: "+i+"j: "+j+"Reward: "+Learner.getModel().getQ(i,j)+"\n");
                }
            }
        }
        } catch (IOException ex) 
        {
            Logger.getLogger(QBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
 
}
