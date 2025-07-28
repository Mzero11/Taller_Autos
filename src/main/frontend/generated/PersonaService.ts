import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
async function listaPersonas_1(init?: EndpointRequestInit_1): Promise<Array<Record<string, unknown> | undefined> | undefined> { return client_1.call("PersonaService", "listaPersonas", {}, init); }
async function save_1(usuario: string | undefined, correo: string | undefined, clave: string | undefined, edad: number | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("PersonaService", "save", { usuario, correo, clave, edad }, init); }
export { listaPersonas_1 as listaPersonas, save_1 as save };
