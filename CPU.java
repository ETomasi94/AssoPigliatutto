/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assopigliatutto;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enrico Tomasi
 */
public class CPU extends Giocatore
{
    Gioco Sessione;
    
    Random rnd = new Random();
    
    public CPU(String n, boolean decision, Gioco g) 
    {
        super(n, decision);
        
        Sessione = g;
    }
    
    public synchronized void GiocaACaso()
    {
        if(!mano.isEmpty())
        {
            int i = rnd.nextInt(mano.size());
            int j = 0;
            Carta card = mano.get(i);

            if(card.IsMarked())
            {
                j = 1 + rnd.nextInt(card.Potenziale.size());
                Sessione.GiocaCarta(this,j,card);
            }
            else
            {
                Sessione.GiocaCarta(this,0,card);
            }
            
            System.out.println("CPU ha giocato: "+card.GetName());
            
            if(card.IsMarked())
            {
                System.out.println("Ed ha preso: ");
                card.StampaSelezione(j);
                System.out.println("\n");
            }

        }
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            if(YourTurn())
            {
                GetTotalPotential(Table);
                GiocaACaso();
            }
        }
    }
    
}
