using System;
using Tizen;

namespace SiWatchApp.Logging
{
    public class Logger
    {
        private readonly string _prefix;
        private const string TAG = "SiWatch";

        public Logger(String name = "")
        {
            _prefix = String.IsNullOrEmpty(name) ? "" : "["+name+"] ";
        }

        public void Debug(params Object[] args)
        {
            if (LoggerFactory.LogLevel > LogLevel.Debug)
                return;

            if(args == null || args.Length == 0)
                return;
            String message = _prefix + String.Join(' ', args);
            Log.Debug("SiWatch", message);
        }

        public void Info(params Object[] args)
        {
            if (LoggerFactory.LogLevel > LogLevel.Info)
                return;

            if (args == null || args.Length == 0)
                return;
            String message = _prefix + String.Join(' ', args);
            Log.Info(TAG, message);
        }

        public void Warn(params Object[] args)
        {
            if (LoggerFactory.LogLevel > LogLevel.Warn)
                return;

            if (args == null || args.Length == 0)
                return;
            String message = _prefix + String.Join(' ', args);
            Log.Warn(TAG, message);
        }

        public void Error(params Object[] args)
        {
            if (LoggerFactory.LogLevel > LogLevel.Error)
                return;

            if (args == null || args.Length == 0)
                return;
            String message = _prefix + String.Join(' ', args);
            Log.Error(TAG, message);
        }
    }
}
