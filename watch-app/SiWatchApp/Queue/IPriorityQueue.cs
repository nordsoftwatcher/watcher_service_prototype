using System;
using System.Threading.Tasks;
using SiWatchApp.Models;

namespace SiWatchApp.Queue
{
    public interface IPriorityQueue : IQueue
    {
        Task Put(object item, Priority priority);
    }
}
