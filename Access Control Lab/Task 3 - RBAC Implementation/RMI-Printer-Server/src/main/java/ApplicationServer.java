import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ApplicationServer {

    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(8081);
        registry.rebind("printer", new PrinterServant());
    }
}
