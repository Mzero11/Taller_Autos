import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { 
  Button, 
  ComboBox, 
  DatePicker, 
  Dialog, 
  Grid, 
  GridColumn, 
  GridItemModel, 
  NumberField, 
  TextField, 
  VerticalLayout,
  HorizontalLayout,
  Card,
  Icon,
  ProgressBar,
  Tabs,
  Tab
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { VehiculoService, TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import VehicleCard from 'Frontend/components/VehicleCard';
//import VehiculoEditForm from 'Frontend/views/VehiculoEditForm';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import { useCallback, useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Vehículos',
  menu: {
    icon: 'vaadin:car',
    order: 1,
    title: 'Vehículos',
  },
};

// Tipo para vehículo basado en la respuesta del backend
type Vehiculo = {
  id: string;
  id_persona: string;
  id_marca: string;
  persona: string;
  marca: string;
  placa: string;
  chasis: string;
  kilometraje: string;
};

type VehiculoEntryFormProps = {
  onVehiculoCreated?: () => void;
};

// Componente de búsqueda y filtros
function SearchAndFilters({
  onSearch,
  onFilter,
  marcas
}: {
  onSearch: (query: string) => void;
  onFilter: (filters: any) => void;
  marcas: { label: string, value: string }[]; // tipo esperable del combo
}) {
  const searchQuery = useSignal('');
  const statusFilter = useSignal('all');
  const marcaFilter = useSignal('all');

  const handleSearch = () => {
    onSearch(searchQuery.value);
  };

  const handleFilter = () => {
    onFilter({
      status: statusFilter.value,
      marca: marcaFilter.value
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
              color: '#2196F3',
              background: '#E3F2FD',
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
            placeholder="Buscar por placa"
            value={searchQuery.value}
            onValueChanged={(e) => searchQuery.value = e.detail.value}
            style={{ minWidth: '300px' }}
          />
          
          <ComboBox
            label="Estado del vehículo"
            items={[
              { label: 'Todos', value: 'all' },
              { label: 'Nuevo (< 10k km)', value: 'new' },
              { label: 'Bueno (10k-50k km)', value: 'good' },
              { label: 'Regular (50k-100k km)', value: 'regular' },
              { label: 'Mantenimiento (> 100k km)', value: 'maintenance' }
            ]}
            value={statusFilter.value}
            onValueChanged={(e) => statusFilter.value = e.detail.value}
          />
          
<ComboBox
  label="Marca"
  items={[{ label: 'Todas las marcas', value: 'all' }, ...marcas]}
            value={marcaFilter.value}
            onValueChanged={(e) => marcaFilter.value = e.detail.value}
          />
        </HorizontalLayout>

        <HorizontalLayout style={{ gap: '12px', justifyContent: 'flex-end' }}>
          <Button 
            theme="tertiary"
            onClick={() => {
              searchQuery.value = '';
              statusFilter.value = 'all';
              marcaFilter.value = 'all';
              onSearch('');
              onFilter({ status: 'all', marca: 'all' });
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

//GUARDAR Vehiculo
function VehiculoEntryForm(props: VehiculoEntryFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const persona = useSignal('');
  const marca = useSignal('');
  const placa = useSignal('');
  const chasis = useSignal('');
  const kilometraje = useSignal('');
  const [listamarca, setListamarca] = useState<any[]>([]);
  const [listapersona, setListapersona] = useState<any[]>([]);

  const [invalidFields, setInvalidFields] = useState<{ [key: string]: boolean }>({});

  useEffect(() => {
    const loadComboData = async () => {
      try {
        const marcas = await VehiculoService.listaMarcaCombo();
        const personas = await VehiculoService.listaPersonaCombo();
        setListamarca(marcas || []);
        setListapersona(personas || []);
      } catch (error) {
        console.error('Error loading combo data:', error);
      }
    };
    loadComboData();
  }, []);//

  const createVehiculo = async () => {
    // Limpiar espacios y normalizar inputs
    const placaVal = placa.value.trim().toUpperCase();
    const chasisVal = chasis.value.trim().toUpperCase();
    const kilometrajeVal = kilometraje.value.trim();

    // Validación básica
    const errors: string[] = [];
    const newInvalidFields: { [key: string]: boolean } = {};

    if (!persona.value.trim()) {
      errors.push('Debe seleccionar una persona.');
      newInvalidFields['persona'] = true;
    }

    if (!marca.value.trim()) {
      errors.push('Debe seleccionar una marca.');
      newInvalidFields['marca'] = true;
    }

    if (!placaVal) {
      errors.push('Debe ingresar la placa.');
      newInvalidFields['placa'] = true;
    }

    if (!chasisVal) {
      errors.push('Debe ingresar el chasis.');
      newInvalidFields['chasis'] = true;
    }

    if (!kilometrajeVal || isNaN(Number(kilometrajeVal)) || Number(kilometrajeVal) <= 0) {
      errors.push('El kilometraje debe ser un número positivo.');
      newInvalidFields['kilometraje'] = true;
    }

    if (errors.length > 0) {
      setInvalidFields(newInvalidFields);
      Notification.show('¡Tiene que llenar todos los apartados!\n' + errors.join('\n'), {
        duration: 5000,
        position: 'top-center',
        theme: 'error',
      });
      return;
    }

    try {
      await VehiculoService.createVehiculo(
        parseInt(persona.value),
        parseInt(marca.value),
        placaVal,
        chasisVal,
        parseInt(kilometrajeVal)
      );
      if (props.onVehiculoCreated) {
        props.onVehiculoCreated();
      }

      // Resetear campos
      persona.value = '';
      marca.value = '';
      placa.value = '';
      chasis.value = '';
      kilometraje.value = '';
      setInvalidFields({});
      setDialogOpened(false);

      Notification.show('Vehículo creado exitosamente', { 
        duration: 3000, 
        position: 'bottom-end', 
        theme: 'success' 
      });
    } catch (error) {
      console.error(error);
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Vehículo"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(event) => setDialogOpened(event.detail.value)}
        header={<h2 className="draggable" style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold', padding: 'var(--lumo-space-m) 0' }}>Registrar Vehículo</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={() => setDialogOpened(false)}>Cancelar</Button>
            <Button theme="primary" onClick={createVehiculo}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}>
          <VerticalLayout style={{ alignItems: 'stretch' }}>
          <ComboBox
            label="Persona del Vehículo"
              items={listapersona}
            value={persona.value}
            onValueChanged={(evt) => {
              persona.value = evt.detail.value;
              setInvalidFields((prev) => ({ ...prev, persona: false }));
            }}
            errorMessage="Seleccione una persona"
            invalid={invalidFields['persona']}
          />
          <ComboBox
            label="Marca"
              items={listamarca}
            value={marca.value}
            onValueChanged={(evt) => {
              marca.value = evt.detail.value;
              setInvalidFields((prev) => ({ ...prev, marca: false }));
            }}
            errorMessage="Seleccione una marca"
            invalid={invalidFields['marca']}
          />
          <NumberField
            label="Kilometraje"
            placeholder="Ej: 120000"
            value={kilometraje.value}
            onValueChanged={(evt) => {
              kilometraje.value = evt.detail.value || '';
              setInvalidFields((prev) => ({ ...prev, kilometraje: false }));
            }}
            errorMessage="Kilometraje requerido (positivo)"
            invalid={invalidFields['kilometraje']}
          />
          <TextField
            label="Chasis del Vehículo"
            value={chasis.value}
            onValueChanged={(evt) => {
              chasis.value = evt.detail.value.toUpperCase();
              setInvalidFields((prev) => ({ ...prev, chasis: false }));
            }}
            errorMessage="Chasis requerido"
            invalid={invalidFields['chasis']}
          />
          <TextField
            label="Placa del Vehículo"
            value={placa.value}
            onValueChanged={(evt) => {
              placa.value = evt.detail.value.toUpperCase();
              setInvalidFields((prev) => ({ ...prev, placa: false }));
            }}
            errorMessage="Placa requerida"
            invalid={invalidFields['placa']}
          />
          </VerticalLayout>
        </VerticalLayout>
      </Dialog>
      
      <Button theme="primary" onClick={() => setDialogOpened(true)}>
        <Icon icon="vaadin:plus" slot="prefix" />
        Nuevo Vehículo
      </Button>
    </>
  );
}

//ELIMINAR VEHICULO
const eliminarVehiculo = async (id: number) => {
  try {
    await VehiculoService.deleteVehiculo(id);
    Notification.show('Vehículo eliminado', { duration: 3000, position: 'bottom-end', theme: 'success' });
  } catch (error) {
    console.error(error);
    handleError(error);
  }
};


//editar Vehiculo
function VehiculoEditForm({
  vehiculo,
  onSave,
  onCancel
}: {
  vehiculo: Vehiculo;
  onSave: () => void;
  onCancel: () => void;
}) {
  const [listamarca, setListamarca] = useState<any[]>([]);
  const [listapersona, setListapersona] = useState<any[]>([]);
  const persona = useSignal(vehiculo.id_persona);
  const marca = useSignal(vehiculo.id_marca);
  const placa = useSignal(vehiculo.placa);
  const chasis = useSignal(vehiculo.chasis);
  const kilometraje = useSignal(vehiculo.kilometraje);

  const [invalidFields, setInvalidFields] = useState<{ [key: string]: boolean }>({});

  useEffect(() => {
    const loadComboData = async () => {
      try {
        const marcas = await VehiculoService.listaMarcaCombo();
        const personas = await VehiculoService.listaPersonaCombo();
        setListamarca(marcas || []);
        setListapersona(personas || []);
      } catch (error) {
        console.error('Error loading combo data:', error);
      }
    };
    loadComboData();
  }, []);

  const updateVehiculo = async () => {
    const errors: string[] = [];
    const newInvalidFields: { [key: string]: boolean } = {};

    if (!persona.value.trim()) {
      errors.push('Debe seleccionar una persona.');
      newInvalidFields['persona'] = true;
    }
    if (!marca.value.trim()) {
      errors.push('Debe seleccionar una marca.');
      newInvalidFields['marca'] = true;
    }
    if (!placa.value.trim()) {
      errors.push('Debe ingresar la placa.');
      newInvalidFields['placa'] = true;
    }
    if (!chasis.value.trim()) {
      errors.push('Debe ingresar el chasis.');
      newInvalidFields['chasis'] = true;
    }
    if (!kilometraje.value || isNaN(Number(kilometraje.value)) || Number(kilometraje.value) <= 0) {
      errors.push('El kilometraje debe ser un número positivo.');
      newInvalidFields['kilometraje'] = true;
    }

    if (errors.length > 0) {
      setInvalidFields(newInvalidFields);
      Notification.show('Errores en el formulario:\n' + errors.join('\n'), {
        duration: 5000,
        position: 'top-center',
        theme: 'error'
      });
      return;
    }

    try {
      await VehiculoService.updateVehiculo(
        parseInt(vehiculo.id),
        parseInt(persona.value),
        parseInt(marca.value),
        placa.value.trim().toUpperCase(),
        chasis.value.trim().toUpperCase(),
        parseInt(kilometraje.value)
      );
      Notification.show('Vehículo actualizado exitosamente', {
        duration: 3000,
        position: 'bottom-end',
        theme: 'success'
      });
      onSave();
    } catch (error) {
      console.error(error);
      handleError(error);
    }
  };

  return (
    <VerticalLayout theme="spacing" style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}>
      <ComboBox
        label="Persona del Vehículo"
        items={listapersona}
        value={persona.value}
        onValueChanged={(evt) => {
          persona.value = evt.detail.value;
          setInvalidFields((prev) => ({ ...prev, persona: false }));
        }}
        errorMessage="Seleccione una persona"
        invalid={invalidFields['persona']}
      />
      <ComboBox
        label="Marca"
        items={listamarca}
        value={marca.value}
        onValueChanged={(evt) => {
          marca.value = evt.detail.value;
          setInvalidFields((prev) => ({ ...prev, marca: false }));
        }}
        errorMessage="Seleccione una marca"
        invalid={invalidFields['marca']}
      />
      <NumberField
        label="Kilometraje"
        placeholder="Ej: 120000"
        value={kilometraje.value}
        onValueChanged={(evt) => {
          kilometraje.value = evt.detail.value || '';
          setInvalidFields((prev) => ({ ...prev, kilometraje: false }));
        }}
        errorMessage="Kilometraje requerido"
        invalid={invalidFields['kilometraje']}
      />
      <TextField
        label="Chasis"
        value={chasis.value}
        onValueChanged={(evt) => {
          chasis.value = evt.detail.value;
          setInvalidFields((prev) => ({ ...prev, chasis: false }));
        }}
        errorMessage="Chasis requerido"
        invalid={invalidFields['chasis']}
      />
      <TextField
        label="Placa"
        value={placa.value}
        onValueChanged={(evt) => {
          placa.value = evt.detail.value;
          setInvalidFields((prev) => ({ ...prev, placa: false }));
        }}
        errorMessage="Placa requerida"
        invalid={invalidFields['placa']}
      />
      <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '16px' }}>
        <Button onClick={onCancel}>Cancelar</Button>
        <Button theme="primary" onClick={updateVehiculo}>Guardar</Button>
      </div>
    </VerticalLayout>
  );
}





//LISTA DE VehiculoS
export default function VehiculoView() {
  const [vehicles, setVehicles] = useState<Vehiculo[]>([]);
  const [filteredVehicles, setFilteredVehicles] = useState<Vehiculo[]>([]);
  const [vehiculoEditar, setVehiculoEditar] = useState<Vehiculo | null>(null);
  const [viewMode, setViewMode] = useState<'cards' | 'grid'>('cards');
  const [loading, setLoading] = useState(true);
  const [marcasFiltro, setMarcasFiltro] = useState<{ label: string, value: string }[]>([]);

useEffect(() => {
  loadVehicles();

  const fetchMarcas = async () => {
    try {
      const marcas = await VehiculoService.listaMarcaCombo();
      const formatted = (marcas || []).map((m: any) => ({
        label: m.label || m.nombre || m.modelo, // ajusta si es necesario
        value: m.value || m.id?.toString() || m.nombre?.toLowerCase()
      }));
      setMarcasFiltro(formatted);
    } catch (e) {
      console.error('Error cargando marcas para filtro', e);
    }
  };

  fetchMarcas();
}, []);


  const loadVehicles = async () => {
    try {
      setLoading(true);
      const data = await VehiculoService.listVehiculo();
      const vehiclesList = data || [];
      setVehicles(vehiclesList as Vehiculo[]);
      setFilteredVehicles(vehiclesList as Vehiculo[]);
    } catch (error) {
      console.error('Error loading vehicles:', error);
      handleError(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadVehicles();
  }, []);

const handleSearch = async (query: string) => {
  if (!query.trim()) {
    setFilteredVehicles(vehicles);
    return;
  }
  try {
    // Llamar al backend una vez, usando atributo por placa
    const resp = await VehiculoService.search('placa', query.trim(), 3);
    const searchResult: Vehiculo[] = resp as Vehiculo[];
    setFilteredVehicles(searchResult);
  } catch (error) {
    console.error('Error en búsqueda backend:', error);
    handleError(error);
  }
};

  const handleFilter = (filters: any) => {
    let filtered = vehicles;

    // Filtro por estado
    if (filters.status !== 'all') {
      filtered = filtered.filter(vehicle => {
        const km = parseInt(vehicle.kilometraje);
        switch (filters.status) {
          case 'new': return km < 10000;
          case 'good': return km >= 10000 && km < 50000;
          case 'regular': return km >= 50000 && km < 100000;
          case 'maintenance': return km >= 100000;
          default: return true;
        }
      });
    }

    // Filtro por marca
if (filters.marca !== 'all') {
  filtered = filtered.filter(vehicle => 
    vehicle.id_marca === filters.marca
  );
}
    setFilteredVehicles(filtered);
  };

  const handleEdit = (vehicle: Vehiculo) => {
    setVehiculoEditar(vehicle);
  };

  const handleDelete = async (id: string) => {
    await eliminarVehiculo(parseInt(id));
    loadVehicles(); // Recargar la lista
  };

 /* if (loading) {
    return (
      <div style={{ 
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center', 
        height: '200px' 
      }}>
        <ProgressBar indeterminate style={{ width: '200px' }} />
      </div>
    );
  }*/

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m" style={{
      background: '#f5f5f5',
      minHeight: '100vh',
      padding: '24px'
    }}>
      {/* Header */}
      <div style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '24px'
      }}>
        <div>
          <h1 style={{ 
            fontSize: '28px', 
            fontWeight: 'bold', 
            color: '#333',
            margin: '0 0 8px 0'
          }}>
            🚗 Gestión de Vehículos
          </h1>
          <p style={{ 
            fontSize: '14px', 
            color: '#666',
            margin: 0
          }}>
            Administra el inventario de vehículos del taller
          </p>
        </div>
        
        <HorizontalLayout style={{ gap: '12px' }}>
          <Button 
            theme="tertiary"
            onClick={() => setViewMode(viewMode === 'cards' ? 'grid' : 'cards')}
          >
            <Icon icon={viewMode === 'cards' ? 'vaadin:table' : 'vaadin:grid'} slot="prefix" />
            {viewMode === 'cards' ? 'Vista Tabla' : 'Vista Tarjetas'}
          </Button>
          
          <VehiculoEntryForm onVehiculoCreated={loadVehicles} />
        </HorizontalLayout>
      </div>

      {/* Búsqueda y filtros */}
      <SearchAndFilters
  onSearch={handleSearch}
  onFilter={handleFilter}
  marcas={marcasFiltro}
/>

      {/* Estadísticas */}
      <Card style={{
        borderRadius: '12px',
        padding: '20px',
        marginBottom: '20px',
        border: '1px solid #e0e0e0',
        background: 'white'
      }}>
        <HorizontalLayout style={{ 
          gap: '24px', 
          justifyContent: 'space-around',
          flexWrap: 'wrap'
        }}>
          <div style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#2196F3' }}>
              {filteredVehicles.length}
            </div>
            <div style={{ fontSize: '12px', color: '#666' }}>Vehículos</div>
          </div>
          
          <div style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#4CAF50' }}>
              {filteredVehicles.filter(v => parseInt(v.kilometraje) < 10000).length}
            </div>
            <div style={{ fontSize: '12px', color: '#666' }}>Nuevos</div>
          </div>
          
          <div style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#FF9800' }}>
              {filteredVehicles.filter(v => parseInt(v.kilometraje) >= 100000).length}
            </div>
            <div style={{ fontSize: '12px', color: '#666' }}>Mantenimiento</div>
          </div>
          
          <div style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#9C27B0' }}>
              {filteredVehicles.length > 0 ? 
                Math.round(filteredVehicles.reduce((sum, v) => sum + parseInt(v.kilometraje), 0) / filteredVehicles.length) : 0
              }
            </div>
            <div style={{ fontSize: '12px', color: '#666' }}>Promedio km</div>
          </div>
        </HorizontalLayout>
      </Card>

      {/* Contenido principal */}
      {viewMode === 'cards' ? (
        // Vista de tarjetas
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(350px, 1fr))',
          gap: '20px',
          padding: '0'
        }}>
          {filteredVehicles.map((vehicle) => (
            <VehicleCard
              key={vehicle.id}
              vehicle={vehicle}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          ))}
        </div>
      ) : (
        // Vista de tabla
        <Card style={{
          borderRadius: '12px',
          border: '1px solid #e0e0e0',
          background: 'white',
          overflow: 'hidden'
        }}>
          <Grid 
            items={filteredVehicles}
            style={{ border: 'none' }}
          >
        <GridColumn path="placa" header="Placa" />
            <GridColumn path="marca.modelo" header="Marca" />
            <GridColumn path="persona.nombre" header="Propietario" />
            <GridColumn path="kilometraje" header="Kilometraje" />
            <GridColumn header="Acciones" autoWidth>
              {({ item }: { item: Vehiculo }) => (
                <HorizontalLayout style={{ gap: '8px' }}>
                                     <Button 
                     theme="tertiary"
                     onClick={() => handleEdit(item)}
                   >
                    <Icon icon="vaadin:edit" slot="prefix" />
                    Editar
                  </Button>
                                     <Button 
                     theme="error"
                     onClick={() => handleDelete(item.id)}
                   >
                    <Icon icon="vaadin:trash" slot="prefix" />
                    Eliminar
                  </Button>
                </HorizontalLayout>
              )}
            </GridColumn>
      </Grid>
        </Card>
      )}

      {/* Mensaje cuando no hay vehículos */}
      {filteredVehicles.length === 0 && !loading && (
        <Card style={{
          borderRadius: '12px',
          padding: '40px',
          textAlign: 'center',
          border: '1px solid #e0e0e0',
          background: 'white'
        }}>
          <Icon 
            icon="vaadin:car" 
            style={{ 
              fontSize: '48px', 
              color: '#ccc',
              marginBottom: '16px'
            }} 
          />
          <h3 style={{ 
            fontSize: '18px', 
            color: '#666',
            margin: '0 0 8px 0'
          }}>
            No se encontraron vehículos
          </h3>
          <p style={{ 
            fontSize: '14px', 
            color: '#999',
            margin: 0
          }}>
            {vehicles.length === 0 ? 
              'Aún no hay vehículos registrados. ¡Agrega el primero!' :
              'Intenta ajustar los filtros de búsqueda.'
            }
          </p>
        </Card>
      )}

      {/* Dialog de edición */}
{vehiculoEditar && (
  <Dialog
    headerTitle={`Editar Vehículo - ${vehiculoEditar.placa}`}
    opened={true}
    onOpenedChanged={({ detail }) => {
      if (!detail.value) setVehiculoEditar(null);
    }}
  >
    <VehiculoEditForm
      vehiculo={vehiculoEditar}
      onSave={() => {
        setVehiculoEditar(null);
        loadVehicles();
      }}
      onCancel={() => setVehiculoEditar(null)}
    />
  </Dialog>
)}
    </main>
  );
}
