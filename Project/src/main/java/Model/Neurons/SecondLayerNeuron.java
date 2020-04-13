package Model.Neurons;

import Main.Settings;
import Model.Results.FirstLayerResultSet;
import Model.Instances.Instance;

import java.util.LinkedList;

public class SecondLayerNeuron implements Neuron {
    LinkedList<Double> wages;
    int index;

    public SecondLayerNeuron(int index) {
        this.wages = new LinkedList<Double>();
        this.index = index;
        initializeWages();
    }

    @Override
    public void initializeWages() {
        for (int i = 0; i< Settings.numberOfNeuronsInFirstLayer; i++) {
            wages.add(Math.random());
        }
    }

    @Override
    public double calculate(Instance instance) {
        return 0;
    }

    @Override
    public double calculate(FirstLayerResultSet resultSet) {
        double result = 0;
        for (int i = 0; i < wages.size(); i++) {
            result += (wages.get(i) * resultSet.getResultSet().get(i));
        }
        return result;
    }

    @Override
    public double activate(Instance instance) {
        return 0;
    }

    @Override
    public double activate(FirstLayerResultSet resultSet) {
        return calculate(resultSet);
    }

    @Override
    public double derivative(double value) {
        return 1;
    }

    public int getIndex() {
        return index;
    }

    public LinkedList<Double> getWages() {
        return wages;
    }

    public void setWages(LinkedList<Double> wages) {
        this.wages = wages;
    }
}
