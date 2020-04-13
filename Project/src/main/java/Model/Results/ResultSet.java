package Model.Results;

import java.util.LinkedList;

public interface ResultSet {
    public void addResult(double result);
    public LinkedList<Double> getResultSet();
    public void setResultSet(LinkedList<Double> resultSet);
}
