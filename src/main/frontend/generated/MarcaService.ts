import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
async function createMarca_1(modelo: string | undefined, fecha: string | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("MarcaService", "createMarca", { modelo, fecha }, init); }
async function deleteMarca_1(id: number | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("MarcaService", "deleteMarca", { id }, init); }
async function listMarca_1(init?: EndpointRequestInit_1): Promise<Array<Record<string, unknown> | undefined> | undefined> { return client_1.call("MarcaService", "listMarca", {}, init); }
async function updateMarca_1(id: number | undefined, modelo: string | undefined, fecha: string | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("MarcaService", "updateMarca", { id, modelo, fecha }, init); }
export { createMarca_1 as createMarca, deleteMarca_1 as deleteMarca, listMarca_1 as listMarca, updateMarca_1 as updateMarca };
