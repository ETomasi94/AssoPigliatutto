/*
ASSO PIGLIATUTTO
TESI DI LAUREA A.A 2020 - 2021

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
da parte della CPU ed un learner di rinforzo apprende a giocare per riuscire a 
suggerire la mossa migliore da effettuare al giocatore
*/
package assopigliatutto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    
    ExecutorService DecisionCapability; 
    
    Future<Double> Res;
    
    boolean Timeout;
    
    int CardsPlayed;
    
    int GreedyPlayed;
    
    public String LastCardReceived;
    
    public String LastCombinationReceived;

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
        
        DecisionCapability = Executors.newSingleThreadExecutor();
        
        Timeout = false;
        
        InterleavingTime = 500;
        
        SpeedMode = false;
        
        CardsPlayed = 0;
        
        GreedyPlayed = 0;
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
    }
    
    /**
     * @METHOD GiocaGreedy
     * 
     * @OVERVIEW Metodo che, nel caso la CPU non riesca, per lo scattare dell'apposito
     *           timeout o per l'impossibilità di effettuare una ricerca in uno spazio
     *           di stati efficiente, consente alla CPU di giocare in maniera greedy
     *           ovvero di selezionare la carta con la presa di valore massimo in virtù
     *           unicamente dello stato corrente
     */
    public synchronized void GiocaGreedy()
    {      
        InterleavingSleep();

        CardsPlayed++;
        
        GreedyPlayed++;
            
        if(!(mano.isEmpty()))
            {
                Carta C = null;

                int TakeIndex = ControllaPresaMassima();

                if(TakeIndex == -1)
            {
                GiocaACaso();
            }
            else
            {
                C = GetCard(TakeIndex);
                
                RegisterLastCards(TakeIndex,C.MaxPotential);

                Sessione.GiocaCarta(this,C.MaxPotential, C);
            }
        }
        else
        {
            Sessione.CambioTurno();
        }
    }
    
    /**
     * @METHOD ControllaPresaMassima
     * 
     * @OVERVIEW Metodo che, quando viene chiamato il metodo GiocaGreedy, controlla
     *           qual è l'indice della carta che consente la presa massima all'interno
     *           dello stato corrente
     * 
     * @return Result Inteero rappresentante l'indice della carta che consente la presa
     *         massima
     */
    public synchronized int ControllaPresaMassima()
    {
        double Max = 0.0;
        int Result = -1;
        
        if(!(mano.isEmpty()))
        {
            for(Carta C : mano)
            {
                if(C.IsMarked())
                {
                   double MaxTake = C.MaxGain;
                   
                   if(MaxTake > Max)
                   {
                       Max = MaxTake;
                       
                       Result = mano.indexOf(C);
                   }
                }
            }
        }
        
        return Result;
    }
    
    /**
     * @METHOD ResocontoGiocata
     * 
     * @OVERVIEW Metodo che stampa in output la mossa effettuata dalla CPU specificandone
     *           la carta giocata e le carte prese dalla combinazione scelta.
     * 
     * @param card Carta giocata dalla CPU
     * @param j Intero rappresentante la combinazione scelta attraverso cui la CPU
     *          effettua la rispettiva presa.
     */
    public synchronized void ResocontoGiocata(Carta card,int j)
    {
        System.out.println("CPU ha giocato: "+card.GetName()+"\n");
            
            if(card.IsMarked())
            {
                System.out.println(" Ed ha preso: ");
                
                System.out.print("\t");
                card.StampaSelezione(j);
                System.out.println("\n");
            }
    }
    
    /**
     * @METHOD SetInterleavingTime
     * 
     * @OVERVIEW Metodo che imposta il tempo di attesa della CPU prima di agire
     *           al fine di garantire un buon interleaving tra i processi
     * 
     * @param ms Intero lungo rappresentante il tempo di attesa in millisecondi
     */
    public void SetInterleavingTime(long ms)
    {
        InterleavingTime = ms;
    }
    
    /**
     * @METHOD TerminatedThinking
     * 
     * @OVERVIEW Metodo che verifica che l'AI abbia terminato la sua ricerca
     *           in uno spazio di stati della mossa migliore.
     * 
     * @return IsDone Valore booleano che indica se l'AI ha terminato la ricerca. 
     */
    public boolean TerminatedThinking()
    {
        return Res.isDone();
    }
    
    /**
     * @METHOD InterleavingSleep
     * 
     * @OVERVIEW Metodo che mette il computer in attesa tramite il metodo Thread.sleep
     *           per un tempo pari al suo tempo di interleaving prefissato.
     * 
     * @catch InterruptedException Se la CPU viene interrotta in maniera anomala
     *          durante l'attesa.
     */
    public synchronized void InterleavingSleep()
    {
        //Sleep inserita per garantire un certo interleaving tra i thread della CPU e del Giocatore
            try 
            {
                Thread.sleep(InterleavingTime);
            } 
            catch (InterruptedException ex) 
            {
                System.out.println("Si è verificato un errore durante la giocata della CPU. la sessione verrà conclusa");
                Sessione.Halt();
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
        Stato S = new Stato("ATTUALE",Table,mano,KB,points,turn,Sessione.Player.mano.size(),0,0);
        
        return S;
    }
    
    /*
        @METHOD Plan
        
        @OVERVIEW Metodo attraverso cui la CPU decide qual è la carta migliore da giocare
                  attraverso l'algoritmo MiniMax con Potatura Alfa-Beta
    
        @PAR S : Stato attuale generato nel metodo Observe dalla stessa CPU
    
        @catch TimeoutException allo scattare del timeout
    
        @catch InterruptedException se la CPU viene interrotta in maniera anomala
        @catch ExecutionException se avviene un errore durante l'esecuzione della CPU
    */
    public synchronized void Plan(Stato S)
    {
        if(!mano.isEmpty())
        {   
            Intelligence.SetState(S,mano.size());
            Res = DecisionCapability.submit(Intelligence);
            
            try 
            {
                Res.get(7L,TimeUnit.SECONDS);
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
                return;
            } 
            catch (TimeoutException ex) 
            {
                Timeout = true;
                System.out.println("TIMEOUT!");
                return;
            }
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
        if(Sessione.Tavolo.size() == 12)
        {
            GiocaGreedy();
        }
        else if(mano.isEmpty())
        {
            Sessione.Switch();
        }
        else
        {   

            if(Intelligence.NoCardsToTake() || Intelligence.EstablishedCard < 0 || Sessione.Tavolo.size() >= 12)
            {
                Timeout = false;

                GiocaGreedy();
            }
            else
            {
                try
                {
                    int CardIndex = Intelligence.EstablishedCard;

                    int PotentialIndex = Intelligence.EstablishedPotential;

                    RegisterLastCards(CardIndex,PotentialIndex);

                    Carta C = mano.get(CardIndex);

                    Sessione.GiocaCarta(this,PotentialIndex, C);
                }
                catch(NullPointerException ex)
                {
                    GiocaGreedy();
                }
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
    
    /**
     * @METHOD RegisterLastCards
     * 
     * @OVERVIEW Metodo di debug che memorizza l'indice dell'ultima carta e dell'ultima
     *           combinazione ricevuti dalla CPU
     * 
     * @param Index Intero rappresentante l'indice dell'ultima carta.
     * @param Comb  Intero rappresentante l'indice dell'ultima combinazione.
     */
    public synchronized void RegisterLastCards(int Index,int Comb)
    {
        LastCardReceived = String.valueOf(Index);
                
        LastCombinationReceived = String.valueOf(Comb);
    }
    
    
    
    
    /*
        @METHOD run
    
        @OVERVIEW Metodo che implementa il ciclo di vita della CPU che viene eseguita secondo
                  un ciclo Percepisci - Pianifica - Agisci quando arriva il suo turno.
    
        @catch InterruptedException se la CPU viene interrotta in maniera anomala.
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
                    Thread.sleep(InterleavingTime);
                    Sessione.RivalutaPotenziale();
                    Stato S = Observe();
                    Plan(S);
                    Act();
                }
                catch(InterruptedException e)
                {
                    System.out.println("LA CPU E' STATA INTERROTTA DURANTE LA SUA ESECUZIONE");
                    Sessione.Halt();
                }
            }
        }
        
        DecisionCapability.shutdown();
        
        System.out.println("CPU TERMINA ESECUZIONE\n");
    }
    
    /*----FINE METODI RELATIVI AL CICLO DI VITA DELLA CPU----*/
    
}
