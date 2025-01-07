package com.rafael.atendimento.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.dto.ReportDTO;
import com.rafael.atendimento.dto.ServiceDayDTO;
import com.rafael.atendimento.dto.UserDTO;
import com.rafael.atendimento.dto.WeeklyHoursDTO;
import com.rafael.atendimento.entity.CustomerService;
import com.rafael.atendimento.enums.ServiceStatus;
import com.rafael.atendimento.repository.CustomerServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
	
	private final CustomerServiceRepository serviceRepository;
	private final ClassService classService;
	private final UserService userService;
	
	public ReportDTO generateReport(Long classId) {
	    List<CustomerService> customerService = serviceRepository.findByClazz_Id(classId);

	    long total = customerService.size();
	    long completed = customerService.stream().filter(a -> a.getStatus() == ServiceStatus.CONCLUIDO).count();
	    long cancelled = customerService.stream().filter(a -> a.getStatus() == ServiceStatus.CANCELADO).count();
	    
	    List<ServiceDayDTO> serviceDays = customerService.stream()
	    	    .collect(Collectors.groupingBy(
	    	        CustomerService::getDate,
	    	        Collectors.summingDouble(service -> {
	    	            long duration = Duration.between(service.getTime_start(), service.getTime_end()).toMinutes();
	    	            return duration / 60.0; // Convertendo minutos para horas
	    	        })
	    	    ))
	    	    .entrySet()
	    	    .stream()
	    	    .map(entry -> new ServiceDayDTO(entry.getKey(), entry.getValue()))
	    	    .collect(Collectors.toList());
	    
	    Map<String, WeeklyHoursDTO> weeklyUsage = customerService.stream()
	    	    .collect(Collectors.groupingBy(
	    	        service -> service.getOwner().getName(), // Agrupa pelo nome do proprietário
	    	        Collectors.groupingBy(
	    	            service -> "Semana " + service.getDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR), // Nomeia as semanas
	    	            Collectors.summingDouble(service -> {
	    	                long duration = Duration.between(service.getTime_start(), service.getTime_end()).toMinutes();
	    	                return duration / 60.0; // Converte minutos para horas
	    	            })
	    	        )
	    	    ))
	    	    .entrySet()
	    	    .stream()
	    	    .collect(Collectors.toMap(
	    	        Map.Entry::getKey,
	    	        entry -> new WeeklyHoursDTO(
	    	            entry.getKey(),
	    	            entry.getValue()
	    	        )
	    	    ));

	    return new ReportDTO(total, completed, cancelled, serviceDays, weeklyUsage);
	}
	
	public byte[] generateReportPdf(Long classId) throws DocumentException, IOException {
		
		ReportDTO reportData = generateReport(classId);
		ClassDTO classData = classService.findById(classId);
		List<UserDTO> monitors = userService.getAllMonitorsInClass(classId);
	    // Lógica para criar o PDF com iText usando os dados do relatório.
		
		try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // Adicionar título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Relatório da Turma", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n")); // Espaço
            
            // Adicionar informações da turma
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            document.add(new Paragraph("Informações da Turma", sectionFont));

            PdfPTable classTable = new PdfPTable(2);
            classTable.setWidthPercentage(100);
            classTable.setSpacingBefore(10f);

            addTableCell(classTable, "Nome da Turma:", PdfPCell.ALIGN_LEFT, true);
            addTableCell(classTable, classData.name(), PdfPCell.ALIGN_LEFT, false);

            addTableCell(classTable, "Data de Criação:", PdfPCell.ALIGN_LEFT, true);
            addTableCell(classTable, classData.date().toString(), PdfPCell.ALIGN_LEFT, false);

            addTableCell(classTable, "Professor Responsável:", PdfPCell.ALIGN_LEFT, true);
            addTableCell(classTable, classData.owner().name(), PdfPCell.ALIGN_LEFT, false);

            document.add(classTable);

            document.add(new Paragraph("\n")); // Espaço
            
            // Adicionar monitores
            document.add(new Paragraph("Monitores", sectionFont));
            if (monitors.isEmpty()) {
                document.add(new Paragraph("Sem monitores.", new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC)));
            } else {
                PdfPTable monitorsTable = new PdfPTable(3); // Tabela com 3 colunas: Nome, Email e Status.
                monitorsTable.setWidthPercentage(100);
                monitorsTable.setSpacingBefore(10f);

                addTableCell(monitorsTable, "Nome", PdfPCell.ALIGN_LEFT, true);
                addTableCell(monitorsTable, "Email", PdfPCell.ALIGN_LEFT, true);
                addTableCell(monitorsTable, "Status", PdfPCell.ALIGN_LEFT, true);

                for (UserDTO monitor : monitors) {
                    addTableCell(monitorsTable, monitor.name(), PdfPCell.ALIGN_LEFT, false);
                    addTableCell(monitorsTable, monitor.email(), PdfPCell.ALIGN_LEFT, false);
                    addTableCell(monitorsTable, monitor.status(), PdfPCell.ALIGN_LEFT, false);
                }

                document.add(monitorsTable);
            }

            document.add(new Paragraph("\n")); // Espaço

            // Adicionar resumo
            document.add(new Paragraph("Resumo dos Atendimentos", sectionFont));

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(100);
            summaryTable.setSpacingBefore(10f);

            addTableCell(summaryTable, "Total de Atendimentos:", PdfPCell.ALIGN_LEFT, true);
            addTableCell(summaryTable, String.valueOf(reportData.totalServices()), PdfPCell.ALIGN_RIGHT, false);

            addTableCell(summaryTable, "Atendimentos Concluídos:", PdfPCell.ALIGN_LEFT, true);
            addTableCell(summaryTable, String.valueOf(reportData.completedServices()), PdfPCell.ALIGN_RIGHT, false);

            addTableCell(summaryTable, "Atendimentos Cancelados:", PdfPCell.ALIGN_LEFT, true);
            addTableCell(summaryTable, String.valueOf(reportData.canceledServices()), PdfPCell.ALIGN_RIGHT, false);

            document.add(summaryTable);

            document.add(new Paragraph("\n")); // Espaço

            // Adicionar dias de atendimento
            document.add(new Paragraph("Dias de Atendimento", sectionFont));
            PdfPTable daysTable = new PdfPTable(2);
            daysTable.setWidthPercentage(100);
            daysTable.setSpacingBefore(10f);

            addTableCell(daysTable, "Data", PdfPCell.ALIGN_CENTER, true);
            addTableCell(daysTable, "Total de Horas", PdfPCell.ALIGN_CENTER, true);

            for (ServiceDayDTO day : reportData.servicesDays()) {
                addTableCell(daysTable, day.date().toString(), PdfPCell.ALIGN_LEFT, false);
                addTableCell(daysTable, String.format("%.2f h", day.hours()), PdfPCell.ALIGN_RIGHT, false);
            }

            document.add(daysTable);

            document.add(new Paragraph("\n")); // Espaço

            // Adicionar uso semanal
            document.add(new Paragraph("Horas Semanais por Professor/Monitor", sectionFont));
            for (Map.Entry<String, WeeklyHoursDTO> entry : reportData.weeklyUsage().entrySet()) {
                document.add(new Paragraph(entry.getKey() + ":", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

                com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
                for (Map.Entry<String, Double> weekData : entry.getValue().hoursByWeek().entrySet()) {
                    list.add(new ListItem(weekData.getKey() + ": " + String.format("%.2f h", weekData.getValue())));
                }
                document.add(list);
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar o PDF", e);
        }
    }
	
	private void addTableCell(PdfPTable table, String content, int alignment, boolean isHeader) {
        Font font = isHeader
                ? new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)
                : new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        table.addCell(cell);
    }

}
