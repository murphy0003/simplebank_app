package com.murphy.simplebank.service.impl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.murphy.simplebank.dto.EmailDetails;
import com.murphy.simplebank.entity.Transaction;
import com.murphy.simplebank.entity.User;
import com.murphy.simplebank.repository.TransactionRepository;
import com.murphy.simplebank.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private static final String FILE = "C:\\Lab\\MyStatement.pdf";
    /*
    * retrieve list of transactions within a date range given an account number
    * generate a pdf file of transactions
    * send the file via email
    * */

    public List<Transaction> generateStatement(String accountNumber,String startDate,String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start)).filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();
        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() +" "+ user.getLastName()+" "+user.getOtherName();
        Document document = new Document(PageSize.A4, 40, 40, 50, 50); // Margins: L, R, T, B
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();
        log.info("Printout pdf file");

        // Colors
        BaseColor lightGray = new BaseColor(230, 230, 230);
        BaseColor darkGray = new BaseColor(100, 100, 100);
        BaseColor accentColor = new BaseColor(0, 102, 204); // Bank blue

        // --- BANK HEADER ---
        PdfPTable bankHeader = new PdfPTable(2);
        bankHeader.setWidthPercentage(100);

        // Bank Logo (placeholder: replace with Image.getInstance("logo.png"))
        PdfPCell logoCell = new PdfPCell(new Phrase("LOGO", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, accentColor)));
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bankHeader.addCell(logoCell);

        // Bank Info
        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoCell.addElement(new Phrase("Simple Bank", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, accentColor)));
        infoCell.addElement(new Phrase("111, Bo Ko Ao, Yangon", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, darkGray)));
        bankHeader.addCell(infoCell);
        document.add(bankHeader);

        // Add spacing
        document.add(new Paragraph(" ")); // Empty line

        // --- CUSTOMER INFO ---
        PdfPTable customerTable = new PdfPTable(2);
        customerTable.setWidthPercentage(100);
        customerTable.setSpacingBefore(10f);

        // Left Column: Dates
        PdfPCell datesCell = new PdfPCell();
        datesCell.setBorder(Rectangle.NO_BORDER);
        datesCell.addElement(new Phrase("Statement Period:", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        datesCell.addElement(new Phrase("From: " + startDate, new Font(Font.FontFamily.HELVETICA, 10)));
        datesCell.addElement(new Phrase("To: " + endDate, new Font(Font.FontFamily.HELVETICA, 10)));
        customerTable.addCell(datesCell);

        // Right Column: Customer Info
        PdfPCell customerCell = new PdfPCell();
        customerCell.setBorder(Rectangle.NO_BORDER);
        customerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        customerCell.addElement(new Phrase("Customer:", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        customerCell.addElement(new Phrase(customerName, new Font(Font.FontFamily.HELVETICA, 10)));
        customerCell.addElement(new Phrase(user.getAddress(), new Font(Font.FontFamily.HELVETICA, 10)));
        customerTable.addCell(customerCell);
        document.add(customerTable);

        // Title
        Paragraph title = new Paragraph("STATEMENT OF ACCOUNT", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, accentColor));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(15f);
        document.add(title);

        // --- TRANSACTIONS TABLE ---
        PdfPTable transactionsTable = new PdfPTable(4);
        transactionsTable.setWidthPercentage(100);
        transactionsTable.setWidths(new float[]{2, 3, 2, 2}); // Column widths

        // Table Headers (with background)
        String[] headers = {"DATE", "TRANSACTION TYPE", "AMOUNT (MMK)", "STATUS"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE)));
            headerCell.setBackgroundColor(accentColor);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5f);
            transactionsTable.addCell(headerCell);
        }

        // Transaction Rows (alternating colors)
        boolean isOddRow = true;
        for (Transaction transaction : transactionList) {
            PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getCreatedAt().toString(), new Font(Font.FontFamily.HELVETICA, 9)));
            PdfPCell typeCell = new PdfPCell(new Phrase(transaction.getTransactionType(), new Font(Font.FontFamily.HELVETICA, 9)));
            PdfPCell amountCell = new PdfPCell(new Phrase(transaction.getAmount().toString(), new Font(Font.FontFamily.HELVETICA, 9)));
            PdfPCell statusCell = new PdfPCell(new Phrase(transaction.getStatus(), new Font(Font.FontFamily.HELVETICA, 9)));

            // Style cells
            for (PdfPCell cell : new PdfPCell[]{dateCell, typeCell, amountCell, statusCell}) {
                cell.setBackgroundColor(isOddRow ? BaseColor.WHITE : lightGray);
                cell.setPadding(5f);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            }
            amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT); // Align amounts to right

            transactionsTable.addCell(dateCell);
            transactionsTable.addCell(typeCell);
            transactionsTable.addCell(amountCell);
            transactionsTable.addCell(statusCell);

            isOddRow = !isOddRow;
        }
        document.add(transactionsTable);

        // --- FOOTER ---
        Paragraph footer = new Paragraph(
                "For inquiries, contact: support@simplebank.com | Phone: +95 123 456 789",
                new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, darkGray)
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20f);
        document.add(footer);

        document.close();
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your requested account statement attached!")
                .attachment(FILE)
                .build();
        emailService.sendEmailAttachment(emailDetails);
        return transactionList;
    }

}
