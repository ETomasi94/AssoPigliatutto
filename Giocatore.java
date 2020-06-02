/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assopigliatutto;

import java.util.ArrayList;

/**
 *
 * @author Enrico Tomasi
 */
public class Giocatore extends Thread
{
    String nome;
    Punteggio points;
    
    boolean mazziere;
    
    boolean turn;
    
    ArrayList<Carta> mano;
    
    public ArrayList<Carta> Table;
    
    public Giocatore(String n,boolean decision)
    {
        nome = n;
        points = new Punteggio();
        mazziere = decision;
        mano = new ArrayList(3);
    }
    
    public void PickCard(ArrayList<Carta> Mazzo)
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
    
    public synchronized void GetTotalPotential(ArrayList<Carta> Tavolo)
    {
        for(Carta c : mano)
        {
            c.CalcolaPotenziale(Tavolo);
        }
    }
    
    public synchronized void PlayCard(Carta card,int combinazione,ArrayList<Carta> Tavolo)
    {
        try
        {
            Tavolo.add(card);
            mano.remove(card);
        }
        catch(IndexOutOfBoundsException e)
        {
            
        }
    }
    
    public void Scopa()
    {
        points.CallScopa();
    }
    
    public synchronized void Score(Carta card)
    {
        points.AddCard(card);
    }
    
    //DEBUG
    public void VisualizzaMano()
    {
        System.out.println("CARTE POSSEDUTE DA "+nome+":\n");
        
        for(Carta c : mano)
        {
            System.out.println(c.GetName());
        }
    }
    
    public synchronized boolean YourTurn()
    {
        return turn;
    }
    
    public void AssignTable(ArrayList<Carta> Tv)
    {
        Table = Tv;
    }
    
    public void AssegnaTurnoInizio(boolean a)
    {
        turn = a;
    }
    
    public void CambioTurno()
    {
        turn = !turn;
    }
    
    public void HandDrop()
    {
        mano.clear();
    }
    
    public void Reset()
    {
        if(!mano.isEmpty())
        {
            mano.clear();
        }
        
        points.ResetScore();
    }
    
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
    
    @Override
    public void run()
    {
        while(true)
        {
            if(YourTurn())
            {
                GetTotalPotential(Table);
            }
                
        }
    }
   
}
