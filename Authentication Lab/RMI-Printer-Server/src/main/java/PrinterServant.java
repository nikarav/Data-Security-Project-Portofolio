import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PrinterServant extends UnicastRemoteObject implements PrinterInterface {

    private HashMap<String, Printer> printers;
    private Boolean serverStatus;
    private HashMap<String,String> tokens = new HashMap<String,String>();
    private HashMap<String,String> configuration = new HashMap<String,String>();

    public PrinterServant() throws RemoteException{
        super();
        printers = new HashMap<>();
        Printer printer1 = new Printer("printer1", List.of("file1", "file2","file3", "file4"));
        Printer printer2 = new Printer("printer2", List.of("file5", "file6"));
        printers.put("printer1", printer1);
        printers.put("printer2", printer2);
        configuration.put("colour", "Black-White");
        configuration.put("size", "A4");
        serverStatus = true;
    }

    @Override
    public String print(String filename, String printer,String token) throws RemoteException {
        try {
            if(serverStatus.equals(false))
                return tokens.containsKey(Security.decrypt(token)) ? Security.encrypt("Server is not running"): Security.encrypt("Not Authenticated");
            return tokens.containsKey(Security.decrypt(token)) ? Security.encrypt(printers.get(Security.decrypt(printer)).print(Security.decrypt(filename))) : Security.encrypt("Not Authenticated");
        }catch(Exception e){
            return Security.encrypt("System error: filename, printer doesn't match an existing one or another error occurred");
        }
    }


    @Override
    public String queue(String printer, String token) throws RemoteException {
        try{
            if(serverStatus.equals(false))
                return tokens.containsKey(Security.decrypt(token)) ? Security.encrypt("Server is not running"): Security.encrypt("Not Authenticated");

            return tokens.containsKey(Security.decrypt(token))?Security.encrypt(printers.get(Security.decrypt(printer)).queue()):Security.encrypt("Not Authenticated");
        }catch(Exception e){
            return Security.encrypt("System error:  printer doesn't match an existing one or another error occurred");
        }
    }

    @Override
    public String topQueue(String printer, String job, String token) throws RemoteException {
        try {
            if(serverStatus.equals(false))
                return tokens.containsKey(Security.decrypt(token)) ? Security.encrypt("Server is not running"): Security.encrypt("Not Authenticated");
            return tokens.containsKey(Security.decrypt(token)) ? Security.encrypt(printers.get(Security.decrypt(printer)).topQueue(Integer.parseInt(Security.decrypt(job)))) : Security.encrypt("Not Authenticated");
        } catch (Exception e) {
            return Security.encrypt("System error:  printer, job doesn't match an existing one or another error occurred");
        }
    }

    @Override
    public String start(String token) throws RemoteException {
        return tokens.containsKey(Security.decrypt(token))?Security.encrypt(startServer()):Security.encrypt("Not Authenticated");
    }

    @Override
    public String stop(String token) throws RemoteException {
        return tokens.containsKey(Security.decrypt(token))?Security.encrypt(stopServer()):Security.encrypt("Not Authenticated");
    }

    @Override
    public String restart(String token) throws RemoteException {
        if(serverStatus.equals(false))
            return tokens.containsKey(Security.decrypt(token)) ? Security.encrypt("Server is not running"): Security.encrypt("Not Authenticated");
        if(tokens.containsKey(Security.decrypt(token))) {
            StringBuilder b = new StringBuilder();
            b.append("Shutting down server...\n");
            b.append("Clearing print queue...\n");
            for (Printer printer : this.printers.values()) {
                printer.clear();
            }
            b.append("Print queue cleared\n");
            b.append("Starting server...\n");
            return Security.encrypt(String.valueOf(b));
        }else{
            return Security.encrypt("Not Authenticated");
        }
    }

    @Override
    public String status(String printer, String token) throws RemoteException {
        if(serverStatus.equals(false))
            return tokens.containsKey(Security.decrypt(token)) ? Security.encrypt("Server is not running"): Security.encrypt("Not Authenticated");
        if(tokens.containsKey(Security.decrypt(token))) {
            try {
                if (printers.containsKey(Security.decrypt(printer))) {
                    return Security.encrypt("Printer is online + \n" + printers.get(Security.decrypt(printer)).status());
                }
                return Security.encrypt("Printer is offline");
            }catch(Exception e){
                return Security.encrypt("System error:  printer doesn't match an existing one or another error occurred");
            }
        }else{
            return Security.encrypt("Not Authenticated");
        }
    }

    @Override
    public String readConfig(String parameter, String token) throws RemoteException {
        if(serverStatus.equals(false))
            return tokens.containsKey(Security.decrypt(token)) ? Security.encrypt("Server is not running"): Security.encrypt("Not Authenticated");
        if(tokens.containsKey(Security.decrypt(token))) {
            try {
                return Security.encrypt(configuration.get(Security.decrypt(parameter)));
            }catch(Exception e){
                return Security.encrypt("System error:  Parameter doesn't match an existing one or another error occurred");
            }
        }else{
            return Security.encrypt("Not Authenticated");
        }
    }

    @Override
    public String setConfig(String parameter, String value, String token) throws RemoteException {
        if(serverStatus.equals(false))
            return tokens.containsKey(Security.decrypt(token)) ? Security.encrypt("Server is not running"): Security.encrypt("Not Authenticated");
        if(tokens.containsKey(Security.decrypt(token))) {
            try {
                if(configuration.containsKey(Security.decrypt(parameter))){
                    configuration.put(Security.decrypt(parameter), Security.decrypt(value));
                    return Security.encrypt("Configuration successfully updated");
                }else{
                    return Security.encrypt("Parameter doesnt exist");
                }
            }catch(Exception e){
                return Security.encrypt("System error:  Parameter doesn't match an existing one or another error occurred");
            }
        }else{
            return Security.encrypt("Not Authenticated");
        }
    }

    @Override
    public String login(String username, String password) throws RemoteException {
        return UserExists(Security.decrypt(username),Security.decrypt(password))?Security.encrypt(generateARandomToken(Security.decrypt(username))):Security.encrypt("username or Password incorrect");
    }

    @Override
    public String logout(String token) throws RemoteException {
        tokens.remove(Security.decrypt(token));
        return Security.encrypt("Logged Out");
    }

    private Boolean UserExists(String Username, String Password){
        DatabaseConnection db = new DatabaseConnection();
        return db.userAuthenticated(Username,Password);
    }
    private String startServer() {
        if(serverStatus.equals(false)){
            serverStatus = true;
            return "Starting server...";
        }else {
            return "Server is already running!";
        }
    }

    private String stopServer() {
        if(serverStatus.equals(true)){
            serverStatus = false;
            return "Shutting down server...";
        }else {
            return "Server is already shut Down!";
        }
    }

    private String generateARandomToken(String Username) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 30;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        tokens.put(generatedString,Username);
        return generatedString;
    } // Article number 5 from https://www.baeldung.com/java-random-string

}
