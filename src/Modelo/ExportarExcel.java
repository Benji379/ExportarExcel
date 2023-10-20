package Modelo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import javax.swing.JTable;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExportarExcel {

    public static void exportToExcel(JTable table, String filePath, Color headerBackgroundColor) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");

        // Establecer el estilo para las celdas de encabezado
        XSSFCellStyle headerStyle = workbook.createCellStyle();

        // Configurar bordes para las celdas de encabezado
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Configurar el color de fondo de las celdas de encabezado
        headerStyle.setFillForegroundColor(new XSSFColor(headerBackgroundColor));
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Configurar el estilo de fuente para las celdas de encabezado
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true); // Texto en negrita para los encabezados
        headerFont.setColor(IndexedColors.BLACK.index); // Color de fuente negro
        headerStyle.setFont(headerFont);

        // Crear una fila para los encabezados
        XSSFRow headerRow = sheet.createRow(0);

        // Aplicar estilo a las celdas de encabezado
        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            XSSFCell headerCell = headerRow.createCell(columnIndex);
            headerCell.setCellValue(table.getColumnName(columnIndex));
            headerCell.setCellStyle(headerStyle);
        }

        // Iterar a través de las filas de la tabla
        for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
            XSSFRow excelRow = sheet.createRow(rowIndex + 1);

            // Establecer el estilo para las celdas de datos
            XSSFCellStyle cellStyle = workbook.createCellStyle();

            // Configurar bordes para las celdas de datos
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
                XSSFCell excelCell = excelRow.createCell(columnIndex);
                Object cellValue = table.getValueAt(rowIndex, columnIndex);

                if (cellValue != null) {
                    excelCell.setCellValue(cellValue.toString());
                } else {
                    excelCell.setCellValue(""); // Si el valor es nulo, se asigna una cadena vacía
                }
                excelCell.setCellStyle(cellStyle);
            }
        }

        // Ajustar automáticamente las columnas
        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }

        // Ajustar automáticamente las filas
        for (int rowIndex = 0; rowIndex <= table.getRowCount(); rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            if (row != null) {
                row.setHeight((short) -1); // Establecer altura automática para todas las filas
            }
        }

        // Aplicar el filtro automático
        XSSFAutoFilter filter = sheet.setAutoFilter(new CellRangeAddress(0, table.getRowCount(), 0, table.getColumnCount() - 1));

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
