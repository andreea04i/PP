import threading #import folosit pentru a lucra cu firele de executie
from queue import Queue

class ThreadPool:
    def __init__(self, num_threads):
        self.num_threads = num_threads
        self.tasks = Queue() #coada de sarcini
        self.threads = [] #lista cu thread-uri     
        self.shutdown_flag = threading.Event() #flag pentru oprirea thread-urilor

        for _ in range(num_threads):
            thread = threading.Thread(target=self.worker) #se creeaza un thread ce ruleaza functia worker
            thread.start()
            self.threads.append(thread) #apoi se adauga thread-ul creat in lista

    def worker(self):
        while not self.shutdown_flag.is_set(): #cat timp flag-ul nu este setat thread-urile lucreaza
            try:
                func, args, result, index = self.tasks.get(timeout=0.1)
                result[index] = func(*args)
                self.tasks.task_done()
            except:
                continue

    def map(self, func, data):
        result = [None] * len(data) #se initializeaza lista cu rezultate 
        chunks = self.split_work(data) 
        #se creeaza sarcini si se adauga in coada
        for index, chunk in enumerate(chunks):
            for i, item in enumerate(chunk):
                self.tasks.put((func, (item,), result, sum(len(c) for c in chunks[:index]) + i))
        self.tasks.join()
        return result

    def split_work(self, data):
        total = len(data)
        base = total // self.num_threads #se impart datele pe thread-uri in mod egal
        rest = total % self.num_threads #restul ce ramane se imparte in mod egal la inceput
        chunks = []
        start = 0
        for i in range(self.num_threads):
            end = start + base + (1 if i < rest else 0)
            chunks.append(data[start:end])
            start = end
        return chunks

    def join(self):
        self.tasks.join() #asteapta ca toate sarcinile sa se termine

    def terminate(self):
        self.shutdown_flag.set() #seteaza flag-ul de oprire
        for thread in self.threads:
            thread.join() #asteapta oprirea tuturor thread-urilor

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        self.terminate()

if __name__ == '__main__':
    def sum_of_digits(x): #functie ce calculeaza suma cifrelor unui numar
        return sum(int(digit) for digit in str(x))


    data = list(range(1, 20)) #lista de date 
    with ThreadPool(5) as pool: #se creeaza un pool cu 5 fire de executie
        results = pool.map(sum_of_digits, data) #se ruleaza functia pe fiecare element
    print(results)
