/*
ASSO PIGLIATUTTO
PROGETTO DI ESPERIENZE DI PROGRAMMAZIONE A.A 2019-2020

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

/*
    @CLASS BOARD
    
    @OVERVIEW Classe che implementa l'interfaccia grafica della schermata principale di gioco
*/
public final class Board extends javax.swing.JFrame 
{
    /*----VARIABILI D'ISTANZA----*/
    
    Gioco Sessione;//Gioco da rappresentare nel contesto grafico
    
    Giocatore CPU;
    Giocatore Player;
    
    ArrayList<Slot> PlayerHand;//Mano del giocatore
    ArrayList<Slot> CPUHand;//Mano della CPU
    ArrayList<Slot> CardsOnTable;//Tavolo dove distribuire le carte
    
    /*
        Lista di interruttori per scegliere tra una delle possibili combinazioni
        rappresentanti ognuna una possibile presa effettuabile con una determinata 
        carta selezionata
    */
    ArrayList<JToggleButton> SwitchList = new ArrayList();

    /*
        Oggetto TesterDebugger che invoca metodi di testing utili a stabilire
        l'efficienza del gioco
    */
    TesterDebugger Test = new TesterDebugger(this);
    
    /*
        Carta selezionata ed analizzata al fine di ricavare tutte le possibili
        prese in accordo alle carte presenti sul tavolo
    */
    Carta AnalyzedCard;

    /*
        Combinazione rappresentante una possibile presa che stiamo correntemente
        esaminando (le carte corrispondenti sono evidenziate)
    */
    int ChoosenCardCombination;
    
    /*Colori con ci contrassegnare le 32 combinazioni, ne viene usato uno ogni tre
      all'incirca
    */
    Color C1 = new Color(255,0,0);//ROSSO
    Color C2 = new Color(0,255,0);//VERDE
    Color C3 = new Color(0,0,255);//BLU
    Color C4 = new Color(0,0,0);//BIANCO
    Color C5 = new Color(255,255,255);//NERO
    Color C6 = new Color(255,100,0);//ARANCIONE
    Color C7 = new Color(255,0,100);//MAGENTA
    Color C8 = new Color(0,255,100);//VERDE CHIARO
    Color C9 = new Color(100,0,255);//VIOLA
    Color C10 = new Color(212,175,55); //ORO METALIZZATO
    Color C11 = new Color(170,169,173); //ARGENTO METALLIZZATO
    Color C12 = new Color(220,220,220).darker();//GRIGIO SCURO
    
    Color DefaultColor = new Color(240,240,240);//COLORE DI DEFAULT, NON CONTRASSEGNA, A RIGOR DI LOGICA, NESSUNA COMBINAZIONE
    
        
    /*----FINE VARIABILI D'ISTANZA----*/
    
    /*----METODO COSTRUTTORE----*/
    
    /*
        @METHOD Board
    
        @OVERVIEW Metodo costruttore che inizializza l'interfaccia grafica per 
                  consentire all'utente di iniziare una nuova partita
    
    */
    public Board() 
    {
        /*
            Inizializzazione delle componenti grafiche dell'interfaccia
        */
        initComponents();
    }
    /*----FINE METODO COSTRUTTORE----*/
    
    /*----METODI DI INIZIALIZZAZIONE DELLA BOARD----*/
    
    /*
        @METHOD NewGame
        
        @OVERVIEW Metodo che inizializza una nuova sessione di gioco e ne vistualizza gli elementi a schermo
    */
    public void NewGame(boolean DoTest)
    {    /*
            Il gioco "sottostante" l'interfaccia inizia dichiarando una nuova partita
        */
        Sessione = new Gioco(this,DoTest);
                  
        /*
            Ricavo dei giocatori (istanziati come thread all'interno della classe gioco)
        */
        CPU = Sessione.GetCPU();
        Player = Sessione.GetPlayer();
        
        AnalyzedCard = null;
        
        ChoosenCardCombination = 0;
        
        /*
            Inizializzazione delle slot, ovvero delle etichette (JLabel) associate alle mani ed al tavolo
            che contentono le informazioni e l'immagine della carta rappresentate
        */
        InizializzaSlot();
        
        UndoButton.setVisible(false);
        OkButton.setVisible(false);
     
       Random r = new Random();
       
       Sessione.NuovoGioco();
       
       Display();
    }

    
    /*
        @METHOD InizializzaSlot
    
        @OVERVIEW Metodo che predispone gli slot dell'interfaccia all'utilizzo, raccogliendoli in base a cosa rappresentano
                  in quattro arraylist diversi per facilitarne la modifica in contemporanea con altri slot
    */
    public void InizializzaSlot()
    {          
        PlayerHand = new ArrayList();
        CPUHand = new ArrayList();
        CardsOnTable = new ArrayList();
        
        //Slot relativi alla mano del giocatore
        PlayerHand.add(new Slot(PlayerLabel1));
        PlayerHand.add(new Slot(PlayerLabel2));
        PlayerHand.add(new Slot(PlayerLabel3));
        
        //Slot relativi alla mano della CPU
        CPUHand.add(new Slot(OpponentLabel1));
        CPUHand.add(new Slot(OpponentLabel2));
        CPUHand.add(new Slot(OpponentLabel3));
        
        //Slot relativi alle carte in tavola
        CardsOnTable.add(new Slot(TableCard1));
        CardsOnTable.add(new Slot(TableCard2));
        CardsOnTable.add(new Slot(TableCard3));
        CardsOnTable.add(new Slot(TableCard4));
        CardsOnTable.add(new Slot(TableCard5));
        CardsOnTable.add(new Slot(TableCard6));
        CardsOnTable.add(new Slot(TableCard7));
        CardsOnTable.add(new Slot(TableCard8));
        CardsOnTable.add(new Slot(TableCard9));
        CardsOnTable.add(new Slot(TableCard10));
        CardsOnTable.add(new Slot(TableCard11));
        CardsOnTable.add(new Slot(TableCard12));
        
        //Slot relativi agli interruttori di selezione combinazione
        SwitchList.add(Combination1);
        SwitchList.add(Combination2);
        SwitchList.add(Combination3);
        SwitchList.add(Combination4);
        SwitchList.add(Combination5);
        SwitchList.add(Combination6);
        SwitchList.add(Combination7);
        SwitchList.add(Combination8);
        SwitchList.add(Combination9);
        SwitchList.add(Combination10);
        SwitchList.add(Combination11);
        SwitchList.add(Combination12);
        SwitchList.add(Combination13);
        SwitchList.add(Combination14);
        SwitchList.add(Combination15);
        SwitchList.add(Combination16);
        SwitchList.add(Combination17);
        SwitchList.add(Combination18);
        SwitchList.add(Combination19);
        SwitchList.add(Combination20);
        SwitchList.add(Combination21);
        SwitchList.add(Combination22);
        SwitchList.add(Combination23);
        SwitchList.add(Combination24);
        SwitchList.add(Combination25);
        SwitchList.add(Combination26);
        SwitchList.add(Combination27);
        SwitchList.add(Combination28);
        SwitchList.add(Combination29);
        SwitchList.add(Combination30);
        SwitchList.add(Combination31);
        SwitchList.add(Combination32);
        SwitchList.add(Combination33);
        SwitchList.add(Combination34);
        SwitchList.add(Combination35);
        SwitchList.add(Combination36);
        SwitchList.add(Combination37);
        SwitchList.add(Combination38);
        
        //Se non sono utili, gli interruttori vengono nascosti al giocatore
        HideSwitches();
    }
    
    /*----FINE METODI DI INIZIALIZZAZIONE DELLA BOARD----*/
    
    /*----METODI DI VISUALIZZAZIONE DEL GIOCO----*/
    
    /*
        @METHOD Display
    
        @OVERVIEW Metodo che raccoglie informazioni relative al gioco e ne visualizza
                  gli elementi correnti su schermo, è utilizzato per aggiornare l'interfaccia
    */
    public synchronized void Display()
    {
        VisualizzaMazzo();
        VisualizzaManoCPU();
        VisualizzaManoGiocatore();
        VisualizzaTavolo();
        VisualizzaTurno();
    }
    
    /*
        @METHOD HideSwitches
    
        @OVERVIEW Metodo che nasconde gli interruttori relativi alla selezione di una combinazione
                  quando questi non sono utili
    */
    private synchronized void HideSwitches()
    {
        for(JToggleButton Switch : SwitchList)
        {
            Switch.setVisible(false);
        }
    }
    
    /*
        @METHOD RevealSwitches
    
        @OVERVIEW Metodo che rivela gli interruttori relativi alla selezione di una combinazione
                  quando questi sono richiesti
    */
    private synchronized void RevealSwitches()
    {
        for(JToggleButton Switch : SwitchList)
        {
            Switch.setVisible(true);
        }
    }
    
    /*
        @METHOD VisualizzaTurno
    
        @OVERVIEW Metodo che visualizza su schermo una stringa che indica se è il turno
                  del giocatore o della CPU per giocare
    */
    private void VisualizzaTurno()
    {
        if(Sessione.Turn)
        {
            CPUTurnLabel.setText("Sta pensando");
            PlayerTurnLabel.setText("");
        }
        else
        {
            CPUTurnLabel.setText("");
            PlayerTurnLabel.setText("E' il tuo turno");
        }
    }
    
    /*
        @METHOD VisualizzaCarta
    
        @OVERVIEW Metodo che imposta come icona di una slot in input l'immagine di una specifica carta
                  ottenuta decodificando un intero (ovvero un codice) in input
    */
    private void VisualizzaCarta(int codice,JLabel Slot)
    {
        /*Ricavo dell'immagine nella cartella CardSkins tramite codice*/
        ImageIcon ic = new javax.swing.ImageIcon(getClass().getResource("/CardSkins/"+codice+".png"));
        
        /*Trasformazione e ridimensionamento dell'immagine in input*/
        Image image = ic.getImage();
        Image newimg = image.getScaledInstance(100, 160,  java.awt.Image.SCALE_SMOOTH);
        ic = new ImageIcon(newimg);
        
        Slot.setIcon(ic);
    }
    /*
        @METHOD VisualizzaTavolo
    
        @OVERVIEW Metodo che scansiona le carte presenti sul tavolo di gioco e le assegna alle relative 
                  slot per consentirne la visualizzazione su schermo
    */
    public synchronized void VisualizzaTavolo()
    {
        ArrayList<Carta> Tavolo = Sessione.GetTavolo();

        int i;
        
        for(i=0; i<Tavolo.size(); i++)
        {
            CardsOnTable.get(i).AssignCard(Tavolo.get(i));
        }
        
        for(i=Tavolo.size();i<CardsOnTable.size();i++)
        {
            CardsOnTable.get(i).SetEmpty();
        }
        
    }
    
    /*
        @METHOD VisualizzaTavolo
    
        @OVERVIEW Metodo che scansiona le carte presenti sul tavolo di gioco e le assegna alle relative 
                  slot per consentirne la visualizzazione su schermo
    */
    public void VisualizzaMazzo()
    {
      if(!Sessione.MazzoVuoto())
      {
        if(CPU.mazziere)
        {
            VisualizzaCarta(41,DeckLabelCPU);
        }
        else
        {
            VisualizzaCarta(41,DeckLabelPlayer);
        }
      }
    }
    
    /*
        @METHOD VisualizzaManoGiocatore
    
        @OVERVIEW Metodo che scansiona le carte presenti nella mano del giocatore e le assegna alle relative 
                  slot per consentirne la visualizzazione su schermo
    */
    public void VisualizzaManoGiocatore()
    {
        ArrayList<Carta> Mano = Player.GetMano();
        
        int i;
        
        for(i=0; i<Mano.size();i++)
        {
            PlayerHand.get(i).AssignCard(Mano.get(i));
        }
        
        for(i=Mano.size();i<PlayerHand.size();i++)
        {
            PlayerHand.get(i).SetEmpty();
        }
    }
    
    /*
        @METHOD VisualizzaManoCPU
    
        @OVERVIEW Metodo che scansiona le carte presenti nella mano della CPU e le assegna alle relative 
                  slot per consentirne la visualizzazione su schermo
    */
    public void VisualizzaManoCPU()
    {
       ArrayList<Carta> Mano = CPU.GetMano();
        
       int i = 0; 
        
       for(i=0; i<Mano.size();i++)
       {
           Carta C = Mano.get(i);
           CPUHand.get(i).AssignCard(C);
           CPUHand.get(i).Hide();
       }
       
       for(i=Mano.size();i<CPUHand.size();i++)
        {
            CPUHand.get(i).SetEmpty();
        }
    }

    /*
        @METHOD HighlightCards
      
        @OVERVIEW Metodo che presa in input una carta, un intero corrispondente ad una combinazione di carte
                  che è possibie prendere giocando la carta ed un colore evidenzia gli interruttori corrispondenti 
                  alla combinazione scelta in modo da consentirne l'individuazione
    */
    private synchronized void HighlightCards(Carta card,int M,Color Cl)
    {
        ConcurrentSkipListMap<Integer,ArrayList<Carta>> Potenziale = card.Potenziale;
        
        if(Potenziale.containsKey(M))
        {
            ArrayList<Carta> Selected = Potenziale.get(M);            
                
            UnColorAll();
            
            for(Carta C : Selected)
            {
                int i = Sessione.Tavolo.indexOf(C);
                JToggleButton J = SwitchList.get(i);

                J.setBackground(Cl);
            }
            
            ChoosenCardCombination = M;
        }    
        
        /*A questo punto, i bottoni "Gioca" e "Torna" diventano visibili per
          consentire all'utente di scegliere se giocare o meno la carta selezionata
          prendendo le carte relative alla combinazione selezionata
        */
        SetChoiceButtonsVisible();
    }
    
    /*
        @METHOD UnColorAll
    
        @OVERVIEW Metodo che setta tutti gli interruttori relativi alle combinazioni
                  al colore di default quando l'utente non sta scegliendo una carta
                  ed una combinazione da giocare
    */
    private void UnColorAll()
    {
        for(JToggleButton JTB : SwitchList)
        {
            JTB.setBackground(DefaultColor);
        }
    }
         
    /*
        @METHOD IsSelected
    
        @OVERVIEW Metodo che verifica se vi è una carta in analisi e se un determinato
                  interruttore è associato ad una combinazione di carte che è possibile
                  prendere attraverso quella carta
    
        @PAR n : Numero associato alla combinazione della carta che interessa al giocatore
        @PAR JTB : Interruttore in questione
    
        @RETURNS Selected : Booleano che ci indica se stiamo analizzando una carta
                            (AnalyzedCard != null) e se l'interruttore è effettivamente
                             associato ad una combinazione per la carta (!CLR.equals(DefaultColor))
                             controllando se ha assunto un colore diverso da quello di defalt
    */
   private boolean IsSelected(int n,JToggleButton JTB)
   {
     if((AnalyzedCard != null) && AnalyzedCard.IsMarked())
     {
       if(AnalyzedCard.HasPotential(n))
       {
        Color CLR = JTB.getBackground();
       
        return !CLR.equals(DefaultColor) && (AnalyzedCard!=null);
       }
       else
       {
           UnClickAll();
           return false;
       }
     }
     else
     {
         UnClickAll();
         return false;
     }
   }
       
   /*
        @METHOD SetChoiceButtonsVisible
   
        @OVERVIEW Metodo che rende visibili i bottoni OkButton ("Gioca") ed
                  UndoButton ("Torna") quando questi sono necessari per consentire
                  all'utente di giocare la carta scelta con la combinazione desiderata
   */
   private void SetChoiceButtonsVisible()
   {
       UndoButton.setVisible(true);
       OkButton.setVisible(true);
   }
   
    /*
        @METHOD UnsetChoiceButtonsVisible
   
        @OVERVIEW Metodo che rende invisibili i bottoni OkButton ("Gioca") ed
                  UndoButton ("Torna") quando questi non sono necessari
   */
   private void UnsetChoiceButtons()
   {
       ChoosenCardCombination = 0;
       
       UndoButton.setVisible(false);
       OkButton.setVisible(false);
   }

   /*
        @METHOD UnClickAll()
   
        @OVERVIEW Metodo che deseleziona tutti gli interruttori non necessari
   */
    private void UnClickAll()
    {
        for(JToggleButton T : SwitchList)
        {
            if(T.isSelected())
            {
                T.doClick();
            }
            
            T.setBackground(DefaultColor);
        }
    }
    
    /*----FINE METODI DI VISUALIZZAZIONE DEL GIOCO----*/
    
    /*----METODI RELATIVI ALLA SELEZIONE DELLE CARTE----*/
    
    /*
        @METHOD AnalyzeCard
    
        @OVERVIEW Metodo che, data la carta scelta in input, colora tutti gli
                  interruttori di un colore diverso (utilizzando sequenzialmente 
                  e ciclicamente i 12 colori disponibili per tutte le 38 combinazioni
                  possibili) a seconda delle prese possibili con quella carta.
                  Se non vi è una scelta associata all'interruttore, quest'ultimo
                  assume il colore di default (e quindi non è un interruttore interessante)
    
                  N.B: Gli interruttori sono associati ognuno alla presa di uguale indice 
                       che è possibile effettuare con la carta
    
                  ES: INTERRUTTORE 1 - |Contrassegna| ----> PRESA 1
    
        @PAR Selected : Carta selezionata dall'utente
    */
       public synchronized void AnalyzeCard(Carta Selected)
    {
        if(!Selected.IsMarked())
        {
            SetChoiceButtonsVisible();
        }
        
        if(Selected.HasPotential(1))
        {
            Combination1.setBackground(C1.darker());
        }
        else
        {
            Combination1.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(2))
        {
            Combination2.setBackground(C2.darker());
        }
        else
        {
            Combination2.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(3))
        {
            Combination3.setBackground(C3.darker());
        }
        else
        {
            Combination3.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(4))
        {
            Combination4.setBackground(C4.darker());
        }
        else
        {
            Combination4.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(5))
        {
            Combination5.setBackground(C5.darker());
        }
        else
        {
            Combination5.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(6))
        {
            Combination6.setBackground(C6.darker()); 
        }
        else
        {
            Combination6.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(7))
        {
            Combination7.setBackground(C7.darker()); 
        }
        else
        {
            Combination7.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(8))
        {
            Combination8.setBackground(C8.darker());
        }
        else
        {
            Combination8.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(9))
        {
            Combination9.setBackground(C9.darker());
        }
        else
        {
            Combination9.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(10))
        {
            Combination10.setBackground(C10.darker());
        }
        else
        {
            Combination10.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(11))
        {
           Combination11.setBackground(C11.darker());
        }
        else
        {
            Combination11.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(12))
        {
            Combination12.setBackground(C12.brighter());
        }
        else
        {
            Combination12.setBackground(DefaultColor);
        }
        if(Selected.HasPotential(13))
        {
            Combination13.setBackground(C1.darker());
        }
        else
        {
            Combination13.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(14))
        {
            Combination14.setBackground(C2.darker());
        }
        else
        {
            Combination14.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(15))
        {
            Combination15.setBackground(C3.darker());
        }
        else
        {
            Combination15.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(16))
        {
            Combination16.setBackground(C4.darker()); 
        }
        else
        {
            Combination16.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(17))
        {
            Combination17.setBackground(C5.darker()); 
        }
        else
        {
            Combination17.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(18))
        {
            Combination18.setBackground(C6.darker());
        }
        else
        {
            Combination18.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(19))
        {
            Combination19.setBackground(C7.darker());
        }
        else
        {
            Combination19.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(20))
        {
            Combination20.setBackground(C8.darker());
        }
        else
        {
            Combination20.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(21))
        {
           Combination21.setBackground(C9.darker());
        }
        else
        {
            Combination21.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(22))
        {
            Combination22.setBackground(C10.darker());
        }
        else
        {
            Combination22.setBackground(DefaultColor);
        }
          if(Selected.HasPotential(23))
        {
            Combination23.setBackground(C11.darker());
        }
        else
        {
            Combination23.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(24))
        {
            Combination24.setBackground(C12.brighter());
        }
        else
        {
            Combination24.setBackground(DefaultColor);
        }
        if(Selected.HasPotential(25))
        {
            Combination25.setBackground(C1.darker());
        }
        else
        {
            Combination25.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(26))
        {
            Combination26.setBackground(C2.darker()); 
        }
        else
        {
            Combination26.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(27))
        {
            Combination27.setBackground(C3.darker()); 
        }
        else
        {
            Combination27.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(28))
        {
            Combination28.setBackground(C4.darker());
        }
        else
        {
            Combination28.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(29))
        {
            Combination29.setBackground(C5.darker());
        }
        else
        {
            Combination29.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(30))
        {
            Combination30.setBackground(C6.darker());
        }
        else
        {
            Combination30.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(31))
        {
           Combination31.setBackground(C7.darker());
        }
        else
        {
            Combination31.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(32))
        {
            Combination32.setBackground(C8.brighter());
        }
        else
        {
            Combination32.setBackground(DefaultColor);
        }
        if(Selected.HasPotential(33))
        {
            Combination33.setBackground(C9.darker());
        }
        else
        {
            Combination33.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(34))
        {
            Combination34.setBackground(C10.darker());
        }
        else
        {
            Combination34.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(35))
        {
            Combination35.setBackground(C11.darker());
        }
        else
        {
            Combination35.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(36))
        {
            Combination36.setBackground(C12.brighter()); 
        }
        else
        {
            Combination36.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(37))
        {
            Combination37.setBackground(C1.darker()); 
        }
        else
        {
            Combination37.setBackground(DefaultColor);
        }
        
        if(Selected.HasPotential(38))
        {
            Combination38.setBackground(C2.darker());
        }
        else
        {
            Combination38.setBackground(DefaultColor);
        }
        
    }    
    /*----FINE METODI RELATIVI ALLA SELEZIONE DELLE CARTE----*/
       
    /*----METODI DI GIOCO----*/
    
    /*
        @METHOD SelectedCard0
       
        @OVERVIEW Metodo che, quando la prima carta della mano del giocatore
                  viene selezionata dall'utente, ne stampa le scelte possibili
                  in output e rende visibili gli interruttori corrispondenti
                  ad ogni possibile scelta
    */
    public synchronized void SelectCard0()
    {
        if(Player.YourTurn())
        {
            RevealSwitches();
            
            if(PlayerHand.get(0).HasCard())
            {
                Carta C = PlayerHand.get(0).Card;
            
                C.StampaPotenziali();
                AnalyzedCard = C;
                AnalyzeCard(C);
            }
        }
    }
    
    /*
        @METHOD SelectedCard1
       
        @OVERVIEW Metodo che, quando la seconda carta della mano del giocatore
                  viene selezionata dall'utente, ne stampa le scelte possibili
                  in output e rende visibili gli interruttori corrispondenti
                  ad ogni possibile scelta
    */
    public synchronized void SelectCard1()
    {
        if(Player.YourTurn()) 
       {
        RevealSwitches();   
           
        if(PlayerHand.get(1).HasCard())
        {
            Carta C = PlayerHand.get(1).Card;
            
            C.StampaPotenziali();
            AnalyzedCard = C;
            AnalyzeCard(C);
        }
       }
    }
    
    /*
        @METHOD SelectedCard2
       
        @OVERVIEW Metodo che, quando la terza carta della mano del giocatore
                  viene selezionata dall'utente, ne stampa le scelte possibili
                  in output e rende visibili gli interruttori corrispondenti
                  ad ogni possibile scelta
    */
    public synchronized void SelectCard2()
    {
        if(Player.YourTurn())
       {
        RevealSwitches();   
           
        if(PlayerHand.get(2).HasCard())
        {
            Carta C = PlayerHand.get(2).Card;
            
            C.StampaPotenziali();
            AnalyzedCard = C;
            AnalyzeCard(C);
        }
       }
    }
    
      /*
        @METHOD PlayCard
    
        @OVERVIEW Metodo che, dati una carta ed un intero rappresentante una sua combinazione in input
                  gioca sul tavolo quella carta prendendo le carte associate alla sua combinazione
    
        @PAR combinazione : Intero rappresentante una combinazione associata ad una carta
        @PAR C : Carta a cui è associata la combinazione in input
    */
    public synchronized void PlayCard(int combinazione,Carta C)
    {
        Sessione.GiocaCarta(Player,combinazione, C);  
        
        /*
            Una volta giocata la carta, gli indicatori della combinazione tornano ad assumere
            il colore di default e a non apparire come selezionati
        */
        UnClickAll();
        UnColorAll();
        
        AnalyzedCard = null;
        ChoosenCardCombination = 0;
        
        UnsetChoiceButtons();
    }
    
    /*----METODI RELATIVI ALL'INTERAZIONE CON L'INTERFACCIA----*/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BoardPanel = new javax.swing.JPanel();
        CardsAndSwitchesPanel = new javax.swing.JPanel();
        SecondHalfOfSwitches = new javax.swing.JPanel();
        Combination25 = new javax.swing.JToggleButton();
        Combination26 = new javax.swing.JToggleButton();
        Combination27 = new javax.swing.JToggleButton();
        Combination28 = new javax.swing.JToggleButton();
        Combination29 = new javax.swing.JToggleButton();
        Combination30 = new javax.swing.JToggleButton();
        Combination31 = new javax.swing.JToggleButton();
        Combination32 = new javax.swing.JToggleButton();
        Combination33 = new javax.swing.JToggleButton();
        Combination34 = new javax.swing.JToggleButton();
        Combination35 = new javax.swing.JToggleButton();
        Combination36 = new javax.swing.JToggleButton();
        Combination37 = new javax.swing.JToggleButton();
        Combination38 = new javax.swing.JToggleButton();
        FirstHalfOfSwitches = new javax.swing.JPanel();
        Combination2 = new javax.swing.JToggleButton();
        Combination1 = new javax.swing.JToggleButton();
        Combination3 = new javax.swing.JToggleButton();
        Combination4 = new javax.swing.JToggleButton();
        Combination5 = new javax.swing.JToggleButton();
        Combination6 = new javax.swing.JToggleButton();
        Combination7 = new javax.swing.JToggleButton();
        Combination8 = new javax.swing.JToggleButton();
        Combination9 = new javax.swing.JToggleButton();
        Combination10 = new javax.swing.JToggleButton();
        Combination11 = new javax.swing.JToggleButton();
        Combination12 = new javax.swing.JToggleButton();
        Combination13 = new javax.swing.JToggleButton();
        Combination14 = new javax.swing.JToggleButton();
        Combination15 = new javax.swing.JToggleButton();
        Combination16 = new javax.swing.JToggleButton();
        Combination17 = new javax.swing.JToggleButton();
        Combination18 = new javax.swing.JToggleButton();
        Combination19 = new javax.swing.JToggleButton();
        Combination20 = new javax.swing.JToggleButton();
        Combination21 = new javax.swing.JToggleButton();
        Combination22 = new javax.swing.JToggleButton();
        Combination23 = new javax.swing.JToggleButton();
        Combination24 = new javax.swing.JToggleButton();
        TableCards = new javax.swing.JPanel();
        TableCard10 = new javax.swing.JLabel();
        TableCard6 = new javax.swing.JLabel();
        TableCard1 = new javax.swing.JLabel();
        TableCard11 = new javax.swing.JLabel();
        TableCard8 = new javax.swing.JLabel();
        TableCard5 = new javax.swing.JLabel();
        TableCard3 = new javax.swing.JLabel();
        TableCard9 = new javax.swing.JLabel();
        TableCard7 = new javax.swing.JLabel();
        TableCard12 = new javax.swing.JLabel();
        TableCard4 = new javax.swing.JLabel();
        TableCard2 = new javax.swing.JLabel();
        StatusOfPlayer = new javax.swing.JPanel();
        PlayerTurnLabel = new javax.swing.JLabel();
        CPUName1 = new javax.swing.JLabel();
        StatusOfCpU = new javax.swing.JPanel();
        CPUName = new javax.swing.JLabel();
        CPUTurnLabel = new javax.swing.JLabel();
        CPUArea = new javax.swing.JPanel();
        DeckLabelCPU = new javax.swing.JLabel();
        CPUCards = new javax.swing.JPanel();
        OpponentLabel1 = new javax.swing.JLabel();
        OpponentLabel2 = new javax.swing.JLabel();
        OpponentLabel3 = new javax.swing.JLabel();
        PlayerControls = new javax.swing.JPanel();
        OkButton = new javax.swing.JButton();
        UndoButton = new javax.swing.JButton();
        PlayerArea = new javax.swing.JPanel();
        DeckLabelPlayer = new javax.swing.JLabel();
        PlayerCards = new javax.swing.JPanel();
        PlayerLabel1 = new javax.swing.JLabel();
        PlayerLabel2 = new javax.swing.JLabel();
        PlayerLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ASSO PIGLIATUTTO");
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(1310, 790));
        setMinimumSize(new java.awt.Dimension(1310, 790));
        setName("Board"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(1310, 790));

        BoardPanel.setBackground(new java.awt.Color(51, 153, 0));
        BoardPanel.setForeground(new java.awt.Color(51, 204, 0));
        BoardPanel.setAlignmentX(0.0F);
        BoardPanel.setAlignmentY(0.0F);
        BoardPanel.setMaximumSize(new java.awt.Dimension(1310, 790));
        BoardPanel.setMinimumSize(new java.awt.Dimension(1310, 790));
        BoardPanel.setPreferredSize(new java.awt.Dimension(1310, 790));
        BoardPanel.setRequestFocusEnabled(false);

        CardsAndSwitchesPanel.setMaximumSize(new java.awt.Dimension(1300, 300));
        CardsAndSwitchesPanel.setMinimumSize(new java.awt.Dimension(1300, 300));
        CardsAndSwitchesPanel.setOpaque(false);
        CardsAndSwitchesPanel.setPreferredSize(new java.awt.Dimension(1300, 300));

        SecondHalfOfSwitches.setAlignmentX(0.0F);
        SecondHalfOfSwitches.setAlignmentY(0.0F);
        SecondHalfOfSwitches.setMaximumSize(new java.awt.Dimension(1290, 60));
        SecondHalfOfSwitches.setMinimumSize(new java.awt.Dimension(1290, 60));
        SecondHalfOfSwitches.setOpaque(false);
        SecondHalfOfSwitches.setPreferredSize(new java.awt.Dimension(1290, 60));

        Combination25.setText("25");
        Combination25.setAlignmentY(0.0F);
        Combination25.setBorder(null);
        Combination25.setMargin(null);
        Combination25.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination25.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination25.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination25MousePressed(evt);
            }
        });
        Combination25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination25ActionPerformed(evt);
            }
        });

        Combination26.setText("26");
        Combination26.setAlignmentY(0.0F);
        Combination26.setBorder(null);
        Combination26.setMargin(null);
        Combination26.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination26.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination26.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination26MousePressed(evt);
            }
        });

        Combination27.setText("27");
        Combination27.setAlignmentY(0.0F);
        Combination27.setBorder(null);
        Combination27.setMargin(null);
        Combination27.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination27.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination27.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination27MousePressed(evt);
            }
        });
        Combination27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination27ActionPerformed(evt);
            }
        });

        Combination28.setText("28");
        Combination28.setAlignmentY(0.0F);
        Combination28.setBorder(null);
        Combination28.setMargin(null);
        Combination28.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination28.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination28.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination28MousePressed(evt);
            }
        });
        Combination28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination28ActionPerformed(evt);
            }
        });

        Combination29.setText("29");
        Combination29.setAlignmentY(0.0F);
        Combination29.setBorder(null);
        Combination29.setMargin(null);
        Combination29.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination29.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination29.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination29MousePressed(evt);
            }
        });

        Combination30.setText("30");
        Combination30.setAlignmentY(0.0F);
        Combination30.setBorder(null);
        Combination30.setMargin(null);
        Combination30.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination30.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination30.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination30MousePressed(evt);
            }
        });

        Combination31.setText("31");
        Combination31.setAlignmentY(0.0F);
        Combination31.setBorder(null);
        Combination31.setMargin(null);
        Combination31.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination31.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination31.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination31MousePressed(evt);
            }
        });

        Combination32.setText("32");
        Combination32.setAlignmentY(0.0F);
        Combination32.setBorder(null);
        Combination32.setMargin(null);
        Combination32.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination32.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination32.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination32MousePressed(evt);
            }
        });

        Combination33.setText("33");
        Combination33.setAlignmentY(0.0F);
        Combination33.setBorder(null);
        Combination33.setMargin(null);
        Combination33.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination33.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination33.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination33MousePressed(evt);
            }
        });

        Combination34.setText("34");
        Combination34.setAlignmentY(0.0F);
        Combination34.setBorder(null);
        Combination34.setMargin(null);
        Combination34.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination34.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination34.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination34MousePressed(evt);
            }
        });

        Combination35.setText("35");
        Combination35.setAlignmentY(0.0F);
        Combination35.setBorder(null);
        Combination35.setMargin(null);
        Combination35.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination35.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination35.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination35MousePressed(evt);
            }
        });

        Combination36.setText("36");
        Combination36.setAlignmentY(0.0F);
        Combination36.setBorder(null);
        Combination36.setMargin(null);
        Combination36.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination36.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination36.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination36MousePressed(evt);
            }
        });
        Combination36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination36ActionPerformed(evt);
            }
        });

        Combination37.setText("37");
        Combination37.setAlignmentY(0.0F);
        Combination37.setBorder(null);
        Combination37.setMargin(null);
        Combination37.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination37.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination37.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination37MousePressed(evt);
            }
        });

        Combination38.setText("38");
        Combination38.setAlignmentY(0.0F);
        Combination38.setBorder(null);
        Combination38.setMargin(null);
        Combination38.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination38.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination38.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination38MousePressed(evt);
            }
        });
        Combination38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination38ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SecondHalfOfSwitchesLayout = new javax.swing.GroupLayout(SecondHalfOfSwitches);
        SecondHalfOfSwitches.setLayout(SecondHalfOfSwitchesLayout);
        SecondHalfOfSwitchesLayout.setHorizontalGroup(
            SecondHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SecondHalfOfSwitchesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SecondHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(SecondHalfOfSwitchesLayout.createSequentialGroup()
                        .addComponent(Combination25, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(Combination26, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Combination27, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Combination28, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Combination29, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Combination30, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Combination37, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SecondHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Combination38, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(SecondHalfOfSwitchesLayout.createSequentialGroup()
                        .addComponent(Combination31, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Combination32, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Combination33, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Combination34, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Combination35, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Combination36, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        SecondHalfOfSwitchesLayout.setVerticalGroup(
            SecondHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SecondHalfOfSwitchesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SecondHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Combination25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SecondHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Combination37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        FirstHalfOfSwitches.setAlignmentX(0.0F);
        FirstHalfOfSwitches.setAlignmentY(0.0F);
        FirstHalfOfSwitches.setMaximumSize(new java.awt.Dimension(1280, 65));
        FirstHalfOfSwitches.setMinimumSize(new java.awt.Dimension(1280, 65));
        FirstHalfOfSwitches.setOpaque(false);
        FirstHalfOfSwitches.setPreferredSize(new java.awt.Dimension(1280, 65));

        Combination2.setText("2");
        Combination2.setAlignmentY(0.0F);
        Combination2.setBorder(null);
        Combination2.setMargin(null);
        Combination2.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination2.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination2.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination2MousePressed(evt);
            }
        });

        Combination1.setText("1");
        Combination1.setAlignmentY(0.0F);
        Combination1.setBorder(null);
        Combination1.setMargin(null);
        Combination1.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination1.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination1.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination1MousePressed(evt);
            }
        });
        Combination1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination1ActionPerformed(evt);
            }
        });

        Combination3.setText("3");
        Combination3.setAlignmentY(0.0F);
        Combination3.setBorder(null);
        Combination3.setMargin(null);
        Combination3.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination3.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination3.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination3MousePressed(evt);
            }
        });
        Combination3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination3ActionPerformed(evt);
            }
        });

        Combination4.setText("4");
        Combination4.setAlignmentY(0.0F);
        Combination4.setBorder(null);
        Combination4.setMargin(null);
        Combination4.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination4.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination4.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination4MousePressed(evt);
            }
        });

        Combination5.setText("5");
        Combination5.setAlignmentY(0.0F);
        Combination5.setBorder(null);
        Combination5.setMargin(null);
        Combination5.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination5.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination5.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination5MousePressed(evt);
            }
        });

        Combination6.setText("6");
        Combination6.setAlignmentY(0.0F);
        Combination6.setBorder(null);
        Combination6.setMargin(null);
        Combination6.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination6.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination6.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination6MousePressed(evt);
            }
        });

        Combination7.setText("7");
        Combination7.setAlignmentY(0.0F);
        Combination7.setBorder(null);
        Combination7.setMargin(null);
        Combination7.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination7.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination7.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination7MousePressed(evt);
            }
        });

        Combination8.setText("8");
        Combination8.setAlignmentY(0.0F);
        Combination8.setBorder(null);
        Combination8.setMargin(null);
        Combination8.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination8.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination8.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination8MousePressed(evt);
            }
        });

        Combination9.setText("9");
        Combination9.setAlignmentY(0.0F);
        Combination9.setBorder(null);
        Combination9.setMargin(null);
        Combination9.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination9.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination9.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination9MousePressed(evt);
            }
        });

        Combination10.setText("10");
        Combination10.setAlignmentY(0.0F);
        Combination10.setBorder(null);
        Combination10.setMargin(null);
        Combination10.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination10.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination10.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination10MousePressed(evt);
            }
        });

        Combination11.setText("11");
        Combination11.setAlignmentY(0.0F);
        Combination11.setBorder(null);
        Combination11.setMargin(null);
        Combination11.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination11.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination11.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination11MousePressed(evt);
            }
        });

        Combination12.setText("12");
        Combination12.setAlignmentY(0.0F);
        Combination12.setBorder(null);
        Combination12.setMargin(null);
        Combination12.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination12.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination12.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination12MousePressed(evt);
            }
        });

        Combination13.setText("13");
        Combination13.setAlignmentY(0.0F);
        Combination13.setBorder(null);
        Combination13.setMargin(null);
        Combination13.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination13.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination13.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination13MousePressed(evt);
            }
        });
        Combination13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination13ActionPerformed(evt);
            }
        });

        Combination14.setText("14");
        Combination14.setAlignmentY(0.0F);
        Combination14.setBorder(null);
        Combination14.setMargin(null);
        Combination14.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination14.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination14.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination14MousePressed(evt);
            }
        });

        Combination15.setText("15");
        Combination15.setAlignmentY(0.0F);
        Combination15.setBorder(null);
        Combination15.setMargin(null);
        Combination15.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination15.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination15.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination15MousePressed(evt);
            }
        });
        Combination15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination15ActionPerformed(evt);
            }
        });

        Combination16.setText("16");
        Combination16.setAlignmentY(0.0F);
        Combination16.setBorder(null);
        Combination16.setMargin(null);
        Combination16.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination16.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination16.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination16MousePressed(evt);
            }
        });
        Combination16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination16ActionPerformed(evt);
            }
        });

        Combination17.setText("17");
        Combination17.setAlignmentY(0.0F);
        Combination17.setBorder(null);
        Combination17.setMargin(null);
        Combination17.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination17.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination17.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination17MousePressed(evt);
            }
        });

        Combination18.setText("18");
        Combination18.setAlignmentY(0.0F);
        Combination18.setBorder(null);
        Combination18.setMargin(null);
        Combination18.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination18.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination18.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination18MousePressed(evt);
            }
        });

        Combination19.setText("19");
        Combination19.setAlignmentY(0.0F);
        Combination19.setBorder(null);
        Combination19.setMargin(null);
        Combination19.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination19.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination19.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination19MousePressed(evt);
            }
        });

        Combination20.setText("20");
        Combination20.setAlignmentY(0.0F);
        Combination20.setBorder(null);
        Combination20.setMargin(null);
        Combination20.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination20.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination20.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination20MousePressed(evt);
            }
        });

        Combination21.setText("21");
        Combination21.setAlignmentY(0.0F);
        Combination21.setBorder(null);
        Combination21.setMargin(null);
        Combination21.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination21.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination21.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination21MousePressed(evt);
            }
        });

        Combination22.setText("22");
        Combination22.setAlignmentY(0.0F);
        Combination22.setBorder(null);
        Combination22.setMargin(null);
        Combination22.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination22.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination22.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination22MousePressed(evt);
            }
        });

        Combination23.setText("23");
        Combination23.setAlignmentY(0.0F);
        Combination23.setBorder(null);
        Combination23.setMargin(null);
        Combination23.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination23.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination23.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination23MousePressed(evt);
            }
        });

        Combination24.setText("24");
        Combination24.setAlignmentY(0.0F);
        Combination24.setBorder(null);
        Combination24.setMargin(null);
        Combination24.setMaximumSize(new java.awt.Dimension(100, 20));
        Combination24.setMinimumSize(new java.awt.Dimension(100, 20));
        Combination24.setPreferredSize(new java.awt.Dimension(100, 20));
        Combination24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination24MousePressed(evt);
            }
        });
        Combination24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FirstHalfOfSwitchesLayout = new javax.swing.GroupLayout(FirstHalfOfSwitches);
        FirstHalfOfSwitches.setLayout(FirstHalfOfSwitchesLayout);
        FirstHalfOfSwitchesLayout.setHorizontalGroup(
            FirstHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FirstHalfOfSwitchesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FirstHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FirstHalfOfSwitchesLayout.createSequentialGroup()
                        .addGroup(FirstHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(FirstHalfOfSwitchesLayout.createSequentialGroup()
                                .addComponent(Combination13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(Combination14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Combination16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(FirstHalfOfSwitchesLayout.createSequentialGroup()
                                .addComponent(Combination3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Combination4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(FirstHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FirstHalfOfSwitchesLayout.createSequentialGroup()
                                .addComponent(Combination5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(Combination12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(FirstHalfOfSwitchesLayout.createSequentialGroup()
                                .addComponent(Combination17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination18, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination21, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination22, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Combination24, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(FirstHalfOfSwitchesLayout.createSequentialGroup()
                        .addComponent(Combination1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(Combination2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        FirstHalfOfSwitchesLayout.setVerticalGroup(
            FirstHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FirstHalfOfSwitchesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FirstHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Combination1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FirstHalfOfSwitchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Combination13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combination24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        TableCards.setAlignmentX(0.0F);
        TableCards.setAlignmentY(0.0F);
        TableCards.setOpaque(false);

        TableCard10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard10.setAlignmentY(0.0F);
        TableCard10.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard10.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard10.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard10.setRequestFocusEnabled(false);

        TableCard6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard6.setAlignmentY(0.0F);
        TableCard6.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard6.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard6.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard6.setRequestFocusEnabled(false);

        TableCard1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard1.setAlignmentY(0.0F);
        TableCard1.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard1.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard1.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard1.setRequestFocusEnabled(false);

        TableCard11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard11.setAlignmentY(0.0F);
        TableCard11.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard11.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard11.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard11.setRequestFocusEnabled(false);

        TableCard8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard8.setAlignmentY(0.0F);
        TableCard8.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard8.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard8.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard8.setRequestFocusEnabled(false);

        TableCard5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard5.setAlignmentY(0.0F);
        TableCard5.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard5.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard5.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard5.setRequestFocusEnabled(false);

        TableCard3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard3.setAlignmentY(0.0F);
        TableCard3.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard3.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard3.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard3.setRequestFocusEnabled(false);

        TableCard9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard9.setAlignmentY(0.0F);
        TableCard9.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard9.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard9.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard9.setRequestFocusEnabled(false);

        TableCard7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard7.setAlignmentY(0.0F);
        TableCard7.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard7.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard7.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard7.setRequestFocusEnabled(false);

        TableCard12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard12.setAlignmentY(0.0F);
        TableCard12.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard12.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard12.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard12.setRequestFocusEnabled(false);

        TableCard4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard4.setAlignmentY(0.0F);
        TableCard4.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard4.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard4.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard4.setRequestFocusEnabled(false);

        TableCard2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TableCard2.setAlignmentY(0.0F);
        TableCard2.setMaximumSize(new java.awt.Dimension(100, 160));
        TableCard2.setMinimumSize(new java.awt.Dimension(100, 160));
        TableCard2.setPreferredSize(new java.awt.Dimension(100, 160));
        TableCard2.setRequestFocusEnabled(false);

        javax.swing.GroupLayout TableCardsLayout = new javax.swing.GroupLayout(TableCards);
        TableCards.setLayout(TableCardsLayout);
        TableCardsLayout.setHorizontalGroup(
            TableCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TableCardsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TableCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(TableCard2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableCard3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableCard4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TableCard5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableCard6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableCard7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableCard8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableCard9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableCard10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableCard11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TableCard12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        TableCardsLayout.setVerticalGroup(
            TableCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TableCardsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TableCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TableCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard6, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard7, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard8, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard9, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard10, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard11, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard12, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TableCard2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout CardsAndSwitchesPanelLayout = new javax.swing.GroupLayout(CardsAndSwitchesPanel);
        CardsAndSwitchesPanel.setLayout(CardsAndSwitchesPanelLayout);
        CardsAndSwitchesPanelLayout.setHorizontalGroup(
            CardsAndSwitchesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardsAndSwitchesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CardsAndSwitchesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FirstHalfOfSwitches, javax.swing.GroupLayout.DEFAULT_SIZE, 1290, Short.MAX_VALUE)
                    .addGroup(CardsAndSwitchesPanelLayout.createSequentialGroup()
                        .addGroup(CardsAndSwitchesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SecondHalfOfSwitches, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TableCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        CardsAndSwitchesPanelLayout.setVerticalGroup(
            CardsAndSwitchesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardsAndSwitchesPanelLayout.createSequentialGroup()
                .addComponent(FirstHalfOfSwitches, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableCards, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SecondHalfOfSwitches, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        StatusOfPlayer.setMaximumSize(new java.awt.Dimension(495, 100));
        StatusOfPlayer.setMinimumSize(new java.awt.Dimension(495, 100));
        StatusOfPlayer.setOpaque(false);
        StatusOfPlayer.setPreferredSize(new java.awt.Dimension(495, 100));

        PlayerTurnLabel.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        PlayerTurnLabel.setForeground(new java.awt.Color(255, 0, 0));
        PlayerTurnLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        CPUName1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CPUName1.setForeground(new java.awt.Color(153, 0, 153));
        CPUName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CPUName1.setText("GIOCATORE");
        CPUName1.setMaximumSize(new java.awt.Dimension(475, 35));
        CPUName1.setMinimumSize(new java.awt.Dimension(475, 35));
        CPUName1.setPreferredSize(new java.awt.Dimension(475, 35));

        javax.swing.GroupLayout StatusOfPlayerLayout = new javax.swing.GroupLayout(StatusOfPlayer);
        StatusOfPlayer.setLayout(StatusOfPlayerLayout);
        StatusOfPlayerLayout.setHorizontalGroup(
            StatusOfPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatusOfPlayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StatusOfPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CPUName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PlayerTurnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        StatusOfPlayerLayout.setVerticalGroup(
            StatusOfPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatusOfPlayerLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(PlayerTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addComponent(CPUName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        StatusOfCpU.setMaximumSize(new java.awt.Dimension(495, 200));
        StatusOfCpU.setMinimumSize(new java.awt.Dimension(495, 200));
        StatusOfCpU.setName(""); // NOI18N
        StatusOfCpU.setOpaque(false);
        StatusOfCpU.setPreferredSize(new java.awt.Dimension(495, 200));

        CPUName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CPUName.setForeground(new java.awt.Color(255, 204, 0));
        CPUName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CPUName.setText("CPU");
        CPUName.setMaximumSize(new java.awt.Dimension(475, 35));
        CPUName.setMinimumSize(new java.awt.Dimension(475, 35));
        CPUName.setPreferredSize(new java.awt.Dimension(475, 35));

        CPUTurnLabel.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        CPUTurnLabel.setForeground(new java.awt.Color(102, 255, 0));
        CPUTurnLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout StatusOfCpULayout = new javax.swing.GroupLayout(StatusOfCpU);
        StatusOfCpU.setLayout(StatusOfCpULayout);
        StatusOfCpULayout.setHorizontalGroup(
            StatusOfCpULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatusOfCpULayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StatusOfCpULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CPUName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CPUTurnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        StatusOfCpULayout.setVerticalGroup(
            StatusOfCpULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatusOfCpULayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CPUName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(CPUTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        CPUArea.setMaximumSize(new java.awt.Dimension(480, 200));
        CPUArea.setMinimumSize(new java.awt.Dimension(480, 200));
        CPUArea.setOpaque(false);
        CPUArea.setPreferredSize(new java.awt.Dimension(480, 200));

        DeckLabelCPU.setAlignmentY(0.0F);
        DeckLabelCPU.setMaximumSize(new java.awt.Dimension(100, 160));
        DeckLabelCPU.setMinimumSize(new java.awt.Dimension(100, 160));
        DeckLabelCPU.setPreferredSize(new java.awt.Dimension(100, 160));

        CPUCards.setMaximumSize(new java.awt.Dimension(340, 180));
        CPUCards.setMinimumSize(new java.awt.Dimension(340, 180));
        CPUCards.setOpaque(false);
        CPUCards.setPreferredSize(new java.awt.Dimension(340, 180));

        OpponentLabel1.setMaximumSize(new java.awt.Dimension(100, 160));
        OpponentLabel1.setMinimumSize(new java.awt.Dimension(100, 160));
        OpponentLabel1.setPreferredSize(new java.awt.Dimension(100, 160));
        OpponentLabel1.setRequestFocusEnabled(false);

        OpponentLabel2.setMaximumSize(new java.awt.Dimension(100, 160));
        OpponentLabel2.setMinimumSize(new java.awt.Dimension(100, 160));
        OpponentLabel2.setPreferredSize(new java.awt.Dimension(100, 160));
        OpponentLabel2.setRequestFocusEnabled(false);

        OpponentLabel3.setMaximumSize(new java.awt.Dimension(100, 160));
        OpponentLabel3.setMinimumSize(new java.awt.Dimension(100, 160));
        OpponentLabel3.setPreferredSize(new java.awt.Dimension(100, 160));
        OpponentLabel3.setRequestFocusEnabled(false);

        javax.swing.GroupLayout CPUCardsLayout = new javax.swing.GroupLayout(CPUCards);
        CPUCards.setLayout(CPUCardsLayout);
        CPUCardsLayout.setHorizontalGroup(
            CPUCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPUCardsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(OpponentLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OpponentLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OpponentLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        CPUCardsLayout.setVerticalGroup(
            CPUCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPUCardsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CPUCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OpponentLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OpponentLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OpponentLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CPUAreaLayout = new javax.swing.GroupLayout(CPUArea);
        CPUArea.setLayout(CPUAreaLayout);
        CPUAreaLayout.setHorizontalGroup(
            CPUAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPUAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DeckLabelCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CPUCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        CPUAreaLayout.setVerticalGroup(
            CPUAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPUAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CPUAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CPUCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DeckLabelCPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        PlayerControls.setOpaque(false);
        PlayerControls.setPreferredSize(new java.awt.Dimension(470, 233));

        OkButton.setBackground(new java.awt.Color(204, 204, 204));
        OkButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        OkButton.setForeground(new java.awt.Color(238, 184, 56));
        OkButton.setText("GIOCA");
        OkButton.setToolTipText("");
        OkButton.setMaximumSize(new java.awt.Dimension(220, 25));
        OkButton.setMinimumSize(new java.awt.Dimension(220, 25));
        OkButton.setPreferredSize(new java.awt.Dimension(220, 25));
        OkButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                OkButtonMousePressed(evt);
            }
        });

        UndoButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        UndoButton.setForeground(new java.awt.Color(255, 51, 51));
        UndoButton.setText("TORNA");
        UndoButton.setActionCommand("RITORNA");
        UndoButton.setMaximumSize(new java.awt.Dimension(220, 25));
        UndoButton.setMinimumSize(new java.awt.Dimension(220, 25));
        UndoButton.setPreferredSize(new java.awt.Dimension(220, 25));
        UndoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                UndoButtonMousePressed(evt);
            }
        });

        PlayerArea.setMaximumSize(new java.awt.Dimension(450, 190));
        PlayerArea.setMinimumSize(new java.awt.Dimension(450, 190));
        PlayerArea.setOpaque(false);
        PlayerArea.setPreferredSize(new java.awt.Dimension(450, 190));

        DeckLabelPlayer.setAlignmentY(0.0F);
        DeckLabelPlayer.setMaximumSize(new java.awt.Dimension(100, 160));
        DeckLabelPlayer.setMinimumSize(new java.awt.Dimension(100, 160));
        DeckLabelPlayer.setPreferredSize(new java.awt.Dimension(100, 160));

        PlayerCards.setMaximumSize(new java.awt.Dimension(340, 180));
        PlayerCards.setMinimumSize(new java.awt.Dimension(340, 180));
        PlayerCards.setOpaque(false);
        PlayerCards.setPreferredSize(new java.awt.Dimension(340, 180));

        PlayerLabel1.setMaximumSize(new java.awt.Dimension(100, 160));
        PlayerLabel1.setMinimumSize(new java.awt.Dimension(100, 160));
        PlayerLabel1.setPreferredSize(new java.awt.Dimension(100, 160));
        PlayerLabel1.setRequestFocusEnabled(false);
        PlayerLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PlayerLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PlayerLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PlayerLabel1MouseExited(evt);
            }
        });

        PlayerLabel2.setMaximumSize(new java.awt.Dimension(100, 160));
        PlayerLabel2.setMinimumSize(new java.awt.Dimension(100, 160));
        PlayerLabel2.setPreferredSize(new java.awt.Dimension(100, 160));
        PlayerLabel2.setRequestFocusEnabled(false);
        PlayerLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PlayerLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PlayerLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PlayerLabel2MouseExited(evt);
            }
        });

        PlayerLabel3.setMaximumSize(new java.awt.Dimension(100, 160));
        PlayerLabel3.setMinimumSize(new java.awt.Dimension(100, 160));
        PlayerLabel3.setPreferredSize(new java.awt.Dimension(100, 160));
        PlayerLabel3.setRequestFocusEnabled(false);
        PlayerLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PlayerLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PlayerLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PlayerLabel3MouseExited(evt);
            }
        });

        javax.swing.GroupLayout PlayerCardsLayout = new javax.swing.GroupLayout(PlayerCards);
        PlayerCards.setLayout(PlayerCardsLayout);
        PlayerCardsLayout.setHorizontalGroup(
            PlayerCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerCardsLayout.createSequentialGroup()
                .addComponent(PlayerLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PlayerLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PlayerLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PlayerCardsLayout.setVerticalGroup(
            PlayerCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerCardsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PlayerCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PlayerLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PlayerLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PlayerLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PlayerAreaLayout = new javax.swing.GroupLayout(PlayerArea);
        PlayerArea.setLayout(PlayerAreaLayout);
        PlayerAreaLayout.setHorizontalGroup(
            PlayerAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DeckLabelPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PlayerCards, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PlayerAreaLayout.setVerticalGroup(
            PlayerAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PlayerAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PlayerCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PlayerAreaLayout.createSequentialGroup()
                        .addComponent(DeckLabelPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addContainerGap())
        );

        javax.swing.GroupLayout PlayerControlsLayout = new javax.swing.GroupLayout(PlayerControls);
        PlayerControls.setLayout(PlayerControlsLayout);
        PlayerControlsLayout.setHorizontalGroup(
            PlayerControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerControlsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PlayerControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlayerControlsLayout.createSequentialGroup()
                        .addComponent(OkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UndoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PlayerControlsLayout.createSequentialGroup()
                        .addComponent(PlayerArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        PlayerControlsLayout.setVerticalGroup(
            PlayerControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerControlsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PlayerArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PlayerControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OkButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UndoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout BoardPanelLayout = new javax.swing.GroupLayout(BoardPanel);
        BoardPanel.setLayout(BoardPanelLayout);
        BoardPanelLayout.setHorizontalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BoardPanelLayout.createSequentialGroup()
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addComponent(CardsAndSwitchesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addGap(280, 280, 280)
                        .addComponent(CPUArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(StatusOfCpU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PlayerControls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(StatusOfPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        BoardPanelLayout.setVerticalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CPUArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StatusOfCpU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addComponent(CardsAndSwitchesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PlayerControls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StatusOfPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PlayerLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel3MouseExited

    }//GEN-LAST:event_PlayerLabel3MouseExited

    private void PlayerLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel3MouseEntered

    }//GEN-LAST:event_PlayerLabel3MouseEntered

    private void PlayerLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel3MouseClicked
        SelectCard2();
    }//GEN-LAST:event_PlayerLabel3MouseClicked

    private void PlayerLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel2MouseExited

    }//GEN-LAST:event_PlayerLabel2MouseExited

    private void PlayerLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel2MouseEntered

    }//GEN-LAST:event_PlayerLabel2MouseEntered

    private void PlayerLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel2MouseClicked
        SelectCard1();
    }//GEN-LAST:event_PlayerLabel2MouseClicked

    private void PlayerLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel1MouseExited

    }//GEN-LAST:event_PlayerLabel1MouseExited

    private void PlayerLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel1MouseEntered

    }//GEN-LAST:event_PlayerLabel1MouseEntered

    private void PlayerLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel1MouseClicked
        SelectCard0();
    }//GEN-LAST:event_PlayerLabel1MouseClicked

    private void Combination1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination1ActionPerformed

    private void Combination1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination1MousePressed
       if(IsSelected(1,Combination1))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,1,C1);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination1MousePressed

    private void Combination3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination3ActionPerformed

    private void Combination2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination2MousePressed
    if(IsSelected(2,Combination2))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,2,C2);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination2MousePressed

    private void Combination3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination3MousePressed
        if(IsSelected(3,Combination3))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,3,C3);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination3MousePressed

    private void Combination4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination4MousePressed
        if(IsSelected(4,Combination4))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,4,C4);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination4MousePressed

    private void Combination5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination5MousePressed
        if(IsSelected(5,Combination5))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,5,C5);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination5MousePressed

    private void Combination6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination6MousePressed
        if(IsSelected(6,Combination6))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,6,C6);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination6MousePressed

    private void Combination7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination7MousePressed
        if(IsSelected(7,Combination7))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,7,C7);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination7MousePressed

    private void Combination8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination8MousePressed
       if(IsSelected(8,Combination8))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,8,C8);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination8MousePressed

    private void Combination9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination9MousePressed
        if(IsSelected(9,Combination9))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,9,C9);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination9MousePressed

    private void Combination10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination10MousePressed
        if(IsSelected(10,Combination10))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,10,C10);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination10MousePressed

    private void Combination11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination11MousePressed
        if(IsSelected(11,Combination11))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,11,C11);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination11MousePressed

    private void Combination12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination12MousePressed
       if(IsSelected(12,Combination12))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,12,C12);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination12MousePressed

    private void UndoButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UndoButtonMousePressed
        UnClickAll();
        UnsetChoiceButtons();
        
        AnalyzedCard = null;
    }//GEN-LAST:event_UndoButtonMousePressed

    private void OkButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OkButtonMousePressed
        PlayCard(ChoosenCardCombination,AnalyzedCard);
        HideSwitches();
    }//GEN-LAST:event_OkButtonMousePressed

    private void Combination13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination13MousePressed
        if(IsSelected(13,Combination13))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,13,C1);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination13MousePressed

    private void Combination13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination13ActionPerformed
  
    }//GEN-LAST:event_Combination13ActionPerformed

    private void Combination14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination14MousePressed
        if(IsSelected(14,Combination14))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,14,C2);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination14MousePressed

    private void Combination15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination15MousePressed
        if(IsSelected(15,Combination15))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,15,C3);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination15MousePressed

    private void Combination15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination15ActionPerformed

    private void Combination16MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination16MousePressed
        if(IsSelected(16,Combination16))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,16,C4);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination16MousePressed

    private void Combination17MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination17MousePressed
        if(IsSelected(17,Combination17))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,17,C5);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination17MousePressed

    private void Combination18MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination18MousePressed
        if(IsSelected(18,Combination18))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,18,C6);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination18MousePressed

    private void Combination19MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination19MousePressed
        if(IsSelected(19,Combination19))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,19,C7);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination19MousePressed

    private void Combination20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination20MousePressed
        if(IsSelected(20,Combination20))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,20,C8);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination20MousePressed

    private void Combination21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination21MousePressed
        if(IsSelected(21,Combination21))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,21,C9);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination21MousePressed

    private void Combination22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination22MousePressed
        if(IsSelected(22,Combination22))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,22,C10);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination22MousePressed

    private void Combination23MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination23MousePressed
        if(IsSelected(23,Combination23))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,23,C11);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination23MousePressed

    private void Combination24MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination24MousePressed
        if(IsSelected(24,Combination24))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,24,C12);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination24MousePressed

    private void Combination16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination16ActionPerformed

    private void Combination24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination24ActionPerformed

    private void Combination25MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination25MousePressed
        if(IsSelected(25,Combination25))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,25,C1);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination25MousePressed

    private void Combination25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination25ActionPerformed

    private void Combination26MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination26MousePressed
       if(IsSelected(26,Combination26))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,26,C2);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination26MousePressed

    private void Combination27MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination27MousePressed
       if(IsSelected(27,Combination27))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,27,C3);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination27MousePressed

    private void Combination27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination27ActionPerformed

    private void Combination28MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination28MousePressed
       if(IsSelected(28,Combination28))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,28,C4);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination28MousePressed

    private void Combination28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination28ActionPerformed

    private void Combination29MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination29MousePressed
        if(IsSelected(29,Combination29))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,25,C5);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination29MousePressed

    private void Combination30MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination30MousePressed
          if(IsSelected(30,Combination30))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,30,C6);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination30MousePressed

    private void Combination31MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination31MousePressed
         if(IsSelected(31,Combination31))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,31,C7);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination31MousePressed

    private void Combination32MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination32MousePressed
         if(IsSelected(32,Combination32))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,32,C8);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination32MousePressed

    private void Combination33MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination33MousePressed
         if(IsSelected(33,Combination33))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,33,C9);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination33MousePressed

    private void Combination34MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination34MousePressed
         if(IsSelected(34,Combination34))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,34,C10);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination34MousePressed

    private void Combination35MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination35MousePressed
         if(IsSelected(35,Combination35))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,35,C11);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination35MousePressed

    private void Combination36MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination36MousePressed
         if(IsSelected(36,Combination36))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,36,C12);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination36MousePressed

    private void Combination36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination36ActionPerformed

    }//GEN-LAST:event_Combination36ActionPerformed

    private void Combination37MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination37MousePressed
         if(IsSelected(37,Combination37))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,37,C1);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination37MousePressed

    private void Combination38MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination38MousePressed
        if(IsSelected(38,Combination38))
       {
            UnClickAll();
            HighlightCards(AnalyzedCard,38,C1);
       }
       else
       {
       }
    }//GEN-LAST:event_Combination38MousePressed

    private void Combination38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination38ActionPerformed

    }//GEN-LAST:event_Combination38ActionPerformed

    /*----FINE METODI RELATIVI ALL'INTERAZIONE CON L'INTERFACCIA----*/
    
    /*----METODI DEL CICLO DI VITA DEL THREAD----*/
    
    /*
        @METHOD Exit
    
        @OVERVIEW Metodo che consente al giocatore di chiudere la finestra ed
                  uscire dal gioco
    */
    public void Exit()
    {
        this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
    }
    
    /*
        @METHOD GoToScores
    
        @OVERVIEW Metodo che consente, a fine partita, una volta calcolati i punteggi
                  complessivi, dichiarare il vincitore e visualizzare l'esito
                  della partita su una finestra apposita, uscendo della finestra
                  relativa alla board
    
       @PAR SPlayer : Punteggio del giocatore
       @PAR SCPU : Punteggio della CPU
       @String Winner : Nome del vincitore
    */
    public void GoToScores(Punteggio SPlayer,Punteggio SCPU,String winner)
    {
        //Finestra relativa ai punteggi
        ScoreFrame Scores = new ScoreFrame();
        
        Scores.DisplayScores(SPlayer, SCPU);
        Scores.SetGioco(Sessione);
        Scores.SetWinnerLabel(winner);
        Scores.setVisible(true);
        this.dispose();
    }

    /*----METODO MAIN----*/
    
    /*
        @METHOD main
    
        @OVERVIEW Metodo principale che consente l'esecuzione del ciclo di vita
                  della Board
    */
    
    public static void main(String args[]) 
    {
        Board X = new Board();
        X.setVisible(true);
    }
    
    /*----FINE METODO MAIN----*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BoardPanel;
    private javax.swing.JPanel CPUArea;
    private javax.swing.JPanel CPUCards;
    private javax.swing.JLabel CPUName;
    private javax.swing.JLabel CPUName1;
    private javax.swing.JLabel CPUTurnLabel;
    private javax.swing.JPanel CardsAndSwitchesPanel;
    private javax.swing.JToggleButton Combination1;
    private javax.swing.JToggleButton Combination10;
    private javax.swing.JToggleButton Combination11;
    private javax.swing.JToggleButton Combination12;
    private javax.swing.JToggleButton Combination13;
    private javax.swing.JToggleButton Combination14;
    private javax.swing.JToggleButton Combination15;
    private javax.swing.JToggleButton Combination16;
    private javax.swing.JToggleButton Combination17;
    private javax.swing.JToggleButton Combination18;
    private javax.swing.JToggleButton Combination19;
    private javax.swing.JToggleButton Combination2;
    private javax.swing.JToggleButton Combination20;
    private javax.swing.JToggleButton Combination21;
    private javax.swing.JToggleButton Combination22;
    private javax.swing.JToggleButton Combination23;
    private javax.swing.JToggleButton Combination24;
    private javax.swing.JToggleButton Combination25;
    private javax.swing.JToggleButton Combination26;
    private javax.swing.JToggleButton Combination27;
    private javax.swing.JToggleButton Combination28;
    private javax.swing.JToggleButton Combination29;
    private javax.swing.JToggleButton Combination3;
    private javax.swing.JToggleButton Combination30;
    private javax.swing.JToggleButton Combination31;
    private javax.swing.JToggleButton Combination32;
    private javax.swing.JToggleButton Combination33;
    private javax.swing.JToggleButton Combination34;
    private javax.swing.JToggleButton Combination35;
    private javax.swing.JToggleButton Combination36;
    private javax.swing.JToggleButton Combination37;
    private javax.swing.JToggleButton Combination38;
    private javax.swing.JToggleButton Combination4;
    private javax.swing.JToggleButton Combination5;
    private javax.swing.JToggleButton Combination6;
    private javax.swing.JToggleButton Combination7;
    private javax.swing.JToggleButton Combination8;
    private javax.swing.JToggleButton Combination9;
    private javax.swing.JLabel DeckLabelCPU;
    private javax.swing.JLabel DeckLabelPlayer;
    private javax.swing.JPanel FirstHalfOfSwitches;
    private javax.swing.JButton OkButton;
    private javax.swing.JLabel OpponentLabel1;
    private javax.swing.JLabel OpponentLabel2;
    private javax.swing.JLabel OpponentLabel3;
    private javax.swing.JPanel PlayerArea;
    private javax.swing.JPanel PlayerCards;
    private javax.swing.JPanel PlayerControls;
    private javax.swing.JLabel PlayerLabel1;
    private javax.swing.JLabel PlayerLabel2;
    private javax.swing.JLabel PlayerLabel3;
    private javax.swing.JLabel PlayerTurnLabel;
    private javax.swing.JPanel SecondHalfOfSwitches;
    private javax.swing.JPanel StatusOfCpU;
    private javax.swing.JPanel StatusOfPlayer;
    private javax.swing.JLabel TableCard1;
    private javax.swing.JLabel TableCard10;
    private javax.swing.JLabel TableCard11;
    private javax.swing.JLabel TableCard12;
    private javax.swing.JLabel TableCard2;
    private javax.swing.JLabel TableCard3;
    private javax.swing.JLabel TableCard4;
    private javax.swing.JLabel TableCard5;
    private javax.swing.JLabel TableCard6;
    private javax.swing.JLabel TableCard7;
    private javax.swing.JLabel TableCard8;
    private javax.swing.JLabel TableCard9;
    private javax.swing.JPanel TableCards;
    private javax.swing.JButton UndoButton;
    // End of variables declaration//GEN-END:variables
}
