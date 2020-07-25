package assopigliatutto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class CPU extends Giocatore
{
    Gioco Sessione;
    
    Random rnd = new Random();
    
    KnowledgeBase KB;
    
    AI Intelligence;

    public CPU(String n, boolean decision, Gioco g, ArrayList<Carta> Tavolo) 
    {
        super(n, decision, Tavolo);
        
        Sessione = g;
        
        KB = new KnowledgeBase(this); 
        
        Intelligence = new AI(this);
    }
    
    public synchronized void GiocaACaso()
    {
        if(!mano.isEmpty())
        {
            try 
            {
                Thread.sleep(750);
            } 
            catch (InterruptedException ex) 
            {
                Sessione.Halt();
            }
            
            int i = rnd.nextInt(mano.size());
            
            int j = 0;
            
            Carta card = mano.get(i);

            if(card.IsMarked())
            {
                j = 1 + rnd.nextInt(card.Potenziale.size());
                Sessione.GiocaCarta(this,j,card);
            }
            else
            {
                Sessione.GiocaCarta(this,0,card);
            }
            
            System.out.println("CPU ha giocato: "+card.GetName()+"\n");
            
            if(card.IsMarked())
            {
                System.out.println(" Ed ha preso: ");
                
                System.out.print("\t");
                card.StampaSelezione(j);
                System.out.println("\n");
            }
        }
        else
        {
            Sessione.CambioTurno();
        }
    }

    public synchronized void Think()
    {
        
    }
    
    public synchronized Stato Observe()
    {
        Stato S = new Stato("ATTUALE",Table,mano,KB,points,turn);
        
        return S;
    }
    
    public synchronized void Plan(Stato S)
    {
        if(!mano.isEmpty())
        {
            Intelligence.BuildTree(S,2);
        }
    }
    
    public synchronized void Act()
    {
        
    }
    
    @Override
    public synchronized void PickHand(ArrayList<Carta> Mazzo)
    {
        Carta C;
        
        for(int i=0; i<3; i++)
        {
            C = Mazzo.get(1);
            mano.add(C);
            Mazzo.remove(1);  
        }
    }
    
    public void AggiornaKB(ArrayList<Carta> Mazzo,ArrayList<Carta> ManoGiocatore)
    {
        KB.Aggiorna(Mazzo, ManoGiocatore);
    }
    
    @Override
    public void run()
    {
        while(active)
        {
            if(YourTurn())
            {
                try
                {
                    Thread.sleep(1000);
                    Stato S = Observe();
                    Plan(S);
                    GiocaACaso();
                }
                catch(InterruptedException e)
                {
                    System.out.println("LA CPU E' STATA INTERROTTA DURANTE LA SUA ESECUZIONE");
                }
            }
        }
        
        System.out.println("CPU TERMINA ESECUZIONE\n");
    }
    
}
