import asyncio

async def factorial(n):
    if n <= 1:
        return 1
    else:
        return n * await factorial(n - 1)

async def worker(name, queue):
    while True:
        n = await queue.get()
        if n is None:
            queue.task_done()
            break
        result = await factorial(n)
        print(f"Worker {name}: factorial({n}) = {result}")
        queue.task_done()

async def main():
    queue = asyncio.Queue()

    for n in [5, 7, 3, 4]:
        await queue.put(n)

    workers = [asyncio.create_task(worker(i, queue)) for i in range(4)]

    await queue.join()

    for _ in range(4):
        await queue.put(None)

    await asyncio.gather(*workers)

asyncio.run(main())
