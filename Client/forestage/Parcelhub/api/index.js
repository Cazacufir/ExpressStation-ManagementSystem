import { UserApi } from "./UserApi";
import { AddressApi } from "./AddressApi";
import { HubApi } from "./HubApi";
import { OrderApi } from "./OrderApi";

export const api = {
	...UserApi,
	...AddressApi,
	...HubApi,
	...OrderApi
}