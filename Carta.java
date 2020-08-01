/*
ASSO PIGLIATUTTO
PROGETTO DI ESPERIENZE DI PROGRAMMAZIONE A.A 2019-2020

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

/*
    @CLASS Carta

    @OVERVIEW Classe che implementa la struttura di una generica carta ed i metodi
              che contribuiscono a definirla
*/
public class Carta implements Comparable<Carta>
{
    /*----VARIABILI D'ISTANZA----*/
    
    String seme;//Seme della carta (Coppe,bastoni,spade,denari)
    String nome;//Nome della carta
    
    Slot position;//Slot in cui è inserita la carta
    
    int valore;//Valore della carta
    int codice;//Codice identificativo della carta (uno per ogni carta)

    ConcurrentSkipListMap<Integer,ArrayList<Carta>> Potenziale;//Lista delle possibili prese
    ConcurrentSkipListMap<Integer,Double> ValoriPotenziale;//Lista dei valori delle possibili prese
    
    //N.B : Le due liste sopra elencate sono in corrispondenza biunivoca l'una con l'altra
    
    double ThoughtProbability;//Probabilità generica della carta
    double Weight;//Peso della carta in termini di guadagno
    
    int MaxPotential;//Indice della scelta di guadagno massimo
    
    /*----FINE VARIABILI D'ISTANZA----*/
    
    /*----METODO COSTRUTTORE----*/
    
    /*
        @METHOD Carta
    
        @OVERVIEW Metodo costruttore di una generica istanza della classe Carta
    
        @PAR s : Carattere rappresentante il seme della carta
        @PAR v : Intero rappresentante il valore della carta
    
        @RETURNS Card : Oggetto di tipo carta
    */
    public Carta(char s,int v)
    {
        valore = v;
        
        nome = "";
        
        codice = valore;
        
        Potenziale = new ConcurrentSkipListMap();
        
        ValoriPotenziale = new ConcurrentSkipListMap();    
    
        /*
            COME VIENE DETERMINATO IL CODICE DELL CARTA:
            
            Il codice della carta serve principalmente a reperirne
            l'immagine all'interno della cartella CardSkins e viene
            determinato nel seguente modo:
        
            - Valore + Seme;
        
            In modo che si presenti la seguente situazione:
        
            -Carte di coppe : Intervallo [0,10]
            -Carte di denari : Intervallo [11,20]
            -Carte di batoni : Intervallo [21,30]
            -Carte di spade : Intervallo [31,40]
        */
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
        
        ThoughtProbability =  1.0;
        
        Weight = 1.0;
    }
    
     /*----FINE METODO COSTRUTTORE----*/
    
    /*----MODIFICATORI DELLE PROPRIETA' DELLA CARTA----*/
    /*
        @METHOD AssegnaSlot
    
        @OVERVIEW Metodo che assegna una slot della board alla carta in questione
    
        @PAR S : Slot da assegnare alla carta
    */
    public void AssegnaSlot(Slot S)
    {
        position = S;
    }
    
    /*
        @METHOD RimuoviDaSlot
    
        @OVERVIEW Metodo che rimuove la carta in questione dalla slot precedentemente assegnata
    */
    public void RimuoviDaSlot()
    {
        if(position != null)
        {
            position = null;
        }
    }
 
    /*
        @METHOD IsMarked
    
        @OVERVIEW Metodo che verifica se una carta ha delle possibili prese associate ad essa 
    */
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
    
    /*
        @METHOD AggiungiPotenziale
    
        @OVERVIEW Metodo che aggiunge nella posizione rappresentata dall'indice dato in input della lista
                  delle possibili prese la carta in input associata a quest'ultima
    
        @PAR marchio : Indice della lista delle possibili prese in cui aggiungere la carta
    
        @PAR c : Carta da aggiungere
    */
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
    
    /*
        @METHOD AggiungiValorePeso
    
        @OVERVIEW Metodo che aggiunge, in una posizione denotata da un intero in input
                  all'interno della lista dei valori delle possibili prese
                  il peso della carta C in input in termini di guadagno relativamente al punteggio Score
                  in input
    
        @PAR marchio : Indice in cui inserire il valore 
        @PAR c : Carta il cui peso in termini di guadagno rappresenta il valore da inserire
        @PAR Score : Punteggio relativamente a cui è calcolato il peso della carta
    */
    public void AggiungiValorePeso(int marchio,Carta c,Punteggio Score)
    {
        Double Scelta = Pesa(c,Score);
        
        if(ValoriPotenziale.containsKey(marchio))
        {
            Double ValoreScelta = ValoriPotenziale.get(marchio);
            Scelta += ValoreScelta;
            ValoriPotenziale.put(marchio,Scelta);
        }
        else
        {
            ValoriPotenziale.put(marchio,Scelta);
        }
    }
    
    /*
        @METHOD ResettaPotenziale
    
        @OVERVIEW Metodo che elimina tutti gli elementi presenti nella lista
                  delle possibili scelte e nella lista dei valori associati
                  ad esse
    */
    public void ResettaPotenziale()
    {
        Potenziale.clear();
        ValoriPotenziale.clear();
    }
    
    /*----FINE MODIFICATORI DELLE PROPRIETA' DELLA CARTA----*/
    
    /*----METODI DI INFERENZA SUL GIOCO----*/
    
    /*
        @METHOD CalcolaPotenziale
    
        @OVERVIEW Metodo che calcola tutte le possibili prese che è possibile effettuare
                  con la carta in relazione alle carte presenti sul tavolo e valutandone
                  il valore relativamente al punteggio corrente.
    
                  Essendo il tavolo ordinato in ordine decrescente di valore, e dato che
                  se vi sono carte di uguale valore alla carta corrente, è possibile prendere
                  soltanto quelle, il metodo esegue una scansione lineare inserendo come possibili
                  scelte, nel caso le trovi, le carte di ugual valore, in caso contrario avvia una 
                  ricerca in DFS su un array a due dimensioni rappresentante le combinazioni di carte
                  possibili (un algoritmo di programmazione dinamica)
    
                  Ragionamento a parte va fatto nel caso in cui questa carta sia un asso, in tal caso
                  se vi sono assi in tavola è possibile soltanto prendere questi ultimi, quindi verrà 
                  effettuata una visita DFS, altrimenti è possibile prendere tutte le carte in tavola, che
                  verranno inserite in un unica presa ricavata da una scansione lineare
    
        @PAR Tavolo : ArrayList di oggetti di tipo Carta in relazione a cui effettuare il calcolo
        @PAR Score : Punteggio in relazione a cui calcolare il valore della presa
    */
    public synchronized void CalcolaPotenziale(ArrayList<Carta> Tavolo,Punteggio Score)
    {
        /*Dato che viene continuamente aggiornato, il potenziale viene ricalcolato
          ogni volta
        */
        this.ResettaPotenziale();
        
        Double Peso = Pesa(this,Score);

        if(!Tavolo.isEmpty())
        {
            if(!IsAnAce())
            {
                int index = 1;
                
                boolean UgualePotenziale = false;
                
                for(Carta C : Tavolo)
                {
                    if(C.GetValue() == valore)
                    {
                        AggiungiPotenziale(index,C);
                        
                        AggiungiValorePeso(index,C,Score);
                        
                        AggiungiValorePeso(index,this,Score);
                        
                        index++;
                        
                        UgualePotenziale = true;
                    }
                }
                
                if(!UgualePotenziale)
                {
                    //Inizio visita DFS
                    PotentialCalculating(Tavolo,0,Peso,Score);
                }
      
            }
            else
            {
              boolean response = false;

              for(Carta C : Tavolo)
               {
                  if(!C.IsAnAce() && !response)
                  {
                    AggiungiPotenziale(1,C);
                  
                    AggiungiValorePeso(1,C,Score);
                  }
                  else
                  {
                        response = true;

                        ResettaPotenziale();
                        
                        //Inizio visita DFS
                        PotentialCalculating(Tavolo,0,Peso,Score);

                  }
               }             
              if(!response)
              {
                  AggiungiValorePeso(1,this,Score);
              }
            }
        }
        
        Weight = PesoMedio();
                
        /*Dichiarazione dell'indice della presa di valore massimo ai fini di una semplificazione della ricerca
         della carta migliore da giocare*/
        GetMaxPotential();
    }
    
    /*
        @METHOD PesoMedio
    
        @OVERVIEW Metodo che calcola la media dei valori delle possibili prese
                  sotto forma di numero in virgola mobile in doppia precisione

        @RETURNS R : Media dei valori delle possibili prese
    */
    public Double PesoMedio()
    {
        Double R = 0.0;
        
        if(!ValoriPotenziale.isEmpty())
        {
            for(Double D : ValoriPotenziale.values())
            {
                R += D;
 
            }

            R /= ValoriPotenziale.size() * (1.00);
        }
        
        return R;
    }
    
    /*
    @METHOD Pesa (Carta)

    @OVERVIEW Metodo che calcola il peso di una carta inteso come guadagno aggiuntivo
              in termini del punteggio attuale dato in input
 
    @PAR Card : Carta in esame
    @PAR Score : Punteggio attuale

    @RETURNS Weight : Numero in virgola mobile a doppia precisione rappresentante il peso della carta
    */
    public Double Pesa(Carta Card,Punteggio Score)
    {
        //Costanti perché sempre graditi, aldilà del punteggio corrente
        double PesoDelSettebello = 1;

        /*Contribuiscono, in base al fatto che sia necessario o meno, a decidere
          se vale la pena di prendere questa carta*/
        double PunteggioCarta = 0.3333;
        double PesoDeiDenari = 0.3333;
        double PesoInPrimiera = 0.3333;
        
        double WeightedValue = 0.0;
        double Result = 0.0;

        if(Card.IsSettebello())
        {
            WeightedValue += PesoDelSettebello;
        }

        if(Score.MaxPrimiera(Card))
        {
            WeightedValue += PesoInPrimiera;
        }

        /*Se ho una carta di denari e non ho ancora la certezza di avere
        più carte di denari dell'avversario, allora la carta vale di più*/
        if(Card.seme.equals("denari") && Score.GetDenari() <= 5)
        {
            WeightedValue += PesoDeiDenari;
        }

         /*Se non ho ancora più carte dell'avversario, allora la carta vale di più*/
        if(Score.GetTotal() <= 20)
        {
            WeightedValue += PunteggioCarta;
        }

        //Punteggio Standard associato ad ogni carta
        WeightedValue *= 1.0;

        Result += WeightedValue;

        return Result;
    }
    
    /*
        @METHOD Pesa (ArrayList<Carta>)
    
        @OVERVIEW Metodo che calcola il peso di tutto un ArrayList di oggetti di tipo carta 
                 inteso come somma dei guadagni aggiuntivi dati dalle carte in termini 
                  del punteggio attuale dato in input
    
        @PAR CardList : ArrayList di oggetti di tipo carta in esame
        @PAR Score : Punteggio relativamente a cui calcolare i guadagni
    
        @RETURNS Weight :  Numero in virgola mobile a doppia precisione rappresentante la
                           somma dei pesi delle carte dell'ArrayList in input
    */
     public Double Pesa(ArrayList<Carta> CardList,Punteggio Score)
    {
        //Costanti perché sempre graditi, aldilà del punteggio corrente
        double PesoDelSettebello = 1;
        
        /*Contribuiscono, in base al fatto che sia necessario o meno, a decidere
          se vale la pena di prendere questa carta*/
        double PunteggioCarta = 0.3333;
        double PesoDeiDenari = 0.3333;
        double PesoInPrimiera = 0.3333;

        
        double WeightedValue = 0.0;
        double Result = 0.0;

        for(Carta Card : CardList)
        {
            if(Card.IsSettebello())
            {
                WeightedValue += PesoDelSettebello;
            }

            if(Score.MaxPrimiera(Card))
            {
                WeightedValue += PesoInPrimiera;
            }

            /*Se ho una carta di denari e non ho ancora la certezza di avere
            più carte di denari dell'avversario, allora la carta vale di più*/
            if(Card.seme.equals("denari") && Score.GetDenari() <= 5)
            {
                WeightedValue += PesoDeiDenari;
            }

             /*Se non ho ancora più carte dell'avversario, allora la carta vale di più*/
            if(Score.GetTotal() <= 20)
            {
                WeightedValue += PunteggioCarta;
            }

            //Punteggio Standard associato ad ogni carta
            WeightedValue += 1.0;

            Result += WeightedValue;
        }


        return Result;
    }
    
    /*
        @METHOD PotentialCalculating
    
        @OVERVIEW Metodo che calcola tutte le possibili prese effettuabili mediante
                  questa carta con i rispettivi valori, inizializzando una visita
                  DFS ricorsiva per il calcolo dei potenziali
    
        @PAR Cards : ArrayList di oggetti di tipo carta in esame
        @PAR start : Intero rappresentante la posizione da cui iniziare la ricerca
        @PAR InitialWeight : Peso iniziale (ovvero peso della carta) che verrà sommato
                             ad ogni valore del potenziale trovato per ricavarne il valore
                             complessivo
        @PAR Score : Punteggio relativamente a cui calcolare il valore delle prese
    */
    public void PotentialCalculating(ArrayList<Carta> Cards,int start,Double InitialWeight,Punteggio Score)
    {        
        ArrayList<ArrayList<Carta>> Result = new ArrayList();
        ArrayList<Carta> Temp = new ArrayList();
        ArrayList<Double> Weights = new ArrayList();
        
        int FinalValue = valore;

        //Inizio della visita DFS ricorsiva
        Evaluate(Cards,start,FinalValue,Temp,Result,Score,0,Weights);

        /*
            Inserimento delle prese in corrispondenza ai rispettivi valori
            nelle liste opportune
        */
        for(int i=0;i<Result.size();i++)
        {
            Potenziale.put(i+1,Result.get(i));
            ValoriPotenziale.put(i+1,Weights.get(i)+InitialWeight);
        }
    }
    
    /*
        @METHOD Evaluate
    
        @OVERVIEW Metodo che inizializza ed esegue una visita DFS ricorsiva sull'ArrayList delle carte
    
        @PAR Cards : ArrayList delle carte in questione
        @PAR start : Intero che rappresenta la posizione di inizio della visita
        @PAR Target : Valore obiettivo (Valore della carta a cui la somma delle carte trovate deve essere equivalente)
        @PAR Temp : ArrayList di supporto alla visita
        @PAR Result : ArrayList che conterrà i risultati
        @PAR Score : Punteggio relativamente a cui calcolare i guadagni
        @PAR InitialWeight : Peso in termini di guadagno da cui la visita comincia
        @PAR Weights : ArrayList contente i valori delle prese
    
        COSTO COMPUTAZIONALE = O(|n|) (DFS senza ripetizioni)
    */
    public void Evaluate(ArrayList<Carta> Cards,int start,int Target,ArrayList<Carta> Temp,ArrayList<ArrayList<Carta>> Result,Punteggio Score,double InitialWeight,ArrayList<Double> Weights)
    {
        Double Weight = InitialWeight;
        
          if(Target==0)
          {             
            Result.add(new ArrayList<>(Temp));
            Weights.add(Weight);
            return;
          }
          
        if(Target<0)
        {
            Weight = 0.0;
            return;
        }
 
        Carta Previous = null;
        
        for(int i=start; i<Cards.size(); i++)
        {
            if(Previous== null || !Previous.equals(Cards.get(i)))
            { 
                Temp.add(Cards.get(i));//Il calcolo inizia ogni volta da un elemento diverso
                
                int value = Cards.get(i).GetValue();
                
                double peso = Pesa(Cards.get(i),Score);
                
                Weight += peso;

                Evaluate(Cards,i+1, Target-value,Temp,Result,Score,Weight,Weights);
                
                peso = Pesa(Temp.get(Temp.size() - 1),Score);
                
                Weight -= peso;
                
                Temp.remove(Temp.size()-1);
                
                Previous=Cards.get(i);
            }
        }
    }
      
    /*----FINE METODI DI INFERENZA SUL GIOCO----*/
    
    /*----METODI DI VERIFICA----*/
    
    /*
        @METHOD IsAnAce
    
        @OVERVIEW Metodo che verifica se la carta è un asso

        @RETURNS Result : Booleano che indica se la carta è un asso
    */
    public boolean IsAnAce()
    {
        return (this.GetValue() == 1);
    }
    
    /*
        @METHOD HasPotential
    
        @OVERVIEW Metodo che verifica se la carta ha associata una presa di indice
                  M o meno
    
        @PAR M : Indice della presa cercata
    
        @RETURNS Result : Booleano che indica se la presa di indice M esiste
    */
    public boolean HasPotential(int M)
    {
        return Potenziale.containsKey(M);
    }
    
    /*
        @METHOD OnSlot
    
        @OVERVIEW Metodo che indica se la carta ha associato uno slot
        
        @RETURNS Result : Booleano che indica se la carta ha associato uno slot o meno
    
    */
     public boolean OnSlot()
    {
        return !(position == null);
    }
    
    /*
        @METHOD IsSettebello
    
        @OVERVIEW Metodo che verifica che la carta in questione sia il SetteBello
                  (ovvero il sette di denari)

        @RETURNS Result : Valore booleano che rappresenta il risultato della verifica

    */
    public boolean IsSettebello()
    {
        return(seme.equals("denari") && (valore==7));
    }
    
    /*----FINE METODI DI VERIFICA----*/
    
    /*
        @METHOD RimuoviPotenziale
    
        @OVERVIEW Metodo che rimuove la presa di indice M dalla lista delle
                  possibili prese della carta
    
        @PAR M : Indice della presa da rimuovere
    */
    public void RimuoviPotenziale(int M)
    {
        Potenziale.remove(M);
    }
    
    /*
        @METHOD GetMaxPotential
    
        @OVERVIEW Metodo che ricava l'indice di potenziale massimo, assegnandolo
                  all'opportuna variabile d'istanza della carta in questione
    */
    public void GetMaxPotential()
    {
      if(IsMarked())
      {
          Set<Entry<Integer,Double>> Entryes = ValoriPotenziale.entrySet();
          
          Double Max = 0.0;
          int Index = 0;
          
          for(Entry E : Entryes)
          {
              Double Value = (Double) E.getValue();
              int Key = (int) E.getKey();
              
              if(Value > Max)
              {
                  Max = Value;
                  Index = Key;
              }
          }
         
          SetMaxPotential(Index);
      }
    }
    
    /*----FINE METODI DI VERIFICA----*/
    
    /*----METODI DI STAMPA E DI DEBUG----*/
    
    /*
        @METHOD StampaPotenziali
    
        @OVERVIEW Metodo che stampa la lista di prese possibili associate alla carta
                  o un opportuno messaggio nel caso non ve ne siano
    */
    public void StampaPotenziali()
    {
        System.out.println("----"+this.nome+"----\n");        
        
       if(!Potenziale.isEmpty())
       {
            Set<Entry<Integer,ArrayList<Carta>>> Es = Potenziale.entrySet();
       
            for(Entry<Integer,ArrayList<Carta>> E : Es)
            {
                System.out.println(" -OPZIONE "+E.getKey()+"");

                for(Carta c : E.getValue())
                {
                    System.out.println(" | "+c.GetName()+" |");
                }
            }
       }
       else
       {
           System.out.println("-NESSUNA CARTA O COMBINAZIONE DI CARTE ASSOCIATA-\n");
       }
    }
    
      /*
        @METHOD StampaPotenziali
    
        @OVERVIEW Metodo che stampa le carte relative alla presa in posizione M in input
                  nel caso questa esista
    
        @PAR M : Posizione della lista delle prese in cui cercare una presa
    */
     public void StampaSelezione(int M)
    {
        if(HasPotential(M))
        {
            for(Carta C : Potenziale.get(M))
            {
                System.out.print("|"+C.nome+"| ");
            }
        }
    }
    
    /*
        @METHOD StampValoriPotenziali
    
        @OVERVIEW Metodo che stampa la lista dei valori associati alle prese
    */
    public void StampaValoriPotenziali()
    {
       if(!ValoriPotenziale.isEmpty())
       {
            Set<Entry<Integer,Double>> Es = ValoriPotenziale.entrySet();
       
            for(Entry<Integer,Double> E : Es)
            {
                System.out.println(" -OPZIONE "+E.getKey()+"");
                
                Double D = E.getValue();
    
                System.out.println(" | "+D+" |");
            }
            
            System.out.println("----"+this.nome+"----\n");        
        
       }
       else
       {
           System.out.println("-NESSUNA CARTA O COMBINAZIONE DI CARTE ASSOCIATA-\n");
       }
    }
    
    /*----FINE METODI DI STAMPA E DI DEBUG----*/
    
    /*----METODI GETTERS E SETTERS----*/
  /*
        @METHOD GetProbability
    
        @OVERVIEW Metodo che restituisce la probabilità specifica di una carta

        @RETURNS Probability : Numero in virgola mobile a doppia precisione che rappresenta
                 la specifica probabilità di incontrare quella carta
    */   
   public double GetProbability()
   {
       return ThoughtProbability;
   }
   
    /*
        @METHOD SetProbability
    
        @OVERVIEW Metodo che imposta la probabilità specifica di una carta ad uno specifico valore in input

        @PAR Probability : Numero in virgola mobile a doppia precisione che rappresenta
              la specifica probabilità di incontrare quella carta
    */  
   public void SetProbability(double Probability)
   {
       ThoughtProbability = Probability;
   }
  
   /*
        @METHOD GetSeme
    
        @OVERVIEW Metodo che restituisce il seme della carta
    
        @RETURNS Seed : Stringa rappresentante il seme della carta
    */   
   public String GetSeme()
    {
        return this.seme;
    }
  
   /*
        @METHOD GetName
    
        @OVERVIEW Metodo che restituisce il nome della carta
   
        @RETURNS Name : Stringa rappresentante il nome della carta
    */    
    public String GetName()
    {
        return this.nome;
    }
   
    /*
        @METHOD GetValue
    
        @OVERVIEW Metodo che restituisce il valore della carta
        
        @RETURNS Value : Intero che rappresenta il valore della carta
    */   
    public int GetValue()
    {
        return this.valore;
    }
    
    /*
        @METHOD GetCodice
    
        @OVERVIEW Metodo che restitiuisce il codice della carta
    
        @RETURNS Code : Intero che rappresenta il valore della carta
    */    
    public int GetCodice()
    {
        return this.codice;
    }
    
    /*
        @METHOD GetSlot
    
        @OVERVIEW Metodo che restituisce la slot in cui la carta è inserita
    
        @RETURNS Position : Slot in cui è inserita la carta
    
        @THROWS
    */    
    public Slot GetSlot()
    {
        return position;
    }
    
    
    /*
        @METHOD GetPrimieraValue
    
        @OVERVIEW Metodo che restituisce il valore di primiera della carta
                  viene utilizzato per il calcolo della primiera
        
        @RETURNS PrimieraValue : Intero che rappresenta il valore di primiera della carta
    */     
     public int GetPrimieraValue()
    {
        int res;
        
        switch(valore)
        {
            case 1:
                res = 16;
                break;
            case 2:
                res = 12;
                break;
            case 3:
                res = 13;
                break;
            case 4:
                res = 14;
                break;
            case 5:
                res = 15;
                break;
            case 6:
                res = 18;
                break;
            case 7:
                res = 21;
                break;
            case 8:
                res = 10;
                break;
            case 9:
                res = 10;
                break;
            case 10:
                res = 10;
                break;
            default:
                throw new IllegalArgumentException();
        }
        
        return res;
    }

   /*
        @METHOD SetMaxPotential
    
        @OVERVIEW Metodo che setta l'indice della presa di massimo valore ad un intero
                  dato in input
    
        @PAR Index : Intero che rappresenta l'indice in cui si vorrebbe trovare la presa
                     di massimo valore
    */  
       public void SetMaxPotential(int Index)
    {
        MaxPotential = Index;
    }
       
    /*----FINE METODI GETTERS E SETTERS----*/
       
    /*----METODI DI CONFRONTO----*/

  /*
        @METHOD CompareTo
    
        @OVERVIEW Metodo di confronto standard per due carte, confrontate per valore
                  l'una rispetto all'altra

       @PAR c1 : Carta da confrontare alla carta in questione
       
        @RETURNS CompareResult : Intero uguale a 
                                  1 se il valore di c1 è maggiore del valore di questa carta
                                  0 se il valore di c1 è uguale al valore di questa carta
                                 -1 altrimenti
    */
    @Override
    public int compareTo(Carta c1) 
    {
        return (c1.valore - this.valore);
    }
    
    /*
        @METHOD CompareTo
    
        @OVERVIEW Metodo di confronto inverso per due carte, confrontate per valore
                  l'una rispetto all'altra

       @PAR c1 : Carta da confrontare alla carta in questione
       
        @RETURNS CompareResult : Intero uguale a 
                                 -1 se il valore di c1 è maggiore del valore di questa carta
                                  0 se il valore di c1 è uguale al valore di questa carta
                                 1 altrimenti
    */ 
    public int ReverseCompareTo(Carta c1)
    {
        return (this.valore - c1.valore);
    }
    
    /*----FINE METODI DI CONFRONTO----*/ 
    
}
