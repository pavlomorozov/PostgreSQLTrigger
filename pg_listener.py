import asyncio
import asyncpg

async def listen():
    conn = await asyncpg.connect("postgresql://postgres:postgres@localhost/callback")
    await conn.add_listener('hero_update', handler)
    loop = asyncio.get_running_loop()
    stop_event = asyncio.Event()

    def stop():
        stop_event.set()

    loop.add_reader(0, lambda: stop() if input().strip() == 'exit' else None)

    await stop_event.wait()
    await conn.close()

def handler(connection, pid, channel, payload):
    print("Update received:", payload)

print('Enter \'exit\' to quit.')
asyncio.run(listen())