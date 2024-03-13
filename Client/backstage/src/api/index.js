import { adminApi } from "./adminController";
import { hubApi } from "./hubController";
import { staffApi } from "./staffController";

export const api = {
    ...adminApi,
    ...hubApi,
    ...staffApi,
}