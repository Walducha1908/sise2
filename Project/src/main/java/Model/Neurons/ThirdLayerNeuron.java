package Model.Neurons;

import Main.Settings;
import Model.Instances.Instance;
import Model.Results.FirstLayerResultSet;
import Model.Results.SecondLayerResultSet;
import org.apache.poi.ss.formula.functions.T;

import java.util.LinkedList;

public class ThirdLayerNeuron implements Neuron {
    LinkedList<Double> wages;
    int index;

    public ThirdLayerNeuron(int index) {
        this.wages = new LinkedList<Double>();
        this.index = index;
        initializeWages();
    }

    public ThirdLayerNeuron(ThirdLayerNeuron neuron) {
        this.wages = new LinkedList<Double>();
        this.index = neuron.index;

        for (int i = 0; i < neuron.getWages().size(); i++) {
            this.wages.add(neuron.getWages().get(i));
        }
    }

    @Override
    public void initializeWages() {
        for (int i = 0; i < Settings.numberOfNeuronsInSecondLayer; i++) {
            wages.add((Math.random() * 2) - 1);
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

    public double calculate(SecondLayerResultSet resultSet) {
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

    public double activate(SecondLayerResultSet resultSet) {
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

    @Override
    public String toString() {
        return "ThirdLayerNeuron{" +
                "wages=" + wages +
                ", index=" + index +
                '}';
    }
}
