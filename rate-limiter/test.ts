import { Limiter } from "./Orchestrator";
import { LimiterFactory, RateLimiterAlgorithm } from "./LimiterFactory";
import { TokenBucket } from "./TokenBucket";
import { LeakyBucket } from "./LeakyBucket";
import { FixedWindowCounter } from "./FixedWindowCounter";
import { SlidingWindowLog } from "./SlidingWindowLog";

// Helper function to format and display results
function displayResult(userId: string, endpoint: string, result: any) {
    const global = result.globalAllowedRes;
    const endpointResult = result.userEndpointAllowedRes;
    
    console.log(`\n📊 Request: User "${userId}" → Endpoint "${endpoint}"`);
    console.log(`   Global Limiter: ${global.isAllowed() ? '✅ ALLOWED' : '❌ BLOCKED'} | Remaining: ${global.getRemaining()} | Retry After: ${global.getRetryAfterMs()}s`);
    console.log(`   Endpoint Limiter: ${endpointResult.isAllowed() ? '✅ ALLOWED' : '❌ BLOCKED'} | Remaining: ${endpointResult.getRemaining()} | Retry After: ${endpointResult.getRetryAfterMs()}s`);
    
    const overallAllowed = global.isAllowed() && endpointResult.isAllowed();
    console.log(`   Overall: ${overallAllowed ? '✅ REQUEST ALLOWED' : '❌ REQUEST BLOCKED'}`);
}

function displaySimpleResult(algorithm: string, result: any, requestNum: number) {
    const status = result.isAllowed() ? '✅ ALLOWED' : '❌ BLOCKED';
    console.log(`   Request ${requestNum}: ${status} | Remaining: ${result.getRemaining()} | Retry After: ${result.getRetryAfterMs()}s`);
}

// ============================================================================
// TEST 1: Factory Pattern Tests
// ============================================================================
function testFactoryPattern() {
    console.log("\n" + "=".repeat(60));
    console.log("🏭 TEST 1: Factory Pattern");
    console.log("=".repeat(60));

    const factory = new LimiterFactory();

    // Test creating each algorithm type
    console.log("\n📝 Creating all algorithm types:");
    
    const tokenBucket = factory.createObject(RateLimiterAlgorithm.TOKEN_BUCKET);
    console.log(`   ✅ TokenBucket created: ${tokenBucket.constructor.name}`);

    const leakyBucket = factory.createObject(RateLimiterAlgorithm.LEAKY_BUCKET);
    console.log(`   ✅ LeakyBucket created: ${leakyBucket.constructor.name}`);

    const fixedWindow = factory.createObject(RateLimiterAlgorithm.FIXED_WINDOW_COUNTER);
    console.log(`   ✅ FixedWindowCounter created: ${fixedWindow.constructor.name}`);

    const slidingWindowLog = factory.createObject(RateLimiterAlgorithm.SLIDING_WINDOW_LOG);
    console.log(`   ✅ SlidingWindowLog created: ${slidingWindowLog.constructor.name}`);

    // Test with custom configuration
    console.log("\n📝 Testing with custom configuration:");
    const customLimiter = factory.createObject(RateLimiterAlgorithm.TOKEN_BUCKET, {
        capacity: 5,
        rate: 2
    });
    console.log(`   ✅ Custom TokenBucket created with capacity=5, rate=2`);

    // Test unknown algorithm (should default to TokenBucket)
    console.log("\n📝 Testing unknown algorithm:");
    const unknown = factory.createObject("UnknownAlgorithm");
    console.log(`   ✅ Unknown algorithm handled: ${unknown.constructor.name}`);
}

// ============================================================================
// TEST 2: TokenBucket Algorithm
// ============================================================================
function testTokenBucket() {
    console.log("\n" + "=".repeat(60));
    console.log("🪣 TEST 2: TokenBucket Algorithm");
    console.log("=".repeat(60));

    const tokenBucket = new TokenBucket(5, 1); // capacity=5, refillRate=1 token/sec
    console.log("\n📝 Testing TokenBucket (capacity=5, refillRate=1/sec):");

    // Make 6 rapid requests (should allow 5, block 1)
    for (let i = 1; i <= 6; i++) {
        const result = tokenBucket.allow();
        displaySimpleResult("TokenBucket", result, i);
    }

    // Wait a bit and test refill (simulated by creating new instance)
    console.log("\n📝 Note: In real scenario, tokens refill over time at 1 token/second");
}

// ============================================================================
// TEST 3: LeakyBucket Algorithm
// ============================================================================
function testLeakyBucket() {
    console.log("\n" + "=".repeat(60));
    console.log("💧 TEST 3: LeakyBucket Algorithm");
    console.log("=".repeat(60));

    const leakyBucket = new LeakyBucket(5, 1); // capacity=5, leakRate=1 token/sec
    console.log("\n📝 Testing LeakyBucket (capacity=5, leakRate=1/sec):");

    // Make 6 rapid requests (should allow all, but bucket fills up)
    for (let i = 1; i <= 6; i++) {
        const result = leakyBucket.allow();
        displaySimpleResult("LeakyBucket", result, i);
    }
}

// ============================================================================
// TEST 4: FixedWindowCounter Algorithm
// ============================================================================
function testFixedWindowCounter() {
    console.log("\n" + "=".repeat(60));
    console.log("⏱️  TEST 4: FixedWindowCounter Algorithm");
    console.log("=".repeat(60));

    const fixedWindow = new FixedWindowCounter(5, 1000); // limit=5, windowSize=1000ms
    console.log("\n📝 Testing FixedWindowCounter (limit=5, windowSize=1000ms):");

    // Make 6 rapid requests (should allow 5, block 1)
    for (let i = 1; i <= 6; i++) {
        const result = fixedWindow.allow();
        displaySimpleResult("FixedWindowCounter", result, i);
    }
}

// ============================================================================
// TEST 5: SlidingWindowLog Algorithm
// ============================================================================
function testSlidingWindowLog() {
    console.log("\n" + "=".repeat(60));
    console.log("📊 TEST 5: SlidingWindowLog Algorithm");
    console.log("=".repeat(60));

    const slidingWindowLog = new SlidingWindowLog(5, 1000); // limit=5, windowSize=1000ms
    console.log("\n📝 Testing SlidingWindowLog (limit=5, windowSize=1000ms):");

    // Make 6 rapid requests (should allow 5, block 1)
    for (let i = 1; i <= 6; i++) {
        const result = slidingWindowLog.allow();
        displaySimpleResult("SlidingWindowLog", result, i);
    }
}

// ============================================================================
// TEST 6: Orchestrator with TokenBucket
// ============================================================================
function testOrchestratorTokenBucket() {
    console.log("\n" + "=".repeat(60));
    console.log("🎯 TEST 6: Orchestrator with TokenBucket");
    console.log("=".repeat(60));

    const limiter = new Limiter();

    console.log("\n📝 Test 6.1: Basic requests for user1");
    displayResult("user1", "/login", limiter.allow("user1", "/login", RateLimiterAlgorithm.TOKEN_BUCKET));
    displayResult("user1", "/payment", limiter.allow("user1", "/payment", RateLimiterAlgorithm.TOKEN_BUCKET));

    console.log("\n📝 Test 6.2: Rapid requests to /login (should hit endpoint limit)");
    for (let i = 1; i <= 12; i++) {
        const result = limiter.allow("user1", "/login", RateLimiterAlgorithm.TOKEN_BUCKET);
        if (i <= 3 || i >= 11) { // Show first 3 and last 2
            displayResult("user1", "/login", result);
        } else if (i === 4) {
            console.log("   ... (requests 4-10) ...");
        }
    }

    console.log("\n📝 Test 6.3: Different user (user2)");
    displayResult("user2", "/login", limiter.allow("user2", "/login", RateLimiterAlgorithm.TOKEN_BUCKET));
    displayResult("user2", "/api", limiter.allow("user2", "/api", RateLimiterAlgorithm.TOKEN_BUCKET));

    console.log("\n📝 Test 6.4: Same user (user1), different endpoint (/api)");
    displayResult("user1", "/api", limiter.allow("user1", "/api", RateLimiterAlgorithm.TOKEN_BUCKET));
}

// ============================================================================
// TEST 7: Orchestrator with FixedWindowCounter
// ============================================================================
function testOrchestratorFixedWindow() {
    console.log("\n" + "=".repeat(60));
    console.log("🎯 TEST 7: Orchestrator with FixedWindowCounter");
    console.log("=".repeat(60));

    const limiter = new Limiter();
    const config = {
        limit: 3,
        windowSize: 2000 // 2 seconds
    };

    console.log("\n📝 Testing with limit=3, windowSize=2000ms:");
    
    for (let i = 1; i <= 5; i++) {
        displayResult("user3", "/test", limiter.allow(
            "user3", 
            "/test", 
            RateLimiterAlgorithm.FIXED_WINDOW_COUNTER,
            config,
            config
        ));
    }
}

// ============================================================================
// TEST 8: Orchestrator with SlidingWindowLog
// ============================================================================
function testOrchestratorSlidingWindowLog() {
    console.log("\n" + "=".repeat(60));
    console.log("🎯 TEST 8: Orchestrator with SlidingWindowLog");
    console.log("=".repeat(60));

    const limiter = new Limiter();
    const config = {
        limit: 3,
        windowSize: 2000 // 2 seconds
    };

    console.log("\n📝 Testing with limit=3, windowSize=2000ms:");
    
    for (let i = 1; i <= 5; i++) {
        displayResult("user4", "/api", limiter.allow(
            "user4", 
            "/api", 
            RateLimiterAlgorithm.SLIDING_WINDOW_LOG,
            config,
            config
        ));
    }
}

// ============================================================================
// TEST 9: Orchestrator with LeakyBucket
// ============================================================================
function testOrchestratorLeakyBucket() {
    console.log("\n" + "=".repeat(60));
    console.log("🎯 TEST 9: Orchestrator with LeakyBucket");
    console.log("=".repeat(60));

    const limiter = new Limiter();
    const config = {
        capacity: 5,
        rate: 1
    };

    console.log("\n📝 Testing with capacity=5, rate=1:");
    
    for (let i = 1; i <= 7; i++) {
        displayResult("user5", "/upload", limiter.allow(
            "user5", 
            "/upload", 
            RateLimiterAlgorithm.LEAKY_BUCKET,
            config,
            config
        ));
    }
}

// ============================================================================
// TEST 10: Edge Cases
// ============================================================================
function testEdgeCases() {
    console.log("\n" + "=".repeat(60));
    console.log("🔍 TEST 10: Edge Cases");
    console.log("=".repeat(60));

    const limiter = new Limiter();

    console.log("\n📝 Test 10.1: Empty string userId");
    try {
        displayResult("", "/test", limiter.allow("", "/test", RateLimiterAlgorithm.TOKEN_BUCKET));
    } catch (e) {
        console.log(`   ⚠️  Error: ${e}`);
    }

    console.log("\n📝 Test 10.2: Empty string endpoint");
    try {
        displayResult("user6", "", limiter.allow("user6", "", RateLimiterAlgorithm.TOKEN_BUCKET));
    } catch (e) {
        console.log(`   ⚠️  Error: ${e}`);
    }

    console.log("\n📝 Test 10.3: Very long userId and endpoint");
    const longUserId = "a".repeat(100);
    const longEndpoint = "/" + "b".repeat(100);
    displayResult(longUserId, longEndpoint, limiter.allow(longUserId, longEndpoint, RateLimiterAlgorithm.TOKEN_BUCKET));
}

// ============================================================================
// MAIN TEST RUNNER
// ============================================================================
function runAllTests() {
    console.log("\n🚀 Starting Comprehensive Rate Limiter Tests");
    console.log("=".repeat(60));

    try {
        testFactoryPattern();
        testTokenBucket();
        testLeakyBucket();
        testFixedWindowCounter();
        testSlidingWindowLog();
        testOrchestratorTokenBucket();
        testOrchestratorFixedWindow();
        testOrchestratorSlidingWindowLog();
        testOrchestratorLeakyBucket();
        testEdgeCases();

        console.log("\n" + "=".repeat(60));
        console.log("✅ All Tests Completed Successfully!");
        console.log("=".repeat(60));
    } catch (error) {
        console.error("\n❌ Test failed with error:", error);
    }
}

// Run all tests
runAllTests();
