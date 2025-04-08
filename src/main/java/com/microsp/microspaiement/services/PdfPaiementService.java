package com.microsp.microspaiement.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.microsp.microspaiement.entities.PaiementSurPlace;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfPaiementService {

    public ByteArrayInputStream export(List<PaiementSurPlace> paiements) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // 6 colonnes : ID, Agence, Date RDV, Créneau, Montant, Date Paiement
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 3.5f, 3, 2.5f, 2.5f, 3});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            table.addCell(new PdfPCell(new Phrase("ID", headFont)));
            table.addCell(new PdfPCell(new Phrase("Agence", headFont)));
            table.addCell(new PdfPCell(new Phrase("Date RDV", headFont)));
            table.addCell(new PdfPCell(new Phrase("Créneau", headFont)));
            table.addCell(new PdfPCell(new Phrase("Montant", headFont)));
            table.addCell(new PdfPCell(new Phrase("Date Paiement", headFont)));

            for (PaiementSurPlace p : paiements) {
                table.addCell(String.valueOf(p.getId_p()));
                table.addCell(p.getAgence());
                table.addCell(p.getDate_rdv());
                table.addCell(p.getCreneau());
                table.addCell(String.format("%.2f", p.getMontant()));
                table.addCell(String.valueOf(p.getDate_paiement()));
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Liste des paiements sur place"));
            document.add(Chunk.NEWLINE);
            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
