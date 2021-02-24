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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/*
    @CLASS StateDatabase
    @OVERVIEW Classe che implementa un database di stati da cui il learner
              prende la conoscenza pregressa necessaria a capire le mosse da
              effettuare e che esso stesso provvede automaticamente
              ad aggiornare
*/
public class StateDatabase implements Serializable
{
  protected Map<Integer,Action> MapToIDs;//Tabella delle associazioni Stato->Azione

 /*----METODO COSTRUTTORE----*/
    
  /**
   * @METHOD StateDatabase
   * 
   * @OVERVIEW Metodo costruttore di un'istanza della classe StateDatabase
   * 
   * @param ProfileName Stringa rappresentante il nome del profilo da associare
   *                    al database da istanziare.
   */
    public StateDatabase(String ProfileName)
    {
        LoadDatabase(ProfileName);
    }
    
 /*----FINE METODO COSTRUTTORE----*/   
 
 /*----METODI DI AGGIORNAMENTO----*/
    
    /**
     * @METHOD AddState
     * 
     * @OVERVIEW Metodo che aggiunge un nuovo stato in input al database 
     *           e vi associa una data azione in input.
     * 
     * @param S Stato da aggiungere al database.
     * @param A Azione in input da associare allo stato S.
     */
    public void AddState(Stato S,Action A)
    {
        int Serial = S.SerialNumber();
        
        if(!(HasID(Serial)))
        {
            MapToIDs.put(Serial,A);
        }
    }
    
    /**
     * @METHOD AddSerial
     * 
     * @OVERVIEW Metodo che aggiunge un nuovo stato in input codificato dal suo
     *           codice seriale al database e vi associa una data azione in input.
     * 
     * @param Sr Codice dello stato da aggiungere al database.
     * @param A Azione in input da associare allo stato codificato da Sr.
     */
    public void AddSerial(int Sr,Action A)
    {
        if(!(HasID(Sr)))
        {
            MapToIDs.put(Sr, A);
            System.out.println("Inserita azione:"+A.Code+"di reward:"+A.Gain+"in stato:"+Sr);
        }
    }
    
    /**
     * @METHOD UpdateDatabase
     * 
     * @OVERVIEW Metodo che, dati in input uno stato, un'azione ed una stringa
     *           rappresentante il nome associato al database, associa allo stato
     *           in input l'azione in input, la aggiunge al database corrente
     *           e lo salva in memoria.
     * 
     * @param S Stato da aggiungere al database.
     * @param A Azione da associare allo stato S.
     * @param nome Nome del giocatore associato al database.
     */
    public void UpdateDatabase(Stato S,Action A,String nome)
    {
       int Serial = S.SerialNumber();
        
        if(!HasID(Serial))
        {
            AddState(S,A);

            SaveDatabase(nome);
        }
        else
        {
            double StoredGain = GetCorrespondingAction(Serial).GetGain();
            double CurrentGain = A.GetGain();

            if(CurrentGain > StoredGain)
            {
                MapToIDs.replace(Serial, A);
            }
        }
    }
    
    public void SerialUpdate(int S,int A,double Reward,String nome)
    {      
        Action Actn = new Action(A,Reward);
        
        if(!HasID(S))
        {
            AddSerial(S,Actn);
        }
        else
        {
            double StoredGain = GetCorrespondingAction(S).GetGain();
            double CurrentGain = Reward;

            if(CurrentGain > StoredGain)
            {
                MapToIDs.replace(S, Actn);
            }
        }
        
        SaveDatabase(nome);
    }
    
    /**
     * @METHOD ChangeSerial
     * 
     * @OVERVIEW Metodo che sostiuisce al seriale in input rappresentante uno
     *           stato presente nel database l'azione associata assegnandogli
     *           una nuova azione in input.
     * 
     * @param Serial Intero grande (classe BigInteger) rappresentante il seriale
     *               dello stato cercato.
     * @param A Azione da associare allo stato contrassegnato da Serial.
     */
    public void ChangeSerial(int Serial,Action A)
    {
        MapToIDs.replace(Serial, A);
    } 
        
    /**
     * @METHOD ClearDatabase
     * 
     * @OVERVIEW Metodo che resetta il database cancellando tutte le associazioni
     *           presenti nella sua tabella;
     */
    public void ClearDatabase()
    {
        MapToIDs.clear();
    }
    
    /**
     * @METHOD CleanUP
     * 
     * @OVERVIEW Metodo che elimina dalla tabella delle associazioni tutti gli
     *           stati associati ad azioni con reward nullo.
     */
    public synchronized void CleanUp()
    {
        Set<Entry<Integer,Action>> View = GetDBView();
        System.out.println("DIMENSIONE DEL DATABASE PRIMA DELLA ROUTINE DI CLEANUP: "+SizeOfDatabase());
        
        for(Entry<Integer,Action> E : View)
        {
            Action ACTN = E.getValue();
            
            if(ACTN.Gain == 0.0)
            {
                int Key = E.getKey();
                
                MapToIDs.remove(Key, ACTN);
            }
        }
        
        System.out.println("DIMENSIONE DEL DATABASE DOPO ROUTINE DI CLEANUP: "+SizeOfDatabase());
    }
    
/*----FINE METODI DI AGGIORNAMENTO----*/
    
/*----METODI DI RICERCA E CONTROLLO----*/
     
    /**
     * @METHOD CheckDatabase
     * 
     * @OVERVIEW Metodo che verifica la presenza di uno stato S all'interno del
     *           database corrente e restituendone, se questo è presente, l'azione
     *           associate,oppure l'azione nulla in caso contrario.
     * 
     * @param S Stato da ricercare all'interno del database corrente.
     * @return Action Azione associata allo stato S, se questi è presente, oppure
     *                un'azione nulla (contrassegnata da codice -1 e reward nullo).
     */
    public Action CheckDatabase(Stato S)
    {
        int Serial = S.SerialNumber();

        if(HasID(Serial))
        {
            return GetCorrespondingAction(Serial);
        }
        else
        {
            return new Action(0,0);
        }
    }
 
    /**
     * @METHOD GetCorrespondingAction
     * 
     * @OVERVIEW Metodo che, dato un numero seriale che contrassegna uno specifico
     *           stato, restituisce, se questo è presente all'interno del database
     *           corrente, l'azione associata.
     * 
     * @param Serial Intero grande (classe BigInteger) rappresentante il codice
     *               seriale dello stato da ricercare all'interno del database
     *               corrente in modo da recuperarne l'azione associata.
     * @return Result Azione associata allo stato codificato da Serial
     *                se quest'ultimo è presente nel database, azione nulla
     *                altrimenti.
     */
    public Action GetCorrespondingAction(int Serial)
    {
        if(MapToIDs.containsKey(Serial))
        {
            Action Result = MapToIDs.get(Serial);
            return Result;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * @METHOD HasId
     * 
     * @OVERVIEW Metodo che, dato un numero seriale che contrassegna uno specifico
     *           stato di gioco, controlla se esso è presente o meno nel database.
     * 
     * @param Serial Intero grande (classe BigInteger) rappresentante lo stato
     *                di cui verificare la presenza all'interno del database corrente.
     * 
     * @return B Valore booleano che indica se lo stato contrassegnato da Serial 
     *           è presente o meno all'interno del database.
     */
    public boolean HasID(int Serial)
    {
        return MapToIDs.containsKey(Serial);
    }
    
/*----FINE METODI DI RICERCA E CONTROLLO----*/       
    
/*----METODI GETTERS----*/
    
    /**
     * @METHOD SizeOfDatabase
     * 
     * @OVERVIEW Metodo che restituisce il numero di associazioni Stato->Azione
     *           presenti all'interno del database corrente.
     * 
     * @return Size Intero rappresentante il numero di associazioni
     *         Stato->Azione presenti all'interno del database corrente.
     */
    public int SizeOfDatabase()
    {
        int Size = MapToIDs.size();

        return Size;
    }
    
    /**
     * @METHOD GetDBView
     * 
     * @OVERVIEW Metodo ausiliario che consente di avere una struttura dati
     *           facile da scansionare in modo da poter enumerare tutte le
     *           associazioni presenti all'interno del database corrente.
     * 
     * @return Result Set di coppie [Seriale,Azione] che contiene tutte le 
     *                associazioni presenti nel database corrente.
     */
    public Set<Entry<Integer,Action>> GetDBView()
    {
        Set<Entry<Integer,Action>> Result = MapToIDs.entrySet();
        
        return Result;
    }
    
/*----FINE METODI GETTERS----*/
    
/*----METODI DI SERIALIZZAZIONE----*/
    /**
     * @METHOD SaveDatabase
     * 
     * @OVERVIEW Metodo che serializza la tabella delle associazioni del database
     *           corrente e la memorizza nella cartella "src/main/profiles" nominandola
     *           a seconda di una stringa in input e associandole il formato ".apdb"
     * 
     * @param name Stringa rappresentante il nome da dare alla tabella delle associazioni
     *             serializzata e memorizzata.
     */
    public void SaveDatabase(String name)
    {              
        try 
        {
            try (FileOutputStream FileOut = new FileOutputStream("src/main/profiles/"+name+".apdb")) {
                ObjectOutputStream Out = new ObjectOutputStream(FileOut);
                
                Out.writeObject(MapToIDs);
                
                Out.close();
            }
      } 
      catch (IOException i) 
      {
         i.printStackTrace();
      }
    }
    
    /**
     * @METHOD LoadDatabase
     * 
     * @OVERVIEW Metodo che, data una stringa in input, carica la tabella delle
     *           associazioni serializzata in memoria e denominata secondo la stringa
     *           e la imposta come tabella delle associazioni corrente.
     *           Nel caso non esista alcuna tabella serializzata in tal modo, ne
     *           crea una nuova nominata secondo la stringa in input.
     * 
     * @param n Stringa che indica il nome della tabella serializzata da cercare.
     */
     public void LoadDatabase(String n)
    {
        String DBPath = "src/main/profiles/"+n+".apdb";
        
        try 
        {
         FileInputStream FileIn = new FileInputStream(DBPath);
         ObjectInputStream In = new ObjectInputStream(FileIn);
         
         MapToIDs = (HashMap) In.readObject();
         
         System.out.println("Database caricato: "+DBPath);

         In.close();
         FileIn.close();
      } 
      catch (ClassNotFoundException ex) 
      { 
          MapToIDs = new HashMap();
      } 
      catch (IOException e)
      {
          MapToIDs = new HashMap();
          SaveDatabase(n);
      }
    }
/*----FINE METODI DI SERIALIZZAZIONE----*/    
     
/*----METODI DI STAMPA----*/
     /**
      * @METHOD PrintDatabase
      * 
      * @OVERVIEW Metodo che stampa in output tutte le associazioni
      *           Stato->Azione, elencando per queste ultime il codice
      *           ed il reward associato.
      */
    public void PrintDatabase()
    {
        SizeOfDatabase();
        System.out.println("STAMPA DEGLI STATI DEL DATABASE: \n");
        
        Set<Entry<Integer,Action>> ES = MapToIDs.entrySet();
        
        for(Entry<Integer,Action> E : ES)
        {
            System.out.println("----");
            System.out.println("CODICE DELLO STATO: "+E.getKey());
            System.out.println(" AZIONE PREDEFINITA: "+E.getValue().GetCode());
            System.out.println("REWARD ASSOCIATO: "+E.getValue().GetGain());
            System.out.println("\n");
        }
    }
    
    /**
     * @METHOD DataBaseDump
     * 
     * @OVERVIEW Metodo di debug che, data una stringa in input, salva, quado
     *           all'interno del tavolo da gioco si clicca sulla scritta relativa
     *           al nome della CPU, tutte le informazioni relative al database degli
     *           stati correnti in un file di testo denominato a seconda dell'ora
     *           corrente (minuti compresi) all'interno della cartella "/src/main".
     * 
     * @param name Stringa rappresentante il nome da associare al file di testo 
     *             risultante dall'esecuzione del metodo corrente.
     */
    public synchronized void DataBaseDump(String name)
    {
        String TimeStamp = new SimpleDateFormat("HHmm").format(new Date());
        
        String path = "src/main/DataBaseDump-"+TimeStamp+".txt";
        
           try (FileWriter FileOut = new FileWriter(path)) 
            {
                FileOut.write("---DATABASE OF "+name+"----\n");
                FileOut.write("\n");
                FileOut.write("SIZE OF DATABASE: "+SizeOfDatabase()+"\n");
                FileOut.write("LISTING ALL STATES IN DETAIL: \n");
                
                Set<Entry<Integer,Action>> ViewOfDatabase = GetDBView();
                
                for(Entry<Integer,Action> E : ViewOfDatabase)
                {
                    FileOut.write("STATE: "+E.getKey()+"\n");
                    FileOut.write("--ACTION: "+E.getValue().GetCode()+"| REWARD: "+E.getValue().GetGain()+"\n");
                    FileOut.write("\n");
                }
                
                FileOut.close();
                
                System.out.println("DATABASE DUMPED");
            } 
           catch (IOException ex) 
           {
               ex.printStackTrace();
           }
    }
    
/*----FINE METODI DI STAMPA----*/    
}
