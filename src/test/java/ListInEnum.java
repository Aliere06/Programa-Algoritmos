
import java.util.ArrayList;
import java.util.Random;

public enum ListInEnum {
    ENUM_A(new ArrayList<Dint>()),
    ENUM_B(new ArrayList<Dint>());

    private ArrayList<Dint> list;

    public static class Dint {
        double dValue;    
        int iValue;

        Dint(double dValue) {
            this.iValue = (int) dValue;
            this.dValue = dValue;
        }

        @Override
        public String toString() {
            return "double= " + dValue + ", int= " + iValue;
        }
    }

    ListInEnum(ArrayList<Dint> list){
        this.list = list;
    }

    public ArrayList<Dint> getList() {
        return list;
    }

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            Dint value = new Dint(random.nextDouble());
            if (value.dValue >= .5) {
                ENUM_A.getList().add(value);
            } else {
                ENUM_B.getList().add(value);
            }
        }
        // Print the lists
        System.out.println("List in ENUM_A:");
        System.out.println(ENUM_A.getList());
        System.out.println("List in ENUM_B:");
        System.out.println(ENUM_B.getList());
    }
}
