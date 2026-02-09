package src.main.java.com.rental.model;

public enum CarType {
    SEDAN("SEDAN", 50.0),
    SUV("SUV", 75.0),
    VAN("Van", 100.0);

private final String displayName;
private final double dailyRate;

CarType(String displayName, double dailyRate){
  this.displayName = displayName;
  this.dailyRate = dailyRate;
}

public String getDisplayName(){
  return displayName;
}

public double getDailyRate(){
  return dailyRate;
}

}
