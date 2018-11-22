using System;
using System.Threading.Tasks;

namespace SiWatchApp.Queue
{
    public interface IPriorityQueue : IQueue
    {
        Task Put(object item, Priority priority);
    }
}
