package Model.Neurons;

import Main.Settings;
import Model.Results.FirstLayerResultSet;
import Model.Instances.Instance;

import java.util.LinkedList;

public class FirstLayerNeuron implements Neuron {
    LinkedList<Double> wages;
    int index;

    public FirstLayerNeuron(int index) {
        this.wages = new LinkedList<Double>();
        this.index = index;
        initializeWages();
    }

    public FirstLayerNeuron(FirstLayerNeuron neuron) {
        this.wages = new LinkedList<Double>();
        this.index = neuron.index;

        for (int i = 0; i < neuron.getWages().size(); i++) {
            this.wages.add(neuron.getWages().get(i));
        }
    }

    @Override
    public void initializeWages() {
        for (int i = 0; i < Settings.numberOfPointsConsidered * 2; i++) {
            wages.add((Math.random() * 2) - 1);
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
        return 0;
    }


    @Override
    public double activate(Instance instance) {
        double sum = calculate(instance);
//        double result = (1 / (1 + Math.exp( -1 * sum)));
        double result = ((Math.exp(sum) - Math.exp(-1 * sum)) / (Math.exp(sum) + Math.exp(-1 * sum)));
        return result;
//        return sum;
    }

    @Override
    public double activate(FirstLayerResultSet resultSet) {
        return 0;
    }

    @Override
    public double derivative(double value) {
//        return (value * (1 - value));
        return ((4 * Math.exp(2 * value)) / (Math.pow((Math.exp(2 * value) + 1), 2)));
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

    @Override
    public String toString() {
        return "FirstLayerNeuron{" +
                "wages=" + wages +
                ", index=" + index +
                '}';
    }
}
