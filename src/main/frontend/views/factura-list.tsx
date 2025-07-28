import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import {
  Button,
  ComboBox,
  VerticalLayout,
  Notification,
} from '@vaadin/react-components';
import { FacturaService, ServicioService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useEffect } from 'react';

export const config: ViewConfig = {
  title: 'Facturar Servicio',
  menu: {
    icon: 'vaadin:money',
    order: 2,
    title: 'Facturar Servicio',
  },
};

type VehiculoOption = {
  label: string;
  value: string;
};

type ServicioOption = {
  label: string;
  value: string; // servicioId
  costo: number;
};

export default function FacturaServicioView() {
  const vehiculoId = useSignal('');
  const servicioId = useSignal('');
  const total = useSignal<number>(0);

  const listaVehiculos = useSignal<VehiculoOption[]>([]);
  const listServicio = useSignal<ServicioOption[]>([]);

  useEffect(() => {
    FacturaService.listaVehiculoCombo().then((data) => {
      listaVehiculos.value = data;
    });

    ServicioService.listServicio().then((data) => {
      listServicio.value = data.map((s) => ({
        label: s.nombre,
        value: s.id.toString(),
        costo: parseFloat(s.costo_total),
      }));
    });
  }, []);

  const generarFactura = async () => {
    if (!vehiculoId.value || !servicioId.value) {
      Notification.show('Por favor seleccione vehículo y servicio', {
        theme: 'error',
      });
      return;
    }

    try {
      const vehiculoIDParsed = parseInt(vehiculoId.value);
      const servicioIDParsed = parseInt(servicioId.value);
      const personaId = await FacturaService.obtenerPersonaIdPorVehiculo(vehiculoIDParsed);

      const base64PDF = await FacturaService.generarFacturaPDF(
        personaId,
        vehiculoIDParsed,
        servicioIDParsed
      );

      const link = document.createElement('a');
      link.href = `data:application/pdf;base64,${base64PDF}`;
      link.download = 'factura_servicio.pdf';
      link.click();

      Notification.show('Factura generada correctamente', { theme: 'success' });

      vehiculoId.value = '';
      servicioId.value = '';
      total.value = 0;
    } catch (error) {
      console.error(error);
      Notification.show('Error al generar la factura', { theme: 'error' });
    }
  };

  return (
    <VerticalLayout className="gap-m p-m" style={{ maxWidth: '400px' }}>
      <h3>Facturar Servicio</h3>

      <ComboBox
        label="Vehículo"
        items={listaVehiculos.value}
        itemLabelPath="label"
        itemValuePath="value"
        value={vehiculoId.value}
        onValueChanged={(e) => (vehiculoId.value = e.detail.value)}
        placeholder="Seleccione un vehículo"
      />

      <ComboBox
        label="Servicio"
        items={listServicio.value}
        itemLabelPath="label"
        itemValuePath="value"
        value={servicioId.value}
        onValueChanged={(e) => {
          servicioId.value = e.detail.value;
          const selected = listServicio.value.find((s) => s.value === e.detail.value);
          total.value = selected?.costo ?? 0;
        }}
        placeholder="Seleccione el servicio"
      />

      <div><strong>Total:</strong> ${total.value.toFixed(2)}</div>

      <Button theme="primary" onClick={generarFactura}>
        Generar Factura
      </Button>
    </VerticalLayout>
  );
}
