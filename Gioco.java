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
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    @CLASS Gioco

    @OVERVIEW Classe che implementa le meccaniche del gioco "sottostante" l'interfaccia
*/
public final class Gioco extends Thread
{
     /*--------VARIABILI D'ISTANZA----------*/
    
    CPU CPU;
    
    Giocatore Player;
    
    ArrayList<Carta> Carte;//Mazzo di carte della sessione
    
    ArrayList<Carta> Tavolo;//Tavolo della sessione su cui vengono distribuite le carte
    
     /*
        Oggetto TesterDebugger che invoca metodi di testing utili a stabilire
        l'efficienza del gioco
    */
    TesterDebugger Test;
    
    /*
        Oggetto Board che implementa l'interfaccia grafica che consente la visualizzazione
        del gioco all'interno di una finestra
    */
    Board Interface;
    
    boolean Turn;//Variabile che indica se è il turno del giocatore o della CPU
    
    boolean Tested;//Variabile che indica se il gioco è stato testato o meno, in modo da consentire l'esecuzione del test finale (FinalTest)
    
    static boolean Halt;//Flag che indica se il gioco va arrestato o meno
               
    Random rnd;//Randomizzatore utile per gli elementi stocastici della partita
       
    /*
        Threadpool per l'esecuzione concorrente dei thread relativi agli avversari
        ovvero il giocatore e la CPU
    */
    ExecutorService Sfidanti = Executors.newFixedThreadPool(2);
     /*-------------------------------------------*/
    
    /*-------METODO COSTRUTTORE--*/
    
    /*
        @METHOD Gioco
           
        @OVERVIEW Metodo costruttore di una sessione di gioco
    
        @PAR I : Interfaccia (oggetto della classe Board) che consente la visualizzazione
                 del gioco in un contesto grafico differente dalla console di output
    
        @PAR DoTest : Booleano che indica se il metodo di testing FinalTest va chiamato o meno
    
        @RETURNS Sessione : Istanza della classe Gioco rappresentante la sessione di gioco corrente
    */
    public Gioco(Board I,boolean DoTest) 
    {              
        rnd = new Random();
        
        InizializzaMazzo();     
        InizializzaGiocatori();

        Halt = false;
        
        Interface = I;

        Test = new TesterDebugger(this); 
        
        Tested = !DoTest;
    }
    /*---------------------------*/
    
    /*----FINE METODO COSTRUTTORE----*/
    
    /*--------METODI DI INIZIALIZZAZIONE DELLE VARIABILI D'ISTANZA----*/
    
    /*
        @METHOD Inizializza Giocatori
    
        @OVERVIEW Metodo che crea una nuova istanza del giocatore, una nuova istanza
                  della CPU, le sottomette al threadpool in modo che siano pronte per l'esecuzione
                  e setta le principali variabili d'istanza dei due avversari in modo che possano interagire
                  con il gioco, in seguito fa sì che i due avversari peschino la loro prima mano
    */
    public void InizializzaGiocatori()
    {
        CPU = new CPU("CPU",true,this,Tavolo);
        Player = new Giocatore("Player",false,Tavolo);
        
        Sfidanti.submit(CPU);
        Sfidanti.submit(Player);

        boolean Mazziere = rnd.nextBoolean(); 

        CPU.SetMazziere(Mazziere);
        
        Player.SetMazziere(!Mazziere);

        CPU.PickHand(Carte);
        
        Player.PickHand(Carte);
        
        Turn = !Mazziere;
        
        CPU.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
    }
    
    /*
        @METHOD RestartPlayers
    
        @OVERVIEW Metodo che completa il reset degli avversari reimpostando le loro
                  variabili d'istanza principali secondo le variabili d'istanza
                  della sessione corrente (da usare per completare il metodo
                  
    */
    public void RestartPlayers()
    {
         boolean Mazziere = rnd.nextBoolean(); 

        CPU.SetMazziere(Mazziere);
        
        Player.SetMazziere(!Mazziere);

        CPU.PickHand(Carte);
        
        Player.PickHand(Carte);
        
        Turn = !Mazziere;
        
        CPU.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
    }
    
    /*
        @METHOD InizializzaMazzo
    
        @OVERVIEW Metodo che inizializza il mazzo di carte istanziando una carta
                  per ogni combinazione di semi e valori possibili ed inserendola
                  all'interno del mazzo
    */
    public ArrayList<Carta> InizializzaMazzo()
    {
        Carte = new ArrayList();
        
        for(int i=1; i<=10; i++)
        {
            Carte.add(new Carta('s',i));
            Carte.add(new Carta('b',i));
            Carte.add(new Carta('c',i));
            Carte.add(new Carta('d',i));
        }      
        
                
        //MISCHIAMO IL MAZZO
        Collections.shuffle(Carte);
        
        return Carte;
    }
    
    /*
        @METHOD InizializzaTavolo
    
        @OVERVIEW Metodo che inizializza il tavolo da gioco inserendovi all'interno
                  le prime quattro carte (quelle "in cima") al mazzo opportunamente
                  mescolato
    */
    public ArrayList<Carta> InizializzaTavolo()
    {
        ArrayList<Carta> T = new ArrayList();
        
        int i;
        
        for(i=0;i<4;i++)
        {   
            T.add(Carte.get(0));
            Carte.remove(0);
        }
        
        CPU.AssignTable(T);
        Player.AssignTable(T);
        
        Tavolo = T;
        
        Collections.sort(Tavolo);
        
        return T;    
    }
    /*-----------------------------------------------------------*/
    
    /*-----METODI DI GIOCO----*/
    /*
        @METHOD DistribuisciInTavolo
    
        @OVERVIEW Metodo che distribuisce sul tavolo quattro carte ad ogni
                  nuova mano successiva a quella iniziale
    */
    public void DistribuisciInTavolo()
    {
        int i = 0;
        
        while(i<4 && (!MazzoVuoto()))
        {
            Tavolo.add(Carte.get(0));
            Carte.remove(0);
            
            i++;
        }
    }
    
    /*
        @METHOD NuovoGioco
    
        @OVERVIEW Metodo che inizializza una nuova sessione di gioco
    */
    public void NuovoGioco()
    {                  
        InizializzaTavolo();   
        
        //Test finale
        if(!Tested)
        {
            FinalTest();
            
            Tested = true;
            
            RestartGame();
            
            System.out.println("VERIFICA RIAVVIO - CARTE PRESENTI NEL MAZZO: "+Carte.size());    
        }
        
        boolean Valida = ControlloValidità();

        if(Valida)
        {               
            Turn = ControllaTurno();     
        }
        else
        {
            InizializzaMazzo();
            
            Player.Reset();
            
            CPU.Reset();
            
            NuovoGioco();
        }
    }
    
    /*
        @METHOD Distribuisci
    
        @OVERVIEW Metodo che distribuisce le carte in tavola e nelle mani
                  dei giocatori ad ogni nuova mano diversa da quella iniziale
    */
    public synchronized void Distribuisci()
    {
        if(!MazzoVuoto())
        {
            CPU.PickHand(Carte);
            Player.PickHand(Carte);
            
            DistribuisciInTavolo();
            
            if(MazzoVuoto())
            {
                System.out.println("ULTIMA MANO");
            }

            Turn = ControllaTurno();
        }
        else
        {
            FinePartita();
        }
    }
    
    /*
        @METHOD FinePartita
    
        @OVERVIEW Metodo che chiamato alla fine di una partita controlla chi è stato
                  l'ultimo giocatore ad effettuare prese e sulla base di questo dichiara
                  il vincitore
    */
    public void FinePartita()
    {
        CheckUltimaPresa();
        
        DichiaraVincitore();
    }
    
    /*
        @METHOD FineMano
    
        @OVERVIEW Metodo che nel caso le mani del giocatore e della CPU siano vuote
                  distribuisce nuovamente le carte
    */
    public boolean FineMano()
    {
        if(CPU.GetMano().isEmpty() && Player.GetMano().isEmpty())
        {
            Distribuisci();
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /*
        @METHOD DichiaraVincitore
    
        @OVERVIEW Metodo che effettua il confronto dei punteggi dei due avversari
                  e dichiara il vincitore della partita
    */
    public void DichiaraVincitore()
    {
        String Winner = "";
        
        int GLOBALCPU = 0;
        int GLOBALPLAYER  = 0;
        
        System.out.println("----DICHIARAZIONE DEL GIOCATORE VINCITORE----");
        
        Punteggio CPUSCORE = DichiaraPunteggioCPU();
        Punteggio PLAYERSCORE = DichiaraPunteggioGiocatore();
        
        System.out.println("NUMERO DI CARTE PRESE IN TOTALE:");
        System.out.println("CPU: "+CPUSCORE.GetTotal()+"| GIOCATORE: "+PLAYERSCORE.GetTotal());
        
        if((CPUSCORE.GetTotal() > PLAYERSCORE.GetTotal()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetTotal() < PLAYERSCORE.GetTotal())
        {
            GLOBALPLAYER++;
        }
        
        System.out.println("NUMERO DI DENARI PRESI IN TOTALE:");
        System.out.println("CPU: "+CPUSCORE.GetDenari()+"| GIOCATORE: "+PLAYERSCORE.GetDenari());
        
        if((CPUSCORE.GetDenari() > PLAYERSCORE.GetDenari()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetDenari() < PLAYERSCORE.GetDenari())
        {
            GLOBALPLAYER++;
        }
        
        System.out.println("SETTEBELLO OTTENUTO: ");
        System.out.println("CPU: "+CPUSCORE.GetSettebello()+"| GIOCATORE: "+PLAYERSCORE.GetSettebello());
        if(CPUSCORE.GetSettebello())
        {
            GLOBALCPU++;
        }
        else if(PLAYERSCORE.GetSettebello())
        {
            GLOBALPLAYER++;
        }
        
        System.out.println("PUNTI DI PRIMIERA: ");
        System.out.println("CPU: "+CPUSCORE.GetPrimiera()+"| GIOCATORE: "+PLAYERSCORE.GetPrimiera());
        if((CPUSCORE.GetPrimiera() > PLAYERSCORE.GetPrimiera()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetPrimiera() < PLAYERSCORE.GetPrimiera())
        {
            GLOBALPLAYER++;
        }
        
        System.out.println("SCOPE OTTENUTE: ");
        System.out.println("CPU: "+CPUSCORE.GetScope()+"| GIOCATORE: "+PLAYERSCORE.GetScope());
        if((CPUSCORE.GetScope() > PLAYERSCORE.GetScope()))
        {
            GLOBALCPU++;
        }
        else if(CPUSCORE.GetScope() < PLAYERSCORE.GetScope())
        {
            GLOBALPLAYER++;
        }
        
        if(GLOBALPLAYER > GLOBALCPU)
        {
            Winner = Player.nome;
            System.out.println("VINCE IL GIOCATORE");
        }
        else if(GLOBALPLAYER < GLOBALCPU)
        {
            Winner = "CPU";
            System.out.println("VINCE LA CPU");
        }
        else
        {
            Winner = "DRAW";
            System.out.println("ABBIAMO UN PAREGGIO");
        }
        
        //Passaggio alla schermata dei punteggi
        Interface.GoToScores(PLAYERSCORE, CPUSCORE, Winner);
    }
    
    /*
        @METHOD ContorllaTurno
    
        @OVERVIEW Metodo chiamato al passaggio da un turno all'altro che aggiorna
                  il giocatore e la CPU sulla situazione del tavolo e consente ad 
                  entrambi di valutare bene la situazione
    */
    public boolean ControllaTurno()
    {
        Player.AssignTable(Tavolo);
        
        CPU.AssignTable(Tavolo);
        
        AggiornaKB();

        RivalutaPotenziale();
 
        if(CPU.YourTurn())
        {
            System.out.println("E' il turno della CPU\n");
            
            return true;
        }
        else
        {
            System.out.println("E' il turno del giocatore\n");
            
            VisualizzaTurno();
            
            return false;
        }
    }
    
    /*
        @METHOD UltimaPresa
    
        @OVERVIEW Metodo che avverte un giocatore che ha effettuato l'ultima presa
                  nella mano corrente
    
        @PAR Plr : Giocatore da avvisare dell'ultima presa effettuata
    */
    public void UltimaPresa(Giocatore Plr)
    {
        if(Plr.equals(Player))
        {
            Player.SetTaken();
            
            CPU.UnsetTaken();
        }
        else
        {
            Player.UnsetTaken();
            
            CPU.SetTaken();
        }
    }
    
    /*
        @METHOD CheckUltimaPresa
    
        @OVERVIEW Metodo che verifica chi è stato tra i due avversari ad aver
                  effettuato l'ultima presa consentendogli di prendere anche le 
                  carte rimaste sul tavolo
    */
    public void CheckUltimaPresa()
    {      
        if(Player.GetLastToTake())
        {
            Ripulisci(Player);
            
            System.out.println("Si aggiudica il resto del tavolo: "+Player.nome);
        }
        else
        {
            Ripulisci(CPU);
            
            System.out.println("Si aggiudica il resto del tavolo: "+CPU.nome);
        }
    }
    
    /*
        @METHOD Ripulisci
    
        @OVERVIEW Metodo che consente al giocatore dato in input di prendere le ultime
                  carte rimaste sul tavolo
    
        @PAR Plyr : Giocatore in input
    */
    public synchronized void Ripulisci(Giocatore Plyr)
    {
        for(Carta card : Tavolo)
        {

            Plyr.Score(card);
        }
        
        Tavolo.clear();
    }
    
    /*
        @METHOD CambioTurno
    
        @OVERVIEW Metodo che cambia il turno corrente ed avvisa entrambi
                  gli avversari del cambiamento
    */
    public void CambioTurno()
    {
        Turn = !Turn;
        
        CPU.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
    }
    
    /*
        @METHOD RivalutaPotenziale
    
        @OVERVIEW Metodo che ordina le carte presenti sul tavolo in ordine decrescente di
                  valore e ordina agli avversari di valutare la situazione
    */
    public synchronized void RivalutaPotenziale()
    {
        Collections.sort(Tavolo);
        
        Player.GetTotalPotential(Tavolo);
        
        CPU.GetTotalPotential(Tavolo);
    }
    
    /*
        @METHOD ControlloValidità
    
        @OVERVIEW Indica, secondo la "Regola dei tre re", se la prima mano è valida o meno
                  in modo da decidere se continuare la partita o ridistribuire le carte
        
        @RETURNS Validità : Booleano che rappresenta la validità della prima mano della partita
    
        ----NOTA----
        REGOLA DEI TRE RE: Affinché la prima mano della partita sia valida devono esserci meno
        di tre carte re (valore 10) sul tavolo
    */
    public boolean ControlloValidità()
    {
        boolean response = true;
        
        int NumeroDiRe = 0;
        
        for(Carta C : Tavolo)
        {
            if(C.GetValue() == 10)
            {
                NumeroDiRe++;
            }
            
            if(NumeroDiRe >= 3)
            {
                response = false;
                System.out.println("PARTITA NON VALIDA, LE CARTE VERRANNO DISTRIBUITE NUOVAMENTE\n");
                try 
                {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) 
                {
                    System.out.println("ERRORE DURANTE L'ESECUZIONE DEL PROGRAMMA\n");
                }
            }
        }
        
        return response;
    }
    
    /*
        @METHOD AggiornaKB
    
        @OVERVIEW Metodo che consente alla CPU di aggiornare la propria Knowledge Base
                  sulla base delle carte che non sono nè sul tavolo nè sulla propria mano
    */
    public void AggiornaKB()
    {
        CPU.AggiornaKB(Carte,Player.mano);
    }
    
    /*
        @METHOD GiocaCarta
    
        @OVERVIEW : Metodo che consente di giocare una carta sul tavolo ed effettuare,
                    se possibile, la presa desiderata
    
       @PAR Plr : Giocatore che effettua la giocata
       @PAR combinazione : Combinazione della presa desiderata (se esiste), settato a 0
            nel caso essa non esista
       @PAR C : Carta da giocare sul tavolo
    */
    public void GiocaCarta(Giocatore Plr,int combinazione,Carta C)
    {
        Plr.mano.remove(C);

        /*
          Nel caso la carta sia un asso, il giocatore può fare scopa se e soltanto se
          vi è un altro asso in campo ed è l'ultimo rimasto
        */
        boolean AssoDoppio = false;
        
            if(!C.IsMarked())
            {
                Tavolo.add(C);
            }
            else
            {
                ArrayList<Carta> Ptz = C.Potenziale.get(combinazione);

                for(Carta card : Ptz)
                {          
                    if(C.IsAnAce() && card.IsAnAce())
                    {
                        AssoDoppio = true;
                    }
                    
                    Tavolo.remove(card);

                    Plr.Score(card);
                }
                
                Plr.Score(C);
                
                UltimaPresa(Plr);
            }

            /*
                Se il tavolo rimane vuoto dopo la giocata, il giocatore fa scopa
            */
            if(Tavolo.isEmpty())
            {
                if(!C.IsAnAce() || (AssoDoppio))
                {
                    Plr.Scopa();
                    
                    UltimaPresa(Plr);
                }
            }
        boolean EndTurn = FineMano();

        CambioTurno();
        
        ControllaTurno();
        
        Interface.Display();
    }
   
    /*-----FINE METODI DI GIOCO----*/

    /*-------METODI DI DEBUG----*/
    
    /*
        @METHOD SgomberaTavolo
    
        @OVERVIEW Metodo che elimina tutte le carte dal tavolo di gioco
    */
    public void SgomberaTavolo()
    {
        Tavolo.clear();
    }
    
    /*
        @METHOD GettaMani
    
        @OVERVIEW Metodo che ordina ai giocatori di gettare tutte le carte
                  presenti all'interno delle proprie mani
    */
    public void GettaMani()
    {
        CPU.HandDrop();
        Player.HandDrop();
    }
    
    /*
        @METHOD ManiScoperte
    
        @OVERVIEW Metodo che in sequenza visualizza entrambi le mani dei giocatori
    */
    public void ManiScoperte()
    {
        CPU.VisualizzaMano();
        Player.VisualizzaMano();
    }
    
    /*
        @METHOD ContaBastoni
    
        @OVERVIEW Metodo che conta il numero di carte di bastoni all'interno
                  di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
    
       @RETURNS A : Risultato del conteggio
    */
    public int ContaBastoni(ArrayList<Carta> CRDS)
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
    
        @OVERVIEW Metodo che conta il numero di carte di spade all'interno
                  di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
    
        @RETURNS A : Risultato del conteggio
    */
    public int ContaSpade(ArrayList<Carta> CRDS)
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
    
        @OVERVIEW Metodo che conta il numero di carte di coppe all'interno
                  di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
    
       @RETURNS A : Risultato del conteggio    
    */
    public int ContaCoppe(ArrayList<Carta> CRDS)
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
    
        @OVERVIEW Metodo che conta il numero di carte di denari all'interno
                  di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
    
     @RETURNS A : Risultato del conteggio    
    */  
    public int ContaDenari(ArrayList<Carta> CRDS)
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
    
        @PAR CRDS : ArrayList di oggetti di tipo carta rappresentante il set di carte in questione
    
        @RETURNS ContainsSB : Risultato della verifica
    */
    public boolean CercaSetteBello(ArrayList<Carta> CRDS)
    {
        Carta C = new Carta('d',7);
        
        return CRDS.contains(C);
    }
    
    /*
        @METHOD ContaValore
    
        @OVERVIEW Metodo che conta il numero di carte di un determinato valore in input
                  all'interno di un ArrayList di oggetti di tipo carta
    
       @PAR CRDS : ArrayList di oggetti di tipo carta su cui effettuare il conteggio
       @PAR v1 : Valore delle carte da includere nel conteggio
    
     @RETURNS A : Risultato del conteggio    
    */ 
    public int ContaValore(ArrayList<Carta> CRDS, int vl)
    {
        int a = 0;
        
        for(Carta C : CRDS)
        {
            if(C.valore == vl)
            {
                a++;
            }
        }
        
        return a;
    }
    
    /*
        @METHOD VerificaKB
    
        @OVERVIEW Metodo che verifica le proprietà principali della Knowledge Base
                  della CPU, stampandole in output
    
       @PAR Computer : CPU di cui esaminare la KnowledgeBase
    */
    public void VerificaKB(CPU Computer)
    {     
        int Confronto = (ContaBastoni(Carte) + ContaBastoni(Player.mano));
        
        System.out.println("CHECK BASTONI: "+Confronto);
        
        Confronto = (ContaCoppe(Carte) + ContaCoppe(Player.mano));
        
        System.out.println("CHECK COPPE: "+Confronto);
        
        Confronto = (ContaSpade(Carte) + ContaSpade(Player.mano));
        
        System.out.println("CHECK SPADE: "+Confronto);
        
        Confronto = (ContaDenari(Carte) + ContaDenari(Player.mano));
        
        System.out.println("CHECK DENARI: "+Confronto);
        
        boolean Verifica = (Computer.KB.Settebello) == (CercaSetteBello(Carte) || CercaSetteBello(Player.mano));
        
        System.out.println("CHECK SETTEBELLO: "+Verifica);
    }
    /*-------FINE METODI DI DEBUG----*/
    
     /*--------------METODI GETTERS E SETTERS-----------------*/
    
    /*
        @METHOD GetTavolo
    
        @OVERVIEW Metodo che restituisce il tavolo da gioco
    
        @RETURNS Table : ArrayList di oggetti di tipo Carta rappresentante il Tavolo da gioco
    */
    public ArrayList<Carta> GetTavolo()
    {
        return Tavolo;
    }

    /*
        @METHOD MazzoVuoto
    
        @OVERVIEW Metodo che verifica se il mazzo è vuoto o meno
    
        @RETURNS Res : Booleano che indica se il mazzo è vuoto o meno
    */
    public boolean MazzoVuoto()
    {
        return (Carte.isEmpty());
    }
    
    /*
        @METHOD CarteRimaste
    
        @OVERVIEW Metodo che indica quante carte sono rimaste nel mazzo
    
        @RETURNS Res : Intero rappresentante il numero di carte rimaste nel mazzo
    */
    public int CarteRimaste()
    {
        return Carte.size();
    }
    
    /*
        @METHOD GetCPU
    
        @OVERVIEW Metodo che restituisce in output l'istanza della CPU in esecuzione
                  all'interno del gioco
    
        @RETURNS Computer : Istanza della CPU in esecuzione all'interno del gioco
    */
    public Giocatore GetCPU()
    {
        return this.CPU;
    }
    
    /*
        @METHOD GetPlayer
    
        @OVERVIEW Metodo che restituisce in output l'istanza del giocatore in esecuzione
                  all'interno del gioco
    
        @RETURNS Computer : Istanza del gicoatore in esecuzione all'interno del gioco
    */
    public Giocatore GetPlayer()
    {
        return this.Player;
    }
    
    /*
        @METHOD DichiaraPunteggioCPU
    
        @OVERVIEW Metoro che restituisce il punteggio della CPU
    
        @RETURNS CPUPoints : Punteggio della CPU
    */
      public Punteggio DichiaraPunteggioCPU()
    {
        return CPU.GetPoints();
    }
    
      /*
        @METHOD DichiaraPunteggioGiocatore
    
        @OVERVIEW Metoro che restituisce il punteggio del Giocatore
    
        @RETURNS PlayerPoints : Punteggio del Giocatore
    */
    public Punteggio DichiaraPunteggioGiocatore()
    {
        return Player.GetPoints();
    }
    /*------------------------------------------------------------------*/
    
    /*------METODI DEL CICLO DI VITA DEL THREAD---------*/
    
    /*
        @METHOD Halt
    
        @OVERVIEW Metodo che pone fine alla partita ed arresta il gioco ed 
                  i thread dei giocatori dopo aver settato il flag di arresto
                  (variabile Halt) a true.
                  Il metodo successivamente ordina all'interfaccia di chiudersi
    */
    public void Halt()
    {
        System.out.println("Fine della partita\n");
        
        Halt = true;
        
        Player.ShutDown();
        CPU.ShutDown();
        
        Sfidanti.shutdownNow();     
        
        Interface.Exit();
    }
    
    /*
        @METHOD IsHalted
    
        @OVERVIEW Metodo che verifica se il gioco è in arresto restituendo l'opportuna
                  variabile d'istanza (Halt)
    
       @RETURNS Halt : Variabile d'istanza che indica se il gioco è in arresto o meno
    */
    public static boolean IsHalted()
    {
        return Halt;
    }
    /*-----------------------------------------------------*/
    
    /*
        @METHOD VisualizzaTurno
    
        @OVERVIEW Metodo che visualizza durante il turno del giocatore le carte nella sua mano
                  e le carte sul tavolo sotto forma di caratteri su console
    */
    public void VisualizzaTurno()
    {
        System.out.println("===================================================");
        System.out.println("----------------TAVOLO-----------------------------");
        for(Carta C : Tavolo)
        {
            System.out.print("| "+C.seme+"-"+C.valore+" |-");
        }
        System.out.println("\n");
        
        System.out.println("------------MANO DEL GIOCATORE----------------------");
        for(Carta C : Player.GetMano())
        {
              System.out.print("| "+C.seme+"-"+C.valore+" |-");
        }
        System.out.println("\n");
        System.out.println("===================================================");
        System.out.println("\n");
    }
    
    /*
        @METHOD StandOff
    
        @OVERVIEW Metodo che verifica se indica se viene riscontrata una delle prime due
                  condizioni di stallo
                   
                  CONDIZIONE DI STALLO 1 : Turno del giocatore e mano del giocatore vuota
                  CONDIZIONE DI STALLO 2 : Turno della CPU e mano della CPU vuota
    
        @RETURNS InStandoff : Valore booleano che indica se una delle due condizioni di cui sopra
                 si è verificata o meno
    */
    public boolean StandOff()
    {
        boolean Condition1 = ((Player.mano.isEmpty()) && (Player.YourTurn()));
        boolean Condition2 = ((CPU.mano.isEmpty()) && (CPU.YourTurn()));
        
        return (Condition1 || Condition2);
    }
    
    /*
        @METHOD NoCardForNoOne
    
        @OVERVIEW Metodo che verifica se viene riscontrata la terza condizione di stallo
    
                 CONDIZIONE DI STALLO 3 : Le mani di entrambi i giocatori sono vuote
    
        @RETURNS NoOneHasCards : Valore booleano che indica se la condizione di cui sopra
                  si è verificata o meno
    */
    public boolean NoCardForNoOne()
    {
        boolean Condition1 = ((Player.mano.isEmpty()) && (CPU.mano.isEmpty()));
        
        return Condition1;
    }
    
    /*
        @METHOD FinalTest
    
        @OVERVIEW Metodo che prepara l'oggetto TesterDebugger ad effettuare il test
                  finale prima dell'esecuzione del gioco, consistente nell'esecuzione
                  sequenziale del test per casi della visita DFS e del test per casi
                  della generazione dell'albero di gioco e della visita mediante
                  algoritmo MiniMax
    */
    public void FinalTest()
    {        
        StopPlayers();
        
        Tavolo = InizializzaTavolo();
        
        Test.Set(Tavolo, Carte);
        
        Test.DFSTest();
        
        Test.AITest();
    }
    
    /*
        @METHOD StopPlayers
    
        @OVERVIEW Metodo che comanda ai due avversari di fermarsi settando l'opportuna 
                  variabile d'istanza della classe Giocatore (turn) a false.
    
                  Utilizzato per impedire che essi interferiscano con il test finale, dove
                  le loro mosse sono stabilite all'interno dei metodi di testing finale
    */
    public void StopPlayers()
    {
        Player.StopTurn();
        CPU.StopTurn();
    }
    
    /*
        @METHOD RestarGame
    
        @OVERVIEW Metodo che re-inizializza il gioco  da zero
                  Utilizzato dopo l'esecuzione del test finale
    */
    public void RestartGame()
    {
        Player.Reset();
        CPU.Reset();
        
        InizializzaMazzo();     
        InizializzaTavolo();
        RestartPlayers();
    }
    
    /*
        @METHOD GoPlayers
    
        @OVERVIEW Metodo che indica ai due avversari che è possibile riprendere l'esecuzione
                  delle loro mosse settando l'opportuna  variabile d'istanza della classe Giocatore (turn) a false.
    
                  Utilizzato dopo l'esecuzione del test finale
    */
    public void GoPlayers()
    {
        boolean Mazziere = rnd.nextBoolean();
        
        Turn = !Mazziere;
        
        CPU.AssegnaTurno(Turn);
        
        Player.AssegnaTurno(!Turn);
    }
     
    /*--------------------------------------*/
    
    /*----METODI DEL CICLO DI VITA DEL THREAD----*/
    
    /*
        @METHOD run
    
        @OVERVIEW Metodo che implementa il ciclo di vita principale dell'istanza
                  della classe gioco
    */
    @Override
     public void run()
     {
         while(!Halt)
         {
             if(StandOff())
             {
                 CambioTurno();
             }
             
             if(NoCardForNoOne())
             {
                 Distribuisci();
             }
         }
     }
}
