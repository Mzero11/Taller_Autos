import { Outlet, useLocation, useNavigate } from 'react-router';
import { useAuth } from 'Frontend/security/auth';
import '@vaadin/icons';
import {
  AppLayout,
  Avatar,
  Icon,
  MenuBar,
  MenuBarItem,
  MenuBarItemSelectedEvent,
  ProgressBar,
  Scroller,
  SideNav,
  SideNavItem,
  HorizontalLayout,
} from '@vaadin/react-components';
import { Suspense } from 'react';
import { createMenuItems } from '@vaadin/hilla-file-router/runtime.js';
import {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {CuentaService} from 'Frontend/generated/endpoints';
import GlobalSearch from 'Frontend/components/GlobalSearch';

function Header() {
  return (
    <div className="flex p-m gap-m items-center" slot="drawer" style={{
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      color: 'white',
      borderRadius: '0 0 12px 0',
      margin: '0 -12px 12px -12px',
      padding: '20px 16px'
    }}>
      <Icon 
        icon="vaadin:car" 
        className="text-primary icon-l" 
        style={{ 
          fontSize: '32px', 
          color: 'white',
          background: 'rgba(255,255,255,0.2)',
          padding: '8px',
          borderRadius: '8px'
        }} 
      />
      <div>
        <div style={{ 
          fontSize: '18px', 
          fontWeight: 'bold',
          marginBottom: '2px'
        }}>
          🚗 Taller de Autos
        </div>
        <div style={{ 
          fontSize: '12px', 
          opacity: 0.9,
          fontWeight: '400'
        }}>
          Sistema de Gestión
        </div>
      </div>
    </div>
  );
}




function MainMenu() {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <SideNav className="mx-m" onNavigate={({ path }) => path != null && navigate(path)} location={location}>
      {createMenuItems().map(({ to, icon, title }) => (
        <SideNavItem path={to} key={to}>
          {icon && <Icon icon={icon} slot="prefix" />}
          {title}
        </SideNavItem>
      ))}
    </SideNav>
  );
}

function UserMenu() {
  const {logout} = useAuth();
  const items = [
    {
      component: (
        <div style={{ 
          display: 'flex', 
          alignItems: 'center', 
          gap: '12px',
          padding: '12px 16px',
          background: 'rgba(255,255,255,0.1)',
          borderRadius: '8px',
          margin: '8px'
        }}>
          <Avatar 
            theme="xsmall" 
            name="Mecánico" 
            colorIndex={5} 
            style={{ 
              background: 'rgba(255,255,255,0.2)',
              border: '2px solid rgba(255,255,255,0.3)'
            }} 
          /> 
          <div>
            <div style={{ 
              fontSize: '14px', 
              fontWeight: '600',
              color: 'black'
            }}>
              Perfil
            </div>
            <div style={{ 
              fontSize: '11px', 
              opacity: 0.8,
              color: 'white'
            }}>
              Administrador
            </div>
          </div>
        </div>
      ),
      children: [
        { 
          text: '👤 Mi Perfil', 
          action: () => console.log('View Profile') 
        },
        { 
          text: '⚙️ Configuración', 
          action: () => console.log('Manage Settings') 
        },
{
  text: '🚪 Cerrar sesión',
  action: logout// ¡ya no necesitas llamar al endpoint manual!
},
      ],
    },
  ];
  const onItemSelected = (event: MenuBarItemSelectedEvent) => {
    const action = (event.detail.value as any).action;
    if (action) {
      action();
    }
  };
  return (
    <div style={{
      background: 'rgba(255,255,255,0.05)',
      borderRadius: '8px',
      margin: '8px'
    }}>
      <MenuBar 
        theme="tertiary-inline" 
        items={items} 
        onItemSelected={onItemSelected} 
        style={{
          background: 'transparent',
          border: 'none'
        }}
      />
    </div>
  );
}

export const config = {
  loginRequired: true,
}

export default function MainLayout() {
  return (
    <AppLayout primarySection="drawer">
      <Header />
      <Scroller slot="drawer">
        <MainMenu />
      </Scroller>
      <UserMenu />
      
      <Suspense fallback={<ProgressBar indeterminate={true} className="m-0" />}>
        <Outlet />
      </Suspense>
    </AppLayout>
  );
}
