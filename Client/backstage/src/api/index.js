import { adminApi } from "./adminController";
import { hubApi } from "./hubController"; 

export const api = {
    ...adminApi,
    ...hubApi
}