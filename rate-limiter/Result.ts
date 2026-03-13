export class Result {
    private allowed: boolean;
    private retryAfter: number;
    private remaining: number;

    constructor(allowed: boolean, retryAfter: number, remaining: number) {
        this.allowed = allowed;
        this.retryAfter = retryAfter;
        this.remaining = remaining;
    }

    public isAllowed(): boolean {
        return this.allowed;
    }

    public getRemaining(): number {
        return this.remaining;
    }

    public getRetryAfterMs(): number {
        return this.retryAfter;
    } 
}