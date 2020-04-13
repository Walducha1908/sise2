package Data;

import Main.Settings;
import Model.MeasurementPoint;
import Model.ReferencePoint;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class DataReader {
    private LinkedList<MeasurementPoint> measurementPointsList;
    private LinkedList<ReferencePoint> referencePointsList;

    public DataReader() {
        this.measurementPointsList = new LinkedList<MeasurementPoint>();
        this.referencePointsList = new LinkedList<ReferencePoint>();
    }

    public void readAllTrainingData() {
        for (int i=1; i<2; i++) {
            readOneTrainingFile(Settings.pathToData + "pozyxAPI_only_localization_measurement" + i + ".xlsx");
        }
    }

    public void readOneTrainingFile(String filepath) {
        try {
            FileInputStream excelFile = new FileInputStream(new File(filepath));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            iterator.next();

            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                cellIterator.next();
                cellIterator.next();
                cellIterator.next();

                int cellCounter = 0;
                MeasurementPoint measurementPoint = new MeasurementPoint();
                ReferencePoint referencePoint = new ReferencePoint();

                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();
                    cellCounter++;

                    if (cellCounter == 1) {
                        measurementPoint.setX(currentCell.getNumericCellValue());
                    } else if (cellCounter == 2) {
                        measurementPoint.setY(currentCell.getNumericCellValue());
                    } else if (cellCounter == 3) {
                        referencePoint.setX(currentCell.getNumericCellValue());
                    } else if (cellCounter == 4) {
                        referencePoint.setY(currentCell.getNumericCellValue());
                    }
                }

                measurementPointsList.add(measurementPoint);
                referencePointsList.add(referencePoint);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<MeasurementPoint> getMeasurementPointsList() {
        return measurementPointsList;
    }

    public LinkedList<ReferencePoint> getReferencePointsList() {
        return referencePointsList;
    }
}
