using System;
using System.Collections.Generic;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Core.Interfaces
{
    public interface IConceptService 
    {
        IEnumerable<MConcept> GetAllByEncounterType(int encounterTypeId);
    }
}