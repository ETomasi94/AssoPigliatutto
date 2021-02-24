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

import java.io.Serializable;

/**
 * @author Enrico Tomasi
 * @Class Action
 * 
 * @OVERVIEW Classe che implementa un'azione effettuabile dal giocatore o dal
 *           learner di rinforzo durante una partita, contraddistinta univocamente
 *           da un codice ed un reward associato.
 */
public class Action implements Serializable
{
    public int Code;//Codice dell'azione
    public double Gain;//Reward associato

/*----METODI COSTRUTTORE----*/    
    /**
     * @METHOD Action
     * 
     * @OVERVIEW Metodo costruttore di un'istanza della classe Action
     * 
     * @param C Codice univoco dell'azione
     * @param G Reward associato all'azione
     */
    public Action(int C,double G)
    {
        Code = C;
        Gain = G;
    }
    
    /**
     * @METHOD Action
     * 
     * @OVERVIEW Metodo costruttore di un'istanza della classe Action
     * 
     * @param Card Intero rappresentante l'indice della carta da giocare 
     *             all'interno della mano del giocatore
     * @param Comb Intero rappresentante l'indice della combinazione scelta
     * @param G Numero in virgola mobile associato alla presa contrassegnata
     *          dalla carta scelta indicizzata dall'intero Card
     *          e dalla combinazione di carte indicizzata dall'intero Comb
     */
    public Action(int Card,int Comb,double G)
    {
        Code = ((Card) * 100) + (Comb);
        Gain = G;
    }
/*----FINE METODI COSTRUTTORE----*/     
    
    /**
     * @METHOD ResetNewAction
     * 
     * @OVERVIEW Metodo che reimposta i valori principali di un'istanza della
     *           classe Action
     * 
     * @param C Intero rappresentante il codice da reimpostare
     * @param G Numero in virgola mobile rappresentante il reward 
     *          associato da reimpostare
     */
    public void ResetNewAction(int C,double G)
    {
        Code = C;
        Gain = G;
    }
    
    /**
     * @METHOD UnsetThisAction
     * 
     * @OVERVIEW Metodo che imposta i valori dell'istanza corrente della classe
     *           in modo che rappresenti un'azione nulla, contraddistinta da 
     *           codice uguale a -1 e reward uguale a 0
     */
    public void UnsetThisAction()
    {
        Code = -1;
        Gain = 0.0;
    }
    
    /**
     * @METHOD GetCode
     * 
     * @OVERVIEW Metodo getter che restituisce in output il codice dell'azione
     * 
     * @return Code Intero rappresentante il codice dell'azione 
     */
    public int GetCode()
    {
        return Code;
    }
    
    /**
     * @METHOD GetGain
     * 
     * @OVERVIEW Metodo getter che restituisce in output il reward associato
     *           all'azione
     * 
     * @return Code Numero in virgola mobile rappresentante il reward
     *         associato all'azione
     */
    public double GetGain()
    {
        return Gain;
    }
    
    /**
     * @METHOD SetCode
     * 
     * @OVERVIEW Metodo setter che imposta il codice dell'azione secondo
     *           un valore intero in input
     * 
     * @param C Intero rappresentante il nuovo codice da assegnare all'azione
     */
    public void SetCode(int C)
    {
        Code = C;
    }
    
    /**
     * @METHOD SetGain
     * 
     * @OVERVIEW Metodo setter che imposta il reward associato all'azione secondo
     *           un valore in virgola mobile in input
     *  
     * @param G Numero in virgola mobile rappresentante il reward da associare
     *          all'azione
     */
    public void SetGain(Double G)
    {
        Gain = G;
    }
       
    /**
     * @METHOD IsAVoidAction
     * 
     * @OVERVIEW Metodo che determina se l'azione corrente è un'azione nulla 
     *           o meno
     * @return NoAction Valore booleano che indica se l'azione corrente è un'azione
     *         nulla o meno
     */
    public boolean IsAVoidAction()
    {
        return NoAction();
    }
    
    /**
     * @METHOD NoAction
     * 
     * @OVERVIEW Metodo che determina se l'azione corrente è un'azione nulla 
     *           o meno verificando esplicitamente che il suo codice sia uguale a -1
     * 
     * @return B Valore booleano che indica se l'azione corrente è un'azione
     *         nulla o meno
     */
    public boolean NoAction()
    {
        return (Code == 0);
    }
    
    /**
     * @METHOD NoReward
     * 
     * @OVERVIEW Metodo che determina se l'azione corrente ha reward associato
     *           nullo o meno
     * 
     * @return B Valore booleano che indica se l'azione ha reward uguale a -1
     */
    public boolean NoReward()
    {
        return (Gain == 0.0);
    }
    
    /**
     * @METHOD IncreaseGain
     * 
     * @OVERVIEW Metodo di debug che incrementa di 1.0 il reward associato all'azione
    */
    public void IncreaseGain()
    {
        Gain++;
    }
    
    /**
     * @METHOD DecreaseGain
     * 
     * @OVERVIEW Metodo di debug che decrementa di 1.0 il reward associato all'azione
    */
    public void DecreaseGain()
    {
        Gain--;
    }
    /**
     * @METHOD GetCard
     * 
     * @OVERVIEW Metodo che scompone il codice di un'azione per restituire in 
     *           output la carta da esso indicizzata.
     * 
     * @param Value Intero rappresentante il codice di un'azione dato in input.
     * 
     * @return Carta Indice della carta denotato dal codice dell'azione. 
     */
     public int GetCard()
    {
        String Scomposizione = String.valueOf(Code);
        String Sc2 = Scomposizione.substring(0,1);
        int Card = Integer.valueOf(Sc2)-1;
        
        return Card;
    }
    
      /**
     * @METHOD GetCombination
     * 
     * @OVERVIEW Metodo che scompone il codice di un'azione per restituire in 
     *           output la combinazione da esso indicizzata.
     * 
     * @param Value Intero rappresentante il codice di un'azione dato in input.
     * 
     * @return Combination Indice della combinazione denotato dal codice dell'azione. 
     */
    public int GetCombination()
    {
        int Combination = 0;
        
        if(Code >= 100)
        {
        String Scomposizione = String.valueOf(Code);
        String Sc2 = Scomposizione.substring(1);
        
        Combination = Integer.valueOf(Sc2);
        }
        else
        {
          Combination = 0;
        }
        
        return Combination;
    }
    
    public void PrintAction()
    {
        if(!IsAVoidAction())
        {
            System.out.println("CODICE AZIONE:"+Code+"REWARD:"+Gain);
            System.out.println("CARTA SUGGERITA:"+GetCard()+"COMBINAZIONE SUGGERITA:"+GetCombination());
        }
    }
 
}
