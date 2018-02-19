namespace IQCare.Sync.Core.Interfaces
{
    public interface IHandle<T> where T : ISyncEvent
    {
        void Handle(T args);
        string GetNotification();
    }
}