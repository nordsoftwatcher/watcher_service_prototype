using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Collections.Immutable;
using System.Linq;
using System.Threading.Tasks;
using SiWatchApp.Models;

namespace SiWatchApp.Queue
{
    public class InMemoryPriorityQueue<T> : IPriorityQueue<T> where T:class
    {
        private readonly IDictionary<Priority, ConcurrentQueue<T>> _queues;
        
        public InMemoryPriorityQueue()
        {
            _queues = new Dictionary<Priority, ConcurrentQueue<T>> {
                    { Priority.Highest, new ConcurrentQueue<T>() },
                    { Priority.High, new ConcurrentQueue<T>() },
                    { Priority.Normal, new ConcurrentQueue<T>() },
                    { Priority.Low, new ConcurrentQueue<T>() },
                    { Priority.Lowest, new ConcurrentQueue<T>() }
            }.ToImmutableDictionary();
        }

        public Task Put(T item)
        {
            if (item == null) {
                throw new ArgumentNullException("item", "Queue does not support null items");
            }

            return Put(item, Priority.Normal);
        }

        public Task<T> Get()
        {
            foreach (var queue in _queues.Keys.OrderBy(p => p).Select(p => _queues[p])) {
                if(queue.TryDequeue(out var item)) {
                    return Task.FromResult(item);
                }
            }
            return Task.FromResult(default(T));
        }

        public Task Put(T item, Priority priority)
        {
            _queues[priority].Enqueue(item);
            return Task.CompletedTask;
        }

        public int Count
        {
            get { return _queues.Values.Sum(q => q.Count); }
        }

        public void Dispose()
        {
            
        }
    }
}
