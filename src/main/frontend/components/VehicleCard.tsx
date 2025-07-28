import { 
  Card, 
  HorizontalLayout, 
  VerticalLayout, 
  Icon, 
  ProgressBar,
  Button
} from '@vaadin/react-components';

interface VehicleCardProps {
  vehicle: {
    id: string;
    placa: string;
    chasis: string;
    kilometraje: string;
    marca: string;
    persona: string;
  };
  onEdit?: (vehicle: any) => void;
  onDelete?: (id: string) => void;
}

// Función para determinar el estado del vehículo basado en kilometraje
function getVehicleStatus(kilometraje: string) {
  const km = parseInt(kilometraje);
  if (km < 10000) {
    return { status: 'Nuevo', color: '#4CAF50', bgColor: '#E8F5E8' };
  } else if (km < 50000) {
    return { status: 'Bueno', color: '#2196F3', bgColor: '#E3F2FD' };
  } else if (km < 100000) {
    return { status: 'Regular', color: '#FF9800', bgColor: '#FFF3E0' };
  } else {
    return { status: 'Mantenimiento', color: '#F44336', bgColor: '#FFEBEE' };
  }
}

// Función para calcular el progreso del kilometraje
function getKilometrajeProgress(kilometraje: string) {
  // Consideramos 200,000 km como el máximo para el progreso
  const maxKm = 200000;
  const km = parseInt(kilometraje);
  return Math.min((km / maxKm) * 100, 100);
}

export default function VehicleCard({ vehicle, onEdit, onDelete }: VehicleCardProps) {
  const status = getVehicleStatus(vehicle.kilometraje);
  const progress = getKilometrajeProgress(vehicle.kilometraje);

  return (
    <Card style={{
      borderRadius: '12px',
      padding: '20px',
      border: '1px solid #e0e0e0',
      background: 'white',
      boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
      transition: 'transform 0.2s, box-shadow 0.2s',
      cursor: 'pointer',
      minHeight: '200px'
    }}
    onMouseEnter={(e) => {
      e.currentTarget.style.transform = 'translateY(-2px)';
      e.currentTarget.style.boxShadow = '0 4px 16px rgba(0,0,0,0.15)';
    }}
    onMouseLeave={(e) => {
      e.currentTarget.style.transform = 'translateY(0)';
      e.currentTarget.style.boxShadow = '0 2px 8px rgba(0,0,0,0.1)';
    }}
    >
      <VerticalLayout style={{ gap: '16px' }}>
        {/* Header con placa y estado */}
        <HorizontalLayout style={{ 
          justifyContent: 'space-between', 
          alignItems: 'center',
          marginBottom: '8px'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <Icon 
              icon="vaadin:car" 
              style={{ 
                fontSize: '24px', 
                color: '#2196F3',
                background: '#E3F2FD',
                padding: '8px',
                borderRadius: '8px'
              }} 
            />
            <div>
              <div style={{ 
                fontSize: '18px', 
                fontWeight: 'bold', 
                color: '#333',
                marginBottom: '4px'
              }}>
                {vehicle.placa}
              </div>
              <div style={{ 
                fontSize: '12px', 
                color: '#666',
                fontWeight: '500'
              }}>
                {vehicle.marca || 'Sin marca'}
              </div>
            </div>
          </div>
          
          <div style={{
            padding: '6px 12px',
            borderRadius: '16px',
            fontSize: '11px',
            fontWeight: '600',
            background: status.bgColor,
            color: status.color,
            border: `1px solid ${status.color}30`
          }}>
            {status.status}
          </div>
        </HorizontalLayout>

        {/* Información del propietario */}
        {vehicle.persona && (
          <div style={{ 
            display: 'flex', 
            alignItems: 'center', 
            gap: '8px',
            padding: '8px 12px',
            background: '#f8f9fa',
            borderRadius: '6px',
            fontSize: '13px',
            color: '#555'
          }}>
            <Icon icon="vaadin:user" style={{ fontSize: '14px', color: '#666' }} />
            <span>{vehicle.persona}</span>
          </div>
        )}

        {/* Kilometraje con progreso */}
        <div style={{ marginTop: '8px' }}>
          <div style={{ 
            display: 'flex', 
            justifyContent: 'space-between', 
            alignItems: 'center',
            marginBottom: '8px'
          }}>
            <span style={{ 
              fontSize: '14px', 
              fontWeight: '600', 
              color: '#333' 
            }}>
              Kilometraje
            </span>
            <span style={{ 
              fontSize: '16px', 
              fontWeight: 'bold', 
              color: '#2196F3' 
            }}>
              {vehicle.kilometraje.toLocaleString()} km
            </span>
          </div>

        </div>

        {/* Información adicional */}
        <div style={{ 
          display: 'flex', 
          alignItems: 'center', 
          gap: '8px',
          padding: '8px 12px',
          background: '#f8f9fa',
          borderRadius: '6px',
          fontSize: '12px',
          color: '#666'
        }}>
          <Icon icon="vaadin:key" style={{ fontSize: '12px' }} />
          <span>Chasis: {vehicle.chasis}</span>
        </div>

        {/* Acciones */}
        <HorizontalLayout style={{ 
          gap: '8px', 
          marginTop: '8px',
          justifyContent: 'flex-end'
        }}>
          <Button 
            theme="tertiary"
            onClick={() => onEdit?.(vehicle)}
            style={{
              background: '#E3F2FD',
              color: '#1976D2',
              border: '1px solid #2196F3',
              borderRadius: '6px',
              fontSize: '12px',
              padding: '6px 12px'
            }}
          >
            <Icon icon="vaadin:edit" slot="prefix" style={{ fontSize: '12px' }} />
            Editar
          </Button>
          
          <Button 
            theme="error"
            onClick={() => onDelete?.(vehicle.id)}
            style={{
              background: '#FFEBEE',
              color: '#D32F2F',
              border: '1px solid #F44336',
              borderRadius: '6px',
              fontSize: '12px',
              padding: '6px 12px'
            }}
          >
            <Icon icon="vaadin:trash" slot="prefix" style={{ fontSize: '12px' }} />
            Eliminar
          </Button>
        </HorizontalLayout>
      </VerticalLayout>
    </Card>
  );
} 