using System;
using System.Threading.Tasks;

namespace SiWatchApp.Services
{
    public interface IJsonStorage : IDisposable
    {
        Task Append(string json);
        Task Clear();
        Task<string> Get();
    }
}
