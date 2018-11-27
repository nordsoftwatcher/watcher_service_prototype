using System;
using System.Threading.Tasks;
using SiWatchApp.Models;

namespace SiWatchApp.Queue
{
    public interface IPriorityQueue<T> : IQueue<T> where T:class
    {
        Task Put(T item, Priority priority);
    }
}
