package Data;

import Calculations.Errors;
import Calculations.NeuralNetwork;
import Main.Settings;
import Model.Instances.TestingInstancesContainer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataWriter {
    public static void saveResults(NeuralNetwork neuralNetwork, NeuralNetwork startingNeuralNetwork, int eras) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        DecimalFormat df = new DecimalFormat("##.##");

        Integer rowCount = 0;

        Row row = sheet.createRow(++rowCount);
        Cell cell = row.createCell(1);
        cell.setCellValue("Results");

        createExcelRow("Final Error:", Double.toString(Errors.calculateErrorOffLine(neuralNetwork)), sheet, ++rowCount);
        createExcelRow("Testing Error:", Double.toString(Errors.calculateTestingError(neuralNetwork)), sheet, ++rowCount);
        createExcelRow("Eras :", Integer.toString(eras), sheet, ++rowCount);
        createExcelRow("Points Considered :", Integer.toString(Settings.numberOfPointsConsidered), sheet, ++rowCount);
        createExcelRow("First Layer neurons :", Integer.toString(Settings.numberOfNeuronsInFirstLayer), sheet, ++rowCount);
        createExcelRow("Second Layer neurons :", Integer.toString(Settings.numberOfNeuronsInSecondLayer), sheet, ++rowCount);

        rowCount += 1;
        createExcelRow("Final neural network", "", sheet, ++rowCount);

        createExcelRow("First Layer", "", sheet, ++rowCount);
        for (int i = 0; i < neuralNetwork.getFirstLayerNeurons().size(); i++) {
            int columnCount = 0;
            row = sheet.createRow(++rowCount);

            Cell valueCell = row.createCell(++columnCount);
            valueCell.setCellValue("Neuron " + i);

            for (int j = 0; j < Settings.numberOfPointsConsidered * 2; j++) {
                valueCell = row.createCell(++columnCount);
                valueCell.setCellValue(neuralNetwork.getFirstLayerNeurons().get(i).getWages().get(j));
            }
        }

        rowCount += 1;
        createExcelRow("Second Layer", "", sheet, ++rowCount);
        for (int i = 0; i < neuralNetwork.getSecondLayerNeurons().size(); i++) {
            int columnCount = 0;
            row = sheet.createRow(++rowCount);

            Cell valueCell = row.createCell(++columnCount);
            valueCell.setCellValue("Neuron " + i);

            for (int j = 0; j < Settings.numberOfNeuronsInFirstLayer; j++) {
                valueCell = row.createCell(++columnCount);
                valueCell.setCellValue(neuralNetwork.getSecondLayerNeurons().get(i).getWages().get(j));
            }
        }

        rowCount += 1;
        createExcelRow("Third Layer", "", sheet, ++rowCount);
        for (int i = 0; i < neuralNetwork.getThirdLayerNeurons().size(); i++) {
            int columnCount = 0;
            row = sheet.createRow(++rowCount);

            Cell valueCell = row.createCell(++columnCount);
            valueCell.setCellValue("Neuron " + i);

            for (int j = 0; j < Settings.numberOfNeuronsInSecondLayer; j++) {
                valueCell = row.createCell(++columnCount);
                valueCell.setCellValue(neuralNetwork.getThirdLayerNeurons().get(i).getWages().get(j));
            }
        }

        rowCount += 1;
        for (int i = 0; i < TestingInstancesContainer.testingInstancesList.size(); i++) {
            createExcelRow("Test instance " + i, Double.toString(Errors.calculateErrorForInstance(neuralNetwork, TestingInstancesContainer.testingInstancesList.get(i))), sheet, ++rowCount);
        }

        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);

        try (
                FileOutputStream outputStream = new FileOutputStream(
                        Settings.pathToReports +
                                "SISE2_Report_Error_" +
                                Math.round(Errors.calculateTestingError(neuralNetwork)) +
                                ".xlsx")) {
            workbook.write(outputStream);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public static void createExcelRow(String label, String value, XSSFSheet sheet, Integer rowCount) {
        Row row = sheet.createRow(rowCount);

        int columnCount = 0;
        Cell LabelCell = row.createCell(++columnCount);
        LabelCell.setCellValue(label);
        Cell valueCell = row.createCell(++columnCount);
        valueCell.setCellValue(value);
    }
}
