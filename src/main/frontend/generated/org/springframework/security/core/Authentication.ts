import type GrantedAuthority_1 from "./GrantedAuthority.js";
interface Authentication {
    principal: unknown;
    credentials: unknown;
    authorities?: Array<GrantedAuthority_1 | undefined>;
    authenticated: boolean;
    details: unknown;
}
export default Authentication;
