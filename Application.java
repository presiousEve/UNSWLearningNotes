import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
   This is the main class contains the main funtion, the access for the whole program
   This program has 7 activeties, you can choose the number of every activities.
   The 7 activities are:
     * 1.Add a slot, all information provided by users
       2.Delete a slot by slot ID (only if not occupied – give appropriate error message otherwise)
       3.List all slots in a well-defined format with information including slot ID, slot type, whether occupied, 
        and if occupied, show the vehicle registration and the owner.
       4.Park a vehicle into a slot (provide slot ID and car information). 
        Handle the cases where the slot is (i) non-existent, and (ii) occupied.
       5.Find a vehicle by registration number and show the slot and the owner if the vehicle is in. 
        Handle the case where the vehicle is not actually parked.
       6.Remove a vehicle by registration number. Handle the case where the vehicle is not actually parked.
       7.Exit
 *
 * @author: Yuting GUO (102893820)
 * @version: 29/02/2020
 */


public class Application
{   
    //the main funtion:
    public static void main(String[] args) {
        //set a number to store the chosen number
        int choice = 0;
        Scanner input = new Scanner(System.in);
        //create a new object of class CarPark:
        CarPark carPark = new CarPark();                
        //the mark of exit the whole system（case 7）
        
        while(choice != 7) {                       
            System.out.println("\nWelcome to Parking Spot System!\n------------------------------------------------------\n Please enter the opration number: \n  1.Add a slot;\n  2.Delete a slot;\n  3.Show all Slots;\n  4.Park a vehicle;\n  5.Find a vehicle;\n  6.Remove a vehicle;\n  7.Exit the system.\n------------------------------------------------------");
            int option = input.nextInt();
            int count = 0;

            switch(option) {
                //===============================================Add a slot======================================================
                case 1:
                    //if the input is incorrect, return to current opration
                    boolean flag = true;  
                    String slotId = "";                  
                    while (flag) {
                        //check if input vaild
                        boolean tmp = checkId(slotId);
                        if (tmp) {
                            //if slod ID not exit, add it.
                            flag = false;                           
                            break;
                        } else {
                            if (count > 0) {
                                System.out.println("Invalid input, please try again.\n");
                            }
                            count += 1;
                            System.out.println( "Please input a new slot ID which starts with a capital letter, followed by a three-digit number.\n('E' for employee; 'D' for visiter)\n Example: 'E123' / 'D456'.");
                            slotId = input.next().toUpperCase();
                        }
                    }
                    //create a class slot object, store the slot id and slot type, the slot is empty, so vhiechel is null
                    Slot slot = new Slot(slotId, slotId.substring(0,1), null);
                    System.out.println(carPark.addSlot(slot));
                    break;

                //=============================================delete a slot=====================================================
                case 2:
                    System.out.println( "Please input a delete slot ID which starts with a capital letter, followed by a three-digit number.\n('E' for employee; 'D' for visiter)\n Example: 'E123' / 'D456'." );
                    String deleteSlotId = input.next().toUpperCase();

                    if (checkId(deleteSlotId)) {//if slot ID is exist
                        //call the delete function
                        System.out.println( carPark.deleteSlot(deleteSlotId) );
                    } else {
                        System.out.println("Input slot id invalid. Please try again.\n");
                    }
                    break;

                //=============================================List all slots=====================================================
                case 3:
                    System.out.println("This is the information of all slots.\n 'E' means employee, 'D' means visiter.");
                    carPark.listSlot();
                    break;

                //=============================================Park a vehicle into a slot=====================================================
                case 4:
                    boolean flag1 = true;  
                    String registrationNum = ""; 
                    String owner = "";
                    String ownerType = "";

                    while (flag1) {
                        //check if input vaild
                        boolean tmp = checkRegistrationNum(registrationNum);
                        if (tmp) {
                            flag1 = false;
                            System.out.println("Invalid input, please try again.");               
                            break;
                        } else {
                            System.out.println("Please input the vehicle registration number(two uppercase letters followed by 3 digits)\nExample: 'TZ234'");
                            registrationNum = input.next().toUpperCase();                            
                        }
                    }
                    System.out.println("Please input the vehicle's owner and owner type ('E' for employee and 'D' visiter):\n (use ',' to saprate them)\n WARING! This has to be right!!!\n");
                    
                    //get owner and owner type from keyboard
                    String ownerString = input.next();
                    //store owner and owner type as a list
                    String[] listOwnerString = ownerString.split(",");
                    
                    //create a new vehicle object into this slot
                    owner = listOwnerString[0];
                    ownerType = listOwnerString[1].toUpperCase();
                    Vehicle vehicle = new Vehicle(registrationNum, owner, ownerType);
                    
                    boolean flag2 = true; 
                    //slotId = "";
                    System.out.println("Please choose a slot:"); 
                    carPark.listSlotId();//list all the solt                              
                    slotId = input.next().toUpperCase();
                    while (flag2) {
                        
                        boolean tmp = carPark.findSlot(slotId);
                        //check if the slot exit
                        if (tmp) {
                            //check if the slot and owner are same type
                            if (slotId.substring(0, 1).equals(ownerType)){
                                //park the vehicle
                                System.out.println( carPark.parkVehicle(slotId, vehicle) );

                            } else {
                                System.out.println("The owner and the slot are not the same type. Please choose another slot.");
                            }
                            flag2 = false;
                        } else {
                            flag2 = false;
                            System.out.println("The slot does not exist.");
                            break;                            
                        }
                    }
                    break;

                //=============================================Find a vehicle=====================================================
                case 5:
                    System.out.println("Please input vehicle registration number:");
                    String findVehicle = input.next().toUpperCase();
                    if (checkRegistrationNum(findVehicle)) {
                        carPark.findVehicleByRegistrationNum(findVehicle);
                    }
                    break;

                //=============================================Remove a vehicle=====================================================
                case 6:
                    System.out.println("Please input the vehicle registration number want to remove.");
                    String removeVehicle = input.next().toUpperCase();
                    if (checkRegistrationNum(removeVehicle)) {
                        carPark.removeVehicleByRegistrationNum(removeVehicle);
                    }
                    break;

                //=============================================Exit=====================================================
                case 7:
                    System.out.println( "You have exited the system. Good Bye!\n" );
                    choice = 7;
                    break;
             }
        }
    }

    //a function to check the slotID （would be better if use regular expression）
    //the slot ID has to be started at 'E' or 'D', which save a field to store the type of slot, make the system more simplify.
    //about the uppercase  dose not be checked is because:
    //  1, its more convinent for user to input the message. its OK if some users lazy
    //  2, increase fault tolerance for the whole system
    public static boolean checkId(String slotId) {
        //slotId = slotId.toUpperCase();
        if (slotId.length() != 4) {
            return false;
        } else if (!slotId.substring(0,1).equals("E") & !slotId.substring(0,1).equals("D")) {
            return false;
        } else if (!Character.isDigit(slotId.charAt(1)) || !Character.isDigit(slotId.charAt(2)) || !Character.isDigit(slotId.charAt(3))) {
            return false; 
        }
        return true;
    }

    //a function to check the registration number
    public static boolean checkRegistrationNum(String registrationNum) {
        if (registrationNum.length() != 5) {
            return false;
        } else if (registrationNum.charAt(0) <'A' || registrationNum.charAt(0) > 'Z') {
            return false;
        } else if (registrationNum.charAt(1) <'A' || registrationNum.charAt(1) > 'Z') {
            return false;
        } else if (!Character.isDigit(registrationNum.charAt(2)) || !Character.isDigit(registrationNum.charAt(3)) || !Character.isDigit(registrationNum.charAt(4))) {
            return false;
        }
        return true;
    }
    
}
