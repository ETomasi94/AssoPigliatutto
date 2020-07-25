/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assopigliatutto;

import java.util.Comparator;

/**
 *
 * @author Enrico Tomasi
 */
public class WeightComparator implements Comparator<Carta>
{

    @Override
    public int compare(Carta C1, Carta C2) 
    {
       if(C1.Weight >= C2.Weight)
       {
           return -1;
       }
       else
       {
           return 1;
       }
    }
    
}
