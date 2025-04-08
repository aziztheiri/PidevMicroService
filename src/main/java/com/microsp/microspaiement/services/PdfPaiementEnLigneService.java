package com.microsp.microspaiement.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.microsp.microspaiement.entities.PaiementEnLigne;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfPaiementEnLigneService {

    public ByteArrayInputStream export(List<PaiementEnLigne> paiements) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(4); // Ajuste selon les colonnes nécessaires
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 3, 3, 3});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            table.addCell(new PdfPCell(new Phrase("ID", headFont)));
            table.addCell(new PdfPCell(new Phrase("Nom", headFont)));
            table.addCell(new PdfPCell(new Phrase("Montant", headFont)));
            table.addCell(new PdfPCell(new Phrase("Date", headFont)));

            for (PaiementEnLigne p : paiements) {
                table.addCell(String.valueOf(p.getId_p()));
                table.addCell(p.getNumeroCarte());
                table.addCell(p.getExpiration());
                table.addCell(p.getCvv());// adapte ces champs
                table.addCell(String.valueOf(p.getMontant()));
                table.addCell(String.valueOf(p.getDate_paiement()));
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Liste des paiements en ligne"));
            document.add(Chunk.NEWLINE);
            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
