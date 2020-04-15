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
    private LinkedList<MeasurementPoint> testingMeasurementPointsList;
    private LinkedList<ReferencePoint> testingReferencePointsList;

    public DataReader() {
        this.measurementPointsList = new LinkedList<MeasurementPoint>();
        this.referencePointsList = new LinkedList<ReferencePoint>();
        this.testingMeasurementPointsList = new LinkedList<MeasurementPoint>();
        this.testingReferencePointsList = new LinkedList<ReferencePoint>();
    }

    public void readAllTrainingData() {
        for (int i = Settings.startFileOffset; i < Settings.startFileOffset + Settings.filesToRead; i++) {
            readOneTrainingFile(Settings.pathToData + "pozyxAPI_only_localization_measurement" + i + ".xlsx");
        }
    }

    public void readAllTestingData() {
        readOneTestingFile(Settings.pathToData + "pozyxAPI_only_localization_dane_testowe_i_dystrybuanta.xlsx");
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
//                        measurementPoint.setX(currentCell.getNumericCellValue());
                        measurementPoint.setX(currentCell.getNumericCellValue() / Settings.dataDivisor);
                    } else if (cellCounter == 2) {
//                        measurementPoint.setY(currentCell.getNumericCellValue());
                        measurementPoint.setY(currentCell.getNumericCellValue()/ Settings.dataDivisor);
                    } else if (cellCounter == 3) {
//                        referencePoint.setX(currentCell.getNumericCellValue());
                        referencePoint.setX(currentCell.getNumericCellValue() / Settings.dataDivisor);
                    } else if (cellCounter == 4) {
//                        referencePoint.setY(currentCell.getNumericCellValue());
                        referencePoint.setY(currentCell.getNumericCellValue() / Settings.dataDivisor);
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

    public void readOneTestingFile(String filepath) {
        try {
            FileInputStream excelFile = new FileInputStream(new File(filepath));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            iterator.next();

            while (iterator.hasNext() && testingMeasurementPointsList.size() < 1540) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                cellIterator.next();
                cellIterator.next();
                cellIterator.next();

                int cellCounter = 0;
                MeasurementPoint measurementPoint = new MeasurementPoint();
                ReferencePoint referencePoint = new ReferencePoint();

                while (cellIterator.hasNext() && cellCounter < 5) {

                    Cell currentCell = cellIterator.next();
                    cellCounter++;

                    if (cellCounter == 1) {
//                        measurementPoint.setX(currentCell.getNumericCellValue());
                        measurementPoint.setX(currentCell.getNumericCellValue() / Settings.dataDivisor);
                    } else if (cellCounter == 2) {
//                        measurementPoint.setY(currentCell.getNumericCellValue());
                        measurementPoint.setY(currentCell.getNumericCellValue()/ Settings.dataDivisor);
                    } else if (cellCounter == 3) {
//                        referencePoint.setX(currentCell.getNumericCellValue());
                        referencePoint.setX(currentCell.getNumericCellValue() / Settings.dataDivisor);
                    } else if (cellCounter == 4) {
//                        referencePoint.setY(currentCell.getNumericCellValue());
                        referencePoint.setY(currentCell.getNumericCellValue() / Settings.dataDivisor);
                    }
                }

                testingMeasurementPointsList.add(measurementPoint);
                testingReferencePointsList.add(referencePoint);
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

    public LinkedList<MeasurementPoint> getTestingMeasurementPointsList() {
        return testingMeasurementPointsList;
    }

    public LinkedList<ReferencePoint> getTestingReferencePointsList() {
        return testingReferencePointsList;
    }
}
