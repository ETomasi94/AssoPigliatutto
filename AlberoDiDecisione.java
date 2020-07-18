package assopigliatutto;

import java.util.Set;
import java.util.TreeSet;

public class AlberoDiDecisione 
{
    Set<Stato> Tree = new TreeSet();
    Stato Radice;
    
    public AlberoDiDecisione(Stato S)
    {
        Radice = S;
        Radice.Parent = null;
    }
    
    public Stato CancellaStatoAttuale(Stato S) 
        {
                if (S.Parent != null) 
                {
                    throw new IllegalStateException("Lo stato che si vuole cancellare non è quello attuale");
                }
                
                Stato NuovoStato = null;
                
                if (!S.GetResults().isEmpty()) 
                {
                        NuovoStato = S.GetResults().get(0);
                        NuovoStato.SetParent(null);
                        S.Results.remove(0);
                        
                        for (Stato Situation : S.Results)
                        {
                                Situation.SetParent(NuovoStato);
                        }
                       
                        NuovoStato.GetResults().addAll(S.Results);
                }
                
                S.Results.clear();     
               return NuovoStato;
        }
      
        public void CancellaStato(Stato S) 
        {
            if (S.Parent != null) 
            {
                int index = S.Parent.GetResults().indexOf(this);
                S.Parent.GetResults().remove(S);
                for (Stato Attuale : S.Results) 
                {
                    Attuale.SetParent(S.Parent);
                }
                
                S.Parent.GetResults().addAll(index, S.GetResults());
            } 
            else 
            {
                 CancellaStatoAttuale(S);
            }
            
            S.Results.clear();
        }
        
        public void StampaStato(Stato S)
        {
            if(S == null)
        {
            return;
        }
        
        System.out.println("STATO: "+S.Label);
        
        System.out.println("KNOWLEDGE BASE:");
        
        S.Actual.KBPrint();
        
        System.out.println("TABLE");
        System.out.println("\n");
        
        for(Carta C : S.Table)
        {
            System.out.print("| "+C.nome+" |");
        }
        
        System.out.println("\n");
        System.out.println("CPU HAND:");
        
        for(Carta C : S.Hand)
        {
            System.out.println("@"+C.nome+"@");
        }
 
        }
        
        public void StampaAlbero()
        {
            if(this == null)
            {
                return;
            }
            
            if(Radice.Results.isEmpty())
            {
                System.out.println("I RISULTATI SONO VUOTI?: "+Radice.Results.isEmpty());
                StampaStato(Radice);
            }
            else
            {
                for(Stato S : Radice.Results)
                {
                    StampaStato(S);
                }
            }
        }
}
