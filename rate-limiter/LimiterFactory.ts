import { FixedWindowCounter } from "./FixedWindowCounter";
import { IRateLimiter } from "./IRateLimiter";
import { LeakyBucket } from "./LeakyBucket";
import { SlidingWindowLog } from "./SlidingWindowLog";
import { TokenBucket } from "./TokenBucket";

export enum RateLimiterAlgorithm {
    TOKEN_BUCKET = 'TokenBucket',
    LEAKY_BUCKET = 'LeakyBucket',
    FIXED_WINDOW_COUNTER = 'FixedWindowCounter',
    SLIDING_WINDOW_LOG = 'SlidingWindowLog'
}

export interface RateLimiterConfig {
    capacity?: number;      // For TokenBucket, LeakyBucket
    rate?: number;          // For TokenBucket (refillRate), LeakyBucket (leakRate)
    limit?: number;         // For FixedWindowCounter, SlidingWindowLog
    windowSize?: number;    // For FixedWindowCounter, SlidingWindowLog (in milliseconds)
}

export class LimiterFactory {
    // Default configuration values
    private static readonly DEFAULT_CAPACITY = 100;
    private static readonly DEFAULT_RATE = 1; // tokens per second
    private static readonly DEFAULT_LIMIT = 10;
    private static readonly DEFAULT_WINDOW_SIZE = 1000; // 1 second in milliseconds

    public createObject(algorithm: string, config?: RateLimiterConfig): IRateLimiter {
        // Normalize algorithm name (case-insensitive)
        const normalizedAlgorithm = algorithm.trim();
        
        // Use provided config or defaults
        const capacity = config?.capacity ?? LimiterFactory.DEFAULT_CAPACITY;
        const rate = config?.rate ?? LimiterFactory.DEFAULT_RATE;
        const limit = config?.limit ?? LimiterFactory.DEFAULT_LIMIT;
        const windowSize = config?.windowSize ?? LimiterFactory.DEFAULT_WINDOW_SIZE;

        switch (normalizedAlgorithm) {
            case RateLimiterAlgorithm.TOKEN_BUCKET:
                return new TokenBucket(capacity, rate);
            
            case RateLimiterAlgorithm.LEAKY_BUCKET:
                return new LeakyBucket(capacity, rate);
            
            case RateLimiterAlgorithm.FIXED_WINDOW_COUNTER:
                return new FixedWindowCounter(limit, windowSize);
            
            case RateLimiterAlgorithm.SLIDING_WINDOW_LOG:
                return new SlidingWindowLog(limit, windowSize);
            
            default:
                // Default to TokenBucket if algorithm not recognized
                console.warn(`Unknown algorithm "${algorithm}", defaulting to TokenBucket`);
                return new TokenBucket(capacity, rate);
        }
    }
}