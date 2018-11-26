using System;
using System.Collections.Generic;

namespace SiWatchApp.Buffer
{
    public interface IBlock<T>
    {
        ICollection<T> Items { get; }
        void Discard();
        void Return();
        bool IsEmpty { get; }
    }
}
