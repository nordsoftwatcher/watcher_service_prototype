using System.Threading.Tasks;
using SiWatchApp.Models;

namespace SiWatchApp.Services
{
    public interface ISyncProxy
    {
        Task<SyncPacket> Sync(SyncPacket packet);
    }
}
