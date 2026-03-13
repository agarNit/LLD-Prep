function createScheduler(maxConcurrency) {
    let running = 0;
    const queue = [];

    function runNext() {
      if (running >= maxConcurrency) return;
      if (queue.length === 0) return;

      const task = queue.shift();
      running++;

      task().finally(() => {
        running--;
        runNext();
      });

      runNext(); // try to fill more slots
    }

    return function schedule(task) {
      queue.push(task);
      runNext();
    };
}

const scheduler = createScheduler(5);
const task = () => new Promise((resolve) => {
  console.log('task started');
  setTimeout(() => {
    console.log('task finished');
    resolve();
  }, 1000);
});

scheduler(task);
scheduler(task);