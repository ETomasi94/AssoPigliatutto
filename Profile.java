/*
ASSO PIGLIATUTTO
TESI DI LAUREA A.A 2020 - 2021

AUTORE : ENRICO TOMASI
NUMERO DI MATRICOLA: 503527

OVERVIEW: Implementazione di un tipico gioco di carte italiano in cui il computer
pianifica le mosse ed agisce valutando mediante ricerca in uno spazio di stati
*/
package assopigliatutto;

import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.github.chen0040.rl.utils.Matrix;
import com.github.chen0040.rl.models.QModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Enrico Tomasi
 * 
 * @CLASS Profile
 * 
 * @OVERVIEW Classe che implementa il profilo di un determinato giocatore.
 */
public class Profile 
{
    String name;//Nome del giocatore
    
    public QLearner Learner;//Learner associato
    
    Giocatore Pl;//Istanza della classe Giocatore da utilizzare durante le partite.
            
    StateDatabase DB;//Database degli stati assegnato al giocatore
    
    FileWriter File;//FileWriter per scrivere in output le statistiche del giocatore
    
    Stats Statistics;//Statistiche relative al giocatore memorizzate nel corso delle partite giocate

/*----METODO COSTRUTTORE----*/    
    /**
     * @METHOD Profile
     * 
     * @OVERVIEW Metodo costruttore di un'istanza della classe Profilo
     * 
     * @param n Stringa rappresentante il nome del giocatore a cui va associato il
     *          profilo da istanziare.
     */
    public Profile(String n)
    {
        name = n;
        
        LoadStats(n);
        
        DB = new StateDatabase(n);
    }
    
/*----FINE METODO COSTRUTTORE----*/    

/*----METODI GETTERS E SETTERS----*/
    /**
     * @METHOD AssignPlayer
     * 
     * @OVERVIEW Metodo che assegna un giocatore dato in input al profilo corrente.
     * 
     * @param Player Giocatore da assegnare al profilo.
     */
    public void AssignPlayer(Giocatore Player)
    {
        Pl = Player;
    }
    
    /**
     * @METHOD SetName
     * 
     * @OVERVIEW Metodo che imposta il nome del profilo in base ad una stringa
     *           data in input.
     * 
     * @param n  Stringa rappresentante il nome da assegnare al profilo.
     */
    public void SetName(String n)
    {
        name = n;
    }
    
    /**
     * @METHOD Reset
     * 
     * @OVERVIEW Metodo che reimposta un profilo rendendolo vuoto.
     */
    public void Reset()
    {
        Pl = null;
        Learner = null;
    }
/*----FINE METODI GETTERS E SETTERS----*/  
    
/*----METODI DI AGGIORNAMENTO----*/    
    
    /**
     * @METHOD UpdateStatsManual
     * 
     * @OVERVIEW Metodo che aggiorna le statistiche associate al profilo dopo
     *           una partita giocata da un giocatore umano
     * 
     * @param WhoWins Intero rappresentante il vincitore della partita come segue:
     *                      - WhoWins = 1 se ha vinto il giocatore
     *                      - WhoWins= 0 se la partita è finita in un pareggio
     *                      - WhoWins = -1 se ha vinto la CPU
     * @param CPUThought Intero rappresentante il numero di carte giocate dalla CPU
     * @param GPUGreedy Intero rappresentante il numero di carte giocate dalla CPU
     *                  in maniera greedy
     */
    public void UpdateStatsManual(int WhoWins,int CPUThought,int GPUGreedy)
    {
        Statistics.UpdateStatsManual(WhoWins,CPUThought, GPUGreedy);
        SaveStats(name);
    }
    
    /**
     * @METHOD UpdateStatsAutomatic
     * 
     * @OVERVIEW Metodo che aggiorna le statistiche associate al profilo dopo
     *           una partita giocata dall'apprendista per rinforzo.
     * 
     * @param Winning Intero rappresentante il vincitore della partita come segue:
     *                      - Winning = 1 se ha vinto il giocatore
     *                      - Winning = 0 se la partita è finita in un pareggio
     *                      - Winning = -1 se ha vinto la CPU
     * 
     * @param Cpl Intero rappresentante il numero di carte giocate dalla CPU.
     * @param Gcpl Intero rappresentante il numero di carte giocate dalla CPU
     *             in maniera greedy.
     * @param Lpl Intero rappresentante il numero di carte giocate dall'apprendista.
     * @param Lgpl Intero rappresentante il numero di carte giocate dall'apprendista
     *             in maniera greedy.
     * @param Expl Intero rappresentante il numero di nuove mosse esplorate.
     * @param Expt Intero rappresentante il numero di mosse memorizzate utilizzate.
     */
    public void UpdateStatsAutomatic(int Winning,int Cpl,int Gcpl,int Lpl,int Lgpl,int Expl,int Expt,double CPUTime,double LearnerTime)
    {
        Statistics.UpdateStatsAutomatic(Winning, Cpl, Gcpl, Lpl, Lgpl, Expl, Expt,CPUTime,LearnerTime);
        ExploredActions();
        SaveStats(name);
    }
    
/*----FINE METODI DI AGGIORNAMENTO----*/   
    
/*----METODI DI SERIALIZZAZIONE----*/    
    
    /**
     * @METHOD Save
     * 
     * @OVERVIEW Metodo che serializza il profilo corrente e lo salva nella
     *           cartella "src/main/profiles".
     */
    public void Save()
    {
        SaveLearner(Learner); 
        Load(name);
        Statistics.PlotGameDispersion();
        Statistics.PlotTrainingDispersion();
    }
    
    public void SaveLearner(QLearner Learner)
    {
        String L;
        this.Learner = Learner;
        
        if(Learner != null)
        {
            L = Learner.toJson();
        }
        else
        {
            Learner = new QLearner(111130,112);
            Learner.getModel().setAlpha(0.9);
            Learner.getModel().setGamma(0.4);

            System.out.println("NUOVO LEARNER CREATO PER IL GIOCAORE "+name);
            L = Learner.toJson();
        }
        
        try 
        {  
            File = new FileWriter("src/main/profiles/"+name+".txt");
            File.write(L);
            System.out.println("LEARNER SALVATO");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
 
        } 
        finally 
        {
 
            try 
            {
                File.flush();
                File.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        } 
    }

    public void SerializeMatrix()
    {
        String MatrixPath = "src/main/profiles/"+name+".mat";
        String AlphaMatrixPath = "src/main/profiles/"+name+".alpha";
         try
        {    
            //Saving of object in a file 
            FileOutputStream file = new FileOutputStream(MatrixPath); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
              
            Matrix object = Learner.getModel().getQ();
            // Method for serialization of object 
            out.writeObject(object); 
              
            out.close(); 
            file.close(); 
            
            FileOutputStream af = new FileOutputStream(AlphaMatrixPath);
            ObjectOutputStream aout = new ObjectOutputStream(af);
            
            Matrix alpha = Learner.getModel().getAlphaMatrix();
            
            aout.writeObject(alpha);
            
            aout.close();
            file.close();
              
            System.out.println("La matrice è stata serializzata"); 
        }    
        catch(IOException ex) 
        { 
            System.out.println("Errore nella serializzazione della matrice"); 
        } 
    }
    
    public void LoadMatrix()
    {
        String MatrixPath = "src/main/profiles/"+name+".mat";
        String AlphaMatrixPath = "src/main/profiles/"+name+".alpha";
        try
        {    
            // Reading the object from a file 
            FileInputStream file = new FileInputStream(MatrixPath); 
            ObjectInputStream in = new ObjectInputStream(file); 
              
            Matrix M = null;
            // Method for deserialization of object 
            M = (Matrix)in.readObject(); 
              
            in.close(); 
            file.close(); 
            
            FileInputStream af = new FileInputStream(AlphaMatrixPath);
            ObjectInputStream ain = new ObjectInputStream(af);
            
            Matrix alpha = Learner.getModel().getAlphaMatrix();
            
            Matrix Alp = null;
            
            Alp =(Matrix)ain.readObject();
            
            ain.close();
            af.close();
              
            System.out.println("La matrice è stata caricata");

            Learner.getModel().setQ(M);
            Learner.getModel().setAlphaMatrix(Alp);

        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught"); 
        } 
          
        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught"); 
        } 
    }
    
    /**
     * @METHOD Load
     * 
     * @OVERVIEW Metodo che, data una stringa in input, carica il profilo
     *           denominato a seconda della stringa, creandolo da zero
     *           se non lo trova.
     * 
     * @param n Stringa rappresentante il nome del profilo da caricare.
     */
    public void Load(String n)
    {
        String path = "src/main/profiles/"+n+".txt";
        FileInputStream Lrnsvd = null;
        Scanner JsonScanner = null;
        StringBuilder Bld = new StringBuilder();
        String json = "";
        
        try 
        {
            Lrnsvd = new FileInputStream(path);
            JsonScanner = new Scanner(Lrnsvd);
            
            while(JsonScanner.hasNextLine())
            {
                String S = JsonScanner.nextLine();
                Bld.append(S);
            }
            
            if(JsonScanner.ioException() != null)
            {
                throw JsonScanner.ioException();
            }
            
            json = Bld.toString();
            
            System.out.println("LEARNER CARICATO : "+path);

            Learner = QLearner.fromJson(json);
            
            ExploredActions();

            System.out.println("PROFILO DI "+n+" CARICATO");
            
            name = n;
            
            Lrnsvd.close();
            
            JsonScanner.close();
        } 
        catch (IOException ex) 
        {
            name = n;
            Save();
        } 
        finally
        {
            try
            {
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
        DB.LoadDatabase(n);

        name = n;
    }
    
    /**
     * @METHOD SaveStats
     * 
     * @OVERVIEW Metodo che serializza le statistiche associate al profilo a seconda
     *           di una data stringa in input e le memorizza nella cartella
     *           "/src/main/profiles"/.
     * 
     * @param Nome Stringa rappresentante il nome da dare alle statistiche da memorizzare.
     */
    public void SaveStats(String Nome)
    {
            try(FileOutputStream FileOut = new FileOutputStream("src/main/profiles/"+Nome+".stats")) 
            {
                ObjectOutputStream Out = new ObjectOutputStream(FileOut);

                Out.writeObject(Statistics);

                Statistics.DumpGameStats();
                Statistics.PlotGameStats();
                
                Out.close();
            } 
            catch (IOException i) 
            {
               i.printStackTrace();
            }
    }
    
    /**
     * @METHOD LoadStats
     * 
     * @OVERVIEW Metodo che, data una stringa in input, carica le statistiche
     *           denominate secondo quella stringa.
     * 
     * @param Nome Stringa rappresentante il nome delle statistiche da caricare.
     */
    public void LoadStats(String Nome)
    {
        String Path = "src/main/profiles/"+Nome+".stats";
        
        try 
        {
         FileInputStream FileIn = new FileInputStream(Path);
         ObjectInputStream In = new ObjectInputStream(FileIn);
         
         Statistics = (Stats) In.readObject();

         In.close();
         FileIn.close();
      } 
      catch (ClassNotFoundException ex) 
      { 
          Statistics = new Stats();
          Statistics.CreateStats(name);
          SaveStats(name);
      } 
      catch (IOException e)
      {
          Statistics = new Stats();
          Statistics.CreateStats(name);
          SaveStats(name);
      }
    }
    
    public double GetBinding(int i,int j)
    {
        return Learner.getModel().getQ(i,j);
    }
    
    public int ExploredActions()
    {
        int ActionsCount = 0;
        int StatesCount = 0;
        boolean Found = false;
        for(int i=0; i<Learner.getModel().getQ().getRowCount(); i++)
        {
            Found = false;
            for(int j=0; j<Learner.getModel().getQ().getColumnCount(); j++)
            {
                double Reward = Learner.getModel().getQ(i,j);
                if(Reward != 0.1)
                {
                    ActionsCount++;
                    Found = true;
                }
            }
            
            if(Found)
            {
              StatesCount++;  
            }
        }
        
        System.out.println("STATES COUNT: "+StatesCount+" ACTIONS COUNT: "+ActionsCount);
        Statistics.SeenStates = StatesCount;
        Statistics.TriedActions = ActionsCount;
        return ActionsCount;
    }
    
    public void RecoverExperienceFromDatabase()
    {
        DB.LoadDatabase(name);
        
        SaveLearner(Learner);
        
        Set<Entry<Integer,Action>> View = DB.GetDBView();
        
        for(Entry<Integer,Action> E : View)
        {
            int State = E.getKey();
            Action Act = E.getValue();
            
            Learner.getModel().setQ(State,Act.Code,Act.Gain);
            System.out.println("RECUPERATO STATO: "+State+" AZIONE:"+Act.Code+" REWARD: "+Act.Gain);
        }
        
        SaveLearner(Learner);
        
    }
    
    public void TraceExperienceOnDatabase()
    {       
        int Rows = Learner.getModel().getStateCount();
        int Columns = Learner.getModel().getActionCount();
        
        int StepCount = 0;
        
        for(int i=0;i<Rows;i++)
        {
            for(int j=0;j<Columns;j++)
            {
                double R = Learner.getModel().getQ(i,j);
                
                if(R != 0.1)
                {
                    DB.SerialUpdate(i, j, R, name);
                    StepCount++;
                }
            }
        }
        
        
    }
    
    public QLearner GetLearner()
    {
        return Learner;
    }
    
    public void PlotValueFunction()
    {
       FileWriter PlotFile = null;
       String path = "src/main/statisticsdumps/plottable/";
        try
        {
            File Plot = new File(path+"ValueFunction.txt");
            PlotFile = new FileWriter(Plot,true);
            PlotFile.write("State|MeanQFunction\n");
            for(int i=0; i<Learner.getModel().getStateCount(); i++)
            {
                double Mean = 0.0;
                int Size = Learner.getModel().getActionCount();
                int Discovered = 0;
                
                for(int j=0; j<Size; j++)
                {
                    double Value = Learner.getModel().getQ(i,j);
                    if(Value != 0.1)
                    {
                        Mean += Value;
                        Discovered++;
                    }
                }
                
                Mean = Mean / (Discovered * 1.0);
                
                if(Mean != 0.0)
                {
                    String MeanValue = String.format("%.4f",Mean);
                    PlotFile.write(i+"|"+MeanValue+"\n");
                }
            }
            PlotFile.close();
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        } 
        finally 
        {
            try 
            {
                PlotFile.close();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        } 
    }
/*----FINE METODI DI SERIALIZZAZIONE----*/       
}
