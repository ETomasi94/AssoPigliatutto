package assopigliatutto;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Slot 
{
    JLabel Label;

    Carta Card;
    
    public Slot(JLabel L)
    {
        Label = L;
    }
    
    public void AssignCard(Carta C)
    {
        if(HasCard())
        {
            RemoveCard();
        }
        
        Card = C;
        C.AssegnaSlot(this);
        
        int codice = C.GetCodice();
        
        ImageIcon ic = new javax.swing.ImageIcon(getClass().getResource("/CardSkins/"+codice+".png"));
        
        Image image = ic.getImage(); // transform it 
        Image newimg = image.getScaledInstance(100, 160,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ic = new ImageIcon(newimg);  // transform it back
        
        Label.setIcon(ic);
    }

     public void Hide()
     {
        ImageIcon ic = new javax.swing.ImageIcon(getClass().getResource("/CardSkins/"+0+".png"));
        
        Image image = ic.getImage(); // transform it 
        Image newimg = image.getScaledInstance(100, 160,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ic = new ImageIcon(newimg);  // transform it back
        
        Label.setIcon(ic);    
     }
     
     public boolean IsChoosable(int n)
     {
         if(HasCard())
         {
            return Card.Potenziale.containsKey(n);
         }
         else
         {
             return false;
         }
     }
     
     public void RemoveCard()
     {
         if(HasCard())
         {
            Card.RimuoviDaSlot();
            
            this.Card = null;
         }
     }

    public void SetEmpty()
    {
        Card = null;
        
        Label.setIcon(null);
        
        Label.revalidate();
    }
    
    public boolean HasCard()
    {
        return !(Card == null);
    }
    
    public Carta GetCard()
    {
        return Card;
    }
    
    public JLabel GetLabel()
    {
        return this.Label;
    }
}
