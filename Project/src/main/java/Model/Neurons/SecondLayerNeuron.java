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
        for (int i = 0; i < Settings.numberOfNeuronsInFirstLayer; i++) {
            wages.add(Math.random());
        }
    }

    @Override
    public double calculate(Instance instance) {
        double result = 0;
        for (int i = 0, j = 0; i < wages.size(); i++, j++) {
            result += (wages.get(i) * instance.getMeasurementPoints().get(j).getX());
            i++;
            result += (wages.get(i) * instance.getMeasurementPoints().get(j).getY());
        }
        return result;
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
        double sum = calculate(instance);
        return (1 / (1 + Math.exp(-0.00001 * sum)));
    }

    @Override
    public double activate(FirstLayerResultSet resultSet) {
        double sum = calculate(resultSet);
//        return (1 / (1 + Math.exp(-1 * sum)));
        return ((Math.exp(sum) - Math.exp(-1 * sum)) / (Math.exp(sum) + Math.exp(-1 * sum)));

//        return sum;
    }

    @Override
    public double derivative(double value) {
//        return (value * (1 - value));
        return ((4 * Math.exp(2 * value)) / Math.pow((Math.exp(2 * value) + 1), 2));
//        return 1;
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
