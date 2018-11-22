using System.Threading.Tasks;

namespace SiWatchApp.Queue
{
    public interface IQueue
    {
        Task Put(object item);
        Task<object> Get();
    }
}
