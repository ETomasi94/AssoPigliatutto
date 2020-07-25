package assopigliatutto;

import java.util.Comparator;

public class ProbabilityComparator implements Comparator<Carta>
{

    @Override
    public int compare(Carta c1, Carta c2) 
    {
         if(c1.ThoughtProbability >= c2.ThoughtProbability)
         {
             return 1;
         }
         else
         {
             return -1;
         }
    }
    @Override
    public Comparator<Carta> reversed() 
    {
        return Comparator.super.reversed(); //To change body of generated methods, choose Tools | Templates.
    }
}
