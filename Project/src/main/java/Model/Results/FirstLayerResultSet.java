package Model.Results;

import java.util.LinkedList;

public class FirstLayerResultSet implements ResultSet {

    private LinkedList<Double> resultSet;

    public FirstLayerResultSet() {
        resultSet = new LinkedList<Double>();
    }

    public void addResult(double result) {
        resultSet.add(result);
    }

    public LinkedList<Double> getResultSet() {
        return resultSet;
    }

    public void setResultSet(LinkedList<Double> resultSet) {
        this.resultSet = resultSet;
    }
}
