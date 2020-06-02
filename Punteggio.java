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
public class Punteggio 
{
    int Total;
    int Denari;
    int Scope;
    int Settebello;
    int Primiera;
    
    ArrayList<Carta> CarteOttenute;
    
    public Punteggio()
    {
        Total = 0;
        Denari = 0;
        Scope = 0;
        Settebello = 0;
        Primiera = 0;
        
        CarteOttenute = new ArrayList();
    }
    
    public void AddCard(Carta C)
    {
        CarteOttenute.add(C);
        
        Total = CarteOttenute.size();
        
        if(C.GetSeme().equals('d'))
        {
            Denari++;
            
            if(C.GetValue() == 7)
            {
                Settebello++;
            }          
        }
    }
    
    public void CallScopa()
    {
        Scope++;
    }
    
    public void ResetScore()
    {
        CarteOttenute.clear();
        
        Total = 0;
        Denari = 0;
        Scope = 0;
        Settebello = 0;
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
    
    public int GetSettebello()
    {
        return Settebello;
    }
    
    public int GetPrimiera()
    {
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
                    Settebello = 1;
                }
                else
                {
                    Settebello = 0;
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
    }
}
