/*
ASSO PIGLIATUTTO
TESI DI LAUREA A.A 2020 - 2021

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

import java.util.Comparator;
/**
 * @CLASS WeightComparator
 * 
 * @OVERVIEW Classe che implementa un confrontatore di carte che stabilisce l'ordine
 *           di queste ultime in base al peso in termini di guadagno
 * 
 * @author Enrico Tomasi
 */
public class WeightComparator implements Comparator<Carta>
{

    /**
     * @METHOD Compare
     * 
     * @OVERVIEW Metodo che, date in input due carte C1 e C2, le confronta e restituisce
     *              1 se il peso in termini di guadagno di C1 è minore del peso in termini di guadagno di C2
     *              0 se il peso in termini di guadagno di C1 è uguale al peso in termini di guadagno di C2
     *              -1 altrimenti
     * 
     * @param C1 : Prima carta da confrontare
     * @param C2 : Seconda carta da confrontare
     * @return  Number : Risultato del confronto che determina l'ordinamento delle due carte
     */
    @Override
    public int compare(Carta C1, Carta C2) 
    {
       if(C1.Weight > C2.Weight)
       {
           return -1;
       }
       else if(C1.Weight < C2.Weight)
       {
           return 1;
       }
       else
       {
           return 0;
       }
    }
    
}
