namespace SiWatchApp.Utils
{
    public interface IRecordQueue<T> where T : class
    {
        void Put(T data);
        T Get();
    }
}
