using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using IQCare.Sync.Core.Model;

namespace IQCare.Sync.Infrastructure
{
    public class SyncContext : DbContext
    {
        public SyncContext() : base("name=iqsyncdatabase")
        {
            Database.SetInitializer<SyncContext>(null);
        }

        public virtual DbSet<User> Users { get; set; }
        public virtual DbSet<Lookup> Lookups { get; set; }
        public virtual DbSet<Module> Modules { get; set; }
        public virtual DbSet<EncounterType> EncounterTypes { get; set; }
        public virtual DbSet<MConcept> Concepts { get; set; }
        public virtual DbSet<MDataTypeMap> DataTypeMaps { get; set; }

        public virtual DbSet<Patient> Patients { get; set; }

        public virtual DbSet<Encounter> Encounters { get; set; }
        public virtual DbSet<Observation> Observations { get; set; }

        public virtual DbSet<LookupHTS> LookupsHts { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Conventions.Remove<PluralizingTableNameConvention>();

            modelBuilder.Entity<MConcept>()
                .HasOptional(i => i.Parent)
                .WithMany(i => i.ChildrenConcepts)
                .HasForeignKey(i => i.ParentId);

            modelBuilder.Entity<Patient>()
                .HasOptional(i => i.Partner)
                .WithMany()
                .HasForeignKey(i => i.PartnerId);


        }
    }
}


