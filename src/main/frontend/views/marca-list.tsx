import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { 
  Button, 
  DatePicker, 
  Dialog, 
  Grid, 
  GridColumn, 
  GridItemModel, 
  TextField, 
  VerticalLayout,
  HorizontalLayout,
  Card,
  Icon,
  ProgressBar,
  ComboBox
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';

import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import BrandCard from 'Frontend/components/BrandCard';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import { MarcaService } from 'Frontend/generated/endpoints';
import { useEffect, useState, useRef } from 'react';

// Tipo para marca basado en la respuesta del backend
type Marca = {
  id: string;
  modelo: string;
  fecha: string;
};

export const config: ViewConfig = {
  title: 'Marcas',
  menu: {
    icon: 'vaadin:factory',
    order: 2,
    title: 'Marcas',
  },
};

type MarcaEntryFormProps = {
  onMarcaCreated?: () => void;
};

// Componente de búsqueda y filtros
function SearchAndFilters({ onSearch, onFilter }: {
  onSearch: (query: string) => void;
  onFilter: (filters: any) => void;
}) {
  const searchQuery = useSignal('');
  const yearFilter = useSignal('all');

  const handleSearch = () => {
    onSearch(searchQuery.value);
  };

  const handleFilter = () => {
    onFilter({
      year: yearFilter.value
    });
  };

  return (
    <Card style={{
      borderRadius: '12px',
      padding: '20px',
      marginBottom: '20px',
      border: '1px solid #e0e0e0',
      background: 'white'
    }}>
      <VerticalLayout style={{ gap: '16px' }}>
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '12px',
          marginBottom: '16px'
        }}>
          <Icon 
            icon="vaadin:search" 
            style={{ 
              fontSize: '20px', 
              color: '#4CAF50',
              background: '#E8F5E8',
              padding: '8px',
              borderRadius: '8px'
            }} 
          />
          <h3 style={{ margin: 0, fontSize: '18px', fontWeight: '600', color: '#333' }}>
            Búsqueda y Filtros
          </h3>
        </div>

        <HorizontalLayout style={{ gap: '16px', flexWrap: 'wrap' }}>
          <TextField
            placeholder="Buscar por nombre de marca..."
            value={searchQuery.value}
            onValueChanged={(e) => searchQuery.value = e.detail.value}
            style={{ minWidth: '300px' }}
          />
          
          <ComboBox
            label="Año de creación"
            items={[
              { label: 'Todos los años', value: 'all' },
              { label: '2020-2024', value: 'recent' },
              { label: '2010-2019', value: 'modern' },
              { label: '2000-2009', value: 'classic' },
              { label: 'Antes de 2000', value: 'vintage' }
            ]}
            value={yearFilter.value}
            onValueChanged={(e) => yearFilter.value = e.detail.value}
          />
        </HorizontalLayout>

        <HorizontalLayout style={{ gap: '12px', justifyContent: 'flex-end' }}>
          <Button 
            theme="tertiary"
            onClick={() => {
              searchQuery.value = '';
              yearFilter.value = 'all';
              onSearch('');
              onFilter({ year: 'all' });
            }}
          >
            <Icon icon="vaadin:refresh" slot="prefix" />
            Limpiar
          </Button>
          
          <Button 
            theme="primary"
            onClick={() => {
              handleSearch();
              handleFilter();
            }}
          >
            <Icon icon="vaadin:search" slot="prefix" />
            Buscar
          </Button>
        </HorizontalLayout>
      </VerticalLayout>
    </Card>
  );
}

// Marca CREATE
function MarcaEntryForm(props: MarcaEntryFormProps) {
  const dialogOpened = useSignal(false);

  const open = () => dialogOpened.value = true;
  const close = () => dialogOpened.value = false;

  const modelo = useSignal('');
  const anio = useSignal('');

  const createMarca = async () => {
    try {
      const modeloVal = modelo.value.trim();
      const anioVal = anio.value.trim();

      if (modeloVal && anioVal) {
        // Convertir la fecha al formato esperado por el backend
        const fecha = new Date(anioVal);
        await MarcaService.createMarca(modeloVal, fecha.toISOString());
        props.onMarcaCreated?.();
        modelo.value = '';
        anio.value = '';
        dialogOpened.value = false;
        Notification.show('Marca creada exitosamente', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('¡Tiene que llenar todos los apartados!', { duration: 5000, position: 'top-center', theme: 'error' });
      }
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Marca"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => dialogOpened.value = event.detail.value}
        header={<h2 className="draggable" style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold', padding: 'var(--lumo-space-m) 0' }}>Registrar Marca</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createMarca}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}>
          <VerticalLayout style={{ alignItems: 'stretch' }}>
            <TextField
              label="Modelo"
              placeholder='Ingrese el modelo de la Marca'
              aria-label='Ingrese el modelo de la Marca'
              value={modelo.value}
              onValueChanged={(evt) => modelo.value = evt.detail.value.trim()}
              errorMessage="Este campo es obligatorio"
              invalid={!modelo.value.trim()}
            />
            <DatePicker
              label="Año de creación"
              placeholder="Seleccione un año"
              aria-label="Seleccione un año"
              value={anio.value}
              onValueChanged={(evt) => anio.value = evt.detail.value}
              errorMessage="Este campo es obligatorio"
              invalid={!anio.value.trim()}
            />
          </VerticalLayout>
        </VerticalLayout>
      </Dialog>
      
      <Button theme="primary" onClick={open}>
        <Icon icon="vaadin:plus" slot="prefix" />
        Nueva Marca
      </Button>
    </>
  );
}

//ELIMINAR MARCA
const eliminarMarca = async (id: number) => {
  try {
    await MarcaService.deleteMarca(id);
    Notification.show('Marca eliminada exitosamente', {
      duration: 4000,
      position: 'bottom-end',
      theme: 'success',
    });
    loadBrands(); // Recarga la lista
  } catch (error) {
    console.error('Error al eliminar marca:', error);
    handleError(error);
  }
};

//LISTA DE MARCAS
export default function MarcaListView() {
  const [brands, setBrands] = useState<Marca[]>([]);
  const [filteredBrands, setFilteredBrands] = useState<Marca[]>([]);
  const [marcaEditar, setMarcaEditar] = useState<Marca | null>(null);
  const [viewMode, setViewMode] = useState<'cards' | 'grid'>('cards');
  const [loading, setLoading] = useState(true);

  const searchQueryRef = useRef('');
  const filterRef = useRef<{ year: string }>({ year: 'all' });

  const loadBrands = async (): Promise<Marca[]> => {
    try {
      setLoading(true);
      const data = await MarcaService.listMarca();
      const brandsList = data || [];
      setBrands(brandsList as Marca[]);
      return brandsList as Marca[];
    } catch (error) {
      console.error('Error loading brands:', error);
      handleError(error);
      return [];
    } finally {
      setLoading(false);
    }
  };

  const recargarYAplicarFiltros = async () => {
    const updatedBrands = await loadBrands();

    const query = searchQueryRef.current;
    const filters = filterRef.current;

    let filtered = updatedBrands;

    if (query.trim()) {
      filtered = filtered.filter(brand =>
        brand.modelo.toLowerCase().includes(query.toLowerCase()) ||
        brand.anio?.includes(query)
      );
    }

    if (filters.year !== 'all') {
      filtered = filtered.filter(brand => {
        if (!brand.fecha) return false;
        const yearStr = brand.fecha.split('-')[0] || brand.fecha.split('/')[2] || brand.fecha;
        const year = parseInt(yearStr);
        if (isNaN(year)) return false;

        switch (filters.year) {
          case 'recent': return year >= 2020;
          case 'modern': return year >= 2010 && year < 2020;
          case 'classic': return year >= 2000 && year < 2010;
          case 'vintage': return year < 2000;
          default: return true;
        }
      });
    }

    setFilteredBrands(filtered);
  };

  const handleSearch = (query: string) => {
    searchQueryRef.current = query;
    recargarYAplicarFiltros();
  };

  const handleFilter = (filters: any) => {
    filterRef.current = filters;
    recargarYAplicarFiltros();
  };

  const eliminarMarca = async (id: number) => {
    try {
      await MarcaService.deleteMarca(id);
      Notification.show('Marca eliminada exitosamente', {
        duration: 4000,
        position: 'bottom-end',
        theme: 'success',
      });
      await recargarYAplicarFiltros();
    } catch (error) {
      console.error('Error al eliminar marca:', error);
      handleError(error);
    }
  };

  const handleDelete = async (id: string) => {
    await eliminarMarca(parseInt(id));
  };

  const handleEdit = (brand: Marca) => {
    setMarcaEditar(brand);
  };

  useEffect(() => {
    recargarYAplicarFiltros();
  }, []);

  return (
    <main style={{ padding: '24px', background: '#f5f5f5', minHeight: '100vh' }}>
      <h1>Catálogo de Marcas</h1>
      <MarcaEntryForm onMarcaCreated={recargarYAplicarFiltros} />

      {/* Aquí puedes agregar tu buscador y filtros si los tienes */}
      {/* Y pasar handleSearch, handleFilter como props si están en otro componente */}

      {loading ? (
        <ProgressBar indeterminate style={{ width: '200px', margin: 'auto' }} />
      ) : filteredBrands.length === 0 ? (
        <p style={{ textAlign: 'center' }}>No hay marcas registradas.</p>
      ) : viewMode === 'cards' ? (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px' }}>
          {filteredBrands.map(brand => (
            <BrandCard
              key={brand.id}
              brand={brand}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          ))}
        </div>
      ) : (
        <Card>
          <Grid items={filteredBrands}>
            <GridColumn path="modelo" header="Marca" />
            <GridColumn path="anio" header="Año" />
            <GridColumn header="Acciones" autoWidth>
              {({ item }: { item: Marca }) => (
                <HorizontalLayout style={{ gap: '8px' }}>
                  <Button theme="tertiary" onClick={() => handleEdit(item)}>
                    <Icon icon="vaadin:edit" slot="prefix" />
                    Editar
                  </Button>
                  <Button theme="error" onClick={() => handleDelete(item.id)}>
                    <Icon icon="vaadin:trash" slot="prefix" />
                    Eliminar
                  </Button>
                </HorizontalLayout>
              )}
            </GridColumn>
          </Grid>
        </Card>
      )}

      {/* Dialog de edición */}
      {marcaEditar && (
        <Dialog
          headerTitle="Editar Marca"
          opened={true}
          onOpenedChanged={({ detail }) => {
            if (!detail.value) setMarcaEditar(null);
          }}
          footer={
            <HorizontalLayout style={{ gap: '12px' }}>
              <Button onClick={() => setMarcaEditar(null)}>Cancelar</Button>
              <Button
                theme="primary"
                onClick={async () => {
                  try {
                    if (marcaEditar.modelo.trim() && marcaEditar.fecha.trim()) {
                      const id = parseInt(marcaEditar.id);
                      const fecha = new Date(marcaEditar.fecha);
                      await MarcaService.updateMarca(id, marcaEditar.modelo, fecha.toISOString());
                      Notification.show('Marca actualizada correctamente', {
                        duration: 4000,
                        position: 'bottom-end',
                        theme: 'success'
                      });
                      setMarcaEditar(null);
                      await recargarYAplicarFiltros();
                    } else {
                      Notification.show('Complete todos los campos', {
                        duration: 4000,
                        position: 'top-center',
                        theme: 'error'
                      });
                    }
                  } catch (error) {
                    console.error(error);
                    handleError(error);
                  }
                }}
              >
                Guardar
              </Button>
            </HorizontalLayout>
          }
        >
          <VerticalLayout style={{ padding: '20px', minWidth: '400px', gap: '12px' }}>
            <TextField
              label="Modelo"
              value={marcaEditar.modelo}
              onValueChanged={(e) =>
                setMarcaEditar({ ...marcaEditar, modelo: e.detail.value })
              }
              required
              errorMessage="Este campo es obligatorio"
            />
            <DatePicker
              label="Fecha de creación"
              value={marcaEditar.fecha}
              onValueChanged={(e) =>
                setMarcaEditar({ ...marcaEditar, fecha: e.detail.value })
              }
              required
              errorMessage="Este campo es obligatorio"
            />
          </VerticalLayout>
        </Dialog>
      )}
    </main>
  );
}
