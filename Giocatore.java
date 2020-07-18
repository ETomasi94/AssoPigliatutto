package assopigliatutto;

import java.util.ArrayList;

public class Giocatore extends Thread
{
    /*--------VARIABILI D'ISTANZA----------*/
    
    String nome;
    
    public Punteggio points;
    
    boolean mazziere;
    
    boolean turn;
    
    boolean active;
    
    boolean hastakenlast;
    
    boolean turnstart;
    
    ArrayList<Carta> mano;
    
    public ArrayList<Carta> Table;
    
    /*-------------------------------------------*/
    
    /*-------METODO COSTRUTTORE--*/
    
    public Giocatore(String n,boolean decision, ArrayList<Carta> Tavolo)
    {
        nome = n;
        points = new Punteggio();
        mazziere = decision;
        mano = new ArrayList(3);
        
        active = true;
        hastakenlast = false;
        
        Table = Tavolo;
    }
    
    /*---------------------------*/
    
    /*-------METODI DI GIOCO-----*/
    
    public synchronized void PickCard(ArrayList<Carta> Mazzo)
    {
        Carta C;
        
        C = Mazzo.get(1);
        mano.add(C);
        Mazzo.remove(1); 
    }
    
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
    
    public synchronized void GetTotalPotential(ArrayList<Carta> Tav)
    {
            for(Carta c : mano)
            {
                c.CalcolaPotenziale(Tav,points);
            }
    }
    
    public synchronized void Scopa()
    {
        points.CallScopa();
    }
    
    public synchronized void Score(Carta card)
    {
        points.AddCard(card);
    }
    
    /*----------------------*/
    
    /*-------METODI DI DEBUG----*/
    
    public synchronized void VisualizzaMano()
    {
        System.out.println("CARTE POSSEDUTE DA "+nome+":\n");
        
        for(Carta c : mano)
        {
            System.out.println(c.GetName());
        }
    }
    
    public synchronized void HandDrop()
    {
        mano.clear();
    }
    
    public synchronized void Reset()
    {
        if(!mano.isEmpty())
        {
            mano.clear();
        }
        
        points.ResetScore();
    }
    
    /*-------FINE METODI DI DEBUG----*/
    
    /*------------METODI GETTERS E SETTERS-------------------*/
    
    public int GetHandCards()
    {
        return mano.size();
    }
            
    
    public String GetName()
    {
        return this.nome;
    }
    
    public Punteggio GetPoints()
    {
        return this.points;
    }
    
    public ArrayList<Carta> GetMano()
    {
        return this.mano;
    }
    
    public void SetMazziere(boolean M)
    {
        this.mazziere = M;
    }  
    
    public boolean IsActive()
    {
        return active;
    }
    
    public synchronized boolean YourTurn()
    {
        return turn;
    }
    
      public synchronized void AssignTable(ArrayList<Carta> Tv)
    {
        Table = Tv;
    }
    
    public synchronized void AssegnaTurno(boolean a)
    {
        turn = a;
    }
    
    public synchronized void CambioTurno()
    {
        turn = !turn;
        
        turnstart = turn;
    }
    
    public void SetTaken()
    {
        hastakenlast = true;
    }
    
    public void UnsetTaken()
    {
        hastakenlast = false;
    }
    
    public boolean GetLastToTake()
    {
        return hastakenlast;
    }
    
    /*-------FINE METODI GETTERS E SETTERS-----------*/
    
    /*------METODI CICLO DI VITA DEL THREAD---------*/
    
    public void ShutDown()
    {
        active = false;
    }
    
    @Override
    public void run()
    {
        while(active)
        {    
            if(YourTurn())
            {
            }
        }
        
        System.out.println("GIOCATORE TERMINA ESECUZIONE\n");
    }
    
    /*------FINE METODI CICLO DI VITA DEL THREAD---------*/
   
}
