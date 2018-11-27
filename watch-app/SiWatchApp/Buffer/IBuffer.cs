using System;
using System.Collections.Generic;
using SiWatchApp.Models;

namespace SiWatchApp.Buffer
{
    public interface IBuffer<T> : IDisposable where T : class
    {
        void Append(T item, Priority priority);
        void AppendAll(IEnumerable<T> items, Priority priority);
        T Take();
        ICollection<T> Take(int count);
        IBlock<T> Get(int count);
        int Count { get; }
    }
}
