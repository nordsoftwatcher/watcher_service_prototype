/// <reference path="./custom.d.ts" />
// tslint:disable
/**
 * SiWatch.Backend.RouteService
 * RouteService REST API
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import * as url from "url";
import * as portableFetch from "portable-fetch";
import { Configuration } from "./configuration";

const BASE_PATH = "http://192.168.88.33:8580/route_service".replace(/\/+$/, "");

/**
 *
 * @export
 */
export const COLLECTION_FORMATS = {
    csv: ",",
    ssv: " ",
    tsv: "\t",
    pipes: "|",
};

/**
 *
 * @export
 * @interface FetchAPI
 */
export interface FetchAPI {
    (url: string, init?: any): Promise<Response>;
}

/**
 *  
 * @export
 * @interface FetchArgs
 */
export interface FetchArgs {
    url: string;
    options: any;
}

/**
 * 
 * @export
 * @class BaseAPI
 */
export class BaseAPI {
    protected configuration: Configuration | undefined;

    constructor(configuration?: Configuration, protected basePath: string = BASE_PATH, protected fetch: FetchAPI = portableFetch) {
        if (configuration) {
            this.configuration = configuration;
            this.basePath = configuration.basePath || this.basePath;
        }
    }
};

/**
 * 
 * @export
 * @class RequiredError
 * @extends {Error}
 */
export class RequiredError extends Error {
    name: "RequiredError" = "RequiredError";
    constructor(public field: string, msg?: string) {
        super(msg);
    }
}

/**
 * 
 * @export
 * @interface CheckPointDto
 */
export interface CheckPointDto {
    /**
     * 
     * @type {string}
     * @memberof CheckPointDto
     */
    address?: string;
    /**
     * 
     * @type {Date}
     * @memberof CheckPointDto
     */
    arrivalTime?: Date;
    /**
     * 
     * @type {Date}
     * @memberof CheckPointDto
     */
    departureTime?: Date;
    /**
     * 
     * @type {string}
     * @memberof CheckPointDto
     */
    description?: string;
    /**
     * 
     * @type {number}
     * @memberof CheckPointDto
     */
    factTime?: number;
    /**
     * ID объекта
     * @type {number}
     * @memberof CheckPointDto
     */
    id?: number;
    /**
     * 
     * @type {number}
     * @memberof CheckPointDto
     */
    latitude?: number;
    /**
     * 
     * @type {number}
     * @memberof CheckPointDto
     */
    longitude?: number;
    /**
     * 
     * @type {string}
     * @memberof CheckPointDto
     */
    name?: string;
    /**
     * 
     * @type {number}
     * @memberof CheckPointDto
     */
    order?: number;
    /**
     * 
     * @type {number}
     * @memberof CheckPointDto
     */
    planTime?: number;
    /**
     * 
     * @type {number}
     * @memberof CheckPointDto
     */
    radius?: number;
}

/**
 * 
 * @export
 * @interface CreateCheckPointInput
 */
export interface CreateCheckPointInput {
    /**
     * 
     * @type {string}
     * @memberof CreateCheckPointInput
     */
    address?: string;
    /**
     * 
     * @type {Date}
     * @memberof CreateCheckPointInput
     */
    arrivalTime?: Date;
    /**
     * 
     * @type {Date}
     * @memberof CreateCheckPointInput
     */
    departureTime?: Date;
    /**
     * 
     * @type {string}
     * @memberof CreateCheckPointInput
     */
    description?: string;
    /**
     * 
     * @type {number}
     * @memberof CreateCheckPointInput
     */
    latitude: number;
    /**
     * 
     * @type {number}
     * @memberof CreateCheckPointInput
     */
    longitude: number;
    /**
     * 
     * @type {string}
     * @memberof CreateCheckPointInput
     */
    name?: string;
    /**
     * 
     * @type {number}
     * @memberof CreateCheckPointInput
     */
    order: number;
    /**
     * 
     * @type {number}
     * @memberof CreateCheckPointInput
     */
    planTime?: number;
    /**
     * 
     * @type {number}
     * @memberof CreateCheckPointInput
     */
    radius: number;
}

/**
 * 
 * @export
 * @interface CreateRouteInput
 */
export interface CreateRouteInput {
    /**
     * 
     * @type {Array<CreateCheckPointInput>}
     * @memberof CreateRouteInput
     */
    checkPoints?: Array<CreateCheckPointInput>;
    /**
     * 
     * @type {Array<CreateRoutePointInput>}
     * @memberof CreateRouteInput
     */
    points?: Array<CreateRoutePointInput>;
    /**
     * 
     * @type {number}
     * @memberof CreateRouteInput
     */
    supervisorId: number;
}

/**
 * 
 * @export
 * @interface CreateRoutePointInput
 */
export interface CreateRoutePointInput {
    /**
     * 
     * @type {number}
     * @memberof CreateRoutePointInput
     */
    latitude: number;
    /**
     * 
     * @type {number}
     * @memberof CreateRoutePointInput
     */
    longitude: number;
    /**
     * 
     * @type {number}
     * @memberof CreateRoutePointInput
     */
    order: number;
}

/**
 * 
 * @export
 * @interface RouteDto
 */
export interface RouteDto {
    /**
     * 
     * @type {Array<CheckPointDto>}
     * @memberof RouteDto
     */
    checkPoints?: Array<CheckPointDto>;
    /**
     * ID объекта
     * @type {number}
     * @memberof RouteDto
     */
    id?: number;
    /**
     * 
     * @type {Array<RoutePointDto>}
     * @memberof RouteDto
     */
    routePoints?: Array<RoutePointDto>;
    /**
     * 
     * @type {string}
     * @memberof RouteDto
     */
    status?: StatusEnum;
    /**
     * 
     * @type {number}
     * @memberof RouteDto
     */
    supervisorId?: number;
}

    /**
     * @export
     * @enum {string}
     */
    export enum StatusEnum {
        NOTSTARTED = 'NOT_STARTED',
        INPROGRESS = 'IN_PROGRESS',
        DONE = 'DONE'
    }


/**
 * 
 * @export
 * @interface RoutePointDto
 */
export interface RoutePointDto {
    /**
     * ID объекта
     * @type {number}
     * @memberof RoutePointDto
     */
    id?: number;
    /**
     * 
     * @type {number}
     * @memberof RoutePointDto
     */
    latitude?: number;
    /**
     * 
     * @type {number}
     * @memberof RoutePointDto
     */
    longitude?: number;
    /**
     * 
     * @type {number}
     * @memberof RoutePointDto
     */
    order?: number;
}


/**
 * RouteApiApi - fetch parameter creator
 * @export
 */
export const RouteApiApiFetchParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @summary Создание маршрута
         * @param {CreateRouteInput} createRouteInput createRouteInput
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createRouteUsingPOST(createRouteInput: CreateRouteInput, options: any = {}): FetchArgs {
            // verify required parameter 'createRouteInput' is not null or undefined
            if (createRouteInput === null || createRouteInput === undefined) {
                throw new RequiredError('createRouteInput','Required parameter createRouteInput was null or undefined when calling createRouteUsingPOST.');
            }
            const localVarPath = `/api/routes`;
            const localVarUrlObj = url.parse(localVarPath, true);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }
            const localVarRequestOptions = Object.assign({ method: 'POST' }, baseOptions, options);
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            localVarHeaderParameter['Content-Type'] = 'application/json';

            localVarUrlObj.query = Object.assign({}, localVarUrlObj.query, localVarQueryParameter, options.query);
            // fix override query string Detail: https://stackoverflow.com/a/7517673/1077943
            delete localVarUrlObj.search;
            localVarRequestOptions.headers = Object.assign({}, localVarHeaderParameter, options.headers);
            const needsSerialization = (<any>"CreateRouteInput" !== "string") || localVarRequestOptions.headers['Content-Type'] === 'application/json';
            localVarRequestOptions.body =  needsSerialization ? JSON.stringify(createRouteInput || {}) : (createRouteInput || "");

            return {
                url: url.format(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary Получение информации о маршруте
         * @param {number} routeId routeId
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getRouteUsingGET(routeId: number, options: any = {}): FetchArgs {
            // verify required parameter 'routeId' is not null or undefined
            if (routeId === null || routeId === undefined) {
                throw new RequiredError('routeId','Required parameter routeId was null or undefined when calling getRouteUsingGET.');
            }
            const localVarPath = `/api/routes/{routeId}`
                .replace(`{${"routeId"}}`, encodeURIComponent(String(routeId)));
            const localVarUrlObj = url.parse(localVarPath, true);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }
            const localVarRequestOptions = Object.assign({ method: 'GET' }, baseOptions, options);
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            localVarUrlObj.query = Object.assign({}, localVarUrlObj.query, localVarQueryParameter, options.query);
            // fix override query string Detail: https://stackoverflow.com/a/7517673/1077943
            delete localVarUrlObj.search;
            localVarRequestOptions.headers = Object.assign({}, localVarHeaderParameter, options.headers);

            return {
                url: url.format(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * RouteApiApi - functional programming interface
 * @export
 */
export const RouteApiApiFp = function(configuration?: Configuration) {
    return {
        /**
         * 
         * @summary Создание маршрута
         * @param {CreateRouteInput} createRouteInput createRouteInput
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createRouteUsingPOST(createRouteInput: CreateRouteInput, options?: any): (fetch?: FetchAPI, basePath?: string) => Promise<RouteDto> {
            const localVarFetchArgs = RouteApiApiFetchParamCreator(configuration).createRouteUsingPOST(createRouteInput, options);
            return (fetch: FetchAPI = portableFetch, basePath: string = BASE_PATH) => {
                return fetch(basePath + localVarFetchArgs.url, localVarFetchArgs.options).then((response) => {
                    if (response.status >= 200 && response.status < 300) {
                        return response.json();
                    } else {
                        throw response;
                    }
                });
            };
        },
        /**
         * 
         * @summary Получение информации о маршруте
         * @param {number} routeId routeId
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getRouteUsingGET(routeId: number, options?: any): (fetch?: FetchAPI, basePath?: string) => Promise<RouteDto> {
            const localVarFetchArgs = RouteApiApiFetchParamCreator(configuration).getRouteUsingGET(routeId, options);
            return (fetch: FetchAPI = portableFetch, basePath: string = BASE_PATH) => {
                return fetch(basePath + localVarFetchArgs.url, localVarFetchArgs.options).then((response) => {
                    if (response.status >= 200 && response.status < 300) {
                        return response.json();
                    } else {
                        throw response;
                    }
                });
            };
        },
    }
};

/**
 * RouteApiApi - factory interface
 * @export
 */
export const RouteApiApiFactory = function (configuration?: Configuration, fetch?: FetchAPI, basePath?: string) {
    return {
        /**
         * 
         * @summary Создание маршрута
         * @param {CreateRouteInput} createRouteInput createRouteInput
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createRouteUsingPOST(createRouteInput: CreateRouteInput, options?: any) {
            return RouteApiApiFp(configuration).createRouteUsingPOST(createRouteInput, options)(fetch, basePath);
        },
        /**
         * 
         * @summary Получение информации о маршруте
         * @param {number} routeId routeId
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getRouteUsingGET(routeId: number, options?: any) {
            return RouteApiApiFp(configuration).getRouteUsingGET(routeId, options)(fetch, basePath);
        },
    };
};

/**
 * RouteApiApi - object-oriented interface
 * @export
 * @class RouteApiApi
 * @extends {BaseAPI}
 */
export class RouteApiApi extends BaseAPI {
    /**
     * 
     * @summary Создание маршрута
     * @param {CreateRouteInput} createRouteInput createRouteInput
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof RouteApiApi
     */
    public createRouteUsingPOST(createRouteInput: CreateRouteInput, options?: any) {
        return RouteApiApiFp(this.configuration).createRouteUsingPOST(createRouteInput, options)(this.fetch, this.basePath);
    }

    /**
     * 
     * @summary Получение информации о маршруте
     * @param {number} routeId routeId
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof RouteApiApi
     */
    public getRouteUsingGET(routeId: number, options?: any) {
        return RouteApiApiFp(this.configuration).getRouteUsingGET(routeId, options)(this.fetch, this.basePath);
    }

}

