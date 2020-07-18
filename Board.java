
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

public final class Board extends javax.swing.JFrame 
{
    Gioco Sessione;
    
    Giocatore CPU;
    Giocatore Player;
    
    ArrayList<Slot> PlayerHand;
    ArrayList<Slot> CPUHand;
    ArrayList<Slot> CardsOnTable;
    
    ArrayList<JToggleButton> SwitchList = new ArrayList();

    TesterDebugger Test = new TesterDebugger(this);
    
    Carta AnalyzedCard;
    
    int TakenOnTable;
    int FreeOnTable;
    
    int ChoosenCardCombination;
    
    //Massimo 11 combinazioni (vedi TesterDebugger - MaxCombinationGen)
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
    Color DefaultColor = new Color(240,240,240);//COLORE DI DEFAULT
    
    public Board() 
    {
        Sessione = new Gioco(this);

        initComponents();
        
        CPU = Sessione.GetCPU();
        Player = Sessione.GetPlayer();
        
        AnalyzedCard = null;
        
        ChoosenCardCombination = 0;
        
        InizializzaSlot();
        
        UndoButton.setVisible(false);
        OkButton.setVisible(false);

        NewGame();
    }
    
    public void NewGame()
    {       
       Random r = new Random();
       
       Sessione.NuovoGioco();
       
       Display();
    }
    
    public void InizializzaSlot()
    {          
        PlayerHand = new ArrayList();
        CPUHand = new ArrayList();
        CardsOnTable = new ArrayList();
        
        PlayerHand.add(new Slot(PlayerLabel1));
        PlayerHand.add(new Slot(PlayerLabel2));
        PlayerHand.add(new Slot(PlayerLabel3));
        
        CPUHand.add(new Slot(OpponentLabel1));
        CPUHand.add(new Slot(OpponentLabel2));
        CPUHand.add(new Slot(OpponentLabel3));
        
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
  
    }
    
    public synchronized void Display()
    {
        VisualizzaMazzo();
        VisualizzaManoCPU();
        VisualizzaManoGiocatore();
        VisualizzaTavolo();
        VisualizzaTurno();
    }
    
    public final void VisualizzaTurno()
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
    
    public final void VisualizzaCarta(int codice,JLabel Slot)
    {
        ImageIcon ic = new javax.swing.ImageIcon(getClass().getResource("/CardSkins/"+codice+".png"));
        
        Image image = ic.getImage(); // transform it 
        Image newimg = image.getScaledInstance(100, 160,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ic = new ImageIcon(newimg);  // transform it back
        
        Slot.setIcon(ic);
    }
    
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
    
    public int DeclareTakenSlots(ArrayList<Slot> CardSet)
    {
        int taken = 0;

        for(Slot s : CardSet)
        {
            if(s.HasCard())
            {
                taken++;
            }
        }
        
        return taken;
    }
    
    public int DeclareFreeSlots(ArrayList<Slot> CardSet)
    {
        int size = CardSet.size();
        int taken = DeclareTakenSlots(CardSet);
        
        return size - taken;
    }

    public synchronized void HighlightCards(Carta card,int M,Color Cl)
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
        
        SetChoiceButtonsVisible();
    }
    
    public void UnColorAll()
    {
        for(JToggleButton JTB : SwitchList)
        {
            JTB.setBackground(DefaultColor);
        }
    }
    
    public synchronized void PlayCard(int combinazione,Carta C)
    {
        Sessione.GiocaCarta(Player,combinazione, C);  
        
        UnClickAll();
        UnColorAll();
        
        AnalyzedCard = null;
        ChoosenCardCombination = 0;
        
        UnsetChoiceButtons();
    }
    
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
    
    /*----METODI RELATIVI ALLA SELEZIONE DELLE CARTE----*/
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
            Combination12.setBackground(C12);
        }
        else
        {
            Combination12.setBackground(DefaultColor);
        }
    }
       
   public boolean IsSelected(int n,JToggleButton JTB)
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
       
   public void SetChoiceButtonsVisible()
   {
       UndoButton.setVisible(true);
       OkButton.setVisible(true);
   }
   
   public void UnsetChoiceButtons()
   {
       ChoosenCardCombination = 0;
       
       UndoButton.setVisible(false);
       OkButton.setVisible(false);
   }

    public void UnClickAll()
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
    
    
    /*----FINE METODI RELATIVI ALLA SELEZIONE DELLE CARTE----*/
    
    public synchronized void SelectCard0()
    {
        if(Player.YourTurn())
        {
            if(PlayerHand.get(0).HasCard())
            {
                Carta C = PlayerHand.get(0).Card;
            
                C.StampaPotenziale();
                AnalyzedCard = C;
                AnalyzeCard(C);
            }
        }
    }
    
    public synchronized void SelectCard1()
    {
        if(Player.YourTurn()) 
       {
        if(PlayerHand.get(1).HasCard())
        {
            Carta C = PlayerHand.get(1).Card;
            
            C.StampaPotenziale();
            AnalyzedCard = C;
            AnalyzeCard(C);
        }
       }
    }
    
    public synchronized void SelectCard2()
    {
        if(Player.YourTurn())
       {
        if(PlayerHand.get(2).HasCard())
        {
            Carta C = PlayerHand.get(2).Card;
            
            C.StampaPotenziale();
            AnalyzedCard = C;
            AnalyzeCard(C);
        }
       }
    }
    
    public void Exit()
    {
        this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BoardPanel = new javax.swing.JPanel();
        YourCard1 = new javax.swing.JPanel();
        PlayerLabel1 = new javax.swing.JLabel();
        YourCard2 = new javax.swing.JPanel();
        PlayerLabel2 = new javax.swing.JLabel();
        YourCard3 = new javax.swing.JPanel();
        PlayerLabel3 = new javax.swing.JLabel();
        OpponentCard1 = new javax.swing.JPanel();
        OpponentLabel1 = new javax.swing.JLabel();
        OpponentCard2 = new javax.swing.JPanel();
        OpponentLabel2 = new javax.swing.JLabel();
        OpponentCard3 = new javax.swing.JPanel();
        OpponentLabel3 = new javax.swing.JLabel();
        DeckLabelPlayer = new javax.swing.JLabel();
        DeckLabelCPU = new javax.swing.JLabel();
        CPUName = new javax.swing.JLabel();
        TableCard1 = new javax.swing.JLabel();
        TableCard2 = new javax.swing.JLabel();
        TableCard3 = new javax.swing.JLabel();
        TableCard4 = new javax.swing.JLabel();
        TableCard5 = new javax.swing.JLabel();
        TableCard6 = new javax.swing.JLabel();
        TableCard7 = new javax.swing.JLabel();
        TableCard8 = new javax.swing.JLabel();
        TableCard9 = new javax.swing.JLabel();
        TableCard10 = new javax.swing.JLabel();
        CPUName1 = new javax.swing.JLabel();
        CPUTurnLabel = new javax.swing.JLabel();
        PlayerTurnLabel = new javax.swing.JLabel();
        TableCard12 = new javax.swing.JLabel();
        TableCard11 = new javax.swing.JLabel();
        Combination1 = new javax.swing.JToggleButton();
        Combination2 = new javax.swing.JToggleButton();
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
        UndoButton = new javax.swing.JButton();
        OkButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ASSO PIGLIATUTTO");
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(1300, 605));
        setMinimumSize(new java.awt.Dimension(1300, 605));
        setName("Board"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1300, 605));
        setResizable(false);
        setSize(new java.awt.Dimension(1080, 605));

        BoardPanel.setBackground(new java.awt.Color(51, 153, 0));
        BoardPanel.setForeground(new java.awt.Color(51, 204, 0));
        BoardPanel.setAlignmentX(0.0F);
        BoardPanel.setAlignmentY(0.0F);
        BoardPanel.setMaximumSize(new java.awt.Dimension(900, 600));
        BoardPanel.setMinimumSize(new java.awt.Dimension(900, 600));

        YourCard1.setMaximumSize(new java.awt.Dimension(100, 160));
        YourCard1.setOpaque(false);
        YourCard1.setPreferredSize(new java.awt.Dimension(100, 160));

        PlayerLabel1.setOpaque(true);
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

        javax.swing.GroupLayout YourCard1Layout = new javax.swing.GroupLayout(YourCard1);
        YourCard1.setLayout(YourCard1Layout);
        YourCard1Layout.setHorizontalGroup(
            YourCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        YourCard1Layout.setVerticalGroup(
            YourCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        YourCard2.setMaximumSize(new java.awt.Dimension(100, 160));
        YourCard2.setOpaque(false);
        YourCard2.setPreferredSize(new java.awt.Dimension(100, 160));

        PlayerLabel2.setOpaque(true);
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

        javax.swing.GroupLayout YourCard2Layout = new javax.swing.GroupLayout(YourCard2);
        YourCard2.setLayout(YourCard2Layout);
        YourCard2Layout.setHorizontalGroup(
            YourCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        YourCard2Layout.setVerticalGroup(
            YourCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        YourCard3.setMaximumSize(new java.awt.Dimension(100, 160));
        YourCard3.setOpaque(false);
        YourCard3.setPreferredSize(new java.awt.Dimension(100, 160));

        PlayerLabel3.setOpaque(true);
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

        javax.swing.GroupLayout YourCard3Layout = new javax.swing.GroupLayout(YourCard3);
        YourCard3.setLayout(YourCard3Layout);
        YourCard3Layout.setHorizontalGroup(
            YourCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        YourCard3Layout.setVerticalGroup(
            YourCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        OpponentCard1.setMaximumSize(new java.awt.Dimension(100, 160));
        OpponentCard1.setOpaque(false);
        OpponentCard1.setPreferredSize(new java.awt.Dimension(100, 160));

        OpponentLabel1.setOpaque(true);

        javax.swing.GroupLayout OpponentCard1Layout = new javax.swing.GroupLayout(OpponentCard1);
        OpponentCard1.setLayout(OpponentCard1Layout);
        OpponentCard1Layout.setHorizontalGroup(
            OpponentCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OpponentLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        OpponentCard1Layout.setVerticalGroup(
            OpponentCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OpponentLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        OpponentCard2.setMaximumSize(new java.awt.Dimension(100, 160));
        OpponentCard2.setOpaque(false);
        OpponentCard2.setPreferredSize(new java.awt.Dimension(100, 160));

        OpponentLabel2.setOpaque(true);

        javax.swing.GroupLayout OpponentCard2Layout = new javax.swing.GroupLayout(OpponentCard2);
        OpponentCard2.setLayout(OpponentCard2Layout);
        OpponentCard2Layout.setHorizontalGroup(
            OpponentCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OpponentLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        OpponentCard2Layout.setVerticalGroup(
            OpponentCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OpponentLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        OpponentCard3.setMaximumSize(new java.awt.Dimension(100, 160));
        OpponentCard3.setOpaque(false);
        OpponentCard3.setPreferredSize(new java.awt.Dimension(100, 160));

        OpponentLabel3.setOpaque(true);

        javax.swing.GroupLayout OpponentCard3Layout = new javax.swing.GroupLayout(OpponentCard3);
        OpponentCard3.setLayout(OpponentCard3Layout);
        OpponentCard3Layout.setHorizontalGroup(
            OpponentCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OpponentLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        OpponentCard3Layout.setVerticalGroup(
            OpponentCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OpponentLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        DeckLabelPlayer.setMaximumSize(new java.awt.Dimension(100, 160));
        DeckLabelPlayer.setMinimumSize(new java.awt.Dimension(100, 160));
        DeckLabelPlayer.setPreferredSize(new java.awt.Dimension(100, 160));

        DeckLabelCPU.setMaximumSize(new java.awt.Dimension(100, 160));
        DeckLabelCPU.setMinimumSize(new java.awt.Dimension(100, 160));
        DeckLabelCPU.setPreferredSize(new java.awt.Dimension(100, 160));

        CPUName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CPUName.setForeground(new java.awt.Color(255, 204, 0));
        CPUName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CPUName.setText("CPU");

        TableCard1.setOpaque(true);

        TableCard2.setOpaque(true);

        TableCard3.setOpaque(true);

        TableCard4.setOpaque(true);

        TableCard5.setOpaque(true);

        TableCard6.setOpaque(true);

        TableCard7.setOpaque(true);

        TableCard8.setOpaque(true);

        TableCard9.setOpaque(true);

        TableCard10.setOpaque(true);

        CPUName1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CPUName1.setForeground(new java.awt.Color(153, 0, 153));
        CPUName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CPUName1.setText("GIOCATORE");

        CPUTurnLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        CPUTurnLabel.setForeground(new java.awt.Color(102, 255, 0));
        CPUTurnLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        PlayerTurnLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        PlayerTurnLabel.setForeground(new java.awt.Color(255, 0, 0));
        PlayerTurnLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        TableCard12.setOpaque(true);

        TableCard11.setOpaque(true);

        Combination1.setText("1");
        Combination1.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination1.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination1.setPreferredSize(new java.awt.Dimension(115, 25));
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

        Combination2.setText("2");
        Combination2.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination2.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination2.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination2MousePressed(evt);
            }
        });

        Combination3.setText("3");
        Combination3.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination3.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination3.setPreferredSize(new java.awt.Dimension(115, 25));
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
        Combination4.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination4.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination4.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination4MousePressed(evt);
            }
        });

        Combination5.setText("5");
        Combination5.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination5.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination5.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination5MousePressed(evt);
            }
        });

        Combination6.setText("6");
        Combination6.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination6.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination6.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination6MousePressed(evt);
            }
        });

        Combination7.setText("7");
        Combination7.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination7.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination7.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination7MousePressed(evt);
            }
        });

        Combination8.setText("8");
        Combination8.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination8.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination8.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination8MousePressed(evt);
            }
        });

        Combination9.setText("9");
        Combination9.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination9.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination9.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination9MousePressed(evt);
            }
        });

        Combination10.setText("10");
        Combination10.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination10.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination10.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination10MousePressed(evt);
            }
        });

        Combination11.setText("11");
        Combination11.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination11.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination11.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination11MousePressed(evt);
            }
        });

        Combination12.setText("12");
        Combination12.setMaximumSize(new java.awt.Dimension(115, 25));
        Combination12.setMinimumSize(new java.awt.Dimension(115, 25));
        Combination12.setPreferredSize(new java.awt.Dimension(115, 25));
        Combination12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination12MousePressed(evt);
            }
        });

        UndoButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        UndoButton.setForeground(new java.awt.Color(255, 51, 51));
        UndoButton.setText("TORNA");
        UndoButton.setActionCommand("RITORNA");
        UndoButton.setMaximumSize(new java.awt.Dimension(400, 50));
        UndoButton.setMinimumSize(new java.awt.Dimension(400, 50));
        UndoButton.setPreferredSize(new java.awt.Dimension(400, 50));
        UndoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                UndoButtonMousePressed(evt);
            }
        });

        OkButton.setBackground(new java.awt.Color(204, 204, 204));
        OkButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        OkButton.setForeground(new java.awt.Color(238, 184, 56));
        OkButton.setText("GIOCA");
        OkButton.setToolTipText("");
        OkButton.setMaximumSize(new java.awt.Dimension(400, 50));
        OkButton.setMinimumSize(new java.awt.Dimension(400, 50));
        OkButton.setPreferredSize(new java.awt.Dimension(400, 50));
        OkButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                OkButtonMousePressed(evt);
            }
        });

        javax.swing.GroupLayout BoardPanelLayout = new javax.swing.GroupLayout(BoardPanel);
        BoardPanel.setLayout(BoardPanelLayout);
        BoardPanelLayout.setHorizontalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BoardPanelLayout.createSequentialGroup()
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addGap(434, 434, 434)
                                .addComponent(TableCard5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TableCard6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(OkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(UndoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(BoardPanelLayout.createSequentialGroup()
                                        .addComponent(TableCard7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TableCard8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createSequentialGroup()
                                        .addComponent(Combination7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Combination8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)))
                                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TableCard9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Combination9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(BoardPanelLayout.createSequentialGroup()
                                        .addComponent(Combination10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(CPUTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                                .addComponent(Combination11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Combination12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(BoardPanelLayout.createSequentialGroup()
                                        .addComponent(TableCard10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(PlayerTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                                .addComponent(TableCard11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(TableCard12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addComponent(YourCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(YourCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(YourCard3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                .addComponent(CPUName1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44))
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addComponent(OpponentCard3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OpponentCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OpponentCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CPUName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(DeckLabelPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addGap(114, 114, 114)
                                .addComponent(DeckLabelCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(BoardPanelLayout.createSequentialGroup()
                                        .addComponent(TableCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TableCard2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TableCard3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TableCard4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(BoardPanelLayout.createSequentialGroup()
                                        .addComponent(Combination1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Combination2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Combination3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Combination4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Combination6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        BoardPanelLayout.setVerticalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DeckLabelCPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OpponentCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OpponentCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OpponentCard3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addComponent(CPUName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CPUTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Combination2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Combination3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TableCard1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard4, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard3, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard6, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard5, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard8, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard7, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard9, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)))
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TableCard11, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(TableCard12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(DeckLabelPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(YourCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(YourCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(YourCard3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(UndoButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OkButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addComponent(PlayerTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(CPUName1)))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
    }//GEN-LAST:event_OkButtonMousePressed

    public void GoToScores(Punteggio SPlayer,Punteggio SCPU,String winner)
    {
        ScoreFrame Scores = new ScoreFrame();
        Scores.DisplayScores(SPlayer, SCPU);
        Scores.SetGioco(Sessione);
        Scores.SetWinnerLabel(winner);
        Scores.setVisible(true);
        this.dispose();
    }

    public static void main(String args[]) 
    {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) 
            {
                if ("Nimbus".equals(info.getName())) 
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } 
        catch (ClassNotFoundException ex) 
        {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (InstantiationException ex) 
        {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (IllegalAccessException ex) 
        {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (javax.swing.UnsupportedLookAndFeelException ex) 
        {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        Board X = new Board();
        X.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BoardPanel;
    private javax.swing.JLabel CPUName;
    private javax.swing.JLabel CPUName1;
    private javax.swing.JLabel CPUTurnLabel;
    private javax.swing.JToggleButton Combination1;
    private javax.swing.JToggleButton Combination10;
    private javax.swing.JToggleButton Combination11;
    private javax.swing.JToggleButton Combination12;
    private javax.swing.JToggleButton Combination2;
    private javax.swing.JToggleButton Combination3;
    private javax.swing.JToggleButton Combination4;
    private javax.swing.JToggleButton Combination5;
    private javax.swing.JToggleButton Combination6;
    private javax.swing.JToggleButton Combination7;
    private javax.swing.JToggleButton Combination8;
    private javax.swing.JToggleButton Combination9;
    private javax.swing.JLabel DeckLabelCPU;
    private javax.swing.JLabel DeckLabelPlayer;
    private javax.swing.JButton OkButton;
    private javax.swing.JPanel OpponentCard1;
    private javax.swing.JPanel OpponentCard2;
    private javax.swing.JPanel OpponentCard3;
    private javax.swing.JLabel OpponentLabel1;
    private javax.swing.JLabel OpponentLabel2;
    private javax.swing.JLabel OpponentLabel3;
    private javax.swing.JLabel PlayerLabel1;
    private javax.swing.JLabel PlayerLabel2;
    private javax.swing.JLabel PlayerLabel3;
    private javax.swing.JLabel PlayerTurnLabel;
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
    private javax.swing.JButton UndoButton;
    private javax.swing.JPanel YourCard1;
    private javax.swing.JPanel YourCard2;
    private javax.swing.JPanel YourCard3;
    // End of variables declaration//GEN-END:variables
}
