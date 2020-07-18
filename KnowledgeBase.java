package assopigliatutto;

import java.util.ArrayList;

public class KnowledgeBase 
{
    ArrayList<Carta> Carte;
    ArrayList<Integer> Valori;
    
    int Totale;
    
    int Residue;
    int Coppe;
    int Bastoni;
    int Spade;
    int Denari;
    
    boolean Settebello;
    
    public KnowledgeBase()
    {
        Inizializza();
    }
    
     public void Inizializza()
    {    
        Valori = new ArrayList();
        
        for(int i=0; i<10; i++)
        {
            Valori.add(i,4);
        }
        
        Residue = 40;
        
        Coppe = 10;
        Bastoni = 10;
        Spade = 10;
        Denari = 10;
        
        Settebello =  true;   
        
    }
     
     public void Aggiorna(ArrayList<Carta> Mazzo, ArrayList<Carta> ManoGiocatore)
     {
         ArrayList<Carta> KnowledgeArray = new ArrayList();
         
         if(!Mazzo.isEmpty())
         {
            KnowledgeArray.addAll(Mazzo);
         }
         
         if(!ManoGiocatore.isEmpty())
         {
            KnowledgeArray.addAll(ManoGiocatore);
         }
         
         Carte = KnowledgeArray;
         
         AggiornaStatisticheCarte();
     }
    
     /*----METODI DI CALCOLO DELLE PROBABILITA'----*/
     
    public float TotalProbability()
    {      
        float Probability =(float) ((float) 1.0/(Residue*1.0));

        return Probability;
    }
    
    public float SeedProbability(String seme)
    {
        float Probability = 0;
        
        switch(seme)
        {
            case("bastoni"):
                Probability = (float) ((Bastoni * (1.0)) / (Residue * (1.0)));
                break;
            case("coppe"):
                Probability = (float) ((Coppe* (1.0)) / (Residue * (1.0)));
                break;
            case("denari"):
                Probability = (float) ((Denari* (1.0)) / (Residue * (1.0)));
                break;
            case("spade"):
                Probability = (float) ((Spade* (1.0)) / (Residue * (1.0)));
                break;
            default:
                Probability = 0;
                break;
        }
        
        return Probability;
    }
    
    public float ValueProbability(int valore)
    {
        float Probability = (float) ((float) (Valori.get(valore-1)*1.0) / (Residue * 1.0));
        
        return Probability;
    }
    
    public void ContaValore(int v)
    {
        int a = 0;
        
        for(Carta C : Carte)
        {
            if(C.valore == v)
            {
                a++;
            }
        }
        
        Valori.add((v-1),a);
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
        //Con il metodo contains applicato ad un nuovo settebello non funzionava
        for(Carta C : CRDS)
        {
            if(C.seme.equals("denari") && C.valore == 7)
            {
                return true;
            }
        }
        
        return false;
    }
    
    public void AggiornaStatisticheCarte()
    {
        Residue = Carte.size();
        Coppe = ContaCoppe(Carte);
        Bastoni = ContaBastoni(Carte);
        Denari = ContaDenari(Carte);
        Spade = ContaSpade(Carte);
        Settebello = CercaSetteBello(Carte);
        
        for(int v=1; v<=10; v++)
        {
            ContaValore(v);
        }
    }
    
    /*----FINE METODI DI CALCOLO DELLE PROBABILITA'----*/
    
    /*----METODI DI VERIFICA----*/
    
    public boolean IsDenari(Carta C)
    {
        return C.GetSeme().equals("denari");
    }
    
    public boolean IsSetteBello(Carta C)
    {
        return ((IsDenari(C)) && (C.GetValue() ==  7));
    }
    
    /*----FINE METODI DI VERIFICA----*/
    
    /*----METODI GETTERS E SETTERS----*/
    
    public int GetValueCount(int i)
    {
        return Valori.get(i-1);
    }
    
    public int GetResidual()
    {
        return Residue;
    }
    
    public void SetResidual(int x)
    {
        Residue = x;
    }
    
    public void SetValueCount(int x,int c)
    {
        Valori.set(x-1,c);
    }
    
    public void SetBastoni(int x)
    {
        Bastoni = x;
    }
    
    public void SetCoppe(int x)
    {
        Coppe = x;
    }
    
    public void SetSpade(int x)
    {
        Spade = x;
    }
    
    public void SetDenari(int x)
    {
        Denari = x;
    }
    
    public void ConfermaSettebello(boolean b)
    {
        Settebello = b;
    }
    
    public void CopyKB(KnowledgeBase Dest)
    {
        Dest.Carte = Carte;
        Dest.Valori = Valori;

        Dest.Totale = Totale;

        Dest.Residue = Residue;
        Dest.Coppe = Coppe;
        Dest.Bastoni = Bastoni;
        Dest.Spade = Spade;
        Dest.Denari = Denari;

        Dest.Settebello = Settebello;
    }
    
    /*----FINE METODI GETTERS E SETTERS----*/
    
    /*----METODI DI STAMPA E DI DEBUG----*/
    
    public synchronized void KBPrint()
    {
        PrintValues();
        
        System.out.println("INOLTRE SO CHE: ");
        
        System.out.println("--RIMANGONO : "+Residue+" CARTE, DI CUI");
        System.out.println("----"+Bastoni+" DI BASTONI");
        System.out.println("----"+Coppe+" DI COPPE");
        System.out.println("----"+Spade+" DI SPADE");
        System.out.println("----"+Denari+" DI DENARI");
        System.out.println("---- E IL SETTEBELLO: "+Settebello);
        System.out.println("----PROBABILITA' DI PESCARE UNA GENERICA CARTA: "+TotalProbability());
    }
    
    public void PrintValues()
    {
        System.out.println("CPU: SO CHE, ORDINATE PER VALORE, SONO POSSIBILI ANCORA TOT CARTE");
        
        for(int i=0;i<10;i++)
        {
            System.out.println("----VALORE: "+(i+1)+" | RESIDUE: "+Valori.get(i)+" | PROBABILITA': "+ValueProbability(Valori.get(i)));
        }
    }
    
    /*----FINE METODI DI STAMPA E DI DEBUG----*/
}
