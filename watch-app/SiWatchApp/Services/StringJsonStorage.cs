using System.Text;
using System.Threading.Tasks;

namespace SiWatchApp.Services
{
    public class StringJsonStorage : IJsonStorage
    {
        private readonly StringBuilder _storage = new StringBuilder();

        public Task Append(string json)
        {
            if (_storage.Length > 0) {
                _storage.Append(',');
            }
            _storage.Append(json);
            return Task.CompletedTask;
        }

        public Task Clear()
        {
            _storage.Clear();
            return Task.CompletedTask;
        }

        public Task<string> Get()
        {
            return Task.FromResult("[" + _storage + "]");
        }

        public void Dispose()
        {
        }
    }
}
