using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Collections.Immutable;
using System.Linq;
using System.Threading.Tasks;

namespace SiWatchApp.Queue
{
    public class InMemoryPriorityQueue : IPriorityQueue
    {
        readonly IDictionary<Priority, ConcurrentQueue<object>> _queues;

        public InMemoryPriorityQueue()
        {
            _queues = new Dictionary<Priority, ConcurrentQueue<object>> {
                    { Priority.Highest, new ConcurrentQueue<object>() },
                    { Priority.High, new ConcurrentQueue<object>() },
                    { Priority.Normal, new ConcurrentQueue<object>() },
                    { Priority.Low, new ConcurrentQueue<object>() },
                    { Priority.Lowest, new ConcurrentQueue<object>() }
            }.ToImmutableDictionary();
        }

        public Task Put(object item)
        {
            if (item == null) {
                throw new ArgumentNullException("item", "Queue does not support null items");
            }

            return Put(item, Priority.Normal);
        }

        public Task<object> Get()
        {
            foreach (var queue in _queues.Keys.OrderBy(p => p).Select(p => _queues[p])) {
                if(queue.TryDequeue(out var item)) {
                    return Task.FromResult(item);
                }
            }
            return Task.FromResult((object)null);
        }

        public Task Put(object item, Priority priority)
        {
            _queues[priority].Enqueue(item);
            return Task.CompletedTask;
        }
    }
}
