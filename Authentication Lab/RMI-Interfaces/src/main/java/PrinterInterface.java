import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A simple interface which does some printing and returning.
 * All methods that are defined in the interface must throw a RemoteException
 *
 */
public interface PrinterInterface extends Remote {

    public String print(String filename, String printer, String token) throws RemoteException;   // prints file filename on the specified printer
    public String queue(String printer,String token) throws RemoteException;   // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    public String topQueue(String printer, String job, String token) throws RemoteException;   // moves job to the top of the queue
    public String start(String token) throws RemoteException;   // starts the print server
    public String stop(String token) throws RemoteException;   // stops the print server
    public String restart(String token) throws RemoteException;   // stops the print server, clears the print queue and starts the print server again
    public String status(String printer, String token) throws RemoteException;  // prints status of printer on the user's display
    public String readConfig(String parameter, String token) throws RemoteException;   // prints the value of the parameter on the user's display
    public String setConfig(String parameter, String value, String token) throws RemoteException;   // sets the parameter to value

    public String login(String username, String Password) throws RemoteException; //login to system
    public String logout(String token) throws RemoteException; //Logout from system
}
