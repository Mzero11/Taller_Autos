import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { 
  Card, 
  HorizontalLayout, 
  VerticalLayout, 
  Icon, 
  ProgressBar,
  Button,
  Grid,
  GridColumn
} from '@vaadin/react-components';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useEffect, useState } from 'react';
import { VehiculoService, MarcaService, TaskService } from 'Frontend/generated/endpoints';

export const config: ViewConfig = {
  title: 'Dashboard - Taller de Autos',
  menu: {
    icon: 'vaadin:dashboard',
    order: 0,
    title: 'Dashboard',
  },
};

// Componente para estadísticas
function StatCard({ title, value, icon, color, subtitle }: {
  title: string;
  value: string | number;
  icon: string;
  color: string;
  subtitle?: string;
}) {
  return (
    <Card className="stat-card" style={{ 
      background: `linear-gradient(135deg, ${color}15, ${color}05)`,
      border: `1px solid ${color}30`,
      borderRadius: '12px',
      padding: '20px',
      minHeight: '120px',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'space-between'
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '8px' }}>
        <Icon 
          icon={icon} 
          style={{ 
            fontSize: '24px', 
            color: color,
            background: `${color}20`,
            padding: '8px',
            borderRadius: '8px'
          }} 
        />
        <span style={{ 
          fontSize: '14px', 
          fontWeight: '600', 
          color: '#666',
          textTransform: 'uppercase',
          letterSpacing: '0.5px'
        }}>
          {title}
        </span>
      </div>
      <div>
        <div style={{ 
          fontSize: '32px', 
          fontWeight: 'bold', 
          color: '#333',
          marginBottom: '4px'
        }}>
          {value}
        </div>
        {subtitle && (
          <div style={{ 
            fontSize: '12px', 
            color: '#888',
            fontWeight: '500'
          }}>
            {subtitle}
          </div>
        )}
      </div>
    </Card>
  );
}

// Componente para vehículos recientes
function RecentVehicles({ vehicles }: { vehicles: any[] }) {
  return (
    <Card style={{ 
      borderRadius: '12px',
      padding: '20px',
      border: '1px solid #e0e0e0'
    }}>
      <div style={{ 
        display: 'flex', 
        alignItems: 'center', 
        gap: '12px', 
        marginBottom: '20px' 
      }}>
        <Icon 
          icon="vaadin:car" 
          style={{ 
            fontSize: '20px', 
            color: '#2196F3',
            background: '#2196F320',
            padding: '6px',
            borderRadius: '6px'
          }} 
        />
        <h3 style={{ margin: 0, fontSize: '18px', fontWeight: '600', color: '#333' }}>
          Vehículos Agregados
        </h3>
      </div>
      
      {vehicles.length > 0 ? (
        <VerticalLayout style={{ gap: '12px' }}>
          {vehicles.slice(0, 5).map((vehicle, index) => (
            <div key={index} style={{
              display: 'flex',
              alignItems: 'center',
              gap: '12px',
              padding: '12px',
              background: '#f8f9fa',
              borderRadius: '8px',
              border: '1px solid #e9ecef'
            }}>
              <Icon 
                icon="vaadin:car" 
                style={{ 
                  fontSize: '16px', 
                  color: '#666',
                  background: '#fff',
                  padding: '4px',
                  borderRadius: '4px'
                }} 
              />
              <div style={{ flex: 1 }}>
                <div style={{ fontWeight: '600', color: '#333', fontSize: '14px' }}>
{vehicle.placa} - {vehicle.marca || 'Sin marca'}
                </div>
                <div style={{ fontSize: '12px', color: '#666' }}>
                  {vehicle.kilometraje} km
                </div>
              </div>
              <div style={{
                padding: '4px 8px',
                borderRadius: '12px',
                fontSize: '10px',
                fontWeight: '600',
                background: '#e3f2fd',
                color: '#1976d2'
              }}>
                
              </div>
            </div>
          ))}
        </VerticalLayout>
      ) : (
        <div style={{ 
          textAlign: 'center', 
          color: '#666', 
          padding: '20px',
          fontSize: '14px'
        }}>
          No hay vehículos registrados
        </div>
      )}
    </Card>
  );
}

//Marcas registradas
function RecentBrands({ brands }: { brands: any[] }) {
  return (
    <Card style={{ 
      borderRadius: '12px',
      padding: '20px',
      border: '1px solid #e0e0e0'
    }}>
      <div style={{ 
        display: 'flex', 
        alignItems: 'center', 
        gap: '12px', 
        marginBottom: '20px' 
      }}>
        <Icon 
          icon="vaadin:factory" 
          style={{ 
            fontSize: '20px', 
            color: '#4CAF50',
            background: '#C8E6C9',
            padding: '6px',
            borderRadius: '6px'
          }} 
        />
        <h3 style={{ margin: 0, fontSize: '18px', fontWeight: '600', color: '#333' }}>
          Marcas Recientes
        </h3>
      </div>

      {brands.length > 0 ? (
        <VerticalLayout style={{ gap: '12px' }}>
          {brands.slice(-5).reverse().map((brand, index) => (
            <div key={index} style={{
              display: 'flex',
              alignItems: 'center',
              gap: '12px',
              padding: '12px',
              background: '#f8f9fa',
              borderRadius: '8px',
              border: '1px solid #e9ecef'
            }}>
              <Icon 
                icon="vaadin:label" 
                style={{ 
                  fontSize: '16px', 
                  color: '#388E3C',
                  background: '#fff',
                  padding: '4px',
                  borderRadius: '4px'
                }} 
              />
              <div style={{ flex: 1 }}>
                <div style={{ fontWeight: '600', color: '#333', fontSize: '14px' }}>
                  {brand.modelo}
                </div>

              </div>
            </div>
          ))}
        </VerticalLayout>
      ) : (
        <div style={{ 
          textAlign: 'center', 
          color: '#666', 
          padding: '20px',
          fontSize: '14px'
        }}>
          No hay marcas registradas
        </div>
      )}
    </Card>
  );
}



export default function DashboardView() {
  const [stats, setStats] = useState({
    totalVehicles: 0,
    totalBrands: 0,
    totalTasks: 0,
    activeServices: 0
  });
  const [recentVehicles, setRecentVehicles] = useState<any[]>([]);
  const [recentBrands, setRecentBrands] = useState<any[]>([]);

  useEffect(() => {
    const loadDashboardData = async () => {
      try {
        // Cargar estadísticas
        const vehicles = await VehiculoService.listVehiculo();
        const brands = await MarcaService.listMarca();
        const tasks = await TaskService.list({ 
          pageNumber: 0, 
          pageSize: 100,
          sort: { orders: [] }
        });
        
        const vehiclesList = vehicles || [];
        const brandsList = brands || [];
        const tasksList = tasks || [];
        
        setStats({
          totalVehicles: vehiclesList.length,
          totalBrands: brandsList.length,
          totalTasks: tasksList.length,
          activeServices: Math.floor(vehiclesList.length * 0.3) // Simulado
        });
        
        setRecentVehicles(vehiclesList.slice(-5).reverse());
        setRecentBrands(brandsList.slice(-5).reverse());
      } catch (error) {
        console.error('Error loading dashboard data:', error);
      }
    };

    loadDashboardData();
  }, []);

  return (
    <div style={{ 
      padding: '24px',
      background: '#f5f5f5',
      minHeight: '100vh'
    }}>
      {/* Header del Dashboard */}
      <div style={{ 
        marginBottom: '32px',
        textAlign: 'center'
      }}>
        <h1 style={{ 
          fontSize: '32px', 
          fontWeight: 'bold', 
          color: '#333',
          margin: '0 0 8px 0'
        }}>
          🚗 Taller de Autos
        </h1>
        <p style={{ 
          fontSize: '16px', 
          color: '#666',
          margin: 0
        }}>
          Bienvenido al Sistema de Gestión del Taller de Autos
        </p>
      </div>

      {/* Estadísticas principales */}
      <HorizontalLayout 
        style={{ 
          gap: '20px', 
          marginBottom: '32px',
          flexWrap: 'wrap'
        }}
      >
        <StatCard
          title="Vehículos Registrados"
          value={stats.totalVehicles}
          icon="vaadin:car"
          color="#2196F3"
          subtitle="Total en el sistema"
        />
        <StatCard
          title="Marcas Disponibles"
          value={stats.totalBrands}
          icon="vaadin:factory"
          color="#4CAF50"
          subtitle="Catálogo de marcas"
        />
      </HorizontalLayout>

      {/* Contenido principal */}
<HorizontalLayout 
  style={{ 
    gap: '24px',
    alignItems: 'flex-start',
    flexWrap: 'wrap'
  }}
>
  <div style={{ flex: 1 }}>
    <RecentVehicles vehicles={recentVehicles} />
  </div>
  <div style={{ flex: 1 }}>
    <RecentBrands brands={recentBrands} />
  </div>
</HorizontalLayout>

    </div>
  );
}
