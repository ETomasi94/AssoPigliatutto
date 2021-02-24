/*
ASSO PIGLIATUTTO
TESI DI LAUREA A.A 2020 - 2021

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

/**
 * @author Enrico Tomasi
 * 
 * @CLASS Options
 * 
 * @OVERVIEW Classe che implementa il riquadro relativo alle opzioni di gioco.
 */
public class Options extends javax.swing.JFrame 
{
    Menu menu;//Menu in cui sono state aperte le opzioni
    String PlayerName;//Nome del giocatore corrente
    Profile Profilo;//Profilo del giocatore corrente
 
  /*----METODO COSTRUTTORE----*/
    /**
     * @METHOD Options
     * 
     * @OVERVIEW Metodo costruttore di un'istanza della classe Options
     */
    public Options() 
    {
        initComponents();
        Profilo = new Profile("Player");
        NameOfPlayer.setText("Player");
        LoadProfile();
        
        SetTextFields(Profilo);
    }

 /*----FINE METODO COSTRUTTORE----*/
    
 /*----METODI DI GESTIONE DEL PROFILO----*/
    /**
     * @METHOD CreateProfile
     * 
     * @OVERVIEW Metodo che, preso in input il nome dal campo di testo relativo
     *           al nome del giocatore, ne crea il rispettivo profilo se questi
     *           non è presente in memoria.
     */
    public void CreateProfile()
    {
        Profilo = new Profile(PlayerName);
        Profilo.Save();
        
        SetTextFields(Profilo);
    }
    
    /**
     * @METHOD SaveProfile
     * 
     * @OVERVIEW Metodo che, preso in input il nome del giocatore dal relativo
     *           campo di testo, ne crea il rispettivo profilo e lo imposta
     *           come profilo corrente.
     */
    public void SaveProfile()
    {
        PlayerName = NameOfPlayer.getText().trim();
        Profilo.SetName(PlayerName);
        Profilo.Save();
        LoadProfile();
    }
    
    /**
     * @METHOD LoadProfile
     * 
     * @OVERVIEW Metodo che, data una stringa in input rappresentata dal testo
     *           nella casella del nome del giocatore, ne carica il profilo.
     */
    public void LoadProfile()
    {
        PlayerName = NameOfPlayer.getText().trim();
        Profilo.Load(PlayerName);
        Profilo.name = PlayerName;
        ChangeName(Profilo.name);
        
        SetTextFields(Profilo);
        
    }
    
 /*----FINE METODI DI GESTIONE DEL PROFILO----*/   
    
/*----METODI GETTERS E SETTERS----*/  
    /**
     * @METHOD SetMenu
     * 
     * @OVERVIEW Metodo che memorizza nell'apposita variabile d'istanza il menu
     *           da cui si sono aperte le opzioni.
     * 
     * @param M Menu da cui si sono aperte le opzioni in input. 
     */
    public void SetMenu(Menu M)
    {
        menu = M;
    }
    
    /**
     * @METHOD ChangeName
     * 
     * @OVERVIEW Metodo che, data una stringa in input recepita dal campo di testo
     *           relativo al nome del giocatore, ne setta il nome secondo una data
     *           stringa in input.
     * 
     * @param name Stringa in input rappresentante il nuovo nome del giocatore
     *             da visualizzare nell'interfaccia di gioco.
     */
    public void ChangeName(String name)
    {
        NameOfPlayer.setText(name);
    }
    
    /**
     * @METHOD SetTextFields
     * 
     * @OVERVIEW Metodo che, dato in input uno specifico profilo, setta tutti
     *           i campi di testo del riquadro delle opzioni in modo da visualizzare
     *           il profilo richiesto dal giocatore e settarlo come profilo corrente
     *           da aggiornare ad ogni partita conclusa.
     * 
     * @param Profilo Profilo in input relativo ad un determinato giocatore. 
     */
    public void SetTextFields(Profile Profilo)
    {
        ProfileNameField.setText(Profilo.name);
        NumberOfStatesField.setText(String.valueOf(Profilo.Statistics.SeenStates));
        CreationDateField1.setText(Profilo.Statistics.TimeOfCreation);
        GamesPlayedField.setText(String.valueOf(Profilo.Statistics.GamesPlayed));
        GamesWonField.setText(String.valueOf(Profilo.Statistics.PlayerWinned));
        AccuracyField.setText(String.valueOf(Profilo.Statistics.PolicyMeanPlanningAccuracy / 10));
    }
            
/*----FINE METODI GETTERS E SETTERS----*/
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        NameOfPlayer = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();
        LoadButton = new javax.swing.JButton();
        ProfileNameField = new javax.swing.JTextField();
        NumberOfStatesField = new javax.swing.JTextField();
        GamesPlayedField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        CreationDateField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        GamesWonField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        AccuracyField = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("-NOME DEL GIOCATORE");

        NameOfPlayer.setText("Player");
        NameOfPlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameOfPlayerActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("- PROIFLO ATTUALE");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setText("OK");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        SaveButton.setText("SALVA PROFILO");
        SaveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                SaveButtonMousePressed(evt);
            }
        });

        LoadButton.setText("CARICA PROFILO");
        LoadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                LoadButtonMousePressed(evt);
            }
        });

        ProfileNameField.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ProfileNameField.setForeground(new java.awt.Color(255, 0, 0));
        ProfileNameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        NumberOfStatesField.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        NumberOfStatesField.setForeground(new java.awt.Color(255, 0, 0));
        NumberOfStatesField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setText("NOME DEL GIOCATORE");

        jLabel4.setText("STATI DEL DATABASE");

        jLabel5.setText("DATA CREAZIONE PROFILO");

        jLabel6.setText("PARTITE GIOCATE");

        jLabel7.setText("PARTITE VINTE");

        jLabel8.setText("VOTO GIOCATORE");

        AccuracyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AccuracyFieldActionPerformed(evt);
            }
        });

        jButton2.setText("RESETTA STATISTICHE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(NameOfPlayer))
                        .addComponent(jLabel1))
                    .addComponent(jLabel2))
                .addGap(0, 34, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NumberOfStatesField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LoadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ProfileNameField)
                    .addComponent(GamesPlayedField)
                    .addComponent(CreationDateField1)
                    .addComponent(GamesWonField)
                    .addComponent(AccuracyField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NameOfPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProfileNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NumberOfStatesField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CreationDateField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel6)
                .addGap(3, 3, 3)
                .addComponent(GamesPlayedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GamesWonField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AccuracyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SaveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LoadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NameOfPlayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameOfPlayerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameOfPlayerActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        PlayerName = NameOfPlayer.getText().trim();
        menu.Update();
        CreateProfile();
        this.dispose();
    }//GEN-LAST:event_jButton1MousePressed

    private void SaveButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveButtonMousePressed
        SaveProfile();
    }//GEN-LAST:event_SaveButtonMousePressed

    private void LoadButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoadButtonMousePressed
        LoadProfile();
    }//GEN-LAST:event_LoadButtonMousePressed

    private void AccuracyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AccuracyFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AccuracyFieldActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Profilo.Statistics.ResetStats();
        SetTextFields(Profilo);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
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
            java.util.logging.Logger.getLogger(Options.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Options.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Options.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Options.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Options().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AccuracyField;
    private javax.swing.JTextField CreationDateField1;
    private javax.swing.JTextField GamesPlayedField;
    private javax.swing.JTextField GamesWonField;
    private javax.swing.JButton LoadButton;
    private javax.swing.JTextField NameOfPlayer;
    private javax.swing.JTextField NumberOfStatesField;
    private javax.swing.JTextField ProfileNameField;
    private javax.swing.JButton SaveButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
