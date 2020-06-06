package assopigliatutto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Gioco
{
    CPU CPU;
    Giocatore Player;
    
    ArrayList<Carta> Carte;
    ArrayList<Carta> Tavolo;
    
    TesterDebugger Test;
    
    boolean Turn;
    
    ExecutorService Sfidanti = Executors.newFixedThreadPool(2);
    
    public Gioco() 
    {
        InizializzaGiocatori();
        InizializzaMazzo();
        
        Test = new TesterDebugger(this); 
    }
    
    public void InizializzaGiocatori()
    {
        CPU = new CPU("CPU",true,this);
        Player = new Giocatore("Player",false);
        
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
        
        CPU.AssegnaTurnoInizio(Mazziere);
        
        Player.AssegnaTurnoInizio(!Mazziere);
        
        Tavolo = InizializzaTavolo();

        boolean Valida = ControlloValidità();
        
        if(Valida)
        {
            Collections.sort(Tavolo);

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
    
    public void Distribuisci()
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
        
        if((CPUSCORE.GetSettebello() > PLAYERSCORE.GetSettebello()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetSettebello() < PLAYERSCORE.GetSettebello())
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
            System.out.println("VINCE IL GIOCATORE");
        }
        else if(GLOBALPLAYER < GLOBALCPU)
        {
            System.out.println("VINCE LA CPU");
        }
        else
        {
            System.out.println("ABBIAMO UN PAREGGIO");
        }
    }
    
    public Punteggio DichiaraPunteggioCPU()
    {
        return CPU.GetPoints();
    }
    
    public Punteggio DichiaraPunteggioGiocatore()
    {
        return Player.GetPoints();
    }
    
    public boolean ControllaTurno()
    {
        Player.AssignTable(Tavolo);
        CPU.AssignTable(Tavolo);
        
        if(CPU.YourTurn())
        {
            System.out.println("E' il turno della CPU\n");
            return true;
        }
        else
        {
            System.out.println("E' il turno del giocatore\n");
            return false;
        }
    }
    
    public void CambioTurno()
    {
        CPU.CambioTurno();
        Player.CambioTurno();
    }
    
    public void RivalutaPotenziale()
    {
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
    
    public void GiocaCarta(Giocatore Plr,int combinazione,Carta C)
    {
        Plr.mano.remove(C);
        Plr.Score(C);
        
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
            }

            if(Tavolo.isEmpty())
            {
                if(!C.IsAnAce() || (AssoDoppio))
                {
                    Plr.Scopa();
                }
            }
        boolean EndTurn = FineMano();
        
        CambioTurno();
        
        ControllaTurno();
        
        RivalutaPotenziale();
    }
    
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
}
