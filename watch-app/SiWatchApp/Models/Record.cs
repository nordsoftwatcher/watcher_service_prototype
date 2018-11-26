using System;

namespace SiWatchApp.Models
{
    public class Record
    {
        public object Value { get; set; }
        public DateTime Timestamp { get; set; } = DateTime.Now;
    }
}
