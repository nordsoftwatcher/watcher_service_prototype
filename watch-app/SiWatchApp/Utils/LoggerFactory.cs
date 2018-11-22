using System;

namespace SiWatchApp.Utils
{
    public static class LoggerFactory
    {
        public static Logger GetLogger(String name)
        {
            return new Logger(name);
        }
    }
}
