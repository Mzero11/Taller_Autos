package trabajofinal.app.base.domain.controller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import trabajofinal.app.base.domain.controller.datastruct.list.LinkedList;
import trabajofinal.app.base.domain.controller.PagoController;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoFactura;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoMarca;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoServicio;
import trabajofinal.app.base.domain.controller.dao.dao_models.DaoVehiculo;
import trabajofinal.app.base.models.Factura;
import trabajofinal.app.base.models.Marca;
import trabajofinal.app.base.models.Servicio;
import trabajofinal.app.base.models.Vehiculo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Endpoint;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Endpoint
@BrowserCallable
@AnonymousAllowed
public class FacturaService {

    private DaoFactura db = new DaoFactura();

    public void generarFactura(Integer personaId, Integer vehiculoId, Integer total) throws Exception {
        db.getObj().setPersona_id(personaId);
        db.getObj().setVehiculo_id(vehiculoId);
        db.getObj().setFecha(new Date());
        db.getObj().setTotal(total);
        db.getObj().setPagado(false); // por defecto no pagado

        if (!db.save()) {
            throw new Exception("No se pudo generar la factura");
        }
    }

    public boolean procesarPago(float total, String moneda) {
        PagoController pago = new PagoController();
        try {
            HashMap<String, String> resultado = pago.request(total, moneda);
            if ("true".equals(resultado.get("estado"))) {
                String checkoutId = resultado.get("id");
                HashMap<String, String> resultadoPago = pago.requestPay(checkoutId);
                return "true".equals(resultadoPago.get("estado"));
            } else {
                System.out.println("Error en request: " + resultado.get("error"));
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<HashMap> listaVehiculoCombo() {
        List<HashMap> lista = new ArrayList<>();
        DaoVehiculo dv = new DaoVehiculo();
        if (!dv.listAll().isEmpty()) {
            Vehiculo[] arreglo = dv.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("value", arreglo[i].getId().toString()); // ✅ corregido
                aux.put("label", arreglo[i].getPlaca());
                lista.add(aux);
            }
        }
        return lista;
    }


    public Integer obtenerPersonaIdPorVehiculo(Integer vehiculoId) throws Exception {
    DaoVehiculo dv = new DaoVehiculo();
    int pos = dv.getPositionById(vehiculoId);
    Vehiculo vehiculo = dv.listAll().get(pos);
    return vehiculo.getPersona_id();
}

public String generarFacturaPDF(Integer personaId, Integer vehiculoId, Integer servicioId) throws Exception {
    DaoVehiculo daoVehiculo = new DaoVehiculo();
    Vehiculo vehiculo = daoVehiculo.listAll().get(daoVehiculo.getPositionById(vehiculoId));

    DaoServicio daoServicio = new DaoServicio();
    Servicio servicio = daoServicio.listAll().get(daoServicio.getPositionById(servicioId));

    DaoFactura daoFactura = new DaoFactura();
    Factura factura = new Factura();
    factura.setPersona_id(personaId);
    factura.setVehiculo_id(vehiculoId);
    factura.setFecha(new Date());
    factura.setTotal(servicio.getCosto_total());
    factura.setPagado(false);

    daoFactura.setObj(factura);
    if (!daoFactura.save()) {
        throw new Exception("No se pudo generar la factura");
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Document document = new Document(PageSize.A4, 50, 50, 50, 50);
    PdfWriter.getInstance(document, baos);
    document.open();

    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
    Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

    // Encabezado
    document.add(new Paragraph("AUTO-SERVICE TECH S.R.L.", titleFont));
    document.add(new Paragraph("RUC: 20481234567 - Av. Los Mecánicos 123, Lima", normalFont));
    document.add(Chunk.NEWLINE);

    // Info cliente
    PdfPTable infoTable = new PdfPTable(2);
    infoTable.setWidthPercentage(100);
    infoTable.setWidths(new int[]{1, 2});
    infoTable.addCell(getCell("N° Factura:", boldFont));
    infoTable.addCell(getCell("000" + factura.getId(), normalFont));
    infoTable.addCell(getCell("Fecha:", boldFont));
    infoTable.addCell(getCell(factura.getFecha().toString(), normalFont));
    infoTable.addCell(getCell("Cliente ID:", boldFont));
    infoTable.addCell(getCell(String.valueOf(personaId), normalFont));
    infoTable.addCell(getCell("Vehículo (Placa):", boldFont));
    infoTable.addCell(getCell(vehiculo.getPlaca(), normalFont));
    document.add(infoTable);

    document.add(new LineSeparator());

    // Tabla de servicios
    PdfPTable table = new PdfPTable(3);
    table.setWidthPercentage(100);
    table.setWidths(new float[]{5, 2, 2});

    table.addCell(getHeaderCell("Descripción del Servicio"));
    table.addCell(getHeaderCell("Cantidad"));
    table.addCell(getHeaderCell("Subtotal"));

    table.addCell(getCell(servicio.getNombre(), normalFont));
    table.addCell(getCell("1", normalFont));
table.addCell(getCell(String.format("$ %.2f", servicio.getCosto_total().floatValue()), normalFont));

    table.addCell(new PdfPCell(new Phrase("")));
    table.addCell(getCell("Total:", boldFont));
table.addCell(getCell(String.format("$ %.2f", servicio.getCosto_total().floatValue()), boldFont));

    document.add(table);

    document.add(Chunk.NEWLINE);
    document.add(new Paragraph("Gracias por confiar en nosotros.", normalFont));
    document.close();

    byte[] pdfBytes = baos.toByteArray();
    return Base64.getEncoder().encodeToString(pdfBytes);
}

// Funciones auxiliares
private PdfPCell getCell(String text, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setBorder(Rectangle.NO_BORDER);
    return cell;
}

private PdfPCell getHeaderCell(String text) {
    PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setBackgroundColor(Color.LIGHT_GRAY);
    return cell;
}


public List<HashMap<String, String>> listaServiciosCombo() {
    List<HashMap<String, String>> lista = new ArrayList<>();
    DaoServicio ds = new DaoServicio(); // Asegúrate de tener este DAO
    if (!ds.listAll().isEmpty()) {
        trabajofinal.app.base.models.Servicio[] servicios = ds.listAll().toArray();
        for (trabajofinal.app.base.models.Servicio s : servicios) {
            HashMap<String, String> map = new HashMap<>();
            map.put("value", s.getId().toString());
            map.put("label", s.getNombre() + " ($" + s.getCosto_total() + ")");
            lista.add(map);
        }
    }
    return lista;
}


}
