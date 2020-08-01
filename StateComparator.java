/*
ASSO PIGLIATUTTO
PROGETTO DI ESPERIENZE DI PROGRAMMAZIONE A.A 2019-2020

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527
 */
package assopigliatutto;

import java.util.Comparator;

/**
 *
 * @author Enrico Tomasi
 */
public class StateComparator implements Comparator<Stato>
{

    @Override
        
     public int compare(Stato S1,Stato S2) 
    {
        if(S1.Gain < S2.Gain)
        {
            return 1;
        }
        else if(S1.Gain > S2.Gain)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
    
}
