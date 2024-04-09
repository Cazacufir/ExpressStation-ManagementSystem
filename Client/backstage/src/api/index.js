import { adminApi } from "./adminApi";
import { hubApi } from "./hubApi";
import { staffApi } from "./staffApi";
import { companyApi } from "./companyApi";
import { deliverApi } from "./deliverApi";
import { ParcelApi } from "./ParcelApi";

export const api = {
    ...adminApi,
    ...hubApi,
    ...staffApi,
    ...companyApi,
    ...deliverApi,
    ...ParcelApi
}