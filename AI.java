package assopigliatutto;

import java.util.ArrayList;

public class AI 
{
    AlberoDiDecisione DecisionTree;
    
    CPU ProcessingUnit;
    
    ArrayList<Carta> Table;
    ArrayList<Carta> Hand;
    
    KnowledgeBase KB;
    Punteggio Score;
    
    boolean IsMax;
    
    public AI(CPU C)
    {
        ProcessingUnit = C;
        Table = C.Table;
        Hand = C.mano;
        KB = C.KB;
        Score = C.points;
    }
    
    public void BuildTree()
    {
        IsMax = ProcessingUnit.turn;
        
        Stato S = new Stato("ATTUALE",Table,Hand,KB,Score,IsMax);

        DecisionTree = new AlberoDiDecisione(S);
           
        FunzioneDiValutazione(S);
    }
    
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
    
    public void FunzioneDiValutazione(Stato S)
    {   
        for(Carta C : S.Hand)
        {
            PrintGuadagno(C);
        }
    }
    
    public void PrintGuadagno(Carta C)
    {
        System.out.println("CARTA IN ESAME: "+C.nome);
        
        if(C.IsMarked())
        {     
            for(int index=1; index<C.Potenziale.size(); index++)
            {
                System.out.println("CON IL POTENZIALE: "+index+" GUADAGNAMO: "+C.ValoriPotenziale.get(index));
                System.out.println("CARTE CHE VERRANNO PRESE:");
                C.StampaSelezione(index);
                System.out.println("\n\n");
            }
        }
        else
        {
            System.out.println("CON QUESTA CARTA NON C'E' ALCUN GUADAGNO");
            System.out.println("\n\n");
        }
    }
    
    public Double Pesa(Carta Card,Punteggio Score,boolean IsMax)
    {
        //Costanti perché sempre graditi, aldilà del punteggio corrente
        double PesoDelSettebello = 1;
        
        /*Contribuiscono, in base al fatto che sia necessario o meno, a decidere
          se vale la pena di prendere questa carta*/
        double PunteggioCarta = 0.3333;
        double PesoDeiDenari = 0.3333;
        double PesoInPrimiera = 0.3333;
        
        double Probability = 0.0;
        
        double WeightedValue = 0.0;

        if(Card.IsSettebello())
        {
            System.out.println("ABBIAMO IL SETTEBELLO, 1 PUNTO");
            WeightedValue += PesoDelSettebello;
        }

        if(Score.MaxPrimiera(Card))
        {
            WeightedValue += PesoInPrimiera;
        }

        /*Se ho una carta di denari e non ho ancora la certezza di avere
        più carte di denari dell'avversario, allora la carta vale di più*/
        if(Card.seme.equals("denari") && Score.GetDenari() <= 5)
        {
            System.out.println("SERVONO ALTRI DENARI, 0.33 PUNTI");
            WeightedValue += PesoDeiDenari;
        }

         /*Se non ho ancora più carte dell'avversario, allora la carta vale di più*/
        if(Score.GetTotal() <= 20)
        {
            System.out.println("SERVONO ALTRE CARTE, 0.33 PUNTI");
            WeightedValue += PunteggioCarta;
        }

        //Punteggio Standard associato ad ogni carta
        WeightedValue += 1.0;

        System.out.println("LA CARTA: "+Card.nome+" VALE: "+WeightedValue+" IN PESO");


        return WeightedValue*Probability;
    }

    public void PrintAlbero()
    {
        DecisionTree.StampaAlbero();
    }
    
    public void PrintStato(Stato S)
    {        
        if(S == null)
        {
            return;
        }
        
        System.out.println("STATO: "+S.Label);
        
        System.out.println("KNOWLEDGE BASE:");
        
        S.Actual.KBPrint();
        
        System.out.println("TABLE");
        System.out.println("\n");
        
        for(Carta C : S.Table)
        {
            System.out.print("| "+C.nome+" |");
        }
        
        System.out.println("\n");
        System.out.println("CPU HAND:");
        
        for(Carta C : S.Hand)
        {
            System.out.println("@"+C.nome+"@");
        }
        
        System.out.println("I RISULTATO SONO VUOTI?: "+S.Results.isEmpty());

    }
    
}
