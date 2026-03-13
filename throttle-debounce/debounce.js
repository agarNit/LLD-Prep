function debounce(fn, delay) {
  let timer;

  return function(...args) {
    clearTimeout(timer);
    timer = setTimeout(() => {
      fn.apply(this, args);
    }, delay);
  }
}

const debouncedLog = debounce((msg) => {
  console.log("Executed:", msg, "at", Date.now());
}, 1000);

// Rapid calls
debouncedLog("First");
debouncedLog("Second");
debouncedLog("Third");