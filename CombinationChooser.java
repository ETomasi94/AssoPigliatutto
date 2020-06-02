/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assopigliatutto;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JToggleButton;

/**
 *
 * @author Enrico Tomasi
 */
public final class CombinationChooser extends javax.swing.JFrame {

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
    
    ArrayList<JToggleButton> SwitchList = new ArrayList();
    
    TesterDebugger Ts;
    
    Board BoardGame;
    
    Slot CardSlot;
    
    Carta Selected;
    
    Giocatore Player;
    
    int ChoosenMark;
    
    private static Boolean End;
    
    public CombinationChooser(Board B,Giocatore P) 
    {
        Player = P;
        
        BoardGame = B;
        
        initComponents();
    }
    
    public synchronized void Start(Slot S)
    {       
        CardSlot = S;

        AnalyzeSlot();

        this.setVisible(true);
        
        System.out.println("CARTA SELEZIONATA: "+S.Card.nome);
    }
    
    public synchronized void AnalyzeSlot()
    {
        Selected = CardSlot.GetCard();
        
        if(Selected.HasPotential(1))
        {
            Combination1.setBackground(C1.darker());
            SwitchList.add(Combination1);
        }
        else
        {
            Combination1.setVisible(false);
        }
        
        if(Selected.HasPotential(2))
        {
            Combination2.setBackground(C2.darker());
            SwitchList.add(Combination2);
        }
        else
        {
            Combination2.setVisible(false);
        }
        
        if(Selected.HasPotential(3))
        {
            Combination3.setBackground(C3.darker());
            SwitchList.add(Combination3);
        }
        else
        {
            Combination3.setVisible(false);
        }
        
        if(Selected.HasPotential(4))
        {
            Combination4.setBackground(C4.darker());
            SwitchList.add(Combination4);
        }
        else
        {
            Combination4.setVisible(false);
        }
        
        if(Selected.HasPotential(5))
        {
            Combination5.setBackground(C5.darker());
            SwitchList.add(Combination5);
        }
        else
        {
            Combination5.setVisible(false);
        }
        
        if(Selected.HasPotential(6))
        {
            Combination6.setBackground(C6.darker()); 
            SwitchList.add(Combination6);
        }
        else
        {
            Combination6.setVisible(false);
        }
        
        if(Selected.HasPotential(7))
        {
            Combination7.setBackground(C7.darker()); 
            SwitchList.add(Combination7);
        }
        else
        {
            Combination7.setVisible(false);
        }
        
        if(Selected.HasPotential(8))
        {
            Combination8.setBackground(C8.darker());
            SwitchList.add(Combination8);
        }
        else
        {
            Combination8.setVisible(false);
        }
        
        if(Selected.HasPotential(9))
        {
            Combination9.setBackground(C9.darker());
            SwitchList.add(Combination9);
        }
        else
        {
            Combination9.setVisible(false);
        }
        
        if(Selected.HasPotential(10))
        {
            Combination10.setBackground(C10.darker());
            SwitchList.add(Combination10);
        }
        else
        {
            Combination10.setVisible(false);
        }
        
        if(Selected.HasPotential(11))
        {
           Combination11.setBackground(C11.darker());
           SwitchList.add(Combination11);
        }
        else
        {
            Combination11.setVisible(false);
        }
    }
    
    
    public void Terminate()
    {
        UnClickAll();
        CardSlot = null;
        BoardGame.ResetMarks();
        this.setVisible(false);
    }
    
    public void UnClickAll()
    {
        for(JToggleButton T : SwitchList)
        {
            if(T.isSelected())
            {
                T.doClick();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ButtonPanel = new javax.swing.JPanel();
        ChooserTitle = new javax.swing.JLabel();
        OkButton = new javax.swing.JButton();
        UndoButton = new javax.swing.JButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SCEGLI LA TUA COMBINAZIONE");
        setMaximumSize(new java.awt.Dimension(1200, 150));
        setMinimumSize(new java.awt.Dimension(1200, 150));
        setPreferredSize(new java.awt.Dimension(1200, 150));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        ButtonPanel.setMaximumSize(new java.awt.Dimension(1500, 100));
        ButtonPanel.setMinimumSize(new java.awt.Dimension(1500, 100));
        ButtonPanel.setPreferredSize(new java.awt.Dimension(1500, 100));
        ButtonPanel.setLayout(new java.awt.BorderLayout());

        ChooserTitle.setBackground(new java.awt.Color(102, 153, 255));
        ChooserTitle.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        ChooserTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChooserTitle.setText("Scelta della combinazione");
        ChooserTitle.setMaximumSize(new java.awt.Dimension(50, 30));
        ChooserTitle.setMinimumSize(new java.awt.Dimension(50, 30));
        ChooserTitle.setName(""); // NOI18N
        ChooserTitle.setPreferredSize(new java.awt.Dimension(50, 30));
        ButtonPanel.add(ChooserTitle, java.awt.BorderLayout.CENTER);

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
        ButtonPanel.add(OkButton, java.awt.BorderLayout.PAGE_END);

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
        ButtonPanel.add(UndoButton, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(ButtonPanel);

        Combination1.setBackground(new java.awt.Color(102, 255, 102));
        Combination1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination1.setForeground(new java.awt.Color(204, 204, 204));
        Combination1.setText("1");
        Combination1.setAlignmentY(0.0F);
        Combination1.setMaximumSize(new java.awt.Dimension(30, 30));
        Combination1.setMinimumSize(new java.awt.Dimension(30, 30));
        Combination1.setPreferredSize(new java.awt.Dimension(30, 30));
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
        getContentPane().add(Combination1);

        Combination2.setBackground(new java.awt.Color(102, 255, 102));
        Combination2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination2.setForeground(new java.awt.Color(204, 204, 204));
        Combination2.setText("2");
        Combination2.setAlignmentY(0.0F);
        Combination2.setMaximumSize(new java.awt.Dimension(30, 30));
        Combination2.setMinimumSize(new java.awt.Dimension(30, 30));
        Combination2.setPreferredSize(new java.awt.Dimension(30, 30));
        Combination2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination2MousePressed(evt);
            }
        });
        Combination2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination2ActionPerformed(evt);
            }
        });
        getContentPane().add(Combination2);

        Combination3.setBackground(new java.awt.Color(102, 255, 102));
        Combination3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination3.setForeground(new java.awt.Color(204, 204, 204));
        Combination3.setText("3");
        Combination3.setAlignmentY(0.0F);
        Combination3.setMaximumSize(new java.awt.Dimension(30, 30));
        Combination3.setMinimumSize(new java.awt.Dimension(30, 30));
        Combination3.setPreferredSize(new java.awt.Dimension(30, 30));
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
        getContentPane().add(Combination3);

        Combination4.setBackground(new java.awt.Color(102, 255, 102));
        Combination4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination4.setForeground(new java.awt.Color(204, 204, 204));
        Combination4.setText("4");
        Combination4.setAlignmentY(0.0F);
        Combination4.setMaximumSize(new java.awt.Dimension(30, 30));
        Combination4.setMinimumSize(new java.awt.Dimension(30, 30));
        Combination4.setPreferredSize(new java.awt.Dimension(30, 30));
        Combination4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination4MousePressed(evt);
            }
        });
        Combination4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination4ActionPerformed(evt);
            }
        });
        getContentPane().add(Combination4);

        Combination5.setBackground(new java.awt.Color(102, 255, 102));
        Combination5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination5.setForeground(new java.awt.Color(204, 204, 204));
        Combination5.setText("5");
        Combination5.setAlignmentY(0.0F);
        Combination5.setMaximumSize(new java.awt.Dimension(30, 30));
        Combination5.setMinimumSize(new java.awt.Dimension(30, 30));
        Combination5.setPreferredSize(new java.awt.Dimension(30, 30));
        Combination5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination5MousePressed(evt);
            }
        });
        Combination5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination5ActionPerformed(evt);
            }
        });
        getContentPane().add(Combination5);

        Combination6.setBackground(new java.awt.Color(102, 255, 102));
        Combination6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination6.setForeground(new java.awt.Color(204, 204, 204));
        Combination6.setText("6");
        Combination6.setAlignmentY(0.0F);
        Combination6.setMaximumSize(new java.awt.Dimension(30, 30));
        Combination6.setMinimumSize(new java.awt.Dimension(30, 30));
        Combination6.setPreferredSize(new java.awt.Dimension(30, 30));
        Combination6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination6MousePressed(evt);
            }
        });
        Combination6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combination6ActionPerformed(evt);
            }
        });
        getContentPane().add(Combination6);

        Combination7.setBackground(new java.awt.Color(102, 255, 102));
        Combination7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination7.setForeground(new java.awt.Color(204, 204, 204));
        Combination7.setText("7");
        Combination7.setToolTipText("");
        Combination7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination7MousePressed(evt);
            }
        });
        getContentPane().add(Combination7);

        Combination8.setBackground(new java.awt.Color(102, 255, 102));
        Combination8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination8.setForeground(new java.awt.Color(204, 204, 204));
        Combination8.setText("8");
        Combination8.setToolTipText("");
        Combination8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination8MousePressed(evt);
            }
        });
        getContentPane().add(Combination8);

        Combination9.setBackground(new java.awt.Color(102, 255, 102));
        Combination9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination9.setForeground(new java.awt.Color(204, 204, 204));
        Combination9.setText("9");
        Combination9.setToolTipText("");
        Combination9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination9MousePressed(evt);
            }
        });
        getContentPane().add(Combination9);

        Combination10.setBackground(new java.awt.Color(102, 255, 102));
        Combination10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination10.setForeground(new java.awt.Color(204, 204, 204));
        Combination10.setText("10");
        Combination10.setToolTipText("");
        Combination10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination10MousePressed(evt);
            }
        });
        getContentPane().add(Combination10);

        Combination11.setBackground(new java.awt.Color(102, 255, 102));
        Combination11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Combination11.setForeground(new java.awt.Color(204, 204, 204));
        Combination11.setText("11");
        Combination11.setToolTipText("");
        Combination11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Combination11MousePressed(evt);
            }
        });
        getContentPane().add(Combination11);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Combination1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination1ActionPerformed

    private void Combination2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination2ActionPerformed

    private void Combination3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination3ActionPerformed

    private void Combination4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination4ActionPerformed

    private void Combination5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination5ActionPerformed

    private void Combination6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combination6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Combination6ActionPerformed

    private void Combination1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination1MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,1);
       ChoosenMark = 1;
    }//GEN-LAST:event_Combination1MousePressed

    private void Combination2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination2MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,2);
       
        ChoosenMark = 2;
    }//GEN-LAST:event_Combination2MousePressed

    private void Combination3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination3MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,3);
       
        ChoosenMark = 3;
    }//GEN-LAST:event_Combination3MousePressed

    private void Combination4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination4MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,4);
       
        ChoosenMark = 4;
    }//GEN-LAST:event_Combination4MousePressed

    private void Combination5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination5MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,5);
       
        ChoosenMark = 5;
    }//GEN-LAST:event_Combination5MousePressed

    private void Combination6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination6MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,6);
       
        ChoosenMark = 6;
    }//GEN-LAST:event_Combination6MousePressed

    private void UndoButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UndoButtonMousePressed
        Terminate();
    }//GEN-LAST:event_UndoButtonMousePressed

    private void Combination7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination7MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,7);
       
        ChoosenMark = 7;
    }//GEN-LAST:event_Combination7MousePressed

    private void Combination8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination8MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,8);
       
        ChoosenMark = 8;
    }//GEN-LAST:event_Combination8MousePressed

    private void Combination9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination9MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,9);
       
        ChoosenMark = 9;
    }//GEN-LAST:event_Combination9MousePressed

    private void Combination10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination10MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,10);
       
        ChoosenMark = 10;
    }//GEN-LAST:event_Combination10MousePressed

    private void Combination11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Combination11MousePressed
       UnClickAll();
       BoardGame.HighlightCards(Selected,11);
       
        ChoosenMark = 11;
    }//GEN-LAST:event_Combination11MousePressed

    private void OkButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OkButtonMousePressed

        BoardGame.PlayCard(ChoosenMark, Selected);
        
        Terminate();
    }//GEN-LAST:event_OkButtonMousePressed

    public static void main(String args[]) 
    {
        End = false;
        
        while(!End)
        {
            
        }
        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JLabel ChooserTitle;
    private javax.swing.JToggleButton Combination1;
    private javax.swing.JToggleButton Combination10;
    private javax.swing.JToggleButton Combination11;
    private javax.swing.JToggleButton Combination2;
    private javax.swing.JToggleButton Combination3;
    private javax.swing.JToggleButton Combination4;
    private javax.swing.JToggleButton Combination5;
    private javax.swing.JToggleButton Combination6;
    private javax.swing.JToggleButton Combination7;
    private javax.swing.JToggleButton Combination8;
    private javax.swing.JToggleButton Combination9;
    private javax.swing.JButton OkButton;
    private javax.swing.JButton UndoButton;
    // End of variables declaration//GEN-END:variables
}
