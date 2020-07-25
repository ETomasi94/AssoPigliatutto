package assopigliatutto;

import java.util.ArrayList;

public class Stato 
{   
    /*----CONFIGURAZIONE RELATIVA AD UNO STATO----*/
    KnowledgeBase Actual;
    
    ArrayList<Carta> Table;
    ArrayList<Carta> Hand;
    
    ArrayList<Carta> Opponent;
    
    Gioco Game;
    
    Punteggio Score;
    
    boolean IsMax;
    
    String Label;
    
    double Gain;
    
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
        
        Gain = 0;
        
        Results = new ArrayList();
    }  
    
    public void GeneraSuccessore()
    {
        boolean turn = !IsMax;

        if(!turn)//CASO MAX (TURNO CPU)
        {
            for(Carta C : Hand)
            {
                if(C.IsMarked() && !Table.isEmpty())
                {                
                    for(int Sc = 1; Sc <=C.Potenziale.size(); Sc++)
                    {
                       if(C.HasPotential(Sc))
                       {
                            Stato S1;
                            S1 = new Stato(Label+"-> "+C.nome+"/"+Sc,FT(Table,C,Sc),FH(Hand,C),FKB(Actual,C),FS(Score,C),turn);
                            S1.Gain = C.ValoriPotenziale.get(Sc);
                            
                            Results.add(S1);
                       }
                    }
                }
                else
                {
                    Stato S1;
                    S1 = new Stato(Label+"-> "+C.nome+"/"+0,FT(Table,C,1),FH(Hand,C),FKB(Actual,C),FS(Score,C),turn); 
                    S1.Gain = 0.0;
                    
                    
                    Results.add(S1);
                }
            }
        }
        else//CASO MIN (TURNO GIOCATORE)
        {
            GetOpponent();
            for(Carta C : Opponent)
            {
                if(C.IsMarked() && !Table.isEmpty())
                {                
                    for(int Sc = 1; Sc <=C.Potenziale.size(); Sc++)
                    {
                       if(C.HasPotential(Sc))
                       {
                           Stato S1;
                            S1 = new Stato(Label+"-> "+C.nome+"/"+Sc,FT(Table,C,Sc),FH(Opponent,C),FKB(Actual,C),FS(Score,C),turn);
                            S1.Gain = C.ValoriPotenziale.get(Sc);
                            
                            Results.add(S1);

                       }

                    }
                }
                else
                {
                    Stato S1;
                    
                    S1 = new Stato(Label+"-> "+C.nome+"/"+0,FT(Table,C,1),FH(Opponent,C),FKB(Actual,C),FS(Score,C),turn); 
                    S1.Gain = 0.0;
                    
                    Results.add(S1);
                }
            }
        }

    }
       
    public ArrayList<Carta> FT(ArrayList<Carta> T,Carta C,int Scelta)
    {
        if(C.HasPotential(Scelta))
        {
            ArrayList<Carta> TB = new ArrayList();
        
            TB.addAll(T);
        
            for(Carta C1 : C.Potenziale.get(Scelta))
            {
                TB.remove(C1);
            }
            
            return TB;
        }
        else
        {
            ArrayList<Carta> TB = new ArrayList();
            
            TB.addAll(T);
            TB.add(C);
            
            return TB;
        }
    }
        
    public ArrayList<Carta> FH(ArrayList<Carta> H,Carta C)
    {
        ArrayList<Carta> HND = new ArrayList();
        
        HND.addAll(H);
        
        HND.remove(C);

        return HND;
    }
    
    public KnowledgeBase FKB(KnowledgeBase K,Carta C)
    {
        KnowledgeBase KB1 = K;
        
        KB1.RimuoviCarta(C);
        
        return KB1;
    }
    
    public Punteggio FS(Punteggio S,Carta C)
    {
        Punteggio SCR = S;
        
        S.AddCard(C);
        
        return SCR;
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
    
    public ArrayList<Carta> GetOpponent()
    {
        Opponent = Actual.GetMostValuableCards(Table,Score);
        return Opponent;
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
    
    public void ResetGain()
    {
        Gain = 0.0;
    }
    
    public void SetGain(Double D)
    {
        Gain = D;
    }
    
    public double GeiGain()
    {
        return Gain;
    }
    
    public void UpdateState(KnowledgeBase KB, ArrayList<Carta> Tb,ArrayList<Carta> Hnd)
    {
        Actual = KB;
        Table = Tb;
        Hand = Hnd;
    }
}
