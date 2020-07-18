package assopigliatutto;

import java.util.ArrayList;

public class Punteggio 
{
    int Total;
    int Denari;
    int Scope;
    boolean Settebello;
    int Primiera;
    
    ArrayList<Carta> CarteOttenute;
    
    public Punteggio()
    {
        Total = 0;
        Denari = 0;
        Scope = 0;
        Settebello = false;
        Primiera = 0;
        
        CarteOttenute = new ArrayList();
    }
    
    public void AddCard(Carta C)
    {
        CarteOttenute.add(C);

        if(C.GetSeme().equals("denari"))
        {
            Denari++;
            
            if(C.GetValue() == 7)
            {
                Settebello = true;
            }          
        }
        
                
        Total = CarteOttenute.size();
        
    }
    
    public void CallScopa()
    {
        System.out.println("SCOPA!");
        Scope++;
    }
    
    public boolean MaxPrimiera(Carta Max)
    {
        boolean result = false;
        
        for(Carta C : CarteOttenute)
        {
            if(C.seme.equals(Max.seme))
            {
                int v1 = Max.GetPrimieraValue();
                int v2 = C.GetPrimieraValue();

                   if(v2 > v1)
                   {
                       result = true;
                   }
            }
        }
        
        return result;
    }
    
    public int PunteggioDiPrimiera()
    {
        int TotalPoints = 0;

        Carta MaxSpade = null;
        Carta MaxBastoni = null;
        Carta MaxDenari = null;
        Carta MaxCoppe = null;
        
        for(Carta C : CarteOttenute)
        {
          switch(C.seme)
          {
              case "spade":
                  if(MaxSpade == null)
                  {
                      MaxSpade = C;
                  }
                  else
                  {
                      int v1 = MaxSpade.GetPrimieraValue();
                      int v2 = C.GetPrimieraValue();
                      
                      if(v2 > v1)
                      {
                          MaxSpade = C;
                      }
                  }
                  break;
              case "bastoni":
                  if(MaxBastoni == null)
                  {
                      MaxBastoni = C;
                  }
                  else
                  {
                      int v1 = MaxBastoni.GetPrimieraValue();
                      int v2 = C.GetPrimieraValue();
                      
                      if(v2 > v1)
                      {
                          MaxBastoni = C;
                      }
                  }
                  break;
              case "denari":
                  if(MaxDenari == null)
                  {
                      MaxDenari = C;
                  }
                  else
                  {
                      int v1 = MaxDenari.GetPrimieraValue();
                      int v2 = C.GetPrimieraValue();
                      
                      if(v2 > v1)
                      {
                          MaxDenari = C;
                      }
                  }
                  break;
              case "coppe":
                  if(MaxCoppe == null)
                  {
                      MaxCoppe = C;
                  }
                  else
                  {
                      int v1 = MaxCoppe.GetPrimieraValue();
                      int v2 = C.GetPrimieraValue();
                      
                      if(v2 > v1)
                      {
                          MaxCoppe = C;
                      }
                  }
                  break;
              default:
                  throw new IllegalArgumentException();
          }
        }
        
        System.out.println("CARTE CANDIDATE PER LA PRIMIERA:");
        System.out.println(MaxSpade.GetName());
        System.out.println(MaxBastoni.GetName());
        System.out.println(MaxDenari.GetName());
        System.out.println(MaxCoppe.GetName());
        
        TotalPoints = MaxSpade.GetPrimieraValue() + MaxBastoni.GetPrimieraValue() + MaxDenari.GetPrimieraValue() + MaxCoppe.GetPrimieraValue();
        
        return TotalPoints;
    }
    
    public void CopyScore(Punteggio Dest)
    {
        Dest.Total = Total;
        Dest.CarteOttenute = CarteOttenute;
        Dest.Denari = Denari;
        Dest.Primiera = Primiera;
        Dest.Scope = Scope;
        Dest.Settebello = Settebello;
    }
    
    public void ResetScore()
    {
        CarteOttenute.clear();
        
        Total = 0;
        Denari = 0;
        Scope = 0;
        Settebello = false;
        Primiera = 0;
    }
    
    public int GetTotal()
    {
        return Total;
    }
    
    public int GetDenari()
    {
        return Denari;
    }
    
    public int GetScope()
    {
        return Scope;
    }
    
    public boolean GetSettebello()
    {
        return Settebello;
    }
    
    public int GetPrimiera()
    {
        Primiera = PunteggioDiPrimiera();
        return Primiera;
    }
    
    public void PrintCards()
    {
        CarteOttenute.forEach((c) -> 
        {
            System.out.println("CARTA OTTENUTA: "+c.GetName());
        });
    }
    
    public void SetCount(int index,int value)
    {
        switch(index)
        {
            case 0:
                Total += value;
                break;
            case 1:
                if(value <= 10)
                {
                    Denari += value;
                }
                else
                {
                    throw new IllegalArgumentException();
                }
                break;
            case 3:
                Scope+=value;
                break;
            case 4:
                if(value>0)
                {
                    Settebello = true;
                }
                else
                {
                    Settebello = false;
                }
                break;
            case 5:
                Primiera +=value;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public void ScorePrint()
    {
        System.out.println("NUMERO TOTALE DI CARTE: "+Total+"\n");
        System.out.println("NUMERO DI DENARI: "+Denari+"\n");
        System.out.println("NUMERO DI SCOPE VINTE: "+Scope+"\n");
        System.out.println("SETTEBELLO : "+Settebello+"\n");
        System.out.println("PRIMIERA: "+Primiera+"\n");
        System.out.println("CARTE OTTENUTE:");
    }
}
