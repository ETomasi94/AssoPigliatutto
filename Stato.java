package assopigliatutto;

import java.util.ArrayList;

public class Stato 
{   
    /*----CONFIGURAZIONE RELATIVA AD UNO STATO----*/
    KnowledgeBase Actual;
    
    ArrayList<Carta> Table;
    ArrayList<Carta> Hand;
    
    Gioco Game;
    
    Punteggio Score;
    
    boolean IsMax;
    
    String Label;
    
    /*----FINE CONFIGURAZIONE RELATIVA AD UNO STATO----*/
    
    /*----IMPLEMENTAZIONE DEI NODI PER L'USO DELLA RICORSIONE----*/
    Stato Parent;
    
    ArrayList<Stato> Results;
    /*----FINE IMPLEMENTAZIONE DEI NODI PER L'USO DELLA RICORSIONE----*/
    
    public Stato(String L,ArrayList<Carta> T,ArrayList<Carta> H,KnowledgeBase K,Punteggio S,boolean Q)
    {
        Actual = K;
        
        Table = T;
        
        Hand = H;
        
        Label = L;

        Score = S;
        
        IsMax = Q;
        
        Parent = null;
        
        Results = new ArrayList();
    }  
        

    public void SetKB(KnowledgeBase KB)
    {
        Actual = KB;
    }
    
    public void SetTable(ArrayList<Carta> Tv)
    {
        Table = Tv;
    }
    
    public void SetHand(ArrayList<Carta> KnownHand)
    {
        Hand = KnownHand;
    }
    
    public KnowledgeBase GetKB()
    {
        return Actual;
    }
    
    public ArrayList<Carta> GetTable()
    {
        return Table;
    }
    
    public ArrayList<Carta> GetHand()
    {
        return Hand;
    }
    
    public Stato AddResult(Stato Res) 
    {
        Res.SetParent(this);
        this.Results.add(Res);
        return Res;
    }
 
    public void AddResultsList(ArrayList<Stato> ListOfStates) 
    {
        for(Stato Res : ListOfStates)
        {
            Res.SetParent(this);
        }
            
        Results.addAll(ListOfStates);
    }

    public ArrayList<Stato> GetResults()
    {
        return Results;
    }
 
    protected void SetParent(Stato Previous) 
    {
        this.Parent = Previous;
    }
 
    public Stato GetParent() 
    {
        return Parent;
    }
    
    public void SetLabel(String L)
    {
        Label = L;
    }
    
    public String GetLabel()
    {
        return Label;
    }
    
    public void SetPunteggio(Punteggio S)
    {
        Score = S;
    }
    
    public Punteggio GetPunteggio()
    {
        return Score;
    }
    
    public void UpdateState(KnowledgeBase KB, ArrayList<Carta> Tb,ArrayList<Carta> Hnd)
    {
        Actual = KB;
        Table = Tb;
        Hand = Hnd;
    }
}
