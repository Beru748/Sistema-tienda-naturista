package com.example.util;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

import com.example.model.cliente;
import com.example.model.detalleVenta;
import com.example.model.venta;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneradorPDF {

    // ── Colores de la tienda ──────────────────────────────────────────────
    private static final BaseColor COLOR_VERDE        = new BaseColor(12, 192, 83);
    private static final BaseColor COLOR_VERDE_OSCURO = new BaseColor(9, 148, 64);
    private static final BaseColor COLOR_GRIS_CLARO   = new BaseColor(248, 245, 241);
    private static final BaseColor COLOR_GRIS_TEXTO   = new BaseColor(35, 31, 32);
    private static final BaseColor COLOR_BLANCO       = BaseColor.WHITE;

    // ── Fuentes ───────────────────────────────────────────────────────────
    private static final Font FUENTE_TITULO     = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD,   COLOR_VERDE_OSCURO);
    private static final Font FUENTE_SUBTITULO  = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, COLOR_GRIS_TEXTO);
    private static final Font FUENTE_SECCION    = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,   COLOR_VERDE_OSCURO);
    private static final Font FUENTE_NORMAL     = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_GRIS_TEXTO);
    private static final Font FUENTE_NEGRITA    = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_GRIS_TEXTO);
    private static final Font FUENTE_ENCABEZADO = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   COLOR_BLANCO);
    private static final Font FUENTE_TOTAL      = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,   COLOR_BLANCO);
    private static final Font FUENTE_PIE        = new Font(Font.FontFamily.HELVETICA,  9, Font.ITALIC, COLOR_GRIS_TEXTO);

    // ── Formato de fecha ──────────────────────────────────────────────────
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    // constructor privado — no se instancia
    private GeneradorPDF() {}

    //metodo principal para generar la factura
    public static void generarFactura(venta v, cliente cli, List<detalleVenta> detalles,String rutaArchivo) {
        try {
            Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter writer = PdfWriter.getInstance(documento,
                    new FileOutputStream(rutaArchivo));

            documento.open();

            agregarEncabezado(documento);
            agregarLineaSeparadora(documento, writer);
            agregarDatosFactura(documento, v, cli);
            agregarLineaSeparadora(documento, writer);
            agregarTablaProductos(documento, detalles);
            agregarTotales(documento, v);
            agregarPieDePagina(documento, writer);

            documento.close();
            System.out.println("Factura generada correctamente en: " + rutaArchivo);

        } catch (Exception e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
        }
    }
    
}