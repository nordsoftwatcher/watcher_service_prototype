namespace SiWatchApp.Logging
{
    public static class LoggerFactory
    {
        public static LogLevel LogLevel { get; set; } = LogLevel.Debug;

        public static Logger GetLogger(string name)
        {
            return new Logger(name);
        }
    }
}
