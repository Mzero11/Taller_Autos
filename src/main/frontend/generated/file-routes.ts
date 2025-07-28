import { createRoute as createRoute_1 } from "@vaadin/hilla-file-router/runtime.js";
import type { AgnosticRoute as AgnosticRoute_1 } from "@vaadin/hilla-file-router/types.js";
import * as Page_1 from "../views/@index.js";
import * as Layout_1 from "../views/@layout.js";
import * as Page_2 from "../views/factura-list.js";
import * as Page_3 from "../views/login.js";
import * as Page_4 from "../views/marca-list.js";
import * as Page_5 from "../views/servicio-list.js";
import * as Page_6 from "../views/vehiculo-list.js";
const routes: readonly AgnosticRoute_1[] = [
    createRoute_1("", Layout_1, [
        createRoute_1("", Page_1),
        createRoute_1("factura-list", Page_2),
        createRoute_1("login", Page_3),
        createRoute_1("marca-list", Page_4),
        createRoute_1("servicio-list", Page_5),
        createRoute_1("vehiculo-list", Page_6)
    ])
];
export default routes;
