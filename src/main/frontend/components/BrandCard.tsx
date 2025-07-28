import { 
  Card, 
  HorizontalLayout, 
  VerticalLayout, 
  Icon, 
  Button
} from '@vaadin/react-components';

interface BrandCardProps {
  brand: {
    id: string;
    modelo: string;
    fecha: string;
  };
  onEdit?: (brand: any) => void;
  onDelete?: (id: string) => void;
}

// Función para obtener el color de la marca basado en el nombre
function getBrandColor(modelo: string) {
  const colors = [
    '#2196F3', '#4CAF50', '#FF9800', '#9C27B0', '#F44336',
    '#00BCD4', '#8BC34A', '#FF5722', '#673AB7', '#3F51B5'
  ];
  
  const index = modelo.length % colors.length;
  return colors[index];
}

// Función para obtener el icono de la marca
function getBrandIcon(modelo: string) {
  const brandIcons: { [key: string]: string } = {
    'toyota': 'vaadin:car',
    'honda': 'vaadin:car',
    'ford': 'vaadin:car',
    'chevrolet': 'vaadin:car',
    'nissan': 'vaadin:car',
    'bmw': 'vaadin:car',
    'mercedes': 'vaadin:car',
    'audi': 'vaadin:car',
    'volkswagen': 'vaadin:car',
    'hyundai': 'vaadin:car',
    'kia': 'vaadin:car',
    'mazda': 'vaadin:car',
    'subaru': 'vaadin:car',
    'mitsubishi': 'vaadin:car',
    'lexus': 'vaadin:car',
    'infiniti': 'vaadin:car',
    'acura': 'vaadin:car',
    'volvo': 'vaadin:car',
    'saab': 'vaadin:car',
    'jaguar': 'vaadin:car',
    'land rover': 'vaadin:car',
    'range rover': 'vaadin:car',
    'porsche': 'vaadin:car',
    'ferrari': 'vaadin:car',
    'lamborghini': 'vaadin:car',
    'maserati': 'vaadin:car',
    'aston martin': 'vaadin:car',
    'bentley': 'vaadin:car',
    'rolls royce': 'vaadin:car',
    'bugatti': 'vaadin:car',
    'mclaren': 'vaadin:car',
    'koenigsegg': 'vaadin:car',
    'pagani': 'vaadin:car',
    'lotus': 'vaadin:car',
    'alpine': 'vaadin:car',
    'lancia': 'vaadin:car',
    'alfa romeo': 'vaadin:car',
    'fiat': 'vaadin:car',
    'lada': 'vaadin:car',
    'skoda': 'vaadin:car',
    'seat': 'vaadin:car',
    'opel': 'vaadin:car',
    'peugeot': 'vaadin:car',
    'renault': 'vaadin:car',
    'citroen': 'vaadin:car',
    'dacia': 'vaadin:car',
    'daihatsu': 'vaadin:car',
    'suzuki': 'vaadin:car',
    'isuzu': 'vaadin:car',
    'datsun': 'vaadin:car',
    'tata': 'vaadin:car',
    'mahindra': 'vaadin:car',
    'maruti': 'vaadin:car',
    'hindustan': 'vaadin:car',
    'premier': 'vaadin:car',
    'bajaj': 'vaadin:car',
    'hero': 'vaadin:car',
    'tvs': 'vaadin:car',
    'yamaha': 'vaadin:car',
    'kawasaki': 'vaadin:car',
    'ducati': 'vaadin:car',
    'harley davidson': 'vaadin:car',
    'indian': 'vaadin:car',
    'triumph': 'vaadin:car',
    'royal enfield': 'vaadin:car',
    'ktm': 'vaadin:car',
    'aprilia': 'vaadin:car',
    'mv agusta': 'vaadin:car',
    'benelli': 'vaadin:car',
    'cf moto': 'vaadin:car',
    'sym': 'vaadin:car',
    'keeway': 'vaadin:car',
    'lexmoto': 'vaadin:car',
    'lifan': 'vaadin:car',
    'zontes': 'vaadin:car',
    'voge': 'vaadin:car',
    'qj motor': 'vaadin:car'
  };
  
  const lowerModelo = modelo.toLowerCase();
  for (const [brand, icon] of Object.entries(brandIcons)) {
    if (lowerModelo.includes(brand)) {
      return icon;
    }
  }
  
  return 'vaadin:factory'; // Icono por defecto
}

export default function BrandCard({ brand, onEdit, onDelete }: BrandCardProps) {
  const brandColor = getBrandColor(brand.modelo);
  const brandIcon = getBrandIcon(brand.modelo);

  return (
    <Card style={{
      borderRadius: '12px',
      padding: '20px',
      border: '1px solid #e0e0e0',
      background: 'white',
      boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
      transition: 'transform 0.2s, box-shadow 0.2s',
      cursor: 'pointer',
      minHeight: '160px',
      position: 'relative',
      overflow: 'hidden'
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
      {/* Fondo decorativo */}
      <div style={{
        position: 'absolute',
        top: '-20px',
        right: '-20px',
        width: '80px',
        height: '80px',
        background: `${brandColor}10`,
        borderRadius: '50%',
        zIndex: 0
      }} />
      
      <VerticalLayout style={{ gap: '16px', position: 'relative', zIndex: 1 }}>
        {/* Header con icono y nombre */}
        <HorizontalLayout style={{ 
          justifyContent: 'space-between', 
          alignItems: 'center',
          marginBottom: '8px'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <Icon 
              icon={brandIcon} 
              style={{ 
                fontSize: '28px', 
                color: brandColor,
                background: `${brandColor}15`,
                padding: '10px',
                borderRadius: '10px'
              }} 
            />
            <div>
              <div style={{ 
                fontSize: '20px', 
                fontWeight: 'bold', 
                color: '#333',
                marginBottom: '4px',
                textTransform: 'capitalize'
              }}>
                {brand.modelo}
              </div>
              {brand.fecha && (
                <div style={{ 
                  fontSize: '12px', 
                  color: '#666',
                  fontWeight: '500'
                }}>
                  Fecha: {brand.fecha}
                </div>
              )}
            </div>
          </div>
        </HorizontalLayout>

        {/* Información adicional */}
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
          <Icon icon="vaadin:factory" style={{ fontSize: '14px', color: '#666' }} />
          <span>Fabricante de automóviles</span>
        </div>

        {/* Estadísticas simuladas */}
        <HorizontalLayout style={{ 
          gap: '12px',
          justifyContent: 'space-between'
        }}>
          <div style={{ 
            textAlign: 'center',
            flex: 1
          }}>
            <div style={{ 
              fontSize: '18px', 
              fontWeight: 'bold', 
              color: brandColor 
            }}>
              {Math.floor(Math.random() * 50) + 10}
            </div>
            <div style={{ 
              fontSize: '10px', 
              color: '#666',
              textTransform: 'uppercase',
              letterSpacing: '0.5px'
            }}>
              Modelos
            </div>
          </div>
          
          <div style={{ 
            textAlign: 'center',
            flex: 1
          }}>
            <div style={{ 
              fontSize: '18px', 
              fontWeight: 'bold', 
              color: brandColor 
            }}>
              {Math.floor(Math.random() * 20) + 5}
            </div>
            <div style={{ 
              fontSize: '10px', 
              color: '#666',
              textTransform: 'uppercase',
              letterSpacing: '0.5px'
            }}>
              Años
            </div>
          </div>
        </HorizontalLayout>

        {/* Acciones */}
        <HorizontalLayout style={{ 
          gap: '8px', 
          marginTop: '8px',
          justifyContent: 'flex-end'
        }}>
          <Button 
            theme="tertiary"
            onClick={() => onEdit?.(brand)}
            style={{
              background: `${brandColor}15`,
              color: brandColor,
              border: `1px solid ${brandColor}`,
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
            onClick={() => onDelete?.(brand.id)}
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