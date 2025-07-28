import { Button, LoginForm, LoginFormLoginEvent, LoginOverlay, PasswordField, TextField, VerticalLayout } from "@vaadin/react-components";
import { ViewConfig } from "@vaadin/hilla-file-router/types.js";
import { redirect, useNavigate, useSearchParams} from "react-router";
import {CuentaService} from "Frontend/generated/endpoints";
import { Notification } from '@vaadin/react-components/Notification';
import { useEffect } from "react";
import { useSignal } from "@vaadin/hilla-react-signals";
import { useAuth, AuthProvider, isLogin } from "Frontend/security/auth";

export const config: ViewConfig = {
    skipLayouts: true,
    menu: {
        exclude: true
    }
}

export default function LoginView() {
    console.log("LOGIN");
    const navigate = useNavigate();
    useEffect(() => {
        isLogin().then(data => {
            if(data == true) 
                navigate("/")
            console.log(data+" -- ")
        }

        );
    }, []);            
    const {state, login} = useAuth();
    const [searchParams] = useSearchParams()
    const hasError = useSignal(false);
    const errores = searchParams.has("error");

    const i18n = {
        header: {
            title: "Taller de Autos",
            description: "Inicia sesión para continuar"
        },
        form:{
            title: "Iniciar sesión",
            username: "Correo electrónico",
            password: "Clave",
            submit: "Ingresar",
            forgotPassword: "¿Olvidaste tu clave?",
        },
        errorMessages: {
            title: "Error de inicio de sesión",
            message: "Usuario o clave incorrectos",
            username: "Usuario Incorrecto",
            password: "Clave Incorrecta",
        },
        additionalInformation: "Por su vehiculo",
        errorMessage: {
            title: "Error de autenticación",
            message: "Usuario o contraseña incorrectos"
        }
};
useEffect(() => {
    CuentaService.createRoles().then(data =>
        hasError.value = false
    );
}, []);
const email = useSignal(' ');
const password = useSignal(' ');
return (
    <main className="flex justify-center items-center w-full h-full" style={{
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      minHeight: '100vh',
      position: 'relative'
    }}>
      {/* Elementos decorativos */}
      <div style={{
        position: 'absolute',
        top: '10%',
        left: '10%',
        width: '200px',
        height: '200px',
        background: 'rgba(255,255,255,0.1)',
        borderRadius: '50%',
        animation: 'float 6s ease-in-out infinite'
      }} />
      <div style={{
        position: 'absolute',
        bottom: '10%',
        right: '10%',
        width: '150px',
        height: '150px',
        background: 'rgba(255,255,255,0.1)',
        borderRadius: '50%',
        animation: 'float 8s ease-in-out infinite reverse'
      }} />
      
      {/* Contenedor principal */}
      <div style={{
        background: 'white',
        borderRadius: '20px',
        padding: '40px',
        boxShadow: '0 20px 40px rgba(0,0,0,0.1)',
        maxWidth: '400px',
        width: '90%',
        position: 'relative',
        zIndex: 1
      }}>
        {/* Header personalizado */}
        <div style={{
          textAlign: 'center',
          marginBottom: '30px'
        }}>
          <div style={{
            fontSize: '48px',
            marginBottom: '16px'
          }}>
            🚗
          </div>
          <h1 style={{
            fontSize: '28px',
            fontWeight: 'bold',
            color: '#333',
            margin: '0 0 8px 0'
          }}>
            Taller de Autos
          </h1>
          <p style={{
            fontSize: '14px',
            color: '#666',
            margin: 0
          }}>
            Sistema de Gestión Automotriz
          </p>
        </div>

        <LoginForm 
          i18n={i18n} 
          error={errores} 
          noForgotPassword
          style={{
            border: 'none',
            boxShadow: 'none'
          }}
            onErrorChanged={(event) =>{
                console.log(event);
                hasError.value = event.detail.value;
            }}
            onLogin={
                async({detail: { username, password }}) => {
                    console.log(username + " " + password);
                    CuentaService.login(username, password).then(async function (data) {
                        console.log(data);
                        if(data?.estado == 'false'){
                            Notification.show("Ingreso Fallido", { duration: 3000, position: 'bottom-end', theme: 'error' });
                            hasError.value = Boolean("true");
                            navigate('/login?error');
                        } else {
                            const {error} = await login(username, password);
                            hasError.value = Boolean(error);
                            const dato = await CuentaService.isLogin();
                          Notification.show("Ingreso Exitoso", { duration: 3000, position: 'bottom-end', theme: 'success' });
                            console.log(dato);
                            window.location.reload();
                            navigate("/", {replace: true});
                    }
                    });
                }
            }
        />
      </div>

      <style>{`
        @keyframes float {
          0%, 100% { transform: translateY(0px); }
          50% { transform: translateY(-20px); }
        }
      `}</style>
        </main>
    );
}
        
