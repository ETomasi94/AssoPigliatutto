/*
ASSO PIGLIATUTTO
PROGETTO DI ESPERIENZE DI PROGRAMMAZIONE A.A 2019-2020

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
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
    
    /*----METODO COSTRUTTORE----*/
    
    /*
        @METHOD Menu
    
        @OVERVIEW Metodo che inizializza un'istanza della classe con le sue componenti
    
        @RETURNS M : Istanza della classe Menu
    */
    public Menu() {
        initComponents();
    }
    
    /*----FINE METODO COSTRUTTORE----*/

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

        jLabel1.setFont(new java.awt.Font("Ravie", 0, 48)); // NOI18N
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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StartButtonMouseClicked(evt);
            }
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

        javax.swing.GroupLayout MenuPanelLayout = new javax.swing.GroupLayout(MenuPanel);
        MenuPanel.setLayout(MenuPanelLayout);
        MenuPanelLayout.setHorizontalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1092, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(148, Short.MAX_VALUE))
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(247, 247, 247)
                        .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(QuitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(StartButton, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(TestButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(58, 58, 58))))
        );
        MenuPanelLayout.setVerticalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(StartButton, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(TestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(QuitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

   
    private void StartButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StartButtonMouseClicked

    }//GEN-LAST:event_StartButtonMouseClicked

    /*
        @METHOD TestButtonMousePressed
    
        @OVERVIEW Metodo che implementa la pressione del pulsante TestButton ("TEST")
                  ed il conseguente inizio del test di una nuova sessione di gioco
    */
    private void TestButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TestButtonMousePressed
        Board board = new Board();
        board.NewGame(true);
        board.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_TestButtonMousePressed
    /*
        @METHOD StartButtonMousePressed
    
        @OVERVIEW Metodo che implementa la pressione del pulsante StartButton ("NUOVA PARTITA")
                  ed il conseguente inizio di una nuova sessione di gioco
     */
    private void StartButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StartButtonMousePressed
        Board board = new Board();
        board.NewGame(false);
        board.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_StartButtonMousePressed

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
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JButton QuitButton;
    private javax.swing.JButton StartButton;
    private javax.swing.JButton TestButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
