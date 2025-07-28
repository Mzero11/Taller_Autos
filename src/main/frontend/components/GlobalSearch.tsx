import { 
  TextField, 
  Button, 
  Card, 
  VerticalLayout, 
  HorizontalLayout,
  Icon,
  ComboBox
} from '@vaadin/react-components';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useState, useEffect } from 'react';

interface SearchResult {
  id: number;
  type: 'vehicle' | 'brand' | 'task';
  title: string;
  subtitle: string;
  description: string;
  icon: string;
  color: string;
  action: () => void;
}

interface GlobalSearchProps {
  onResultSelect?: (result: SearchResult) => void;
}

export default function GlobalSearch({ onResultSelect }: GlobalSearchProps) {
  const searchQuery = useSignal('');
  const searchType = useSignal('all');
  const [isOpen, setIsOpen] = useState(false);
  const [results, setResults] = useState<SearchResult[]>([]);
  const [loading, setLoading] = useState(false);

  // Simulación de resultados de búsqueda
  const mockSearchResults: SearchResult[] = [
    {
      id: 1,
      type: 'vehicle',
      title: 'ABC-123',
      subtitle: 'Toyota Corolla',
      description: 'Vehículo con 45,000 km - Propietario: Juan Pérez',
      icon: 'vaadin:car',
      color: '#2196F3',
      action: () => console.log('Ver vehículo ABC-123')
    },
    {
      id: 2,
      type: 'vehicle',
      title: 'XYZ-789',
      subtitle: 'Honda Civic',
      description: 'Vehículo con 78,000 km - Propietario: María García',
      icon: 'vaadin:car',
      color: '#4CAF50',
      action: () => console.log('Ver vehículo XYZ-789')
    },
    {
      id: 3,
      type: 'brand',
      title: 'Toyota',
      subtitle: 'Fabricante japonés',
      description: 'Marca fundada en 1937 - 50+ modelos disponibles',
      icon: 'vaadin:factory',
      color: '#FF9800',
      action: () => console.log('Ver marca Toyota')
    },
    {
      id: 4,
      type: 'task',
      title: 'Cambio de aceite',
      subtitle: 'Pendiente',
      description: 'Tarea programada para el vehículo ABC-123',
      icon: 'vaadin:tools',
      color: '#9C27B0',
      action: () => console.log('Ver tarea cambio de aceite')
    }
  ];

  const performSearch = async (query: string, type: string) => {
    if (!query.trim()) {
      setResults([]);
      return;
    }

    setLoading(true);
    
    // Simular delay de búsqueda
    await new Promise(resolve => setTimeout(resolve, 300));

    // Filtrar resultados basados en la consulta y tipo
    const filtered = mockSearchResults.filter(result => {
      const matchesQuery = 
        result.title.toLowerCase().includes(query.toLowerCase()) ||
        result.subtitle.toLowerCase().includes(query.toLowerCase()) ||
        result.description.toLowerCase().includes(query.toLowerCase());
      
      const matchesType = type === 'all' || result.type === type;
      
      return matchesQuery && matchesType;
    });

    setResults(filtered);
    setLoading(false);
  };

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      performSearch(searchQuery.value, searchType.value);
    }, 300);

    return () => clearTimeout(timeoutId);
  }, [searchQuery.value, searchType.value]);

  const handleResultClick = (result: SearchResult) => {
    result.action();
    onResultSelect?.(result);
    setIsOpen(false);
    searchQuery.value = '';
  };

  const getTypeLabel = (type: string) => {
    switch (type) {
      case 'vehicle': return 'Vehículos';
      case 'brand': return 'Marcas';
      case 'task': return 'Tareas';
      default: return 'Todo';
    }
  };

  const getTypeIcon = (type: string) => {
    switch (type) {
      case 'vehicle': return 'vaadin:car';
      case 'brand': return 'vaadin:factory';
      case 'task': return 'vaadin:tools';
      default: return 'vaadin:search';
    }
  };

  return (
    <div style={{ position: 'relative', width: '100%', maxWidth: '600px' }}>
      <Card style={{
        borderRadius: '12px',
        padding: '16px',
        border: '1px solid #e0e0e0',
        background: 'white',
        boxShadow: isOpen ? '0 4px 20px rgba(0,0,0,0.15)' : '0 2px 8px rgba(0,0,0,0.1)',
        transition: 'box-shadow 0.2s'
      }}>
        <VerticalLayout style={{ gap: '12px' }}>
          {/* Barra de búsqueda principal */}
          <HorizontalLayout style={{ gap: '12px', alignItems: 'center' }}>
            <Icon 
              icon="vaadin:search" 
              style={{ 
                fontSize: '18px', 
                color: '#666',
                marginLeft: '8px'
              }} 
            />
            
            <TextField
              placeholder="Buscar vehículos, marcas, tareas..."
              value={searchQuery.value}
              onValueChanged={(e) => searchQuery.value = e.detail.value}
              onFocus={() => setIsOpen(true)}
              style={{ 
                flex: 1,
                border: 'none',
                background: 'transparent'
              }}
            />
            
            <ComboBox
              items={[
                { label: 'Todo', value: 'all' },
                { label: 'Vehículos', value: 'vehicle' },
                { label: 'Marcas', value: 'brand' },
                { label: 'Tareas', value: 'task' }
              ]}
              value={searchType.value}
              onValueChanged={(e) => searchType.value = e.detail.value}
              style={{ minWidth: '120px' }}
            />
          </HorizontalLayout>

          {/* Resultados de búsqueda */}
          {isOpen && (searchQuery.value.trim() || results.length > 0) && (
            <div style={{
              borderTop: '1px solid #e0e0e0',
              paddingTop: '12px',
              maxHeight: '400px',
              overflowY: 'auto'
            }}>
              {loading ? (
                <div style={{
                  textAlign: 'center',
                  padding: '20px',
                  color: '#666'
                }}>
                  <Icon icon="vaadin:spinner" style={{ fontSize: '20px', marginBottom: '8px' }} />
                  <div>Buscando...</div>
                </div>
              ) : results.length > 0 ? (
                <VerticalLayout style={{ gap: '8px' }}>
                  {results.map((result) => (
                    <div
                      key={`${result.type}-${result.id}`}
                      onClick={() => handleResultClick(result)}
                      style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '12px',
                        padding: '12px',
                        borderRadius: '8px',
                        cursor: 'pointer',
                        transition: 'background-color 0.2s',
                        border: '1px solid transparent'
                      }}
                      onMouseEnter={(e) => {
                        e.currentTarget.style.background = '#f8f9fa';
                        e.currentTarget.style.borderColor = '#e0e0e0';
                      }}
                      onMouseLeave={(e) => {
                        e.currentTarget.style.background = 'transparent';
                        e.currentTarget.style.borderColor = 'transparent';
                      }}
                    >
                      <Icon 
                        icon={result.icon} 
                        style={{ 
                          fontSize: '20px', 
                          color: result.color,
                          background: `${result.color}15`,
                          padding: '8px',
                          borderRadius: '6px',
                          minWidth: '36px',
                          textAlign: 'center'
                        }} 
                      />
                      
                      <div style={{ flex: 1 }}>
                        <div style={{
                          fontSize: '14px',
                          fontWeight: '600',
                          color: '#333',
                          marginBottom: '2px'
                        }}>
                          {result.title}
                        </div>
                        <div style={{
                          fontSize: '12px',
                          color: '#666',
                          marginBottom: '2px'
                        }}>
                          {result.subtitle}
                        </div>
                        <div style={{
                          fontSize: '11px',
                          color: '#999'
                        }}>
                          {result.description}
                        </div>
                      </div>
                      
                      <div style={{
                        padding: '4px 8px',
                        borderRadius: '12px',
                        fontSize: '10px',
                        fontWeight: '600',
                        background: `${result.color}15`,
                        color: result.color,
                        textTransform: 'uppercase'
                      }}>
                        {getTypeLabel(result.type)}
                      </div>
                    </div>
                  ))}
                </VerticalLayout>
              ) : searchQuery.value.trim() ? (
                <div style={{
                  textAlign: 'center',
                  padding: '20px',
                  color: '#666'
                }}>
                  <Icon icon="vaadin:search-minus" style={{ fontSize: '24px', marginBottom: '8px', color: '#ccc' }} />
                  <div>No se encontraron resultados</div>
                  <div style={{ fontSize: '12px', color: '#999', marginTop: '4px' }}>
                    Intenta con otros términos de búsqueda
                  </div>
                </div>
              ) : (
                <div style={{
                  padding: '16px',
                  color: '#666',
                  fontSize: '14px'
                }}>
                  <div style={{ marginBottom: '12px', fontWeight: '600' }}>
                    Búsquedas rápidas:
                  </div>
                  <VerticalLayout style={{ gap: '8px' }}>
                    {[
                      { icon: 'vaadin:car', label: 'Buscar vehículos por placa', color: '#2196F3' },
                      { icon: 'vaadin:factory', label: 'Buscar marcas de autos', color: '#4CAF50' },
                      { icon: 'vaadin:tools', label: 'Buscar tareas pendientes', color: '#FF9800' }
                    ].map((item, index) => (
                      <div
                        key={index}
                        style={{
                          display: 'flex',
                          alignItems: 'center',
                          gap: '8px',
                          padding: '8px',
                          borderRadius: '6px',
                          cursor: 'pointer',
                          transition: 'background-color 0.2s'
                        }}
                        onMouseEnter={(e) => e.currentTarget.style.background = '#f8f9fa'}
                        onMouseLeave={(e) => e.currentTarget.style.background = 'transparent'}
                      >
                        <Icon 
                          icon={item.icon} 
                          style={{ 
                            fontSize: '14px', 
                            color: item.color 
                          }} 
                        />
                        <span style={{ fontSize: '12px' }}>{item.label}</span>
                      </div>
                    ))}
                  </VerticalLayout>
                </div>
              )}
            </div>
          )}
        </VerticalLayout>
      </Card>

      {/* Overlay para cerrar la búsqueda */}
      {isOpen && (
        <div
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            zIndex: -1
          }}
          onClick={() => setIsOpen(false)}
        />
      )}
    </div>
  );
} 