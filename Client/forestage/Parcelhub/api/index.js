import { UserApi } from "./UserApi";
import { AddressApi } from "./AddressApi";

export const api = {
	...UserApi,
	...AddressApi
}