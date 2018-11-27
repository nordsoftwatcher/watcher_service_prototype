using System;
using System.Threading.Tasks;

namespace SiWatchApp.Queue
{
    public interface IQueue<T> : IDisposable where T: class
    {
        Task Put(T item);
        Task<T> Get();
        int Count { get; }
    }
}
