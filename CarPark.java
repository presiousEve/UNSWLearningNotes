import java.util.ArrayList;
import java.util.List;

/**
 * This is the CarPark class, which contains every oprations of this system
    The oprations are:
        1, addSlot(): add slot in this parking lot
        2, deleteSlot(): delete slot by slot ID
        3, existSlot(): check if the slot exit
        4, listSlot(): list all slot information
        5, 

 *
 * @author: Yuting GUO (102893820)
 * @version: 29/02/2020
 */

class CarPark {
    //create an arraylist to store every slot message, everyone in it is a slot object
    ArrayList<Slot> slotList = new ArrayList<Slot>();
    
    //This is add slot function
    //  check if the slot is already exist, if not, add in.
    public String addSlot(Slot slot) {
        if (existSlot(slot.getSlotId()) != -1) {//slot is already exist
            //System.out.println("slotID" + slot.getSlotId());
            return "Slot has existsed. Please add another slot.\n";
        } else {//if not exist, add slotID to slot list
            slotList.add(slot);
            return "Slot add success!\n";
        }
    }

    //This is delete slot funtion.
    //  1, check if the slot ID exist
    //  2, check if the slot ID occupied
    //  3, if the above conditions are true, delete this slot.
    public String deleteSlot(String slotId) {
        int slotIndex = existSlot(slotId);//get index of slot ID in slot list
        if (slotIndex != -1) {
            if (slotList.get(slotIndex).getOccupied()) {//check if this slot is occupied 
                return "This slot is currently occupied. Cannot be deleted.\n";
            } else {
                slotList.remove(slotIndex);
                return "Delete slot: " + slotId + " success!\n";
            } 
        } else {
            return "This slot dose not exist.\n";
        }
    }

    //This function is used to check if the slod by slot ID is exist
    //  if exist, return the index of slot
    //  if not ,return -1
    public int existSlot(String slotId) {//return the slot's location in slot list
        int i;
        for (i = 0; i < slotList.size(); i++) {
            //find slot in slot list throuth location and determine if they are equal
            if (slotList.get(i).getSlotId().equals(slotId)) {
                return i;
            }
        }
        return -1;
    }

    //This is list all slot information function
    //  if the slot has the vehicle in, show its information. Otherwise empty.
    public void listSlot() {
        System.out.println("Slot ID | Slot Type | Occupied or Not | Vehicle Registration | Owner");
        for (int i = 0; i < slotList.size(); i++) {
            System.out.print("\n" + String.format("%-10s", slotList.get(i).getSlotId()) + String.format("%-12s",slotList.get(i).getSlotType()) + String.format("%-6s",slotList.get(i).getOccupied()));
            if (slotList.get(i).getOccupied()){
                System.out.println( " " + slotList.get(i).getVehicle().getVehicleRegistration() + " " + (slotList.get(i).getVehicle()).getOwner() + "\n");
            }
        } 
    }

    //This funtion is used to show all the slot ID
    //  usually called by other opration functions
    public void listSlotId(){
        for (int i = 0; i < slotList.size(); i++){
            System.out.print(slotList.get(i).getSlotId() + "\n");
        }
    }

    //This function is used to check if the slot exist by slot ID
    //  return a boolean value
    public boolean findSlot(String slotId) { 
        int slotIndex = existSlot(slotId);
        if (slotIndex != -1) {
            //System.out.println(slotList.get(slotIndex));
            return true;
        } else {
            return false;
        }
    }

    //This is parkVehichel function. used to park a new car in the parking slot
    //  1, check if the slot type is same as the vehichel owner type（in main function）
    //  2, check if the slot is occupied
    //  3, if all above true, park the car into this vehicel
    public String parkVehicle(String slotId, Vehicle vehicle){
        //get slot ID 
        int i = existSlot(slotId);

        if (slotList.get(i).getOccupied()){//check if the solt oppcupied

            return "This slot is occupied. Please choose another slot.\n";

        } else {
            
            //set the vehichel in the current slot object
            slotList.get(i).setVehicle(vehicle);//declarition is null, set vehicel first 

            //change the slot occupation
            slotList.get(i).setOccupied(true);

            return " Vehicle " + vehicle.getVehicleRegistration() + " is parked in slot " + slotId + ".\n";
       
        } 
    }

    //This function is used by find vehicle by registration number
    //  if the vehciel does not parked, system out and choose park car opration 
    public void findVehicleByRegistrationNum(String registrationNum) {
        int i;
        //find vehicle in every slot
        for (i = 0; i < slotList.size(); i++) {
            if (slotList.get(i).getVehicle().getVehicleRegistration().equals(registrationNum)) {
                System.out.println("This vehicle in slot " + slotList.get(i).getSlotId() + "\nOwner is " + slotList.get(i).getVehicle().getOwner());
            } else{
                System.out.println("Cannot find this Vehicle.\n Please choose case4 to park this vehicle.\n");
            }
        }
        
    }

    //This function is used by remove vehciel by registration number
    //  if the vehciel does not parked, system out and choose park car opration 
    public void removeVehicleByRegistrationNum(String registrationNum){
        for (int i = 0; i < slotList.size(); i++){
            //if car number found
            if (slotList.get(i).getVehicle().getVehicleRegistration().equals(registrationNum)){
                //change slot occupation
                slotList.get(i).setOccupied(false);
                //put null to the vehicle for current object slot
                slotList.get(i).setVehicle(null);
                System.out.println("Remove success!\n");
            } else {
                System.out.println("The vehicle " + registrationNum + "dose not exist.\n Please choose case4 to park this vehicle.\n");
            }
        }
        
    }
   
}       
