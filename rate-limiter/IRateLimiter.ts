import { Result } from "./Result";

export interface IRateLimiter {
    allow(): Result;
}