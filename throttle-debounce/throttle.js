function throttle(fn, delay) {
  let lastCall = 0;

  return function(...args) {
    const now = Date.now();
    if (now-lastCall >= delay) {
      lastCall = now;
      fn.apply(this, args);
    }
  }
}

const throttledLog = throttle((msg) => {
  console.log("Executed:", msg, "at", Date.now());
}, 1000);

// Rapid calls
throttledLog("First");
throttledLog("Second");
throttledLog("Third");