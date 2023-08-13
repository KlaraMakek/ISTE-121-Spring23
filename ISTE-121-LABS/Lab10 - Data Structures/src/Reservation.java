/**
 * Reservation - class to represent a reservation
 * @author  D. Patric
 * @version 2205
 */

/**
 * Reservation System
 * @author Klara Makek
 * @version 2023
 *          ISTE 121 LAB09
 */
/**
 * !!!!! implements Comparable<Reservation>
 */
public class Reservation implements Comparable<Reservation> {
   private String confirmation;
   private String name;
   private String widget;
   private String startDateTime;

   public Reservation(String _conf, String _name,
         String _widget, String _startDateTime) {
      confirmation = _conf;
      name = _name;
      widget = _widget;
      startDateTime = _startDateTime;
   }

   // accessor/mutator for confiration #
   public String getConfirmation() {
      return confirmation;
   }

   public void setConfirmation(String _conf) {
      confirmation = _conf;
   };

   // accessor/mutator for name
   public String getName() {
      return name;
   }

   public void setName(String _name) {
      name = _name;
   };

   // accessor/mutator for widget
   public String getWidget() {
      return widget;
   }

   public void setWidget(String _widget) {
      widget = _widget;
   };

   // accessor/mutator for start date and time
   public String getStartDateTime() {
      return startDateTime;
   }

   public void setStartDateTime(String _startDateTime) {
      startDateTime = _startDateTime;
   };

   /** toString */
   public String toString() {
      return String.format("%-7.7s %-20.20s %-17.17s %s",
            confirmation, name, widget, startDateTime);
   }

   @Override
   public int compareTo(Reservation o) {
      /** comapre to name */
   /*can be reverse
    *       return o.getName().compareTo(name);
    */
      return name.compareTo(o.getName());

   }
}