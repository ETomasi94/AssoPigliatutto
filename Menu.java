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

import java.awt.event.WindowEvent;
/*
    @CLASS Menu

    @OVERVIEW Classe che implementa le proprietà ed il comportamento della finestra
              del menù iniziale di gioco
*/
public class Menu extends javax.swing.JFrame 
{

    boolean Testing = false;
    Options options;
    
    Gioco Actual;
    
    /*----METODO COSTRUTTORE----*/
    
    /*
        @METHOD Menu
    
        @OVERVIEW Metodo che inizializza un'istanza della classe con le sue componenti
    
        @RETURNS M : Istanza della classe Menu
    */
    public Menu() {
        initComponents();
        options = new Options();
        options.SetMenu(this);
        NameDisplayer.setText("Stai giocando come "+options.PlayerName);
    }
    
    /*----FINE METODO COSTRUTTORE----*/

    /**
     * @METHOD Update
     * 
     * @OVERVIEW Metodo che aggiorna la scritta relativa al giocatore attivo
     *           durante l'esecuzione corrente visualizzandone il nome.
     */
    public void Update()
    {
        NameDisplayer.setText("Stai giocando come "+options.PlayerName);
    }
    
    /**
     * @METHOD StartGame
     * 
     * @OVERVIEW Metodo che inizializza una sessione di gioco secondo determinati
     *           paramteri in input.
     * 
     * @param Test Valore booleano che indica se la partita da inizializzare
     *             è un test delle funzionalità di gioco (metodo FinalTest della classe Gioco).
     * @param Train Valore booleano che indica se nella partita da inizializzare
     *              l'avversario della CPU è un giocatore umano (nel caso Train sia false)
     *              o l'apprendista per rinforzo (nel caso Train sia true).
     * @param Times Intero che rappresenta, nel caso a giocare sia l'apprendista per rinforzo
     *              il numero di partite consecutive da giocare per migliorarne la strategia.
     */
    public void StartGame(boolean Test, boolean Train,int Times)
    {
        Board board = new Board();
        board.NewGame(Test,Train,options.Profilo);
        board.Sessione.SetTimesToTrain(Times);
        Actual = board.Sessione;
        board.setVisible(true);
        this.dispose();
    }
    /*----METODI DI INTERAZIONE CON L'INTERFACCIA----*/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MenuPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        QuitButton = new javax.swing.JButton();
        StartButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        TestButton = new javax.swing.JButton();
        LearnerButton = new javax.swing.JButton();
        NameDisplayer = new javax.swing.JLabel();
        TrainingButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ASSO PIGLIATUTTO");
        setMinimumSize(new java.awt.Dimension(1300, 605));
        setResizable(false);
        setSize(new java.awt.Dimension(1300, 605));

        MenuPanel.setBackground(new java.awt.Color(51, 204, 0));
        MenuPanel.setAlignmentX(0.0F);
        MenuPanel.setAlignmentY(0.0F);
        MenuPanel.setMaximumSize(new java.awt.Dimension(900, 600));
        MenuPanel.setMinimumSize(new java.awt.Dimension(900, 600));
        MenuPanel.setPreferredSize(new java.awt.Dimension(900, 600));

        jLabel1.setFont(new java.awt.Font("Ravie", 0, 60)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ASSO PIGLIA TUTTO");

        QuitButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        QuitButton.setText("ESCI");
        QuitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                QuitButtonMousePressed(evt);
            }
        });

        StartButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        StartButton.setText("NUOVA PARTITA");
        StartButton.setMaximumSize(new java.awt.Dimension(150, 30));
        StartButton.setMinimumSize(new java.awt.Dimension(150, 30));
        StartButton.setPreferredSize(new java.awt.Dimension(150, 30));
        StartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                StartButtonMousePressed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CardSkins/Menu icon.png"))); // NOI18N

        TestButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TestButton.setText("TEST ");
        TestButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TestButtonMousePressed(evt);
            }
        });

        LearnerButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LearnerButton.setText("OPZIONI");
        LearnerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                LearnerButtonMousePressed(evt);
            }
        });
        LearnerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LearnerButtonActionPerformed(evt);
            }
        });

        NameDisplayer.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        NameDisplayer.setForeground(new java.awt.Color(255, 51, 51));
        NameDisplayer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        TrainingButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TrainingButton.setText("GUARDAMI GIOCARE");
        TrainingButton.setMaximumSize(new java.awt.Dimension(150, 30));
        TrainingButton.setMinimumSize(new java.awt.Dimension(150, 30));
        TrainingButton.setPreferredSize(new java.awt.Dimension(150, 30));
        TrainingButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TrainingButtonMousePressed(evt);
            }
        });

        javax.swing.GroupLayout MenuPanelLayout = new javax.swing.GroupLayout(MenuPanel);
        MenuPanel.setLayout(MenuPanelLayout);
        MenuPanelLayout.setHorizontalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1092, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MenuPanelLayout.createSequentialGroup()
                                .addGap(157, 157, 157)
                                .addComponent(NameDisplayer, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(199, 199, 199)))
                        .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(QuitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(StartButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TestButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LearnerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TrainingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        MenuPanelLayout.setVerticalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuPanelLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(StartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LearnerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TrainingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(QuitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(NameDisplayer, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /*
        @METHOD QuitButtonMousePressed
    
        @OVERVIEW Metodo che implementa la pressione del pulsante QuitButton ("ESCI") e la
                  conseguente uscita dal gioco
    */
    private void QuitButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QuitButtonMousePressed
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_QuitButtonMousePressed

   
    /*
        @METHOD TestButtonMousePressed
    
        @OVERVIEW Metodo che implementa la pressione del pulsante TestButton ("TEST")
                  ed il conseguente inizio del test di una nuova sessione di gioco
    */
    private void TestButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TestButtonMousePressed
        StartGame(true,false,0);
    }//GEN-LAST:event_TestButtonMousePressed
    /*
        @METHOD StartButtonMousePressed
    
        @OVERVIEW Metodo che implementa la pressione del pulsante StartButton ("NUOVA PARTITA")
                  ed il conseguente inizio di una nuova sessione di gioco
     */
    private void StartButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StartButtonMousePressed
        StartGame(false,false,0);
    }//GEN-LAST:event_StartButtonMousePressed

      /*
        @METHOD LearnerButtonMousePressed
    
        @OVERVIEW Metodo che implementa la scelta di configurare il learner di gioco
                  (un reinforcement learner) mediante il menù contestuale appropriato
     */
    private void LearnerButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LearnerButtonMousePressed
       options.setVisible(true);
    }//GEN-LAST:event_LearnerButtonMousePressed

    private void LearnerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LearnerButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LearnerButtonActionPerformed

    private void TrainingButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TrainingButtonMousePressed

        StartGame(false,true,100);
    }//GEN-LAST:event_TrainingButtonMousePressed

    /*----FINE METODI DI INTERAZIONE CON L'INTERFACCIA----*/
    
    /*----METODI DEL CICLO DI VITA DEL THREAD----*/
    
    /*
        @METHOD main
    
        @OVERVIEW Metodo che implementa il ciclo di vita della finestra
                  del menù iniziale di gioco
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
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }
    
    /*----FINE METODI DEL CICLO DI VITA DEL THREAD----*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LearnerButton;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JLabel NameDisplayer;
    private javax.swing.JButton QuitButton;
    private javax.swing.JButton StartButton;
    private javax.swing.JButton TestButton;
    private javax.swing.JButton TrainingButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
