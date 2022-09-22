import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Adam on 01/11/2021
 *
 * @author : Adam
 * @date : 01/11/2021
 * @projects : AuthenticationLab-Assingment-2
 */
public class PolicyReader {

        public static HashMap<String, HashMap<String,Boolean>> readAccessControlPolicy() throws Exception {
            return get2DHashMapFrom2DArray(readFile());
        }

        private static String[][] readFile() throws FileNotFoundException {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(".\\RMI-Printer-Server\\src\\main\\AccessControlList.txt")));
            int rows = 10;
            int columns = 13;
            String [][] myArray = new String[rows][columns];
            while(sc.hasNextLine()) {
                for (int i=0; i<myArray.length; i++) {
                    String[] line = sc.nextLine().trim().split(" ");
                    for (int j=0; j<line.length; j++) {
                        myArray[i][j] = (line[j]);
                    }
                }
            }
            for (int i = 0; i < myArray.length; i++) {
                for (int j = 0; j < myArray[i].length; j++) {
                   // System.out.println(myArray[i][j]);
                }
            }
            return myArray;
        }

        private static HashMap<String, HashMap<String,Boolean>> get2DHashMapFrom2DArray(String[][] table){
            HashMap<String, HashMap<String,Boolean>> policy = new HashMap<String, HashMap<String,Boolean>>();
            for (int j = 1; j < table[0].length; j++) {
                HashMap<String,Boolean> entry = new HashMap<String,Boolean>();
                for (int i = 1; i < table.length; i++) {
                     entry.put(table[i][0],table[i][j].equals("1"));
                }
                policy.put(table[0][j],entry);
            }
            return policy;
        }
}