using System;

namespace SiWatchApp.Utils
{
    public class PersistedQueue
    {
        private readonly string _storagePath;

        public PersistedQueue(string storagePath)
        {
            _storagePath = storagePath;
        }

        public void Put(object payload)
        {

        }

        public object Get()
        {

        }
    }
}
