package assopigliatutto;

import java.util.ArrayList;

public class AI 
{
    Stato DecisionTree;
    
    CPU ProcessingUnit;
    
    ArrayList<Carta> Table;
    ArrayList<Carta> Hand;
    ArrayList<Carta> Opponent;
    
    KnowledgeBase KB;
    Punteggio Score;
    
    boolean IsMax;
    
    public AI(CPU C)
    {
        ProcessingUnit = C;
    }
    
    public void BuildTree(Stato State,int depth)
    { 
       if(depth < 0)
       {
           throw new IllegalArgumentException();
       }
       
       if(depth == 0)
       {
           State.Results = null;
       }
       
       if(depth > 0)
       {
           for(int i=0; i<depth; i++)
           {
               State.GeneraSuccessore();
               
               System.out.println("SIZE: "+SizeOfTree(State));
               
               for(Stato S : State.Results)
               {
                   S.GeneraSuccessore();
               }
           }
       }
       
       DecisionTree = State;
           
       PrintAlbero();
    }
    
    /*------METODI DI VALUTAZIONE E CALCOLO GUADAGNO-------*/
    
    public void FunzioneDiValutazione(Stato S,Carta C,int Scelta)
    {   
    }
    
    public int MinGain(Carta C)
    {
        double Min = C.ValoriPotenziale.get(1);
        
        int Index = 1;
        
        if(C.IsMarked())
        {
            for(int i=1; i<C.ValoriPotenziale.size();i++)
            {
                double Current = C.ValoriPotenziale.get(i);

                if(Current < Min)
                {
                    Min = Current;
                    Index = i;

                }
            }
        } 
        return Index;
    }
    
    public int MaxGain(Carta C)
    {
        double Max = 0.0;
        
        int Index = 1;
        
        if(C.IsMarked())
        {
            for(int i=1; i<C.ValoriPotenziale.size();i++)
            {
                double Current = C.ValoriPotenziale.get(i);

                if(Current > Max)
                {
                    Max = Current;
                    Index = i;

                }
            }
        }     
        return Index;
    }
    
    public void WeightSortTest(ArrayList<Carta> Carte,Punteggio Score)
    {
        for(Carta C : Carte)
        {
            System.out.println("CARTA: "+C.nome+" PESO: "+C.Weight);
        }
    }
    
    public void PrintGuadagno(Carta C)
    {
        System.out.println("CARTA IN ESAME: "+C.nome);
        
        if(C.IsMarked())
        {     
            for(int index=1; index<=C.Potenziale.size(); index++)
            {
                    System.out.println("CON IL POTENZIALE: "+index+" GUADAGNAMO: "+C.ValoriPotenziale.get(index));
                    System.out.println("CARTE CHE VERRANNO PRESE:");
                    C.StampaSelezione(index);
                    System.out.println("\n\n");
            }
            
            int MAX = MaxGain(C);
            int MIN = MinGain(C);
        
            System.out.println("CARTA: "+C.nome+" IL MASSIMO GUADAGNO E' IN POSIZIONE: "+MAX+" E IL VALORE E': "+C.ValoriPotenziale.get(MAX)+"\n");
            System.out.println("CARTA: "+C.nome+" IL MINIMO GUADAGNO E' IN POSIZIONE: "+MIN+" E IL VALORE E': "+C.ValoriPotenziale.get(MIN)+"\n");
        }
        else
        {
            System.out.println("CON QUESTA CARTA NON C'E' ALCUN GUADAGNO");
            System.out.println("\n\n");
        }
    }  
    /*------FINE METODI DI VALUTAZIONE E CALCOLO GUADAGNO-------*/
    
    /*-----METODI DI PREDIZIONE ED INFERENZA------*/
    
    private boolean Turn()
    {
        return ProcessingUnit.turn;
    }

    public void UpdateTable(ArrayList<Carta> T)
    {
        Table.clear();
        Table.addAll(T);
    }
    
    public void UpdateHand(ArrayList<Carta> H)
    {
        Hand.clear();   
        Hand.addAll(H);
    }
    
    public void UpdateKBandOpponent(KnowledgeBase K)
    {
        K.CopyKB(KB);
        Opponent = KB.Carte;
    }
    
    public void UpdateScore(Punteggio Sc)
    {
        Sc.CopyScore(Score); 
    }
    
    /*-----FINE METODI DI PREDIZIONE ED INFERENZA------*/
    
    /*-------METODI DI STAMPA E DEBUG-------*/
    
    public String DeclarePlayer(boolean player)
    {
        if(player == true)
        {
            return "CPU";
        }
        else
        {
            return "PLAYER";
        }
    }
    
    private int SizeOfTree(Stato S)
    {
        int sum = 0;

        if(S == null)
        {
            return 0;
        }

        int Count = 0;
        Count++;
        
        if(!S.Results.isEmpty())
        {
          for(Stato S1 : S.Results)
          {
            Count += SizeOfTree(S1);
        }
        }
        
        return Count;
    }
    
    public void PrintAlbero()
    {
        Stato S = DecisionTree;
        
        if(S == null)
        {
            return;
        }
       
        PrintStato(S);    

        System.out.println("STATI DELL'ALBERO: "+SizeOfTree(DecisionTree));
        
    }
    
    public void PrintStato(Stato S)
    {        
        if(S == null)
        {
            return;
        }
        
        System.out.println("---------STATO: "+S.Label+"---------");
        
        System.out.println("TURNO: "+DeclarePlayer(S.IsMax)+"\n");

        //System.out.println("KNOWLEDGE BASE:");
        
        //S.Actual.KBPrint();
        
        System.out.print("TABLE: ");

        if(!S.Table.isEmpty())
        {
            for(Carta C : S.Table)
            {
                System.out.print("| "+C.nome+" |");
            }
        }
        else
        {
            System.out.println("TABLE IS EMPTY!");
        }
        
        System.out.println("\n");
        System.out.print("CPU HAND: ");
        
        if(!S.Hand.isEmpty())
        {
            for(Carta C : S.Hand)
            {
                System.out.print("@ "+C.nome+" @ "+"NUMERO POTENZIALI: "+C.Potenziale.size()+" ");
            }
        }
        else
        {
            System.out.println("HAND IS EMPTY!");
        }
        
        System.out.println("\n");

        System.out.println("GUADAGNO: "+S.Gain);
        
        System.out.println("--------------------------------------------------\n");
        
        if(!S.Results.isEmpty())
        {
            for(Stato S1: S.Results)
            {
                PrintStato(S1);
            }
        }
    }
    
     /*-------FINE METODI DI STAMPA E DEBUG-------*/
    
}
