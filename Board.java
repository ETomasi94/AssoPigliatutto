
package assopigliatutto;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public final class Board extends javax.swing.JFrame 
{
    Gioco Sessione;
    
    Giocatore CPU;
    Giocatore Player;
    
    ArrayList<Slot> PlayerHand;
    ArrayList<Slot> CPUHand;
    ArrayList<Slot> CardsOnTable;
    
    CombinationChooser Chs = new CombinationChooser(this,Player);
    
    TesterDebugger Test = new TesterDebugger(this);
    
    int TakenOnTable;
    int FreeOnTable;
    
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
    
    public Board() 
    {
        Sessione = new Gioco();

        initComponents();
        
        CPU = Sessione.GetCPU();
        Player = Sessione.GetPlayer();
        
        InizializzaSlot();

        NewGame();
    }
    
    public void NewGame()
    {       
       Random r = new Random();
       
       Sessione.NuovoGioco();
       
       Display();
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
        
        CardsOnTable.get(0).SetMarkLabel(Mark1);
        CardsOnTable.get(1).SetMarkLabel(Mark2);
        CardsOnTable.get(2).SetMarkLabel(Mark3);
        CardsOnTable.get(3).SetMarkLabel(Mark4);      
        CardsOnTable.get(4).SetMarkLabel(Mark5);
        CardsOnTable.get(5).SetMarkLabel(Mark6);
        CardsOnTable.get(6).SetMarkLabel(Mark7);
        CardsOnTable.get(7).SetMarkLabel(Mark8);
        CardsOnTable.get(8).SetMarkLabel(Mark9);
        CardsOnTable.get(9).SetMarkLabel(Mark10);
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
    
    public void ColorMark(int Mark,Slot S)
    {
        JLabel Indicator = S.GetMarkLabel();
        
        switch (Mark) 
        {
            case 1:
                Indicator.setForeground(C1);
                Indicator.setText("1");
                break;
            case 2:
                Indicator.setForeground(C2);
                Indicator.setText("2");
                break;
            case 3:
                Indicator.setForeground(C3);
                Indicator.setText("3");
                break;
            case 4:
                Indicator.setForeground(C4);
                Indicator.setText("4");
                break;
            case 5:
                Indicator.setForeground(C5);
                Indicator.setText("5");
                break;
            case 6:
                Indicator.setForeground(C6);
                Indicator.setText("6");
                break;
            case 7:
                Indicator.setForeground(C7);
                Indicator.setText("7");
                break;
            case 8:
                Indicator.setForeground(C8);
                Indicator.setText("8");
                break;
            case 9:
                Indicator.setForeground(C9);
                Indicator.setText("9");
                break;
            case 10:
                Indicator.setForeground(C10);
                Indicator.setText("10");
                break;
            case 11:
                Indicator.setForeground(C11);
                Indicator.setText("11");
                break;
            default:
                Indicator.setForeground(C11.brighter());
                Indicator.setText("UNK");
                break;
        }
    }
    
    public void HideMark(Slot S)
    {
        JLabel MarkLabel = S.GetMarkLabel();
            
        MarkLabel.setText("");
    }
    
    public void ResetMarks()
    {
        for(Slot S : CardsOnTable)
        {
            HideMark(S);
        }
    }
    
    public synchronized void HighlightCards(Carta card,int M)
    {
        ResetMarks();

            ConcurrentSkipListMap<Integer,ArrayList<Carta>> Potenziale = card.Potenziale;
            
            if(Potenziale.containsKey(M))
            {
                ArrayList<Carta> Selected = Potenziale.get(M);            
                
                for(Carta C : Selected)
                {
                    Slot TSlot = C.GetSlot();

                    ColorMark(M,TSlot);
                }
            }
    
    }
    
    public void PlayCard(int combinazione,Carta C)
    {
        Sessione.GiocaCarta(Player,combinazione, C);
        Sessione.RivalutaPotenziale();
        
        Display();
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
    
    public synchronized void PlayCard0()
    {
        if(Player.YourTurn())
        {
            if(PlayerHand.get(0).HasCard())
            {
                PlayerHand.get(0).Card.StampaPotenziale();
                Chs.Start(PlayerHand.get(0));
            }
        }
    }
    
    public synchronized void PlayCard1()
    {
          if(Player.YourTurn()) 
       {
        if(PlayerHand.get(1).HasCard())
        {
            PlayerHand.get(1).Card.StampaPotenziale();
            Chs.Start(PlayerHand.get(1));
        }
       }
    }
    
    public synchronized void PlayCard2()
    {
        if(Player.YourTurn())
       {
        if(PlayerHand.get(2).HasCard())
        {
            PlayerHand.get(2).Card.StampaPotenziale();
            Chs.Start(PlayerHand.get(2));
        }
       }
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
        Mark1 = new javax.swing.JLabel();
        Mark2 = new javax.swing.JLabel();
        Mark3 = new javax.swing.JLabel();
        Mark4 = new javax.swing.JLabel();
        Mark5 = new javax.swing.JLabel();
        Mark6 = new javax.swing.JLabel();
        Mark7 = new javax.swing.JLabel();
        Mark8 = new javax.swing.JLabel();
        Mark9 = new javax.swing.JLabel();
        Mark10 = new javax.swing.JLabel();
        CPUTurnLabel = new javax.swing.JLabel();
        PlayerTurnLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ASSO PIGLIATUTTO");
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(900, 600));
        setMinimumSize(new java.awt.Dimension(900, 600));
        setName("Board"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(900, 600));

        BoardPanel.setBackground(new java.awt.Color(51, 153, 0));
        BoardPanel.setForeground(new java.awt.Color(51, 204, 0));
        BoardPanel.setAlignmentX(0.0F);
        BoardPanel.setAlignmentY(0.0F);
        BoardPanel.setMaximumSize(new java.awt.Dimension(900, 600));
        BoardPanel.setMinimumSize(new java.awt.Dimension(900, 600));
        BoardPanel.setPreferredSize(new java.awt.Dimension(900, 600));

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

        Mark1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Mark2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Mark3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Mark4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Mark5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Mark6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Mark7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Mark8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Mark9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Mark10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Mark10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        CPUTurnLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        CPUTurnLabel.setForeground(new java.awt.Color(102, 255, 0));
        CPUTurnLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        PlayerTurnLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        PlayerTurnLabel.setForeground(new java.awt.Color(255, 0, 0));
        PlayerTurnLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout BoardPanelLayout = new javax.swing.GroupLayout(BoardPanel);
        BoardPanel.setLayout(BoardPanelLayout);
        BoardPanelLayout.setHorizontalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BoardPanelLayout.createSequentialGroup()
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addComponent(DeckLabelPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CPUName1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(BoardPanelLayout.createSequentialGroup()
                                        .addGap(76, 76, 76)
                                        .addComponent(DeckLabelCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(BoardPanelLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(Mark1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(Mark2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)
                                        .addComponent(Mark3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)
                                        .addComponent(Mark4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(44, 44, 44)
                                        .addComponent(Mark5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(36, 36, 36)
                                .addComponent(Mark6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(188, 188, 188)
                                .addComponent(CPUName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TableCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TableCard2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TableCard3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TableCard4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addComponent(TableCard5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TableCard6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(Mark7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(Mark8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(CPUTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createSequentialGroup()
                                                .addComponent(Mark9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(34, 34, 34)
                                                .addComponent(Mark10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(32, 32, 32))))
                                    .addGroup(BoardPanelLayout.createSequentialGroup()
                                        .addComponent(TableCard7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TableCard8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TableCard9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TableCard10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(14, 14, 14))))
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addComponent(OpponentCard3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OpponentCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OpponentCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addComponent(YourCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(YourCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(YourCard3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PlayerTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)))))
                .addContainerGap())
        );
        BoardPanelLayout.setVerticalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BoardPanelLayout.createSequentialGroup()
                                .addComponent(CPUName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CPUTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(DeckLabelCPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoardPanelLayout.createSequentialGroup()
                        .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(OpponentCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OpponentCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OpponentCard3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Mark2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mark4)
                    .addComponent(Mark3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mark6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mark5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mark8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mark7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mark10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mark9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mark1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TableCard1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(TableCard4, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(TableCard3, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(TableCard6, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(TableCard5, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(TableCard8, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(TableCard7, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(TableCard2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(TableCard9, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(TableCard10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(DeckLabelPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(YourCard2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(YourCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(YourCard3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(BoardPanelLayout.createSequentialGroup()
                        .addComponent(PlayerTurnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(CPUName1)))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1083, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PlayerLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel1MouseClicked
        PlayCard0();
    }//GEN-LAST:event_PlayerLabel1MouseClicked

    private void PlayerLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel2MouseClicked
        PlayCard1();
    }//GEN-LAST:event_PlayerLabel2MouseClicked

    private void PlayerLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel3MouseClicked
       PlayCard2();
    }//GEN-LAST:event_PlayerLabel3MouseClicked

    private void PlayerLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel1MouseEntered

    }//GEN-LAST:event_PlayerLabel1MouseEntered

    private void PlayerLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel2MouseEntered

    }//GEN-LAST:event_PlayerLabel2MouseEntered

    private void PlayerLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel3MouseEntered
   
    }//GEN-LAST:event_PlayerLabel3MouseEntered

    private void PlayerLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel1MouseExited

    }//GEN-LAST:event_PlayerLabel1MouseExited

    private void PlayerLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel2MouseExited

    }//GEN-LAST:event_PlayerLabel2MouseExited

    private void PlayerLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayerLabel3MouseExited

    }//GEN-LAST:event_PlayerLabel3MouseExited


    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Board().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BoardPanel;
    private javax.swing.JLabel CPUName;
    private javax.swing.JLabel CPUName1;
    private javax.swing.JLabel CPUTurnLabel;
    private javax.swing.JLabel DeckLabelCPU;
    private javax.swing.JLabel DeckLabelPlayer;
    private javax.swing.JLabel Mark1;
    private javax.swing.JLabel Mark10;
    private javax.swing.JLabel Mark2;
    private javax.swing.JLabel Mark3;
    private javax.swing.JLabel Mark4;
    private javax.swing.JLabel Mark5;
    private javax.swing.JLabel Mark6;
    private javax.swing.JLabel Mark7;
    private javax.swing.JLabel Mark8;
    private javax.swing.JLabel Mark9;
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
    private javax.swing.JLabel TableCard2;
    private javax.swing.JLabel TableCard3;
    private javax.swing.JLabel TableCard4;
    private javax.swing.JLabel TableCard5;
    private javax.swing.JLabel TableCard6;
    private javax.swing.JLabel TableCard7;
    private javax.swing.JLabel TableCard8;
    private javax.swing.JLabel TableCard9;
    private javax.swing.JPanel YourCard1;
    private javax.swing.JPanel YourCard2;
    private javax.swing.JPanel YourCard3;
    // End of variables declaration//GEN-END:variables
}
