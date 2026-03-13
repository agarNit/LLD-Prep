import { IRateLimiter } from "./IRateLimiter";
import { Result } from "./Result";

export class SlidingWindowLog implements IRateLimiter {
    private limit: number;
    private timeStamps: number[] = [];
    private windowSize: number;

    constructor(limit: number, windowSize: number) {
        this.limit = limit;
        this.windowSize = windowSize;
    }

    public allow(): Result {
        const now = Date.now();
        this.timeStamps = this.timeStamps.filter((ts) => now - ts < this.windowSize);
        
        if (this.timeStamps.length < this.limit) {
            this.timeStamps.push(now);
            return new Result(true, 0, this.limit - this.timeStamps.length);
        }
        
        // Calculate retryAfter: time until oldest request expires from the window
        // The oldest timestamp will be the first one (assuming we maintain sorted order)
        // Or we can find the minimum timestamp
        if (this.timeStamps.length > 0) {
            const oldestTimestamp = Math.min(...this.timeStamps);
            const timeUntilOldestExpires = this.windowSize - (now - oldestTimestamp);
            return new Result(false, Math.max(0, timeUntilOldestExpires) / 1000, 0);
        }
        
        return new Result(false, this.windowSize / 1000, 0);
    }
}