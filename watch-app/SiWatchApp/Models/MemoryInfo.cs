namespace SiWatchApp.Models
{
    public struct MemoryInfo
    {
        public int FreeSystemMemory { get; set; }
        public ulong? FreeStorageMemory { get; set; }

        public override string ToString()
        {
            return $"MemoryInfo{{FreeSystemMemory={FreeSystemMemory},FreeStorageMemory={FreeStorageMemory}}}";
        }
    }
}
