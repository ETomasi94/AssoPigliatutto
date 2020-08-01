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

/*
    @CLASS ScoreFrame

    @OVERVIEW Classe che implementa la finestra relativa alla visualizzazione dei punteggi
              con le sue proprietà ed i suoi comportamenti
*/
public class ScoreFrame extends javax.swing.JFrame 
{
    /*----VARIABILI D'ISTANZA----*/
    
    Punteggio ScorePlayer;//Punteggio del giocatore
    Punteggio ScoreCPU;//Punteggio della CPU
    
    Gioco Sessione;//Sessione di gioco corrente
    
    /*----FINE VARIABILI D'ISTANZA----*/
    
    /*----METODO COSTRUTTORE----*/

    /*
        @METHOD ScoreFrame
        
        @OVERVIEW Metodo che costruisce un'istanza della classe ScoreFrame inizializzando
                  le sue componenti
    
        @RETURNS Frame : Istanza della classe ScoreFrame rappresentante la finestra di visualizzazione
                 punteggi corrente
    */
    public ScoreFrame() 
    {
        initComponents();
    }
    
    /*----FINE METODO COSTRUTTORE----*/
    
    /*----METODI DI MODIFICA DELLA FINESTRA----*/
    
    /*
        @METHOD SetGioco
    
        @OVERVIEW Metodo che imposta la sessione di gioco corrente ad un'istanza
                  della classe gioco data in input
    
        @PAR G : Istanza della classe Gioco rappresentante la sessione di gioco corrente
    */
    public void SetGioco(Gioco G)
    {
        Sessione = G;
    }
    
    /*
        @METHOD DisplayScores
    
        @OVERVIEW Metodo che visualizza i punteggi degli avversari all'interno della
                  finestra dell'istanza della classe in questione
    
        @PAR SPlayer : Punteggio corrente del giocatore
        @PAR SCPU : Punteggio corrente della CPU
    */
    public void DisplayScores(Punteggio SPlayer,Punteggio SCPU)
    {
        ScorePlayer = SPlayer;
        ScoreCPU = SCPU;
        
        TakenPlayer.setText(""+ScorePlayer.GetTotal());
        TakenCPU.setText(""+ScoreCPU.GetTotal());
        
        CoinsPlayer.setText(""+ScorePlayer.GetDenari());
        CoinsCPU.setText(""+ScoreCPU.GetDenari());
        
        SettebelloPlayer.setText(Settebello(ScorePlayer));
        SettebelloCPU.setText(Settebello(ScoreCPU));
        
        ScopePlayer.setText(""+ScorePlayer.GetScope());
        ScopeCPU.setText(""+ScoreCPU.GetScope());
        
        PrimieraOfPlayer.setText(""+ScorePlayer.GetPrimiera());
        PrimieraOfCPU.setText(""+ScoreCPU.GetPrimiera());
    }
    
    /*
        @METHOD Settebello
    
        @OVERVIEW Metodo che stampa, in base alla variabile d'istanza relativa alla presa del
                  Settebello da parte del giocatore contenuta nel punteggio ad egli associato
                  una stringa rappresentante il fatto che il Settebello sia stato preso o meno
    
        @PAR Score : Punteggio relativamente a cui dichiarare la presa o meno del settebello
    */
    private String Settebello(Punteggio Score)
    {
        String result;
        
        if(Score.Settebello)
        {
            result = "PRESO";
        }
        else
        {
            result = "NON PRESO";
        }
        
        return result;
    }
    
    /*
        @METHOD SetWinnerLabel
    
        @OVERVIEW Metodo che consente la visualizzazione di una stringa che denota
                  quale avversario ha vinto la partita o se c'è stato un pareggio
    
        @PAR nome : Nome dell'avversario che ha vinto la partita (uguale a "DRAW" in caso di pareggio)
     */
    public void SetWinnerLabel(String nome)
    {
        String text = "";
        
        WinnerLabel.setBackground(Color.white);
        
        if(nome.equals("CPU"))
        {
            text = "HA VINTO LA CPU!";
        }
        else if (nome.equals("DRAW"))
        {
            text = "C'E' STATO UN PAREGGIO!";
        }
        else
        {
            text = "HA VINTO IL GIOCATORE";
        }
        
        WinnerLabel.setText(text);
    }
    
    /*----FINE METODI DI MODIFICA DELLA FINESTRA----*/
    
    /*----METODI DI INTERAZIONE CON L'INTERFACCIA----*/

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ScoreFramePanel = new javax.swing.JPanel();
        Titolo = new javax.swing.JLabel();
        CPUName = new javax.swing.JLabel();
        PlayerL = new javax.swing.JLabel();
        SettebelloCheck = new javax.swing.JLabel();
        CartePrese = new javax.swing.JLabel();
        DenariPresi = new javax.swing.JLabel();
        PrimieraTotalizzata = new javax.swing.JLabel();
        ScopeOttenute = new javax.swing.JLabel();
        TakenPlayer = new javax.swing.JLabel();
        CoinsCPU = new javax.swing.JLabel();
        PrimieraOfPlayer = new javax.swing.JLabel();
        TakenCPU = new javax.swing.JLabel();
        CoinsPlayer = new javax.swing.JLabel();
        SettebelloPlayer = new javax.swing.JLabel();
        ScopePlayer = new javax.swing.JLabel();
        ScopeCPU = new javax.swing.JLabel();
        SettebelloCPU = new javax.swing.JLabel();
        PrimieraOfCPU = new javax.swing.JLabel();
        QuitButton = new javax.swing.JButton();
        BackToMenuButton1 = new javax.swing.JButton();
        WinnerLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ASSO PIGLIATUTTO");
        setMaximumSize(new java.awt.Dimension(1300, 605));
        setMinimumSize(new java.awt.Dimension(1300, 605));
        setSize(new java.awt.Dimension(1300, 605));

        ScoreFramePanel.setBackground(new java.awt.Color(51, 153, 0));
        ScoreFramePanel.setMaximumSize(new java.awt.Dimension(1300, 605));
        ScoreFramePanel.setMinimumSize(new java.awt.Dimension(1300, 605));
        ScoreFramePanel.setPreferredSize(new java.awt.Dimension(1300, 605));

        Titolo.setFont(new java.awt.Font("Ravie", 0, 48)); // NOI18N
        Titolo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Titolo.setText("ECCO I RISULTATI DELLA PARTITA");
        Titolo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        CPUName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CPUName.setForeground(new java.awt.Color(255, 153, 0));
        CPUName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CPUName.setText("CPU");

        PlayerL.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        PlayerL.setForeground(new java.awt.Color(153, 0, 153));
        PlayerL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PlayerL.setText("GIOCATORE");

        SettebelloCheck.setFont(new java.awt.Font("Sitka Banner", 1, 24)); // NOI18N
        SettebelloCheck.setForeground(new java.awt.Color(51, 204, 255));
        SettebelloCheck.setText("SETTEBELLO OTTENUTO");
        SettebelloCheck.setMaximumSize(new java.awt.Dimension(220, 60));
        SettebelloCheck.setMinimumSize(new java.awt.Dimension(220, 60));
        SettebelloCheck.setPreferredSize(new java.awt.Dimension(220, 60));

        CartePrese.setFont(new java.awt.Font("Sitka Banner", 1, 24)); // NOI18N
        CartePrese.setForeground(new java.awt.Color(51, 204, 255));
        CartePrese.setText("CARTE PRESE");
        CartePrese.setMaximumSize(new java.awt.Dimension(220, 60));
        CartePrese.setMinimumSize(new java.awt.Dimension(220, 60));
        CartePrese.setPreferredSize(new java.awt.Dimension(220, 60));

        DenariPresi.setFont(new java.awt.Font("Sitka Banner", 1, 24)); // NOI18N
        DenariPresi.setForeground(new java.awt.Color(51, 204, 255));
        DenariPresi.setText("DENARI PRESI");
        DenariPresi.setMaximumSize(new java.awt.Dimension(220, 60));
        DenariPresi.setMinimumSize(new java.awt.Dimension(220, 60));
        DenariPresi.setPreferredSize(new java.awt.Dimension(220, 60));

        PrimieraTotalizzata.setFont(new java.awt.Font("Sitka Banner", 1, 24)); // NOI18N
        PrimieraTotalizzata.setForeground(new java.awt.Color(51, 204, 255));
        PrimieraTotalizzata.setText("PUNTI DI PRIMIERA");
        PrimieraTotalizzata.setMaximumSize(new java.awt.Dimension(220, 60));
        PrimieraTotalizzata.setMinimumSize(new java.awt.Dimension(220, 60));
        PrimieraTotalizzata.setPreferredSize(new java.awt.Dimension(220, 60));

        ScopeOttenute.setFont(new java.awt.Font("Sitka Banner", 1, 24)); // NOI18N
        ScopeOttenute.setForeground(new java.awt.Color(51, 204, 255));
        ScopeOttenute.setText("SCOPE OTTENUTE");
        ScopeOttenute.setMaximumSize(new java.awt.Dimension(220, 60));
        ScopeOttenute.setMinimumSize(new java.awt.Dimension(220, 60));
        ScopeOttenute.setPreferredSize(new java.awt.Dimension(220, 60));

        TakenPlayer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        TakenPlayer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TakenPlayer.setAlignmentY(0.0F);
        TakenPlayer.setMaximumSize(new java.awt.Dimension(160, 30));
        TakenPlayer.setMinimumSize(new java.awt.Dimension(160, 30));
        TakenPlayer.setPreferredSize(new java.awt.Dimension(160, 30));

        CoinsCPU.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CoinsCPU.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CoinsCPU.setAlignmentY(0.0F);
        CoinsCPU.setMaximumSize(new java.awt.Dimension(160, 30));
        CoinsCPU.setMinimumSize(new java.awt.Dimension(160, 30));
        CoinsCPU.setPreferredSize(new java.awt.Dimension(160, 30));

        PrimieraOfPlayer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        PrimieraOfPlayer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PrimieraOfPlayer.setAlignmentY(0.0F);
        PrimieraOfPlayer.setMaximumSize(new java.awt.Dimension(160, 30));
        PrimieraOfPlayer.setMinimumSize(new java.awt.Dimension(160, 30));
        PrimieraOfPlayer.setPreferredSize(new java.awt.Dimension(160, 30));

        TakenCPU.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        TakenCPU.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TakenCPU.setAlignmentY(0.0F);
        TakenCPU.setMaximumSize(new java.awt.Dimension(160, 30));
        TakenCPU.setMinimumSize(new java.awt.Dimension(160, 30));
        TakenCPU.setPreferredSize(new java.awt.Dimension(160, 30));

        CoinsPlayer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CoinsPlayer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CoinsPlayer.setAlignmentY(0.0F);
        CoinsPlayer.setMaximumSize(new java.awt.Dimension(160, 30));
        CoinsPlayer.setMinimumSize(new java.awt.Dimension(160, 30));
        CoinsPlayer.setPreferredSize(new java.awt.Dimension(160, 30));

        SettebelloPlayer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        SettebelloPlayer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SettebelloPlayer.setAlignmentY(0.0F);
        SettebelloPlayer.setMaximumSize(new java.awt.Dimension(160, 30));
        SettebelloPlayer.setMinimumSize(new java.awt.Dimension(160, 30));
        SettebelloPlayer.setPreferredSize(new java.awt.Dimension(160, 30));

        ScopePlayer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        ScopePlayer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ScopePlayer.setAlignmentY(0.0F);
        ScopePlayer.setMaximumSize(new java.awt.Dimension(160, 30));
        ScopePlayer.setMinimumSize(new java.awt.Dimension(160, 30));
        ScopePlayer.setPreferredSize(new java.awt.Dimension(160, 30));

        ScopeCPU.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        ScopeCPU.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ScopeCPU.setAlignmentY(0.0F);
        ScopeCPU.setMaximumSize(new java.awt.Dimension(160, 30));
        ScopeCPU.setMinimumSize(new java.awt.Dimension(160, 30));
        ScopeCPU.setPreferredSize(new java.awt.Dimension(160, 30));

        SettebelloCPU.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        SettebelloCPU.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SettebelloCPU.setAlignmentY(0.0F);
        SettebelloCPU.setMaximumSize(new java.awt.Dimension(160, 30));
        SettebelloCPU.setMinimumSize(new java.awt.Dimension(160, 30));
        SettebelloCPU.setPreferredSize(new java.awt.Dimension(160, 30));

        PrimieraOfCPU.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        PrimieraOfCPU.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PrimieraOfCPU.setAlignmentY(0.0F);
        PrimieraOfCPU.setMaximumSize(new java.awt.Dimension(160, 30));
        PrimieraOfCPU.setMinimumSize(new java.awt.Dimension(160, 30));
        PrimieraOfCPU.setPreferredSize(new java.awt.Dimension(160, 30));

        QuitButton.setBackground(new java.awt.Color(153, 153, 153));
        QuitButton.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        QuitButton.setText("ESCI");
        QuitButton.setMaximumSize(new java.awt.Dimension(270, 60));
        QuitButton.setMinimumSize(new java.awt.Dimension(270, 60));
        QuitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                QuitButtonMousePressed(evt);
            }
        });
        QuitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitButtonActionPerformed(evt);
            }
        });

        BackToMenuButton1.setBackground(new java.awt.Color(0, 204, 102));
        BackToMenuButton1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        BackToMenuButton1.setText("TORNA AL MENU");
        BackToMenuButton1.setMaximumSize(new java.awt.Dimension(270, 60));
        BackToMenuButton1.setMinimumSize(new java.awt.Dimension(270, 60));
        BackToMenuButton1.setPreferredSize(new java.awt.Dimension(300, 60));
        BackToMenuButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                BackToMenuButton1MousePressed(evt);
            }
        });
        BackToMenuButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackToMenuButton1ActionPerformed(evt);
            }
        });

        WinnerLabel.setFont(new java.awt.Font("Trajan Pro", 1, 36)); // NOI18N
        WinnerLabel.setForeground(new java.awt.Color(255, 255, 255));
        WinnerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout ScoreFramePanelLayout = new javax.swing.GroupLayout(ScoreFramePanel);
        ScoreFramePanel.setLayout(ScoreFramePanelLayout);
        ScoreFramePanelLayout.setHorizontalGroup(
            ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ScoreFramePanelLayout.createSequentialGroup()
                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ScoreFramePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Titolo, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE))
                    .addGroup(ScoreFramePanelLayout.createSequentialGroup()
                        .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ScoreFramePanelLayout.createSequentialGroup()
                                .addGap(233, 233, 233)
                                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(PrimieraOfPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CoinsPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(SettebelloPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ScopePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TakenPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PlayerL, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(ScoreFramePanelLayout.createSequentialGroup()
                                        .addGap(98, 98, 98)
                                        .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(DenariPresi, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(SettebelloCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(CartePrese, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ScoreFramePanelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(PrimieraTotalizzata, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(ScopeOttenute, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(99, 99, 99)
                                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PrimieraOfCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TakenCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CoinsCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ScopeCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(SettebelloCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CPUName, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(ScoreFramePanelLayout.createSequentialGroup()
                                .addGap(374, 374, 374)
                                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(WinnerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(ScoreFramePanelLayout.createSequentialGroup()
                                        .addComponent(BackToMenuButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(QuitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ScoreFramePanelLayout.setVerticalGroup(
            ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ScoreFramePanelLayout.createSequentialGroup()
                .addComponent(Titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PlayerL, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CPUName, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TakenPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TakenCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CartePrese, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CoinsPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DenariPresi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CoinsCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SettebelloPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SettebelloCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SettebelloCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScopePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ScopeOttenute, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ScopeCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PrimieraTotalizzata, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PrimieraOfPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PrimieraOfCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(WinnerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ScoreFramePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(QuitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BackToMenuButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ScoreFramePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ScoreFramePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void QuitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitButtonActionPerformed

    }//GEN-LAST:event_QuitButtonActionPerformed

    private void BackToMenuButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackToMenuButton1ActionPerformed

    }//GEN-LAST:event_BackToMenuButton1ActionPerformed
    /**
     * @METHOD QuitButtonMousePressed
     * @OVERVIEW Metodo che implementa il comportamento della finestra di visualizzazione
     *           dei punteggi al momento della pressione del bottone QuitButton ("ESCI") e la
     *           conseguente chiusura del gioco
     */
    private void QuitButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QuitButtonMousePressed
        Sessione.Halt();
        this.dispose();
    }//GEN-LAST:event_QuitButtonMousePressed
/**
 * @METHOD BackToMenuButton1MousePressed
 * @OVERVIEW Metodo che implementa il comportamento della finestra di visualizzazione dei punteggi
 *           al momento della pressione del bottone BackToMenuButton1 ("TORNA AL MENU") e il conseguente
 *           ritorno al menu iniziale di gioco
 * 
 */
    private void BackToMenuButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackToMenuButton1MousePressed
        Menu menu = new Menu();
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BackToMenuButton1MousePressed

    /*----FINE METODI DI INTERAZIONE CON L'INTERFACCIA----*/
    
    /*----METODI DEL CICLO DI VITA DEL THREAD----*/
    
    /**
     * @METHOD main
     * 
     * @OVERVIEW Metodo che implementa il ciclo di vita principale della finestra di visualizzazione
     *           dei punteggi
     * 
    */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScoreFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScoreFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScoreFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScoreFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScoreFrame().setVisible(true);
            }
        });
    }
    
    /*----FINE METODI DEL CICLO DI VITA DEL THREAD----*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackToMenuButton1;
    private javax.swing.JLabel CPUName;
    private javax.swing.JLabel CartePrese;
    private javax.swing.JLabel CoinsCPU;
    private javax.swing.JLabel CoinsPlayer;
    private javax.swing.JLabel DenariPresi;
    private javax.swing.JLabel PlayerL;
    private javax.swing.JLabel PrimieraOfCPU;
    private javax.swing.JLabel PrimieraOfPlayer;
    private javax.swing.JLabel PrimieraTotalizzata;
    private javax.swing.JButton QuitButton;
    private javax.swing.JLabel ScopeCPU;
    private javax.swing.JLabel ScopeOttenute;
    private javax.swing.JLabel ScopePlayer;
    private javax.swing.JPanel ScoreFramePanel;
    private javax.swing.JLabel SettebelloCPU;
    private javax.swing.JLabel SettebelloCheck;
    private javax.swing.JLabel SettebelloPlayer;
    private javax.swing.JLabel TakenCPU;
    private javax.swing.JLabel TakenPlayer;
    private javax.swing.JLabel Titolo;
    private javax.swing.JLabel WinnerLabel;
    // End of variables declaration//GEN-END:variables
}
