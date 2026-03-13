import { IRateLimiter } from "./IRateLimiter";
import { Result } from "./Result";

export class FixedWindowCounter implements IRateLimiter {
    private limit: number;
    private windowSize: number;
    private windowStart: number;
    private count: number = 0;

    constructor(limit: number, windowSize: number) {
        this.limit = limit;
        this.windowSize = windowSize;
        this.windowStart = Date.now();
    }

    public allow(): Result {
        const now = Date.now();
        if (now - this.windowStart > this.windowSize) {
            this.windowStart = now;
            this.count = 0;
        }

        if (this.count < this.limit) {
            this.count++;
            return new Result(true, 0, this.limit-this.count);
        }
        // Calculate retryAfter: time until next window starts (in seconds)
        const retryAfter = (this.windowSize - (now - this.windowStart)) / 1000;
        return new Result(false, Math.max(0, retryAfter), 0);
    }
}