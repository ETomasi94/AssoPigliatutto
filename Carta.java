
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
    
    public Carta(char s,int v)
    {
        valore = v;
        
        nome = "";
        
        codice = valore;
        
        Potenziale = new ConcurrentSkipListMap();
        
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
        position = null;
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
    
    public synchronized void CalcolaPotenziale(ArrayList<Carta> Tavolo)
    {
        /*Dato che viene continuamente aggiornato, il potenziale viene ricalcolato
          ogni volta
        */
        this.ResettaPotenziale();
        
        boolean UgualePotenziale = false;
        
        if(!Tavolo.isEmpty())
        {
            if(valore != 1)
            {
                Carta c;

                int i = 0;
                int index = 1;

                int now_sum = 0;

                //Avanziamo fin quando troviamo carte di valore maggiore della selezionata
                while(Tavolo.get(i).GetValue() > valore && i < (Tavolo.size()-1))
                {
                    i++;
                }

                //Includiamo tra le possibili scelte tutte le carte di valore uguale alla carta selezionata
                while(Tavolo.get(i).GetValue() == valore && i < (Tavolo.size()-1))
                {
                    if(Tavolo.get(i).GetValue() == valore)
                    {
                        UgualePotenziale = true;
                        c = Tavolo.get(i);
                        AggiungiPotenziale(index,c);
                        index++;
                    }

                    i++;
                }

               if(Tavolo.get(i).GetValue() == valore)
               {
                    UgualePotenziale = true;
                   
                   c = Tavolo.get(i);
                   AggiungiPotenziale(index,c);
                   index++;
               }


            //Calcoliamo le carte che sommate danno il valore della nostra carta
            if(!UgualePotenziale)
            {
                while(i < (Tavolo.size()))
                {   
                    c = Tavolo.get(i);

                    int j = i+1;

                    int currentsum = c.GetValue();

                    while(j < (Tavolo.size()))
                    {
                        Carta d = Tavolo.get(j);

                        currentsum += d.GetValue();

                        if(currentsum < valore)
                        {
                            AggiungiPotenziale(index,d);
                        }

                        if(currentsum == valore)
                        {
                            AggiungiPotenziale(index,c);
                            AggiungiPotenziale(index,d);
                            currentsum = c.GetValue();
                            index++;
                        }

                        if(currentsum > valore)
                        {
                            currentsum = c.GetValue();
                            RimuoviPotenziale(index);
                        }

                        if(j == (Tavolo.size() - 1) && d.GetValue() != valore)
                        {
                            RimuoviPotenziale(index);
                        }

                        j++;
                    }

                    i++;  
                }
            }
         }
         else
         {
           int i = 0;
           int index = 1;

           boolean response = false;

           while(i < Tavolo.size())
           {
               Carta C = Tavolo.get(i);

               if(C.IsAnAce())
               {
                   response = true;
                   this.AggiungiPotenziale(index, C);
                   index++;
               }

               if(!response)
               {
                   this.AggiungiPotenziale(index, C);
               }

               i++;
           }
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
            System.out.print("|"+C.nome+"|");
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
                System.out.println("-OPZIONE "+E.getKey()+"");

                for(Carta c : E.getValue())
                {
                    System.out.println("| "+c.GetName()+" |");
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

    @Override
    public int compareTo(Carta c1) 
    {
        return (c1.valore - this.valore);
    }
  
}
