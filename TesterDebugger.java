package assopigliatutto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TesterDebugger 
{
    Gioco Sessione;
    
    Giocatore Computer;
    Giocatore P1;
    
    ArrayList<Carta> Mz;
    ArrayList<Carta> Tv;
    
    int Attempts;
    
    Board B;
    
    public TesterDebugger(Gioco game)
    {
        Sessione = game;
        Computer = game.CPU;
        P1 = game.Player;
        Mz = game.Carte;
        Tv = game.Tavolo;
        
        Attempts = 0;
    }
    
    public TesterDebugger(Board board)
    {
        B = board;
    }
    
    public static Carta decodifica(int codice)
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
    
    public void TestConf()
    {
        Tv.remove(3);
        Tv.add(3,new Carta('s',P1.GetMano().get(0).GetValue()));
        Tv.add(4,new Carta('c',P1.GetMano().get(0).GetValue()));
        Tv.add(3,new Carta('d',P1.GetMano().get(0).GetValue()));
    }   
      
    public void StampaConsoleDebug()
    {        
        StampaMazzo(Mz);
        
        System.out.println("\n");

        System.out.println("GIOCATORI: "+Computer.GetName()+" MAZZIERE: "+Computer.mazziere+" || "+P1.GetName()+" MAZZIERE: "+P1.mazziere);

        System.out.println("\n");
        
        Computer.VisualizzaMano();
        
        System.out.println("\n");
        
        P1.VisualizzaMano();
        
        System.out.println("\n");
        
        StampaTavolo(Tv);
        
        StampaMazzo(Mz);
    }
    
    public void StampaMazzo(ArrayList<Carta> Mazzo)
    {
        System.out.println("ANALIZZIAMO IL MAZZO\n");
        System.out.println("SONO PRESENTI "+Mazzo.size()+" CARTE");
        
        for(int i=0; i<Mazzo.size(); i++)
        {
            Carta C = Mazzo.get(i);
            System.out.println("CARTA: "+C.nome+" |VALORE: "+C.valore+" |SEME: "+C.seme+" |CODICE: "+C.codice);
        } 
    }
    
    public void StampaTavolo(ArrayList<Carta> T)
    {
        System.out.println("ANALIZZIAMO IL TAVOLO\n");
        System.out.println("SONO PRESENTI "+T.size()+" CARTE");
        
        for(int i=0; i<T.size(); i++)
        {
            Carta C = T.get(i);
            System.out.println("CARTA: "+C.nome+" |VALORE: "+C.valore+" |SEME: "+C.seme+" |CODICE: "+C.codice);
        } 
    }
    
    public void TestPotenziali()
    {
        for(Carta c : P1.GetMano())
        {
            c.StampaPotenziale();
        }
    }
    
    public void SlotTest()
    {
        ArrayList<Slot> Pl = B.PlayerHand;
        ArrayList<Slot> Cpu = B.CPUHand;
        ArrayList<Slot> T = B.CardsOnTable;
        
        System.out.println("SLOT MANO GIOCATORE: ");
        
        for(Slot S : Pl)
        {         
            if(S.HasCard())
            {
                Carta C = S.GetCard();
                
                System.out.print("NOME CARTA: "+C.nome);
                C.StampaPotenziale();
            }
        }
        
        System.out.println("SLOT CPU: ");
        
        for(Slot S : Cpu)
        {          
            if(S.HasCard())
            {
                Carta C = S.GetCard();
                
                System.out.print("NOME CARTA: "+C.nome);
                C.StampaPotenziale();
            }
        }
        
        System.out.println("SLOT TAVOLO: ");
        
        for(Slot S : T)
        {           
            if(S.HasCard())
            {
                Carta C = S.GetCard();
                
                System.out.print("NOME CARTA: "+C.nome);
                C.StampaPotenziale();
            }
        }
    }

    public void CombinationTest()
    {       
        if(!Tv.isEmpty())
        {
            Tv.clear();
        }
        
        int V1 = P1.GetMano().get(0).GetValue();
        int V2 = P1.GetMano().get(1).GetValue();
        int V3 = P1.GetMano().get(2).GetValue();
        
        int Value1 = Math.max(V1,V2);
        Value1 = Math.max(Value1, V3);

        int Value2 = (int) Math.ceil(Value1 / 2);
        int Value3 = (int) Math.ceil(Value1 / 3);
        
        System.out.println("VALUE1: "+Value1+" VALUE2: "+Value2+" VALUE3: "+Value3);
        
        Tv.add(new Carta('d',Value1));
        
        Tv.add(new Carta('b',Value2));
        Tv.add(new Carta('c',(Value1 - Value2)));
        
        Tv.add(new Carta('b',Value3));
        Tv.add(new Carta('c',Value3));
        Tv.add(new Carta('d',(Value1 - (2*Value3))));
        
    }
    
    public void MaxCombinationGen(ArrayList<Carta> Tav)
    {
        Carta C1 = new Carta('d',10);
        Carta C2 = new Carta('s',10);
        Carta C3 = new Carta('b',10);
        
        ClearCards(Tav);
        
        P1.mano.add(0,C1);  
        P1.mano.add(1,C2);  
        P1.mano.add(2,C3);
        
        Tav.clear();
        
        Tav.add(new Carta('c',5));
        Tav.add(new Carta('d',5));
        Tav.add(new Carta('c',5));
        Tav.add(new Carta('b',5));
        Tav.add(new Carta('s',3));
        Tav.add(new Carta('d',3));
        Tav.add(new Carta('c',3));
        Tav.add(new Carta('d',1));
        Tav.add(new Carta('b',2));
        Tav.add(new Carta('s',2));
        
        Collections.sort(Tav);
    }
    
    public void AceTest(ArrayList<Carta> Tav)
    {
        Carta C1 = new Carta('d',1);
        Carta C2 = new Carta('s',1);
        Carta C3 = new Carta('b',1);
        
        ClearCards(Tav);
        
        P1.mano.add(0,C1);     
        P1.mano.add(1,C2);
        P1.mano.add(2,C3);
        
        Tav.add(new Carta('c',5));
        Tav.add(new Carta('b',5));
        Tav.add(new Carta('s',3));
        Tav.add(new Carta('s',2));
        
        Collections.sort(Tav); 
    }
    
    public void ReverseAceTest(ArrayList<Carta> Tav)
    {
        Carta C1 = new Carta('d',1);
        Carta C2 = (new Carta('b',1));
        Carta C3 = (new Carta('d',5));

        ClearCards(Tav);

        P1.mano.add(0,C1);      
        P1.mano.add(1,C2); 
        P1.mano.add(2,C3);
        
        Tav.add(new Carta('d',10));
        Tav.add(new Carta('s',1));
        Tav.add(new Carta('c',1));

        Collections.sort(Tav); 
    }
    
    public void ScopaTest(ArrayList<Carta> Tav)
    {
        Carta C1 = new Carta('d',10);
        Carta C2 = new Carta('s',7);
        Carta C3 = new Carta('b',1);
        
        ClearCards(Tav);
          
        P1.mano.add(0,C1);      
        P1.mano.add(1,C2);    
        P1.mano.add(2,C3);
        
        Tav.add(new Carta('s',10));
        Tav.add(new Carta('d',7));
     
        Collections.sort(Tav); 
    }
    
    public void TestValidità(ArrayList<Carta> Tav)
    {
        if(Attempts == 0)
        {
            if(!Tav.isEmpty())
            {
                Tav.clear();
            }

            Tav.add(new Carta('c',10));
            Tav.add(new Carta('b',10));
            Tav.add(new Carta('d',10));
            
            Attempts++;
        }
    }
    
    public void ResetAttempts()
    {
        Attempts = 0;
    }
    
    public void TestDistribuzioneMano()
    {
        int i=0;
        
        while(!Sessione.MazzoVuoto())
        {
            System.out.println("MANO NUMERO: "+i);
            
            System.out.println("CARTE NEL MAZZO: "+Sessione.Carte.size());
            
            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(TesterDebugger.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            StampaTavolo(Sessione.Tavolo);
            Sessione.ManiScoperte();
            
            Sessione.SgomberaTavolo();
            Sessione.GettaMani();
            Sessione.Distribuisci();
            
            i++;
           
        }
    }
    
    public void TestFinePartita()
    {
        TestDistribuzioneMano();
        
        Punteggio CPUPoints = Sessione.DichiaraPunteggioCPU();
        Punteggio PlayerPoints = Sessione.DichiaraPunteggioGiocatore();
        
        CPUPoints.SetCount(0,22);
        CPUPoints.SetCount(1,7);
        CPUPoints.SetCount(3,2);
        CPUPoints.SetCount(4,0);
        CPUPoints.SetCount(5,2);
        
        PlayerPoints.SetCount(0,18);
        PlayerPoints.SetCount(1,3);
        PlayerPoints.SetCount(3,4);
        PlayerPoints.SetCount(4,1);
        PlayerPoints.SetCount(5,4);
        
        
        System.out.println("PUNTEGGIO CPU\n");
        CPUPoints.ScorePrint();
        
        System.out.println("PUNTEGGIO GIOCATORE\n");
        PlayerPoints.ScorePrint();
        
        if(Sessione.MazzoVuoto())
        {
            Sessione.SgomberaTavolo();
            Sessione.GettaMani();
            Sessione.Distribuisci();
        }
    }
    
    public void ConfigurazioneCritica(ArrayList<Carta> Tav)
    {
            ClearCards(Tav);
            
            Computer.mano.add(new Carta('s',9));
            Computer.mano.add(new Carta('s',4));

            Tav.add(new Carta('c',7));
            Tav.add(new Carta('s',7));
            Tav.add(new Carta('b',5));
            Tav.add(new Carta('c',2));
            
            P1.HandDrop();
            P1.mano.add(new Carta('s',8));
            P1.mano.add(new Carta('d',7));
    }
    
    public void CalcoloPotenzialeMultiploTest(ArrayList<Carta> Tav)
    {
        ClearCards(Tav);
        
        Carta C1 = new Carta('s',8);
        Carta C2 = new Carta('s',9);
        Carta C3 = new Carta('s',10);
        
        Carta C4 = new Carta('d',8);
        Carta C5 = new Carta('d',9);
        Carta C6 = new Carta('d',10);
        
        Carta C7 = new Carta('s',5);
        Carta C8 = new Carta('c',2);
        Carta C9 = new Carta('d',2);
        Carta C10 = new Carta('b',1);
        Carta C11 = new Carta('b',1);
        Carta C12 = new Carta('d',1);
        Carta C13 = new Carta('c',8);
        Carta C14 = new Carta('c',9);
        Carta C15 = new Carta('c',10);
        
        P1.mano.add(C1);
        P1.mano.add(C2);
        P1.mano.add(C3);
        
        Computer.mano.add(C4);
        Computer.mano.add(C5);
        Computer.mano.add(C6);
        
        Tav.add(C7);
        Tav.add(C8);
        Tav.add(C9);
        Tav.add(C10);
        Tav.add(C11);
        Tav.add(C12);
    }
    
    public void PrimieraComparatorTest(ArrayList<Carta> Mazzo)
    {
        PrimieraComparator Comparator = new PrimieraComparator();
        
        Collections.sort(Mazzo,Comparator.reversed());
        
        StampaMazzo(Mazzo);
    }
    
    public void HaltTest()
    {
        Sessione.Halt();
    }
    
    public void ClearCards(ArrayList<Carta> Tav)
    {
          if(!Tav.isEmpty())
            {
                Tav.clear();
            }
            
            if(!Computer.mano.isEmpty())
            {
                Computer.mano.clear();
            }
            
            if(!P1.mano.isEmpty())
            {
                P1.mano.clear();
            }
    }
}
