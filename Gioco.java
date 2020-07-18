package assopigliatutto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Gioco extends Thread
{
     /*--------VARIABILI D'ISTANZA----------*/
    CPU CPU;
    
    Giocatore Player;
    
    ArrayList<Carta> Carte;
    
    ArrayList<Carta> Tavolo;
    
    TesterDebugger Test;
    
    Board Interface;
    
    boolean Turn;
    
    static boolean Halt;
    
    ExecutorService Sfidanti = Executors.newFixedThreadPool(2);
     /*-------------------------------------------*/
    
    /*-------METODO COSTRUTTORE--*/
    
    public Gioco(Board I) 
    {
        InizializzaGiocatori();
        InizializzaMazzo();
        
        Halt = false;
        
        Interface = I;

        Test = new TesterDebugger(this); 
    }
    /*---------------------------*/
    
    /*--------METODI DI INIZIALIZZAZIONE DELLE VARIABILI D'ISTANZA----*/
    public void InizializzaGiocatori()
    {
        CPU = new CPU("CPU",true,this,Tavolo);
        Player = new Giocatore("Player",false,Tavolo);
        
        Sfidanti.submit(CPU);
        Sfidanti.submit(Player);
    }
    
    public void InizializzaMazzo()
    {
        Carte = new ArrayList();
        
        for(int i=1; i<=10; i++)
        {
            Carte.add(new Carta('s',i));
            Carte.add(new Carta('b',i));
            Carte.add(new Carta('c',i));
            Carte.add(new Carta('d',i));
        }      
    }
    
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
        
        return T;    
    }
    /*-----------------------------------------------------------*/
    
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
    
    public void NuovoGioco()
    {       
        Random rnd = new Random();
        
        //MISCHIAMO IL MAZZO
        Collections.shuffle(Carte);
       
        boolean Mazziere = rnd.nextBoolean(); 

        CPU.SetMazziere(Mazziere);
        
        Player.SetMazziere(!Mazziere);

        CPU.PickHand(Carte);
        
        Player.PickHand(Carte);
        
        Turn = !Mazziere;
        
        CPU.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
        
        Tavolo = InizializzaTavolo();

        boolean Valida = ControlloValidità();
        
        if(Valida)
        {
            Collections.sort(Tavolo);
            
            Test.ConfigurazioneCritica(Tavolo);
            
            Turn = ControllaTurno();
        }
        else
        {
            InizializzaMazzo();
            
            Player.Reset();
            CPU.Reset();
            
            NuovoGioco();
        }
    }
    
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
    
    public void FinePartita()
    {
        CheckUltimaPresa();
        
        DichiaraVincitore();
    }
    
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
    
    public void DichiaraVincitore()
    {
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
            Winner = Player.nome;
            System.out.println("VINCE IL GIOCATORE");
        }
        else if(GLOBALPLAYER < GLOBALCPU)
        {
            Winner = "CPU";
            System.out.println("VINCE LA CPU");
        }
        else
        {
            Winner = "DRAW";
            System.out.println("ABBIAMO UN PAREGGIO");
        }
        
        Interface.GoToScores(PLAYERSCORE, CPUSCORE, Winner);
    }
    
    public boolean ControllaTurno()
    {
        Player.AssignTable(Tavolo);
        
        CPU.AssignTable(Tavolo);
        
        AggiornaKB();

        RivalutaPotenziale();
 
        if(CPU.YourTurn())
        {
            System.out.println("E' il turno della CPU\n");
            
            return true;
        }
        else
        {
            System.out.println("E' il turno del giocatore\n");
            
            VisualizzaTurno();
            
            return false;
        }
    }
    
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
    
    public synchronized void Ripulisci(Giocatore Plyr)
    {
        for(Carta card : Tavolo)
        {
            System.out.println("@ "+card.nome);
            
            Plyr.Score(card);
        }
        
        Tavolo.clear();
    }
    
    public void CambioTurno()
    {
        Turn = !Turn;
        
        CPU.AssegnaTurno(Turn);
        Player.AssegnaTurno(!Turn);
    }
    
    public synchronized void RivalutaPotenziale()
    {
        Collections.sort(Tavolo);
        
        Player.GetTotalPotential(Tavolo);
        
        CPU.GetTotalPotential(Tavolo);
    }
    
    public boolean ControlloValidità()
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
    
    public void AggiornaKB()
    {
        CPU.AggiornaKB(Carte,Player.mano);
    }
    
    public void GiocaCarta(Giocatore Plr,int combinazione,Carta C)
    {
        Plr.mano.remove(C);

        boolean AssoDoppio = false;
        
            if(!C.IsMarked())
            {
                Tavolo.add(C);
            }
            else
            {
                ArrayList<Carta> Ptz = C.Potenziale.get(combinazione);

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

            if(Tavolo.isEmpty())
            {
                if(!C.IsAnAce() || (AssoDoppio))
                {
                    Plr.Scopa();
                    
                    UltimaPresa(Plr);
                }
            }
        boolean EndTurn = FineMano();

        CambioTurno();
        
        ControllaTurno();
        
        Interface.Display();
    }

    /*-------METODI DI DEBUG----*/
    public void SgomberaTavolo()
    {
        Tavolo.clear();
    }
    
    public void GettaMani()
    {
        CPU.HandDrop();
        Player.HandDrop();
    }
    
    public void ManiScoperte()
    {
        CPU.VisualizzaMano();
        Player.VisualizzaMano();
    }
    
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
    
    public boolean CercaSetteBello(ArrayList<Carta> CRDS)
    {
        Carta C = new Carta('d',7);
        
        return CRDS.contains(C);
    }
    
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
    public ArrayList<Carta> GetTavolo()
    {
        return Tavolo;
    }

    public boolean MazzoVuoto()
    {
        return (Carte.isEmpty());
    }
    
    public int CarteRimaste()
    {
        return Carte.size();
    }
    
    public Giocatore GetCPU()
    {
        return this.CPU;
    }
    
    public Giocatore GetPlayer()
    {
        return this.Player;
    }
    
      public Punteggio DichiaraPunteggioCPU()
    {
        return CPU.GetPoints();
    }
    
    public Punteggio DichiaraPunteggioGiocatore()
    {
        return Player.GetPoints();
    }
    /*------------------------------------------------------------------*/
    
    /*------METODI DEL CICLO DI VITA DEL THREAD---------*/
    public void Halt()
    {
        System.out.println("Fine della partita\n");
        
        Halt = true;
        
        Player.ShutDown();
        CPU.ShutDown();
        
        Sfidanti.shutdownNow();     
        
        Interface.Exit();
    }
    
    public static boolean IsHalted()
    {
        return Halt;
    }
    /*-----------------------------------------------------*/
    
     public Carta DecodificaCarta(int codice)
    {
        Carta C;
        
        char s;
        int v;
        
         if(codice >= 30)
         {
             s = 's';
             v = codice - 30;
         }
         else if(codice<30 && codice>=20)
         {
             s = 'b';
             v = codice - 20;
         }
         else if(codice<20 && codice>=10)
         {
             s = 'c';
             v = codice - 10;
         }
         else if(codice<10 && codice>=0)
         {
             s = 'd';
             v = codice;
         }
         else
         {
             throw new IllegalArgumentException();
         }
         
         C = new Carta(s,v);
         
         return C;
    }
     
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
    
    public boolean StandOff()
    {
        boolean Condition1 = ((Player.mano.isEmpty()) && (Player.YourTurn()));
        boolean Condition2 = ((CPU.mano.isEmpty()) && (CPU.YourTurn()));
        
        return (Condition1 || Condition2);
    }
    
    public boolean NoCardForNoOne()
    {
        boolean Condition1 = ((Player.mano.isEmpty()) && (CPU.mano.isEmpty()));
        
        return Condition1;
    }
     
    @Override
     public void run()
     {
         while(!Halt)
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
     }
}
