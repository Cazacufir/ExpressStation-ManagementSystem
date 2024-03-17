import { adminApi } from "./adminController";
import { hubApi } from "./hubController";
import { staffApi } from "./staffController";
import { companyApi } from "./companyController";
import { deliverApi } from "./deliverController";

export const api = {
    ...adminApi,
    ...hubApi,
    ...staffApi,
    ...companyApi,
    ...deliverApi,
}