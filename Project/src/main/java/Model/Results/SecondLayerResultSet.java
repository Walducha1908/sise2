package Model.Results;

import Main.Settings;

import java.util.LinkedList;

public class SecondLayerResultSet implements  ResultSet{
    private LinkedList<Double> resultSet;

    public SecondLayerResultSet() {
        resultSet = new LinkedList<Double>();
    }

    public void addResult(double result) {
        if (resultSet.size() < Settings.numberOfNeuronsInSecondLayer) {
            resultSet.add(result);
        } else {
            System.out.println("Too many results in final results set");
        }
    }

    public LinkedList<Double> getResultSet() {
        return resultSet;
    }

    public void setResultSet(LinkedList<Double> resultSet) {
        this.resultSet = resultSet;
    }
}
