import java.util.ArrayList;

/**
 * Roster - program to contain a roster of players.
 * Note: The uniformNumber is the key attribute.
 * @author  D. Patric
 * @version 2215
 * @ASSESSME.INTENSITY:HIGH
 */
 
public class Roster{
   private int uniformNumber;
   private String playerName;
   private String position;
   private int heightInInches;
   private double weightInPounds;
   private String experience;
   private String college;
   
   /**
    * constructor - all fields required
    */
   public Roster(int num, String name, String pos, int height, double weight, String exp, String _college) {
      uniformNumber = num;
      playerName = name;
      position = pos;
      heightInInches = height;
      weightInPounds = weight;
      experience = exp;
      college = _college;
   }
   
   /**
    * accessors
    */
   public int getUniformNumber() { 
      return uniformNumber; }
   public String getPlayerName() { 
      return playerName; }
   public String getPosition() { 
      return position; }
   public int getHeightInInches() { 
      return heightInInches; }
   public double getWeightInPounds() { 
      return weightInPounds; }
   public String getExperience() { 
      return experience; }
   public String getCollege() { 
      return college; }
   
   /**
    * modifiers
    */
   public void setUniformNumber(int num) { uniformNumber = num; }
   public void setPlayerName(String name) { playerName = name; }
   public void setPosition(String pos) { position = pos; }
   public void setHeightInInches(int height) { heightInInches = height; }
   public void setWeightInPounds(double weight) { weightInPounds = weight; }
   public void setExperience(String exp) { experience = exp; }
   public void setCollege(String _college) { college = _college; }

   /**
    * toString
    */
   public String toString() {
      return String.format("%2d %-20.20s %-3.3s %2d %5.2f %2.2s %-20.20s",
                           uniformNumber, playerName, position, heightInInches,
                           weightInPounds, experience, college);
   }

   /* 
   @Override
   public int compareTo(Object o) {
      // TODO Auto-generated method stub
      return playerName.compareTo(o.getPlayerName());
   }
   */
}



