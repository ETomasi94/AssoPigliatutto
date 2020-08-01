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
import java.util.Random;

/*
    @CLASS CPU

    @OVERVIEW Classe che implementa le proprietà ed i comportamenti della CPU
*/
public class CPU extends Giocatore
{
    //Sesione in cui la CPU gioca
    Gioco Sessione;
    
    //Randomizzatore delle scelte (Metodo Gioca a Caso)
    Random rnd = new Random();
    
    /*Knowledge Base che memorizza le informazioni sulle carte residue senza
      contenere informazioni sulle carte possedute dall'avversario*/
    KnowledgeBase KB;
    
    /*
        Intelligenza Artificiale della CPU
    */
    AI Intelligence;

    /*----METODO COSTRUTTORE----*/
    
    /*
        @METHOD CPU
    
        @OVERVIEW Metodo costruttore che inizializza un'istanza della CPU
    
        @PAR n : Stringa rappresentante il nome della CPU
        @PAR decision : Booleano che indica se la CPU è o meno il mazziere della partita
        @PAR g : Sessione in cui la CPU gioca
        @PAR Tavolo : Tavolo della sessione riguardo cui la CPU prende delle decisioni
    
        @RETURNS Computer : Istanza di una CPU
    */
    public CPU(String n, boolean decision, Gioco g, ArrayList<Carta> Tavolo) 
    {
        super(n, decision, Tavolo);
        
        Sessione = g;
        
        KB = new KnowledgeBase(this); 
        
        Intelligence = new AI();
    }
    
    /*----FINE METODO COSTRUTTORE----*/
    
    /*----METODI RELATIVI AL CICLO DI VITA DELLA CPU----*/
    
    /*
        @METHOD GiocaACaso
    
        @OVERVIEW Metodo attraverso cui la CPU sceglie una carta in maniera casuale
                  tra quelle all'interno della sua mano e prende le carte associate
                  ad una potenziale presa anch'essa scelta in maniera casuale tra
                  quella associate alla carta
    */
    public synchronized void GiocaACaso()
    {
        if(!mano.isEmpty())
        {
            //Sleep inserita per garantire un certo interleaving tra i thread della CPU e del Giocatore
            try 
            {
                Thread.sleep(750);
            } 
            catch (InterruptedException ex) 
            {
                Sessione.Halt();
            }
            
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
            
            System.out.println("CPU ha giocato: "+card.GetName()+"\n");
            
            if(card.IsMarked())
            {
                System.out.println(" Ed ha preso: ");
                
                System.out.print("\t");
                card.StampaSelezione(j);
                System.out.println("\n");
            }
        }
        else
        {
            Sessione.CambioTurno();
        }
    }

    /*
        @METHOD Observe
    
        @OVERVIEW Metodo attraverso cui la CPU osserva l'ambiente di gioco e formula
                  lo stato attuale da cui partirà la ricerca della carta migliore da
                  giocare
    */
    public synchronized Stato Observe()
    {
        Stato S = new Stato("ATTUALE",Table,mano,KB,points,turn,Sessione.Player.mano.size());
        
        return S;
    }
    
    /*
        @METHOD Plan
        
        @OVERVIEW Metodo attraverso cui la CPU decide qual è la carta migliore da giocare
                  attraverso l'algoritmo MiniMax con Potatura Alfa-Beta
    
        @PAR S : Stato attuale generato nel metodo Observe dalla stessa CPU
    */
    public synchronized void Plan(Stato S)
    {
        if(!mano.isEmpty())
        {   
            Intelligence.BuildTree(S,mano.size());
            Intelligence.Decide(mano.size());
        }
    }
    
    /*
        @METHOD Act
    
        @OVERVIEW Metodo attraverso cui la CPU agisce sulla sessione giocando la carta
                  stimata come migliore o, in assenza di prese o di carte da stimare,
                  gioca una carta e sceglie una presa in maniera casuale
    */
    public synchronized void Act()
    {
        if(Intelligence.NoCardsToTake() || mano.isEmpty())
        {
            System.out.println("IL COMPUTER NON PUO' FARE PRESE, VERRA' GIOCATA UNA CARTA A CASO");
            GiocaACaso();
        }
        else
        {
            int CardIndex = Intelligence.EstablishedCard;
            int PotentialIndex = Intelligence.EstablishedPotential;
            
            Carta C = mano.get(CardIndex);
            
            Sessione.GiocaCarta(this,PotentialIndex, C);
            
             System.out.println("CPU ha scelto di giocare: "+C.GetName()+"\n");
            
            if(C.IsMarked())
            {
                System.out.println(" Ed ha preso: ");
                
                System.out.print("\t");
                C.StampaSelezione(PotentialIndex);
                System.out.println("\n");
            }
        }
    }
    
    /*
        @METHOD PickHand
    
        @OVERVIEW Metodo attraverso cui la CPU pesca quattro carte dal mazzo
    
        @PAR Mazzo : ArrayList di oggetti di tipo Carta rappresentante il mazzo
                     da cui la CPU ed il giocatore pescano le carte
    */
    @Override
    public synchronized void PickHand(ArrayList<Carta> Mazzo)
    {
        Carta C;
        
        for(int i=0; i<3; i++)
        {
            C = Mazzo.get(1);
            mano.add(C);
            Mazzo.remove(1);  
        }
    }
    
    /*
        @METHOD AggiornaKB
    
        @OVERVIEW Metodo attraverso cui la CPU aggiorna la sua KnowledgeBase prendendo in input
                  le carte nel mazzo e le carte nella mano del giocatore
    
                  N.B: Per una simulazione realistica di un ragionamento intelligente al pari di quello
                  di un essere umano, la CPU non distinguerà comunque quali carte sono presenti nella mano
                  del giocatore e quali no, quindi farà inferenza sul tutte le carte che non sono nella sua mano
                  o sul tavolo ed effettuerà una stima al caso pessimo in cui il giocatore ha le carte più promettenti
                  come guadagno
        
        @PAR Mazzo : Mazzo della sessione
        @PAR ManoGiocatore : Carte nella mano del giocatore
    */
    public synchronized void AggiornaKB(ArrayList<Carta> Mazzo,ArrayList<Carta> ManoGiocatore)
    {
        KB.Aggiorna(Mazzo, ManoGiocatore);
    }
    
    /*
        @METHOD Testing
    
        @OVERVIEW Metodo che verifica se il test finale del gioco (FinalTest) è stato 
                  eseguito in modo tale che la CPU non interferisca con esso
    */
    public synchronized boolean Testing()
    {
        return !Sessione.Tested;
    }
    
    /*
        @METHOD run
    
        @OVERVIEW Metodo che implementa il ciclo di vita della CPU che viene eseguita secondo
                  un ciclo Percepisci - Pianifica - Agisci quando arriva il suo turno
    */
    @Override
    public void run()
    {
        while(active)
        {
            if(YourTurn() && !Testing())
            {
                try
                {
                    Thread.sleep(500);
                    Stato S = Observe();
                    Plan(S);
                    Act();
                }
                catch(InterruptedException e)
                {
                    System.out.println("LA CPU E' STATA INTERROTTA DURANTE LA SUA ESECUZIONE");
                }
            }
        }
        
        System.out.println("CPU TERMINA ESECUZIONE\n");
    }
    
    /*----FINE METODI RELATIVI AL CICLO DI VITA DELLA CPU----*/
    
}
