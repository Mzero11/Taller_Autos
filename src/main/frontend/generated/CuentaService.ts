import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
import type Authentication_1 from "./org/springframework/security/core/Authentication.js";
async function createRoles_1(init?: EndpointRequestInit_1): Promise<Record<string, string | undefined> | undefined> { return client_1.call("CuentaService", "createRoles", {}, init); }
async function getAuthentication_1(init?: EndpointRequestInit_1): Promise<Authentication_1 | undefined> { return client_1.call("CuentaService", "getAuthentication", {}, init); }
async function isLogin_1(init?: EndpointRequestInit_1): Promise<boolean | undefined> { return client_1.call("CuentaService", "isLogin", {}, init); }
async function login_1(email: string | undefined, password: string | undefined, init?: EndpointRequestInit_1): Promise<Record<string, unknown> | undefined> { return client_1.call("CuentaService", "login", { email, password }, init); }
async function logout_1(init?: EndpointRequestInit_1): Promise<Record<string, string | undefined> | undefined> { return client_1.call("CuentaService", "logout", {}, init); }
export { createRoles_1 as createRoles, getAuthentication_1 as getAuthentication, isLogin_1 as isLogin, login_1 as login, logout_1 as logout };
