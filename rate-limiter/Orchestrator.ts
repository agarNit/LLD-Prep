import { IRateLimiter } from "./IRateLimiter";
import { LimiterFactory, RateLimiterConfig } from "./LimiterFactory";

export class Limiter {
    private globalLimiters: Map<string, IRateLimiter>;
    private userEndpointLimiters: Map<string, IRateLimiter>;
    private factory = new LimiterFactory();

    constructor() {
        this.globalLimiters = new Map();
        this.userEndpointLimiters = new Map();
    }

    public allow(
        userId: string, 
        endpoint: string, 
        algorithm: string,
        globalConfig?: RateLimiterConfig,
        endpointConfig?: RateLimiterConfig
    ): any {
        // Create or get global limiter for user (algorithm is set on first request and persists)
        if (!this.globalLimiters.has(userId)) {
            this.globalLimiters.set(userId, this.factory.createObject(algorithm, globalConfig));
        }

        // Create or get endpoint-specific limiter (algorithm is set on first request and persists)
        const userEndpoint = `${userId}:${endpoint}`;
        if (!this.userEndpointLimiters.has(userEndpoint)) {
            this.userEndpointLimiters.set(userEndpoint, this.factory.createObject(algorithm, endpointConfig));
        }

        const globalAllowed = this.globalLimiters.get(userId)!.allow();
        const userEndpointAllowed = this.userEndpointLimiters.get(userEndpoint)!.allow();

        const res = {
            "globalAllowedRes": globalAllowed,
            "userEndpointAllowedRes": userEndpointAllowed
        }
        return res;
    }
}