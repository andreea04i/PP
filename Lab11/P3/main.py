import threading
from queue import Queue

class ThreadPool:
    def __init__(self, num_threads):
        self.num_threads = num_threads
        self.tasks = Queue()
        self.threads = []
        self.shutdown_flag = threading.Event()

        for _ in range(num_threads):
            thread = threading.Thread(target=self.worker)
            thread.start()
            self.threads.append(thread)

    def worker(self):
        while not self.shutdown_flag.is_set():
            try:
                func, args, result, index = self.tasks.get(timeout=0.1)
                result[index] = func(*args)
                self.tasks.task_done()
            except:
                continue

    def map(self, func, data):
        result = [None] * len(data)
        chunks = self.split_work(data)
        for index, chunk in enumerate(chunks):
            for i, item in enumerate(chunk):
                self.tasks.put((func, (item,), result, sum(len(c) for c in chunks[:index]) + i))
        self.tasks.join()
        return result

    def split_work(self, data):
        total = len(data)
        base = total // self.num_threads
        rest = total % self.num_threads
        chunks = []
        start = 0
        for i in range(self.num_threads):
            end = start + base + (1 if i < rest else 0)
            chunks.append(data[start:end])
            start = end
        return chunks

    def join(self):
        self.tasks.join()

    def terminate(self):
        self.shutdown_flag.set()
        for thread in self.threads:
            thread.join()

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        self.terminate()

if __name__ == '__main__':
    def sum_of_squares_of_digits(x):
        return sum(int(digit) for digit in str(x))


    data = list(range(1, 20))
    with ThreadPool(5) as pool:
        results = pool.map(sum_of_squares_of_digits, data)
    print(results)