/*
ASSO PIGLIATUTTO
PROGETTO DI ESPERIENZE DI PROGRAMMAZIONE A.A 2019-2020

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

import java.util.ArrayList;

/*
    @CLASS Punteggio

    @OVERVIEW Classe che implementa la memorizzazione ed il calcolo del punteggio
              di uno degli avversari
*/
public class Punteggio 
{
    /*----VARIABILI D'ISTANZA----*/
    
    int Total;
    int Denari;
    int Scope;
    boolean Settebello;
    int Primiera;
    
    ArrayList<Carta> CarteOttenute;
    
    /*----FINE VARIABILI D'ISTANZA----*/
    
    /*----METODO COSTRUTTORE----*/
    
    /*
        @METHOD Punteggio
    
        @OVERVIEW Metodo costruttore della classe che inizializza una nuova istanza
                  relativa al punteggio di uno dei due avversari inizializzando le sue
                  variabili a dei valori predefiniti
    
       @RETURNS Score : Nuovo punteggio relativo al giocatore 
    */
    public Punteggio()
    {
        Total = 0;
        Denari = 0;
        Scope = 0;
        Settebello = false;
        Primiera = 0;
        
        CarteOttenute = new ArrayList();
    }
    
     /*----FINE METODO COSTRUTTORE----*/
    
    /*----METODI DI MODIFICA DEL PUNTEGGIO----*/
    
    /*
        @METHOD AddCard
    
        @OVERVIEW Metodo che aggiunge una carta a quelle ottenute dal giocatore
                  aggiornando le variabili d'istanza a seconda di alcune sue 
                  caratteristiche
    
        @PAR C : Carta da aggiungere alla lista di quelle già ottenute
    */
    public void AddCard(Carta C)
    {
        CarteOttenute.add(C);

        if(C.GetSeme().equals("denari"))
        {
            Denari++;
            
            if(C.GetValue() == 7)
            {
                Settebello = true;
            }          
        }
        
                
        Total = CarteOttenute.size();
        
    }
    
    /*
        @METHOD CallScopa
    
        @OVERVIEW Metodo che, quando il giocatore a cui è associato il punteggio corrente
                  effettua una scopa, incrementa l'apposito
    */
    public void CallScopa()
    {
        System.out.println("SCOPA!");
        Scope++;
    }
    
    /*
        @METHOD MaxPrimiera
    
        @OVERVIEW Metodo che verifica se una determinata carta data in input
                  assume il valore di primiera massimo ottenuto finora
    
        @PAR Max : Carta da esaminare
    
        @RETURNS MaxP : Valore booleano che indica se la carta in esame ha
                        il massimo valore di primiera
    */
    public boolean MaxPrimiera(Carta Max)
    {
        boolean result = false;
        
        for(Carta C : CarteOttenute)
        {
            if(C.seme.equals(Max.seme))
            {
                int v1 = Max.GetPrimieraValue();
                int v2 = C.GetPrimieraValue();

                   if(v2 > v1)
                   {
                       result = true;
                   }
            }
        }
        
        return result;
    }
    
    /*
        @METHOD PunteggioDiPrimiera
    
        @OVERVIEW Metodo che calcola la somma delle carte di valore massimo in primiera
                  in modo da decretare il punteggio di primiera del giocatore associato
                  al punteggio 
    
        @RETURNS TotalPoints : Intero rappresentante il punteggio in primiera ricavato 
                 dalla somma della carte con valore di primiera massimo
    */
    public int PunteggioDiPrimiera()
    {
        int TotalPoints = 0;

        Carta MaxSpade = null;
        Carta MaxBastoni = null;
        Carta MaxDenari = null;
        Carta MaxCoppe = null;
        
        for(Carta C : CarteOttenute)
        {
          switch(C.seme)
          {
              case "spade":
                  if(MaxSpade == null)
                  {
                      MaxSpade = C;
                  }
                  else
                  {
                      int v1 = MaxSpade.GetPrimieraValue();
                      int v2 = C.GetPrimieraValue();
                      
                      if(v2 > v1)
                      {
                          MaxSpade = C;
                      }
                  }
                  break;
              case "bastoni":
                  if(MaxBastoni == null)
                  {
                      MaxBastoni = C;
                  }
                  else
                  {
                      int v1 = MaxBastoni.GetPrimieraValue();
                      int v2 = C.GetPrimieraValue();
                      
                      if(v2 > v1)
                      {
                          MaxBastoni = C;
                      }
                  }
                  break;
              case "denari":
                  if(MaxDenari == null)
                  {
                      MaxDenari = C;
                  }
                  else
                  {
                      int v1 = MaxDenari.GetPrimieraValue();
                      int v2 = C.GetPrimieraValue();
                      
                      if(v2 > v1)
                      {
                          MaxDenari = C;
                      }
                  }
                  break;
              case "coppe":
                  if(MaxCoppe == null)
                  {
                      MaxCoppe = C;
                  }
                  else
                  {
                      int v1 = MaxCoppe.GetPrimieraValue();
                      int v2 = C.GetPrimieraValue();
                      
                      if(v2 > v1)
                      {
                          MaxCoppe = C;
                      }
                  }
                  break;
              default:
                  throw new IllegalArgumentException();
          }
        }
        
        System.out.println("CARTE CANDIDATE PER LA PRIMIERA:");
        System.out.println(MaxSpade.GetName());
        System.out.println(MaxBastoni.GetName());
        System.out.println(MaxDenari.GetName());
        System.out.println(MaxCoppe.GetName());
        
        TotalPoints = MaxSpade.GetPrimieraValue() + MaxBastoni.GetPrimieraValue() + MaxDenari.GetPrimieraValue() + MaxCoppe.GetPrimieraValue();
        
        return TotalPoints;
    }
    
    /*----FINE METODI DI MODIFICA DEL PUNTEGGIO----*/
    
    /*----METODI EXTRA----*/
    
     /*
        @METHOD CopyScore
    
        @OVERVIEW Metodo che, preso un Punteggio in input, copia tutti i valori
                  del Punteggio corrente in quest'ultimo.
                  Utilizzata originariamente per la generazione degli stati dell'albero
                  di gioco
    
       @PAR Dest : Punteggio in cui copiare i valori del Punteggio corrente
    */
    public void CopyScore(Punteggio Dest)
    {
        if(Dest == null)
        {
            System.out.println("CREANDO UN NUOVO PUNTEGGIO: ");
            Dest = new Punteggio();
        }
        
        Dest.Total = Total;
        Dest.CarteOttenute = CarteOttenute;
        Dest.Denari = Denari;
        Dest.Primiera = Primiera;
        Dest.Scope = Scope;
        Dest.Settebello = Settebello;
    }
    
     /*----FINE METODI EXTRA----*/
    
    /*----METODI GETTERS E SETTERS----*/
    
    /*
        @METHOD ResetScore
    
        @OVERVIEW Metodo che imposta i valori delle variabili d'istanza del punteggio al valore
                  assunto al momento della creazione, resettando di fatto il punteggio
    */
    public void ResetScore()
    {
        CarteOttenute.clear();
        
        Total = 0;
        Denari = 0;
        Scope = 0;
        Settebello = false;
        Primiera = 0;
    }
    
    /*
        @METHOD GetTotal
    
        @OVERVIEW Metodo che restituisce il numero totale di carte ottenute
    
        @RETURNS Total : Intero rappresentante il numero totale di carte ottenute
    */
    public int GetTotal()
    {
        return Total;
    }
      
    /*
        @METHOD GetDenari
    
        @OVERVIEW Metodo che restituisce il numero totale di carte di denari ottenute
    
        @RETURNS Denari : Intero rappresentante il numero totale di carte di denari ottenute
    */
    public int GetDenari()
    {
        return Denari;
    }
    
    /*
        @METHOD GetScope
    
        @OVERVIEW Metodo che restituisce il numero totale di scope effettuate
    
        @RETURNS Denari : Intero rappresentante il numero totale di scope effettuate
    */
    public int GetScope()
    {
        return Scope;
    }
    
    /*
        @METHOD GetSettebello
    
        @OVERVIEW Metodo che verifica se il giocatore a cui è associato il punteggio ha già
                  ottenuto il Settebello (ovvero il sette di denari) o meno
    
        @RETURNS Result : Risultato della verifica
    */
    public boolean GetSettebello()
    {
        return Settebello;
    }
    
    /*
        @METHOD GetPrimiera
    
        @OVERVIEW Metodo che restituisce il punteggio di primiera del giocatore
                  a cui è associato il punteggio corrente
        
        @RETURNS ScorePrimiera : Intero rappresentante il punteggio di primiera del giocatore
    */
    public int GetPrimiera()
    {
        Primiera = PunteggioDiPrimiera();
        return Primiera;
    }
    
    /*
        @METHOD SetCount 
    
        @OVERVIEW Metodo che incrementa  la variabile d'istanza selezionata ad un valore
                  determinato da un numero intero dato in output secondo il seguente
                  schema:
    
                  |Index|    |Cosa modifica|
                     0    -> Numero di carte totali ottenute
                     1    -> Numero di denari ottenuti
                     3    -> Numero di scope effettuate
                     4    -> Settebello preso (value > 0) o meno (value <= 0)
                     5    -> Punti di primiera
    
        @PAR index : Indice della variabile da modificare
        @PAR value : Valore di incremento della variabile scelta
    */
    public void SetCount(int index,int value)
    {
        switch(index)
        {
            case 0:
                Total += value;
                break;
            case 1:
                if(value <= 10)
                {
                    Denari += value;
                }
                else
                {
                    throw new IllegalArgumentException();
                }
                break;
            case 3:
                Scope+=value;
                break;
            case 4:
                if(value>0)
                {
                    Settebello = true;
                }
                else
                {
                    Settebello = false;
                }
                break;
            case 5:
                Primiera +=value;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
    /*----FINE METODI GETTERS E SETTERS----*/
    
    /*----METODI DI STAMPA E DEBUG----*/
    
    /*
        @METHOD PrintCards
    
        @OVERVIEW Metodo che stampa in output tutte le carte ottenute dal giocatore
                  associato al punteggio corrente
    */
    public void PrintCards()
    {
        CarteOttenute.forEach((c) -> 
        {
            System.out.println("CARTA OTTENUTA: "+c.GetName());
        });
    }
    
    /*
        @METHOD ScorePrint
    
        @OVERVIEW Metodo che stampa in output le principali variabili d'istanza
                  del punteggio corrente con il loro valore attuale
    */
    public void ScorePrint()
    {
        System.out.println("NUMERO TOTALE DI CARTE: "+Total+"\n");
        System.out.println("NUMERO DI DENARI: "+Denari+"\n");
        System.out.println("NUMERO DI SCOPE VINTE: "+Scope+"\n");
        System.out.println("SETTEBELLO : "+Settebello+"\n");
        System.out.println("PRIMIERA: "+Primiera+"\n");
        System.out.println("CARTE OTTENUTE:");
    }
    
    /*----FINE METODI DI STAMPA E DEBUG----*/
}
