
package assopigliatutto;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

public class Carta implements Comparable<Carta>
{
    String seme;
    String nome;
    
    Slot position;
    
    int valore;
    int codice;

    ConcurrentSkipListMap<Integer,ArrayList<Carta>> Potenziale;
    ConcurrentSkipListMap<Integer,Double> ValoriPotenziale;
    
    public Carta(char s,int v)
    {
        valore = v;
        
        nome = "";
        
        codice = valore;
        
        Potenziale = new ConcurrentSkipListMap();
        ValoriPotenziale = new ConcurrentSkipListMap();
        
        switch(v)
        {
            case 1:
                nome += "Asso di ";
                break;
            case 2:
                nome += "Due di ";
                break;
            case 3:
                nome += "Tre di ";
                break;
            case 4:
                nome += "Quattro di ";
                break;
            case 5:
                nome += "Cinque di ";
                break;
            case 6:
                nome += "Sei di ";
                break;
            case 7:
                nome += "Sette di ";
                break;
            case 8:
                nome += "Fante di ";
                break;
            case 9:
                nome += "Cavallo di ";
                break;
            case 10:
                nome += "Re di ";
                break;
            default:
                throw new IllegalArgumentException();
        }
        
        switch(s)
        {
            case 's':
                seme = "spade";
                codice += 30;
                break;
            case 'b':
                seme = "bastoni";
                codice += 20;
                break;
            case 'c':
                seme = "coppe";
                codice += 0;
                break;
            case 'd':
                seme = "denari";
                codice += 10;
                break;
            default:
                throw new IllegalArgumentException();
        }
   
        nome += seme;
    }
    
    public void AssegnaSlot(Slot S)
    {
        position = S;
    }
    
    public void RimuoviDaSlot()
    {
        if(position != null)
        {
            position = null;
        }
    }
 
    public boolean IsMarked()
    {
        if((Potenziale != null))
        {
            return !(Potenziale.isEmpty());
        }
        else
        {
            return false;
        }
    }
    
    public void AggiungiPotenziale(int marchio, Carta c)
    {
       ArrayList Scelte; 
        
       if(Potenziale.containsKey(marchio))
       {
            Scelte = Potenziale.get(marchio);
            
            if(Scelte.contains(c))
            {
                return;
            }
       }
       else
       {
           Scelte = new ArrayList();
           Potenziale.put(marchio, Scelte);
       }
        
        Scelte.add(c);
    }
    
    public void ResettaPotenziale()
    {
        Potenziale.clear();
    }
    
    public synchronized void CalcolaPotenziale(ArrayList<Carta> Tavolo,Punteggio Score)
    {
        /*Dato che viene continuamente aggiornato, il potenziale viene ricalcolato
          ogni volta
        */
        this.ResettaPotenziale();

        if(!Tavolo.isEmpty())
        {
            if(!IsAnAce())
            {
                int index = 1;
                
                boolean UgualePotenziale = false;
                
                for(Carta C : Tavolo)
                {
                    if(C.GetValue() == valore)
                    {
                        AggiungiPotenziale(index,C);
                        
                        index++;
                        
                        UgualePotenziale = true;
                    }
                }
                
                if(!UgualePotenziale)
                {
                    PotentialCalculating(Tavolo,0,0,Score);
                }
      
            }
            else
            {
              int index = 1;
             
              boolean response = false;

              for(Carta C : Tavolo)
               {
                  if(C.IsAnAce() && !response)
                  {
                        response = true;

                        ResettaPotenziale();

                        AggiungiPotenziale(index,C);
                        
                        index++;
                  }

                  AggiungiPotenziale(1,C);
               }
            }
        }
    }
    
    public Double Pesa(Carta Card,Punteggio Score)
    {
        //Costanti perché sempre graditi, aldilà del punteggio corrente
        double PesoDelSettebello = 1;
        double PesoDelleScope = 1;
        
        /*Contribuiscono, in base al fatto che sia necessario o meno, a decidere
          se vale la pena di prendere questa carta*/
        double PunteggioCarta = 0.3333;
        double PesoDeiDenari = 0.3333;
        double PesoInPrimiera = 0.3333;
        
        double Probability = 0.0;
        
        double WeightedValue = 0.0;
        double Result = 0.0;

        if(Card.IsSettebello())
        {
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
            WeightedValue += PesoDeiDenari;
        }

         /*Se non ho ancora più carte dell'avversario, allora la carta vale di più*/
        if(Score.GetTotal() <= 20)
        {
            WeightedValue += PunteggioCarta;
        }

        //Punteggio Standard associato ad ogni carta
        WeightedValue += 1.0;

        Result += WeightedValue;
 
        return Result;
    }
    
     public Double Pesa(ArrayList<Carta> CardList,Punteggio Score)
    {
        //Costanti perché sempre graditi, aldilà del punteggio corrente
        double PesoDelSettebello = 1;
        
        /*Contribuiscono, in base al fatto che sia necessario o meno, a decidere
          se vale la pena di prendere questa carta*/
        double PunteggioCarta = 0.3333;
        double PesoDeiDenari = 0.3333;
        double PesoInPrimiera = 0.3333;

        
        double WeightedValue = 0.0;
        double Result = 0.0;

        for(Carta Card : CardList)
        {
            if(Card.IsSettebello())
            {
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
                WeightedValue += PesoDeiDenari;
            }

             /*Se non ho ancora più carte dell'avversario, allora la carta vale di più*/
            if(Score.GetTotal() <= 20)
            {
                WeightedValue += PunteggioCarta;
            }

            //Punteggio Standard associato ad ogni carta
            WeightedValue += 1.0;

            Result += WeightedValue;
        }


        return Result;
    }
    
    public void PotentialCalculating(ArrayList<Carta> Cards,int start,int FirstIndex,Punteggio Score)
    {        
        ArrayList<ArrayList<Carta>> Result = new ArrayList();
        ArrayList<Carta> Temp = new ArrayList();
        ArrayList<Double> Weights = new ArrayList();
        
        int FinalValue = valore;

        Evaluate(Cards,start,FinalValue,Temp,Result,Score,0,Weights);

        for(int i=0;i<Result.size();i++)
        {
            if(!Potenziale.containsValue(Result.get(i)))
            {              
                Potenziale.put(i+1,Result.get(i));
                ValoriPotenziale.put(i+1,Weights.get(i));
            }
        }
    }
    
    public void Evaluate(ArrayList<Carta> Cards,int start,int Target,ArrayList<Carta> Temp,ArrayList<ArrayList<Carta>> Result,Punteggio Score,double InitialWeight,ArrayList<Double> Weights)
    {
        Double Weight = InitialWeight;
        
          if(Target==0)
          {             
            Result.add(new ArrayList<Carta>(Temp));
            Weights.add(Weight);
            return;
          }
          
        if(Target<0)
        {
            return;
        }
 
        Carta Previous = null;
        
        for(int i=start; i<Cards.size(); i++)
        {
            if(Previous== null || !Previous.equals(Cards.get(i)))
            { 
                Temp.add(Cards.get(i));//Il calcolo inizia ogni volta da un elemento diverso
                
                int value = Cards.get(i).GetValue();
                
                double peso = Pesa(Cards.get(i),Score);
                
                Weight += peso;

                Evaluate(Cards,i+1, Target-value,Temp,Result,Score,Weight,Weights);
                
                peso = Pesa(Temp.get(Temp.size() - 1),Score);
                
                Weight -= peso;
                
                Temp.remove(Temp.size()-1);
                
                Previous=Cards.get(i);
            }
        }
    }
      
    public boolean IsAnAce()
    {
        return (this.GetValue() == 1);
    }
    
    public boolean HasPotential(int M)
    {
        return Potenziale.containsKey(M);
    }
    
    public void StampaSelezione(int M)
    {
        for(Carta C : Potenziale.get(M))
        {
            System.out.print("|"+C.nome+"| ");
        }
    }
    
    public void RimuoviPotenziale(int M)
    {
        Potenziale.remove(M);
    }

    public void StampaPotenziale()
    {
        System.out.println("----"+this.nome+"----\n");        
        
       if(!Potenziale.isEmpty())
       {
            Set<Entry<Integer,ArrayList<Carta>>> Es = Potenziale.entrySet();
       
            for(Entry<Integer,ArrayList<Carta>> E : Es)
            {
                System.out.println(" -OPZIONE "+E.getKey()+"");

                for(Carta c : E.getValue())
                {
                    System.out.println(" | "+c.GetName()+" |");
                }
            }
       }
       else
       {
           System.out.println("-NESSUNA CARTA O COMBINAZIONE DI CARTE ASSOCIATA-\n");
       }
    }
    
    public boolean OnSlot()
    {
        return !(position == null);
    }
    
    public String GetSeme()
    {
        return this.seme;
    }
    
    public String GetName()
    {
        return this.nome;
    }
    
    public int GetValue()
    {
        return this.valore;
    }
    
    public int GetCodice()
    {
        return this.codice;
    }
    
    public Slot GetSlot()
    {
        return position;
    }
    
    public boolean IsSettebello()
    {
        return(seme.equals("denari") && (valore==7));
    }
    
     public int GetPrimieraValue()
    {
        int res;
        
        switch(valore)
        {
            case 1:
                res = 16;
                break;
            case 2:
                res = 12;
                break;
            case 3:
                res = 13;
                break;
            case 4:
                res = 14;
                break;
            case 5:
                res = 15;
                break;
            case 6:
                res = 18;
                break;
            case 7:
                res = 21;
                break;
            case 8:
                res = 10;
                break;
            case 9:
                res = 10;
                break;
            case 10:
                res = 10;
                break;
            default:
                throw new IllegalArgumentException();
        }
        
        return res;
    }
     
   public int SemeValue()
   {
       int result = 0;
       
       switch(seme)
       {
          case("coppe"):
            result = 0;
            break;
          case("denari"):
            result = 10;
            break;
          case("bastoni"):
            result = 20;
            break;
          case("spade"):
             result = 30;
             break;
          default:
             throw new IllegalArgumentException();     
       }
       
       return result;
   }

    @Override
    public int compareTo(Carta c1) 
    {
        return (c1.valore - this.valore);
    }
    
    public int ReverseCompareTo(Carta c1)
    {
        return (this.valore - c1.valore);
    }
  
}
