import { adminApi } from "./adminController";
import { hubApi } from "./hubController";
import { staffApi } from "./staffController";
import { companyApi } from "./companyController";

export const api = {
    ...adminApi,
    ...hubApi,
    ...staffApi,
    ...companyApi,
}