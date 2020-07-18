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
public class PrimieraComparator implements Comparator<Carta>
{    
   

    @Override
    public int compare(Carta C1, Carta C2) 
    {
        return (GetValueToCompare(C1) - GetValueToCompare(C2));
    }

    @Override
    public Comparator reversed() {
        return Comparator.super.reversed(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int GetValueToCompare(Carta C)
    {
        return (21+C.SemeValue()+C.GetPrimieraValue());
    }
    
}
