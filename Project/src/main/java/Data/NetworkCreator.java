package Data;

import Calculations.NeuralNetwork;
import Main.Settings;
import Model.MeasurementPoint;
import Model.ReferencePoint;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class NetworkCreator {
    public static NeuralNetwork recreateNetworkFromFile() {
        NeuralNetwork neuralNetwork = new NeuralNetwork();

        try {
            FileInputStream excelFile = new FileInputStream(new File(Settings.creationFile));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            for (int i = 0; i < 3; i++) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                cellIterator.next();
                Cell currentCell = cellIterator.next();

                if (i == 0) {
                    Settings.numberOfNeuronsInFirstLayer = (int) (currentCell.getNumericCellValue());
                } else if (i == 1) {
                    Settings.numberOfNeuronsInSecondLayer = (int) (currentCell.getNumericCellValue());
                } else if (i == 2) {
                    Settings.numberOfPointsConsidered = (int) (currentCell.getNumericCellValue());
                }
            }

            iterator.next();

            for (int i = 0; i < neuralNetwork.getFirstLayerNeurons().size(); i++) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                LinkedList<Double> wages = new LinkedList<Double>();
                cellIterator.next();
                for (int j = 0; j < neuralNetwork.getFirstLayerNeurons().get(i).getWages().size(); j++) {
                    Cell currentCell = cellIterator.next();
                    wages.add(Double.parseDouble(currentCell.getStringCellValue()));
                }
                neuralNetwork.getFirstLayerNeurons().get(i).setWages(wages);
            }

            iterator.next();

            for (int i = 0; i < neuralNetwork.getSecondLayerNeurons().size(); i++) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                LinkedList<Double> wages = new LinkedList<Double>();
                cellIterator.next();
                for (int j = 0; j < neuralNetwork.getSecondLayerNeurons().get(i).getWages().size(); j++) {
                    Cell currentCell = cellIterator.next();
                    wages.add(Double.parseDouble(currentCell.getStringCellValue()));
                }
                neuralNetwork.getSecondLayerNeurons().get(i).setWages(wages);
            }

            iterator.next();

            for (int i = 0; i < neuralNetwork.getThirdLayerNeurons().size(); i++) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                LinkedList<Double> wages = new LinkedList<Double>();
                cellIterator.next();
                for (int j = 0; j < neuralNetwork.getThirdLayerNeurons().get(i).getWages().size(); j++) {
                    Cell currentCell = cellIterator.next();
                    wages.add(Double.parseDouble(currentCell.getStringCellValue()));
                }
                neuralNetwork.getThirdLayerNeurons().get(i).setWages(wages);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(neuralNetwork);
        return neuralNetwork;
    }
}
