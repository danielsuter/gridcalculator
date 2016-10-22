package ch.danielsuter.gridcalculator.writer;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.model.Group;
import ch.danielsuter.gridcalculator.model.Participant;
import ch.danielsuter.gridcalculator.util.GridCalculatorFormatter;
import com.google.common.base.Preconditions;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class RankingTemplate {
    private Workbook templateWorkbook;
    private Sheet templateSheet;
    private int currentRow;

    public RankingTemplate(File templateFile) {
        try {
            templateWorkbook = new XSSFWorkbook(new FileInputStream(templateFile));
        } catch (IOException ex) {
            throw new RuntimeException("Could not read template " + templateFile.getAbsolutePath());
        }

        Preconditions.checkArgument(templateWorkbook.getNumberOfSheets() == 1,
                "Template must have exactly one worksheet");
        templateSheet = templateWorkbook.getSheetAt(0);
        currentRow = 7;
    }

    public void setGroups(Iterable<Group> groups) {
        for (Group group : groups) {
            writeGroup(group);
        }
    }

    private void writeGroup(Group group) {
        writeHeader(group.getFilterCriteria());
        writeParticipants(group.getAll());
    }

    private void writeHeader(FilterCriteria filterCriteria) {
        String value = GridCalculatorFormatter.formatForGroupSheetHeader(filterCriteria);
        Row row = templateSheet.createRow(currentRow);
        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);

        currentRow++;
    }

    private void writeParticipants(List<Participant> participants) {
        for (int i = 0; i < participants.size(); i++) {
            Participant participant = participants.get(i);

            Row row = templateSheet.createRow(currentRow++);
            Cell rankingCell = row.createCell(0, Cell.CELL_TYPE_STRING);
            rankingCell.setCellValue(calculateRanking(i));

            Cell nameCell = row.createCell(1, Cell.CELL_TYPE_STRING);
            String fullName = participant.getFirstname() + " " + participant.getLastname();
            nameCell.setCellValue(fullName);

            Cell clubCell = row.createCell(2, Cell.CELL_TYPE_STRING);
            clubCell.setCellValue(participant.getClub());

        }
        currentRow++;
    }

    private String calculateRanking(int rowIndex) {
        String rang;
        switch (rowIndex) {
            case 0:
                rang = "1.";
                break;
            case 1:
                rang = "2.";
                break;
            case 2:
            case 3:
                rang = "3.";
                break;
            default:
                rang = "";
        }
        return rang;
    }

    public Workbook getWorkbook() {
        return templateWorkbook;
    }
}
