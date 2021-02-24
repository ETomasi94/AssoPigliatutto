/*
ASSO PIGLIATUTTO
TESI DI LAUREA A.A 2020 - 2021

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

import java.util.Set;

/*
@CLASS Mossa
@OVERVIEW Classe che descrive una mossa di gioco come una sequenza stato-azione-reward
*/
public class Mossa 
{
    public int OldState;//Codice dello stato corrente
    public int NewState;//Codice dello stato risultante
    
    public int Action;//Azione effettuata durante la mossa
    public double Reward;//Reward ottenuto dopo l'esecuzione della mossa

    public Set<Integer> PossibleActions;//Azioni possibili nello stato corrente
    
/*----METODI COSTRUTTORI----*/
    /**
        @METHOD Mossa
        @OVERVIEW Metodo costruttore delle istanze della classe Mossa
    
        @param O : Vecchio stato
        @param A : Azione intrapresa
        @param N : Nuovo stato a cui si arriva
        @param R : Reward che si ottiene dal transire dallo stato O allo stato N
        * 
    */
    public Mossa(int O,int A,int N,double R)
    {
        OldState = O;
        NewState = N;
        Action = A;
        Reward = R;
    }
    
    /**
     *   @METHOD Mossa
     *   @OVERVIEW Metodo costruttore delle istanze della classe Mossa
     *
     *   @param O : Vecchio stato
     *   @param A : Azione intrapresa
     *   @param N : Nuovo stato a cui si arriva
     *   @param R : Reward che si ottiene dal transire dallo stato O allo stato N
     *   @param ActionSet Set di azioni possibili durante la mossa istanziata
     */
    public Mossa(int O,int A,int N,double R,Set<Integer> ActionSet)
    {
        OldState = O;
        NewState = N;
        Action = A;
        Reward = R;
        PossibleActions = ActionSet;
    }
 /*----FINE METODI COSTRUTTORI----*/
    
 /*----METODI GETTERS E SETTERS----*/
    /**
     * @METHOD GetOldState
     * 
     * @OVERVIEW Metodo che restituisce in output lo stato corrente della mossa
     * 
     * @return OldState stato corrente della mossa 
     */
    public int GetOldState()
    {
        return OldState;
    }
    
    /**
     * @METHOD GetAction
     * 
     * @OVERVIEW Metodo che restituisce in output l'azione associata alla mossa
     * 
     * @return Acted stringa che codifica l'azione associata alla mossa.
     */
    public String GetAction()
    {
        String Acted = String.valueOf(Action);
        return Acted;
    }
 /*----FINE METODI GETTERS E SETTERS----*/   
    
 /*----METODI DI DEBUG----*/
 
 /*----FINE METODI DI DEBUG----*/ 
    
}
