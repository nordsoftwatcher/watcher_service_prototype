using System;
using System.Collections.Concurrent;

namespace SiWatchApp.Utils
{
    public class MemoryRecordQueue<T> : IRecordQueue<T> where T : class
    {
        private static readonly Logger LOGGER = LoggerFactory.GetLogger("MemoryRecordQueue");

        readonly ConcurrentQueue<T> _queue = new ConcurrentQueue<T>();

        public void Put(T data)
        {
            if (data == null) {
                throw new ArgumentNullException("data");
            }


            _queue.Enqueue(data);
        }

        public T Get()
        {
            if (_queue.TryDequeue(out var data)) {
                return data;
            }
            return null;
        }
    }
}
