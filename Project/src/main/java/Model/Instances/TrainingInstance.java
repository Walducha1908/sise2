package Model.Instances;

import Main.Settings;
import Model.MeasurementPoint;
import Model.ReferencePoint;

import java.util.LinkedList;

public class TrainingInstance extends Instance {

    public TrainingInstance() {
        super();
    }

    @Override
    public String toString() {
        return "TrainingInstance {" + "\n" +
                "measurementPoints = " + measurementPoints + "\n" +
                "referencePoint = " + referencePoint +
                '}';
    }
}
