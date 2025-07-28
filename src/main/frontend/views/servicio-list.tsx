import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import {
  Button,
  Dialog,
  Grid,
  GridColumn,
  HorizontalLayout,
  Icon,
  Notification,
  ProgressBar,
  TextField,
  VerticalLayout,
} from '@vaadin/react-components';
import { useEffect, useState } from 'react';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { ServicioService } from 'Frontend/generated/endpoints';

type Servicio = {
  id: string;
  nombre: string;
  descripcion: string;
  costo_total: number;
};

export const config: ViewConfig = {
  title: 'Servicios',
  menu: {
    icon: 'vaadin:tools',
    title: 'Servicios',
    order: 3,
  },
};

function ServicioEntryForm({ onServicioCreated }: { onServicioCreated?: () => void }) {
  const dialogOpened = useSignal(false);
  const nombre = useSignal('');
  const descripcion = useSignal('');
  const costoTotal = useSignal('');

  const open = () => dialogOpened.value = true;
  const close = () => dialogOpened.value = false;

  const createServicio = async () => {
    try {
      const nombreVal = nombre.value.trim();
      const descripcionVal = descripcion.value.trim();
      const costoVal = parseInt(costoTotal.value);

      if (!nombreVal || !descripcionVal || isNaN(costoVal) || costoVal <= 0) {
        Notification.show('Todos los campos son obligatorios y el costo debe ser mayor que 0', {
          theme: 'error',
          position: 'top-center'
        });
        return;
      }

      await ServicioService.createServicio(nombreVal, descripcionVal, costoVal);
      nombre.value = '';
      descripcion.value = '';
      costoTotal.value = '';
      dialogOpened.value = false;
      Notification.show('Servicio creado exitosamente', { theme: 'success', position: 'bottom-end' });
      onServicioCreated?.();
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        opened={dialogOpened.value}
        onOpenedChanged={(e) => dialogOpened.value = e.detail.value}
        header={<h2>Registrar Servicio</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createServicio}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout style={{ width: '300px', maxWidth: '100%' }}>
          <TextField
            label="Nombre del Servicio"
            value={nombre.value}
            onValueChanged={(e) => nombre.value = e.detail.value}
            required
          />
          <TextField
            label="Descripción"
            value={descripcion.value}
            onValueChanged={(e) => descripcion.value = e.detail.value}
            required
          />
          <TextField
            label="Costo total (en número)"
            value={costoTotal.value}
            onValueChanged={(e) => costoTotal.value = e.detail.value}
            required
            pattern="[0-9]+"
            errorMessage="Ingrese un valor numérico"
          />
        </VerticalLayout>
      </Dialog>

      <Button theme="primary" onClick={open}>
        <Icon icon="vaadin:plus" slot="prefix" />
        Nuevo Servicio
      </Button>
    </>
  );
}

export default function ServicioListView() {
  const [servicios, setServicios] = useState<Servicio[]>([]);
  const [loading, setLoading] = useState(true);
  const [servicioEditar, setServicioEditar] = useState<Servicio | null>(null);
  const [searchText, setSearchText] = useState('');

  const loadServicios = async () => {
    try {
      setLoading(true);
      const data = await ServicioService.listServicio();
      setServicios(data as Servicio[]);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  };

  const eliminarServicio = async (id: string) => {
    try {
      await ServicioService.deleteServicio(parseInt(id));
      Notification.show('Servicio eliminado correctamente', { theme: 'success', position: 'bottom-end' });
      await loadServicios();
    } catch (error) {
      handleError(error);
    }
  };

  const handleEdit = (servicio: Servicio) => {
    setServicioEditar(servicio);
  };

  const guardarCambios = async () => {
    if (!servicioEditar) return;

    const { id, nombre, descripcion, costo_total } = servicioEditar;
    if (!nombre.trim() || !descripcion.trim() || isNaN(costo_total) || costo_total <= 0) {
      Notification.show('Complete todos los campos y asegúrese que el costo es válido', {
        duration: 4000,
        theme: 'error',
        position: 'top-center'
      });
      return;
    }

    try {
      await ServicioService.updateServicio(parseInt(id), nombre.trim(), descripcion.trim(), costo_total);
      Notification.show('Servicio actualizado correctamente', {
        duration: 4000,
        theme: 'success',
        position: 'bottom-end'
      });
      setServicioEditar(null);
      await loadServicios();
    } catch (error) {
      handleError(error);
    }
  };

  const buscarServicios = async () => {
    try {
      if (searchText.trim() === '') {
        Notification.show('Ingresa un texto para buscar', {
          theme: 'error',
          position: 'top-center'
        });
        return;
      }
      const resultados = await ServicioService.search('nombre', searchText, 3); // 3 = contiene
      setServicios(resultados as Servicio[]);
    } catch (error) {
      handleError(error);
    }
  };

  const ordenarServicios = async (atributo: string, tipo: number) => {
    try {
      const resultados = await ServicioService.order(atributo, tipo); // 1 = ascendente, 2 = descendente
      setServicios(resultados as Servicio[]);
    } catch (error) {
      handleError(error);
    }
  };

  useEffect(() => {
    loadServicios();
  }, []);

  return (
    <main style={{ padding: '24px', background: '#f9f9f9', minHeight: '100vh' }}>
      <h1>Catálogo de Servicios</h1>

      <ServicioEntryForm onServicioCreated={loadServicios} />

      <HorizontalLayout style={{ gap: '12px', margin: '20px 0' }}>
        <TextField
          placeholder="Buscar por nombre"
          value={searchText}
          onValueChanged={(e) => setSearchText(e.detail.value)}
          clearButtonVisible
        />
        <Button theme="primary" onClick={buscarServicios}>
          <Icon icon="vaadin:search" slot="prefix" />
          Buscar
        </Button>
        <Button theme="tertiary" onClick={loadServicios}>
          Limpiar
        </Button>
        <Button onClick={() => ordenarServicios('nombre', 1)}>
          Ordenar A-Z
        </Button>
        <Button onClick={() => ordenarServicios('nombre', 2)}>
          Ordenar Z-A
        </Button>
      </HorizontalLayout>

      {loading ? (
        <ProgressBar indeterminate style={{ width: '200px', marginTop: '20px' }} />
      ) : servicios.length === 0 ? (
        <p style={{ textAlign: 'center', marginTop: '20px' }}>No hay servicios registrados.</p>
      ) : (
        <Grid items={servicios} style={{ marginTop: '20px' }}>
          <GridColumn path="nombre" header="Nombre" />
          <GridColumn path="descripcion" header="Descripción" />
          <GridColumn path="costo_total" header="Costo (Bs)" />
          <GridColumn
            header="Acciones"
            renderer={({ item }) => (
              <HorizontalLayout style={{ gap: '8px' }}>
                <Button theme="tertiary" onClick={() => handleEdit(item)}>
                  <Icon icon="vaadin:edit" />
                </Button>
                <Button theme="error" onClick={() => eliminarServicio(item.id)}>
                  <Icon icon="vaadin:trash" />
                </Button>
              </HorizontalLayout>
            )}
          />
        </Grid>
      )}

      {servicioEditar && (
        <Dialog
          headerTitle="Editar Servicio"
          opened
          onOpenedChanged={({ detail }) => {
            if (!detail.value) setServicioEditar(null);
          }}
          footer={
            <HorizontalLayout style={{ gap: '12px' }}>
              <Button onClick={() => setServicioEditar(null)}>Cancelar</Button>
              <Button theme="primary" onClick={guardarCambios}>Guardar</Button>
            </HorizontalLayout>
          }
        >
          <VerticalLayout style={{ padding: '20px', minWidth: '400px', gap: '12px' }}>
            <TextField
              label="Nombre"
              value={servicioEditar.nombre}
              onValueChanged={(e) =>
                setServicioEditar({ ...servicioEditar, nombre: e.detail.value })
              }
              required
              errorMessage="Este campo es obligatorio"
            />
            <TextField
              label="Descripción"
              value={servicioEditar.descripcion}
              onValueChanged={(e) =>
                setServicioEditar({ ...servicioEditar, descripcion: e.detail.value })
              }
              required
              errorMessage="Este campo es obligatorio"
            />
            <TextField
              label="Costo total"
              value={servicioEditar.costo_total.toString()}
              onValueChanged={(e) =>
                setServicioEditar({ ...servicioEditar, costo_total: parseInt(e.detail.value || '0') })
              }
              required
              pattern="[0-9]+"
              errorMessage="Ingrese un número válido"
            />
          </VerticalLayout>
        </Dialog>
      )}
    </main>
  );
}
