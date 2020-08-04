/*
ASSO PIGLIATUTTO
PROGETTO DI ESPERIENZE DI PROGRAMMAZIONE A.A 2019-2020

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.PriorityQueue;

/*
    @CLASS KnowledgeBase

    @OVERVIEW Classe che rappresenta la conoscenza della CPU relativamente alle carte
              non presenti nè sul tavolo nè all'interno della propria mano in modo da
              consentirle di fare inferenza e decidere le proprie mosse sulla base
              di quanto ne ricava
*/
public class KnowledgeBase 
{
    /*-----VARIABILI D'ISTANZA----*/
    WeightComparator Comparator = new WeightComparator();//Confrontatore per ordinare le carte in ordine decrescente di guadagno
    
    PriorityQueue<Carta> Carte = new PriorityQueue(40,Comparator);
    
    ArrayList<Integer> Valori;
    
    int Totale;
    
    int CarteAvversario;
    
    int Residue;
    int Coppe;
    int Bastoni;
    int Spade;
    int Denari;
    
    
    boolean Settebello;
    
    CPU ProcessingUnit;
    /*-----FINE VARIABILI D'ISTANZA----*/
    
    /*----METODO COSTRUTTORE----*/
    
    /*
        @METHOD KnowledgeBase
    
        @OVERVIEW Metodo costruttore della classe KnowledgeBase
    
        @PAR Comp : Computer a cui appartiene la KnowledgeBase
    
        @RETURNS KB : Istanza della classe KnowledgeBase
    */
    public KnowledgeBase(CPU Comp)
    {
        ProcessingUnit = Comp;
        Inizializza();
    }
    
    /*----FINE METODO COSTRUTTORE----*/
    
    /*----METODI DI INIZIALIZZAZIONE ED AGGIORNAMENTO----*/
    
    /*
        @METHOD Inizializza
    
        @OVERVIEW Metodo che inizializza le variabili d'istanza a dei valori
                  prestabiliti in modo da inizializzare la KnowledgeBase
    */
     public void Inizializza()
    {    
        Valori = new ArrayList();
        
        for(int i=0; i<10; i++)
        {
            Valori.add(i,4);
        }
        
        Residue = 40;
        
        Coppe = 10;
        Bastoni = 10;
        Spade = 10;
        Denari = 10;
        
        Settebello =  true;   
        
    }
     
     /*
        @METHOD Aggiorna
     
        @OVERVIEW Metodo che aggiorna la conoscenza del computer relativamente alle
                  carte non presenti nè sul tavolo nè all'interno della propria mano
                  senza che il computer sappia distinguere quali tra queste siano nel
                  mazzo e quali siano del giocatore
     
       @PAR Mazzo : ArrayList di oggetti di tipo carta rappresentante il mazzo della sessione di gioco
       @PAR ManoGiocatore : ArrayList di oggetti di tipo carta rappresentante la mano dell'avversario
     
        N.B: Il mazzo e la mano del giocatore vengono visto come un'unico ArrayList, quindi la CPU non può
        determinare se una generica carta si trova in uno dei due ArrayList in input o nell'altro
     */
     public void Aggiorna(ArrayList<Carta> Mazzo, ArrayList<Carta> ManoGiocatore)
     {
         CarteAvversario = ManoGiocatore.size();
         
         Carte.clear();
         
         Carte.addAll(Mazzo);
         Carte.addAll(ManoGiocatore);
         
         AggiornaStatisticheCarte();
     }
     
      /*----FINE METODI DI INIZIALIZZAZIONE ED AGGIORNAMENTO----*/
    
     /*----METODI DI CALCOLO DELLE PROBABILITA'----*/
     
     /*
        @METHOD TotalProbability
     
        @OVERVIEW Metodo che calcola la probabilità di una generica carta rimanente di essere pescata su tutte
                  le carte residue
     
         ----TOTAL PROBABILITY : 1 / (Numero di carte rimanenti nel mazzo o nella mano del giocatore)
     
        @RETURNS Probability : Numero in virgola mobile a doppia precisione rappresentante la
                  probabilità cercata
     */
    public double TotalProbability()
    {      
        double Probability = (1.0/(Residue*1.0));
        
        //Riduciamo il numero di cifre decimali a tre
        Probability = BigDecimal.valueOf(Probability).setScale(3,RoundingMode.HALF_DOWN).doubleValue();

        return Probability;
    }
    
    /*
        @METHOD SeedProbability
     
        @OVERVIEW Metodo che calcola la probabilità di una generica carta pescata di essere di un determinato seme
     
         ----SEED PROBABILITY : (Numero di carte rimanenti del seme cercato) / (Numero di carte rimanenti nel mazzo o nella mano del giocatore)
     
        @RETURNS Probability : Numero in virgola mobile a doppia precisione rappresentante la
                  probabilità cercata
     */
    public double SeedProbability(String seme)
    {
        double Probability;
        Probability = 0.0;
        
        switch(seme)
        {
            case("bastoni"):
                Probability = ((Bastoni * (1.0)) / (Residue * (1.0)));
                break;
            case("coppe"):
                Probability = ((Coppe* (1.0)) / (Residue * (1.0)));
                break;
            case("denari"):
                Probability = ((Denari* (1.0)) / (Residue * (1.0)));
                break;
            case("spade"):
                Probability = ((Spade* (1.0)) / (Residue * (1.0)));
                break;
            default:
                Probability = 0.0;
                break;
        }
        
        Probability = BigDecimal.valueOf(Probability).setScale(3,RoundingMode.HALF_DOWN).doubleValue();
        
        return Probability;
    }
    
    /*
        @METHOD ValueProbability
     
        @OVERVIEW Metodo che calcola la probabilità di una generica carta pescata di essere di un determinato valore
     
         ----SEED PROBABILITY : (Numero di carte rimanenti del valore cercato) / (Numero di carte rimanenti nel mazzo o nella mano del giocatore)
     
        @RETURNS Probability : Numero in virgola mobile a doppia precisione rappresentante la
                  probabilità cercata
     */
    public double ValueProbability(int valore)
    {
        double Probability = ((Valori.get(valore-1)*1.0) / (Residue * 1.0));
        
        Probability = BigDecimal.valueOf(Probability).setScale(3,RoundingMode.HALF_DOWN).doubleValue();
        
        return Probability;
    }
    
    /*
        @METHOD ContaValore
    
        @OVERVIEW  Metodo che conta le carte di un determinato valore aggiornando
                   l'opportuna variabile d'istanza della KnowledgeBase
    
        @PAR v : Intero rappresentante le carte da includere nel conteggio
    */
    public void ContaValore(int v)
    {
        int a = 0;
        
        for(Carta C : Carte)
        {
            if(C.valore == v)
            {
                a++;
            }
        }
        
        Valori.add((v-1),a);
    }
    
    /*
        @METHOD ContaBastoni
    
        @OVERVIEW  Metodo che conta le carte di bastoni all'interno
                   di uno specifico set di carte aggiornando
                   l'opportuna variabile d'istanza della KnowledgeBase
    
        @PAR CRDS : Coda con priorità di oggetti di tipo carta rappresentante il set di carte in esame
    
       @RETURNS Number : Intero rappresentante il risultato del conteggio
    */
    public int ContaBastoni(PriorityQueue<Carta> CRDS)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.seme.equals("bastoni"))
            {
                a++;
            }
        }
        
        return a;
    }
    
    /*
        @METHOD ContaSpade
    
        @OVERVIEW  Metodo che conta le carte di spade all'interno
                   di uno specifico set di carte aggiornando
                   l'opportuna variabile d'istanza della KnowledgeBase
    
        @PAR CRDS : Coda con priorità di oggetti di tipo carta rappresentante il set di carte in esame
    
        @RETURNS Number : Intero rappresentante il risultato del conteggio
    */
    public int ContaSpade(PriorityQueue<Carta> CRDS)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.seme.equals("spade"))
            {
                a++;
            }
        }
        
        return a;
    }
    
    /*
        @METHOD ContaCoppe
    
        @OVERVIEW  Metodo che conta le carte di coppe all'interno
                   di uno specifico set di carte aggiornando
                   l'opportuna variabile d'istanza della KnowledgeBase
    
        @PAR CRDS :  Coda con priorità di oggetti di tipo carta rappresentante il set di carte in esame
    
        @RETURNS Number : Intero rappresentante il risultato del conteggio
    */
    public int ContaCoppe(PriorityQueue<Carta> CRDS)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.seme.equals("coppe"))
            {
                a++;
            }
        }
        
        return a;
    }
    
    /*
        @METHOD ContaDenari
    
        @OVERVIEW  Metodo che conta le carte di denari all'interno
                   di uno specifico set di carte aggiornando
                   l'opportuna variabile d'istanza della KnowledgeBase
    
        @PAR CRDS : Coda con priorità di oggetti di tipo carta rappresentante il set di carte in esame
    
        @RETURNS Number : Intero rappresentante il risultato del conteggio
    */
    public int ContaDenari(PriorityQueue<Carta> CRDS)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.seme.equals("denari"))
            {
                a++;
            }
        }
        
        return a;
    }
    
      /*
        @METHOD CercaSetteBello
    
        @OVERVIEW Metodo che verifica se la carta Settebello (ovvero il sette di denari)
                  è presente all'interno di un set di carte o meno
    
        @PAR CRDS : Coda con priorità di oggetti di tipo carta rappresentante il set di carte in esame
    
        @RETURNS ContainsSB : Risultato della verifica
    */
    public boolean CercaSetteBello(PriorityQueue<Carta> CRDS)
    {
        //Con il metodo contains applicato ad un nuovo settebello non funzionava
        for(Carta C : CRDS)
        {
            if(C.seme.equals("denari") && C.valore == 7)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /*
        @METHOD AggiornaStatisticheCarte
    
        @OVERVIEW Metodo che aggiorna le variabili d'istanza della classe relative
                  alle carte residue ed alle loro proprietà facendo partire l'analisi
                  dal set di carte residue
    */
    public void AggiornaStatisticheCarte()
    {
        Residue = Carte.size();
        Coppe = ContaCoppe(Carte);
        Bastoni = ContaBastoni(Carte);
        Denari = ContaDenari(Carte);
        Spade = ContaSpade(Carte);
        Settebello = CercaSetteBello(Carte);
        
        for(int v=1; v<=10; v++)
        {
            ContaValore(v);
        }
    }
    
    /*
        @METHOD RimuoviCarta
    
        @OVERVIEW Metodo che aggiorna la KnowledgeBase rimuovendo una carta da
                  quelle residue ed aggiornando in seguito le statistiche della
                  classe
    
        @PAR C : Carta da rimuovere
    */
    public void RimuoviCarta(Carta C)
    {
        Carte.remove(C);
        
        AggiornaStatisticheCarte();
    }
    
    /*----FINE METODI DI CALCOLO DELLE PROBABILITA'----*/
    
    /*----METODI DI VERIFICA----*/
    
    /*
        @METHOD IsDenari
    
        @OVERVIEW Metodo che verifica se una determinata carta in input è di denari
        
        @PAR C : Carta da esaminare
    
        @RETURNS Result : Booleano che rappresenta il risultato della verifica
    */
    public boolean IsDenari(Carta C)
    {
        return C.GetSeme().equals("denari");
    }
    
    /*
        @METHOD IsSettebello
    
        @OVERVIEW Metodo che verifica che la carta in questione sia il SetteBello
                  (ovvero il sette di denari)

        @RETURNS Result : Valore booleano che rappresenta il risultato della verifica

    */
    public boolean IsSetteBello(Carta C)
    {
        return ((IsDenari(C)) && (C.GetValue() ==  7));
    }
    
    /*----FINE METODI DI VERIFICA----*/
    
    /*----METODI GETTERS E SETTERS----*/
    
    /*
        @METHOD GetValueCount
    
        @OVERVIEW Metodo che restituisce il numero di carte di un determinato
                  valore presenti all'interno delle carte residue
    
        @PAR i : Intero rappresentante il valore delle carte da includere nel conteggio
    
        @RETURNS NumberOfI : Intero rappresentante il risultato del conteggio
    */
    public int GetValueCount(int i)
    {
        return Valori.get(i-1);
    }
    
    /*
        @METHOD GetResidual
    
        @OVERVIEW Metodo che restituisce il numero di carte residue
    
        @RETURNS Residual : Intero rappresentante il numero di carte residue
    */
    public int GetResidual()
    {
        return Residue;
    }
    
    /*
        @METHOD GetMostValuableCards
    
        @OVERVIEW Metodo che fa inferenza sulle carte residue, valutando quali siano
                  le più promettenti in termini di guadagno possibile con una singola presa
                  e dunque selezionando tra queste le migliori tre
    
                  Utilizzato per stimare quale possa essere, al caso pessimo, la mano che l'avversario
                  possiede, in modo da fare una valutazione efficiente di quale sia la carta ottima da giocare
    
        @RETURNS MVCRDS : ArrayList di oggetti di tipo carta rappresentanti le tre carte più promettenti che l'avversario
                 può avere all'interno della sua mano
    */
    public ArrayList<Carta> GetMostValuableCards(ArrayList<Carta> Tavolo,Punteggio Score,int PlayerCards)
    {
        ArrayList<Carta> Res = new ArrayList<Carta>();
        
        if(!Tavolo.isEmpty())
        {
            for(Carta C : Carte)
             {
                C.CalcolaPotenziale(Tavolo, Score);
             }

            
            for(int i=0; i<PlayerCards; i++)
            {
                Res.add(Carte.peek());
            }
        }

        return Res;
    }
    
    /*
        @METHOD SetResidual
    
        @OVERVIEW Metodo che imposta il numero di carte residue ad un intero in input
    
        @PAR x : Intero a cui impostare il numero di carte residue
    */
    public void SetResidual(int x)
    {
        Residue = x;
    }
    
    /*
        @METHOD SetValueCount
    
        @OVERVIEW Metodo che imposta il numero di carte di un determinato valore
                  ad un intero in input
    
       @PAR x : Valore di cui il conteggio va modificato
       @PAR c : Valore a cui impostare il conteggio desiderato
    */
    public void SetValueCount(int x,int c)
    {
        Valori.set(x-1,c);
    }
    
    /*
        @METHOD SetBastoni
    
        @OVERVIEW Metodo che imposta il numero di carte di bastoni residue ad un valore intero in input
    
        @PAR x : Valore intero a cui impostare il conteggio in questione
    */
    public void SetBastoni(int x)
    {
        Bastoni = x;
    }
    
     /*
        @METHOD SetCoppe
    
        @OVERVIEW Metodo che imposta il numero di carte di coppe residue ad un valore intero in input
    
        @PAR x : Valore intero a cui impostare il conteggio in questione
    */
    public void SetCoppe(int x)
    {
        Coppe = x;
    }
    
     /*
        @METHOD SetSpade
    
        @OVERVIEW Metodo che imposta il numero di carte di spade residue ad un valore intero in input
    
        @PAR x : Valore intero a cui impostare il conteggio in questione
    */
    public void SetSpade(int x)
    {
        Spade = x;
    }
    
     /*
        @METHOD SetDenari
    
        @OVERVIEW Metodo che imposta il numero di carte di denari residue ad un valore intero in input
    
        @PAR x : Valore intero a cui impostare il conteggio in questione
    */
    public void SetDenari(int x)
    {
        Denari = x;
    }
    
    /*
        @METHOD ConfermaSetteBello
    
        @OVERVIEW Metodo che dichiara se il SetteBello (ovvero il sette di denari) è presente tra le carte
                  residue impostando l'opportuna variabile d'istanza (il valore booleano Settebello) ad un
                  valore booleano in input
    
        @PAR b : Valore booleano a cui impostare la variabile SetteBello
    */
    public void ConfermaSettebello(boolean b)
    {
        Settebello = b;
    }
    
    /*
        @METHOD CopyKB
    
        @OVERVIEW Metodo che, presa una KnowledgeBase in input, copia tutti i valori
                  della KnowledgeBase corrente in quest'ultima.
                  Utilizzata originariamente per la generazione degli stati dell'albero
                  di gioco
    
       @PAR Dest : KnowledgeBase in cui copiare i valori della KnowledgeBase corrente
    */
    public void CopyKB(KnowledgeBase Dest)
    {
        if(Dest == null)
        {
            System.out.println("CREANDO UNA NUOVA KB");
            Dest = new KnowledgeBase(ProcessingUnit);
        }
        
        Dest.Carte = Carte;
        Dest.Valori = Valori;

        Dest.Totale = Totale;

        Dest.Residue = Residue;
        Dest.Coppe = Coppe;
        Dest.Bastoni = Bastoni;
        Dest.Spade = Spade;
        Dest.Denari = Denari;

        Dest.Settebello = Settebello;
    }
    
    /*----FINE METODI GETTERS E SETTERS----*/
    
    /*----METODI DI STAMPA E DI DEBUG----*/
    
    /*
        @METHOD TestMVC
    
        @OVERVIEW Metodo di Testing che verifica che la KnowledgeBase inferisca
                  correttamente sulle carte residue catalogandole in ordine
                  decrescente di valore in termini di guardagno con una singola
                  presa in relazione alla situazione attuale del Tavolo ed al punteggio
                  corrente, entrambi passati in input al metodo in questione
    
        @PAR Tavolo : ArrayList di oggetti di tipo carta rappresentante il tavolo da gioco
             e la sua configurazione attuale
    
        @PAR Score : Punteggio attuale in relazione a cui stimare le carte più promettenti
    */
    public void TestMVC(ArrayList<Carta> Tavolo,Punteggio Score)
    {
        for(Carta C : Carte)
         {
            C.CalcolaPotenziale(Tavolo, Score);
         }

        System.out.println("STAMPIAMO LE CARTE ORDINATE SECONDO IL GUADAGNO: ");
        
        for(Carta C : Carte)
        {
            System.out.println("| CARTA: "+C.nome+" | | GUADAGNO: "+C.Weight+" |");
        }
    }
    
    /*
        @METHOD KBPrint
    
        @OVERVIEW Metodo che stampa in output le principali variabili d'istanza della classe
    */
    public synchronized void KBPrint()
    {
        PrintValues();
        
        System.out.println("INOLTRE SO CHE: ");
        
        System.out.println("--RIMANGONO : "+Residue+" CARTE, DI CUI");
        System.out.println("----"+Bastoni+" DI BASTONI");
        System.out.println("----"+Coppe+" DI COPPE");
        System.out.println("----"+Spade+" DI SPADE");
        System.out.println("----"+Denari+" DI DENARI");
        System.out.println("---- E IL SETTEBELLO: "+Settebello);
        System.out.println("----PROBABILITA' DI PESCARE UNA GENERICA CARTA: "+TotalProbability());
    }
    
    /*
        @METHOD PrintValues
    
        @OVERVIEW Metodo che stampa in output il conteggio dei vari valori possibili all'interno delle carte residue
                  e la loro probabilità
    */
    public void PrintValues()
    {
        System.out.println("CPU: SO CHE, ORDINATE PER VALORE, SONO POSSIBILI ANCORA TOT CARTE");
        
        for(int i=0;i<10;i++)
        {
            System.out.println("----VALORE: "+(i+1)+" | RESIDUE: "+Valori.get(i)+" | PROBABILITA': "+ValueProbability(Valori.get(i)));
        }
    }
    /*----FINE METODI DI STAMPA E DI DEBUG----*/
}
