import { IRateLimiter } from "./IRateLimiter";
import { Result } from "./Result";

export class TokenBucket implements IRateLimiter {
    private capacity: number;
    private refillRate: number;
    private tokens: number;
    private lastRefilledTime: number;

    constructor(capacity: number, refillRate: number) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefilledTime = Date.now();
    }

    public allow(): Result {
        this.refill();
        if (this.tokens > 0 ) {
            this.tokens -= 1;
            return new Result(true, 0, this.tokens);
        }
        const retryAfter = 1 / this.refillRate;
        return new Result(false, retryAfter, 0); 
    }

    private refill(): void{
        const now = Date.now();
        const timePassed = (now - this.lastRefilledTime) / 1000;
        const tokensToAdd = timePassed * this.refillRate;
        this.tokens = Math.min(this.capacity, this.tokens + tokensToAdd);
        this.lastRefilledTime = now;
    }
}