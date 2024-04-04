import { UserApi } from "./UserApi";
import { AddressApi } from "./AddressApi";
import { HubApi } from "./HubApi";
import { OrderApi } from "./OrderApi";
import { ParcelApi } from "./ParcelApi";

export const api = {
	...UserApi,
	...AddressApi,
	...HubApi,
	...OrderApi,
	...ParcelApi
}