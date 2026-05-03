package com.example.util;

import com.example.model.cliente;
import com.example.model.detalleVenta;
import com.example.model.venta;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneradorPDF {

    // ── Colores de la tienda ──────────────────────────────────────────────
    private static final DeviceRgb COLOR_VERDE        = new DeviceRgb(12, 192, 83);
    private static final DeviceRgb COLOR_VERDE_OSCURO = new DeviceRgb(9, 148, 64);
    private static final DeviceRgb COLOR_GRIS_CLARO   = new DeviceRgb(248, 245, 241);
    private static final DeviceRgb COLOR_GRIS_TEXTO   = new DeviceRgb(35, 31, 32);

    // ── Formato de fecha ──────────────────────────────────────────────────
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    // constructor privado — no se instancia
    private GeneradorPDF() {}

    // ── Método principal ──────────────────────────────────────────────────

    /**
     * Genera la factura en PDF con toda la información de la venta.
     *
     * @param v            objeto venta con los datos generales
     * @param cli          objeto cliente con sus datos
     * @param detalles     lista de productos comprados
     * @param rutaArchivo  ruta donde se guarda el PDF
     */
    public static void generarFactura(venta v, cliente cli,
                                      List<detalleVenta> detalles,
                                      String rutaArchivo) {
        try {
            PdfWriter writer      = new PdfWriter(rutaArchivo);
            PdfDocument pdfDoc    = new PdfDocument(writer);
            Document documento    = new Document(pdfDoc);
            documento.setMargins(40, 40, 40, 40);

            agregarEncabezado(documento);
            agregarSeparador(documento);
            agregarDatosFactura(documento, v, cli);
            agregarSeparador(documento);
            agregarTablaProductos(documento, detalles);
            agregarTotales(documento, v);
            agregarSeparador(documento);
            agregarPieDePagina(documento);

            documento.close();
            System.out.println("Factura generada correctamente en: " + rutaArchivo);

        } catch (FileNotFoundException e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
        }
    }

    // ── Secciones del PDF ─────────────────────────────────────────────────

    private static void agregarEncabezado(Document doc) {

        // nombre de la tienda
        Paragraph nombre = new Paragraph("🌿 Natural & Belleza")
                .setFontSize(22)
                .setBold()
                .setFontColor(COLOR_VERDE_OSCURO)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(4);
        doc.add(nombre);

        // slogan
        Paragraph slogan = new Paragraph("Tu tienda de productos naturales y de belleza")
                .setFontSize(11)
                .setFontColor(COLOR_GRIS_TEXTO)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(4);
        doc.add(slogan);

        // contacto
        Paragraph contacto = new Paragraph(
                "Valledupar, Cesar  |  Tel: 605-123-4567  |  naturalbelleza@email.com")
                .setFontSize(10)
                .setFontColor(COLOR_GRIS_TEXTO)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        doc.add(contacto);
    }

    private static void agregarSeparador(Document doc) {
        Table separador = new Table(UnitValue.createPercentArray(new float[]{1}))
                .useAllAvailableWidth()
                .setMarginBottom(8)
                .setMarginTop(4);

        Cell celda = new Cell()
                .setHeight(2)
                .setBackgroundColor(COLOR_VERDE)
                .setBorder(Border.NO_BORDER);
        separador.addCell(celda);
        doc.add(separador);
    }

    private static void agregarDatosFactura(Document doc, venta v, cliente cli) {

        // tabla de dos columnas
        Table tabla = new Table(UnitValue.createPercentArray(new float[]{1f, 1f}))
                .useAllAvailableWidth()
                .setMarginBottom(10);

        // columna izquierda — datos de la factura
        Cell celdaFactura = new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph("FACTURA DE VENTA")
                        .setBold().setFontSize(11).setFontColor(COLOR_VERDE_OSCURO))
                .add(new Paragraph("No. " + String.format("%05d", v.getIdVenta()))
                        .setBold().setFontSize(10))
                .add(new Paragraph("Fecha: " + v.getFechaHora().format(FORMATO_FECHA))
                        .setFontSize(10))
                .add(new Paragraph("Método de pago: " + v.getMetodoPago())
                        .setFontSize(10));
        tabla.addCell(celdaFactura);

        // columna derecha — datos del cliente
        Cell celdaCliente = new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph("DATOS DEL CLIENTE")
                        .setBold().setFontSize(11).setFontColor(COLOR_VERDE_OSCURO))
                .add(new Paragraph("Nombre: " + cli.getNombre())
                        .setFontSize(10))
                .add(new Paragraph("Cédula: " + cli.getCedula())
                        .setFontSize(10))
                .add(new Paragraph("Teléfono: " + cli.getTelefono())
                        .setFontSize(10));
        tabla.addCell(celdaCliente);

        doc.add(tabla);
    }

    private static void agregarTablaProductos(Document doc, List<detalleVenta> detalles) {

        // título de la sección
        doc.add(new Paragraph("DETALLE DE PRODUCTOS")
                .setBold()
                .setFontSize(11)
                .setFontColor(COLOR_VERDE_OSCURO)
                .setMarginBottom(8));

        // tabla de productos con 5 columnas
        Table tabla = new Table(UnitValue.createPercentArray(new float[]{1f, 3f, 1.2f, 1.5f, 1.5f}))
                .useAllAvailableWidth()
                .setMarginBottom(10);

        // encabezados
        tabla.addHeaderCell(crearCeldaEncabezado("#"));
        tabla.addHeaderCell(crearCeldaEncabezado("Producto"));
        tabla.addHeaderCell(crearCeldaEncabezado("Cantidad"));
        tabla.addHeaderCell(crearCeldaEncabezado("Precio Unitario"));
        tabla.addHeaderCell(crearCeldaEncabezado("Subtotal"));

        // filas de productos
        int contador = 1;
        for (detalleVenta d : detalles) {
            boolean filaAlterna = contador % 2 == 0;

            tabla.addCell(crearCeldaDato(
                String.valueOf(contador), TextAlignment.CENTER, filaAlterna));
            tabla.addCell(crearCeldaDato(
                "Producto #" + d.getIdProducto(), TextAlignment.LEFT, filaAlterna));
            tabla.addCell(crearCeldaDato(
                String.valueOf(d.getCantidad()), TextAlignment.CENTER, filaAlterna));
            tabla.addCell(crearCeldaDato(
                formatearPrecio(d.getPrecioUnitario()), TextAlignment.RIGHT, filaAlterna));
            tabla.addCell(crearCeldaDato(
                formatearPrecio(d.getSubTotal()), TextAlignment.RIGHT, filaAlterna));

            contador++;
        }

        doc.add(tabla);
    }

    private static void agregarTotales(Document doc, venta v) {

        // tabla de totales alineada a la derecha
        Table tabla = new Table(UnitValue.createPercentArray(new float[]{1.5f, 1f}))
                .setWidth(UnitValue.createPercentValue(45))
                .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                .setMarginBottom(20);

        // celda etiqueta
        Cell celdaEtiqueta = new Cell()
                .setBackgroundColor(COLOR_VERDE)
                .setBorder(Border.NO_BORDER)
                .setPadding(8)
                .add(new Paragraph("TOTAL A PAGAR")
                        .setBold()
                        .setFontSize(13)
                        .setFontColor(ColorConstants.WHITE));

        // celda valor
        Cell celdaValor = new Cell()
                .setBackgroundColor(COLOR_VERDE)
                .setBorder(Border.NO_BORDER)
                .setPadding(8)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(new Paragraph(formatearPrecio(v.getTotal()))
                        .setBold()
                        .setFontSize(13)
                        .setFontColor(ColorConstants.WHITE));

        tabla.addCell(celdaEtiqueta);
        tabla.addCell(celdaValor);
        doc.add(tabla);
    }

    private static void agregarPieDePagina(Document doc) {

        doc.add(new Paragraph("¡Gracias por su compra!")
                .setBold()
                .setFontSize(11)
                .setFontColor(COLOR_VERDE_OSCURO)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(5));

        doc.add(new Paragraph(
                "Este documento es comprobante de su compra en Natural & Belleza.\n" +
                "Conserve esta factura para cualquier reclamación.")
                .setFontSize(9)
                .setItalic()
                .setFontColor(COLOR_GRIS_TEXTO)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(4));
    }

    // ── Métodos auxiliares ────────────────────────────────────────────────

    private static Cell crearCeldaEncabezado(String texto) {
        return new Cell()
                .setBackgroundColor(COLOR_VERDE)
                .setBorder(Border.NO_BORDER)
                .setPadding(7)
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(texto)
                        .setBold()
                        .setFontSize(10)
                        .setFontColor(ColorConstants.WHITE));
    }

    private static Cell crearCeldaDato(String texto, TextAlignment alineacion, boolean filaAlterna) {
        Cell celda = new Cell()
                .setPadding(6)
                .setTextAlignment(alineacion)
                .add(new Paragraph(texto).setFontSize(10).setFontColor(COLOR_GRIS_TEXTO));

        if (filaAlterna) {
            celda.setBackgroundColor(COLOR_GRIS_CLARO);
        }

        return celda;
    }

    private static String formatearPrecio(double precio) {
        return String.format("$%,.0f", precio);
    }
}