package Model.Instances;

import Main.Settings;
import Model.MeasurementPoint;
import Model.ReferencePoint;

import java.util.LinkedList;

public class Instance {
    LinkedList<MeasurementPoint> measurementPoints;
    ReferencePoint referencePoint;

    public Instance() {
        this.measurementPoints = new LinkedList<MeasurementPoint>();
        this.referencePoint = new ReferencePoint();
    }

    /*
        Last added measurement point is the one we are training with.
        Other points - points that were measured before our training point.
     */
    public boolean addMeasurementPoint(MeasurementPoint measurementPoint) {
        if (measurementPoints.size() < Settings.numberOfPointsConsidered) {
            measurementPoints.add(measurementPoint);
            return true;
        }
        return false;
    }

    public LinkedList<MeasurementPoint> getMeasurementPoints() {
        return measurementPoints;
    }

    public ReferencePoint getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(ReferencePoint referencePoint) {
        this.referencePoint = referencePoint;
    }

}
