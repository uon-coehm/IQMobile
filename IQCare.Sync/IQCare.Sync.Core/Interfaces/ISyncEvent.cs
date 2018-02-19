using System;

namespace IQCare.Sync.Core.Interfaces
{
    public interface ISyncEvent
    {
        DateTime DateOccurred { get; }
    }
}