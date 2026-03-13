import { IRateLimiter } from "./IRateLimiter";
import { Result } from "./Result";

export class LeakyBucket implements IRateLimiter {
    private capacity: number;
    private leakRate: number;
    private tokens: number = 0;
    private lastLeakTime: number;

    constructor(capacity: number, leakRate: number) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.lastLeakTime = Date.now();
    }

    public allow(): Result {
        this.leak();
        if (this.tokens < this.capacity ) {
            this.tokens++;
            return new Result(true, 0, this.capacity - this.tokens);
        }
        // Calculate retryAfter: time until one token leaks (in seconds)
        const retryAfter = 1 / this.leakRate;
        return new Result(false, retryAfter, 0); 
    }

    private leak(): void{
        const now = Date.now();
        const timePassed = (now - this.lastLeakTime) / 1000;
        const tokensUsed = timePassed * this.leakRate;
        this.tokens = Math.max(0, this.tokens - tokensUsed);
        this.lastLeakTime = now;
    }
}