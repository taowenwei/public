import { writable } from "svelte/store";

export const appData = writable({ cities: [], currCity: "hello", periods: [] });
