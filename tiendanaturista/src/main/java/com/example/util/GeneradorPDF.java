package com.example.util;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

import com.example.model.cliente;
import com.example.model.detalleVenta;
import com.example.model.venta;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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

    private static void agregarEncabezado(Document doc) throws DocumentException {

        // nombre de la tienda
        Paragraph nombre = new Paragraph("🌿 Natural & Belleza", FUENTE_TITULO);
        nombre.setAlignment(Element.ALIGN_CENTER);
        nombre.setSpacingAfter(4);
        doc.add(nombre);

        // slogan
        Paragraph slogan = new Paragraph("Tu tienda de productos naturales y de belleza", FUENTE_SUBTITULO);
        slogan.setAlignment(Element.ALIGN_CENTER);
        slogan.setSpacingAfter(4);
        doc.add(slogan);

        // info de contacto
        Paragraph contacto = new Paragraph("Valledupar, Cesar  |  Tel: 605-123-4567  |  naturalbelleza@email.com", FUENTE_SUBTITULO);
        contacto.setAlignment(Element.ALIGN_CENTER);
        contacto.setSpacingAfter(10);
        doc.add(contacto);
    }

    private static void agregarLineaSeparadora(Document doc, PdfWriter writer)
            throws DocumentException {
        PdfContentByte cb = writer.getDirectContent();
        cb.setColorStroke(COLOR_VERDE);
        cb.setLineWidth(1.5f);
        cb.moveTo(doc.leftMargin(), writer.getVerticalPosition(false));
        cb.lineTo(doc.right(), writer.getVerticalPosition(false));
        cb.stroke();
        doc.add(new Paragraph(" "));
    }

    private static void agregarDatosFactura(Document doc, venta v, cliente cli)
            throws DocumentException {

        // tabla de dos columnas — datos de factura y datos del cliente
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1f, 1f});
        tabla.setSpacingAfter(10);

        // columna izquierda — datos de la factura
        PdfPCell celdaFactura = new PdfPCell();
        celdaFactura.setBorder(Rectangle.NO_BORDER);
        celdaFactura.addElement(new Phrase("FACTURA DE VENTA", FUENTE_SECCION));
        celdaFactura.addElement(new Phrase("No. " + String.format("%05d", v.getIdVenta()), FUENTE_NEGRITA));
        celdaFactura.addElement(new Phrase("Fecha: " + v.getFechaHora().format(FORMATO_FECHA), FUENTE_NORMAL));
        celdaFactura.addElement(new Phrase("Método de pago: " + v.getMetodoPago(), FUENTE_NORMAL));
        tabla.addCell(celdaFactura);

        // columna derecha — datos del cliente
        PdfPCell celdaCliente = new PdfPCell();
        celdaCliente.setBorder(Rectangle.NO_BORDER);
        celdaCliente.addElement(new Phrase("DATOS DEL CLIENTE", FUENTE_SECCION));
        celdaCliente.addElement(new Phrase("Nombre: " + cli.getNombre(), FUENTE_NORMAL));
        celdaCliente.addElement(new Phrase("Cédula: " + cli.getCedula(), FUENTE_NORMAL));
        celdaCliente.addElement(new Phrase("Teléfono: " + cli.getTelefono(), FUENTE_NORMAL));
        tabla.addCell(celdaCliente);

        doc.add(tabla);
    }

    private static void agregarTablaProductos(Document doc, List<detalleVenta> detalles)
            throws DocumentException {

        // título de la sección
        Paragraph titulo = new Paragraph("DETALLE DE PRODUCTOS", FUENTE_SECCION);
        titulo.setSpacingBefore(5);
        titulo.setSpacingAfter(8);
        doc.add(titulo);

        // tabla de productos
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1f, 3f, 1.2f, 1.5f, 1.5f});
        tabla.setSpacingAfter(10);

        // encabezados de la tabla
        tabla.addCell(crearCeldaEncabezado("#"));
        tabla.addCell(crearCeldaEncabezado("Producto"));
        tabla.addCell(crearCeldaEncabezado("Cantidad"));
        tabla.addCell(crearCeldaEncabezado("Precio Unitario"));
        tabla.addCell(crearCeldaEncabezado("Subtotal"));

        // filas de productos
        int contador = 1;
        for (detalleVenta d : detalles) {
            boolean filaAlterna = contador % 2 == 0;

            tabla.addCell(crearCeldaDato(
                String.valueOf(contador), Element.ALIGN_CENTER, filaAlterna));
            tabla.addCell(crearCeldaDato(
                "Producto #" + d.getIdProducto(), Element.ALIGN_LEFT, filaAlterna));
            tabla.addCell(crearCeldaDato(
                String.valueOf(d.getCantidad()), Element.ALIGN_CENTER, filaAlterna));
            tabla.addCell(crearCeldaDato(
                formatearPrecio(d.getPrecioUnitario()), Element.ALIGN_RIGHT, filaAlterna));
            tabla.addCell(crearCeldaDato(
                formatearPrecio(d.getSubTotal()), Element.ALIGN_RIGHT, filaAlterna));

            contador++;
        }

        doc.add(tabla);
    }

    private static void agregarTotales(Document doc, venta v)
            throws DocumentException {

        // tabla de totales alineada a la derecha
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(45);
        tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.setWidths(new float[]{1.5f, 1f});
        tabla.setSpacingAfter(20);

        // fila del total
        PdfPCell celdaEtiqueta = new PdfPCell(new Phrase("TOTAL A PAGAR", FUENTE_TOTAL));
        celdaEtiqueta.setBackgroundColor(COLOR_VERDE);
        celdaEtiqueta.setPadding(8);
        celdaEtiqueta.setBorder(Rectangle.NO_BORDER);
        celdaEtiqueta.setHorizontalAlignment(Element.ALIGN_LEFT);
        tabla.addCell(celdaEtiqueta);

        PdfPCell celdaValor = new PdfPCell(new Phrase(formatearPrecio(v.getTotal()), FUENTE_TOTAL));
        celdaValor.setBackgroundColor(COLOR_VERDE);
        celdaValor.setPadding(8);
        celdaValor.setBorder(Rectangle.NO_BORDER);
        celdaValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.addCell(celdaValor);

        doc.add(tabla);
    }

    
}