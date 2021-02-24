/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assopigliatutto;

import com.github.chen0040.rl.learning.qlearn.QLearner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Enrico Tomasi
 */
public class StubGame extends Gioco
{
    int ActualPlayed;
    
    double Progress;
    
    Profile DojoProfile;
    LearningPlayer LP;
    LearningPlayer Dojo;
    QBot DojoBot;
    QLearner DojoLearner;
    
    public StubGame(boolean DoTest,boolean IsTraining,Profile P,Profile DjPrf,int ToPlay,int Played)
    {
        super(null,false,true,P);
        
        rnd = new Random();
        
        TestGame = DoTest;
        
        TrainingGame = IsTraining;
        
        TimesToTrainGame = 1;
        
        ActualPlayed = Played;
        
        Progress = ((double) Played / (double) ToPlay) * 100.0;
        
        InizializzaProfili(P,DjPrf);
        
        InizializzaMazzo();     
         
        InizializzaGiocoAutonomo(Gamer.name,DjPrf.name);
               
        InizializzaTurni();
        
        InizializzaBots();
        
        InizializzaDettagli();
    }
  
    public void InizializzaGiocoAutonomo(String Plr,String DJPlr)
    {
        InizializzaApprendisti(Plr,DJPlr);
        
        SpeedUp();
        
        AttivaSfidanti();
    }
    
    public void InizializzaBots()
    {
        BotQ = new QBot(this,Learner,true);
        BotQ.SetPlayer(Player);
        
        DojoBot = new QBot(this,DojoLearner,true);
        DojoBot.SetPlayer(Dojo);
    }
    
    @Override
      public void InizializzaTurni()
    {
        boolean Mazziere = rnd.nextBoolean(); 

        Dojo.SetMazziere(Mazziere);
        
        Player.SetMazziere(!Mazziere);

        Dojo.PickHand(Carte);
        
        Player.PickHand(Carte);
        
        Turn = !Mazziere;
        
        Dojo.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
    }
      
    public void InizializzaProfili(Profile P,Profile DojFile)
    {
        Gamer = P;
        Database = P.DB;
        Learner = P.Learner;
        
        DojoProfile = DojFile;
        DojoLearner = DojFile.Learner;
    }
    
       public void InizializzaApprendisti(String PlayerName,String DojoPName)
    {
        Player = new LearningPlayer(PlayerName,false,this,Tavolo);
        Gamer.AssignPlayer(Player);
        
        Dojo = new LearningPlayer(DojoPName,false,this,Tavolo);
        DojoProfile.AssignPlayer(Dojo);
    }
    
     public void InizializzaDettagli()
    {
        Halt = false;
        
        Endgame = false;

        Test = new TesterDebugger(this); 

        Scores = null;
    }
    
        /**
     * @METHOD AttivaSfidanti
     * 
     * @OVERVIEW Metodo che attiva i thread relativi al giocatore (o al bot) 
     *           ed alla CPU in modo da iniziare la partita.
     */
    @Override
    public void AttivaSfidanti()
    {
        InizializzaApprendista("Dojo");
        InizializzaApprendista("Player");
        
        Sfidanti.submit(Dojo);
        Sfidanti.submit(Player);  
    }
    
    @Override
    public void DichiaraVincitore()
    {
        String Winner = "";
        
        int GLOBALCPU = 0;
        int GLOBALPLAYER  = 0;

        Punteggio CPUSCORE = DichiaraPunteggioCPU();
        Punteggio PLAYERSCORE = DichiaraPunteggioGiocatore();
      
        if((CPUSCORE.GetTotal() > PLAYERSCORE.GetTotal()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetTotal() < PLAYERSCORE.GetTotal())
        {
            GLOBALPLAYER++;
        }
              
        if((CPUSCORE.GetDenari() > PLAYERSCORE.GetDenari()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetDenari() < PLAYERSCORE.GetDenari())
        {
            GLOBALPLAYER++;
        }
     
        if(CPUSCORE.GetSettebello())
        {
            GLOBALCPU++;
        }
        else if(PLAYERSCORE.GetSettebello())
        {
            GLOBALPLAYER++;
        }
 
        if((CPUSCORE.GetPrimiera() > PLAYERSCORE.GetPrimiera()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetPrimiera() < PLAYERSCORE.GetPrimiera())
        {
            GLOBALPLAYER++;
        }
        
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

        int CPl = Dojo.CardsPlayed;
        int CGPl = DojoBot.CardsPlayedGreedily;
        int LCPl = Player.LearnerCardsPlayed;
        int LCGPl = BotQ.CardsPlayedGreedily;
        int Expl = BotQ.ExploredMoves;
        int Expt = BotQ.ExploitedMoves;

        Dojo.CalculateMeanDecisionTime();
        Player.CalculateMeanDecisionTime();
            
        double DojoTime = Dojo.MeanDecisionTime;
        double LearnerTime = Player.MeanDecisionTime;
            
        Gamer.Learner = Learner;
        Gamer.SaveLearner(Learner);
        Gamer.UpdateStatsAutomatic(Winning,CPl,CGPl,LCPl,LCGPl,Expl,Expt,DojoTime,LearnerTime);
        
        DojoProfile.Learner = DojoLearner;
        DojoProfile.SaveLearner(DojoLearner);
        
        System.out.println("PERCENTUALE TRAINING COMPLETATA: "+Progress+"%\n");
        
        ResetGame();
        }
    
    @Override
    public void Switch()
    {
        CambioTurno();

        ControllaTurno();
    }
    
    @Override
    public void Halt()
    {
        System.out.println("Fine della partita\n");
        
        Halt = true;
        
        Player.ShutDown();
        
        Dojo.ShutDown();
        
        Sfidanti.shutdownNow();  
    }
    
    @Override
    public void ResetGame()
    {               
    Player.ShutDown();

    Dojo.ShutDown();

    Sfidanti.shutdownNow();  

    this.SetTimesToTrain(TimesToTrainGame--);

    if(TrainingGame)
    {
        BotQ.ClearMoves();
    }

    this.NuovoGioco();
        
    }
    
    /*
        @METHOD InizializzaTavolo
    
        @OVERVIEW Metodo che inizializza il tavolo da gioco inserendovi all'interno
                  le prime quattro carte (quelle "in cima") al mazzo opportunamente
                  mescolato
    */
    @Override
    public ArrayList<Carta> InizializzaTavolo()
    {
        ArrayList<Carta> T = new ArrayList();
        
        int i;
        
        for(i=0;i<4;i++)
        {   
            T.add(Carte.get(0));
            Carte.remove(0);
        }
        
        Dojo.AssignTable(T);
        Player.AssignTable(T);
        
        Tavolo = T;
        
        Collections.sort(Tavolo);
        
        return T;    
    }
    
    /*
        @METHOD NuovoGioco
    
        @OVERVIEW Metodo che inizializza una nuova sessione di gioco
    */
    @Override
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
            
            Dojo.Reset();
            
            NuovoGioco();
        }
        
        RewardsDuringGame = new ArrayList();
        
        MeanRewards = new ArrayList();
    }
    
    /*
        @METHOD Distribuisci
    
        @OVERVIEW Metodo che distribuisce le carte in tavola e nelle mani
                  dei giocatori ad ogni nuova mano diversa da quella iniziale
    */
    @Override
    public synchronized void Distribuisci()
    {
        if(!MazzoVuoto())
        {
            Dojo.PickHand(Carte);
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
        @METHOD FineMano
    
        @OVERVIEW Metodo che nel caso le mani del giocatore e della CPU siano vuote
                  distribuisce nuovamente le carte
    */
    @Override
    public boolean FineMano()
    {
        if(Dojo.GetMano().isEmpty() && Player.GetMano().isEmpty())
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
        @METHOD ContorllaTurno
    
        @OVERVIEW Metodo chiamato al passaggio da un turno all'altro che aggiorna
                  il giocatore e la CPU sulla situazione del tavolo e consente ad 
                  entrambi di valutare bene la situazione
    */
    public boolean ControllaTurno()
    {
        Player.AssignTable(Tavolo);
        
        Dojo.AssignTable(Tavolo);
        
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
            
            Dojo.UnsetTaken();
        }
        else
        {
            Player.UnsetTaken();
            
            Dojo.SetTaken();
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
            Ripulisci(Dojo);
            
            System.out.println("Si aggiudica il resto del tavolo: "+CPU.nome);
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
        
        Dojo.GetTotalPotential(Tavolo);
    }
    
    @Override
    public void DisplayOnInterface(Carta Card,int Comb,Giocatore P)
    {
        
    }
    
    
}


